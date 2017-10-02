package crawler.wikipedia;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import writer.JSONWriter;

import java.io.IOException;
import java.net.URISyntaxException;

public class WikipediaStealer {

    private static final String pageURL = "https://id.wikipedia.org/wiki/Bintang";
    private static final String POS = "bintang";
    private JSONWriter writer;

    public WikipediaStealer() {
        writer = new JSONWriter();
    }

    @SuppressWarnings("unchecked")
    public void stealForTesting() throws IOException, URISyntaxException {
        JSONArray dataTesting = new JSONArray(), arrayTestCase = new JSONArray(); // Root is dataTesting and arrayTestCase is Second child (From root)
        JSONObject dataTestingObject = new JSONObject(); // First child (From root)
        dataTestingObject.put("POS", POS);

        System.out.println("PLEASE WAIT READY TO FETCH DATA FROM : " + pageURL);
        Document document = Jsoup.connect(pageURL).get();
        Elements paragraph = document.select("div#bodyContent.mw-body-content div#mw-content-text.mw-content-ltr div.mw-parser-output p");
        System.out.println("DATA DOWNLOADED, START PROCESSING");
        String[] splitParagraph;
        for (Element element : paragraph) {
            splitParagraph = element.text().replaceAll("\\[\\d+\\]", "").split("\\. ");
            for (String text : splitParagraph) {
                if (containsIgnoreCase(text, POS)) {
                    JSONObject objectTestCase = new JSONObject();
                    if (text.charAt(text.length() - 1) != '.') {
                        text = text + ".";
                    }
                    text = text.replaceAll("\"", "");
                    objectTestCase.put("text", text);
                    objectTestCase.put("sense", "");
                    arrayTestCase.add(objectTestCase);
                }
            }
        }
        dataTestingObject.put("testcase", arrayTestCase);
        dataTesting.add(dataTestingObject);
        writer.writeTesting(dataTesting.toJSONString());
        System.out.println("DONE !");
    }

    private boolean containsIgnoreCase(String s, String search) {
        if (s == null || search == null)
            return false;
        int length = search.length();
        if (length == 0) return true;
        for (int i = s.length() - length; i >= 0; i--) {
            if (s.regionMatches(true, i, search, 0, length)) return true;
        }
        return false;
    }

}