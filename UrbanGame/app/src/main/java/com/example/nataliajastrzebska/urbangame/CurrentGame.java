package com.example.nataliajastrzebska.urbangame;

/**
 * Created by Przemysław on 2015-11-15.
 */
public class CurrentGame {

    private GameInformation gameInformation;
    private static CurrentGame currentGameInstance = null;

    private CurrentGame(){};

    public static CurrentGame getInstance() {
        if (currentGameInstance == null) {
            currentGameInstance = new CurrentGame();
        }
        return currentGameInstance;
    }

    public GameInformation getGameInformation() {
        return gameInformation;
    }

    public void setGameInformation(GameInformation gameInformation) {
        this.gameInformation = gameInformation;
    }


}
