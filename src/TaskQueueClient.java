import java.util.Arrays;
import java.util.Random;

public class TaskQueueClient  extends Task{
	
	private QueueBrokerImpl broker;

	
	public TaskQueueClient(String queueBrokerName) throws Exception {
		this.broker= new QueueBrokerImpl(queueBrokerName);
	}
	
	public void run() {
		MessageQueue msgQueue;
		
		while (true) {
			try {
				//New connection to A on port 666
				msgQueue = broker.connect("A", 666); 		
				
				//We send a message to the server
				byte[] msgToSend = new byte[1024];
				Random r= new Random();
				r.nextBytes(msgToSend);
				msgQueue.send(msgToSend, 0, msgToSend.length);
				
				//Receive the response
				byte[] msgReceived = msgQueue.receive();
				
				if (!Arrays.equals(msgToSend, msgReceived))	{
					throw new Exception("Message send and received aren't the same. Message send : "+msgToSend+" .Message received : "+msgReceived );
				}
				Thread.sleep(50);

				msgQueue.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
//		try {
//			//nouvelle connexion a A sur le port 666
//			msgQueue = broker.connect("A", 666); 		
//			
//			// Reception du message
//			byte[] message = msgQueue.receive(); 
//			
//			System.out.print("Message : ");
//			for (int i = 0; i < message.length; i++) {
//				System.out.print((char)message[i]);
//			}
//			
//			msgQueue.closed();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
	}

}
