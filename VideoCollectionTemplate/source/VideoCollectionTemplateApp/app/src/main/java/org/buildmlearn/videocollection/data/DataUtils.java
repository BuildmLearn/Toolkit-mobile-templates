package org.buildmlearn.videocollection.data;

import android.content.Context;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * @brief Contains xml data utils for video collection template's app.
 *
 * Created by Anupam (opticod) on 13/5/16.
 */
public class DataUtils {

    public static String[] readTitleAuthor(Context myContext) {
        String result[] = new String[2];
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setValidating(false);

        DocumentBuilder db;
        Document doc;
        try {
            db = dbf.newDocumentBuilder();
            doc = db.parse(myContext.getAssets().open(org.buildmlearn.videocollection.Constants.XMLFileName));
            doc.normalize();

            result[0] = doc.getElementsByTagName("title").item(0).getChildNodes()
                    .item(0).getNodeValue();

            result[1] = doc.getElementsByTagName("name").item(0).getChildNodes()
                    .item(0).getNodeValue();

        } catch (ParserConfigurationException | FileNotFoundException | SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
