
public class TaskQueueServer extends Task{

	private QueueBrokerImpl broker;

	public TaskQueueServer(String queueBrokerName) throws Exception {
		this.broker= new QueueBrokerImpl(queueBrokerName);

	}

	public void run() {
		MessageQueue msgQueue;
		while (true) {
			try {
				msgQueue=broker.accept(666);
				
				// We read the message sent by the client in the buffer
				byte[] msgRead = msgQueue.receive(); 
				
				//Send message via channel
				msgQueue.send(msgRead, 0, 1024);
	
				Thread.sleep(50);
				msgQueue.close();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
//		try {
//			msgQueue=broker.accept(666);
//
//			byte[] test =("Hello World").getBytes();
//			//envoie d'un message
//			msgQueue.send(test, 0, test.length);
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
