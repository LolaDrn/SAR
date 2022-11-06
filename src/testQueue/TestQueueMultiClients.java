package testQueue;

public class TestQueueMultiClients {
	public static void main(String[] args) throws Exception {
			
			int nbClient=5;
			
			TaskQueueServer server =new TaskQueueServer("A");
			server.start();
			
			TaskQueueClient[] clients = new TaskQueueClient[nbClient];
			for (int i = 0; i < nbClient; i++) {
				clients[i] = new TaskQueueClient(Integer.toString(i));
			}
			for (int i = 0; i < nbClient; i++) {
	
				clients[i].start();
			}
	}
}
