package gremlins;
import processing.core.PImage;
import processing.core.PApplet;


public abstract class Block {

  private final int SPRITESIZE = 20;

  private PImage blockImg;
  protected Position position;
  private boolean passable;
  private boolean destroyable;

  public Block(PImage blockImg, Position position, boolean passable, boolean destroyable){
    this.blockImg = blockImg;
    this.position = position;
    this.passable = passable;
    this.destroyable = destroyable;


  }

  public Block(boolean passable) {
    this.passable = passable;
  }

  public void reset() {
    return;
  }

  public void setImg(PImage newImg) {
    this.blockImg = newImg;
  }

  public void draw(PApplet app) {
    if (this.blockImg == null) {
      return;
    }
    // draw PImages onto the screen
    app.image(this.blockImg, this.position.x*SPRITESIZE, this.position.y*SPRITESIZE);
  }

  public abstract void tick();

  public abstract boolean isDoor();

  public void destroy() {
    return;
  }

  public boolean isPassable() {
    return this.passable;

  }


  public void makePassable() {
    this.passable = true;
  }
  public void removePassable() {
    this.passable = false;
  }
  public int getX() {
    return this.position.x;
  }

  public int getY() {
    return this.position.y;
  }



}
