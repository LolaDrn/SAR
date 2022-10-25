/***
 * Allows to create usable MessageQueues. 
 * The connect and accept methods can occur regardless of the order in which they are called.
 * @author lola
 *
 */
public class QueueBrokerImpl extends QueueBroker{
	
	private Broker broker;

	/***
	 * Constructor of the class, used to create an instance of QueueBroker.
	 * @param name (string) - the name of the QueueBroker
	 * @throws Exception
	 */
	public QueueBrokerImpl(String name) throws Exception {
		super(name);
		this.broker=new BrokerImpl(name);
	}

	/***
	 * The QueueBroker waits for a connection on the specified port. 
	 * Blocking method as long as the appointment is not accepted. 
	 * The messageQueue is then created.
	 * @param port (int) – the port on which the QueueBroker/Broker waits for the connection
	 * @return the MessageQueue associated to the QueueBroker which do the accept
	 * @throws Exception
	 */
	@Override
	public MessageQueue accept(int port) throws Exception {
		MessageQueue msgQueue = null;
		try {
			//creation of the channel to communicate
			Channel channel=broker.accept(port);
			//creation of the messageQueue which will take care of the communication
			msgQueue= new MessageQueueImpl(channel);
		} catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
		return msgQueue;
	}

	/***
	 * Connection to another QueueBroker whose name is given on the same specified port. 
	 * Blocking method as long as the appointment is not accepted. 
	 * The messageQueue is then created.
	 * @param name (string) - the name of the QueueBroker/Broker to connect
	 * @param port (int) - the port on which the QueueBroker/Broker waits for the connection
	 * @return the MessageQueue associated to the QueueBroker which do the connect
	 */
	@Override
	public MessageQueue connect(String name, int port) {
		MessageQueue msgQueue = null;
		try {
			//creation of the channel to communicate
			Channel channel=broker.connect(name, port);
			//creation of the messageQueue which will take care of the communication
			msgQueue= new MessageQueueImpl(channel);
		} catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
		return msgQueue;
	}

}
