package channel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/***
 * The BrokerManager class manages the Brokers and creates the rdv for 2 brokers
 * @author lola
 *
 */
public class BrokerManager {
	private HashMap<String, Broker> brokerMap;
	private List<Rdv> rdv;

	private static BrokerManager brokerManager;

	/***
	 * Enables to access the brokerManager instance
	 * @return the instance of brokerManager
	 */
	public synchronized static BrokerManager getInstance() {
		if (brokerManager == null)
			brokerManager = new BrokerManager();
		return brokerManager;
	}
	
	/***
	 * Constructor of the class.
	 * Private to restrict the instantiation of the class to a single object.
	 */
	private BrokerManager() {
		this.brokerMap= new HashMap<String, Broker>();
		this.rdv= new ArrayList<>();
	}

	public HashMap<String, Broker> getBufferMap() {
		return brokerMap;
	}
	
	/***
	 * If there is no rdv for the specified name and port the function will create it and add it to its rdv list.
	 * If there is already a rdv with a connect in the rdv list, the method blocs until this rdv is over.
	 * Call the connect function of the rdv class to get the channel associated to the broker b.
	 * Remove the rdv after the channel is created.
	 * @param b (Broker) - the broker which do the connect
	 * @param name (String) - the name of the broker to connect
	 * @param port (int) - the port on which the broker do the connection
	 * @return the channel associated to the broker b
	 * @throws InterruptedException
	 */
	public  Channel connect(Broker b, String name, int port) throws InterruptedException {
		Channel connectChannel = null;
		Rdv connectRdv=null;
		
		synchronized (rdv) {
			//search in the list rdv if an appointment corresponding to the name and port exists
			for (int i=0; i<rdv.size();i++) {
				if (rdv.get(i).name.equals(name) && rdv.get(i).port==port) {
					connectRdv=rdv.get(i);
				}
			}
		}
			
		// already existing appointment with a connect --> incoming connects wait for a new appointment to exist
		if (connectRdv!=null && connectRdv.getCb()!=null) {
			connectRdv=null;
			synchronized(this) {
				wait();
			}
		}
		
		synchronized (rdv) {
			//if the appointment does not exist, we create it, we add the broker to it and we add it to the list of appointments
			if (connectRdv==null) {
				connectRdv = new Rdv(name, port);
				rdv.add(connectRdv);
			}
		}
		
		connectChannel = connectRdv.connect(b);

		//once the connection is established, remove the appointment from the list
		synchronized (rdv) {
			rdv.remove(connectRdv);
		}	
		
		return connectChannel;
		
	}
	
	/***
	 * If there is no rdv for the specified name and port the function will create it and add it to its rdv list.
	 * Call the accept function of the rdv class to get the channel associated to the broker b.
	 * Remove the rdv after the channel is created and notify others threads that the accept is finished.
	 * @param b(Broker) - the broker which do the accept
	 * @param name (String) - the name of the broker b
	 * @param port (int) - the port on which the broker waits for the connection
	 * @return the channel associated to the broker b
	 * @throws InterruptedException
	 */
	public Channel accept(Broker b, String name, int port) throws InterruptedException {
		Channel acceptChannel;
		Rdv acceptRdv=null;
		
		synchronized (rdv) {
			//search in the list rdv if an appointment corresponding to the name and port exists
			for (int i=0; i<rdv.size();i++) {
				if (rdv.get(i).name==name && rdv.get(i).port==port) {
					acceptRdv=rdv.get(i);
				}
			}
			
			//if the appointment does not exist, we create it, we add the broker to it and we add it to the list of appointments
			if (acceptRdv==null) {
				acceptRdv = new Rdv(name, port);
				rdv.add(acceptRdv);
			}
		}
		
		acceptChannel = acceptRdv.accept(b);
		synchronized (rdv) {
			rdv.remove(acceptRdv);
		}
		
		synchronized(this) {
			notifyAll();
		}
		return acceptChannel;
	}
	
	
}
