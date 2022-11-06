package testQueueBroadcast;

import java.util.ArrayList;

import channel.Task;
import queue.MessageQueue;
import queue.QueueBrokerImpl;

public class TaskServerBroadcast extends Task{
	
	private int connectionPort;
	private QueueBrokerImpl broker;
	private int nbClient;
	private ArrayList<MessageQueue> messageQueuesList;

	
	public TaskServerBroadcast(int connectionPort, String queueBrokerName, int nbClient) throws Exception {
		super();
		this.connectionPort = connectionPort;
		this.broker = new QueueBrokerImpl(queueBrokerName);
		this.nbClient = nbClient;
		this.messageQueuesList=new ArrayList<MessageQueue>();
	}


	@Override
	public void run() {
		while (true) {
			try {
				//accepts connection for each client
				for (int i=0; i<nbClient;i++) {
						messageQueuesList.add(broker.accept(connectionPort));
				}
				//receives messages from each clients and sends them to each other clients
				for (int i=0; i<nbClient; i++) {
					byte[] message = messageQueuesList.get(i).receive();
					System.out.println("Server received a message");
					for (int j=0; j<nbClient; j++) {
						if (i!=j) {
							messageQueuesList.get(j).send(message, 0, message.length);
						}
					}
				}
				//remove the messageQueue from the list
				for(int i=0; i<nbClient; i++) {
					messageQueuesList.remove(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			
			}
		}
	}

}
