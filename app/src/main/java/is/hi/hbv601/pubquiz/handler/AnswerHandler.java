package is.hi.hbv601.pubquiz.handler;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import is.hi.hbv601.pubquiz.fragment.AnswerListenerFragment;

public class AnswerHandler {
    private AnswerListenerFragment fragment;

    public AnswerHandler(String questionId, String quizId, String teamName, AnswerListenerFragment fragment)
    {
        this.fragment = fragment;

        DatabaseReference answerRef = FirebaseDatabase.getInstance().getReference("answers/" + quizId + "/" + questionId + "/" + teamName + "/");
        answerRef.addValueEventListener(new AnswerListener());
    }

    private class AnswerListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Set the answer string
            String answer = dataSnapshot.child("answer").getValue(String.class);
            Boolean isCorrect = dataSnapshot.child("isCorrect").getValue(Boolean.class);

            fragment.updateAnswer(answer, isCorrect);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
