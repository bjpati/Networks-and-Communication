package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
	
	public TCPClient () {
		
	}
   
	ByteArrayOutputStream fromServerBuffer = new ByteArrayOutputStream();

    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {
    	
    	try {
     
    	Socket clientSocket = new Socket(hostname, port);
  
    	OutputStream outputData = clientSocket.getOutputStream();
    	
    	outputData.write(toServerBytes, 0, toServerBytes.length);
    	
    	InputStream inputData = clientSocket.getInputStream();
    	
    	byte [] array = new byte [1024];
    	int sssb = 0;
    	
    	while ((sssb = inputData.read(array)) != -1)
    	{
    		fromServerBuffer.write(array, 0, sssb);
    	}
    	
    	clientSocket.close();
        
    	}
    	catch (IOException e)
    	{
    		System.out.println("Somthing went wrong");
    		return e.getMessage().getBytes();
    	}
    	
    	 return fromServerBuffer.toByteArray();
    }
}
