package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class MemoryGameController {
  @FXML private Circle lightOne;
  @FXML private Circle lightTwo;
  @FXML private Circle lightThree;
  @FXML private Circle lightFour;
  @FXML private Circle lightFive;
  @FXML private Circle lightSix;
  @FXML private Circle lightSeven;
  @FXML private Circle lightEight;
  @FXML private Circle lightNine;

  private ArrayList<Circle> lights;
  private HashSet<Circle> sequence;
  private ArrayList<Circle> lightsPressed;

  @FXML
  public void initialize() {
    this.lights = new ArrayList<Circle>();
    this.sequence = new HashSet<Circle>();
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

    ChooseSequence(6);
    showLights();
  }

  private void ChooseSequence(int sequenceLength) {
    Random random = new Random();
    for (int i = 0; i < sequenceLength; i++) {
      Circle chosen = lights.get(random.nextInt(lights.size()));
      while (sequence.contains(chosen)) {
        chosen = lights.get(random.nextInt(lights.size()));
      }
      sequence.add(chosen);
      chosen.setFill(Color.YELLOW);
    }
  }

  private void showLights() {
    Timeline timeline = new Timeline();
    timeline.setCycleCount(sequence.size());
    ArrayList<Circle> lightsQueue = new ArrayList<Circle>(sequence);
    KeyFrame frame =
        new KeyFrame(
            Duration.seconds(1),
            new EventHandler<ActionEvent>() {

              Circle currentLight = null;

              @Override
              public void handle(ActionEvent event) {
                if (currentLight != null) {
                  currentLight.setFill(Color.BLUE);
                }
                Circle light = lightsQueue.remove(0);
                light.setFill(Color.GREEN);
                currentLight = light;
              }
            });

    timeline.getKeyFrames().add(frame);
    timeline.play();
  }

  @FXML
  private void onLightPressed(MouseEvent event) throws IOException {
    lightsPressed.add((Circle) event.getSource());
    Circle pressed = (Circle) event.getSource();
    pressed.setFill(Color.PINK);
  }
}
