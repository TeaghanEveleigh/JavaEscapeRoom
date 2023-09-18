package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class MemoryGameController {
  private static double sequenceSeconds = 0.5;
  private static int maxSequenceLength = 4;

  @FXML private Circle lightOne;
  @FXML private Circle lightTwo;
  @FXML private Circle lightThree;
  @FXML private Circle lightFour;
  @FXML private Circle lightFive;
  @FXML private Circle lightSix;
  @FXML private Circle lightEight;
  @FXML private Circle lightSeven;
  @FXML private Circle lightNine;

  private ArrayList<Circle> lights;
  private ArrayList<Circle> sequence;
  private ArrayList<Circle> lightsPressed;
  private Color baseColor = Color.AQUAMARINE;
  private boolean playerWon;
  private boolean showingSequence = false;
  private int currentSequenceLength = 1;

  @FXML
  public void initialize() {
    this.lights = new ArrayList<Circle>();
    this.sequence = new ArrayList<Circle>();
    this.lightsPressed = new ArrayList<Circle>();

    lights.add(lightOne);
    lights.add(lightTwo);
    lights.add(lightThree);
    lights.add(lightFour);
    lights.add(lightFive);
    lights.add(lightSix);
    lights.add(lightSeven);
    lights.add(lightEight);
    lights.add(lightNine);

    resetLights();
    ChooseSequence(4);
    showSequence(currentSequenceLength);
  }

  private void ChooseSequence(int sequenceLength) {
    Random random = new Random();
    for (int i = 0; i < sequenceLength; i++) {
      Circle chosen = lights.get(random.nextInt(lights.size()));
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
    ArrayList<Circle> lightsQueue = new ArrayList<Circle>(sequence);
    System.out.println(lightsQueue.size());
    for (int i = 0; i < sequenceLength; i++) {
      Circle light = lightsQueue.get(i);
      timeline
          .getKeyFrames()
          .add(
              new KeyFrame(
                  Duration.seconds((i + 1) * sequenceSeconds),
                  e -> {
                    setLight(light, Color.YELLOW);
                  }));
      timeline
          .getKeyFrames()
          .add(
              new KeyFrame(
                  Duration.seconds((i + 2) * sequenceSeconds),
                  e -> {
                    setLight(light, baseColor);
                  }));
    }
    timeline.play();
    timeline.setOnFinished(
        e -> {
          showingSequence = false;
        });
  }

  private void setLight(Circle light, Color color) {
    light.setFill(color);
  }

  private void setAllLights(Color color) {
    System.out.println(color);
    for (Circle light : this.lights) {
      light.setFill(color);
    }
  }

  private void resetLights() {
    setAllLights(baseColor);
  }

  private void checkSequence() {
    // Disable user input
    showingSequence = true;

    for (int i = 0; i < currentSequenceLength; i++) {
      if (!lightsPressed.get(i).equals(sequence.get(i))) {
        Timeline timeline = flashLights(Color.RED);
        timeline.setOnFinished(e -> showSequence(currentSequenceLength));
        return;
      }
    }

    Timeline timeline = flashLights(Color.GREEN);
    if (currentSequenceLength++ >= maxSequenceLength) {
      playerWon = true;
      System.out.println("WON");
    } else {
      timeline.setOnFinished(e -> showSequence(currentSequenceLength));
    }
  }

  private Timeline flashLights(Color color) {

    Timeline timeline = new Timeline();

    // Create a keyframe to set the lights to the specified color
    KeyFrame colorFrame = new KeyFrame(Duration.seconds(0.1), e -> setAllLights(color));

    // Create a keyframe to reset the lights to the base color
    KeyFrame resetFrame = new KeyFrame(Duration.seconds(0.2), e -> setAllLights(baseColor));

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

    Circle pressed = (Circle) event.getSource();
    pressed.setFill(Color.PINK);
    lightsPressed.add(pressed);
    System.out.println("size " + lightsPressed.size());
    System.out.println("seq leng " + currentSequenceLength);
    if (lightsPressed.size() == currentSequenceLength) {
      checkSequence();
      lightsPressed.clear();
    }
  }
}
