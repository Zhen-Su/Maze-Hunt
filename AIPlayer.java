import java.lang.Math;
public class AIPlayer extends Player {

  public AIPlayer() {
    super("AI");
    super.coins = 5;
  }
  @Override
  public String toString() {
    return super.toString();
  }
  public int direction(int numberofExits) {
    return (int)(Math.random() * ((numberofExits - 1) + 1)) + 1;
    
  }
}
