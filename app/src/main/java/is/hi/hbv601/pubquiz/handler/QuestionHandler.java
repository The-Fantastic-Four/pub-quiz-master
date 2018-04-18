package is.hi.hbv601.pubquiz.handler;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import is.hi.hbv601.pubquiz.fragment.QuestionListernerFragment;
import is.hi.hbv601.pubquiz.model.Question;

public class QuestionHandler {
    private QuestionListernerFragment fragment;
    private String questionId;
    private long questionNumber;

    public QuestionHandler(String questionId, long questionNumber, QuestionListernerFragment fragment)
    {
        this.fragment = fragment;
        this.questionId = questionId;
        this.questionNumber = questionNumber;

        DatabaseReference questionRef = FirebaseDatabase.getInstance().getReference("questions/" + questionId);
        questionRef.addValueEventListener(new QuestionListener());
    }

    private class QuestionListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Update the question if it changes
            Question q = dataSnapshot.getValue(Question.class);
            if (q != null) {
                q.setQuestionId(questionId);
                q.setNumber(questionNumber);
            }

            fragment.updateQuestion(q);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
