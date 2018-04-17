package is.hi.hbv601.pubquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
}
