
public class Rdv {
	private Broker ab;
	private Broker cb;
	private Channel ch;
	String name;
	int port;
	
	public Rdv(String name, int port) {
		super();
		this.name = name;
		this.port = port;
	}
	
	public synchronized Channel connect(Broker b) throws InterruptedException {
		ChannelImpl connectChannel = null;
		cb=b;
		while (ab==null) {
			wait();
		}
		
		//creation des nouveaux channels, un pour broker accept et un pour broker connect
    	connectChannel=new ChannelImpl();
    	ChannelImpl acceptChannel=new ChannelImpl();
    	
    	connectChannel.setMatch(acceptChannel);
    	acceptChannel.setMatch(connectChannel);
    	
    	// Objet utilise comme verrou pour la synchronisation dans les methodes read et write
    	Object lock = new Object();
    	connectChannel.setLock(lock);
    	acceptChannel.setLock(lock);
    	
    	//le channel du rdv est celui du broker accept
    	this.setCh(acceptChannel);
		
		//notifie que le channel est cree
		notifyAll();
		return connectChannel;
		
		
	}
	
	public synchronized Channel accept(Broker b) throws InterruptedException {
		ab=b;
		//notifie que ab n'est pas null
		notifyAll();
		//tant que le channel n'est pas cree par connect
		while (ch==null) {
			wait();
		}
		return this.ch;
			
		}
	
	
	public Broker getAb() {
		return ab;
	}

	public void setAb(Broker ab) {
		this.ab = ab;
	}

	public Broker getCb() {
		return cb;
	}

	public void setCb(Broker cb) {
		this.cb = cb;
	}

	public Channel getCh() {
		return ch;
	}

	public void setCh(Channel ch) {
		this.ch = ch;
	}

	
	

}
