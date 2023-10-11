package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.concurrent.Task;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.KeypadListener;
import nz.ac.auckland.se206.Passcode;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.SecurityRoomDoorListener;
import nz.ac.auckland.se206.StoneCarvingListener;
import nz.ac.auckland.se206.WiresListener;
import nz.ac.auckland.se206.game.Keypad;
import nz.ac.auckland.se206.game.Portal;
import nz.ac.auckland.se206.game.SecurityRoomDoor;
import nz.ac.auckland.se206.game.SolidBox;
import nz.ac.auckland.se206.game.StoneCarving;
import nz.ac.auckland.se206.game.Wires;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

public class SecurityController extends GameController
    implements KeypadListener, WiresListener, SecurityRoomDoorListener, StoneCarvingListener {

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
  @FXML private ImageView arrow1;
  @FXML private ImageView arrow2;
  @FXML private ImageView arrow3;
  @FXML private ImageView arrow4;
  @FXML private ImageView arrow5;
  Passcode passcode = Passcode.getInstance();

  // @FXML private Text number0;
  @FXML
  private void handleClearEnter(MouseEvent event) {
    Rectangle clickRectangle = (Rectangle) event.getSource();
    if (clickRectangle == clear) {
      numbers.setText("");
      numberOfnumbers = 0;
    } else if (clickRectangle == enter) {
      checkPin();
    }
  }

  @FXML
  private void showDoorLabel() {
    doorLabel.setOpacity(1);
    interractHint.setOpacity(1);
    arrow5.toBack();
  }

  @FXML
  void hideDoorLabel() {
    doorLabel.setOpacity(0);
    interractHint.setOpacity(0);
    arrow5.toFront();
  }

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

  @FXML
  public void disableCamera() {
    cameraLine1.toBack();
    cameraLine2.toBack();
    cameraBase.toBack();
    cameraTriangle.toBack();
    boundsObjects.remove(exitBlock);
    // what you can do here is also remove obstacle preventing the player from moving into the
    // camera area
  }

  @Override
  public void initialize() {
    stoneText.setText("Discovered " + passcode.getThirdNum() + " century");
    super.initialize();
    exitBlock = new SolidBox(boundingBoxThree);
    boundsObjects.add(new SolidBox(boundingBoxOne));
    boundsObjects.add(new SolidBox(boundingBoxTwo));
    boundsObjects.add(new SolidBox(boundingBox1));
    boundsObjects.add(new SolidBox(boundingBox2));
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
    boundsObjects.add(exitBlock);
    boundsObjects.add(new Keypad(keypadBounds, this));
    boundsObjects.add(new Wires(wiresBounds, this));
    boundsObjects.add(new SecurityRoomDoor(securityDoorBounds, this));
    boundsObjects.add(new Portal(dinosaurRoomBounds, this, AppUi.DINOSAUR_ROOM));
    boundsObjects.add(new StoneCarving(stoneCarvingBounds, this));
    this.player.setBoundingBoxes(boundsObjects);
    this.player.setPosX(54);
    this.player.setPosY(300);
    applyFloatingAnimation(arrow1);
    applyFloatingAnimation(arrow2);
    applyFloatingAnimation(arrow3);
    applyFloatingAnimation(arrow4);
    applyFloatingAnimation(arrow5);
    
  }

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

  private void enterValue(String value) {
    numbers.setText(numbers.getText() + value);
    numberOfnumbers++;
  }

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

    if (pin.equals(numbers.getText())) {
      GameState.isDoorOpen = true;
      GameState.isExitDoorUnlocked = true;
      GameController.updateAllChecklists();
      correctColor.toFront();
      correctTxt.toFront();
      numbers.setOpacity(0);

      // Wait for 0.5 seconds then hide the keypad
      PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
      pause.setOnFinished(event -> hideKeyPad());
      pause.play();

    } else {
      incorrectColor.toFront();
      incorrectTxt.toFront();
      numbers.setOpacity(0);
      numbers.setText("");

      // Wait for 0.5 seconds then reset the numbers
      PauseTransition pause = new PauseTransition(Duration.seconds(1));
      pause.setOnFinished(event -> resetNumbers());
      pause.play();
    }
  }

  @FXML
  private void showWireLabel() {
    wireLabel.setOpacity(1);
    interractHint.setOpacity(1);
    arrow2.toBack();
  }

  @FXML
  private void hideWireLabel() {
    wireLabel.setOpacity(0);
    interractHint.setOpacity(0);
    arrow2.toFront();
  }

  private void resetNumbers() {
    numbers.setText("");
    numberOfnumbers = 0;
    incorrectColor.toBack();
    incorrectTxt.toBack();
    numbers.setOpacity(1);
  }

  @FXML
  private void showKeyPad() {
    blurScreen.toFront();
    keypadRectangle.toFront();

    keypad.toFront();

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
    zeroKey.toFront(); // This makes sure the numbers text is visible on the top
  }

  @FXML
  private void hideKeyPad() {
    blurScreen.toBack();
    keypadRectangle.toBack();
    numberRectangle.toBack();
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
    correctColor.setOpacity(0);
    incorrectColor.setOpacity(0);
    correctTxt.setOpacity(0);
    incorrectTxt.setOpacity(0);
    zeroKey.toBack();
  }

  @Override
  public void keypadInteracted() {
    if (!GameState.isDoorOpen) {
      showKeyPad();
    } else if (!GameState.isTreasureStolen) {
      Task<Void> task =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              disableHintChatAndExit();
              ai.runGpt(
                  new ChatMessage("user", GptPromptEngineering.getMustStealItem()), hackerTextArea);
              enableHintChatAndExit();
              return null;
            }
          };
      new Thread(task).start();
    } else {
      //hjgjhgkjgjgjgjgjhgjgjgkjgjkgjgjg
      FXMLLoader gameWonLoader = App.getFxmlLoader("gamewon");
    try {
      SceneManager.addUi(AppUi.GAME_WON, gameWonLoader.load());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    SceneManager.addController(AppUi.GAME_WON, gameWonLoader.getController());
      App.switchScenes(AppUi.GAME_WON);
    }
  }

  @Override
  public void keypadTouched() {
    showDoorLabel();
  }

  @Override
  public void keypadNotTouched() {
    hideDoorLabel();
    hideKeyPad();
  }

  @Override
  public void wiresInteracted() {
    if (SceneManager.containsUi(AppUi.WIRES_GAME)) {
      App.switchScenes(AppUi.WIRES_GAME);
    } else {
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

  @Override
  public void wiresTouched() {
    showWireLabel();
  }

  @Override
  public void wiresUntouched() {
    hideWireLabel();
  }

  @Override
  public void securityDoorInteracted() {
    pauseRoom();
    App.switchScenes(AppUi.SECURITY_ROOM);
  }

  @Override
  public void securityDoorTouched() {
    showSecurity();
  }

  @Override
  public void securityDoorUntouched() {
    hideSecurity();
  }

  @FXML
  private void showStoneCarving() {
    blurScreen.toFront();
    stoneCarving.toFront();
    stoneText.toFront();
  }

  @FXML
  private void hideStoneCarving() {
    blurScreen.toBack();
    stoneCarving.toBack();
    stoneText.toBack();
  }

  @FXML
  private void showCarvingLabel() {
    carvingLabel.setOpacity(1);
    arrow4.toBack();
  }

  @FXML
  private void hideCarvingLabel() {
    carvingLabel.setOpacity(0);
    arrow4.toFront();
  }

  @Override
  public void stoneCarvingInteracted() {
    showStoneCarving();
  }

  @Override
  public void stoneCarvingTouched() {
    showCarvingLabel();
  }

  @Override
  public void stoneCaringUntouched() {
    hideCarvingLabel();
    hideStoneCarving();
  }

  private void applyFloatingAnimation(ImageView imageView) {
    TranslateTransition translateTransition =
        new TranslateTransition(Duration.seconds(1), imageView);
    translateTransition.setByY(5);
    translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
    translateTransition.setAutoReverse(true);
    translateTransition.play();
  }
}
