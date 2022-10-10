import java.util.HashMap;

public class Main {

	public static void main (String args[]) throws Exception {
		TaskA A=new TaskA("A");
		TaskB B=new TaskB("B",666);
		A.start();
		B.start();
		
		try {
			A.join();
			B.join();
		}catch(Exception e) {
			e.printStackTrace();
		}
		/*
		int connexionPort=0;
		TaskA A=new TaskA("A");
		TaskB B;
		while (true) {
			B=new TaskB("B", connexionPort);
				
			A.start();
			B.start();
			
			try {
				A.join();
				B.join();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}*/
		
		/*TaskQueueA A=new TaskQueueA("A");
		TaskQueueB B=new TaskQueueB("B");
		
		A.start();
		B.start();
		
		try {
			A.join();
			B.join();
		}catch(Exception e) {
			e.printStackTrace();
		}*/
		
		}
	
}