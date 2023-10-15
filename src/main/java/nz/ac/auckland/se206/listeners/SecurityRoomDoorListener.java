package nz.ac.auckland.se206.listeners;

public interface SecurityRoomDoorListener {
  public void onSecurityDoorInteracted();

  public void onSecurityDoorTouched();

  public void onSecurityDoorUntouched();
}
