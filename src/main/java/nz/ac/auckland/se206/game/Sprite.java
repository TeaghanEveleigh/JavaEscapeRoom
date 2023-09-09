package nz.ac.auckland.se206.game;

import javafx.scene.image.Image;

public abstract class Sprite {
  protected Image image;
  protected int posX;
  protected int posY;
  protected int width;
  protected int height;

  public Sprite(String imagePath, int width, int height, int posX, int posY) {
    this.image =
        new Image(
            getClass().getResource(imagePath).toExternalForm(), width, height, false, false, false);
    this.width = width;
    this.height = height;
    this.posX = posX;
    this.posY = posY;
  }

  public Image getImage() {
    return this.image;
  }

  public int getPosX() {
    return posX;
  }

  public int getPosY() {
    return posY;
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  public void setPosX(int posX) {
    this.posX = posX;
  }

  public void setPosY(int posY) {
    this.posY = posY;
  }
}
