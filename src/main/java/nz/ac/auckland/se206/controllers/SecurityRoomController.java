package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.Passcode;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.Timers;
import nz.ac.auckland.se206.game.Keypad;
import nz.ac.auckland.se206.game.Portal;
import nz.ac.auckland.se206.game.SecurityRoomDoor;
import nz.ac.auckland.se206.game.SolidBox;
import nz.ac.auckland.se206.game.StoneCarving;
import nz.ac.auckland.se206.game.Suspicion;
import nz.ac.auckland.se206.game.Wires;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.listeners.KeypadListener;
import nz.ac.auckland.se206.listeners.SecurityRoomDoorListener;
import nz.ac.auckland.se206.listeners.StoneCarvingListener;
import nz.ac.auckland.se206.listeners.SuspicionListener;
import nz.ac.auckland.se206.listeners.WiresListener;

/**
 * Controller class for the security room. This is where the player can interact with the keypad and
 * wires game.
 */
public class SecurityRoomController extends GameController
    implements KeypadListener,
        WiresListener,
        SecurityRoomDoorListener,
        StoneCarvingListener,
        SuspicionListener {

  @FXML private Label carvingLabel;
  @FXML private ImageView stoneCarving;
  @FXML private Text stoneText;
  @FXML private Rectangle blurScreen;
  @FXML private Line cameraLine1;
  @FXML private Line cameraLine2;
  @FXML private Ellipse cameraBase;
  @FXML private ImageView cameraTriangle;
  @FXML private Text interractHint;
  @FXML private Label wireLabel;
  @FXML private Label doorLabel;
  @FXML private Label hintsLabel;
  @FXML private Rectangle correctColor;
  @FXML private Rectangle incorrectColor;
  @FXML private Text incorrectTxt;
  @FXML private Text correctTxt;
  @FXML private Rectangle one;
  @FXML private Rectangle two;
  @FXML private Rectangle three;
  @FXML private Rectangle four;
  @FXML private Rectangle five;
  @FXML private Rectangle six;
  @FXML private Rectangle seven;
  @FXML private Rectangle eight;
  @FXML private Rectangle nine;
  @FXML private Rectangle zero1;
  @FXML private Rectangle clear;
  @FXML private Rectangle enter;
  @FXML private ImageView keypad;
  @FXML private Rectangle numberRectangle;
  @FXML private Text numbers;
  @FXML private Rectangle keypadRectangle;
  private int numberOfnumbers = 0;

  @FXML private Text number1;
  @FXML private Text number2;
  @FXML private Text number3;
  @FXML private Text number4;
  @FXML private Text number5;
  @FXML private Text number6;
  @FXML private Text number7;
  @FXML private Text number8;
  @FXML private Text number9;

  @FXML private Text zeroKey;
  @FXML private Rectangle number0;

  // Bounding boxes
  @FXML private Rectangle boundingBox2;
  @FXML private Rectangle boundingBox3;
  @FXML private Rectangle boundingBox4;
  @FXML private Rectangle boundingBox5;
  @FXML private Rectangle boundingBox6;
  @FXML private Rectangle boundingBox7;
  @FXML private Rectangle boundingBox8;
  @FXML private Rectangle boundingBox9;
  @FXML private Rectangle boundingBox10;
  @FXML private Rectangle boundingBox11;
  @FXML private Rectangle boundingBox12;
  @FXML private Rectangle boundingBox13;
  @FXML private Rectangle boundingBox14;
  @FXML private Rectangle boundingBox15;
  @FXML private Rectangle boundingBox16;
  @FXML private Line securityLine1;
  @FXML private Line securityLine2;
  @FXML private Line securityLine3;
  @FXML private Text securityText;

  // Bounding boxes
  @FXML private Rectangle boundingBoxOne;
  @FXML private Rectangle boundingBoxTwo;
  @FXML private Rectangle wiresBounds;
  @FXML private Rectangle keypadBounds;
  @FXML private Rectangle boundingBoxThree;
  @FXML private Rectangle securityDoorBounds;
  @FXML private Rectangle dinosaurRoomBounds;
  @FXML private Rectangle stoneCarvingBounds;
  private SolidBox exitBlock;
  @FXML private Rectangle boundingBox1;

  // Arrows for interactables
  @FXML private ImageView arrow1;
  @FXML private ImageView arrow2;
  @FXML private ImageView arrow3;
  @FXML private ImageView arrow4;
  @FXML private ImageView arrow5;
  @FXML private ProgressBar suspicionProgressBar;
  @FXML private Rectangle suspicionRectangle;
  private Passcode passcode = Passcode.getInstance();
  private Suspicion suspicion;

  /**
   * Runs when the clear or enter button is pressed. Clears the numbers text if clear is pressed and
   * checks the pin if enter is pressed.
   *
   * @param event the mouse event triggered by the clear or enter button
   */

  @FXML
  private void handleClearEnter(MouseEvent event) {
    Rectangle clickRectangle = (Rectangle) event.getSource();
    if (clickRectangle == clear) { // If the clear button was pressed
      numbers.setText("");
      numberOfnumbers = 0;
    } else if (clickRectangle == enter) { // If the enter button was pressed
      checkPin(); // Check the pin
    }
  }

  /** Runs when the player moves close to the door. Shows the door label and interact hint. */
  @FXML
  private void showDoorLabel() {
    doorLabel.setOpacity(1);
    interractHint.setOpacity(1);
    arrow5.toBack();
  }

  /** Runs when the player moves away from the door. Hides the door label and interact hint. */
  @FXML
  void hideDoorLabel() {
    doorLabel.setOpacity(0);
    interractHint.setOpacity(0);
    arrow5.toFront();
  }

  /** Shows the security to the user. */
  @FXML
  private void showSecurity() {
    // Change the stroke width of all three security lines to 3

    // Change the fill of all three security lines to white
    securityLine1.setStroke(Color.WHITE);
    securityLine2.setStroke(Color.WHITE);
    securityLine3.setStroke(Color.WHITE);

    // Change the opacity of the security text to 1 (fully visible)
    securityText.setOpacity(1);
    interractHint.setOpacity(1);
    arrow3.toBack();
  }

  /** Hides the security from the user. */
  @FXML
  private void hideSecurity() {
    // Reset the stroke width of all three security lines (assuming it was originally 1, adjust as
    // necessary)
    securityLine1.setStrokeWidth(3);
    securityLine2.setStrokeWidth(3);
    securityLine3.setStrokeWidth(3);

    // Reset the fill of all three security lines (assuming it was originally null, adjust as
    // necessary)
    securityLine1.setStroke(Color.TRANSPARENT);
    securityLine2.setStroke(Color.TRANSPARENT);
    securityLine3.setStroke(Color.TRANSPARENT);

    // Change the opacity of the security text to 0 (completely hidden)
    securityText.setOpacity(0);
    interractHint.setOpacity(0);
    arrow3.toFront();
  }

  /** Disables the camera in the security room. */
  @FXML
  public void disableCamera() {
    cameraLine1.toBack();
    cameraLine2.toBack();
    cameraBase.toBack();
    cameraTriangle.toBack();
    boundsObjects.remove(exitBlock);
    boundsObjects.remove(suspicion);
  }

  /**
   * Initializes the security room. This method is automatically called after the fxml file has been
   * loaded.
   */
  @Override
  public void initialize() {
    suspicion = new Suspicion(boundingBoxThree, this, suspicionProgressBar, suspicionRectangle);
    super.initialize();
    boundsObjects.add(new SolidBox(boundingBox3));
    boundsObjects.add(new SolidBox(boundingBox4));
    boundsObjects.add(new SolidBox(boundingBox5));
    boundsObjects.add(new SolidBox(boundingBox6));
    boundsObjects.add(new SolidBox(boundingBox7));
    boundsObjects.add(new SolidBox(boundingBox8));
    boundsObjects.add(new SolidBox(boundingBox9));
    boundsObjects.add(new SolidBox(boundingBox10));
    boundsObjects.add(new SolidBox(boundingBox11));
    boundsObjects.add(new SolidBox(boundingBox12));
    boundsObjects.add(new SolidBox(boundingBox13));
    boundsObjects.add(new SolidBox(boundingBox14));
    boundsObjects.add(new SolidBox(boundingBox15));
    boundsObjects.add(new SolidBox(boundingBox16));
    boundsObjects.add(new Keypad(keypadBounds, this));
    boundsObjects.add(new Wires(wiresBounds, this));
    boundsObjects.add(new SecurityRoomDoor(securityDoorBounds, this));
    boundsObjects.add(new Portal(dinosaurRoomBounds, this, AppUi.DINOSAUR_ROOM));
    boundsObjects.add(new StoneCarving(stoneCarvingBounds, this));
    this.player.setBoundingBoxes(boundsObjects);
    // Applies the animations to each arrow in the room
    applyFloatingAnimation(arrow1);
    applyFloatingAnimation(arrow2);
    applyFloatingAnimation(arrow3);
    applyFloatingAnimation(arrow4);
    applyFloatingAnimation(arrow5);
  }

  /**
   * Handles the event when the mouse is clicked.
   *
   * @param event the mouse event triggered by the mouse click.
   */
  @FXML
  private void handleMouseClicked(MouseEvent event) {
    // Get the source rectangle that was clicked
    Rectangle clickedRectangle = (Rectangle) event.getSource();

    // Determine which rectangle was clicked and append the appropriate number
    if (numberOfnumbers < 6) {
      if (clickedRectangle == one) {
        enterValue(" 1");
      } else if (clickedRectangle == two) {
        enterValue(" 2");
      } else if (clickedRectangle == three) {
        enterValue(" 3");
      } else if (clickedRectangle == four) {
        enterValue(" 4");
      } else if (clickedRectangle == five) {
        enterValue(" 5");
      } else if (clickedRectangle == six) {
        enterValue(" 6");
      } else if (clickedRectangle == seven) {
        enterValue(" 7");
      } else if (clickedRectangle == eight) {
        enterValue(" 8");
      } else if (clickedRectangle == nine) {
        enterValue(" 9");
      } else if (clickedRectangle == zero1) {
        enterValue(" 0");
      }
    }
  }

  /**
   * Enters a value into the numbers text for the keypad.
   *
   * @param value the value to be entered
   */
  private void enterValue(String value) {
    numbers.setText(numbers.getText() + value); // Append the value to the numbers text
    numberOfnumbers++;
  }

  /** Checks the pin entered by the user. */
  private void checkPin() {
    // Get the keycode from the Passcode singleton and format it
    String pin = Passcode.getInstance().getKeyCode();
    pin =
        " "
            + pin.charAt(0)
            + " "
            + pin.charAt(1)
            + " "
            + pin.charAt(2)
            + " "
            + pin.charAt(3)
            + " "
            + pin.charAt(4);

    System.out.println(numbers.getText());

    if (pin.equals(numbers.getText())) { // If the pin is correct
      GameState.isDoorOpen = true;
      GameState.isExitDoorUnlocked = true;
      GameController
          .updateAllChecklists(); // Update the checklist to show that the exit door has been
      // unlocked
      correctColor.toFront();
      correctTxt.toFront();
      numbers.setOpacity(0);

      // Wait for 0.5 seconds then hide the keypad
      PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
      pause.setOnFinished(event -> hideKeyPad());
      pause.play();

    } else { // If the pin is incorrect
      incorrectColor.toFront();
      incorrectTxt.toFront();
      numbers.setOpacity(0);
      numbers.setText(""); // Reset the numbers text

      // Wait for 0.5 seconds then reset the numbers
      PauseTransition pause = new PauseTransition(Duration.seconds(1));
      pause.setOnFinished(event -> resetNumbers());
      pause.play();
    }
  }

  /** Runs when the player moves close to the wires. Shows the wire label and interact hint. */
  @FXML
  private void showWireLabel() {
    wireLabel.setOpacity(1);
    interractHint.setOpacity(1);
    arrow2.toBack();
  }

  /** Runs when the player moves away from the wires. Hides the wire label and interact hint. */
  @FXML
  private void hideWireLabel() {
    wireLabel.setOpacity(0);
    interractHint.setOpacity(0);
    arrow2.toFront();
  }

  /** Resets the numbers entered into the keypad. */
  private void resetNumbers() {
    numbers.setText("");
    numberOfnumbers = 0;
    incorrectColor.toBack();
    incorrectTxt.toBack();
    numbers.setOpacity(1);
  }

  /** Puts the keypad to the front of the screen for the user to enter the keypad. */
  @FXML
  private void showKeyPad() {
    blurScreen.toFront();
    keypadRectangle.toFront();

    keypad.toFront();

    // Bring all the components of the keypad to the front of the screen in the right order
    numberRectangle.toFront();
    numbers.toFront();
    number0.toFront();
    number1.toFront();
    number2.toFront();
    number3.toFront();
    number4.toFront();
    number5.toFront();
    number6.toFront();
    number7.toFront();
    number8.toFront();
    number9.toFront(); // Making sure the keypad image is in front of its background
    one.toFront();
    two.toFront();
    three.toFront();
    four.toFront();
    five.toFront();
    six.toFront();
    seven.toFront();
    eight.toFront();
    nine.toFront();
    zero1.toFront();
    clear.toFront();
    enter.toFront();
    zeroKey.toFront(); 
    numbers.setOpacity(1); // Reset the opacity of the numbers text
    // Enable interaction for the keys
    one.setDisable(false);
    two.setDisable(false);
    three.setDisable(false);
    four.setDisable(false);
    five.setDisable(false);
    six.setDisable(false);
    seven.setDisable(false);
    eight.setDisable(false);
    nine.setDisable(false);
    zero1.setDisable(false);
    clear.setDisable(false);
    enter.setDisable(false);// This makes sure the numbers text is visible on the top
  }

  /** Hides the keypad from the user. */
  @FXML
  private void hideKeyPad() {
    blurScreen.toBack();
    keypadRectangle.toBack();
    numberRectangle.toBack();
    // Sends the numbers to the back of the screen
    number1.toBack();
    number2.toBack();
    number3.toBack();
    number4.toBack();
    number5.toBack();
    number6.toBack();
    number7.toBack();
    number8.toBack();
    number9.toBack();

    keypad.toBack();
    // Sends the number backgrounds to the back of the screen
    one.toBack();
    two.toBack();
    three.toBack();
    four.toBack();
    five.toBack();
    six.toBack();
    seven.toBack();
    eight.toBack();
    nine.toBack();
    zero1.toBack();
    number0.toBack();
    clear.toBack();
    enter.toBack();
    numbers.setOpacity(0);
    // Sends the keypad text to the back of the screen
    correctColor.setOpacity(0);
    incorrectColor.setOpacity(0);
    correctTxt.setOpacity(0);
    incorrectTxt.setOpacity(0);
    zeroKey.toBack();
    numbers.setOpacity(0); // Clear the numbers text
    // Disable interaction for the keys
    one.setDisable(true);
    two.setDisable(true);
    three.setDisable(true);
    four.setDisable(true);
    five.setDisable(true);
    six.setDisable(true);
    seven.setDisable(true);
    eight.setDisable(true);
    nine.setDisable(true);
    zero1.setDisable(true);
    clear.setDisable(true);
    enter.setDisable(true);
  }

  /** This method runs when the player interacts with the keypad. */
  @Override
  public void keypadInteracted() {
    if (!GameState.isDoorOpen) { // If the door hasn't already been unlocked
      showKeyPad();
    } else if (!GameState.isTreasureStolen) {
      Task<Void> task =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              disableHintChatAndExit();
              // Tell the user they need to steal the item before they can escape
              ai.runGpt(
                  new ChatMessage("user", GptPromptEngineering.getMustStealItem()), hackerTextArea);
              enableHintChatAndExit();
              return null;
            }
          };
      new Thread(task).start();
    } else { // If the door has already been unlocked
      // load the game won scene
      FXMLLoader gameWonLoader = App.getFxmlLoader("gamewon");
      try {
        SceneManager.addUi(AppUi.GAME_WON, gameWonLoader.load());
      } catch (IOException e) {
        e.printStackTrace();
      }
      SceneManager.addController(AppUi.GAME_WON, gameWonLoader.getController());
      App.switchScenes(AppUi.GAME_WON);
    }
  }

  /** Runs when the user hovers over the keypad. */
  @Override
  public void keypadTouched() {
    showDoorLabel();
  }

  /** Runs when the user moves away from the keypad. */
  @Override
  public void keypadNotTouched() {
    hideDoorLabel();
    hideKeyPad();
  }

  /** Runs when the user interacts with the wires. */
  @Override
  public void wiresInteracted() {
    if (SceneManager.containsUi(AppUi.WIRES_GAME)) { // If the wires game has already been loaded
      App.switchScenes(AppUi.WIRES_GAME);
    } else { // If the wires game hasn't been loaded yet
      FXMLLoader wiresLoader = App.getFxmlLoader("wires");
      try {
        SceneManager.addUi(AppUi.WIRES_GAME, wiresLoader.load());
      } catch (IOException e) {
        e.printStackTrace();
      }
      SceneManager.addController(AppUi.WIRES_GAME, wiresLoader.getController());
      App.switchScenes(AppUi.WIRES_GAME);
    }
  }

  /** Runs when the user moves close to the wires. */
  @Override
  public void wiresTouched() {
    showWireLabel();
  }

  /** Runs when the user moves away from the wires. */
  @Override
  public void wiresUntouched() {
    hideWireLabel();
  }

  /** Runs when the user interacts with the security door. */
  @Override
  public void onSecurityDoorInteracted() {
    pauseRoom();
    App.switchScenes(AppUi.SECURITY_ROOM); // Load the security room
  }

  /** Runs when the user moves close to the security door. */
  @Override
  public void onSecurityDoorTouched() {
    showSecurity();
  }

  /** Runs when the user moves away from the security door. */
  @Override
  public void onSecurityDoorNotTouched() {
    hideSecurity();
  }

  /** Runs when the user interacts with the stone carving. */
  @FXML
  private void showStoneCarving() {
    blurScreen.toFront();
    stoneCarving.toFront();
    stoneText.toFront();
  }

  /** Runs when the user exits from the stone carving. */
  @FXML
  private void hideStoneCarving() {
    blurScreen.toBack();
    stoneCarving.toBack();
    stoneText.toBack();
  }

  /** Runs when the user moves close to the stone carving. */
  @FXML
  private void showCarvingLabel() {
    carvingLabel.setOpacity(1);
    arrow4.toBack();
  }

  /** Runs when the user moves away from the stone carving. */
  @FXML
  private void hideCarvingLabel() {
    carvingLabel.setOpacity(0);
    arrow4.toFront();
  }

  /** Runs when the user interacts with the stone carving. Shows the stone carving and the text */
  @Override
  public void stoneCarvingInteracted() {
    showStoneCarving();
  }

  /** Runs when the user moves close to the stone carving. */
  @Override
  public void stoneCarvingTouched() {
    showCarvingLabel();
  }

  /** Runs when the user moves away from the stone carving. */
  @Override
  public void stoneCaringUntouched() {
    hideCarvingLabel();
    hideStoneCarving();
  }

  /** Applies the animation to all the arrows in the room. */
  private void applyFloatingAnimation(ImageView imageView) {
    TranslateTransition translateTransition =
        new TranslateTransition(Duration.seconds(1), imageView);
    translateTransition.setByY(5);
    translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
    translateTransition.setAutoReverse(true);
    translateTransition.play();
  }

  /** Runs when the user moves into the camera visual zone. */
  @Override
  public void suspicionTouched() {
    suspicionProgressBar.toFront();
    suspicionRectangle.toFront();
  }

  /** Runs when the user moves out of the camera visual zone. */
  @Override
  public void suspicionNotTouched() {
    suspicionRectangle.toBack();
  }

  /** Runs when the user is caught by the camera. */
  @Override
  public void suspicionReached() {
    Timers mainTimer = Timers.getInstance();
    mainTimer.subtractTime(10); // Subtract 10 seconds from the main timer
  }

  @Override
  public void start() {
    started = true;
  }

  @Override
  public void reset() {
    GameState value = GameState.getInstance();
    value.subscribe(hintsLabel);
    stoneText.setText("Discovered " + passcode.getThirdNum() + " century");
    this.player.setPosX(54);
    this.player.setPosY(300);

    numbers.setText("");
    numberOfnumbers = 0;
    cameraLine1.toFront();
    cameraLine2.toFront();
    cameraBase.toFront();
    cameraTriangle.toFront();
    boundsObjects.add(suspicion);
    System.out.println("dsadsadsa");
    Timers mainTimer = Timers.getInstance();
    mainTimer.subscribeLabel(mainTimerLabel);
  }
}
