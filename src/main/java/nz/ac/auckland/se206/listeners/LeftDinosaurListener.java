package nz.ac.auckland.se206.listeners;

/** This interface is used to listen for interactions with the left dinosaur. */
public interface LeftDinosaurListener {

  /** This method is used to listen for when the left dinosaur is interacted with. */
  public void leftDinosaurInteracted();

  /** This method is used to listen for when the left dinosaur is touched. */
  public void leftDinosaurTouched();

  /** This method is used to listen for when the left dinosaur is not touched. */
  public void leftDinosaurUntouched();
}
