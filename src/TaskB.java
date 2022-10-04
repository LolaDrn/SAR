
public class TaskB extends Task{
	
	protected BrokerA broker;

	
	public TaskB(String brokerName) throws Exception {
		this.broker= new BrokerA(brokerName);
	}
	
	public void run() {
		Channel channel;
		try {
			//nouvelle connexion a A sur le port 666
			channel = broker.connect("A", 666); 		

			byte[] bytes = new byte[1000];
			
			// On lit le message dans le buffer 
			int bytesRead = channel.read(bytes, 0, 20); 
			
			System.out.print("Message : ");
			for (int i = 0; i < bytesRead; i++) {
				System.out.print((char)bytes[i]);
			}
			
			channel.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
