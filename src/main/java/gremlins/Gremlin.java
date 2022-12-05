package gremlins;
import processing.core.PImage;
import processing.core.PApplet;
import java.util.Random;
import java.util.Date;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;


public class Gremlin extends Sprite {
  private int timer;
  private Random rnd;
  private Set<Projectile> projectiles = new HashSet<Projectile>();
  private PImage slime;
  private int cooldown;



  /**Class Constructor which returns a Gremlin object type
   * The class constructor sets the random seed for the gremlins to its address in memory
   * This allows each gremlin to have a unique seed
   * @param gremlinImg PImage representing gremlin image
   * @param slime PImage represesting slime image
   * @param mapObject reference to mapObject. Allows all its public methods to be called
   * @param cooldown The cooldown in number of ticks for when a gremlin can shoot*/
  public Gremlin(PImage gremlinImg, PImage slime, Map mapObject, int cooldown) {
    super(gremlinImg, mapObject, 1);
    this.slime = slime;
    rnd = new Random();
    rnd.setSeed( (int) System.identityHashCode(this));
    this.timer = 0;
    this.cooldown = cooldown;
  

  }

  /**Resets the Gremlins position to its starting position and clears all projectiles from screen */
  public void reset() {
    super.resetPosition();
    this.projectiles.clear();
  }
  

  /** Clears gremlin projectiles from screen */
  public void clearProjectiles() {
    this.projectiles.clear();
  }
  

  /** Checks whether gremlin projectile location corresponds with x and y coord
   * @param xcoord Horizontal tile location for overlap with Gremlin projectile 
   * @param ycoord Vertial tile location for overlap with Gremlin projectile
   * @return Returns a boolean type. True if projectile is hit otherwise false */
  public boolean isProjectileHit(int xcoord, int ycoord) {
    ArrayList<Projectile> toRemove = new ArrayList<Projectile>();
    boolean returnValue = false;
    for (Projectile projectile : projectiles) {
      if (projectile.getX() == xcoord && projectile.getY() == ycoord) {
        toRemove.add(projectile);
        returnValue = true;
      }
    }
    for (Projectile projectile : toRemove) {
      projectiles.remove(projectile);
    }
    return returnValue;
  }

  
  /**Overrides the super move type
   * Call super move method and if returns false changes the gremlin direction to a random new direction
   * @return boolean Always returns true as Gremlins direction is changed if move is invalid */
  @Override
  public boolean move() {
    if (!super.move()) {
      ArrayList<Direction> validDirections = super.getValidMoves();
      super.setDirection(validDirections.get(rnd.nextInt(validDirections.size())));
      super.move();
    }
    return true;
  }
  
  /**Adds a projectile to the gremlins stored projectiles object
   * Sets projectile direction to gremlin direction*/
  public void shoot() {
    projectiles.add(new Projectile(slime, this.position.copy(), this.direction, this.mapObject));
  }

  

  /**Tick method iterates the cooldown timer and also causes gremlin to shoot if timer divisible by cooldown
   * Moves the gremlin if the tick is divisible by 20 representing a whole new tile gremlin is on
   * Also checks for collisions between gremlin and wizard and gremlin projectiles with objects*/
  @Override 
  public void tick() {
    super.tick();
    if (this.timer++ % 20 == 0) {
      this.move();
    }
    if (this.timer % this.cooldown == 0) {
      this.shoot();
    }
    for (Projectile projectile : projectiles) {
      if (mapObject.isWizardHit(projectile.getX(), projectile.getY())) {
        mapObject.resetLevel();
        break;
      }
    }
    ArrayList<Projectile> toRemove = new ArrayList<Projectile>();
    for (Projectile projectile : projectiles) {
      if (!projectile.tick()) {
        toRemove.add(projectile);
      }
    }
    for (Projectile projectile : toRemove) {
      projectiles.remove(projectile);
    }



  }

  /**Ovverrides the super draw type, drawing all gremlin projectiles to screen
   * Calls super draw method to draw Gremlin to the screen*/
  @Override
  public void draw(PApplet app) {
    for (Projectile projectile : projectiles) {
      projectile.draw(app);
    }
    super.draw(app);
  }
}
