package snakeandladdergame;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static GameManager instance;
    private final List<SnakeAndLadderGame> games;

    private GameManager() {
        games = new ArrayList<>();
    }

    public static synchronized GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void startNewGame(List<String> playerNames) {
        SnakeAndLadderGame game = new SnakeAndLadderGame(playerNames);
        games.add(game);
        new Thread(() -> game.play()).start();
    }
}


/**

thread part can be also written as 



import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class GameManager {
    private List<SnakeAndLadderGame> games = new ArrayList<>();
    private ExecutorService gameExecutor = Executors.newCachedThreadPool();

    public void startNewGame(List<String> playerNames) {
        SnakeAndLadderGame game = new SnakeAndLadderGame(playerNames);
        games.add(game);
        gameExecutor.submit(game::play); // Uses a thread pool instead of creating new threads
    }
}



*/





