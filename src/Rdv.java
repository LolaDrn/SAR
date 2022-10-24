/***
 * The class manages the creation of the channels
 * @author lola
 *
 */
public class Rdv {
	private Broker ab;
	private Broker cb;
	private Channel ch;
	String name;
	int port;
	
	/***
	 * Constructor of the class.
	 * @param name (string) - the name of the broker doing the accept
	 * @param port (int) - the port on which the connection have to be done
	 */
	public Rdv(String name, int port) {
		super();
		this.name = name;
		this.port = port;
	}
	
	/***
	 * The method blocs until an accept arrived
	 * Two channels are then created, one for broker b, one for the accept broker, and associated to their respective brokers.
	 * Notify the accept that the channels are created. 
	 * @param b (Broker) - the broker which do the connect
	 * @return the created channel associated to the broker b
	 * @throws InterruptedException
	 */
	public synchronized Channel connect(Broker b) throws InterruptedException {
		ChannelImpl connectChannel = null;
		cb=b;
		while (ab==null) {
			wait();
		}
		
		//creation of the new channels, on for broker accept and one for broker connect
    	connectChannel=new ChannelImpl();
    	ChannelImpl acceptChannel=new ChannelImpl();
    	
    	connectChannel.setMatch(acceptChannel);
    	acceptChannel.setMatch(connectChannel);
    	
    	//object used as lock for synchronization in write and read methods
    	Object lock = new Object();
    	connectChannel.setLock(lock);
    	acceptChannel.setLock(lock);
    	
    	//the channel of the class rdv corresponds to the broker accept channel
    	this.setCh(acceptChannel);
		
		//notify that the channel is created
		notifyAll();
		return connectChannel;
		
		
	}
	
	/***
	 * Notify connect that their is an accept.
	 * The method blocs until the channels are created
	 * @param b (Broker) - the broker which do the connect
	 * @return the created channel associated to the broker b
	 * @throws InterruptedException
	 */
	public synchronized Channel accept(Broker b) throws InterruptedException {
		ab=b;
		//notify that ab is not null
		notifyAll();
		//while the channel isn't created by the connect method, the method is blocking
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
