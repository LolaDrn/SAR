import java.util.HashMap;

public class Main {

	public static void main (String args[]) throws Exception {
		ServerTask A=new ServerTask("A", 666);
		ClientTask B=new ClientTask("B","A", 666);
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