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
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

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

  @FXML
  private void onHackerExitClicked() {
    disableHackerPanel();
    disableChat();
  }

  @FXML
  public void onTalkToHackerPressed() {
    enableHackerPanel();
  }

  @FXML
  public void onChatPressed() {
    enableChat();
  }

  @FXML
  private void onChatExitClicked() {
    disableChat();
  }

  @FXML
  private void onSubmitPressed() {
    String message = chatTextField.getText();
    chatTextField.clear();
    if (message.length() > 0) {
      disableHintChatAndExit();
      try {
        ai.runGpt(
            new ChatMessage("user", GptPromptEngineering.getChatResponse(message)), hackerTextArea);
      } catch (ApiProxyException e) {
        e.printStackTrace();
      }
      enableHintChatAndExit();
    }
  }

  @FXML
  protected void onHintPressed() {
    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            disableHintChatAndExit();
            GameState gameState = GameState.getInstance();
            if (GameState.isHard || (Integer.parseInt(GameState.getHintsLeft()) <= 0)) {
              ai.runGpt(
                  new ChatMessage("user", GptPromptEngineering.getCantGiveHint()), hackerTextArea);
              enableHintChatAndExit();
              return null;
            }
            if (!GameState.isLasersDisabled && !GameState.isCamerasDisabled) {
              ai.runGpt(
                  new ChatMessage("user", GptPromptEngineering.getNothingDisabledHint()),
                  hackerTextArea);
            } else if (GameState.isLasersDisabled) {
              if (!GameState.isTreasureStolen) {
                ai.runGpt(
                    new ChatMessage(
                        "user", GptPromptEngineering.getLasersDisabledButNotStolenHint()),
                    hackerTextArea);
              } else if (GameState.isCamerasDisabled) {
                ai.runGpt(
                    new ChatMessage("user", GptPromptEngineering.getBothDisabledHint()),
                    hackerTextArea);
              } else if (!GameState.isCamerasDisabled) {
                ai.runGpt(
                    new ChatMessage("user", GptPromptEngineering.getLasersButNotCameraHint()),
                    hackerTextArea);
              }
            } else if (GameState.isCamerasDisabled && !GameState.isLasersDisabled) {
              ai.runGpt(
                  new ChatMessage("user", GptPromptEngineering.getCameraButNotLasersHint()),
                  hackerTextArea);
            }
            enableHintChatAndExit();
            if (GameState.isMedium) {
              gameState.subtractHint();
            }
            return null;
          }
        };
    new Thread(task).start();
  }

  // Disables the hacker panel
  public void disableHackerPanel() {
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

  // Disables the hint and exit buttons
  public void disableHintChatAndExit() {
    hintButton.setDisable(true);
    chatButton.setDisable(true);
    exitHackerPanelImage.setDisable(true);
  }

  // Enables the hint and exit buttons
  public void enableHintChatAndExit() {
    hintButton.setDisable(false);
    chatButton.setDisable(false);
    exitHackerPanelImage.setDisable(false);
  }

  // Enables the hacker panel
  public void enableHackerPanel() {
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

  // Enables the chat panel
  public void enableChat() {
    chatPanelBackground.toFront();
    submitButton.toFront();
    submitButton.setDisable(false);
    chatTextField.toFront();
    exitChatImage.toFront();
    exitChatImage.setDisable(false);
  }

  // Disables the chat panel
  public void disableChat() {
    chatPanelBackground.toBack();
    submitButton.toBack();
    submitButton.setDisable(true);
    chatTextField.toBack();
    exitChatImage.toBack();
    exitChatImage.setDisable(true);
  }
}
