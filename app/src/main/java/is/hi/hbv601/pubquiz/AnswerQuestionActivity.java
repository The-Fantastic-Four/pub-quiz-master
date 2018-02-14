package is.hi.hbv601.pubquiz;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Shows appropriate question and submits answer when user has ðushed the answer button.
 *
 * @author Ragnheiður Ásta Karlsdóttir rak4@hi.is
 * @author Viktor Alex Brynjarsson vab18@hi.is
 * @date 13.2.2018
 */
public class AnswerQuestionActivity extends AppCompatActivity {

    TextView questionNumber;
    EditText questionAnswer;
    long questionId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);

        questionNumber = findViewById(R.id.questionNumber);
        questionAnswer = findViewById(R.id.questionAnswer);

        GetQuestionHandler getQuestionHandler = new GetQuestionHandler();
        getQuestionHandler.execute();

        final AnswerQuestionHandler answerQuestionHandler = new AnswerQuestionHandler();

        /*client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });*/

        Button questionAnswerButton = findViewById(R.id.questionAnswerButton);
        questionAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerQuestionHandler.execute();
            }
        });
    }

    public class GetQuestionHandler extends AsyncTask {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Object doInBackground(Object[] objects) {
            MediaType json = MediaType.parse("application/json; charset=utf-8");
            Request request = new Request.Builder()
                    .url("https://pub-quiz-server.herokuapp.com/question")
                    .post(RequestBody.create(json, "{\"public\": 0}"))
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            try
            {
                JSONObject jsonObject = new JSONObject((String) o);
                questionNumber.setText("" + jsonObject.getInt("question_number"));
                questionId = jsonObject.getLong("id");
            } catch (JSONException exc) {
                exc.printStackTrace();
            }
        }
    }

    public class AnswerQuestionHandler extends AsyncTask {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Object doInBackground(Object[] objects) {

            JSONObject answer = new JSONObject();
            try {
                answer.put("answer", questionAnswer.getText());
                answer.put("team_id", 99);
                answer.put("question_id", questionId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            MediaType json = MediaType.parse("application/json; charset=utf-8");
            Request request = new Request.Builder()
                    .url("https://pub-quiz-server.herokuapp.com/answer")
                    .post(RequestBody.create(json, answer.toString()))
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            // TODO Check if successful answer

            Intent answerQuestionIntent = new Intent(AnswerQuestionActivity.this, AnswerQuestionActivity.class);
            startActivity(answerQuestionIntent);
        }
    }

}
