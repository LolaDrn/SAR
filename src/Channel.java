import java.io.IOException;

public abstract class Channel {

	public abstract int read(byte[] bytes, int offset, int length) throws IOException;
	
	public abstract int write(byte[] bytes, int offset, int length) throws IOException;
	
	public abstract void disconnect();
	
	public abstract boolean disconnected();

}
