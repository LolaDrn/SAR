import java.io.IOException;
import java.util.concurrent.locks.Lock;

public class ChannelImpl extends Channel{
	
	private boolean isDisconnected;
	private ChannelImpl match;
	private Object lock;
	private CircularBuffer buf;

	public ChannelImpl() {
		this.isDisconnected = false;
		this.buf=new CircularBuffer(100);
	}

	@Override
	public int read(byte[] bytes, int offset, int length) throws IOException {
		
        int i = 0;
        
        synchronized(lock) {

	        try {
	        	//bloquant tant qu'il n'y a rien a lire
	    		while (this.buf.empty()) {
	    			//si deconnexion d'un des cote on ferme tout
	    			if (this.match.disconnected() || this.isDisconnected) { 
	    				this.match.disconnect();
	    				this.disconnect();
	    				throw new IOException("Channels have been disconnected");
	    			}
					lock.wait();
	    		}	        	

	    		while (i < bytes.length && i < length && !this.buf.empty()) {
	    			//si deconnexion d'un des cote on ferme tout
	    			if (this.match.disconnected() || this.isDisconnected) { 
	    				this.match.disconnect();
	    				this.disconnect();
	    				throw new IOException("Channels have been disconnected");
	    			}
	            	bytes[offset + i] = buf.pull();
	            	i++;
	            }
	        	lock.notifyAll();
	            	
	        } 
	        catch (Exception e) {
	        	e.printStackTrace();
				System.out.println("Any bytes have been read");
				return 0;        }
        }
        return i;

  	}

	@Override
	public int write(byte[] bytes, int offset, int length) throws IOException {
		
		int i = 0;
        
        synchronized(lock) {
        	

				try {
					
				//On ne peut pas écrire tant que le buffer est plein --> bloquant
	    		while (this.buf.full()) {
	    			//si deconnexion d'un des cote on ferme tout
	    			if (this.match.disconnected() || this.isDisconnected) { 
	    				this.match.disconnect();
	    				this.disconnect();
	    				throw new IOException("Channels have been disconnected");
	    			}
						lock.wait();
					
	    		}
				while (i < bytes.length && i < length && !this.buf.full()) {

						//si deconnexion d'un des cote on ferme tout
						if (this.match.disconnected() || this.isDisconnected) { 
							this.match.disconnect();
							this.disconnect();
							throw new IOException("Channels have been disconnected");
						}
						//on ecrit dans le buffer du channel auquel on est connecte
						this.match.buf.push(bytes[offset +i]);
				   		i++;
	        	lock.notifyAll();
				}
				}
				catch (Exception e) {
					e.printStackTrace();
					System.out.println("Any bytes have been read");
					return 0;        
				}
	        }
        
		return i; 
	}
	
	@Override
	public void disconnect() {
		isDisconnected=true;	
		lock=null;
		match=null;
		buf=null;
	}

	@Override
	public boolean disconnected() {
		return isDisconnected;
    }
	

	public void setMatch(ChannelImpl match) {
		this.match = match;
	}
	

	public ChannelImpl getMatch() {
		return match;
	}

	public void setLock(Object lock) {
		this.lock = lock;
	}

}
