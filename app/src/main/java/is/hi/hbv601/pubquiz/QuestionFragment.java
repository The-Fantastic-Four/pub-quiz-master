package is.hi.hbv601.pubquiz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import is.hi.hbv601.pubquiz.R;
import is.hi.hbv601.pubquiz.model.Question;
import is.hi.hbv601.pubquiz.model.QuizHolder;

/**
 * Fragment for showing the user to read and answer a question
 * Created by viktoralex on 14.3.2018.
 */
public class QuestionFragment extends Fragment {

    private Question question;

    private TextView questionNumber;
    private TextView questionText;
    private EditText questionAnswer;
    private Button questionAnswerButton;

    private String firebaseAnswer = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_question, container, false);

        // Find the view items for referencing later
        questionNumber = v.findViewById(R.id.questionNumber);
        questionAnswer = v.findViewById(R.id.questionAnswer);
        questionText = v.findViewById(R.id.questionText);
        questionAnswerButton = v.findViewById(R.id.questionAnswerButton);

        // Set and event for the answer question button
        questionAnswerButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuizHolder quiz = QuizHolder.getInstance();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(
                        "answers/" + quiz.getQuizId() + "/" + question.getQuestionId() + "/" + quiz.getTeamName() + "/answer");

                // Save the answer to the db
                mDatabase.setValue(questionAnswer.getText().toString());

                // Send the user to the next question (if there is any)
                ((QuestionPagerActivity)QuestionFragment.this.getActivity()).nextQuestion();
            }
        });

        updateView();

        return v;
    }

    // Used to set which question this fragment is supposed to show
    public void setQuestion(final String questionId, final long questionNumber)
    {
        // Listen to question
        DatabaseReference questionRef = FirebaseDatabase.getInstance().getReference("questions/" + questionId);
        questionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Update the question if it changes
                Question q = dataSnapshot.getValue(Question.class);
                if (q != null) {
                    q.setQuestionId(questionId);
                    q.setNumber(questionNumber);
                }

                question = q;

                // Update the view if the view items have been set
                updateView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Listen to answers
        QuizHolder quiz = QuizHolder.getInstance();
        DatabaseReference answerRef = FirebaseDatabase.getInstance().getReference("answers/" + quiz.getQuizId() + "/" + questionId + "/" + quiz.getTeamName() + "/answer");
        answerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Set the answer string
                firebaseAnswer = dataSnapshot.getValue(String.class);

                // Update the view if the view items have been set
                updateView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Update the view (set the question text etc.) if the question and the view items have been loaded
    private void updateView()
    {
        if (question == null)
            return;

        if (questionText == null ||
                questionNumber == null ||
                questionAnswer == null ||
                questionAnswerButton == null)
            return;

        questionNumber.setText(String.format(
                getResources().getString(R.string.question_number),
                question.getNumber()));

        questionAnswer.setText(firebaseAnswer);

        if ("text".equals(question.getType()))
        {
            questionText.setVisibility(View.VISIBLE);
            questionText.setText(question.getQuestion());
        }
        else // question is blank
        {
            questionText.setVisibility(View.GONE);
        }
    }

}
