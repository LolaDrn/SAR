import java.util.List;
import java.util.Map;

public abstract class Broker {
	//nom du Broker
	protected String name;
	//liste des ports sur lequels le broker ecoute
	protected List<Integer> listeningports;
	//hashmap des ports recevant une connexion d'un Broker
	protected Map<Integer, ChannelImpl> incoming;
		
	
	public Broker(String name) throws Exception {
		synchronized(BrokerManager.getInstance()) {
			//le nom du broker doit etre unique
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
