import java.util.HashMap;

public class BrokerManager {
	private HashMap<String, Broker> bufferMap;

	private static BrokerManager brokerManager;

	public synchronized static BrokerManager getInstance() {
		if (brokerManager == null)
			brokerManager = new BrokerManager();
		return brokerManager;
	}
	
	private BrokerManager() {
		this.bufferMap= new HashMap<String, Broker>();
	}

	public HashMap<String, Broker> getBufferMap() {
		return bufferMap;
	}
	
	
}
