package is.hi.hbv601.pubquiz;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import is.hi.hbv601.pubquiz.model.Question;
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

    // Instance variables
    TextView questionNumber;
    EditText questionAnswer;
    private String androidId;
    Question question;

    /**
     * Runs when the activity is created
     * @param savedInstanceState state of the saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);

        questionNumber = findViewById(R.id.questionNumber);
        questionAnswer = findViewById(R.id.questionAnswer);

        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        // Fetch question from server
        GetQuestionHandler getQuestionHandler = new GetQuestionHandler();
        getQuestionHandler.execute();

        final AnswerQuestionHandler answerQuestionHandler = new AnswerQuestionHandler();

   //     final LeaveQuiz leaveQuiz = new LeaveQuiz();
        // Sends the answer to server on button click
        Button questionAnswerButton = findViewById(R.id.questionAnswerButton);
        questionAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerQuestionHandler.execute();
            }
        });

  /*      //leave Quiz
        Button leaveQuizButton = findViewById( R.id.leaveQuizButton);
        leaveQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaveQuiz.execute(  );
            }
        });
  */
    }

    public void update()
    {
        questionNumber.setText(
                String.format(
                        getResources().getString(R.string.question_number),
                        this.question.getQuestionNumber()));
    }

    /**
     * Handles the task of fetching info on the question to the server, asynchronously.
     */
    public class GetQuestionHandler extends AsyncTask {

        OkHttpClient client = new OkHttpClient();

        // Requests info from server
        @Override
        protected Object doInBackground(Object[] objects) {
            MediaType json = MediaType.parse("application/json; charset=utf-8");
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.pub_quiz_server_base_url) +
                         getResources().getString(R.string.get_question_path))
                    .post(RequestBody.create(json, "{}"))
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        // Handles the response from server
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            try
            {
                JSONObject jsonObject = new JSONObject((String) o);

                question = new Question();
                question.setId(jsonObject.getLong("id"));
                question.setQuestion(jsonObject.getString("question"));
                question.setQuestionNumber(jsonObject.getInt("question_number"));
                question.setTotalQuestions(jsonObject.getInt("total_questions"));
                question.setType(jsonObject.getString("question_type"));

                update();
            } catch (JSONException exc) {
                exc.printStackTrace();
            }
        }
    }

    /**
     * Handles the task of sending the answers to the server, asynchronously.
     */
    public class AnswerQuestionHandler extends AsyncTask {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Object doInBackground(Object[] objects) {

            JSONObject answer = new JSONObject();
            try {
                answer.put("answer", questionAnswer.getText());
                answer.put("team_id", 99);
                answer.put("question_id", question.getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Sends info to server
            MediaType json = MediaType.parse("application/json; charset=utf-8");
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.pub_quiz_server_base_url) +
                            getResources().getString(R.string.answer_question_path))
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

        // Handles response from server
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            // TODO Check if successful answer

            Intent answerQuestionIntent = new Intent(AnswerQuestionActivity.this, AnswerQuestionActivity.class);
            answerQuestionIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(answerQuestionIntent);
        }
    }

/*    //Handles the task of removing a Team from a quiz.
    public class LeaveQuiz extends AsyncTask {
        OkHttpClient client = new OkHttpClient();

        @Override
        protected Object doInBackground(Object[] objects) {

            JSONObject removedTeam = new JSONObject();
            try {
                removedTeam.accumulate("android_id", androidId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Sends info to server
            MediaType json = MediaType.parse("application/json; charset=utf-8");
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.pub_quiz_server_base_url) +
                            getResources().getString(R.string.remove_team))
                    .post(RequestBody.create(json, removedTeam.toString()))
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        // Handles response from server
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            // TODO Check if successful answer

            Intent signInIntent = new Intent(AnswerQuestionActivity.this, RegisterTeamFragment.class);
            signInIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(signInIntent);
        }
    }
    */
}
