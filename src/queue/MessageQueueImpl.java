package queue;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import channel.Channel;

/***
 * Allows the exchange of messages, therefore the exchange of packet flows. A message is a variable-sized payload of bytes.
 * Uses channels to write and read a given stream of bytes in order to send and receive complete messages.
 * The form of the message includes a header containing the length of the message on 4 bytes then the content corresponding to the message.
 * @author lola
 *
 */
public class MessageQueueImpl extends MessageQueue{
	
	private Channel channel;
	private Lock sendLock= new ReentrantLock() ;
	private Lock receiveLock=new ReentrantLock();
	private boolean isClosed=true;

	/***
	 * Constructor of the class. Is called in QueueBrokerImpl.
	 * @param channel
	 */
	public MessageQueueImpl(Channel channel) {
		this.channel=channel;
		this.isClosed=false;
	}
	
	/***
	 * Sends the part of bytes between offset and offset+length. Sends the message as a whole.
	 * Blocking while waiting for all bytes to be sent.
	 * @param bytes (byte[]) - the buffer containing the bytes to write
	 * @param offset (int) - the start offset in array bytes
	 * @param length (int) - the number of bytes to read
	 */	 
	@Override
	public void send(byte[] bytes, int offset, int length) {
		//block the resource in order to send the whole message
		this.sendLock.lock();
		//byte array corresponding to the size of the message to send
		byte[] messageSize= ByteBuffer.allocate(Integer.BYTES).putInt(length).array();
		int nbBytes =0;
		try {
			//we first write the size of the message
			nbBytes=this.channel.write(messageSize, 0, Integer.BYTES);
			//write the message
			nbBytes+=this.channel.write(bytes, offset, length);
			if(nbBytes != length+Integer.BYTES) {
				throw new Exception ("There is an error when sending the message. Not all bytes were sent.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("The message wasn't sent, try again");
		}
		//unblock the resource
		this.sendLock.unlock();
	}

	/***
	 * Read the entirety of the message sent. Receives the message as a whole.
	 * Blocking until everything is read.
	 * @return the array of bytes containing the message the other sent
	 */
	@Override
	public byte[] receive() {
		//block the resource in order to receive the whole message
		this.receiveLock.lock();
		byte[] messageSizeBytes =new byte[100];
		byte[] message = null;
		int messageSizeInt=0;
		int nbBytes=0;
		try {
			//we retrieve the size of the message (first 4 bytes=length of the message)
			nbBytes=this.channel.read(messageSizeBytes, 0, 4);
			//convert message size to int
			messageSizeInt=ByteBuffer.wrap(messageSizeBytes).getInt();

			//read the message
			message = new byte[messageSizeInt];
			nbBytes+=this.channel.read(message, 0, messageSizeInt);
			if(nbBytes != messageSizeInt+4) {
				throw new Exception ("An error occur when reading the message. Not all bytes were read.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("The message can't be read, try again");
		}
		//unblock the resource
		this.receiveLock.unlock();
		return message;	
	}

	/***
	 * Closes the connection between two MessageQueues therefore disconnects the two MessageQueues involved in the connection.
	 */
	@Override
	public void close() {
		this.channel.disconnect();
		this.isClosed=true;
		this.channel=null;
		this.receiveLock=null;
		this.sendLock=null;
	}

	/***
	 * Allows you to know if the MessageQueue is disconnected. If so return true.
	 * @return a boolean indicating whether the MessageQueue is disconnected.
	 */
	@Override
	public boolean closed() {
		return isClosed;
	}

}
