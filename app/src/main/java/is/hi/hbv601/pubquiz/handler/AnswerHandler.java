package is.hi.hbv601.pubquiz.handler;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import is.hi.hbv601.pubquiz.fragment.AnswerListenerFragment;
import is.hi.hbv601.pubquiz.model.QuizHolder;

public class AnswerHandler {
    private AnswerListenerFragment fragment;

    private String questionId;
    private String quizId;
    private String teamName;

    public AnswerHandler(String questionId, String quizId, String teamName, AnswerListenerFragment fragment)
    {
        this.fragment = fragment;
        this.questionId = questionId;
        this.quizId = quizId;
        this.teamName = teamName;

        DatabaseReference answerRef = FirebaseDatabase.getInstance().getReference("answers/" + quizId + "/" + questionId + "/" + teamName + "/");
        answerRef.addValueEventListener(new AnswerListener());
    }

    public void setAnswer(String answer)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(
                "answers/" + this.quizId + "/" + this.questionId + "/" + this.teamName + "/answer");

        // Save the answer to the db
        mDatabase.setValue(answer);
    }

    public void setAnswerIsCorrect(Boolean isCorrect)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(
                "answers/" + this.quizId + "/" + this.questionId + "/" + this.teamName + "/isCorrect");

        // Save the answer to the db
        mDatabase.setValue(isCorrect);
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
