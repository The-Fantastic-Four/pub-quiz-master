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
import is.hi.hbv601.pubquiz.model.QuizHolder;

public class QuestionPagerActivity extends AppCompatActivity {

    private ViewPager questionViewPager;
    private List<Question> questions;

    private boolean questionsChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_pager);

        questionViewPager = findViewById(R.id.question_view_pager);
        questions = new ArrayList<>();

        FragmentManager fragmentManager = getSupportFragmentManager();
        questionViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                if (questionsChanged) {
                    notifyDataSetChanged();
                    questionsChanged = false;
                }

                Question q = questions.get(position);
                QuestionFragment cf = new QuestionFragment();
                cf.setQuestion(q);
                return cf;
            }

            @Override
            public int getCount() {
                if (questionsChanged) {
                    notifyDataSetChanged();
                    questionsChanged = false;
                }

                return questions.size();
            }
        });

        QuizHolder quiz = QuizHolder.getInstance();
        Query questionsInQuiz = FirebaseDatabase.getInstance().getReference("quizzes/" + quiz.getQuizId() + "/questions").orderByValue();

        questionsInQuiz.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(QuestionPagerActivity.this,
                        "onChildAdded: " + dataSnapshot.getKey(),
                        Toast.LENGTH_LONG).show();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("questions/" + dataSnapshot.getKey());
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Question q = dataSnapshot.getValue(Question.class);

                        questions.add(q);

                        questionsChanged = true;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(QuestionPagerActivity.this,
                        "onChildChanged",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Toast.makeText(QuestionPagerActivity.this,
                        "onChildRemoved",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(QuestionPagerActivity.this,
                        "onChildMoved",
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
}
