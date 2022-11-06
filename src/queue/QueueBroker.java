package queue;

public abstract class QueueBroker {
	public QueueBroker(String name) {
	}
	public abstract MessageQueue accept(int port) throws Exception;
	public abstract MessageQueue connect(String name, int port);
}
