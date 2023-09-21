package nz.ac.auckland.se206.gpt;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class Ai {

  private static ChatCompletionRequest chatCompletionRequest =
      new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(100);

  /**
   * Generates a response from API based on the prompt given to it and appends this message to the
   * text area
   *
   * @param msg the prompt to be sent to the API
   * @param textArea the text area to append the response to
   * @return the response from the API
   * @throws ApiProxyException
   */
  public static ChatMessage runGpt(ChatMessage msg, TextArea textArea) throws ApiProxyException {
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
                  Platform.runLater(
                      () -> {
                        textArea.appendText(textToAppend);
                        sb.setLength(0);
                      });
                }
                try {
                  Thread.sleep(50);
                } catch (InterruptedException e) {
                  // handle interruption if needed
                }
              }
              if (sb.length() > 0) {
                Platform.runLater(() -> textArea.appendText(sb.toString()));
              }
              Platform.runLater(() -> textArea.appendText("\n\n"));
              return null;
            }
          };
      new Thread(task).start();
      System.out.println("Done");

      return result.getChatMessage();
    } catch (ApiProxyException e) {
      // TODO handle exception appropriately
      e.printStackTrace();
      return null;
    }
  }
}
