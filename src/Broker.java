import java.util.ArrayList;
import java.util.HashMap;

public abstract class Broker {
	//nom du Broker
	protected String name;
	//liste des ports sur lequels le broker ecoute
	protected ArrayList<Integer> listeningports= new ArrayList<Integer>();
	//hashmap des ports recevant une connexion d'un Broker
	protected HashMap<Integer, ChannelImpl> incoming= new HashMap<Integer, ChannelImpl>();
		
	
	public Broker(String name) throws Exception {
		//le nom du broker doit etre unique
		if (!BrokerManager.getInstance().getBufferMap().containsKey(name)){
			this.name=name;
			BrokerManager.getInstance().getBufferMap().put(name,this);
		}
		else {
			throw new Exception("The broker name has to be unique");
		}
	}

	public abstract Channel accept(int port) throws Exception;
	
	public abstract Channel connect(String name, int port) throws Exception;
}
