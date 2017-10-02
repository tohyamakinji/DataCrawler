package main;

import additional.WordLemmatizer;
import crawler.blog.AdjectiveStealer;
import crawler.blog.VerbStealer;
import crawler.detik.DetikStealer;
import crawler.kompas.KompasStealer;
import crawler.wikipedia.WikipediaStealer;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URISyntaxException;

public class XProgram {

    private XProgram(byte code) throws IOException, ParseException, URISyntaxException {
        switch (code) {
            case 1 :
                new AdjectiveStealer().steal(new WordLemmatizer());
                break;
            case 2 :
                new VerbStealer().steal(new WordLemmatizer());
                break;
            case 3 :
                new KompasStealer().stealForTesting();
                break;
            case 4 :
                new WikipediaStealer().stealForTesting();
                break;
            case 5 :
                new DetikStealer().stealForTesting();
                break;
        }
    }

    public static void main(String[] args) {
        try {
            new XProgram((byte) 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}