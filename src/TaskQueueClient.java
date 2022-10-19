
public class TaskQueueClient  extends Task{
	
	private QueueBrokerImpl broker;

	
	public TaskQueueClient(String queueBrokerName) throws Exception {
		this.broker= new QueueBrokerImpl(queueBrokerName);
	}
	
	public void run() {
		MessageQueue msgQueue;
		try {
			//nouvelle connexion a A sur le port 666
			msgQueue = broker.connect("A", 666); 		
			
			// Reception du message
			byte[] message = msgQueue.receive(); 
			
			System.out.print("Message : ");
			for (int i = 0; i < message.length; i++) {
				System.out.print((char)message[i]);
			}
			
			msgQueue.closed();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
