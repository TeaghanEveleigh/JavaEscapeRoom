package nz.ac.auckland.se206;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.input.KeyCode;

public class KeyState {
  private static Set<KeyCode> keysDown = new HashSet<KeyCode>();

  public static void keyPressed(KeyCode keyCode) {
    keysDown.add(keyCode);
  }

  public static void keyReleased(KeyCode keyCode) {
    keysDown.remove(keyCode);
  }

  public static void resetKeys() {
    keysDown.clear();
  }

  public static Set<KeyCode> getKeysPressed() {
    return keysDown;
  }
}
