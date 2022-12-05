package gremlins;

import processing.core.PApplet;
import processing.core.PFont;
import processing.data.JSONObject;
import processing.data.JSONArray;
import java.util.*;
import java.io.*;
import java.util.ArrayList;



public class App extends PApplet {

    private static final int WIDTH = 720;
    private static final int HEIGHT = 720;
    private static final int BOTTOMBAR = 60;
    private static final int FPS = 60;
    public String configPath;
    private PFont font;
    private Wizard wizard;
    private Map mapObject;
    private ArrayList<Gremlin> gremlins;
    private boolean hasWon = false;
    private boolean gameOver = false;
    private long start;
    private int currentFrameRate = 0;
    private int FPSCount = 0;

    public App() {
        this.configPath = "config.json";

    }

    /**
     * Initialise the setting of the window size.
    */
    public void settings() {
        size(WIDTH, HEIGHT);
    }


    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
    */
    public void setup() {
        frameRate(FPS);
        // Load images during setup
        this.font = createFont("Arial", 16);
        JSONObject conf = loadJSONObject(new File(this.configPath));
        this.mapObject = new Map(this);
        this.mapObject.setMapLevels((JSONArray) conf.get("levels"));
        this.mapObject.nextLevel();
        this.wizard = mapObject.getWizard();
        this.wizard.setNumberOfLives((int) conf.get("lives"));
        this.gremlins = mapObject.getGremlins();
        this.start = System.currentTimeMillis();


    }

    /**
     * Receive key pressed signal from the keyboard.
    */
    public void keyPressed(){
      if (this.keyCode == 222) {
        this.wizard.superPower();
      }
      if (this.keyCode == 32) {
        this.wizard.shoot();
      }
      if (!this.wizard.allreadyMoving() && !this.wizard.isInSuperPower()) {
        if (this.keyCode == 37) {
          this.wizard.setDirection(Direction.LEFT);
          this.wizard.move();
          return;
        } if (this.keyCode == 38) {
          this.wizard.setDirection(Direction.UP);
          this.wizard.move();
          return;
        } if (this.keyCode == 39) {
          this.wizard.setDirection(Direction.RIGHT);
          this.wizard.move();
          return;
        } if (this.keyCode == 40)  {
          this.wizard.setDirection(Direction.DOWN);
          this.wizard.move();
          return;
        }
      }
    }

    /**
     * Receive key released signal from the keyboard.
    */
    public void keyReleased(){
      if (this.keyCode >= 37 && this.keyCode <= 40) {
        if (this.wizard.isInSuperPower()) {
          return;
        } else {
          this.wizard.stopMoving();
        }
      }
    }

    
    public void draw() {
      ++FPSCount;
      super.background(207, 185, 151);
      super.textFont(font, 20);
      super.fill(0);
      if (this.mapObject.isWizardAtDoor()) {
        this.hasWon = !this.mapObject.nextLevel();
      }
      this.gameOver = !this.wizard.hasLivesRemaining();
      if (this.hasWon) {
        super.textFont(font, 40);
        super.text("You Win!", WIDTH/2 - 100, HEIGHT/2);
        return;
      } else if (this.gameOver) {
        super.textFont(font, 40);
        super.text("Gameover Wizard lost all lives!", WIDTH/2 - 310, HEIGHT/2);
        return;
      }
      if (System.currentTimeMillis() - start >= 1000) {
        start = System.currentTimeMillis();
        currentFrameRate = FPSCount;
        FPSCount = 0;
      }

      super.text("Lives: ", 20, (int) (34.5*20));
      super.text("Level: " + mapObject.getFormattedLevelString(), 160, (int) (34.5*20));
      super.text("FPS: " + currentFrameRate, 280, (int) (34.5*20));
      this.mapObject.draw(this);
      this.mapObject.tick();
      this.wizard.draw(this);
      this.wizard.tick();
      for (Gremlin gremlin : gremlins) {
        gremlin.draw(this);
      }
      
      for (Gremlin gremlin : gremlins) {
        if (this.mapObject.isWizardHit(gremlin.getX(), gremlin.getY())) {
          this.mapObject.resetLevel();
          break;
        }
      }
      
    } 

    public static void main(String[] args) {
        PApplet.main("gremlins.App");
    }
}
