package is.hi.hbv601.pubquiz.handler;

import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

import is.hi.hbv601.pubquiz.model.QuestionReference;

public class QuizQuestionHandler {
    private final List<QuestionReference> questions;
    private final FragmentStatePagerAdapter adapter;

    public QuizQuestionHandler(String quizId, List<QuestionReference> questions, FragmentStatePagerAdapter adapter) {
        this.questions = questions;
        this.adapter = adapter;

        // Fetch the questions in this quiz
        Query questionsInQuiz = FirebaseDatabase.getInstance().getReference("quizzes/" + quizId + "/questions").orderByValue();

        // When the questions change add to the list and let the fragment pager adapter know the
        // data has changed
        questionsInQuiz.addChildEventListener(new QuestionChildEventListener());
    }

    private class QuestionChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            long questionNumber = Long.parseLong(dataSnapshot.getValue().toString());
            questions.add(new QuestionReference(dataSnapshot.getKey(), questionNumber));

            adapter.notifyDataSetChanged();
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
    }
}
