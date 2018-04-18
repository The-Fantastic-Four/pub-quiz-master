package is.hi.hbv601.pubquiz.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import is.hi.hbv601.pubquiz.R;
import is.hi.hbv601.pubquiz.handler.QuizStatusHandler;
import is.hi.hbv601.pubquiz.model.QuizHolder;
import is.hi.hbv601.pubquiz.utils.QuizIntent;
import is.hi.hbv601.pubquiz.view.GifImageView;

/***
 * Activity shown when the pub quiz has not started yet.
 * Created by viktoralex 04.04.2018
 */
public class WaitActivity extends AppCompatActivity {

    QuizStatusHandler quizStatusHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        GifImageView gifImageView = findViewById(R.id.GifImageView);
        gifImageView.setGifImageResource(R.drawable.loading);

        // Fetch the questions in this quiz
        final QuizHolder quiz = QuizHolder.getInstance();

        // Switch to another activity if the quiz status changes
        quizStatusHandler = new QuizStatusHandler(quiz.getQuizId(), this);
    }

    // Stops back function of back button and changes it to exit if pressed twice.
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.quit_quiz_dialog_title))
                .setMessage(getResources().getString(R.string.quit_quiz_dialog_text))
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        WaitActivity.super.onBackPressed();
                        WaitActivity.this.finish();
                    }
                }).create().show();
    }
}
