package nz.ac.auckland.se206.listeners;

/** This interface is used to listen for interactions with the right dinosaur. */
public interface RightDinosaurListener {

  /** This method is used to listen for when the right dinosaur is interacted with. */
  public void rightDinosaurInteracted();

  /** This method is used to listen for when the right dinosaur is touched. */
  public void rightDinosaurTouched();

  /** This method is used to listen for when the right dinosaur is not touched. */
  public void rightDinosaurUntouched();
}
