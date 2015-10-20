package com.example.nataliajastrzebska.urbangame;

import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;

/**
 * Created by Przemysław on 2015-10-20.
 */
public class XMLSerializer {

    public String serialize(GameInformation game) {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            //GAME TAG SERIALIZATION
            serializer.startTag("", "Game");
            serializer.attribute("", "name", game.getName());
            serializer.attribute("", "author", game.getAuthor());
            serializer.attribute("", "pointsNumber", String.valueOf(game.getNumberOfPoints()));

            for (GamePoint point : game.getPoints()) {
                //POINT TAG SERIALIZATION
                serializer.startTag("", "Point");
                serializer.attribute("", "number", String.valueOf(point.getNumber()));
                serializer.attribute("", "name", point.getName());
                serializer.attribute("", "xCoordinate", String.valueOf(point.getCoordinateX()));
                serializer.attribute("", "yCoordinate", String.valueOf(point.getCoordinateY()));
                serializer.attribute("", "plot", point.getPlot());
                serializer.attribute("", "hint", point.getHint());

                //TASK TAG SERIALIZATION
                serializer.startTag("", "Task");
                serializer.attribute("", "question", point.getGameTask().getQuestion());
                serializer.attribute("", "answer", String.valueOf(point.getGameTask().getCorrectAnswer()));
                serializer.attribute("", "a", point.getGameTask().getAnswers().get(0));
                serializer.attribute("", "b", point.getGameTask().getAnswers().get(1));
                serializer.attribute("", "c", point.getGameTask().getAnswers().get(2));
                serializer.attribute("", "d", point.getGameTask().getAnswers().get(3));

                serializer.endTag("", "Task");
                serializer.endTag("", "Point");
            }

            serializer.endTag("", "Game");
            serializer.endDocument();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }
}
