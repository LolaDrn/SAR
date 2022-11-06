package testChannel;

public class TestMultiClients {
	public static void main(String[] args) throws Exception {
		
		int nbClient=5;
		
		TaskServer server =new TaskServer("A",666);
		server.start();
		
		TaskClient[] clients = new TaskClient[nbClient];
		for (int i = 0; i < nbClient; i++) {
			clients[i] = new TaskClient(Integer.toString(i),"A", 666);
		}
		for (int i = 0; i < nbClient; i++) {

			clients[i].start();
		}

	}
			
	
}
