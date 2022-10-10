import java.nio.ByteBuffer;

public class TaskB extends Task{
	
	private BrokerImpl broker;
	private int connexionPort;

	
	public TaskB(String brokerName, int port) throws Exception {
		this.broker= new BrokerImpl(brokerName);
		this.connexionPort=port;
	}
	
	public void run() {
		/*
		Channel channel;
		int count=0;

		try {
			//nouvelle connexion a A sur le port 666
			channel = broker.connect("A", connexionPort); 		

			byte[] bytes = new byte[64];
			
			//on envoie un message au server
			byte[] msgWrite =ByteBuffer.allocate(1024).putInt(count).array();
			//ecriture d'un message dans le buffer
			channel.write(msgWrite, 0, msgWrite.length);
			
			// On lit le message dans le buffer 
			int bytesRead = channel.read(bytes, 0, 20); 
			
			System.out.print("Message received by client: ");
			for (int i = 0; i < bytesRead; i++) {
				System.out.print((char)bytes[i]);
			}
			
			channel.disconnect();
			count=(count+1)%255;
			
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		Channel channel;
		try {
			//nouvelle connexion a A sur le port 666
			channel = broker.connect("A", 666); 			
			byte[] bytes = new byte[1000];
			
			// On lit le message dans le buffer (on précise la taille du message à lire pour le moment)
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
