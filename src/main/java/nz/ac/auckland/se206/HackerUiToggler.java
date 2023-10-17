package nz.ac.auckland.se206;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.gpt.Ai;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;

/**
 * This class is used to toggle the hacker UI. The main rooms, as well as the wires game and memory
 * game, extend this class since they use the same hacker UI.
 */
public abstract class HackerUiToggler {

  @FXML protected Button hintButton;
  @FXML protected Button chatButton;
  @FXML protected Button talkToHackerButton;
  protected Ai ai = new Ai();

  // Hacker panel
  @FXML protected Rectangle hackerPanelBackground;
  @FXML protected ImageView exitHackerPanelImage;
  @FXML protected ImageView hackerIcon;
  @FXML protected TextArea hackerTextArea;
  @FXML protected Label mainTimerLabel;
  @FXML private Label hintsLabel;
  @FXML protected TextField chatTextField;
  @FXML protected ImageView exitChatImage;
  @FXML protected Button submitButton;
  @FXML protected Rectangle chatPanelBackground;
  @FXML protected Button objectivesButton;

  /** This method runs when the user wants to exit the hacker panel. */
  @FXML
  private void onHackerExitClicked() {
    disableHackerPanel();
    disableChat();
  }

  /** This method runs when the user wants to ask for a hint or chat to the hacker. */
  @FXML
  public void onTalkToHackerPressed() {
    enableHackerPanel();
  }

  /** This method runs when the user wants to chat to the hacker. */
  @FXML
  public void onChatPressed() {
    enableChat();
  }

  /** This method runs when the user wants to exit the chat panel. */
  @FXML
  private void onChatExitClicked() {
    disableChat();
  }

  /** This method runs when the user wants to submit a message to the hacker. */
  @FXML
  private void onSubmitPressed() {
    String message = chatTextField.getText();
    chatTextField.clear(); // Clear the text field
    if (message.length() > 0) {
      disableHintChatAndExit();
      Task<Void> task =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              disableHintChatAndExit();
              // Tells the user they have solved the wires riddle and completed the game
              // Gives the response to the user's message
              ai.runGpt(
                  new ChatMessage("user", GptPromptEngineering.getChatResponse(message)),
                  hackerTextArea);
              enableHintChatAndExit();
              return null;
            }
          };
      new Thread(task).start();
      enableHintChatAndExit();
    }
  }

  /** This method runs when the user wants to ask for a hint. */
  @FXML
  protected void onHintPressed() {
    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            disableHintChatAndExit(); // Disable the hint and exit buttons
            GameState gameState = GameState.getInstance();
            if (GameState.isHard || (Integer.parseInt(GameState.getHintsLeft()) <= 0)) {
              // Tell the user they can't get a hint
              ai.runGpt(
                  new ChatMessage("user", GptPromptEngineering.getCantGiveHint()), hackerTextArea);
              enableHintChatAndExit(); // Enable the hint and exit buttons
              return null;
            }
            if (!GameState.isLasersDisabled
                && !GameState.isCamerasDisabled) { // If nothing is disabled
              ai.runGpt(
                  new ChatMessage("user", GptPromptEngineering.getNothingDisabledHint()),
                  hackerTextArea);
            } else if (GameState.isLasersDisabled) { // If the lasers are disabled
              if (!GameState.isTreasureStolen) { // If the treasure hasn't been stolen
                ai.runGpt(
                    new ChatMessage(
                        "user", GptPromptEngineering.getLasersDisabledButNotStolenHint()),
                    hackerTextArea);
              } else if (GameState.isCamerasDisabled) { // If the cameras are disabled
                ai.runGpt(
                    new ChatMessage("user", GptPromptEngineering.getBothDisabledHint()),
                    hackerTextArea);
              } else if (!GameState.isCamerasDisabled) { // If the cameras aren't disabled
                ai.runGpt(
                    new ChatMessage("user", GptPromptEngineering.getLasersButNotCameraHint()),
                    hackerTextArea);
              }
            } else if (GameState.isCamerasDisabled
                && !GameState.isLasersDisabled) { // If the cameras are disabled but not the lasers
              ai.runGpt(
                  new ChatMessage("user", GptPromptEngineering.getCameraButNotLasersHint()),
                  hackerTextArea);
            }
            enableHintChatAndExit(); // Enable the hint and exit buttons
            if (GameState.isMedium) {
              gameState.subtractHint(); // Subtract a hint
            }
            return null;
          }
        };
    new Thread(task).start();
  }

  /** This method runs when the user wants to exit from the hacker panel. */
  public void disableHackerPanel() {
    // Sends all hacker panel items to the back
    hackerPanelBackground.toBack();
    hintButton.toBack();
    chatButton.toBack();
    hackerIcon.toBack();
    hintsLabel.toBack();
    hackerTextArea.toBack();
    exitHackerPanelImage.toBack();
    exitHackerPanelImage.setDisable(true);
    talkToHackerButton.setDisable(false);
  }

  /**
   * This method disables the hint, chat and exit buttons when the user is chatting to the hacker.
   */
  public void disableHintChatAndExit() {
    hintButton.setDisable(true);
    chatButton.setDisable(true);
    exitHackerPanelImage.setDisable(true);
  }

  /**
   * This method enables the hint, chat and exit buttons when the user is done chatting to the
   * hacker.
   */
  public void enableHintChatAndExit() {
    hintButton.setDisable(false);
    chatButton.setDisable(false);
    exitHackerPanelImage.setDisable(false);
  }

  /** This method enables the hacker panel when the user wants to interact with the hacker. */
  public void enableHackerPanel() {
    // Sends all hacker panel items to the front
    hackerPanelBackground.toFront();
    hintButton.toFront();
    chatButton.toFront();
    hackerIcon.toFront();
    hintsLabel.toFront();
    hackerTextArea.toFront();
    exitHackerPanelImage.toFront();
    exitHackerPanelImage.setDisable(false);
    talkToHackerButton.setDisable(true);
  }

  /** This method enables the chat panel when the user wants to chat to the hacker. */
  public void enableChat() {
    // Brings the chat panel to the front
    chatPanelBackground.toFront();
    submitButton.toFront();
    submitButton.setDisable(false);
    chatTextField.toFront();
    exitChatImage.toFront();
    exitChatImage.setDisable(false);
  }

  /** This method disables the chat panel when the user wants to exit the chat panel. */
  public void disableChat() {
    // Sends all chat panel items to the back
    chatPanelBackground.toBack();
    submitButton.toBack();
    submitButton.setDisable(true);
    chatTextField.toBack();
    exitChatImage.toBack();
    exitChatImage.setDisable(true);
  }
}
