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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601.pubquiz.model.Question;
import is.hi.hbv601.pubquiz.model.QuestionReference;
import is.hi.hbv601.pubquiz.model.QuizHolder;

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
        Query questionsInQuiz = FirebaseDatabase.getInstance().getReference("quizzes/" + quiz.getQuizId() + "/questions").orderByValue();

        questionsInQuiz.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                long questionNumber = Long.parseLong(dataSnapshot.getValue().toString());
                questions.add(new QuestionReference(dataSnapshot.getKey(), questionNumber));

                fragmentPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(QuestionPagerActivity.this,
                        "onChildChanged: " + dataSnapshot.getKey(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Toast.makeText(QuestionPagerActivity.this,
                        "onChildRemoved: " + dataSnapshot.getKey(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(QuestionPagerActivity.this,
                        "onChildMoved: " + dataSnapshot.getKey(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(QuestionPagerActivity.this,
                        "onCancelled",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void nextQuestion()
    {
        questionViewPager.setCurrentItem(questionViewPager.getCurrentItem() + 1, true);
    }
}
