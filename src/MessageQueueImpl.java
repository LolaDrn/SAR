import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MessageQueueImpl extends MessageQueue{
	private Channel channel;
	private Lock sendLock= new ReentrantLock() ;
	private Lock receiveLock=new ReentrantLock();
	private boolean isClosed=true;

	public MessageQueueImpl(Channel channel) {
		this.channel=channel;
		this.isClosed=false;
	}
	
	@Override
	public void send(byte[] bytes, int offset, int length) {
		//bloque la ressoucre afin d'envoyer tout le message 
		this.sendLock.lock();
		//tableau de byte corrzspondant a la taille du message a envoyer
		byte[] messageSize= ByteBuffer.allocate(Integer.BYTES).putInt(length).array();
		int nbBytes =0;
		try {
			//on ecrit d'abord la taille du message
			nbBytes=this.channel.write(messageSize, 0, Integer.BYTES);
			//ecriture du message
			nbBytes+=this.channel.write(bytes, offset, length);
			if(nbBytes != length+Integer.BYTES) {
				throw new Exception ("There is an error when sending the message. Not all bytes were sent.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("The message wasn't sent, try again");
		}
		//debloque la ressource
		this.sendLock.unlock();
	}

	@Override
	public byte[] receive() {
		//bloque la ressoucre afin d'envoyer tout le message 
		this.receiveLock.lock();
		byte[] messageSizeBytes =new byte[100];
		byte[] message = null;
		int messageSizeInt=0;
		int nbBytes=0;
		try {
			//on recupere la taille du message (4 premiers octets=longeur du message)
			nbBytes=this.channel.read(messageSizeBytes, 0, 4);
			//on convertie la taille du message en int
			messageSizeInt=Integer.valueOf(new String(messageSizeBytes));
			//lecture du message
			nbBytes+=this.channel.read(message, 0, messageSizeInt);
			if(nbBytes != messageSizeInt+4) {
				throw new Exception ("An error occur when reading the message. Not all bytes were read.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("The message can't be read, try again");
		}
		//debloque la ressource
		this.sendLock.unlock();
		return message;	
	}

	@Override
	public void close() {
		this.channel.disconnect();
		this.isClosed=true;
		this.channel=null;
		this.receiveLock=null;
		this.sendLock=null;
	}

	@Override
	public boolean closed() {
		return isClosed;
	}

}
