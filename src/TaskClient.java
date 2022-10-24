import java.util.Arrays;
import java.util.Random;

public class TaskClient extends Task{
	
	private BrokerImpl broker;
	private String broker2connect;
	private int connectionPort;

	
	public TaskClient(String brokerName,String name, int port) throws Exception {
		this.broker= new BrokerImpl(brokerName);
		this.broker2connect=name;
		this.connectionPort=port;
	}
	
	public void run() {
		
		Channel channel;
		
		while (true) {
			try {
				//New connection to A on port 666
				channel = broker.connect(broker2connect, connectionPort); 		
				
				//We send a message to the server
				byte[] msgToSend = new byte[1024];
				Random r= new Random();
				r.nextBytes(msgToSend);
				channel.write(msgToSend, 0, msgToSend.length);
				
				//Receive the response
				byte[] msgReceived = new byte[1024];
				int bytesRead = channel.read(msgReceived, 0, 1024);
				
				if (!Arrays.equals(msgToSend, msgReceived))	{
					throw new Exception("Message send and received aren't the same. Message send : "+msgToSend+" .Message received : "+msgReceived );
				}
				channel.disconnect();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
