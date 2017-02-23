package fernandoperez.lifemanager.parsers;

import android.graphics.Bitmap;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import fernandoperez.lifemanager.models.Configurations;
import fernandoperez.lifemanager.utils.Triplet;

/**
 * Created by fernandoperez on 2/23/17.
 */

public class BackupParser {

    // We don't use namespaces
    private static final String ns = null;

    public List<Triplet<Configurations, List<String>, List<String>>> parse(Reader in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in);
            parser.nextTag();
            return readAllConfigurations(parser);
        } finally {
            in.close();
        }
    }

    private List<Triplet<Configurations, List<String>, List<String>>>  readAllConfigurations(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Triplet<Configurations, List<String>, List<String>>>  configurations = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "Backup");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Configuration")) {
                configurations.add(readConfiguration(parser));
            } else {
                skip(parser);
            }
        }
        return configurations;
    }


    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private Triplet<Configurations, List<String>, List<String>> readConfiguration(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "Configuration");
        Long confId = null;
        String confName = null;
        String confLocation = null;
        List<String> arrivingServices = null;
        List<String> leavingServices = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "ID":
                    confId = readConfId(parser);
                    break;

                case "Name":
                    confName = readConfName(parser);
                    break;

                case "Location":
                    confLocation = readLocation(parser);
                    break;

                case "Arriving":
                    arrivingServices = readArrivingServices(parser);
                    break;

                case "Leaving":
                    leavingServices = readLeavingServices(parser);
                    break;

                default:
                    skip(parser);
            }
        }
        Configurations configurations = new Configurations(null, confName);

        return new Triplet<>(configurations, arrivingServices, leavingServices);
    }

    // Processes title tags in the feed.
    private Long readConfId(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "ID");
        Long id = Long.getLong(readText(parser));
        parser.require(XmlPullParser.END_TAG, null, "ID");
        return id;
    }

    // Processes link tags in the feed.
    private String readLocation(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "Location");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "Location");
        return link;
    }

    // Processes summary tags in the feed.
    private String readConfName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "Name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "Name");
        return name;
    }

    private List<String> readArrivingServices(XmlPullParser parser) throws IOException, XmlPullParserException {
        List<String> services = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "Arriving");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Service")) {
                services.add(readService(parser));
            } else {
                skip(parser);
            }
        }
        return services;
    }

    private List<String> readLeavingServices(XmlPullParser parser) throws IOException, XmlPullParserException {
        List<String> services = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "Leaving");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Service")) {
                services.add(readService(parser));
            } else {
                skip(parser);
            }
        }
        return services;
    }

    private String readService(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "Service");
        String serviceName = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "Name":
                    serviceName = readServName(parser);
                    break;

                default:
                    skip(parser);
            }
        }

        return serviceName;
    }

    // Processes summary tags in the feed.
    private String readServName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "Name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "Name");
        return name;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
