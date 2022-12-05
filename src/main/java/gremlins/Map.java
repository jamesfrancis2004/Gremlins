package gremlins;
import processing.core.PApplet;
import processing.core.PImage;
import java.io.*;
import java.util.*;
import java.util.Random;
import java.util.ArrayList;
import processing.data.JSONArray;
import processing.data.JSONObject;



public class Map {
  private static final int MAPWIDTH = 36;
  private static final int MAPHEIGHT = 33;
  private static final int IMAGESIZE = 20;
  private static final int RESPAWNRADIUS = 10;
  private Random rnd = new Random();
  private PImage brickwallImg;
  private PImage brickDestroyed0Img;
  private PImage brickDestroyed1Img;
  private PImage brickDestroyed2Img;
  private PImage brickDestroyed3Img;
  private PImage stonewallImg;
  private PImage wizardLeftImg;
  private PImage wizardRightImg;
  private PImage wizardUpImg;
  private PImage wizardDownImg;
  private PImage wizardFireBallImg;
  private PImage gremlinImg;
  private PImage gremlinSlimeImg;
  private PImage doorImg;
  private PImage powerUpImg;
  private Wizard wizard;
  private final int FPS = 60;
  private ArrayList<Gremlin> gremlins = new ArrayList<Gremlin>();
  private JSONArray levelConfig;
  private int levelCount = 0;
  private Block[][] map;
  private int totalPowerUpTimer = 600;
  private int currentPowerUpTimer  = 600;
  private int totalPowerUpCooldown = 300;
  private int currentPowerUpCooldown = 0;
  ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();

  /**
   * Class constructor. Returns a map object, which can be used in in-game objects to handle in-game logic
   * The map object loads all required assets, and creates a new wizard object
   * @param app An object of type pApplet to load assets using the PApplet loadImage function
   * @return A map object
   * @see Map*/
  public Map(PApplet app) {
      this.map = new Block[MAPHEIGHT][MAPWIDTH];
      this.brickwallImg = app.loadImage(app.getClass().getResource("brickwall.png").getPath().replace("%20", " "));
      this.brickDestroyed0Img = app.loadImage(app.getClass().getResource("brickwall_destroyed0.png").getPath().replace("%20", " "));
      this.brickDestroyed1Img = app.loadImage(app.getClass().getResource("brickwall_destroyed1.png").getPath().replace("%20", " "));
      this.brickDestroyed2Img = app.loadImage(app.getClass().getResource("brickwall_destroyed2.png").getPath().replace("%20", " "));
      this.brickDestroyed3Img = app.loadImage(app.getClass().getResource("brickwall_destroyed3.png").getPath().replace("%20", " "));
      this.stonewallImg = app.loadImage(app.getClass().getResource("stonewall.png").getPath().replace("%20", " "));;
      this.wizardLeftImg = app.loadImage(app.getClass().getResource("wizard0.png").getPath().replace("%20", " "));
      this.wizardRightImg = app.loadImage(app.getClass().getResource("wizard1.png").getPath().replace("%20", " "));
      this.wizardUpImg = app.loadImage(app.getClass().getResource("wizard2.png").getPath().replace("%20", " "));
      this.wizardDownImg = app.loadImage(app.getClass().getResource("wizard3.png").getPath().replace("%20", " "));
      this.wizardFireBallImg = app.loadImage(app.getClass().getResource("fireball.png").getPath().replace("%20", " "));
      this.gremlinImg = app.loadImage(app.getClass().getResource("gremlin.png").getPath().replace("%20", " "));
      this.gremlinSlimeImg = app.loadImage(app.getClass().getResource("slime.png").getPath().replace("%20", " "));
      this.doorImg = app.loadImage(app.getClass().getResource("door.png").getPath().replace("%20", " "));
      this.powerUpImg = app.loadImage(app.getClass().getResource("powerup.png").getPath().replace("%20", " "));

      this.rnd.setSeed((int) new Date().getTime() / 1000);
      this.wizard = new Wizard(this.wizardLeftImg,this.wizardRightImg,
                               this.wizardUpImg, this.wizardDownImg,
                               this.wizardFireBallImg, this);


  }
  /**
   * Sets the map level Json array file
   * @param levels is a JSONArray object, which represents the map config file
   * @return returns void
   * @see JSONArray */
  public void setMapLevels(JSONArray levels) {
    this.levelConfig = levels;
  }
  
  /**
   * Goes to the next level of the map config
   * The function reads in the map text file and changes the Map Block[][] to the specified level 
   * All gremlins and powerups are also cleared from the screen and sprites are reset
   * If there is no next level the function returns false to indicate game has been won
   * @return Returns a boolean value. If true went to next level else won
   * @see FILEIO */
  public boolean nextLevel() {
      this.currentPowerUpCooldown = 0;
      this.gremlins.clear();
      this.powerUps.clear();
      if (this.levelCount+1 > this.levelConfig.size()) {
        return false;
      }
      ++levelCount;
      JSONObject currentLevel = (JSONObject) this.levelConfig.get(levelCount-1);
      String fileName = (String) currentLevel.get("layout");
      int wizardCooldown = (int) ((double) currentLevel.get("wizard_cooldown") * FPS);
      int gremlinCooldown = (int) ((double) currentLevel.get("enemy_cooldown") * FPS);
      int wizardSuperCooldown = (int) ((double) currentLevel.get("super_cooldown") * FPS);
      try {
        String line;
        int height = 0;
        char c;
        File fp = new File(fileName);
        Scanner sc = new Scanner(fp);
        while (sc.hasNextLine()) {
          line = sc.nextLine();
          for (int width = 0; width < MAPWIDTH; ++width) {
            c = line.charAt(width);
            if (c == 'B') {
              this.map[height][width] = new Brick(this.brickwallImg, new Position(width, height),
                                                  this.brickDestroyed0Img, this.brickDestroyed1Img,
                                                  this.brickDestroyed2Img, this.brickDestroyed3Img);
            } else if (c == 'X') {
              this.map[height][width] = new StoneWall(this.stonewallImg, new Position(width, height));
            } else if (c == 'W') {
              this.wizard.setPosition(new Position(width, height));
              this.wizard.setStartingPosition(new Position(width, height));
              this.wizard.setCooldown(wizardCooldown);
              this.wizard.setSuperCooldown(wizardSuperCooldown);
              this.map[height][width] = new Air(new Position(width, height));
            } else if (c == 'G') {
              Gremlin newGremlin = new Gremlin(this.gremlinImg, this.gremlinSlimeImg, this, gremlinCooldown);
              newGremlin.setPosition(new Position(width, height));
              newGremlin.setStartingPosition(new Position(width, height));
              this.gremlins.add(newGremlin);
              this.map[height][width] = new Air(new Position(width, height));
            } else if (c == ' ') {
              this.map[height][width] = new Air(new Position(width, height));
            } else if (c == 'E') {
              this.map[height][width] = new Door(this.doorImg, new Position(width, height));
            } else {
              this.map[height][width] = new Air(new Position(width, height));
            }
          }
          ++height;
        }
        sc.close();
        return true;
      } catch (FileNotFoundException e) {
        System.out.println("No file in directory");
        return false;
      }
  }

  /**
   * Function returns a string to indicate currentlevel/totallevel
   * @return String type
   * */

  public String getFormattedLevelString() {
    return levelCount + "/" + levelConfig.size();
  }
  /** 
   * Function checks if the wizard is at the door
   * If the wizard is at the door it returns true otherwise false
   * @return boolean */
  public boolean isWizardAtDoor() {
    return this.map[this.wizard.getY()][this.wizard.getX()].isDoor();
  }


  /**Function checks whether a certain x and y coord corresponds to passable tile
   * If tile is passable it returns true else false
   * The x and y coord are also in absolute format so must be converted to a tile position
   * @param xcoord int type that represents a horizontal tile location
   * @param ycoord int type that represents a vertical tile location
   * @return boolean type*/
  public boolean checkValidMove(int xcoord, int ycoord) {
    return this.map[ycoord/IMAGESIZE][xcoord/IMAGESIZE].isPassable();
  }


  /**Function tries to destroy a block at a certain x and y coord
   * If the block can be destroyed it is destroyed otherwise nothing happens
   * The x and y coord are in absolute format so must be converted to a tile position
   * @param xcoord int type representing a horzontal tile location
   * @param ycoord int type representing a vertical tile location*/
  public void destroy(int xcoord, int ycoord) {
    this.map[ycoord/IMAGESIZE][xcoord/IMAGESIZE].destroy();
  }
  
  /** Function returns a random position on the map, corresponding to a isPassable tile
   * Function will continue to generate random positions at least 10 tiles away from the wizard
   * This continues until the position corresponds to a block which isPassable.
   * @return Returns a Position object 
   * @see Position*/
  private Position getRandomPos() {
    int newX, newY;
    int minX, minY, maxX, maxY;
    int xRange, yRange;
    if (this.wizard.getX() + RESPAWNRADIUS >= MAPWIDTH - 1) {
      minX = 0;
      maxX = this.wizard.getX() - RESPAWNRADIUS;
    } else {
      minX = this.wizard.getX() + 10;
      maxX = MAPWIDTH - 1;
    }
    if (this.wizard.getY() + RESPAWNRADIUS >= MAPHEIGHT - 1) {
      minY = 0;
      maxY = this.wizard.getY() - RESPAWNRADIUS;
    } else {
      minY = this.wizard.getY() + RESPAWNRADIUS;
      maxY = MAPHEIGHT - 1;

    }
    xRange = maxX - minX;
    yRange = maxY - minY;

    do {
      newX = minX + Math.abs(rnd.nextInt() % xRange);
      newY = minY + Math.abs(rnd.nextInt() % yRange);
    } while (!(this.map[newY][newX].isPassable()));
    return new Position(newX, newY);



  }

  /**Function resets the position of the gremlin to a random spot on the map
   * New position must be at least 10 tiles away from the wizard
   * @param gremlin An object of type Gremlin, representing the gremlin for action to be performed on
   * @see Sprite.setPosition()
   * @see Map.getRandomPos()*/

  private void killGhost(Gremlin gremlin) {
    gremlin.setPosition(this.getRandomPos());
  }  

  /**Function checks whether the gremlin collides with an object at a certain x and y coord
   * If the gremlins position overlaps with the tile of the x and y coord the germlin is reset
   * @param xcoord the xcoord of the object which is being checked for colliding with the gremlin
   * @param ycoord the ycoord of the object which is being checked for colliding with the gremlin
   * @return returns type boolean. If true gremlin hit, otherwise no hit.
   * @see Map.killGhost() */

  public boolean isGremlinHit(int xcoord, int ycoord) {
    for (Gremlin gremlin :  this.gremlins) {
      if (xcoord == gremlin.getX() && ycoord == gremlin.getY()) {
        this.killGhost(gremlin);
        gremlin.clearProjectiles();
        return true;
      }
      
    }
    return false;
  }

  /** Function checks for collision with gremlin projectile at a certain x and y coord
   * If the projectile position overlaps with the tile of the x and y coord the projectile is removed
   * @param xcoord the tile of the object being checked for collision
   * @param ycoord the tile of the object being checked for collision
   * @return Returns a boolean type. If true projectile is hit, otherwise not
   * @see Gremlin.isProjectileHit() */
  public boolean isGremlinProjectileHit(int xcoord, int ycoord) {
    for (Gremlin gremlin : this.gremlins) {
      if (gremlin.isProjectileHit(xcoord, ycoord)) {
        return true;
      }
    }
    return false;
  }

  /** Is called when the wizard is killed
   * Resets the level to its original state
   * @see Wizard.reset()
   * @see Block.reset()
   * @see Gremlin.reset()*/
  public void resetLevel() {
    this.currentPowerUpCooldown = 0;
    this.wizard.reset();
    for (Gremlin gremlin : this.gremlins) {
      gremlin.reset(); 
      
    }
    for (Block[] blocks : this.map) 
      for (Block block : blocks) {
      block.reset();
    }
    
  }
  
  /**Returns true if certain x and y coord corresponds with wizard tile location
   * @param xcoord horizontal tile location corresponding to object location
   * @param ycoord vertical tile location corresponding to object location
   * @see Wizard.getX()
   * @see Wizard.getY()
   * @return returns a boolean type true if wizard otherwise  false*/
  

  public boolean isWizardHit(int xcoord, int ycoord) {
      if (this.wizard.getX() == xcoord && 
          this.wizard.getY() == ycoord) {
        return true;
      }
       
    return false;

  }

  /** Returns a reference to the wizard.
   * @return Returns the unique wizard created in the Map object*/

  
  public Wizard getWizard() {
    return this.wizard;
  }

  /** Returns an ArrayList storing references to all the in-game Gremlins
   * @return Returns ArrayList<Gremlin> */

  public ArrayList<Gremlin> getGremlins() {
    return this.gremlins;
  }

  /** The tick function is responsible for in-game updates each frame
   * The tick function spawns new powerups to the screen and also ticks all in-game gremlins
   * @see PowerUp
   * */
  public void tick() {
    boolean clearPowerUps = false;
    if (powerUps.size() == 0) {
      this.currentPowerUpTimer--;
    }
    if (this.currentPowerUpTimer == 0) {
      this.currentPowerUpTimer = this.totalPowerUpTimer;
      this.powerUps.add(new PowerUp(this.powerUpImg, this.getRandomPos()));
    }

    for (PowerUp powerUp : powerUps) {
      if (this.wizard.getX() == powerUp.getX() 
          && this.wizard.getY() == powerUp.getY()) {
        this.currentPowerUpCooldown = this.totalPowerUpTimer;
        clearPowerUps = true;
        for (Gremlin gremlin : gremlins) {
          gremlin.clearProjectiles();
        }
      }
    }
    if (clearPowerUps) {
      this.powerUps.clear();

    }
    if (this.currentPowerUpCooldown > 0) {
      this.currentPowerUpCooldown--;

    } else {
      for (Gremlin gremlin : gremlins) {
        gremlin.tick();
      }
    }

    
  }

  /** The function is responsible for drawing the map to the screen
   * Each object in Block[][] Map is drawn to the screen
   * The powerup is also drawn to the screen
   * @param app An object of type pApplet allowing the PApplet.draw() to be called */
  public void draw(PApplet app) {

    for (int height = 0; height < MAPHEIGHT; ++height) 
            for (int width = 0; width < MAPWIDTH; ++width) {
              this.map[height][width].draw(app);
            }
    for (PowerUp powerUp : this.powerUps) {
      powerUp.draw(app);
    }
  }
  
  
}
