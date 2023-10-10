package nz.ac.auckland.se206;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.ExitRoomController;
import nz.ac.auckland.se206.controllers.SecurityController;

/** Represents the state of the game. */
public class GameState {

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether the key has been found. */
  public static boolean isKeyFound = false;

  /** Randomly generated order of endpoints for the wires game */
  public static String wiresSequence = generateWiresSequence();

  /** Indicates whether the lasers have been disabled */
  public static boolean isLasersDisabled = false;

  /** Indicates whether the cameras have been disabled */
  public static boolean isCamerasDisabled = false;

  /** Indicated whether the treasure has been stolen */
  public static boolean isTreasureStolen = false;

  // Difficulty booleans
  public static boolean isEasy = true;
  public static boolean isMedium = false;
  public static boolean isHard = false;

  public static int timeLimit = 2;
  public static boolean textToSpeech = false;
  public static boolean isDoorOpen = false;

  public static int hintsLeft = 5;

  // Sets the difficulty to easy
  public static void setEasy() {
    isEasy = true;
    isMedium = false;
    isHard = false;
  }

  /** Generates a random ordering of the numbers one through four inclusive */
  public static String generateWiresSequence() {
    String[] array = {"1", "2", "3", "4"};
    List<String> list = Arrays.asList(array);
    Collections.shuffle(list);
    StringBuilder sb = new StringBuilder();
    for (String s : list) {
      sb.append(s);
    }
    System.out.println(sb.toString());
    return sb.toString();
  }

  // Sets the difficulty to medium
  public static void setMedium() {
    isEasy = false;
    isMedium = true;
    isHard = false;
  }

  // Sets the difficulty to hard
  public static void setHard() {
    isEasy = false;
    isMedium = false;
    isHard = true;
  }

  public static void disableCamera() {
    SecurityController cameraRoomController =
        (SecurityController) SceneManager.getUiController(AppUi.EXIT_ROOM);
    cameraRoomController.disableCamera();
    isCamerasDisabled = true;
  }

  public static void openSafe() {
    ExitRoomController safeRoomController =
        (ExitRoomController) SceneManager.getUiController(AppUi.SECURITY_ROOM);
    safeRoomController.safeOpen();
  }
}
