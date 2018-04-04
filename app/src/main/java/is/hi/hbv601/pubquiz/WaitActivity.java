package is.hi.hbv601.pubquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import is.hi.hbv601.pubquiz.model.QuizHolder;
import is.hi.hbv601.pubquiz.view.GifImageView;

/***
 * Activity shown when the pub quiz has not started yet.
 * Created by viktoralex 04.04.2018
 */
public class WaitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        GifImageView gifImageView = findViewById(R.id.GifImageView);
        gifImageView.setGifImageResource(R.drawable.loading);

        // Fetch the questions in this quiz
        final QuizHolder quiz = QuizHolder.getInstance();

        // Switch to another activity if the quiz status changes
        Query currentState = FirebaseDatabase.getInstance().getReference("quizzes/" + quiz.getQuizId() + "/status");
        currentState.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = dataSnapshot.getValue(String.class);

                if (status == null)
                    return;

                if (status.equals("not started")) {
                    // Do nothing, it is this activity
                } else if (status.equals("in progress")) {
                    Intent nextIntent = new Intent(WaitActivity.this, QuestionPagerActivity.class);
                    startActivity(nextIntent);
                    WaitActivity.this.finish();
                } else if (status.equals("review")) {
                    Intent nextIntent = new Intent(WaitActivity.this, ReviewPagerActivity.class);
                    startActivity(nextIntent);
                    WaitActivity.this.finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
