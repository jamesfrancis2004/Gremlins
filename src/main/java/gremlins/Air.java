package gremlins;

import processing.core.PApplet;

public class Air extends Block {

   /** Class Constructor returning a StoneWall type
   * Calls constructor on super block type
   * @param position represents tile location of block
   */
  public Air(Position position) {
    super(true);
    this.position = position;
  }
  /** Overrides draw method as nothing is drawn to screen for air block */
  @Override
  public void draw(PApplet app) {
    return;
  }

  /** Always returns false as block is not of instance Door 
   * @return false. Not of type door*/
  public boolean isDoor() {
    return false;
  }
  
  /** Tick does nothing as object is stateless. */
  public void tick() {
    return;
  }
}
