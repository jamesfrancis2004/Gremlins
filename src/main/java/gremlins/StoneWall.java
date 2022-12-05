package gremlins;
import processing.core.PImage;

public class StoneWall extends Block {

  /**Class Constructor returning a StoneWall type
   * Calls constructor on super block type
   * @param img PImage representing the stonewall image
   * @param position Position object representing tile location
   * @see Block*/
  public StoneWall(PImage img, Position position) {
    super(img, position, false, false);
  }
  
  /**Method which always return false as is not door
   * @return False as stonewall is not of type door*/
  public boolean isDoor() {
    return false;
  }
  
  /**Tick method does nothing for StoneWall as it is a stateless object */
  public void tick() {
    return;
  }
}
