package nz.ac.auckland.se206.controllers;

import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Passcode;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.Timers;
import nz.ac.auckland.se206.game.Door;
import nz.ac.auckland.se206.game.LeftDinosaur;
import nz.ac.auckland.se206.game.Object;
import nz.ac.auckland.se206.game.Portal;
import nz.ac.auckland.se206.game.RightDinosaur;
import nz.ac.auckland.se206.game.SolidBox;
import nz.ac.auckland.se206.game.Suspicion;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.listeners.LeftDinosaurListener;
import nz.ac.auckland.se206.listeners.ObjectListener;
import nz.ac.auckland.se206.listeners.RightDinosaurListener;
import nz.ac.auckland.se206.listeners.SuspicionListener;

/**
 * Controller class for the laser room. This is where the player can interact with the dinosaurs and
 * steal the treasure. They need to disables the lasers in this room to steal the treasure.
 */
public class LaserRoomController extends GameController
    implements LeftDinosaurListener, RightDinosaurListener, ObjectListener, SuspicionListener {
  @FXML private Text interactHint;
  @FXML private Label dinoLabel1;
  @FXML private Label dinoLabel2;
  @FXML private ImageView object;
  @FXML private ImageView laserShadow1;
  @FXML private ImageView laserShadow2;
  @FXML private ImageView laserShadow3;
  @FXML private ImageView laser1;
  @FXML private ImageView laser2;
  @FXML private ImageView laser3;
  @FXML private Rectangle boundingBoxOne;
  @FXML private Rectangle boundingBoxTwo;
  @FXML private Rectangle doorRectangle;
  @FXML private Label itemLabel;
  @FXML private Rectangle leftDinosaurBounds;
  @FXML private Rectangle rightDinosaurBounds;
  @FXML private Rectangle objectBounds;
  @FXML private Text trexText;
  @FXML private Text paroText;
  @FXML private Rectangle plaque;
  @FXML private Rectangle blur;
  @FXML private ImageView arrow;
  @FXML private ImageView arrow1;
  @FXML private ImageView arrow3;
  @FXML private Label hintsLabel;
  @FXML private ProgressBar suspicionProgressBar;
  @FXML private Rectangle suspicionRectangle;

  private SolidBox laserBox;

  /**
   * Initialises the controller class. This method is automatically called after the fxml file has
   * been loaded.
   */
  @Override
  public void initialize() {
    Passcode passcode = Passcode.getInstance();
    GameState value = GameState.getInstance();
    value.subscribe(hintsLabel); // subscribe to hints
    trexText.setText("T-Rex Discovered " + passcode.getFirstNum() + " century");
    paroText.setText("Parasaurolophus Discovered " + passcode.getSecondNum() + " century");

    super.initialize(); // call super initialize

    // Sets the bounding boxes for the room
    boundsObjects.add(
        new Suspicion(boundingBoxOne, this, suspicionProgressBar, suspicionRectangle));
    boundsObjects.add(new SolidBox(boundingBoxTwo));
    boundsObjects.add(new Portal(doorRectangle, this, AppUi.EXIT_ROOM));
    boundsObjects.add(new LeftDinosaur(leftDinosaurBounds, this));
    boundsObjects.add(new RightDinosaur(rightDinosaurBounds, this));
    boundsObjects.add(new Door(doorRectangle, this, AppUi.MAIN_MENU));
    this.player.setBoundingBoxes(boundsObjects);
    this.player.setPosX(54);
    this.player.setPosY(350);

    // Applies the bouncing animations for the arrows
    applyFloatingAnimation(arrow);
    applyFloatingAnimation(arrow1);
    applyFloatingAnimationx(arrow3);
    enableHackerPanel();
    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            disableHintChatAndExit(); // Disables the hint chat and exit buttons
            // Introduces the game to the user
            ai.runGpt(
                new ChatMessage("user", GptPromptEngineering.getIntroduction()), hackerTextArea);
            enableHintChatAndExit(); // Enables the hint chat and exit buttons
            exitHackerPanelImage.setDisable(false);
            return null;
          }
        };
    new Thread(task).start();
  }

  /** Disables the lasers in the room. */
  @FXML
  public void disableLasers() {
    laser1.toBack();
    laser2.toBack();
    laser3.toBack();
    laserShadow1.toBack();
    laserShadow2.toBack();
    laserShadow3.toBack();
    itemLabel.toFront();
    boundsObjects.remove(laserBox);
    boundsObjects.add(new Object(objectBounds, this));
    player.setBoundingBoxes(boundsObjects);
  }

  /** Shows the item label. */
  @FXML
  private void itemLabelShow() {
    itemLabel.setOpacity(0.8);
    interactHint.toFront();
  }

  /** Hides the item label. */
  @FXML
  private void itemLabelHide() {
    itemLabel.setOpacity(0);
    interactHint.toBack();
  }

  /** Runs when the user steals the treasure. */
  @FXML
  private void stealItem() {
    GameState.isTreasureStolen = true;
    GameController
        .updateAllChecklists(); // Updates the checklist to show that the treasure has been stolen
    object.toBack();
    itemLabel.toBack();
    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            disableHintChatAndExit(); // Disables the hint chat and exit buttons
            ai.runGpt(
                new ChatMessage("user", GptPromptEngineering.getObjectStolen()),
                hackerTextArea); // Tells the user that they have stolen the treasure
            enableHintChatAndExit(); // Enables the hint chat and exit buttons
            return null;
          }
        };
    new Thread(task).start();
  }

  /** Shows the label of the forst dinosaur when the user goes near it. */
  @FXML
  private void showDinoLabel1() {
    dinoLabel1.setOpacity(0.8);
    interactHint.toFront();
    arrow.toBack();
  }

  /** Hides the label of the first dinosaur when the user goes away from it. */
  @FXML
  private void hideDinoLabel1() {
    dinoLabel1.setOpacity(0);
    interactHint.toBack();
    arrow.toFront();
  }

  /**
   * Runs when the user interacts with the left dinosaur. Shows the plaque for the left dinosaur.
   */
  @Override
  public void leftDinosaurInteracted() {
    showParoPlaque();
  }

  /** Runs when the user touches the left dinosaur. Shows the label for the left dinosaur. */
  @Override
  public void leftDinosaurTouched() {
    showDinoLabel1();
  }

  /** Runs when the user goes away from the left dinosaur. Hides the label for the left dinosaur. */
  @Override
  public void leftDinosaurUntouched() {
    hideDinoLabel1();
    hideParoPlaque();
  }

  /**
   * Runs when the user interacts with the right dinosaur. Shows the plaque for the right dinosaur.
   */
  @Override
  public void rightDinosaurInteracted() {
    showTrexPlaque();
  }

  /** Runs when the user touches the right dinosaur. Shows the label for the right dinosaur. */
  @Override
  public void rightDinosaurTouched() {
    showDinoLabelTwo();
  }

  /**
   * Runs when the user goes away from the right dinosaur. Hides the label for the right dinosaur.
   */
  @Override
  public void rightDinosaurUntouched() {
    hideDinoLabelTwo();
    hideTrexPlaque();
  }

  /** Runs when the user interacts with the item. Steals the item. */
  @Override
  public void objectInteracted() {
    stealItem();
  }

  /** Runs when the user touches the item. Shows the label for the item. */
  @Override
  public void objectTouched() {
    itemLabelShow();
  }

  /** Runs when the user goes away from the item. Hides the label for the item. */
  @Override
  public void objectUntouched() {
    itemLabelHide();
  }

  /** Shows the label for the second dinosaur. */
  @FXML
  private void showDinoLabelTwo() {
    dinoLabel2.setOpacity(0.8);
    interactHint.toFront();
    arrow1.toBack();
  }

  /** Hides the label for the second dinosaur. */
  @FXML
  private void hideDinoLabelTwo() {
    dinoLabel2.setOpacity(0);
    interactHint.toBack();
    arrow1.toFront();
  }

  /** Shows the plaque for the left dinosaur. */
  @FXML
  private void showParoPlaque() {
    blur.toFront();
    plaque.toFront();
    paroText.toFront();
  }

  /** Hides the plaque for the left dinosaur. */
  @FXML
  private void hideParoPlaque() {
    plaque.toBack();
    blur.toBack();
    paroText.toBack();
  }

  /** Shows the plaque for the right dinosaur. */
  @FXML
  private void showTrexPlaque() {
    blur.toFront();
    plaque.toFront();
    trexText.toFront();
  }

  /** Hides the plaque for the right dinosaur. */
  @FXML
  private void hideTrexPlaque() {
    plaque.toBack();
    blur.toBack();
    trexText.toBack();
  }

  /**
   * Applies the floating animation in the x direction to the image view arrows for the interactable
   * objects.
   *
   * @param imageView the image view to apply the animation to.
   */
  private void applyFloatingAnimation(ImageView imageView) {
    TranslateTransition translateTransition =
        new TranslateTransition(Duration.seconds(1), imageView);
    translateTransition.setByY(5); // set the distance to move
    translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
    translateTransition.setAutoReverse(true);
    translateTransition.play();
  }

  /**
   * Applies the floating animation in the y direction to the image view arrows for the interactable
   * objects.
   *
   * @param imageView the image view to apply the animation to.
   */
  private void applyFloatingAnimationx(ImageView imageView) {
    TranslateTransition translateTransition =
        new TranslateTransition(Duration.seconds(1), imageView);
    translateTransition.setByX(5); // set the distance to move
    translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
    translateTransition.setAutoReverse(true);
    translateTransition.play();
  }

  /** Runs when the suspicion bar is touched. */
  @Override
  public void suspicionTouched() {
    suspicionProgressBar.toFront();
    suspicionRectangle.toFront();
  }

  /** Runs when the suspicion is rising. */
  @Override
  public void suspicionNotTouched() {
    suspicionRectangle.toBack();
    // progress bar will be sent to back on end of progress
  }

  /** Runs when the suspicion is not rising anymore. */
  @Override
  public void suspicionReached() {
    System.out.println("Too long in lasers - subtracting time");
    Timers mainTimer = Timers.getInstance();
    mainTimer.subtractTime(10); // Subtracts 10 seconds from the main timer
  }
}
