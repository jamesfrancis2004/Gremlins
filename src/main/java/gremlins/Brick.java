package gremlins;
import processing.core.PImage;
import processing.core.PApplet;
public class Brick extends Block {
  private boolean isDestroyed = false;
  private PImage[] brickDestroyedImgs = new PImage[5];
  private PImage startingImage;
  private int imgIndex = 0;
  private int brickDestroyedTick = 0;

  /**Class Constructor returning a Brick type
   * Calls constructor on super block type
   * @param img PImage representing the normal brick image
   * @param position Position object representing tile location
   * @param brickDestroyed0 PImage representing first brick destroyed image
   * @param brickDestroyed1 PImage representing second brick destroyed image
   * @param brickDestroyed2 PImage representing third brick destroyed image
   * @param brickDestroyed3 PImage representing fourth brick destroyed image
   * @see Block*/
 
  public Brick(PImage img, Position position, 
               PImage brickDestroyed0, PImage brickDestroyed1,
               PImage brickDestroyed2, PImage brickDestroyed3) {
    super(img, position, false, true);
    this.brickDestroyedImgs[0] = brickDestroyed0;
    this.brickDestroyedImgs[1] = brickDestroyed1;
    this.brickDestroyedImgs[2] = brickDestroyed2;
    this.brickDestroyedImgs[3] = brickDestroyed3;
    this.brickDestroyedImgs[4] = null;
    this.startingImage = img;

    

  }

  
  /** Overrides super destroy method
   * Brick type can be destroyed so isDestroyed is set to true and object becomes passable */
  @Override
  public void destroy() {
    this.isDestroyed = true;
    super.makePassable();
    

  }

  /** Overrides super reset method
   *  Object is made non passable and image is set to starting image*/
  @Override 
  public void reset() {
    super.setImg(this.startingImage);
    this.isDestroyed = false;
    super.removePassable();
    this.imgIndex = 0;
  }

  /**Function always returns false as not isinstance of door.
   * @return False not instance Door*/
  public boolean isDoor() {
    return false;
  }

  /**Overrides the draw method
   * Calls the objects tick method and then draws it to the screen */
  @Override
  public void draw(PApplet app) {
    this.tick();
    super.draw(app);
  }
  
  /**Tick method checks whether object is destroyed
   * If destroyed the destroyed tick is iterated and if divisible by 4 the imgCount is also iterated*/
  public void tick() {
    if (this.isDestroyed) {
      if (imgIndex != 5) {
        if (brickDestroyedTick++ % 4 == 0) {
          super.setImg(brickDestroyedImgs[imgIndex++]);
        
        }
    
      }
    }

  }






}
