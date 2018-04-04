package is.hi.hbv601.pubquiz;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
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

import is.hi.hbv601.pubquiz.model.QuestionReference;
import is.hi.hbv601.pubquiz.model.QuizHolder;

/**
 * Activity for the user to review answers given by another team
 * Created by viktoralex on 04.04.2018
 */
public class ReviewPagerActivity extends AppCompatActivity {

    private ViewPager reviewViewPager;
    private List<QuestionReference> questions;

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
                final FragmentStatePagerAdapter fragmentPagerAdapter = new FragmentStatePagerAdapter(fragmentManager) {
                    @Override
                    public Fragment getItem(int position) {
                        QuestionReference question = questions.get(position);
                        ReviewFragment rf = new ReviewFragment();
                        rf.setReviewQuestion(reviewTeam, question.getQuestionId(), question.getQuestionNumber());
                        return rf;
                    }

                    @Override
                    public int getCount() {
                        return questions.size();
                    }
                };

                reviewViewPager.setAdapter(fragmentPagerAdapter);

                Query questionsInQuiz = FirebaseDatabase.getInstance().getReference("quizzes/" + quiz.getQuizId() + "/questions").orderByValue();

                // When the questions change add to the list and let the fragment pager adapter know the
                // data has changed
                questionsInQuiz.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        long questionNumber = Long.parseLong(dataSnapshot.getValue().toString());
                        questions.add(new QuestionReference(dataSnapshot.getKey(), questionNumber));

                        fragmentPagerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        // Not implemented
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        // Not implemented
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        // Not implemented
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Not implemented
                    }
                });

                // Switch to the current question if it changes
                Query currentQuestion = FirebaseDatabase.getInstance().getReference("quizzes/" + quiz.getQuizId() + "/currentQuestion");
                currentQuestion.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int currentQuestion = dataSnapshot.getValue(Integer.class);

                        Toast.makeText(ReviewPagerActivity.this,
                                String.format(
                                        getResources().getString(R.string.question_switch_toast),
                                        currentQuestion),
                                Toast.LENGTH_SHORT).show();

                        // Decrease by one so that we don't look like such nerds
                        // (currentQuestion is not zero indexed, item number is)
                        reviewViewPager.setCurrentItem(currentQuestion - 1, true);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Switch to another activity if the quiz status changes
        Query currentState = FirebaseDatabase.getInstance().getReference("quizzes/" + quiz.getQuizId() + "/status");
        currentState.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = dataSnapshot.getValue(String.class);

                if (status == null)
                    return;

                if (status.equals("not started")) {
                    Intent nextIntent = new Intent(ReviewPagerActivity.this, WaitActivity.class);
                    startActivity(nextIntent);
                    ReviewPagerActivity.this.finish();
                } else if (status.equals("in progress")) {
                    Intent nextIntent = new Intent(ReviewPagerActivity.this, QuestionPagerActivity.class);
                    startActivity(nextIntent);
                    ReviewPagerActivity.this.finish();
                } else if (status.equals("review")) {
                    // Do nothing, it is this activity
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }
}
