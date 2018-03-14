    package is.hi.hbv601.pubquiz;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import is.hi.hbv601.pubquiz.R;
import is.hi.hbv601.pubquiz.model.Question;

    /**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment {

    Question question;

    TextView questionNumber;
    TextView questionText;
    EditText questionAnswer;

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

        updateView();

        return v;
    }

    public void setQuestion(String questionId, final long questionNumber)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("questions/" + questionId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Question q = dataSnapshot.getValue(Question.class);
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
