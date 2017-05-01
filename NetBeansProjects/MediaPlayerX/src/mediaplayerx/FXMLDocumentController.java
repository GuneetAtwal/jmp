/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediaplayerx;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

/**
 *
 * @author guneetatwal
 */
public class FXMLDocumentController implements Initializable {

    private String filePath;
    
    private MediaPlayer mediaPlayer;
    
    @FXML
    private MediaView mediaView;
    
    @FXML
    private Slider volumeSlider;
    
    @FXML
    private Slider seekSlider;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    @FXML
    private void openFile()
    {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Select a file", "*.mp4");
            fileChooser.getExtensionFilters().add(extensionFilter);
            File file = fileChooser.showOpenDialog(null);
            filePath = file.toURI().toString();
        
            if(filePath != null)
            {
                Media media = new Media(filePath);
                mediaPlayer = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayer);
                    
                    /*
                     * Fit the media to the mediaView dimensions 
                     */
                    DoubleProperty width = mediaView.fitWidthProperty();
                    DoubleProperty height = mediaView.fitHeightProperty();
                    
                    /*
                     * Binding  the height and width
                     */
                    width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
                    height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
                    
                    volumeSlider.setValue(mediaPlayer.getVolume() * 100);
                    volumeSlider.valueProperty().addListener(new InvalidationListener() {
                    @Override
                    public void invalidated(Observable observable) {
                            mediaPlayer.setVolume(volumeSlider.getValue() / 100);
                    }
                });
                    
                   mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                    @Override
                    public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                        seekSlider.setValue(newValue.toSeconds());                        
                    }
                });
                   
                   seekSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                           mediaPlayer.seek(Duration.seconds(seekSlider.getValue()));
                    }
                });
                    
                mediaPlayer.play();
            }
        
    }
    
    @FXML
    private void playMedia()
    {
        if(mediaPlayer.getStatus()==MediaPlayer.Status.PLAYING)
        {
            mediaPlayer.pause();
        }
        else
        {
            mediaPlayer.play();
        }
            
    }
    
    @FXML
    private void pauseMedia()
    {
        mediaPlayer.pause();
    }
    
    @FXML
    private void stopMedia()
    {
        mediaPlayer.stop();
    }
}
