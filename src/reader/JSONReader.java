package reader;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;

public class JSONReader {

    private JSONParser parser;

    private JSONArray kompasWebArray;

    public JSONReader() throws IOException, ParseException {
        this.parser = new JSONParser();
        initializeKompasStream();
    }

    public JSONArray getKompasWebArray() {
        return kompasWebArray;
    }

    private void initializeKompasStream() throws IOException, ParseException {
        InputStreamReader kompasWebStream = new InputStreamReader(getClass().getResourceAsStream("/data/kompasweb.json"));
        kompasWebArray = (JSONArray) parser.parse(kompasWebStream);
        kompasWebStream.close();
    }

    public void clear() {
        if (kompasWebArray != null)
            kompasWebArray.clear();
    }

}