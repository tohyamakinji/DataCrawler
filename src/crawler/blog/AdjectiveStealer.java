package crawler.blog;

import additional.WordLemmatizer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdjectiveStealer {

    public void steal(WordLemmatizer lemmatizer) throws IOException {
        Document document = Jsoup.connect("http://www.tipsbelajarbahasainggris.com/contoh-kata-sifat-dalam-bahasa-inggris/").get();
        Elements adjectiveData = document.select("#genesis-content article div.entry-content table tbody tr td");
        String[] splitData;
        List<String> adjectivesList = new ArrayList<>();
        for (Element element : adjectiveData) {
            if (element.outerHtml().contains("<td width=\"236\">&nbsp")) {
                splitData = element.ownText().split("\\W+");
                for (String text : splitData) {
                    if (!text.equals("Tak") && !text.equals("Tidak") && !text.equals("Tak kenal takut")
                            && !text.equals("Ambigu") && !text.equals("Dwimakna")) {
                        text = lemmatizer.getLemmatizer().lemmatize(text);
                        if (!adjectivesList.contains(text)) {
                            adjectivesList.add(text);
                        }
                    }
                }
            }
        }
        StringBuilder builder = new StringBuilder();
        for (String adjective : adjectivesList) {
            builder.append("\"").append(adjective).append("\"").append(",").append("\n");
        }
        System.out.println(builder.toString());
    }
}