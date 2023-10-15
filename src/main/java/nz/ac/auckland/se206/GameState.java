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
  private List<Label> subscribers = new ArrayList<>();
  private static final GameState instance = new GameState();

  public static void disableCamera() {
    SecurityRoomController cameraRoomController =
        (SecurityRoomController) SceneManager.getUiController(AppUi.EXIT_ROOM);
    cameraRoomController.disableCamera();
    isCamerasDisabled = true;
  }

  public static void openSafe() {
    ExitRoomController safeRoomController =
        (ExitRoomController) SceneManager.getUiController(AppUi.SECURITY_ROOM);
    safeRoomController.safeOpen();
  }

  // Public static method to get the single instance of GameState
  public static GameState getInstance() {
    return instance;
  }

  public static String getHintsLeft() {
    return hintsLeft;
  }

  public static String generateWiresSequence() {
    String[] array = {"1", "2", "3", "4"};
    List<String> list = Arrays.asList(array);
    Collections.shuffle(list);
    StringBuilder sb = new StringBuilder();
    for (String s : list) {
      sb.append(s);
    }
    return sb.toString();
  }

  // Private constructor to ensure singleton property
  private GameState() {
    setMedium(); // Just for initialization, you can remove or change this line as needed
  }

  public void subscribe(Label label) {
    subscribers.add(label);
    updateLabels();
  }

  public void subtractHint() {
    if (isMedium) {
      System.out.println("Before subtracting hint: " + hintsLeft); // Debugging print
      int numberOfHints = Integer.parseInt(hintsLeft);
      numberOfHints--;
      hintsLeft = String.valueOf(numberOfHints);
      System.out.println("After subtracting hint: " + hintsLeft); // Debugging print
      updateLabels();
    }
  }

  private void updateLabels() {
    Platform.runLater(
        () -> {
          for (Label label : subscribers) {
            if (isHard) {
              label.setText("Database Down");
            } else if (isEasy) {
              label.setText("No hint limit"); // This is the infinity symbol
            } else {
              label.setText("Hints left: " + hintsLeft);
            }
            System.out.println("Label updated: " + label.getText()); // Debugging print
          }
        });
  }

  public void setEasy() {
    isEasy = true;
    isMedium = false;
    isHard = false;
    updateLabels(); // If you want to update labels when difficulty is changed
  }

  public void setMedium() {
    isEasy = false;
    isMedium = true;
    isHard = false;
    hintsLeft = "5";
    updateLabels();
  }

  public void setHard() {
    isEasy = false;
    isMedium = false;
    isHard = true;
    hintsLeft = "∞";
    updateLabels();
  }
}
