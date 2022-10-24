import java.io.IOException;
import java.util.concurrent.locks.Lock;

/***
 * The Channel class corresponds to a means of communication between 2 tasks. 
 * A channel is a stream of lossless FIFO bytes.
 * The class can be shared by threads but it is up to the user to handle concurrency control
 * @author lola
 *
 */
public class ChannelImpl extends Channel{
	
	private boolean isDisconnected;
	private ChannelImpl match;
	private Object lock;
	private CircularBuffer buf;

	/***
	 * Constructor of the class. Is called in BrokerImpl.
	 */
	public ChannelImpl() {
		this.isDisconnected = false;
		this.buf=new CircularBuffer(64);
	}

	/***
	 * The Read method blocs as long as it has nothing to read. 
	 * When there is something to read, it reads a maximum of « length » bytes from the input stream and put them into the given byte array « bytes ». 
	 * The first byte read is stored in « bytes[offset] », the second in « bytes[offset+1 », ect… until « bytes[offset+lenght-1] ».
	 * If the disconnection of one of the brokers is detected,  it throws an exception and stops what it was doing. 
	 * Writing then closing does not therefore guarantee that the data will be read.
	 * @param bytes (byte[]) - the buffer in which the data is read
	 * @param offset (int) - the start offset in array bytes
	 * @param length (int) - the maximum number of bytes to read
	 * @throws IOException
	 * @return the total number of bytes read in the buffer (Included between 0 and length) (int)
	 */
	@Override
	public int read(byte[] bytes, int offset, int length) throws IOException {
		
        int i = 0;
        
        synchronized(lock) {
    		while (i < bytes.length && i < length ) {

		        try {
		        	//if one of the sides is disconnected, everything is closed
		        	if (this.match.disconnected() || this.isDisconnected) { 
	    				this.match.disconnect();
	    				this.disconnect();
	    				throw new IOException("Channels have been disconnected");
	    			}
	    			
		        	// blocking while there is nothing to read
		    		if (this.buf.empty()) {
						lock.wait();
		    		}	        	
	
		    		else {	
		            	bytes[offset + i] = buf.pull();
		            	i++;
				   		lock.notifyAll();
		    		}
		        } 
		        catch (Exception e) {
		        	e.printStackTrace();
					System.out.println("An error occured, not all bytes have been read");
					return i;        }
    		}
        }
        return i;

  	}
	
	/***
	 * The write method blocs as long as the buffer is full. 
	 * Writes a part from the given byte array. 
	 * This part corresponds to the bytes from the indice « offset » to the indice « offset »+« length ».
	 * If the disconnection of one of the brokers is detected, it throws an exception and stops what it was doing. 
	 * Writing then closing does not therefore guarantee that the data will be read.
	 * @param bytes (byte[]) - the buffer containing the data to write
	 * @param offset (int) - the start offset in array bytes
	 * @param length (int) - the maximum number of bytes to read
	 * @throws IOException
	 * @return the total number of bytes write in the buffer (int)
	 */
	@Override
	public int write(byte[] bytes, int offset, int length) throws IOException {
		
		int i = 0;
        
        synchronized(lock) {
        	
			while (i < bytes.length && i < length) {

				try {
					//if one of the sides is disconnected, everything is closed
	    			if (this.match.disconnected() || this.isDisconnected) { 
	    				this.match.disconnect();
	    				this.disconnect();
	    				throw new IOException("Channels have been disconnected");
	    			}
	    			
	    			//We cannot write while the buffer is full --> blocking
		    		if (match.buf.full()) {
							lock.wait();
						
		    		}
		    		else {
		    			//we write in the buffer of the channel to which we are connected
						this.match.buf.push(bytes[offset +i]);
				   		i++;
				   		lock.notifyAll();
		    		}

				}
				catch (Exception e) {
					e.printStackTrace();
					System.out.println("An error occured, not all bytes have been read");
					return i;        
				}
			}
	    }
        
		return i; 
	}
	
	/***
	 * Disconnects the two brokers involved in the channel so closes the channel.
	 */
	@Override
	public void disconnect() {
		isDisconnected=true;	
		lock=null;
		match=null;
		buf=null;
	}

	/***
	 * Enables to know if the channel is connected or not. 
	 * If the channel is deconnected return true.
	 * @return A boolean indicating if the channel is disconnected (boolean)
	 */
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
