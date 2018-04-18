package is.hi.hbv601.pubquiz.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601.pubquiz.R;
import is.hi.hbv601.pubquiz.adapter.ReviewFragmentStatePagerAdapter;
import is.hi.hbv601.pubquiz.fragment.ReviewFragment;
import is.hi.hbv601.pubquiz.handler.QuestionPageHandler;
import is.hi.hbv601.pubquiz.handler.QuizQuestionHandler;
import is.hi.hbv601.pubquiz.handler.QuizStatusHandler;
import is.hi.hbv601.pubquiz.model.QuestionReference;
import is.hi.hbv601.pubquiz.model.QuizHolder;

/**
 * Activity for the user to review answers given by another team
 * Created by viktoralex on 04.04.2018
 */
public class ReviewPagerActivity extends AppCompatActivity {

    private ViewPager reviewViewPager;
    private List<QuestionReference> questions;

    QuizStatusHandler quizStatusHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_pager);

        reviewViewPager = findViewById(R.id.review_view_pager);
        questions = new ArrayList<>();

        // Fetch the questions in this quiz
        final QuizHolder quiz = QuizHolder.getInstance();

        // Find which team's answer this team should review (stored in /reviewers/[quizId]/[teamName])
        DatabaseReference reviewRef = FirebaseDatabase.getInstance().getReference("reviewers/" + quiz.getQuizId() + "/" + quiz.getTeamName());
        reviewRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final String reviewTeam = dataSnapshot.getValue(String.class);

                FragmentManager fragmentManager = getSupportFragmentManager();

                // Needs to be stored so that the child event listener can reference and notify when data changes
                // References the questions list of question references
                final FragmentStatePagerAdapter fragmentPagerAdapter = new ReviewFragmentStatePagerAdapter(reviewTeam, questions, fragmentManager);

                reviewViewPager.setAdapter(fragmentPagerAdapter);

                // Add questions to adapter
                new QuizQuestionHandler(quiz.getQuizId(), questions, fragmentPagerAdapter);

                // Switch to the current question if it changes
                new QuestionPageHandler(quiz.getQuizId(), reviewViewPager);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Switch to another activity if the quiz status changes
        quizStatusHandler = new QuizStatusHandler(quiz.getQuizId(), this);
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }
}
