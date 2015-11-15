package com.example.nataliajastrzebska.urbangame;

import android.content.Context;

import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by Przemysław on 2015-11-15.
 */
public class GameStorage {

    private static GameStorage gameStorageInstance = null;
    private static Context context = null;

    private GameStorage() {
    }

    public void init(Context context) {
        this.context = context;
    }

    //Instance getter
    public static GameStorage getInstance() {
        if (gameStorageInstance == null) {
            gameStorageInstance = new GameStorage();
        }
        return gameStorageInstance;
    }


    public ArrayList<String> loadGamesNames() {
        ArrayList<String> list = new ArrayList<String>();
        File[] files = context.getFilesDir().listFiles();
        for (File file : files) {
            if (file.getName().endsWith(".ginf")) {
                list.add(file.getName());
            }
        }
        return list;
    }

    public void saveGame(GameInformation gameInformation) {
        XMLSerializer serializer = new XMLSerializer();
        FileOutputStream outputStream;
        StringBuilder stringBuilder = new StringBuilder(40);
        String gameName = gameInformation.getName();
        stringBuilder.append(gameName);
        stringBuilder.append(".ginf");
        stringBuilder.toString();
        try {
            outputStream = context.openFileOutput(stringBuilder.toString(), Context.MODE_PRIVATE);
            outputStream.write(serializer.serialize(gameInformation).getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GameInformation loadGame(String gameName) {
        XMLPullParser parser = new XMLPullParser();
        GameInformation gameInforamtion = null;
        try {
            FileInputStream inputStream = context.openFileInput(gameName);
            gameInforamtion = parser.parse(inputStream);
            return gameInforamtion;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
