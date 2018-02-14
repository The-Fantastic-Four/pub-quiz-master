package is.hi.hbv601.pubquiz;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);

        questionNumber = findViewById(R.id.questionNumber);

        GetQuestionHandler getQuestionHandler = new GetQuestionHandler();
        getQuestionHandler.execute();

        /*client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });*/

        EditText questionAnswer = findViewById(R.id.questionAnswer);

        Button questionAnswerButton = findViewById(R.id.questionAnswerButton);
        questionAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
            } catch (JSONException exc) {
                exc.printStackTrace();
            }
        }
    }

}
