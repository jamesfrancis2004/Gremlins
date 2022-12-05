package gremlins;

import processing.core.PImage;


public class WizardProjectile extends Projectile {


  public WizardProjectile(PImage img, Position position, Direction direction, Map mapObject) {
     super(img, position, direction, mapObject); 

  
  }



  @Override
  public boolean tick() {
    int[] offsets = super.getProjectileOffset();
    if (!super.move()) {
      super.mapObject.destroy(this.position.x + offsets[0], this.position.y + offsets[1]);
      return false;
    }
    return true;
  }


}
