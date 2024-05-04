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