package com.lele.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketUtil {
	public static String inputStream2String(InputStream input) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = input.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	public static void sendObject(Object object, Socket socket) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject(object);
		oos.flush();
	}

	public static Object receiveObject(Socket socket) throws IOException, ClassNotFoundException {
		InputStream input = socket.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(input);
		return ois.readObject();
	}
}
