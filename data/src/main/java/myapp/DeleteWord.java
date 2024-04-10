package myapp;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DeleteWord {
	public static void deleteWord(String word) {
		try {
			Path path = Path.of("data/List.txt");
			List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
			lines.removeIf(line -> (line.split("\\t")[0].equals(word)));
			try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
				for (String line : lines) {
					writer.write(line);
					writer.newLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}