package crawler.blog;

import additional.WordLemmatizer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VerbStealer {

    public void steal(WordLemmatizer lemmatizer) throws IOException {
        Document document = Jsoup.connect("http://www.caramudahbelajarbahasainggris.net/2013/04/1001-kata-kerja-bahasa-inggris-beserta-artinya.html").get();
        Elements adjectiveData = document.select("#page div article #post-295 div.single_post div.post-single-content.box.mark-links div table tbody tr td");
        String[] splitData;
        List<String> verbList = new ArrayList<>();
        for (Element element : adjectiveData) {
            if (element.outerHtml().contains("<td style=\"border-left: none; border-top: none;\">")) {
                element = element.child(0);
                splitData = element.ownText().split("\\W+");
                for (String text : splitData) {
                    text = lemmatizer.getLemmatizer().lemmatize(text);
                    if (!verbList.contains(text)) {
                        verbList.add(text);
                    }
                }
            }
        }
        StringBuilder builder = new StringBuilder();
        for (String adjective : verbList) {
            builder.append("\"").append(adjective).append("\"").append(",").append("\n");
        }
        System.out.println(builder.toString());
    }

}