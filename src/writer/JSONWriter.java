package writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.URISyntaxException;

public class JSONWriter {

    public void writeTesting(String JSON) throws URISyntaxException, IOException {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(JSON);

        File file = new File(getClass().getResource("/result/testing.json").toURI());
        OutputStream stream = new FileOutputStream(file, false);
        stream.write(gson.toJson(element).getBytes());
        stream.flush();
        stream.close();
    }

}