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
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment {

    Question question;

    TextView questionNumber;
    TextView questionText;
    EditText questionAnswer;
    Button questionAnswerButton;

    public QuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_question, container, false);

        //Set textview for team name in UI and setting listeners.
        questionNumber = v.findViewById(R.id.questionNumber);
        questionAnswer = v.findViewById(R.id.questionAnswer);
        questionText = v.findViewById(R.id.questionText);
        questionAnswerButton = v.findViewById(R.id.questionAnswerButton);

        questionAnswerButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuizHolder quiz = QuizHolder.getInstance();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(
                        "answers/" + quiz.getQuizId() + "/" + question.getQuestionId() + "/" + quiz.getTeamName() + "/answer");

                mDatabase.setValue(questionAnswer.getText().toString());

                ((QuestionPagerActivity)QuestionFragment.this.getActivity()).nextQuestion();
            }
        });

        updateView();

        return v;
    }

    public void setQuestion(final String questionId, final long questionNumber)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("questions/" + questionId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Question q = dataSnapshot.getValue(Question.class);
                q.setQuestionId(questionId);
                q.setNumber(questionNumber);

                question = q;

                updateView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateView()
    {
        if (question == null)
            return;

        if (questionText != null) {
            questionText.setText(question.getQuestion());
        }

        if (questionNumber != null) {
            questionNumber.setText(String.format(
                    getResources().getString(R.string.question_number),
                    question.getNumber()));
        }
    }

}
