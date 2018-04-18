package is.hi.hbv601.pubquiz.handler;

import android.support.v4.view.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class QuestionPageHandler {
    public QuestionPageHandler(String quizId, final ViewPager questionPager) {

        Query currentQuestion = FirebaseDatabase.getInstance().getReference("quizzes/" + quizId + "/currentQuestion");
        currentQuestion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int currentQuestion = dataSnapshot.getValue(Integer.class);

                // Decrease by one so that we don't look like such nerds
                // (currentQuestion is not zero indexed, item number is)
                questionPager.setCurrentItem(currentQuestion - 1, true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
