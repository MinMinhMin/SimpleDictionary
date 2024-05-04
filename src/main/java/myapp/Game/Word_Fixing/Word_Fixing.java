package myapp.Game.Word_Fixing;

import myapp.SuggestionBox.Words;

import java.util.LinkedHashSet;
import java.util.Set;

public class Word_Fixing {

	public String word;
	public String guessing_word;

	public Word_Fixing(String mode) {

		this.word = Words.get_a_random_word(mode);
		this.guessing_word = getGuessing(this.word);
	}

	public String getGuessing(String word) {

		int length = word.length();
		Set<Integer> set = new LinkedHashSet<>();
		StringBuilder stringBuilder = new StringBuilder();
		while (stringBuilder.length() != length) {

			int old_size = set.size();
			int index = (int) (Math.random() * length);
			set.add(index);
			if (set.size() != old_size) {

				stringBuilder.append(word.charAt(index));
			}


		}
		return stringBuilder.toString();

	}


}
