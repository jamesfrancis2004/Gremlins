package gremlins;
import processing.core.PImage;


public class Door extends Block {

/**Class Constructor returning a Door type
   * Calls constructor on super block type
   * @param img PImage representing the stonewall image
   * @param position Position object representing tile location
   * @see Block
   * */
  public Door(PImage door, Position position) {
    super(door, position, true, false);


  }
  

  /** Tick function does nothing as door type is stateless */
  public void tick() {
    return;
  }

  /**Always returns true as isinstance of door
   * @return true. Current instance is door*/
  public boolean isDoor() {
    return true;
  }
}
