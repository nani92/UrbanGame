package com.example.nataliajastrzebska.urbangame;

import java.util.ArrayList;


/**
 * Created by Przemysław on 2015-10-20.
 */
public class GameInformation {

    private ArrayList<GamePoint> points;
    private int numberOfPoints;
    private String name;
    private String author;
    private int shouldShowDirection = 0;
    private int isRPG = 1;
    private GameTypeEnum gameType = GameTypeEnum.GAME_FOR_TIME;

    public GameInformation() {
        points = new ArrayList<GamePoint>();
    }

    public ArrayList<GamePoint> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<GamePoint> points) {
        this.points = points;
    }
    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public void setNumberOfPoints(int numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setShouldShowDirection(int shouldShowDirection){
        this.shouldShowDirection = shouldShowDirection;
    }

    public int getShouldShowDirection(){
        return shouldShowDirection;
    }

    public void setIsRPG(int isRPG){
        this.isRPG = isRPG;
    }

    public int getIsRPG(){
        return isRPG;
    }

    public void setGameType(GameTypeEnum gameType){
        this.gameType = gameType;
    }

    public GameTypeEnum getGameType (){
        return gameType;
    }

    @Override
    public String toString() {
        //TODO
        // add newly added in object shouldShow..., isRPG, gameType
        return "GameInformation{" +
                "points=" + points.toString() +
                ", numberOfPoints=" + numberOfPoints +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
