package nz.ac.auckland.se206.listeners;

/** This interface is used to listen for interactions with the security room door. */
public interface SecurityRoomDoorListener {

  /** This method listens for when the security room door is interacted with. */
  public void onSecurityDoorInteracted();

  /** This method listens for when the security room door is touched. */
  public void onSecurityDoorTouched();

  /** This method listens for when the security room door is not touched. */
  public void onSecurityDoorNotTouched();
}
