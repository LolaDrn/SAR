import java.util.HashMap;

public class Main {

	public static void main (String args[]) throws Exception {
				
		TaskA A=new TaskA("A");
		TaskB B=new TaskB("B");
		
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