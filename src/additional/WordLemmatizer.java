package additional;

import jsastrawi.morphology.DefaultLemmatizer;
import jsastrawi.morphology.Lemmatizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class WordLemmatizer {

    private Lemmatizer lemmatizer;

    public WordLemmatizer() throws IOException {
        Set<String> dictionary = new HashSet<>();
        InputStream stream = Lemmatizer.class.getResourceAsStream("/root-words.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String s;
        while ((s = reader.readLine()) != null) {
            dictionary.add(s);
        }
        lemmatizer = new DefaultLemmatizer(dictionary);
        reader.close();
        stream.close();
    }

    public Lemmatizer getLemmatizer() {
        return lemmatizer;
    }

}