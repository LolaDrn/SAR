import java.util.List;
import java.util.Map;

public abstract class Broker {
	//Broker name
	protected String name;
	//list of the ports on which the broker is listening
	protected List<Integer> listeningports;
	//hashmap of the ports receiving a connection from a broker
	protected Map<Integer, ChannelImpl> incoming;
		
	
	public Broker(String name) throws Exception {
		synchronized(BrokerManager.getInstance()) {
			//the broker name must be unique 
			if (!BrokerManager.getInstance().getBufferMap().containsKey(name)){
				this.name=name;
				BrokerManager.getInstance().getBufferMap().put(name,this);
			}
			else {
				throw new Exception("The broker name has to be unique");
			}
		}
	}

	public abstract Channel accept(int port) throws Exception;
	
	public abstract Channel connect(String name, int port) throws Exception;
}
