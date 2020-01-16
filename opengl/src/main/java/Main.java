//Testing Don't mind me :D

public class Main {

    public static void main(String[] args) {
        try {
            boolean testing = true;
            System.out.print(testing);
            boolean vSynced = true;
            IGameLogic iGameLogic = new DummyGame();
            GameEngine gameEngine = new GameEngine("GameEngine", 900, 400, vSynced, iGameLogic);
            gameEngine.run();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

}