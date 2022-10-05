
public class QueueBrokerImpl extends QueueBroker{
	
	private Broker broker;

	public QueueBrokerImpl(String name) throws Exception {
		super(name);
		this.broker=new BrokerImpl(name);
	}

	@Override
	public MessageQueue accept(int port) throws Exception {
		MessageQueue msgQueue = null;
		try {
			//creation du channel pour communiquer
			Channel channel=broker.accept(port);
			//creation du messageQueue qui se chargera de la communication
			msgQueue= new MessageQueueImpl(channel);
		} catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
		return msgQueue;
	}

	@Override
	public MessageQueue connect(String name, int port) {
		MessageQueue msgQueue = null;
		try {
			//creation du channel pour communiquer
			Channel channel=broker.connect(name, port);
			//creation du messageQueue qui se chargera de la communication
			msgQueue= new MessageQueueImpl(channel);
		} catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
		return msgQueue;
	}

}
