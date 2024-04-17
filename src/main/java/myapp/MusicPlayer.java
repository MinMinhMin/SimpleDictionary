package myapp;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.List;

public class MusicPlayer {
	private ToggleButton play;
	private Button previous,next;
	private Slider volumeslider;

	public ToggleButton getPlay() {
		return play;
	}

	public void setPlay(ToggleButton play) {
		this.play = play;
	}

	public Button getPrevious() {
		return previous;
	}

	public void setPrevious(Button previous) {
		this.previous = previous;
	}

	public Button getNext() {
		return next;
	}

	public void setNext(Button next) {
		this.next = next;
	}

	public Slider getVolumeslider() {
		return volumeslider;
	}

	public void setVolumeslider(Slider volumeslider) {
		this.volumeslider = volumeslider;
	}

	public Label getNameOfSong() {
		return nameOfSong;
	}

	public void setNameOfSong(Label nameOfSong) {
		this.nameOfSong = nameOfSong;
	}

	private List<String> songs;
	private List<String> songnames;
	private int currentSongIndex = 0;
	private Label nameOfSong ;
	private Media media ;
	private MediaPlayer mediaPlayer ;

	public MusicPlayer(ToggleButton play,Button next,Button previous, Slider volumeslider,List<String> songs, List<String> songnames,Label nameOfSong){
		this.play = play;
		this.next = next;
		this.previous = previous;
		this.volumeslider = volumeslider;
		this.songs = songs;
		this.songnames = songnames;
		media = new Media(new File(songs.get(currentSongIndex)).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		this.nameOfSong = nameOfSong;
		this.nameOfSong.setText(songnames.get(currentSongIndex));

	}

	public void setup(){
		volumeslider.setMin(0);
		volumeslider.setMax(100);
		volumeslider.setValue(mediaPlayer.getVolume() * 100);
		volumeslider.valueProperty().addListener((observable, oldValue, newValue) -> {
			mediaPlayer.setVolume(newValue.doubleValue() / 100);
		});
		play.getStylesheets().add(MainController.class.getResource("Styling.css").toExternalForm());
		play.getStyleClass().add("playSongButton");
		play.setOnAction(event -> {
			if(play.isSelected()){
				mediaPlayer.play();
				play.getStyleClass().remove("playSongButton");
				play.getStyleClass().add("pauseSongButton");
			}else {
				mediaPlayer.pause();
				play.getStyleClass().remove("pauseSongButton");
				play.getStyleClass().add("playSongButton");
			}
		});
		next.setOnAction(event -> {
			currentSongIndex = (currentSongIndex + 1) % songs.size();
			nameOfSong.setText(songnames.get(currentSongIndex));
			mediaPlayer.stop();
			mediaPlayer = new MediaPlayer(new Media(new File(songs.get(currentSongIndex)).toURI().toString()));
			mediaPlayer.play();
			if (!play.isSelected()) {
				play.setSelected(true);
				play.getStyleClass().remove("playSongButton");
				play.getStyleClass().add("pauseSongButton");
			}
		});

		previous.setOnAction(event -> {
			currentSongIndex = (currentSongIndex - 1 + songs.size()) % songs.size();
			nameOfSong.setText(songnames.get(currentSongIndex));
			mediaPlayer.stop();
			mediaPlayer = new MediaPlayer(new Media(new File(songs.get(currentSongIndex)).toURI().toString()));
			mediaPlayer.play();
			if (!play.isSelected()) {
				play.setSelected(true);
				play.getStyleClass().remove("playSongButton");
				play.getStyleClass().add("pauseSongButton");
			}
		});

	}

}
