package nz.ac.auckland.se206.listeners;

/** This interface is used to listen for interactions with the keypad. */
public interface KeypadListener {

  /** This method listens for when the keypad is interacted with. */
  public void keypadInteracted();

  /** This method listens for when the keypad is touched. */
  public void keypadTouched();

  /** This method listens for when the keypad is not touched. */
  public void keypadNotTouched();
}
