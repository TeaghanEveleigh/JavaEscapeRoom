package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.ExitRoomController;
import nz.ac.auckland.se206.controllers.SecurityRoomController;

/**
 * This class is used to store the state of the game. It is a singleton class, so it can be accessed
 * from anywhere in the application.
 */
public class GameState {

  public static boolean isLasersDisabled = false;
  public static boolean isCamerasDisabled = false;
  public static boolean isTreasureStolen = false;
  public static boolean isKeycodeFound = false;
  public static boolean isExitDoorUnlocked = false;
  public static boolean isEasy = true;
  public static boolean isMedium = false;
  public static boolean isHard = false;
  public static int timeLimit = 2;
  public static boolean textToSpeech = false;
  public static boolean isDoorOpen = false;
  public static String wiresSequence = generateWiresSequence();

  private static String hintsLeft;
  private static final GameState instance = new GameState();

  /** This method disables the cameras in the room to exit from. */
  public static void disableCamera() {
    SecurityRoomController cameraRoomController =
        (SecurityRoomController) SceneManager.getUiController(AppUi.EXIT_ROOM);
    cameraRoomController.disableCamera(); // Disable the cameras
    isCamerasDisabled = true;
  }

  /** This method opens the safe in the security room. */
  public static void openSafe() {
    ExitRoomController safeRoomController =
        (ExitRoomController) SceneManager.getUiController(AppUi.SECURITY_ROOM);
    safeRoomController.safeOpen(); // Open the safe
  }

  /**
   * This method is used to get the instance of the GameState singleton.
   *
   * @return The instance of the GameState singleton.
   */
  public static GameState getInstance() {
    return instance;
  }

  /**
   * This method returns the number of hints left.
   *
   * @return The number of hints left.
   */
  public static String getHintsLeft() {
    return hintsLeft;
  }

  /**
   * This method generates a random sequence of wires that the user needs to match to win the lasers
   * game.
   *
   * @return The random sequence of wires to match.
   */
  public static String generateWiresSequence() {
    String[] array = {"1", "2", "3", "4"};
    List<String> list = Arrays.asList(array);
    Collections.shuffle(list); // Shuffle the list into a random order
    StringBuilder sb = new StringBuilder();
    for (String s : list) {
      sb.append(s); // Append each element of the list to the string builder
    }
    return sb.toString();
  }

  private List<Label> subscribers = new ArrayList<>();

  /** This constructor creates a new GameState instance to ensure singleton. */
  private GameState() {
    setMedium(); // Just for initialization, you can remove or change this line as needed
  }

  /**
   * This method adds a label to the list of subscribers to update when the number of hints left or
   * timer changes.
   *
   * @param label The label to add.
   */
  public void subscribe(Label label) {
    subscribers.add(label);
    updateLabels(); // Update labels when a new subscriber is added
  }

  /** This method decreases the number of hints left for the user to use once they have used one. */
  public void subtractHint() {
    if (isMedium) { // Only subtract a hint if the difficulty is medium
      System.out.println("Before subtracting hint: " + hintsLeft); // Debugging print
      int numberOfHints = Integer.parseInt(hintsLeft);
      numberOfHints--;
      hintsLeft = String.valueOf(numberOfHints);
      System.out.println("After subtracting hint: " + hintsLeft); // Debugging print
      updateLabels();
    }
  }

  /** This method updates the labels of the hints left label in all rooms. */
  private void updateLabels() {
    Platform.runLater(
        () -> {
          for (Label label : subscribers) {
            if (isHard) { // If the difficulty is hard, set the label to 0
              label.setText("Database Down");
            } else if (isEasy) { // If the difficulty is easy, set the label to infinity
              label.setText("No hint limit");
            } else { // If the difficulty is medium, set the label to the number of hints left
              label.setText("Hints left: " + hintsLeft);
            }
            System.out.println("Label updated: " + label.getText());
          }
        });
  }

  /** This method sets the difficulty to easy and updates hint left label accordingly. */
  public void setEasy() {
    isEasy = true;
    isMedium = false;
    isHard = false;
  }

  /** This method sets the difficulty to medium and updates hint left label accordingly. */
  public void setMedium() {
    isEasy = false;
    isMedium = true;
    isHard = false;
    hintsLeft = "5";
  }

  /** This method sets the difficulty to hard and updates hint left label accordingly. */
  public void setHard() {
    isEasy = false;
    isMedium = false;
    isHard = true;
    hintsLeft = "∞";
  }
}
