package gremlins;

import processing.core.PImage;
import processing.core.PApplet;

public class Projectile {
  private final int velocity = 4;
  private PImage projectileImg;
  protected Position position;
  private final int SPRITESIZE = 20;
  private Direction direction;
  protected Map mapObject;
  private boolean isSuperPower;


  /** Class constructor for projectile
   * @param img PImage represents the image for the projectile 
   * @param position Position object representing current position of projectile
   * @param direction Direction object representing Direction projectile is moving in
   * @param mapObject a reference to the map, allows it to call all of Map's public methods*/
  public Projectile(PImage img, Position position, Direction direction, Map mapObject) {
    this.projectileImg = img;
    this.position = position;
    this.direction = direction;
    this.mapObject = mapObject;
    this.isSuperPower = false;
  }
  

  /** If a projectile is superpower this is set
   * @param isSuperPower boolean value representing if is superpower */
  public void setIsSuperPower(boolean isSuperPower) {
    this.isSuperPower = isSuperPower;
  }

  /**Determines if projectile is superpower
   * @return true if superpower else return false*/
  public boolean isSuperPower() {
    return this.isSuperPower;
  }

  
  /** Draws the projectile to the screen
   * @param app uses app.draw method to draw to screen*/
  public void draw(PApplet app) {
    app.image(this.projectileImg, this.position.x, this.position.y);

  }

  /**Gets the projectile offset off the next block
   * Array at index 0 represents x coord and index 1 represents y coord 
   * @return int[] Array representing x and y directionional offsets*/
  protected int[] getProjectileOffset() {
    int xOffset = 0;
    int yOffset = 0;

    switch (this.direction) {
      case LEFT:
        xOffset = -20;
        break;

      case RIGHT:
        xOffset = 20;
        break;

      case UP:
        yOffset = -20;
        break;

      case DOWN:
        yOffset = 20;
        break;

      default:
        break;
    }
    return new int[] {xOffset, yOffset};
  }

  

  /**Moves the projectile in direction
   * @return false if projectile can't move so it is deleted. otherwise true*/
  public boolean move() {
    switch (this.direction) {
      case LEFT:
        if (this.mapObject.checkValidMove(this.position.x + (this.position.x % 20) - SPRITESIZE,
                this.position.y)) {
          this.position.x -= velocity;
          return true;
        }
        return false;
      case RIGHT:
        if (this.mapObject.checkValidMove(this.position.x + SPRITESIZE, this.position.y)) {
          this.position.x += velocity;
          return true;
        }
        return false;
      case UP:
        if (this.mapObject.checkValidMove(this.position.x,
                this.position.y + (this.position.y % 20) - SPRITESIZE)) {
          this.position.y -= velocity;
          return true;
        }
        return false;
      case DOWN:
        if (this.mapObject.checkValidMove(this.position.x, this.position.y + SPRITESIZE)) {
          this.position.y += velocity;
          return true;
        }
        return false;
      default:
        return false;
    }
  }
  
  /**Tick function moves the projectile each frame */
  public boolean tick() {
    if (!this.move()) {
      return false;
    }
    return true;

  }
  
  /**Gets the x tile the projectile is currently on
   * @return int of x tile projectile is on */
  public int getX() {
    return this.position.x / SPRITESIZE;
  }

  /**Gets the y tile the projectile is currently on
   * @return int of y tile projectile is on */
  public int getY() {
    return this.position.y / SPRITESIZE;
  }

}
