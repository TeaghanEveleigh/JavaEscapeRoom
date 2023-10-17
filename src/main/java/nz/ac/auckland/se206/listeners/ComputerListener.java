package nz.ac.auckland.se206.listeners;

/** This interface is used to listen for interactions with the computer. */
public interface ComputerListener {

  /** This method listens for when the computer is interacted with. */
  public void computerInteracted();

  /** This method listens for when the computer is touched. */
  public void computerTouched();

  /** This method listens for when the computer is not touched. */
  public void computerNotTouched();
}
