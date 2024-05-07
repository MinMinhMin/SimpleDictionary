package myapp.Music;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import myapp.MainController;

import java.io.File;
import java.util.List;

public class MusicPlayer {
	private ToggleButton play;
	private Button previous, next;
	private Slider volumeslider;
	private final List<String> songs;
	private final List<String> songnames;
	private int currentSongIndex = 0;//Chỉ số bài hát hiện tại
	private Label nameOfSong;
	private final Media media;
	private MediaPlayer mediaPlayer;
	//Phương thức khởi tạo MusicPlayer với nút play, next, previous ,thanh volume, list bài hát, tên bài hát, (tên bài hát đang chơi - Label)
	public MusicPlayer(ToggleButton play, Button next, Button previous, Slider volumeslider, List<String> songs, List<String> songnames, Label nameOfSong) {
		this.play = play;
		this.next = next;
		this.previous = previous;
		this.volumeslider = volumeslider;
		this.songs = songs;
		this.songnames = songnames;
		media = new Media(new File(songs.get(currentSongIndex)).toURI().toString());
		mediaPlayer = new MediaPlayer(media); //tạo mediaplayer vói chỉ số của file hiện tại trong list songs
		this.nameOfSong = nameOfSong;
		this.nameOfSong.setText(songnames.get(currentSongIndex));// tên bài hát đc gán cho chỉ số hiện tại của songnames

	}

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

	private void setEnd() {
		this.mediaPlayer.setOnEndOfMedia(() -> next.fire());
	}// với mỗi lần bài hát kết thúc tự động ấn nút next

	//dùng MusicPLayer.setup() để cài đặt chức năng cho cái đói tượng được play,next,previous,volumeslider,...
	public void setup() {
		setEnd();
		volumeslider.setMin(0);
		volumeslider.setMax(100);
		volumeslider.setValue(mediaPlayer.getVolume() * 100);
		volumeslider.valueProperty().addListener((observable, oldValue, newValue) -> {
			mediaPlayer.setVolume(newValue.doubleValue() / 100);//setup thanh volume
		});
		play.getStylesheets().add(MainController.class.getResource("Styling.css").toExternalForm());
		play.getStyleClass().add("playSongButton");
		play.setOnAction(event -> {
			if (play.isSelected()) {
				//chuyển đổi trạng thái nút play -> pause khi ấn lần đầu
				this.mediaPlayer.play();
				play.getStyleClass().remove("playSongButton");
				play.getStyleClass().add("pauseSongButton");
			} else {
				//chuyển đổi trạng thái nút páue -> play khi ấn lần sau
				this.mediaPlayer.pause();
				play.getStyleClass().remove("pauseSongButton");
				play.getStyleClass().add("playSongButton");
			}
		});
		next.setOnAction(event -> {
			// chỉ số của bài hát đc thay đổi khi ấn next (bài tiếp theo)
			currentSongIndex = (currentSongIndex + 1) % songs.size();
			nameOfSong.setText(songnames.get(currentSongIndex));
			this.mediaPlayer.stop();
			this.mediaPlayer = new MediaPlayer(new Media(new File(songs.get(currentSongIndex)).toURI().toString()));
			setEnd();//với mỗi mediaplayer ta setend() để cài đặt trạng thái kết thúc của mediaplayer (chuyển sang bài tiếp theo)
			this.mediaPlayer.play();
			if (!play.isSelected()) {
				play.setSelected(true);
				play.getStyleClass().remove("playSongButton");
				play.getStyleClass().add("pauseSongButton");
			}
		});

		previous.setOnAction(event -> {
			//same với nút next
			currentSongIndex = (currentSongIndex - 1 + songs.size()) % songs.size();
			nameOfSong.setText(songnames.get(currentSongIndex));
			this.mediaPlayer.stop();
			this.mediaPlayer = new MediaPlayer(new Media(new File(songs.get(currentSongIndex)).toURI().toString()));
			mediaPlayer.play();
			if (!play.isSelected()) {
				play.setSelected(true);
				play.getStyleClass().remove("playSongButton");
				play.getStyleClass().add("pauseSongButton");
			}
		});

	}

}
