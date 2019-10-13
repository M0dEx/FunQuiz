package me.m0dex.funquiz.questions.opentriviadb;

import me.m0dex.funquiz.FunQuiz;
import me.m0dex.funquiz.questions.Question;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenTriviaDB {

    private final String url;
    private FunQuiz instance;

    private int index;

    public OpenTriviaDB(FunQuiz _instance) {

        instance = _instance;

        url = "https://opentdb.com/api.php?amount=50&type=multiple";

        index = 0;
    }

    public List<Question> getQuestions() {

        List<Question> output = new ArrayList<>();

        try {
            URL uri = new URL(url);
            HttpURLConnection con = (HttpURLConnection) uri.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);

            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(new InputStreamReader(con.getInputStream()));

            if((long) response.get("response_code") != 0) {
                instance.getLogger().severe("An exception has occured while requesting Open Trivia DB questions: Bad response (!= 0)");
                return output;
            }

            JSONArray questionArray = (JSONArray) response.get("results");

            for(Object object : questionArray) {

                JSONObject questionObject = (JSONObject) object;
                String question = (String) questionObject.get("question");
                String answer = (String) questionObject.get("correct_answer");

                output.add(new Question("OTDB-" + index, question, Arrays.asList(answer), instance.getSettings().defaultRewards, instance));

                index++;
            }

        } catch (Exception ex) {
            instance.getLogger().severe("An exception has occured while requesting Open Trivia DB questions: ");
            ex.printStackTrace();
        }

        return output;
    }
}
