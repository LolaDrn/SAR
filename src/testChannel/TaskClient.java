package testChannel;
import java.util.Arrays;
import java.util.Random;

import channel.BrokerImpl;
import channel.Channel;
import channel.Task;

/***
 * This task plays the role of the client. 
 * It wants to connect to a given broker name on a given port.
 * Once the the connection is established, it writes a message to the other broker and receives the answer.
 * Then it disconnects.
 * These three steps are done in a loop.
 * @author lola
 *
 */
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
				Thread.sleep(50);

				channel.disconnect();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
