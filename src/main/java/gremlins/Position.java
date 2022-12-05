package gremlins;

public class Position {

  public int x;
  public int y;


  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Position copy() {
    return new Position(this.x, this.y);
  }

  // Definition of struct to store avatar x y coords
}
