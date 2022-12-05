package gremlins;
import processing.core.PImage;
import processing.core.PApplet;
import java.util.ArrayList;



public abstract class Sprite {
  protected Position position;
  private Position startingPosition;
  protected final int SPRITESIZE = 20;
  private PImage spriteImg;
  protected Direction direction;
  protected Map mapObject;
  private int velocity;
  private boolean isMoving;
  private boolean finishedMoving;

  
  /**Constructor which returns Sprite type
   * @param img PImage represents the image of the sprite
   * @param mapObject Refererence to object of type Map allows all its public methods to be called
   * @param velocity int type, velocity in pixels of object */

  public Sprite(PImage img, Map mapObject, int velocity) {
    this.spriteImg = img;
    this.mapObject = mapObject;
    this.direction = Direction.LEFT;
    this.velocity = velocity;
    this.isMoving = false;
    this.finishedMoving = true;
  }

  /** Resets the position of the sprite to its starting position */
  protected void resetPosition() {
    this.position = this.startingPosition.copy();
  }

  /** Sets the starting position of sprite
   * Multiples x and y coord by spritesize to give absolute position
   * @param position The position to set the starting position to*/

  public void setStartingPosition(Position position) {
    this.startingPosition = position;
    this.startingPosition.x *= SPRITESIZE;
    this.startingPosition.y *= SPRITESIZE;

  }
  
  /** Sets the position of a sprite
   * Multiplies the x and y coord by spritesize to give absolute position
   * @param position The position to set the sprites position to*/
  public void setPosition(Position newPosition) {
    this.position = newPosition;
    this.position.x *= SPRITESIZE;
    this.position.y *= SPRITESIZE;
    this.isMoving = false;
    this.finishedMoving = true;
  }

  /** Abstract method reset defined in child classes */
  public abstract void reset();
  

  /** sets the sprite img to a new image
   * @param sprite newimage to set spriteImg to*/
  protected void setSprite(PImage sprite) {
    this.spriteImg = sprite;
  }

  /** Tick method for sprite
   * The tick method is responsible for sprite movement
   * If isMoving is true then the sprite is moved in its direction
   * IF finishedMoving is false then the sprite is moved until it reaches next whole tile 
   * Otherwise function does not move sprite*/
  public void tick() {
    if (this.position.x % SPRITESIZE == 0 && this.position.y % SPRITESIZE == 0) {
      if (!this.isMoveValid()) {
        this.finishedMoving = true;
        this.isMoving = false;
      }
    }
    if (!this.finishedMoving) {
      switch (this.direction) {
        case LEFT:
          this.position.x -= this.velocity;
          break;
        case RIGHT:
          this.position.x += this.velocity;
          break;
        case UP:
          this.position.y -= this.velocity;
          break;
        case DOWN:
          this.position.y += this.velocity;
          break;
      }
      if (!isMoving && !this.finishedMoving) {
        if (this.position.x % SPRITESIZE == 0 && this.position.y % SPRITESIZE == 0) {
          this.finishedMoving = true;
        }
      
      }
    }
  }
  

  /**Checks if sprite move in certain derection is valid
   * If move is valid true is returned, otherwise false
   * @return boolean either true or false depending on if move is valid*/
  private boolean isMoveValid() {
    switch (this.direction) {
      case LEFT:
        if (this.mapObject.checkValidMove(this.position.x + (this.position.x % 20) - SPRITESIZE, 
                                          this.position.y)) {
          return true;
        }
        return false;
      case RIGHT:
        if (this.mapObject.checkValidMove(this.position.x + SPRITESIZE, this.position.y)) {
          return true;
        }
        return false;
      case UP:
        if (this.mapObject.checkValidMove(this.position.x,
                this.position.y + (this.position.y % 20) - SPRITESIZE)) {
          return true;
        }
        return false;
      case DOWN:
        if (this.mapObject.checkValidMove(this.position.x, this.position.y + SPRITESIZE)) {
          return true;
        }
        return false;
      default:
        return false;
    }

  }


  /**Sets the sprite isMoving and finishedMoving variables
   * If move is valid sprite is moved otherwise not
   * @return boolean true if sprite can move otherwise false*/
  public boolean move() {
    if (this.isMoveValid()) {
      this.finishedMoving = false;
      this.isMoving = true;
      return true;
    }
    return false;

  }
  
  /**Sets isMoving = false
   * if sprite is still inbetween tiles it is moved until the next whole tile*/
  public void stopMoving() {
    this.isMoving = false;
  }

  /**allreadyMoving returns a boolean corresponding to whether the sprite is in motion
   * @return true if sprite ismoving otherwise false*/
  public boolean allreadyMoving() {
    return (this.isMoving || !this.finishedMoving);
  }
  

  /**Sets sprite direction to new direction specified
   * @param direction enum of direction type corresponding to new sprite direction */
  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  /**Returns an ArrayList of enums of all valid sprite moves
   * Does not return a move opposite to current sprite move unless that is the only valid move
   * @return ArrayList<Direction> of all valid moves*/
  protected ArrayList<Direction> getValidMoves() {
    ArrayList<Direction> filteredValidDirections = new ArrayList<Direction>(3);
    ArrayList<Direction> allValidDirections = new ArrayList<Direction>(4);
    if (mapObject.checkValidMove(this.position.x-SPRITESIZE, this.position.y)) {
        allValidDirections.add(Direction.LEFT);
        if (this.direction != Direction.RIGHT)
          filteredValidDirections.add(Direction.LEFT);
    }
    if (mapObject.checkValidMove(this.position.x+SPRITESIZE, this.position.y)) {
        allValidDirections.add(Direction.RIGHT);
        if (this.direction != Direction.LEFT)
          filteredValidDirections.add(Direction.RIGHT);
    }
    if (mapObject.checkValidMove(this.position.x, this.position.y-SPRITESIZE)) {
        allValidDirections.add(Direction.UP);
        if (this.direction != Direction.DOWN)
          filteredValidDirections.add(Direction.UP);
    }
    if (mapObject.checkValidMove(this.position.x, this.position.y+SPRITESIZE)) {
        allValidDirections.add(Direction.DOWN);
        if (this.direction != Direction.UP)
          filteredValidDirections.add(Direction.DOWN);
    }
    if (filteredValidDirections.size() != 0) {
      return filteredValidDirections;

    } else {
      return allValidDirections;

    }

  }
  
  /** Generic draw function
   * Draws sprite to screen*/
  public void draw(PApplet app) {
    // draw PImages onto the screen
    app.image(this.spriteImg, this.position.x, this.position.y);
  }
  

  /** Gets the x tile the sprite is on
   * @return int type corresponding to horizontal tile sprite is on*/
  public int getX() {
    return this.position.x / SPRITESIZE;
  }
  
  /** Gets the y tile the sprite is on
   * @return int type corresponding to vertial tile sprite is on */
  public int getY() {
    return this.position.y / SPRITESIZE;
  }
      
  
}
