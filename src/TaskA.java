
public class TaskA extends Task{
	
	private BrokerImpl broker;
	

	public TaskA(String brokerName) throws Exception {
		this.broker= new BrokerImpl(brokerName);

	}

	public void run() {
		/*
		Channel channel;
		int connexionPort=0;
		try {
			while (true) {
				
				channel=broker.accept(connexionPort);
				
				// On lit le message envoye par le client dans le buffer 
				byte[] msgRead = new byte[64];
	
				int bytesRead = channel.read(msgRead, 0, 20); 
				
				System.out.print("Message received by server: ");
				for (int i = 0; i < bytesRead; i++) {
					System.out.print((char)msgRead[i]);
				}
	
				//ecriture d'un message dans le buffer
	
				byte[] test =("Hello World").getBytes();
				channel.write(test, 0, test.length);
				connexionPort++;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
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
