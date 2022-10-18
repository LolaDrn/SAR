
public class TestMultiClients {
	public static void main(String[] args) throws Exception {
		
		int nbClient=5;
		
		ServerTask server =new ServerTask("A",666);
		server.start();
		
		ClientTask[] clients = new ClientTask[nbClient];
		for (int i = 0; i < nbClient; i++) {
			clients[i] = new ClientTask(Integer.toString(i),"A", 666);
			clients[i].start();
		}
	}
			
//			try {
//				clients[i].join();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		/*
		TaskB[] clients = new TaskB[5];
		TaskA[] serveurs = new TaskA[5];
		
		for (int i = 0; i < 5; i++) {
			serveurs[i] = new TaskA(Integer.toString(5+i), i);
			clients[i] = new TaskB(Integer.toString(i),Integer.toString(5+i), i);
			serveurs[i].start();
			clients[i].start();
			
			try {
				clients[i].join();
				serveurs[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
//		}
//		try {
//			server.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//	}
}
