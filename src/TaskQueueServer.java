
public class TaskQueueServer extends Task{

	private QueueBrokerImpl broker;

	public TaskQueueServer(String queueBrokerName) throws Exception {
		this.broker= new QueueBrokerImpl(queueBrokerName);

	}

	public void run() {
		MessageQueue msgQueue;
		try {
			msgQueue=broker.accept(666);

			byte[] test =("Hello World").getBytes();
			//envoie d'un message
			msgQueue.send(test, 0, test.length);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
