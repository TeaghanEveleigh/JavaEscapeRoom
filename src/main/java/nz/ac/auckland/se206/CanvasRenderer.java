package nz.ac.auckland.se206;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import nz.ac.auckland.se206.game.Sprite;

/** This class is used to render entities to a canvas. */
public class CanvasRenderer {

  private Set<Sprite> entities;
  private Canvas canvas;
  private GraphicsContext graphicsContext;
  private Image backgroundImage;

  /**
   * This constructor creates a new CanvasRenderer with the given canvas and graphics context.
   *
   * @param canvas The canvas to render to.
   * @param context The graphics context to render with.
   */
  public CanvasRenderer(Canvas canvas, GraphicsContext context) {
    this.entities = new HashSet<Sprite>();
    this.canvas = canvas;
    this.graphicsContext = context;
  }

  /**
   * This method adds an entity to the renderer.
   *
   * @param entity The entity to add.
   */
  public void addEntity(Sprite entity) {
    entities.add(entity);
  }

  /**
   * This method removes an entity from the renderer.
   *
   * @param entity The entity to remove.
   */
  public void removeEntity(Sprite entity) {
    entities.remove(entity);
  }

  /** This method clears all entities from the renderer. */
  public void clearAllEntities() {
    entities.clear();
  }

  /** This method renders all entities to the canvas. */
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

      graphicsContext.drawImage(image, posX, posY, width, height); // Draw the entity
    }
  }

  /** This method sets the background image. */
  public void setBackground(Image backgroundImage) {
    this.backgroundImage = backgroundImage;
  }

  /** This method clears the background image. */
  public void clearBackground() {
    this.backgroundImage = null;
  }
}
