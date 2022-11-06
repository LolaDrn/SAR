package channel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/***
 * Manage channels, 1 task creates 1 broker with 1 name
 * The class can be shared by threads but it is up to the user to handle concurrency control
 * @author lola
 *
 */
public class BrokerImpl extends Broker{

	/***
	 * Constructor of the class. Uses a string representing the name of the object to create it. 
	 * @param name (string) – the name of the broker
	 * @throws Exception
	 */
	public BrokerImpl(String name) throws Exception {
		super(name);
		listeningports= new ArrayList<Integer>();
		incoming = new HashMap<Integer, ChannelImpl>();
	}

	/***
	 * The broker waits for a connection on the specified port
	 * Blocking while waiting for an appointment to be accepted, the shared channel is then created.
	 * @param port (int) – the port on which the broker waits for the connection
	 * @return the channel associated to the broker which do the accept
	 * @throws Exception
	 */
	@Override
	public Channel accept(int port) throws Exception {
		Channel c = null;
		try {
			c=BrokerManager.getInstance().accept(this, this.name, port);
		}catch (Exception e) {
			e.printStackTrace();
		}
    	System.out.println("The broker " + this.name + " has accepted a connection on port " + port );
		return c;
	}

	/***
	 * Connection to another Broker whose name is given and on the specified port.
	 * Blocking while waiting for an appointment to be accepted, the shared channel is then created.
	 * @param name (string) - the name of the broker to connect
	 * @param port (int) - the port on which the broker waits for the connection
	 * @return the channel associated to the broker which do the connect
	 * @throws Exception
	 */
	@Override
	public Channel connect(String name, int port) throws Exception {
		Channel c = null;
		try {
			c=BrokerManager.getInstance().connect(this, name, port);
		}catch (Exception e) {
			e.printStackTrace();
		}
    	System.out.println("The broker " + this.name + " is connected on port " + port );
		return c;
	}

}