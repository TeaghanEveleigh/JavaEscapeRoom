package nz.ac.auckland.se206.listeners;

/** This interface is used to listen for interactions with objects. */
public interface ObjectListener {

  /** This method listens for when an object is interacted with. */
  public void objectInteracted();

  /** This method listens for when an object is touched. */
  public void objectTouched();

  /** This method listens for when an object is not touched. */
  public void objectUntouched();
}
