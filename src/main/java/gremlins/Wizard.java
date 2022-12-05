package gremlins;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import processing.core.PImage;
import processing.core.PApplet;
import processing.core.PShape;



public class Wizard extends Sprite {
  private int powerUpTimer;
  private int superPowerUpTimer;
  private int lives;
  private boolean inSuperPower;
  private final int livesRenderHeight = (int) (33.65*20);
  private final int livesRenderStarting = 82;
  private PImage wizardLivesImg;
  private PImage wizardFireBall;
  private PImage wizardLeft;
  private PImage wizardRight;
  private PImage wizardUp;
  private PImage wizardDown;
  private Set<WizardProjectile> projectiles = new HashSet<WizardProjectile>();
  private int cooldown;
  private int superCooldown;

  /**
   * A class constructor which returns an object of type wizard
   * Takes all required wizard animation images and also takes a reference to the mapobject
   * @return Returns a wizard type
   * @param WizardLeft PImage corresponding to wizardLeft image
   * @param WizardRight PImage corresponding to wizardRight image
   * @param wizardUp PImage corresponding to wizardUp image
   * @param WizardDown PImage corresponding to wizardDown image 
   * @param mapObject Map type object, allowing all public map methods to be called by wizard
   * @see Map
   * @see PImage */

  public Wizard(PImage wizardLeft, PImage wizardRight,
                PImage wizardUp, PImage wizardDown,
                PImage wizardFireBall,
                Map mapObject) {
    super(wizardLeft, mapObject, 2);
    this.wizardLeft = wizardLeft;
    this.wizardRight = wizardRight;
    this.wizardUp = wizardUp;
    this.wizardDown = wizardDown;
    this.wizardFireBall = wizardFireBall;
    this.powerUpTimer = 0;
    this.superPowerUpTimer = 0;
    this.wizardLivesImg = wizardLeft;
  }

  /**Overrides original method in wizard parent Sprite
   * Calls the super method but also sets wizard image to corresponding direction
   * @param newDirection Direction object representing new direction wizard is going
   * @see Sprite 
   * @see Direction */

  @Override
  public void setDirection(Direction newDirection) {
    super.setDirection(newDirection);
    switch (newDirection) {
      case LEFT:
        super.setSprite(this.wizardLeft);
        break;
      case RIGHT:
        super.setSprite(this.wizardRight);
        break;
      case UP:
        super.setSprite(this.wizardUp);
        break;
      case DOWN:
        super.setSprite(this.wizardDown);
        break;
    }


  }
  

  /**Sets the wizards number of lives to what was specified in the config.json file 
   * @param lives int type object representing number of wizard lives*/

  public void setNumberOfLives(int lives) {
    this.lives = lives;
  }

  /**Sets the wizard cooldown for its ability to shoot fireballs
   * @param cooldown int type representing number of ticks between cooldowns */

  public void setCooldown(int cooldown) {
    this.cooldown = cooldown;
  }
   /**Sets the wizard cooldown for its ability to travel in its fireballs
   * @param superCooldown int type representing number of ticks between cooldowns */ 
  public void setSuperCooldown(int superCooldown) {
    this.superCooldown = superCooldown;

  }

  /** Iterates the lives counter by -1 */
  public void removeLife() {
    this.lives--;
  }

  /** Returns a boolean type representing whether the wizard has lives remaining
   * If returns true wizard still has lives remaining 
   * @return boolean type representing if wizard has lives remaining*/
  public boolean hasLivesRemaining() {
    if (this.lives == 0) {
      return false;
    }
    return true;
  }


  /** Removes a life from the wizard and then resets its position and clears it projectiles */
  public void reset() {
    this.removeLife();
    super.resetPosition();
    this.projectiles.clear();
  }

  /**Checks whether the wizard is allowed to shoot
   * then adds a projectile to the ArrayList<WizardProjectile> */
  public void shoot() {
    if (this.powerUpTimer == 0) {
      projectiles.add(new WizardProjectile(this.wizardFireBall,
                                           super.position.copy(),
                                           this.direction,
                                           this.mapObject));
      this.powerUpTimer = this.cooldown;
    }

  }

  /** Checks whether the wizard is allowed to use superpower
   * Similar to shoot method; however, wizard position object is not copied */
  public void superPower() {
    if (this.superPowerUpTimer == 0) {
      WizardProjectile newProjectile = new WizardProjectile(
          this.wizardFireBall,
          super.position,
          this.direction,
          this.mapObject
      );
      newProjectile.setIsSuperPower(true);
      projectiles.add(newProjectile);
      this.inSuperPower = true;
      this.superPowerUpTimer = this.superCooldown;
      super.stopMoving();

    }
  }
  
  /** Checks if the wizard is in his superpower
   * @return boolean type. if True wizard in superpower else not*/
  public boolean isInSuperPower() {
    return this.inSuperPower;
  }
  
  /**Overrides sprite tick method
   * Checks whether wizard is in superpower if in superpower wizard is not ticked
   * All wizard projectiles are also ticked and removed if they collide with other objects
   * All timers are also iterated*/
  @Override
  public void tick() {
    if (!this.inSuperPower) {
      super.tick();

    }
    ArrayList<WizardProjectile> toRemove = new ArrayList<WizardProjectile>();
    for (WizardProjectile projectile : projectiles) {
      if (!projectile.tick()) {
        toRemove.add(projectile);
      }
    }
    for (WizardProjectile projectile : projectiles ) {
      if (mapObject.isGremlinHit(projectile.getX(), projectile.getY())) {
        toRemove.add(projectile);
      } else if (mapObject.isGremlinProjectileHit(projectile.getX(), projectile.getY())) {
        toRemove.add(projectile);
      }
    }
    for (WizardProjectile projectile : toRemove) {
      if (this.inSuperPower) {
        if (projectile.isSuperPower()) {
          this.inSuperPower = false;
          this.position.x = (this.position.x / SPRITESIZE) * SPRITESIZE;
          this.position.y = (this.position.y / SPRITESIZE) * SPRITESIZE;

        }
      }
      projectiles.remove(projectile);
    }
    if (this.powerUpTimer > 0) {
      this.powerUpTimer -= 1;
    }
    if (this.superPowerUpTimer > 0) {
      this.superPowerUpTimer -= 1;
    }
  }
  
  /** Overrides sprite draw method
   * Call sprite super draw method to draw the wizard to the screen
   * Also draws lives to screen and cooldown bar to screen
   * */
  @Override
  public void draw(PApplet app) {
    for (int i = 0; i < lives; ++i) {
      app.image(this.wizardLivesImg, livesRenderStarting+(super.SPRITESIZE*i), livesRenderHeight);
    }
    super.draw(app);
    for (WizardProjectile projectile : projectiles) {
      projectile.draw(app);
    }
    if (this.powerUpTimer > 0) {
      app.fill(0, 255, 0);
      app.rect(380.0f, (33.9f*20),
          (0 + (100f*((float) this.powerUpTimer / (float) this.cooldown))),
          10.0f);
    } 
    if (this.superPowerUpTimer > 0) {
      app.fill(255, 0, 0);
      app.rect(500.0f, (33.9f*20),
          (0 + (100f*((float) this.superPowerUpTimer / (float) this.superCooldown))),
          10.0f);
    }
  }






  
  
}
