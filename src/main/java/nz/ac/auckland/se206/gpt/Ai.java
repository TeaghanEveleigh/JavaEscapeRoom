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

/**
 * This class represents the AI in the game. It is responsible for generating responses from the API
 * and appending them to the text area.
 */
public class Ai {

  private final TextToSpeech textToSpeech = new TextToSpeech();

  /**
   * Generates a response from API based on the prompt given to it and appends this message to the
   * text area.
   *
   * @param msg the prompt to be sent to the API.
   * @param textArea the text area to append the response to.
   * @return the response from the API.
   * @throws ApiProxyException if there is an error with the API.
   */
  public ChatMessage runGpt(ChatMessage msg, TextArea textArea) throws ApiProxyException {
    ChatCompletionRequest chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.2).setTopP(0.5).setMaxTokens(100);
    chatCompletionRequest.addMessage(msg);

    Platform.runLater(textArea::clear);

    // Play the hacking sound
    String hackingSound =
        getClass().getResource("/sounds/computer-processing-sound-effect.mp3").toString();
    Media media = new Media(hackingSound);
    MediaPlayer hackingSoundPlayer = new MediaPlayer(media);
    hackingSoundPlayer.play();

    try {
      // Get the response from the API
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      String content = result.getChatMessage().getContent();

      Platform.runLater(hackingSoundPlayer::stop);

      if (GameState.textToSpeech) { // If text to speech is enabled
        Task<Void> task1 =
            new Task<Void>() {
              @Override
              protected Void call() throws Exception {
                textToSpeech.speak(content); // Speak the response
                textToSpeech.terminate();
                return null;
              }
            };
        new Thread(task1).start();
      }

      appendTextToTextArea(content, textArea); // Append the response to the text area

      System.out.println("Done");
      return result.getChatMessage();
    } catch (ApiProxyException e) {
      e.printStackTrace();
      return null;
    } finally {
      hackingSoundPlayer.dispose(); // Ensure resources are released
    }
  }

  /**
   * This method appends text to the text area a few characters at a time. This is done to simulate
   * the text being typed out.
   *
   * @param content the text to append.
   * @param textArea the text area to append the text to.
   */
  private void appendTextToTextArea(String content, TextArea textArea) {
    StringBuilder sb = new StringBuilder();
    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            for (char letter : content.toCharArray()) {
              sb.append(letter);
              if (sb.length() % 2 == 0) { // Append every 2 characters
                final String textToAppend = sb.toString();
                Platform.runLater(
                    () -> {
                      textArea.appendText(textToAppend);
                      sb.setLength(0); // Clear the string builder
                    });
              }
              try {
                Thread.sleep(20);
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore the interrupt status
              }
            }

            if (sb.length() > 0) {
              Platform.runLater(
                  () -> textArea.appendText(sb.toString())); // Append the remaining characters
            }

            Platform.runLater(() -> textArea.appendText("\n")); // Append a new line
            return null;
          }
        };
    new Thread(task).start();
  }
}
