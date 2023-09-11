package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

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

  @FXML
  public void initialize() {
    this.lights = new ArrayList<Circle>();
    this.sequence = new HashSet<Circle>();

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
}
