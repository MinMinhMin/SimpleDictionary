package myapp;

import java.io.*;

public class FileUtil {

	public static void AddtoFIle(String fileName, String line) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
			writer.write(line);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}