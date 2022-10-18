import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BrokerManager {
	private HashMap<String, Broker> brokerMap;
	private List<Rdv> rdv;
	private Lock lockRdv = new ReentrantLock();

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
		
		synchronized (lockRdv) {
			lockRdv.lock();
			//recherche dans la liste des rdv si un rdv correspondant au nom et port existe
			for (int i=0; i<rdv.size();i++) {
				if (rdv.get(i).name.equals(name) && rdv.get(i).port==port) {
					connectRdv=rdv.get(i);
				}
			}
			
			//si le rdv n'existe pas on le cree, on lui ajoute le broker et on l'ajoute à la liste des rdv
			if (connectRdv==null) {
				connectRdv = new Rdv(name, port);
				rdv.add(connectRdv);
			}
			
			//rdv deja existant avec un accept
			else if (connectRdv.getAb()!=null) {
					rdv.remove(connectRdv);
			}
			
			lockRdv.unlock();
			//rdv deja existant avec un connect --> les connects qui arrivent attendent qu'un nouveau rdv existe
			if (connectRdv.getCb()!=null) {
				synchronized(this) {
					wait();
				}
			}
		}
		connectChannel = connectRdv.connect(b);
		return connectChannel;
		
	}
	
	public Channel accept(Broker b, String name, int port) throws InterruptedException {
		Channel acceptChannel;
		Rdv acceptRdv=null;
		synchronized (lockRdv) {
			lockRdv.lock();
			//recherche dans la liste des rdv si un rdv correspondant au nom et port existe
			for (int i=0; i<rdv.size();i++) {
				if (rdv.get(i).name==name && rdv.get(i).port==port) {
					acceptRdv=rdv.get(i);
				}
			}
			
			//si le rdv n'existe pas on le cree, on lui ajoute le broker et on l'ajoute à la liste des rdv
			if (acceptRdv==null) {
				acceptRdv = new Rdv(name, port);
				rdv.add(acceptRdv);
			}
			
			//rdv deja existant avec un connect
			else if (acceptRdv.getCb()!=null) {
				rdv.remove(acceptRdv);
			}
			lockRdv.unlock();

		}
		acceptChannel = acceptRdv.accept(b);
		synchronized(this) {
			notifyAll();
		}
		return acceptChannel;
	}
	
	
}
