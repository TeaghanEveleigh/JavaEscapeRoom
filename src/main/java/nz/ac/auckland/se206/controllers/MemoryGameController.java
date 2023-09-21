package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class MemoryGameController {
  private static double sequenceSeconds = 0.5;
  private static int maxSequenceLength = 6;

  @FXML private ImageView lightOne;
  @FXML private ImageView lightTwo;
  @FXML private ImageView lightThree;
  @FXML private ImageView lightFour;
  @FXML private ImageView lightFive;
  @FXML private ImageView lightSix;
  @FXML private ImageView lightEight;
  @FXML private ImageView lightSeven;
  @FXML private ImageView lightNine;
  @FXML private ImageView lightTen;

  private ArrayList<ImageView> lights;
  private ArrayList<ImageView> sequence;
  private ArrayList<ImageView> lightsPressed;
  private Image baseLightImage =
      new Image(getClass().getResource("/images/push-button.png").toExternalForm());
  private Image lightPressedImage =
      new Image(getClass().getResource("/images/push-button-glow.png").toExternalForm());
  private Image lightGlowRedImage =
      new Image(getClass().getResource("/images/push-button-glow-red.png").toExternalForm());
  private Image lightGlowGreenImage =
      new Image(getClass().getResource("/images/push-button-glow-green.png").toExternalForm());

  private boolean playerWon;
  private boolean showingSequence = false;
  private int currentSequenceLength = 1;

  @FXML
  public void initialize() {
    this.lights = new ArrayList<ImageView>();
    this.sequence = new ArrayList<ImageView>();
    this.lightsPressed = new ArrayList<ImageView>();

    lights.add(lightOne);
    lights.add(lightTwo);
    lights.add(lightThree);
    lights.add(lightFour);
    lights.add(lightFive);
    lights.add(lightSix);
    lights.add(lightSeven);
    lights.add(lightEight);
    lights.add(lightNine);
    lights.add(lightTen);

    resetAllLights();
    ChooseSequence(6);
    showSequence(currentSequenceLength);
  }

  private void ChooseSequence(int sequenceLength) {
    Random random = new Random();
    for (int i = 0; i < sequenceLength; i++) {
      ImageView chosen = lights.get(random.nextInt(lights.size()));
      while (sequence.contains(chosen)) {
        chosen = lights.get(random.nextInt(lights.size()));
      }
      sequence.add(chosen);
    }
  }

  private void showSequence(int sequenceLength) {
    showingSequence = true;
    Timeline timeline = new Timeline();
    timeline.setCycleCount(1);
    ArrayList<ImageView> lightsQueue = new ArrayList<ImageView>(sequence);
    System.out.println(lightsQueue.size());
    for (int i = 0; i < sequenceLength; i++) {
      ImageView light = lightsQueue.get(i);
      timeline
          .getKeyFrames()
          .add(
              new KeyFrame(
                  Duration.seconds((i + 1) * sequenceSeconds),
                  e -> {
                    setLight(light, lightPressedImage);
                  }));
      timeline
          .getKeyFrames()
          .add(
              new KeyFrame(
                  Duration.seconds((i + 2) * sequenceSeconds),
                  e -> {
                    resetLight(light);
                  }));
    }
    timeline.play();
    timeline.setOnFinished(
        e -> {
          showingSequence = false;
        });
  }

  private void setLight(ImageView light, Image lightImage) {
    light.setImage(lightImage);
  }

  private void resetLight(ImageView light) {
    light.setImage(baseLightImage);
  }

  private void setAllLights(Image lightImage) {
    for (ImageView light : this.lights) {
      setLight(light, lightImage);
    }
  }

  private void resetAllLights() {
    for (ImageView light : this.lights) {
      resetLight(light);
    }
  }

  private void checkSequence() {
    // Disable user input
    showingSequence = true;

    for (int i = 0; i < currentSequenceLength; i++) {
      if (!lightsPressed.get(i).equals(sequence.get(i))) {
        Timeline timeline = flashLights(lightGlowRedImage);
        timeline.setOnFinished(e -> showSequence(currentSequenceLength));
        return;
      }
    }

    Timeline timeline = flashLights(lightGlowGreenImage);
    if (currentSequenceLength++ >= maxSequenceLength) {
      playerWon = true;
      System.out.println("WON");
    } else {
      timeline.setOnFinished(e -> showSequence(currentSequenceLength));
    }
  }

  private Timeline flashLights(Image lightImage) {

    Timeline timeline = new Timeline();

    // Create a keyframe to set the lights to the specified color
    KeyFrame colorFrame = new KeyFrame(Duration.seconds(0.1), e -> setAllLights(lightImage));

    // Create a keyframe to reset the lights to the base color
    KeyFrame resetFrame = new KeyFrame(Duration.seconds(0.2), e -> resetAllLights());

    // Add the keyframes to the timeline
    timeline.getKeyFrames().addAll(colorFrame, resetFrame);

    // Set the cycle count to control the number of times lights flash
    timeline.setCycleCount(2); // Adjust the number of flashes as needed

    // Play the timeline
    timeline.play();

    return timeline;
  }

  @FXML
  private void onLightPressed(MouseEvent event) throws IOException {
    if (showingSequence) return;

    ImageView pressed = (ImageView) event.getSource();
    setLight(pressed, lightPressedImage);
    lightsPressed.add(pressed);
    System.out.println("size " + lightsPressed.size());
    System.out.println("seq leng " + currentSequenceLength);
    if (lightsPressed.size() == currentSequenceLength) {
      checkSequence();
      lightsPressed.clear();
    }
  }
}
