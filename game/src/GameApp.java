import game.*;

public class GameApp {
    public static void main(String[] args) throws Exception {
        Game game = new SlidePuzzle("スライドパズル");
        game.play();
    }
}
