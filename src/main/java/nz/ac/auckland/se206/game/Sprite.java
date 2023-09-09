package nz.ac.auckland.se206.game;

import javafx.scene.image.Image;

public abstract class Sprite {
  protected Image image;
  protected int posX;
  protected int posY;

  public Sprite(String imagePath, int width, int height, int posX, int posY) {
    this.image =
        new Image(
            getClass().getResource(imagePath).toExternalForm(), width, height, false, false, false);
  }

  public int getPosX() {
    return posX;
  }

  public int getPosY() {
    return posY;
  }

  public void setPosX(int posX) {
    this.posX = posX;
  }

  public void setPosY(int posY) {
    this.posY = posY;
  }
}
