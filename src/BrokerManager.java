import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BrokerManager {
	private HashMap<String, Broker> brokerMap;
	private List<Rdv> rdv;

	private static BrokerManager brokerManager;

	public synchronized static BrokerManager getInstance() {
		if (brokerManager == null)
			brokerManager = new BrokerManager();
		return brokerManager;
	}
	
	private BrokerManager() {
		this.brokerMap= new HashMap<String, Broker>();
		this.rdv= new ArrayList<>();
	}

	public HashMap<String, Broker> getBufferMap() {
		return brokerMap;
	}
	
	public  Channel connect(Broker b, String name, int port) throws InterruptedException {
		Channel connectChannel = null;
		Rdv connectRdv=null;
		
		//recherche dans la liste des rdv si un rdv correspondant au nom et port existe
		for (int i=0; i<rdv.size();i++) {
			if (rdv.get(i).name==name && rdv.get(i).port==port) {
				connectRdv=rdv.get(i);
			}
		}
		
		//si le rdv n'existe pas on le cree, on lui ajoute le broker et on l'ajoute à la liste des rdv
		if (connectRdv==null) {
			connectRdv = new Rdv(name, port);
			connectRdv.setCb(b);
			rdv.add(connectRdv);
			connectChannel = connectRdv.connect();

		}
		
		//rdv deja existant avec un accept
		else if (connectRdv.getAb()!=null) {
			connectRdv.setCb(b);
			connectChannel = connectRdv.connect();

		}
		
		//rdv deja existant avec un connect --> les connects qui arrivent attendent qu'un nouveau rdv existe
		else if (connectRdv.getCb()!=null) {
			synchronized(this) {
				wait();
			}
		}
		
		return connectChannel;
		
	}
	
	public Channel accept(Broker b, String name, int port) throws InterruptedException {
		Channel acceptChannel;
		Rdv acceptRdv=null;
		
		//recherche dans la liste des rdv si un rdv correspondant au nom et port existe
		for (int i=0; i<rdv.size();i++) {
			if (rdv.get(i).name==name && rdv.get(i).port==port) {
				acceptRdv=rdv.get(i);
			}
		}
		
		//si le rdv n'existe pas on le cree, on lui ajoute le broker et on l'ajoute à la liste des rdv
		if (acceptRdv==null) {
			acceptRdv = new Rdv(name, port);
			acceptRdv.setAb(b);
			rdv.add(acceptRdv);
		}
		
		//rdv deja existant avec un connect
		else if (acceptRdv.getCb()!=null) {
			acceptRdv.setAb(b);
		}
		
		acceptChannel = acceptRdv.accept();
		synchronized(this) {
			notifyAll();
		}
		return acceptChannel;
	}
	
	
}
