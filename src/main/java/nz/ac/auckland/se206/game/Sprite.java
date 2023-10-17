package nz.ac.auckland.se206.game;

import javafx.scene.image.Image;

/** This class represents a sprite in the game. Each sprite in the game extends this class. */
public abstract class Sprite {
  protected Image image;
  protected int posX;
  protected int posY;
  protected int width;
  protected int height;

  /**
   * This constructor creates a new sprite in the game.
   *
   * @param imagePath The path to the image of the sprite.
   * @param width The width of the sprite.
   * @param height The height of the sprite.
   * @param posX The x position of the sprite.
   * @param posY The y position of the sprite.
   */
  public Sprite(String imagePath, int width, int height, int posX, int posY) {
    this.image =
        new Image(
            getClass().getResource(imagePath).toExternalForm(), width, height, false, false, false);
    this.width = width;
    this.height = height;
    this.posX = posX;
    this.posY = posY;
  }

  /**
   * This method is used to get the image of the sprite.
   *
   * @return the image of the sprite.
   */
  public Image getImage() {
    return this.image;
  }

  /**
   * This method is used to get the x position of the sprite.
   *
   * @return the x position of the sprite.
   */
  public int getPosX() {
    return posX;
  }

  /**
   * This method is used to get the y position of the sprite.
   *
   * @return the y position of the sprite.
   */
  public int getPosY() {
    return posY;
  }

  /**
   * This method is used to get the width of the sprite.
   *
   * @return the width of the sprite.
   */
  public int getWidth() {
    return this.width;
  }

  /**
   * This method is used to get the height of the sprite.
   *
   * @return the height of the sprite.
   */
  public int getHeight() {
    return this.height;
  }

  /**
   * This method sets the x position of the sprite.
   *
   * @param posX The x position of the sprite to set.
   */
  public void setPosX(int posX) {
    this.posX = posX;
  }

  /**
   * This method sets the y position of the sprite.
   *
   * @param posY The y position of the sprite to set.
   */
  public void setPosY(int posY) {
    this.posY = posY;
  }
}
