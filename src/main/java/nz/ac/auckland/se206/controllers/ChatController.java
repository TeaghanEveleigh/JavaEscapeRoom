package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class ChatController {
  @FXML private TextArea chatTextArea;
  @FXML private TextField inputText;
  @FXML private Button sendButton;

  private ChatCompletionRequest chatCompletionRequest;

  @FXML
  public void initialize() throws ApiProxyException {
    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(100);
  }

  private void appendChatMessage(ChatMessage msg) {
    chatTextArea.appendText(msg.getRole() + ": " + msg.getContent() + "\n\n");
  }

  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    chatCompletionRequest.addMessage(msg);
    try {
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      String content = result.getChatMessage().getContent();

      StringBuilder sb = new StringBuilder();
      Task<Void> task =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              for (char letter : content.toCharArray()) {
                sb.append(letter);
                if (sb.length() % 2 == 0) {
                  final String textToAppend = sb.toString();
                  Platform.runLater(() -> chatTextArea.appendText(textToAppend));
                  sb.setLength(0);
                }
                try {
                  Thread.sleep(50);
                } catch (InterruptedException e) {
                  // handle interruption if needed
                }
              }
              if (sb.length() > 0) {
                Platform.runLater(() -> chatTextArea.appendText(sb.toString()));
              }
              Platform.runLater(() -> chatTextArea.appendText("\n\n"));
              return null;
            }
          };
      new Thread(task).start();

      return result.getChatMessage();
    } catch (ApiProxyException e) {
      // TODO handle exception appropriately
      e.printStackTrace();
      return null;
    }
  }

  @FXML
  private void onSendMessage(ActionEvent event) {
    // Start new thread to handle the message sending and processing
    new Thread(
            () -> {
              try {
                String message = inputText.getText();
                if (message.trim().isEmpty()) {
                  return;
                }

                // Clear input text on the UI thread
                Platform.runLater(() -> inputText.clear());

                ChatMessage msg = new ChatMessage("user", message);

                // Append chat message on the UI thread
                Platform.runLater(() -> appendChatMessage(msg));

                ChatMessage lastMsg = runGpt(msg);

                // Update UI based on response from the assistant
                Platform.runLater(
                    () -> {
                      if (lastMsg.getRole().equals("assistant")
                          && lastMsg.getContent().startsWith("Correct")) {
                        GameState.isRiddleResolved = true;
                      }
                    });
              } catch (ApiProxyException e) {
                e.printStackTrace();
                // TODO handle exception appropriately
              }
            })
        .start();
  }

  @FXML
  private void onGoBack(ActionEvent event) throws ApiProxyException, IOException {}
}
