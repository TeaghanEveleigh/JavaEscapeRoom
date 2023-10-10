package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.BaseController;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.Timers;

public class MainMenuController implements BaseController {
  @FXML private Button startGameButton;
  @FXML private Button tutorialButton;
  @FXML private Button gameSettingButton;
  @FXML private Text loadingText;
  @FXML private ProgressBar loadingBar;
  @FXML private ImageView loadingScreen;
  @FXML
    public void initialize() {
        // Add a listener to the sceneProperty of the loading bar
        loadingBar.sceneProperty().addListener(new ChangeListener<Scene>() {
            @Override
            public void changed(ObservableValue<? extends Scene> observableValue, Scene oldScene, Scene newScene) {
                // Check if the loading bar is added to a scene
                if (newScene != null) {
                    // Reset the progress and send elements to back
                    loadingBar.setProgress(0);
                    loadingScreen.toBack();
                    loadingBar.toBack();
                    loadingText.toBack();
                }
            }
        });
    }
  @FXML
  private void onSettingsPressed(ActionEvent event) throws IOException {
    App.setRoot(AppUi.GAME_SETTINGS);
  }

  @FXML
  private void onTutorialPressed(ActionEvent event) throws IOException {
    // TODO: implement
  }

  @FXML
private void onStartPressed(ActionEvent event) {
    loadingScreen.toFront();
    loadingText.toFront();
    loadingBar.toFront();

    PauseTransition pause = new PauseTransition(Duration.seconds(1));
    pause.setOnFinished(e -> startLoading());
    pause.play();
}

private void startLoading() {
    try {
       

        // Load and show progress in steps
        loadRoom1();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private void loadRoom1() throws IOException {
    FXMLLoader dinosaurRoomLoader = App.getFxmlLoader("room1");
    SceneManager.addUi(AppUi.DINOSAUR_ROOM, dinosaurRoomLoader.load());
    SceneManager.addController(AppUi.DINOSAUR_ROOM, dinosaurRoomLoader.getController());
    loadingBar.setProgress(0.33);

    PauseTransition pause = new PauseTransition(Duration.millis(250));
    pause.setOnFinished(e -> {
        try {
            loadRoom2();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    });
    pause.play();
}
private void loadRoom2() throws IOException {
  FXMLLoader exitRoomLoader = App.getFxmlLoader("securityroom");
  SceneManager.addUi(AppUi.EXIT_ROOM, exitRoomLoader.load());
  SceneManager.addController(AppUi.EXIT_ROOM, exitRoomLoader.getController());
  loadingBar.setProgress(0.66); // Updated to 66%

  PauseTransition pause = new PauseTransition(Duration.millis(250));
  pause.setOnFinished(e -> {
      try {
          loadRoom3();
      } catch (IOException ioException) {
          ioException.printStackTrace();
      }
  });
  pause.play();
}

private void loadRoom3() throws IOException {
  FXMLLoader securityRoomLoader = App.getFxmlLoader("room2");
  SceneManager.addUi(AppUi.SECURITY_ROOM, securityRoomLoader.load());
  SceneManager.addController(AppUi.SECURITY_ROOM, securityRoomLoader.getController());
  loadingBar.setProgress(1); // Updated to 100%

  PauseTransition pause = new PauseTransition(Duration.millis(250));
  pause.setOnFinished(e -> App.switchScenes(AppUi.DINOSAUR_ROOM));
  pause.play();
  Timers.getInstance().initializeMainCountdown(GameState.timeLimit);
  
  
}

}
