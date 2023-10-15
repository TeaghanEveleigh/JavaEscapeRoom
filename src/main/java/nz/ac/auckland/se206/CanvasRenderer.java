package nz.ac.auckland.se206;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import nz.ac.auckland.se206.game.Sprite;

public class CanvasRenderer {

  private Set<Sprite> entities;
  private Canvas canvas;
  private GraphicsContext graphicsContext;
  private Image backgroundImage;

  public CanvasRenderer(Canvas canvas, GraphicsContext context) {
    this.entities = new HashSet<Sprite>();
    this.canvas = canvas;
    this.graphicsContext = context;
  }

  public void addEntity(Sprite entity) {
    entities.add(entity);
  }

  public void removeEntity(Sprite entity) {
    entities.remove(entity);
  }

  public void clearAllEntities() {
    entities.clear();
  }

  public void renderEntities() {
    // Clear the canvas
    graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    if (this.backgroundImage != null) {
      graphicsContext.drawImage(backgroundImage, 0, 0);
    }

    // Loop over entities and draw them to the canvas
    for (Sprite entity : entities) {
      Image image = entity.getImage();
      int posX = entity.getPosX();
      int posY = entity.getPosY();
      double width = entity.getHeight();
      double height = entity.getHeight();

      graphicsContext.drawImage(image, posX, posY, width, height);
    }
  }

  public void setBackground(Image backgroundImage) {
    this.backgroundImage = backgroundImage;
  }

  public void clearBackground() {
    this.backgroundImage = null;
  }
}
