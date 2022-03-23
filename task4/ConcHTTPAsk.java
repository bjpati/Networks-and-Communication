

import java.io.IOException;
import java.net.*;

public class ConcHTTPAsk {
	
	public static void main (String[] args) throws IOException {
		
		ServerSocket welcomeSocket = new ServerSocket(Integer.parseInt(args[0]));
		while (true)
		{
			Socket clientSocket = welcomeSocket.accept();
			MyRunnable Clientconniction = new MyRunnable(clientSocket);
            		Thread ClientThreadconniction = new Thread(Clientconniction);
           		ClientThreadconniction.start();
		}
		
	}

}
