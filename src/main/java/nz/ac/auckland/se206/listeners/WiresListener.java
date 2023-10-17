package nz.ac.auckland.se206.listeners;

/** This interface is used to listen for interactions with the wires. */
public interface WiresListener {

  /** This method is used to listen for when the wires are interacted with. */
  public void wiresInteracted();

  /** This method is used to listen for when the wires are touched. */
  public void wiresTouched();

  /** This method is used to listen for when the wires are not touched. */
  public void wiresUntouched();
}
