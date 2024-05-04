package myapp;

import java.net.URL;
import java.net.URLConnection;


public class InternetConnectionService {


	public static boolean isInternetConnected(){
		try {
			final URL url = new URL("http://www.google.com");
			final URLConnection conn = url.openConnection();
			conn.connect();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}