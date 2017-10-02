package crawler.detik;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import writer.JSONWriter;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URISyntaxException;

public class DetikStealer {

    private static final String POS = "bintang";
    private static final int RANGE = 10;

    private JSONWriter writer;

    public DetikStealer() {
        writer = new JSONWriter();
    }

    @SuppressWarnings("unchecked")
    public void stealForTesting() throws IOException, URISyntaxException {
        JSONArray dataTesting = new JSONArray(); // Array root
        JSONObject dataTestingObject = new JSONObject(); // First child (From root)
        dataTestingObject.put("POS", POS);
        JSONArray arrayTestCase = new JSONArray(); // Second child (From root)

        Document pageSearch;
        boolean isRetry = false;
        for (int i = 1; i <= RANGE; i++) {
            try {
                pageSearch = Jsoup.connect("https://www.detik.com/search/searchall?query=" + POS + "&sortby=time&page=" + i).get();
                isRetry = false;
                Elements searchResult = pageSearch.select("div.wrapper.full div.container.content div.list.media_rows.list-berita article a span.box_text");
                if (!searchResult.isEmpty()) {
                    for (int j = 0; j < searchResult.size(); j++) {
                        System.out.println("RETRIEVE PAGE " + searchResult.get(j).parent().attr("href"));
                        try {
                            POSStealer(searchResult.get(j).parent().attr("href"), arrayTestCase);
                            isRetry = false;
                        } catch (ConnectException e) {
                            if (!isRetry) {
                                isRetry = true;
                                j--;
                                System.out.println("TIMEOUT when access page " + searchResult.get(j).parent().attr("href") + ", TRYING RETRY PROCESS FOR THE SECOND TIME ...");
                            } else {
                                isRetry = false;
                                System.out.println("TIMEOUT when access page " + searchResult.get(j).parent().attr("href") + ", GIVE UP !! TRYING TO PROCESS OTHER PAGE ...");
                            }
                        }
                    }
                }
            } catch (ConnectException e) {
                if (!isRetry) {
                    isRetry = true;
                    i--;
                    System.out.println("TIMEOUT when searching word : " + POS + ", at RANGE : " + i + ", TRYING TO RETRY PROCESS FOR SECOND TIME ...");
                } else {
                    isRetry = false;
                    System.out.println("TIMEOUT when searching word : " + POS + ", at RANGE : " + i + ", GIVE UP !! TRYING TO PROCESS OTHER RANGE ...");
                }

            }
        }
        dataTestingObject.put("testcase", arrayTestCase);
        dataTesting.add(dataTestingObject);
        writer.writeTesting(dataTesting.toJSONString());
        System.out.println("DONE !");
    }

    private void POSStealer(String pageURL, JSONArray testArray) throws IOException {
        if (!getCSSQuery(pageURL).equals("")) {
            String css = getCSSQuery(pageURL);
            Document document = Jsoup.connect(pageURL).get();
            Elements elements = document.select(css);
            if (elements.isEmpty()) {
                if (pageURL.contains("https://food.detik.com")) {
                    elements = document.select("div.maincontainer div.container div.kiri.content_1 div.content_detail div.artikel2 div.wrap_detail");
                    if (!elements.isEmpty()) textCrawling(elements, testArray);
                } else if (pageURL.contains("https://news.detik.com")) {
                    elements = document.select("div.container div.content div.detail_content article div div.detail_text");
                    if (!elements.isEmpty()) textCrawling(elements, testArray);
                }
            } else {
                textCrawling(elements, testArray);
            }
        } else {
            System.out.println("WARNING : PAGE EXCLUDED (IGNORED) FOR UNDETERMINED CSS QUERY !");
        }
    }

    @SuppressWarnings("unchecked")
    private void textCrawling(Elements elements, JSONArray testArray) {
        String[] splitText;
        for (Element element : elements) {
            splitText = element.text().split("\\. ");
            for (String text : splitText) {
                if (containsIgnoreCase(text, POS)) {
                    if (text.charAt(text.length() - 1) != '.') {
                        text = text + ".";
                    }
                    text = text.replaceAll("\"", "");
                    JSONObject objectTestCase = new JSONObject();
                    objectTestCase.put("text", text);
                    objectTestCase.put("sense", "");
                    testArray.add(objectTestCase);
                    System.out.println(text);
                }
            }
        }
    }

    private String getCSSQuery(String pageURL) {
        if (pageURL.contains("https://wolipop.detik.com")) {
            return "div.maincontainer div.container div#detikcontent.clearfix div.kiri div.content_detail.box div.artikel2 div.text_detail";
        } else if (pageURL.contains("https://sport.detik.com")) {
            return "div.container div.content div.detail_content article div div#detikdetailtext.detail_text";
        } else if (pageURL.contains("https://travel.detik.com")) {
            return "main.main div.container div.main__col.clearfix div.content div#news.read div.clearfix div#detikdetailtext.read__content.pull-left";
        } else if (pageURL.contains("https://finance.detik.com")) {
            return "div.container div.content div.detail_content article div div#detikdetailtext.detail_text";
        } else if (pageURL.contains("https://news.detik.com")) {
            return "div.container div.content div.detail_content article div div#detikdetailtext.detail_text";
        } else if (pageURL.contains("https://hot.detik.com")) {
            if (pageURL.contains("https://hot.detik.com/celeb-of-the-month")) {
                return "div.content_area div.detail div.detail_text";
            } else if (pageURL.contains("https://hot.detik.com/detiktv")) {
                return "";
            }
            return "div.wrapper_atas div.container div.content div.detail_content article div div#detikdetailtext.detail_text";
        } else if (pageURL.contains("https://inet.detik.com")) {
            return "div#content div.container div#news.lm_content div.box div.pd15 article div div#detikdetailtext.detail_text";
        } else if (pageURL.contains("https://oto.detik.com")) {
            return "div#content div.container.relative div.lm_content article.box.pd15 div div#detikdetailtext.detail_text";
        } else if (pageURL.contains("https://health.detik.com")) {
            return "div.maincontainer div.container div#detikcontent div.kiri div.sub_kiri.clearfix div.content_detail div.text_detail";
        } else if (pageURL.contains("https://food.detik.com")) {
            return "div.maincontainer div.container div.kiri.content_1 div.content_detail div.artikel2 div.text_detail";
        }
        return "";
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