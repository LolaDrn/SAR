package testQueueBroadcast;

public class TestBroadcast {
	public static void main(String[] args) throws Exception {
		
		int nbClients=2;
		
		TaskServerBroadcast server =new TaskServerBroadcast(666, "A",nbClients);
		server.start();
		
		TaskClientSenderBroadcast[] clientsSender = new TaskClientSenderBroadcast[nbClients];
		TaskClientReceiverBroadcast[] clientsReceiver = new TaskClientReceiverBroadcast[nbClients];

		for (int i = 0; i < nbClients; i++) {
			clientsSender[i] = new TaskClientSenderBroadcast(Integer.toString(i), 666, "A");
			clientsReceiver[i] = new TaskClientReceiverBroadcast(Integer.toString(i), 666, "A");
}
		for (int i = 0; i < nbClients; i++) {
			clientsSender[i].start();
			clientsReceiver[i].start();
		}
}
}
