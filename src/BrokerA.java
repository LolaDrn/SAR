public class BrokerA extends Broker{

	public BrokerA(String name) throws Exception {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public synchronized Channel accept(int port) throws Exception { 
    	if (this.listeningports.contains(port)) {
    		throw new Exception("The port: "+port+" is already used");
    	}
    	
    	this.listeningports.add(port);
    	
    	//bloquant en attente d'une connection sur ce port
    	while (this.listeningports.contains(port)) {
		    	wait(300);
    	}
    	//recuperation du channel ajoute a la liste dans le connect
    	Channel connected= this.incoming.get(port);

    	synchronized (connected) { 
    		connected.notifyAll();
		}
    	
    	System.out.println("The broker " + this.name + " has accepted a connection on port " + port + ", can communicate with "+ this.incoming.get(port).getMatch());
    	return connected;
    }

	@Override
	public synchronized Channel connect(String name, int port) throws Exception { 
    	//tant qu'on ne trouve pas le broker demande on bloque
    	while (!BrokerManager.getInstance().getBufferMap().containsKey(name)) { 
	    	wait(300);
    	}
    	
    	Broker match=BrokerManager.getInstance().getBufferMap().get(name);
    	
    	//creation des nouveaux channels, un pour broker accept et un pour broker connect
    	ChannelA connectChannel=new ChannelA();
    	ChannelA acceptChannel=new ChannelA();
    	
    	connectChannel.setMatch(acceptChannel);
    	acceptChannel.setMatch(connectChannel);
    	
    	// Objet utilise comme verrou pour la synchronisation dans les methodes read et write
    	Object lock = new Object();
    	connectChannel.setLock(lock);
    	acceptChannel.setLock(lock);
    			
    	//ajoute le port et channel au hashmap et supprime le port d'ecoute de la liste du broker en accept
    	if (match.listeningports.contains(port)) {
    		match.incoming.put(port, acceptChannel);
    		match.listeningports.remove(match.listeningports.indexOf(port));
    	}
    	
    	//notification de l'autre broker 
    	synchronized (match) { 
    		match.notifyAll();
		}
		
		while (match.listeningports.contains(port)) {
			synchronized (acceptChannel) {
				acceptChannel.wait();
			}
		}
    	
    	//ajoute le port et le channel a la liste incoming du broker
    	this.incoming.put(port, connectChannel);

    	System.out.println("The broker " + this.name + " is connected on port " + port + " with "+match);
    	
		return connectChannel;
    }

}