package is.hi.hbv601.pubquiz;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import is.hi.hbv601.pubquiz.fragment.AnswerListenerFragment;
import is.hi.hbv601.pubquiz.fragment.QuestionListernerFragment;
import is.hi.hbv601.pubquiz.handler.AnswerHandler;
import is.hi.hbv601.pubquiz.handler.QuestionHandler;
import is.hi.hbv601.pubquiz.model.Answer;
import is.hi.hbv601.pubquiz.model.Question;
import is.hi.hbv601.pubquiz.model.QuizHolder;

/**
 * Fragment for showing the user to read and answer a question
 * Created by viktoralex on 14.3.2018.
 */
public class QuestionFragment extends Fragment implements AnswerListenerFragment, QuestionListernerFragment {

    private Question question;

    private TextView questionNumber;
    private TextView questionText;
    private EditText questionAnswer;
    private ImageView imageView;

    private QuestionHandler questionHandler;
    private AnswerHandler answerHandler;

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
        Button questionAnswerButton = v.findViewById(R.id.questionAnswerButton);
        imageView = v.findViewById( R.id.picture_view );

        // Set and event for the answer question button
        questionAnswerButton.setOnClickListener(new View.OnClickListener() {
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
        questionHandler = new QuestionHandler(questionId, questionNumber, this);

        // Listen to answers
        QuizHolder quiz = QuizHolder.getInstance();
        answerHandler = new AnswerHandler(questionId, quiz.getQuizId(), quiz.getTeamName(), this);
    }

    @Override
    public void updateAnswer(String answer, Boolean isCorrect) {
        this.firebaseAnswer = answer;
        updateView();
    }

    @Override
    public void updateQuestion(Question question) {
        this.question = question;
        updateView();
    }

    // Update the view (set the question text etc.) if the question and the view items have been loaded
    private void updateView()
    {
        if (question == null || !isAdded())
            return;

        if (questionText != null && question.getType().equals("text")) {
            questionText.setText(question.getQuestion());
            imageView.setVisibility( View.GONE );
        } else if(question.getType().equals("picture")){
            imageView.setVisibility(View.VISIBLE);
            String url = question.getQuestion();
            //String url = "http://i.imgur.com/DvpvklR.png";
            Picasso.get().load( url ).into( imageView );
           // questionText.setText("thetta er mynd");

        }else{
            // If either the text is null or it is a blank question show an empty string
            questionText.setText("");
        }

        if (questionNumber != null) {
            questionNumber.setText(String.format(
                    getResources().getString(R.string.question_number),
                    question.getNumber()));
        }

        if (questionAnswer != null) {
            questionAnswer.setText(firebaseAnswer);
        }
    }
}