package myapp;

import java.net.URL;
import java.net.URLConnection;


public class InternetConnectionService {


	//Kiểm tra mạng
	public static boolean isInternetConnected() {
		try {
			final URL url = new URL("http://www.google.com");
			final URLConnection conn = url.openConnection();
			conn.setConnectTimeout(200);
			conn.setReadTimeout(200);
			conn.connect();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}