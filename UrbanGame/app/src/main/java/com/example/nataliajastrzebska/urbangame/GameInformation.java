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

    @Override
    public String toString() {
        return "GameInformation{" +
                "points=" + points.toString() +
                ", numberOfPoints=" + numberOfPoints +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
