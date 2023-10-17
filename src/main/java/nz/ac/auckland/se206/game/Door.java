package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.GameController;

/**
 * This class represents a door in the game. The player can interact with the door to move to the
 * next room.
 */
public class Door extends Interactable {

  private AppUi nextRoot;
  private GameController leavingRoom;

  /**
   * This constructor creates a new door for the player to interact with.
   *
   * @param rectangle The rectangle to define the bounds of the door.
   * @param leavingRoom The room the player is leaving.
   * @param nextRoot The root of the next room.
   */
  public Door(Rectangle rectangle, GameController leavingRoom, AppUi nextRoot) {
    super(rectangle);
    this.nextRoot = nextRoot;
  }

  /** This method is called when the player interacts with the door. */
  @Override
  public void interact() {
    leavingRoom.pauseRoom();
    App.switchScenes(nextRoot);
  }

  /** This method is called when the player touches the door. */
  @Override
  public void touched() {}

  /** This method is called when the player stops touching the door. */
  @Override
  public void notTouched() {}
}
