package testChannel;

import channel.BrokerImpl;
import channel.Channel;
import channel.Task;

/***
 * This task plays the role of the server. 
 * It accepts a connection on a given port.
 * Once the the connection is established, it reads the message sent by the other broker and writes the same message as the answer.
 * Then it disconnects.
 * These three steps are done in a loop.
 * @author lola
 *
 */
public class TaskServer extends Task{
	
	private BrokerImpl broker;
	private int connectionPort;
	

	public TaskServer(String brokerName, int port) throws Exception {
		this.broker= new BrokerImpl(brokerName);
		this.connectionPort= port;
	}
	
	public void run() {
		
		Channel channel;
		while (true) {
			try {
				channel=broker.accept(connectionPort);
				
				// We read the message sent by the client in the buffer
				byte[] msgRead = new byte[1024];
				int bytesRead = channel.read(msgRead, 0, 1024); 
				
				//Send message via channel
				int bytesWritten = channel.write(msgRead, 0, 1024);
	
				Thread.sleep(50);
				channel.disconnect();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
}
