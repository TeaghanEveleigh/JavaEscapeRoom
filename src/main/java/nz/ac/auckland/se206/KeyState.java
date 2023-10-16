package nz.ac.auckland.se206;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.input.KeyCode;

/** This class tracks the state of each key when it is pressed or released. */
public class KeyState {
  private static Set<KeyCode> keysDown = new HashSet<KeyCode>();

  /**
   * This method is called when a key is pressed.
   *
   * @param keyCode The key that was pressed.
   */
  public static void keyPressed(KeyCode keyCode) {
    keysDown.add(keyCode);
  }

  /**
   * This method is called when a key is released.
   *
   * @param keyCode The key that was released.
   */
  public static void keyReleased(KeyCode keyCode) {
    keysDown.remove(keyCode);
  }

  /** This method resets the keys that are currently pressed. */
  public static void resetKeys() {
    keysDown.clear();
  }

  /**
   * This method returns a set of all keys that are currently pressed.
   *
   * @return A set of all keys that are currently pressed.
   */
  public static Set<KeyCode> getKeysPressed() {
    return keysDown;
  }
}
