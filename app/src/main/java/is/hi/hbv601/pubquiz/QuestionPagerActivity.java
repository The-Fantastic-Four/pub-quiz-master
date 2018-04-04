package is.hi.hbv601.pubquiz;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601.pubquiz.model.QuestionReference;
import is.hi.hbv601.pubquiz.model.QuizHolder;

/**
 * Activity for the user to scroll through the questions in the quiz
 * Created by viktoralex on 14.3.2018.
 */
public class QuestionPagerActivity extends AppCompatActivity {

    private ViewPager questionViewPager;
    private List<QuestionReference> questions;

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

        // Fetch the questions in this quiz
        QuizHolder quiz = QuizHolder.getInstance();
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
                // Not implemented yet
                // Probably not needed because the fragment is listening to changes itself
                Toast.makeText(QuestionPagerActivity.this,
                        "onChildChanged: " + dataSnapshot.getKey(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Not implemented yet
                Toast.makeText(QuestionPagerActivity.this,
                        "onChildRemoved: " + dataSnapshot.getKey(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // Not implemented yet
                Toast.makeText(QuestionPagerActivity.this,
                        "onChildMoved: " + dataSnapshot.getKey(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Not implemented yet
                Toast.makeText(QuestionPagerActivity.this,
                        "onCancelled",
                        Toast.LENGTH_LONG).show();
            }
        });

        // Switch to the current question if it changes
        Query currentQuestion = FirebaseDatabase.getInstance().getReference("quizzes/" + quiz.getQuizId() + "/currentQuestion");
        currentQuestion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int currentQuestion = dataSnapshot.getValue(Integer.class);

                Toast.makeText(QuestionPagerActivity.this,
                        String.format(
                                getResources().getString(R.string.question_switch_toast),
                                currentQuestion),
                        Toast.LENGTH_SHORT).show();

                // Decrease by one so that we don't look like such nerds
                // (currentQuestion is not zero indexed, item number is)
                questionViewPager.setCurrentItem(currentQuestion - 1, true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Move the user onto the next question
    public void nextQuestion()
    {
        questionViewPager.setCurrentItem(questionViewPager.getCurrentItem() + 1, true);
    }
}
