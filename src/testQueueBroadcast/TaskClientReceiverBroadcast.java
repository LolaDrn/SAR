package testQueueBroadcast;

import java.util.Arrays;

import channel.Task;
import queue.MessageQueue;
import queue.QueueBrokerImpl;

public class TaskClientReceiverBroadcast extends Task {
	
	private QueueBrokerImpl broker;
	private int connectionPort;
	private String queueBroker2connect;

	public TaskClientReceiverBroadcast(String queueBrokerName, int port, String queueBroker2connectName) throws Exception {
		super();
		this.broker = new QueueBrokerImpl(queueBrokerName);	
		this.connectionPort=port;
		this.queueBroker2connect=queueBroker2connectName;
	}

	@Override
	public void run() {
		while (true) {
			MessageQueue msgQueue=null;
			
			//waits until the messageQueue is created by the corresponding sender client
			synchronized(TaskClientSenderBroadcast.msgQueuesMap) {
				while(!TaskClientSenderBroadcast.msgQueuesMap.containsKey(broker.getBroker().getName()+" sender")) {
					try {
						TaskClientSenderBroadcast.msgQueuesMap.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				msgQueue = TaskClientSenderBroadcast.msgQueuesMap.get(broker.getBroker().getName()+" sender");
			}
			
			//receives messages
			byte[] msgReceived = msgQueue.receive();
			System.out.println("message received by "+broker.getBroker().getName());
		}
		
		
	}

}
