package myapp;

public class ContextMenuController {
	private String selectedWord;

	public void handleWordForm() {
		handleOption("Word Form");
	}

	public void handleOpposite() {
		handleOption("Opposite");
	}

	public void handleClosestWord() {
		handleOption("Closest Word");
	}

	private void handleOption(String option) {
		System.out.println(selectedWord + ": " + option);
	}

	public void setSelectedWord(String word) {
		this.selectedWord = word;
	}
}