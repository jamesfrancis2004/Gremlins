package gremlins;


import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.util.*;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class SampleTest {

    @Test
    public void testLoadingMap() {
      App app = new App();
      app.noLoop();
      JSONObject conf = app.loadJSONObject(new File(app.configPath));
      PApplet.runSketch(new String[] {"App"}, app);
      app.setup();
      app.delay(1000);
      Map mapObject = new Map(app);
      mapObject.setMapLevels((JSONArray) conf.get("levels"));



    }
    public void simpleTest() {
        
    }
}
