package is.hi.hbv601.pubquiz.activity;

import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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

import is.hi.hbv601.pubquiz.fragment.QuestionFragment;
import is.hi.hbv601.pubquiz.R;
import is.hi.hbv601.pubquiz.handler.QuestionPageHandler;
import is.hi.hbv601.pubquiz.handler.QuizStatusHandler;
import is.hi.hbv601.pubquiz.model.QuestionReference;
import is.hi.hbv601.pubquiz.model.QuizHolder;

/**
 * Activity for the user to scroll through the questions in the quiz
 * Created by viktoralex on 14.3.2018.
 */
public class QuestionPagerActivity extends AppCompatActivity {

    private ViewPager questionViewPager;
    private List<QuestionReference> questions;

    QuizStatusHandler quizStatusHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_pager);

        questionViewPager = findViewById(R.id.question_view_pager);
        questions = new ArrayList<>();

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Needs to be stored so that the child event listener can reference and notify when data changes
        // References the questions list of question references
        final FragmentStatePagerAdapter fragmentPagerAdapter = new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                QuestionReference question = questions.get(position);

                QuestionFragment cf = new QuestionFragment();
                cf.setQuestion(question.getQuestionId(), question.getQuestionNumber());
                return cf;
            }

            @Override
            public int getCount() {
                return questions.size();
            }
        };

        questionViewPager.setAdapter(fragmentPagerAdapter);

        QuizHolder quiz = QuizHolder.getInstance();

        // Fetch the questions in this quiz
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
        new QuestionPageHandler(quiz.getQuizId(), questionViewPager);

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
                        QuestionPagerActivity.super.onBackPressed();

                        QuizHolder quiz = QuizHolder.getInstance();
                        DatabaseReference mDatabaseTeam = FirebaseDatabase.getInstance().getReference(
                                "quizzes/" + quiz.getQuizId() + "/teams/" + quiz.getTeamName() );
                        mDatabaseTeam.removeValue();

                        QuestionPagerActivity.this.finish();
                    }
                }).create().show();
    }

    // Move the user onto the next question
    public void nextQuestion()
    {
        questionViewPager.setCurrentItem(questionViewPager.getCurrentItem() + 1, true);
    }
}