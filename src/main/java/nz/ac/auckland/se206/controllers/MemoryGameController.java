package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.BaseController;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.Timers;
import nz.ac.auckland.se206.gpt.Ai;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class MemoryGameController implements BaseController {
  private static double sequenceSeconds = 0.5;
  private static int maxSequenceLength = 6;

  @FXML private ImageView lightOne;
  @FXML private ImageView lightTwo;
  @FXML private ImageView lightThree;
  @FXML private ImageView lightFour;
  @FXML private ImageView lightFive;
  @FXML private ImageView lightSix;
  @FXML private ImageView lightEight;
  @FXML private ImageView lightSeven;
  @FXML private ImageView lightNine;
  @FXML private ImageView lightTen;

  // Hacker Panel
  @FXML private ImageView exitHackerPanelImage;
  @FXML private ImageView hackerIcon;
  @FXML private Rectangle hackerRectangle;
  @FXML private TextArea hackerTextArea;

  @FXML private Button hintButton;
  @FXML private Button backButton;

  @FXML private Label mainTimerLabel;

  private Ai ai = new Ai();
  private ArrayList<ImageView> lights;
  private ArrayList<ImageView> sequence;
  private ArrayList<ImageView> lightsPressed;
  private Image baseLightImage =
      new Image(getClass().getResource("/images/push-button.png").toExternalForm());
  private Image lightPressedImage =
      new Image(getClass().getResource("/images/push-button-glow.png").toExternalForm());
  private Image lightGlowRedImage =
      new Image(getClass().getResource("/images/push-button-glow-red.png").toExternalForm());
  private Image lightGlowGreenImage =
      new Image(getClass().getResource("/images/push-button-glow-green.png").toExternalForm());

  private boolean showingSequence = false;
  private int currentSequenceLength = 1;

  @FXML
  public void initialize() {
    Timers mainTimer = Timers.getInstance();
    mainTimer.subscribeLabel(mainTimerLabel);
    this.lights = new ArrayList<ImageView>();
    this.sequence = new ArrayList<ImageView>();
    this.lightsPressed = new ArrayList<ImageView>();
    enableHackerPanel();
    getIntroduction();
    hackerTextArea.setEditable(false);

    lights.add(lightOne);
    lights.add(lightTwo);
    lights.add(lightThree);
    lights.add(lightFour);
    lights.add(lightFive);
    lights.add(lightSix);
    lights.add(lightSeven);
    lights.add(lightEight);
    lights.add(lightNine);
    lights.add(lightTen);
  }

  private void chooseSequence(int sequenceLength) {
    Random random = new Random();
    for (int i = 0; i < sequenceLength; i++) {
      ImageView chosen = lights.get(random.nextInt(lights.size()));
      while (sequence.contains(chosen)) {
        chosen = lights.get(random.nextInt(lights.size()));
      }
      sequence.add(chosen);
    }
  }

  private void showSequence(int sequenceLength) {
    showingSequence = true;
    Timeline timeline = new Timeline();
    timeline.setCycleCount(1);
    ArrayList<ImageView> lightsQueue = new ArrayList<ImageView>(sequence);
    System.out.println(lightsQueue.size());
    for (int i = 0; i < sequenceLength; i++) {
      ImageView light = lightsQueue.get(i);
      timeline
          .getKeyFrames()
          .add(
              new KeyFrame(
                  Duration.seconds((i + 1) * sequenceSeconds),
                  e -> {
                    setLight(light, lightPressedImage);
                  }));
      timeline
          .getKeyFrames()
          .add(
              new KeyFrame(
                  Duration.seconds((i + 2) * sequenceSeconds),
                  e -> {
                    resetLight(light);
                  }));
    }
    timeline.play();
    timeline.setOnFinished(
        e -> {
          showingSequence = false;
        });
  }

  private void setLight(ImageView light, Image lightImage) {
    light.setImage(lightImage);
  }

  private void resetLight(ImageView light) {
    light.setImage(baseLightImage);
  }

  private void setAllLights(Image lightImage) {
    for (ImageView light : this.lights) {
      setLight(light, lightImage);
    }
  }

  private void resetAllLights() {
    for (ImageView light : this.lights) {
      resetLight(light);
    }
  }

  private void checkSequence() {
    // Disable user input
    showingSequence = true;

    for (int i = 0; i < currentSequenceLength; i++) {
      if (!lightsPressed.get(i).equals(sequence.get(i))) {
        Timeline timeline = flashLights(lightGlowRedImage);
        timeline.setOnFinished(e -> showSequence(currentSequenceLength));
        return;
      }
    }

    Timeline timeline = flashLights(lightGlowGreenImage);
    if (currentSequenceLength++ >= maxSequenceLength) {

      Room2Controller roomController =
          (Room2Controller) SceneManager.getUiController(AppUi.SECURITY_ROOM);
      roomController.safeOpen();
      PauseTransition pause = new PauseTransition(Duration.seconds(0.75));
      pause.setOnFinished(
          e -> {
            roomController.unpauseRoom();
            App.switchScenes(AppUi.SECURITY_ROOM);
          });

      System.out.println("WON");
      GameState.isKeycodeFound = true;
      GameController.updateAllChecklists();
      enableHackerPanel();
      Task<Void> task =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              disableHintAndExit();
              ai.runGpt(
                  new ChatMessage("user", GptPromptEngineering.getMemoryGameSolved()),
                  hackerTextArea);
              enableHintAndExit();
              exitHackerPanelImage.setDisable(false);
              return null;
            }
          };
      new Thread(task).start();
    } else {
      timeline.setOnFinished(e -> showSequence(currentSequenceLength));
    }
  }

  private Timeline flashLights(Image lightImage) {

    Timeline timeline = new Timeline();

    // Create a keyframe to set the lights to the specified color
    KeyFrame colorFrame = new KeyFrame(Duration.seconds(0.1), e -> setAllLights(lightImage));

    // Create a keyframe to reset the lights to the base color
    KeyFrame resetFrame = new KeyFrame(Duration.seconds(0.2), e -> resetAllLights());

    // Add the keyframes to the timeline
    timeline.getKeyFrames().addAll(colorFrame, resetFrame);

    // Set the cycle count to control the number of times lights flash
    timeline.setCycleCount(2); // Adjust the number of flashes as needed

    // Play the timeline
    timeline.play();

    return timeline;
  }

  public void start() {
    resetAllLights();
    sequence.clear();
    chooseSequence(6);
    currentSequenceLength = 1;
    showSequence(currentSequenceLength);
  }

  @FXML
  private void onLightPressed(MouseEvent event) throws IOException {
    if (showingSequence) return;

    ImageView pressed = (ImageView) event.getSource();
    setLight(pressed, lightPressedImage);
    lightsPressed.add(pressed);
    if (lightsPressed.size() == currentSequenceLength) {
      checkSequence();
      lightsPressed.clear();
    }
  }

  /**
   * This method gives a hint to the user through the AI
   *
   * @throws ApiProxyException
   */
  @FXML
  private void onHintPressed() throws ApiProxyException {
    enableHackerPanel();
    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            disableHintAndExit();
            if (GameState.isHard || (GameState.hintsLeft <= 0)) {
              ai.runGpt(
                  new ChatMessage("user", GptPromptEngineering.getCantGiveHint()), hackerTextArea);
            } else {
              ai.runGpt(
                  new ChatMessage("user", GptPromptEngineering.getMemoryGameHint()),
                  hackerTextArea);
            }
            enableHintAndExit();
            if (GameState.isMedium) {
              GameState.hintsLeft--;
            }
            return null;
          }
        };
    new Thread(task).start();
  }

  /**
   * This method returns the user to the main menu.
   *
   * @throws IOException
   */
  @FXML
  private void onBackPressed() throws IOException {
    this.sequence.clear();
    this.lightsPressed.clear();
    App.switchScenes(AppUi.SECURITY_ROOM);
  }

  @FXML
  public void onExitClicked() {
    disableHackerPanel();
  }

  public void getIntroduction() {
    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            disableHintAndExit();
            ai.runGpt(
                new ChatMessage("user", GptPromptEngineering.getMemoryGameIntroduction()),
                hackerTextArea);
            enableHintAndExit();
            return null;
          }
        };
    new Thread(task).start();
  }

  public void disableHackerPanel() {
    hackerIcon.toBack();
    hackerRectangle.toBack();
    hackerTextArea.toBack();
    exitHackerPanelImage.toBack();
    exitHackerPanelImage.setDisable(true);
  }

  public void disableHintAndExit() {
    hintButton.setDisable(true);
    exitHackerPanelImage.setDisable(true);
    backButton.setDisable(true);
  }

  public void enableHintAndExit() {
    hintButton.setDisable(false);
    exitHackerPanelImage.setDisable(false);
    backButton.setDisable(false);
  }

  public void enableHackerPanel() {
    hackerRectangle.toFront();
    hackerIcon.toFront();
    hackerTextArea.toFront();
    exitHackerPanelImage.toFront();
    exitHackerPanelImage.setDisable(false);
  }
}
