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
import android.widget.Toast;
import is.hi.hbv601.pubquiz.AnswerQuestionActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import is.hi.hbv601.pubquiz.model.QuizHolder;

/**
 * Created by ${Fannar} on 13.2.2018.
 */

/*
fragment for team registration
 */
public class RegisterTeamFragment extends Fragment{

    //instance variables
    private EditText teamNameTextView;
    private EditText roomNameTextView;
    private Button sendTeam;

    //runs when the fragment is created
    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }
    //runs when fragments is created and initializes UI objects
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.register_team, container, false );

        //Set textview for team name in UI and setting listeners.
        teamNameTextView = v.findViewById( R.id.etTeamName );

        // set textview for room name and setting listener
        roomNameTextView = v.findViewById( R.id.etRoomName );

        //initilize btn and set listener
        sendTeam = v.findViewById( R.id.btTeamReg );
        sendTeam.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String teamName = teamNameTextView.getText().toString();
                final String quizId = roomNameTextView.getText().toString();

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("quizzes");

                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //
                        DataSnapshot quiz = dataSnapshot.child(quizId);
                        if (quiz.exists()) {
                            DataSnapshot team = quiz.child("teams").child(teamName);
                            if (team.exists()) {

                                // Compare registered phone id to current phone id
                                if (team.getValue().toString().equals(getPhoneId())) {
                                    // Is okay team is re-connecting
                                    Toast.makeText(RegisterTeamFragment.this.getContext(),
                                            "Is ok, reconnect.",
                                            Toast.LENGTH_LONG).show();
                                    setQuiz(quizId, teamName);
                                    openQuiz();

                                } else {
                                    // Oh no, can't have the same name :(
                                    Toast.makeText(RegisterTeamFragment.this.getContext(),
                                            "Team name already registered, sorry.",
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                DatabaseReference newTeamRef = quiz.child("teams").child(teamName).getRef();
                                newTeamRef.setValue(getPhoneId());
                                Toast.makeText(RegisterTeamFragment.this.getContext(),
                                        "Created new team.",
                                        Toast.LENGTH_LONG).show();

                                setQuiz(quizId, teamName);
                                openQuiz();
                            }

                        } else {
                            Toast.makeText(RegisterTeamFragment.this.getContext(),
                                    "Quiz not found.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(RegisterTeamFragment.this.getContext(),
                                "Connection cancelled, please try again.",
                                Toast.LENGTH_LONG).show();
                    }
                });
                Intent i = new Intent(v.getContext(), AnswerQuestionActivity.class);
                startActivity(i);
            }
        } );
        return v;
    }

    private String getPhoneId() {
        return Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private void setQuiz(String quizId, String teamName) {
        QuizHolder quiz = QuizHolder.getInstance();
        quiz.setQuizId(quizId);
        quiz.setTeamName(teamName);
    }

    private void openQuiz() {
        Intent answerQuestionIntent = new Intent(RegisterTeamFragment.this.getActivity(), QuestionPagerActivity.class);
        answerQuestionIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(answerQuestionIntent);
    }
}

