package nz.ac.auckland.se206.listeners;

/** This interface is used to listen for interactions with the safe. */
public interface SafeListener {

  /** This method is used to listen for when the safe is interacted with. */
  public void safeInteracted();

  /** This method is used to listen for when the safe is touched. */
  public void safeTouched();

  /** This method is used to listen for when the safe is not touched. */
  public void safeNotTouched();
}
