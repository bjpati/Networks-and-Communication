

import java.net.*;

import tcpclient.TCPClient;

import java.io.*;

public class MyRunnable implements Runnable {

	public Socket clientSocket;
	
	static boolean shutdown = false;
	static Integer timeout = null;
	static Integer limit = null;

	byte[] serverBuffer = new byte[1024];

	public MyRunnable(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public void run() {

		String status = null;
		String hostname = null;
		int port = 0;
		String stringFC = "";
		int sssb = 0;
		String stringDecode = "";

		String HTTP200 = "HTTP/1.1 200 OK\n\n";
		String HTTP400 = "HTTP/1.1 400 Bad Request\n\n";
		String HTTP404 = "HTTP/1.1 404 Not Found\n\n";

		try {
			System.out.println("Conniction suc");
			InputStream inputData = clientSocket.getInputStream();
			OutputStream outputData = clientSocket.getOutputStream();

			while ((sssb = inputData.read(serverBuffer)) != -1) {
				stringDecode = new String(serverBuffer, 0, sssb);

				String[] split = stringDecode.split("[?&= ]", 10);

				System.out.println(stringDecode);

				if (split[0].equals("GET") && split[1].equals("/ask") && stringDecode.contains("HTTP/1.1")) {
					status = HTTP200;
					for (int i = 0; i < split.length; i++) {
						if (split[i].equals("hostname")) {
							hostname = split[i + 1];
						} else if (split[i].equals("port")) {
							port = Integer.parseInt(split[i + 1]);
						} else if (split[i].equals("string")) {
							stringFC = split[i + 1];
						}
					}
					outputData.write(status.getBytes("UTF-8"));
				}

				else {
					status = HTTP400;
					outputData.write(status.getBytes("UTF-8"));
				}

				break;

			}

			if (!(status.contains(HTTP400))) {
				try {
					byte[] toServerByte = stringFC.getBytes("UTF-8");
					TCPClient tcp = new TCPClient(shutdown, timeout, limit);
					byte[] tcpClient = tcp.askServer(hostname, port, toServerByte);
					outputData.write(tcpClient);
				}

				catch (IOException e) {
					status = HTTP404;
					outputData.write(status.getBytes("UTF-8"));
				}
			} else {
				outputData.write(status.getBytes("UTF-8"));
			}

			clientSocket.close();

		}

		catch (IOException e) {
			System.out.println("Somthing went wrong");
		}

	}
}
