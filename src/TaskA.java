
public class TaskA extends Task{
	
	protected BrokerA broker;

	public TaskA(String brokerName) throws Exception {
		this.broker= new BrokerA(brokerName);

	}

	public void run() {
		Channel channel;
		try {
			channel=broker.accept(666);

			byte[] test =("Hello World").getBytes();
			//ecriture d'un message dans le buffer
			channel.write(test, 0, test.length);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
