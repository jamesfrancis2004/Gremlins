package gremlins;
import processing.core.PImage;


class PowerUp extends Block {

  /**Class Constructor returning a PowerUp type
   * Calls constructor on super block type
   * @param img PImage representing the stonewall image
   * @param position Position object representing tile location
   * @see Block*/
  public PowerUp(PImage powerUpImg, Position position) {
    super(powerUpImg, position, true, false);
  }

  /** Tick does nothing as object is stateless */
  public void tick() {
    return;
  }
  

  /** Always returns false as object is not of type door.
   * @return false. Not of type door*/
  public boolean isDoor() {
    return false;
  }
}
