/**
 * Shows appropriate question and submits answer when user has ðushed the answer button.
 *
 * @author Ragnheiður Ásta Karlsdóttir rak4@hi.is
 * @author Viktor Alex Brynjarsson vab18@hi.is
 * @date 13.2.2018
 */
package is.hi.hbv601.pubquiz;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AnswerQuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);

        final TextView questionNumber = (TextView) findViewById(R.id.questionNumber);

        MediaType json = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://pub-quiz-server.herokuapp.com/question")
                .post(RequestBody.create(json, "{\"public\": 0}"))
                .build();

        // Response response = client.newCall(request).execute();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try
                {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    questionNumber.setText("" + jsonObject.getInt("question_number"));
                } catch (JSONException exc) {
                    exc.printStackTrace();
                }
            }
        });

        EditText questionAnswer = (EditText) findViewById(R.id.questionAnswer);

        Button questionAnswerButton = (Button) findViewById(R.id.questionAnswerButton);
        questionAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}
