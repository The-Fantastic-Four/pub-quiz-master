package is.hi.hbv601.pubquiz;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import is.hi.hbv601.pubquiz.R;
import is.hi.hbv601.pubquiz.model.Question;
import is.hi.hbv601.pubquiz.model.QuizHolder;
import is.hi.hbv601.pubquiz.model.Team;

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
    private Button leaveQuizButton;
    private EditText teamNameTextView;
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
        leaveQuizButton = v.findViewById( R.id.leaveQuizButton);


        // Set and event for the answer question button
        questionAnswerButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuizHolder quiz = QuizHolder.getInstance();
                final String teamName = teamNameTextView.getText().toString();
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

        /*
               leaveQuizButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuizHolder quiz = QuizHolder.getInstance();
                final String phoneId = getPhoneId();
                final String teamName = teamNameTextView.getText().toString();
                DatabaseReference mDatabaseTeam = FirebaseDatabase.getInstance().getReference(
                    "quizzes/" + quiz.getQuizId());


                mDatabaseTeam.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot teams = dataSnapshot.child( teamName );
                        if (teams.getValue().toString().equals( phoneId )) {
                            teams.getRef().removeValue();
                            leaveQuiz();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(QuestionFragment.this.getContext(),
                                "Connection cancelled, please try again.",
                                Toast.LENGTH_LONG).show();
                    }

                });
            }
        } );

         */

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

        if (questionText != null) {
            questionText.setText(question.getQuestion());
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

    private void leaveQuiz() {
        Intent leaveQuizIntent = new Intent(QuestionFragment.this.getActivity(), RegisterTeamActivity.class);
        leaveQuizIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(leaveQuizIntent);
    }

    private String getPhoneId() {
        return Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }


}
