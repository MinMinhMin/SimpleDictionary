package myapp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

	public static void AddtoFIle(String fileName, String line) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
			writer.write(line);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<String> getlist(String filename) {

		List<String> list = new ArrayList<>();

		try {
			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {

				list.add(line);
			}
			bufferedReader.close();
			fileReader.close();
			return list;

		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}

	}


	public static void cache() {
		try {
			PrintWriter writer = new PrintWriter("data/cache.txt");
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String getCache() {
		try {
			FileReader fileReader = new FileReader("data/cache.txt");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String cache = bufferedReader.readLine();
			fileReader.close();
			bufferedReader.close();
			return cache;


		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

}