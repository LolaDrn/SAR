package testQueue;

public class TestQueueOneClient {

	public static void main (String args[]) throws Exception {
		TaskQueueServer A=new TaskQueueServer("A");
		TaskQueueClient B=new TaskQueueClient("B");
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

