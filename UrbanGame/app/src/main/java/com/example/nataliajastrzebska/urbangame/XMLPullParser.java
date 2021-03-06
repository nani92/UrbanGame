package com.example.nataliajastrzebska.urbangame;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;


/**
 * Created by Przemysław on 2015-10-20.
 */
public class XMLPullParser {
    private GameInformation gameInformation;
    private GamePoint gamePoint;
    private GameTask gameTask;
    public GameInformation parse(InputStream is) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(is, null);


            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = xmlPullParser.getName();
                switch (eventType) {

                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("game")) {
                            gameInformation = new GameInformation();
                            gameInformation.setName(xmlPullParser.getAttributeValue(null, "name"));
                            gameInformation.setAuthor(xmlPullParser.getAttributeValue(null, "author"));
                            gameInformation.setIsRPG(Integer.parseInt(xmlPullParser.getAttributeValue(null, "isRPG")));
                            gameInformation.setShouldShowDirection(Integer.parseInt(xmlPullParser.getAttributeValue(null, "shouldShowDirection")));
                            gameInformation.setNumberOfPoints(Integer.parseInt(xmlPullParser.getAttributeValue(null, "pointsNumber")));
                        } else if (tagname.equalsIgnoreCase("point")) {
                            gamePoint = new GamePoint();
                            gamePoint.setName(xmlPullParser.getAttributeValue(null, "name"));
                            gamePoint.setNumber(Integer.parseInt(xmlPullParser.getAttributeValue(null, "number")));
                            gamePoint.setCoordinateX(Double.parseDouble(xmlPullParser.getAttributeValue(null, "xCoordinate")));
                            gamePoint.setCoordinateY(Double.parseDouble(xmlPullParser.getAttributeValue(null, "yCoordinate")));
                            gamePoint.setPlot(xmlPullParser.getAttributeValue(null, "plot"));
                            gamePoint.setHint(xmlPullParser.getAttributeValue(null, "hint"));
                        } else if (tagname.equalsIgnoreCase("task")) {
                            gameTask = new GameTask();
                            Log.d("Natalia", "xml type= " + xmlPullParser.getAttributeValue(null, "type"));
                            gameTask.setTaskType(xmlPullParser.getAttributeValue(null, "type"));
                            if (gameTask.getTaskType() == TaskType.ABCD) {
                                Log.d("Natalia", "ABCD");
                                gameTask.getAnswers().add(xmlPullParser.getAttributeValue(null, "a"));
                                gameTask.getAnswers().add(xmlPullParser.getAttributeValue(null, "b"));
                                gameTask.getAnswers().add(xmlPullParser.getAttributeValue(null, "c"));
                                gameTask.getAnswers().add(xmlPullParser.getAttributeValue(null, "d"));
                                gameTask.setCorrectAnswer(Integer.parseInt(xmlPullParser.getAttributeValue(null, "answer")));
                            }
                            else {
                                Log.d("Natalia", "not ABCD");
                                gameTask.setAnswer(xmlPullParser.getAttributeValue(null, "answer"));
                            }
                            gameTask.setQuestion(xmlPullParser.getAttributeValue(null, "question"));

                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("point")) {
                            gamePoint.setGameTask(gameTask);
                            gameInformation.getPoints().add(gamePoint);
                        }

                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }


        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Natalia", "return GI" + gameInformation);
        return gameInformation;
    }
}

