
public class ServerTask extends Task{
	
	private BrokerImpl broker;
	private int connectionPort;
	

	public ServerTask(String brokerName, int port) throws Exception {
		this.broker= new BrokerImpl(brokerName);
		this.connectionPort= port;
	}

	public void run() {
		
		Channel channel;
		while (true) {
			try {
				channel=broker.accept(connectionPort);
				
				// On lit le message envoye par le client dans le buffer 
				byte[] msgRead = new byte[1024];
	
				int bytesRead = channel.read(msgRead, 0, 1024); 
				
				//Envoie du message via le channel
				int bytesWritten = channel.write(msgRead, 0, 1024);
	
				Thread.sleep(50);
				channel.disconnect();
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
}
