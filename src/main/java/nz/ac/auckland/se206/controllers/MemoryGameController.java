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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.BaseController;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.HackerUiToggler;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.Timers;
import nz.ac.auckland.se206.gpt.Ai;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

/**
 * Controller class for the memory game. This game unlocks the safe in the security room through a
 * memory game. The game involves a series of lights flashing in a sequence that the player has to
 * match
 */
public class MemoryGameController extends HackerUiToggler implements BaseController {
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
  private boolean started = false;

  /**
   * Initializes the controller class. This method is automatically called after the fxml file has
   * been loaded.
   */
  @FXML
  public void initialize() {
    Timers mainTimer = Timers.getInstance();
    mainTimer.subscribeLabel(mainTimerLabel);

    this.lights = new ArrayList<ImageView>();
    this.sequence = new ArrayList<ImageView>();
    this.lightsPressed = new ArrayList<ImageView>();

    enableHackerPanel(); // enables the hacker panel
    disableChat(); // disables the chat
    hackerTextArea.setEditable(false);

    // Add all the lights to the lights array
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

  /**
   * This method chooses a random sequence of lights to flash.
   *
   * @param sequenceLength the length of the sequence to flash.
   */
  private void chooseSequence(int sequenceLength) {
    Random random = new Random();
    for (int i = 0; i < sequenceLength; i++) {
      ImageView chosen = lights.get(random.nextInt(lights.size())); // Choose a random light
      while (sequence.contains(chosen)) {
        chosen = lights.get(random.nextInt(lights.size()));
      }
      sequence.add(chosen);
    }
  }

  /**
   * This method shows the sequence of lights to the user.
   *
   * @param sequenceLength the length of the sequence to show.
   */
  private void showSequence(int sequenceLength) {
    showingSequence = true;
    Timeline timeline = new Timeline();
    timeline.setCycleCount(1);
    ArrayList<ImageView> lightsQueue = new ArrayList<ImageView>(sequence);
    System.out.println(lightsQueue.size());
    for (int i = 0; i < sequenceLength; i++) { // For each light in the sequence
      ImageView light = lightsQueue.get(i);
      timeline
          .getKeyFrames()
          .add(
              new KeyFrame(
                  Duration.seconds((i + 1) * sequenceSeconds),
                  e -> {
                    setLight(light, lightPressedImage); // Turn the light on
                  }));
      timeline
          .getKeyFrames()
          .add(
              new KeyFrame(
                  Duration.seconds((i + 2) * sequenceSeconds),
                  e -> {
                    resetLight(light); // Turn the light off
                  }));
    }
    timeline.play();
    timeline.setOnFinished(
        e -> {
          showingSequence = false;
        });
  }

  /**
   * This method sets the light to the specified image.
   *
   * @param light the light to set.
   * @param lightImage the image to set the light to.
   */
  private void setLight(ImageView light, Image lightImage) {
    light.setImage(lightImage);
  }

  /**
   * This method resets the light to the base image.
   *
   * @param light the light to reset.
   */
  private void resetLight(ImageView light) {
    light.setImage(baseLightImage);
  }

  /**
   * This method sets all the lights.
   *
   * @param lightImage the image to set the lights to.
   */
  private void setAllLights(Image lightImage) {
    for (ImageView light : this.lights) {
      setLight(light, lightImage);
    }
  }

  /** This method resets all the lights. */
  private void resetAllLights() {
    for (ImageView light : this.lights) {
      resetLight(light);
    }
  }

  /** This method checks the sequence of lights pressed by the user. */
  private void checkSequence() {
    // Disable user input
    showingSequence = true;

    for (int i = 0; i < currentSequenceLength; i++) {
      if (!lightsPressed
          .get(i)
          .equals(sequence.get(i))) { // If the user pressed the wrong light sequence
        Timeline timeline =
            flashLights(lightGlowRedImage); // Flash the lights red to show incorrect sequence
        timeline.setOnFinished(e -> showSequence(currentSequenceLength));
        return;
      }
    }

    Timeline timeline =
        flashLights(lightGlowGreenImage); // Flash the lights green to show correct sequence
    if (currentSequenceLength++ >= maxSequenceLength) {
      GameState.openSafe(); // Open the safe
      PauseTransition pause = new PauseTransition(Duration.seconds(0.75));
      pause.setOnFinished(
          e -> {
            App.switchScenes(AppUi.SECURITY_ROOM); // Switch to the security room
          });

      System.out.println("WON");
      GameState.isKeycodeFound = true;
      GameController.updateAllChecklists(); // Update the checklist to show the safe has been opened
      enableHackerPanel();
      Task<Void> task =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              disableHintChatAndExit();
              // Runs the GPT-3 model to tell the user they opened the safe
              ai.runGpt(
                  new ChatMessage("user", GptPromptEngineering.getMemoryGameSolved()),
                  hackerTextArea);
              enableHintChatAndExit();
              exitHackerPanelImage.setDisable(false);
              return null;
            }
          };
      new Thread(task).start();
    } else {
      timeline.setOnFinished(e -> showSequence(currentSequenceLength));
    }
  }

  /**
   * This method flashes the lights to the specified color.
   *
   * @param lightImage the image to flash the lights to.
   * @return the timeline of the flashing lights.
   */
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

  /**
   * This method starts the memory game.
   *
   * @throws IOException if the fxml file cannot be loaded.
   */
  public void start() {
    if (!started) {
      getIntroduction();
    }

    // Reset the lights
    resetAllLights();
    sequence.clear();
    chooseSequence(6);
    currentSequenceLength = 1;
    showSequence(currentSequenceLength);

    started = true;
  }

  /**
   * Runs when the user presses a light.
   *
   * @throws IOException if the input is incorrect.
   */
  @FXML
  private void onLightPressed(MouseEvent event) throws IOException {
    if (showingSequence) {
      return;
    }

    ImageView pressed = (ImageView) event.getSource();
    setLight(pressed, lightPressedImage); // Turn the light on
    lightsPressed.add(pressed);
    if (lightsPressed.size()
        == currentSequenceLength) { // If the user has pressed the correct number of lights
      checkSequence();
      lightsPressed.clear();
    }
  }

  /** Runs when the user asks for a hint for the memory game. */
  @Override
  @FXML
  protected void onHintPressed() {
    enableHackerPanel();
    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {

            disableHintChatAndExit();

            if (GameState.isHard || (Integer.parseInt(GameState.getHintsLeft()) <= 0)) {
              // Tells the user they cant get a hint
              ai.runGpt(
                  new ChatMessage("user", GptPromptEngineering.getCantGiveHint()), hackerTextArea);
            } else {
              // Tells the user the hint
              ai.runGpt(
                  new ChatMessage("user", GptPromptEngineering.getMemoryGameHint()),
                  hackerTextArea);
              if (GameState.isMedium) {
                GameState.getInstance().subtractHint(); // Changed this line to use getInstance()
              }
            }
            enableHintChatAndExit();

            return null;
          }
        };
    new Thread(task).start();
  }

  /**
   * This method is used to take the user back to the security room.
   *
   * @throws IOException if the fxml file cannot be loaded.
   */
  @FXML
  private void onBackPressed() throws IOException {
    this.sequence.clear();
    this.lightsPressed.clear();
    App.switchScenes(AppUi.SECURITY_ROOM);
  }

  /** This method gets the introduction for the memory game. */
  public void getIntroduction() {
    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            disableHintChatAndExit();
            // Runs the GPT-3 model to introduce the player to the game and tell them the aim
            ai.runGpt(
                new ChatMessage("user", GptPromptEngineering.getMemoryGameIntroduction()),
                hackerTextArea);
            enableHintChatAndExit();
            return null;
          }
        };
    new Thread(task).start();
  }
}
