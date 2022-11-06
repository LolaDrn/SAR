package testQueueBroadcast;

import java.util.HashMap;
import java.util.Random;

import channel.Task;
import queue.MessageQueue;
import queue.MessageQueueImpl;
import queue.QueueBrokerImpl;

public class TaskClientSenderBroadcast extends Task{
	
	public static HashMap<String, MessageQueue> msgQueuesMap = new HashMap<>();

	private QueueBrokerImpl broker; 
	private String queueBroker2connect;
	private int connectionPort;
	
	public TaskClientSenderBroadcast(String queueBrokerName, int port, String queueBroker2connectName) throws Exception {
		super();
		this.broker = new QueueBrokerImpl(queueBrokerName+" sender");	
		this.connectionPort=port;
		this.queueBroker2connect=queueBroker2connectName;
	}

	@Override
	public void run() {
		while(true) {
			//connection with the server
			MessageQueue msgQueue=null;
			msgQueue=broker.connect(queueBroker2connect, connectionPort);
			
			synchronized(msgQueuesMap) {
				if(!msgQueuesMap.containsKey(broker.getBroker().getName())) {
					msgQueuesMap.put(broker.getBroker().getName(), msgQueue);
					msgQueuesMap.notifyAll();
				}
			}
			try {
				//send a message to the server
				byte[] msgToSend = new byte[1024];
				Random r= new Random();
				r.nextBytes(msgToSend);
				msgQueue.send(msgToSend, 0, msgToSend.length);
				System.out.println("message send by "+broker.getBroker().getName());
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			msgQueue.close();
			synchronized(msgQueuesMap) {
				msgQueuesMap.remove(broker.getBroker().getName());
			}
		}

	}

}
