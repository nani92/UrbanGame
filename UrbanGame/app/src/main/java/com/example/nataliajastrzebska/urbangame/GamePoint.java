package com.example.nataliajastrzebska.urbangame;

import java.util.Date;

/**
 * Created by Przemysław on 2015-10-20.
 */
public class GamePoint {

    private String name;
    private String hint;
    private String plot;
    private int number;
    private float coordinateX;
    private float coordinateY;
    private GameTask gameTask;

    public GamePoint() {
        gameTask = new GameTask();
    }

    public GameTask getGameTask() {
        return gameTask;
    }

    public void setGameTask(GameTask gameTask) {
        this.gameTask = gameTask;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public float getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(float coordinateX) {
        this.coordinateX = coordinateX;
    }

    public float getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(float coordinateY) {
        this.coordinateY = coordinateY;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }



    @Override
    public String toString() {
        return "GamePoint{" +
                "name='" + name + '\'' +
                ", hint='" + hint + '\'' +
                ", plot='" + plot + '\'' +
                ", number=" + number +
                ", coordinateX=" + coordinateX +
                ", coordinateY=" + coordinateY +
                ", gameTask=" + gameTask.toString() +
                '}';
    }
}
