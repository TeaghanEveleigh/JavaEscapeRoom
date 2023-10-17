package nz.ac.auckland.se206.listeners;

/** This interface is used to listen for interactions with the exit room door. */
public interface ExitRoomDoorListener {

  /** This method listens for when the exit room door is interacted with. */
  public void exitDoorInteracted();

  /** This method listens for when the exit room door is touched. */
  public void exitDoorTouched();

  /** This method listens for when the exit room door is not touched. */
  public void exitDoorUntouched();
}
