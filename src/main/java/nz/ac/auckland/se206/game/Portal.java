package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.GameController;

/**
 * This class represents a portal in the game. The player can interact with the portal to move to
 * the next room.
 */
public class Portal extends Interactable {

  private AppUi nextRoom;
  private GameController originalController;

  /**
   * This constructor creates a new portal for the player to interact with.
   *
   * @param rectangle The rectangle to define the bounds of the portal.
   * @param originalController The room the player is leaving.
   * @param nextRoom The root of the next room.
   */
  public Portal(Rectangle rectangle, GameController originalController, AppUi nextRoom) {
    super(rectangle);
    this.originalController = originalController;
    this.nextRoom = nextRoom;
  }

  /** This method is called when the player interacts with the portal. */
  @Override
  public void interact() {}

  /** This method is called when the player touches the portal. */
  @Override
  public void touched() {
    if (touched) {
      return;
    }
    originalController.pauseRoom();
    App.switchScenes(nextRoom);
    touched = true;
  }

  /** This method is called when the player stops touching the portal. */
  @Override
  public void notTouched() {
    if (!touched) {
      return;
    }
    touched = false;
  }
}
