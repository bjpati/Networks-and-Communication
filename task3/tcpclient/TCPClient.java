package tcpclient;

import java.net.*;
import java.io.*;

public class TCPClient {

	ByteArrayOutputStream fromServerBuffer = new ByteArrayOutputStream();
	boolean shutdown = false;
	Integer timeout = null;
	Integer limit = null;

	public TCPClient(boolean shutdown, Integer timeout, Integer limit) {

		this.shutdown = shutdown;
		this.timeout = timeout;
		this.limit = limit;

	}

	public byte[] askServer(String hostname, int port, byte[] toServerBytes) throws IOException {

		byte[] array = new byte[1024];
		int sssb = 0;

		Socket clientSocket = new Socket(hostname, port);

		try {

			OutputStream outputData = clientSocket.getOutputStream();

			outputData.write(toServerBytes, 0, toServerBytes.length);

			if (this.shutdown)
				clientSocket.shutdownOutput();

			InputStream inputData = clientSocket.getInputStream();

			if (this.timeout != null) {
				clientSocket.setSoTimeout(this.timeout);

			}

			if (this.limit == null || this.limit == 0) {
				while ((sssb = inputData.read(array)) != -1) {
					fromServerBuffer.write(array, 0, sssb);
				}
			}

			else if (this.limit <= array.length && this.limit != 0) {
				int temp = array.length;
				if (limit < array.length) {
					temp = limit;
					while (temp != 0 && (sssb = inputData.read(array, 0, temp)) != -1) {
						fromServerBuffer.write(array, 0, sssb);
						break;
					}
				}
			} else {
				int temp1 = limit;
				while ((sssb = inputData.read(array)) != -1) {
					fromServerBuffer.write(array, 0, sssb);
					temp1 = temp1 - array.length;

					if (temp1 > array.length) {
						continue;
					} else {
						sssb = inputData.read(array, 0, temp1);
						fromServerBuffer.write(array, 0, sssb);
						break;
					}
				}

			}

		} catch (SocketTimeoutException e) {
			clientSocket.shutdownOutput();
		} catch (IOException e) {
			System.out.println("Somthing went wrong");
			return e.getMessage().getBytes();
		} finally {
			return fromServerBuffer.toByteArray();
		}
	}

}
