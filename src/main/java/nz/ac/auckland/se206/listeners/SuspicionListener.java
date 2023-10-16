package nz.ac.auckland.se206.listeners;

/** This interface is used to listen for interactions with the suspicion inducing objects. */
public interface SuspicionListener {

  /** This method listens for when the suspicion inducing object is interacted with. */
  public void suspicionTouched();

  /** This method listens for when the suspicion inducing object is not touched. */
  public void suspicionNotTouched();

  /** This method listens for when the mximum suspicion level is reached. */
  public void suspicionReached();
}
