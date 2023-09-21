package nz.ac.auckland.se206;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Represents the state of the game. */
public class GameState {

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether the key has been found. */
  public static boolean isKeyFound = false;

  /** Randomly generated order of endpoints for the wires game */
  public static String wiresSequence = generateWiresSequence();

  /** Generates a random ordering of the numbers one through four inclusive */
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
}
