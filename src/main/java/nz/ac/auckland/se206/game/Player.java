package nz.ac.auckland.se206.game;

public class Player extends Sprite {

  public static final String imagePath = "/images/player.png";

  public Player(int width, int height, int posX, int posY) {
    super(imagePath, width, height, posX, posY);
  }
}
