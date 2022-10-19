import java.util.HashMap;

public class Main {

	public static void main (String args[]) throws Exception {
		TaskServer A=new TaskServer("A", 666);
		TaskClient B=new TaskClient("B","A", 666);
		A.start();
		B.start();
		
		try {
			A.join();
			B.join();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}