package nz.ac.auckland.se206.gpt;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;
import nz.ac.auckland.se206.speech.TextToSpeech;

public class Ai {

  private static TextToSpeech textToSpeech = new TextToSpeech();

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
  public ChatMessage runGpt(ChatMessage msg, TextArea textArea) throws ApiProxyException {
    chatCompletionRequest.addMessage(msg);
    textArea.clear();
    String hackingSound =
        getClass().getResource("/sounds/computer-processing-sound-effect.mp3").toString();
    Media media = new Media(hackingSound);
    MediaPlayer hackingSoundPlayer = new MediaPlayer(media);
    hackingSoundPlayer.play();
    try {
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      String content = result.getChatMessage().getContent();
      hackingSoundPlayer.stop();
      if (GameState.textToSpeech) {
        Task<Void> task1 =
            new Task<Void>() {
              @Override
              protected Void call() throws Exception {
                textToSpeech.speak(content);
                textToSpeech.terminate();
                return null;
              }
            };
        new Thread(task1).start();
      }

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
              Platform.runLater(() -> textArea.appendText("\n"));
              return null;
            }
          };
      new Thread(task).start();
      System.out.println("Done");
      return result.getChatMessage();
    } catch (ApiProxyException e) {
      e.printStackTrace();
      return null;
    }
  }
}
