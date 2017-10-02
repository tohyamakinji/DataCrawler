package crawler.kompas;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import reader.JSONReader;
import writer.JSONWriter;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;

public class KompasStealer {

    private JSONWriter writer;

    public KompasStealer() {
        writer = new JSONWriter();
    }

    @SuppressWarnings("unchecked")
    public void stealForTesting() throws IOException, ParseException, URISyntaxException {
        JSONArray dataTesting = new JSONArray(); // Array root
        JSONReader reader = new JSONReader();
        JSONObject data;
        JSONArray urlArray;
        for (Object o : reader.getKompasWebArray()) {
            data = (JSONObject) o;

            JSONObject dataTestingObject = new JSONObject(); // First child (From root)
            dataTestingObject.put("POS", data.get("POS"));
            JSONArray arrayTestCase = new JSONArray(); // Second child (From root)

            urlArray = (JSONArray) data.get("URL");
            for (Object o1 : urlArray) {
                System.out.println("URL Processed : " + o1);
                try {
                    POSStealer((String) o1, (String) data.get("POS"), arrayTestCase);
                } catch (SocketTimeoutException e){
                    System.out.println("TIMEOUT when access URL : " + o1 + ", system will process next URL");
                }
            }

            dataTestingObject.put("testcase", arrayTestCase);
            dataTesting.add(dataTestingObject);
        }
        reader.clear();
        writer.writeTesting(dataTesting.toJSONString());
    }

    @SuppressWarnings("unchecked")
    private void POSStealer(String pageURL, String POS, JSONArray testArray) throws IOException {

        Document document = Jsoup.connect(pageURL).get();
        Elements paragraph = document
                .select("div.col-bs10-7.js-read-article div.read__article.mt2.clearfix.js-tower-sticky-parent div.col-bs9-7 div.read__content p");
        String[] splitParagraph;
        for (Element element : paragraph) {
            splitParagraph = element.text().split("\\. ");
            for (String text : splitParagraph) {
                if (containsIgnoreCase(text, POS)) {
                    JSONObject objectTestCase = new JSONObject();
                    if (text.charAt(text.length() - 1) != '.') {
                        text = text + ".";
                    }
                    text = text.replaceAll("\"", "");
                    objectTestCase.put("text", text);
                    objectTestCase.put("sense", "");
                    testArray.add(objectTestCase);
                }
            }
        }
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