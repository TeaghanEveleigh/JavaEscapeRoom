package nz.ac.auckland.se206.listeners;

/** This interface is used to listen for interactions with the stone carving. */
public interface StoneCarvingListener {

  /** This method is used to listen for when the stone carving is interacted with. */
  public void stoneCarvingInteracted();

  /** This method is used to listen for when the stone carving is touched. */
  public void stoneCarvingTouched();

  /** This method is used to listen for when the stone carving is not touched. */
  public void stoneCaringUntouched();
}
