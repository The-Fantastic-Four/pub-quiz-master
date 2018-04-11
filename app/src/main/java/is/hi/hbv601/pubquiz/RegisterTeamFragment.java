package is.hi.hbv601.pubquiz;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import is.hi.hbv601.pubquiz.model.QuizHolder;

/**
 * Fragment for the teams to register into a quiz
 * Created by ${Fannar} on 13.2.2018.
 */
public class RegisterTeamFragment extends Fragment {

    // View items
    private EditText teamNameTextView;
    private EditText roomNameTextView;


    // Find view items and set event listeners
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.fragment_register_team, container, false );

        // Find the team name input
        teamNameTextView = v.findViewById( R.id.etTeamName );
        //if team was registered before Qr scanner was opened it is set.
        String team = getActivity().getIntent().getStringExtra( "teamName" );
        teamNameTextView.setText( team );

        // Find the room name input
        roomNameTextView = v.findViewById( R.id.etRoomName );
        // if room name has been found with Qr scanners it is set.
        String qr = getActivity().getIntent().getStringExtra( "roomName" );
        roomNameTextView.setText( qr );

        // Find register button and hook and event listener on it
        Button sendTeam = v.findViewById(R.id.btTeamReg);
        AppCompatImageButton qrCode = v.findViewById( R.id.btQRCode );
        qrCode.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openQr();
            }
        } );
        sendTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the team and room names that the user wrote
                final String teamName = teamNameTextView.getText().toString();
                final String quizId = roomNameTextView.getText().toString();

                // Find all quizzes
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("quizzes/" + quizId);
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Check if the quiz exists
                        if (!dataSnapshot.exists()) {
                            Toast.makeText(RegisterTeamFragment.this.getContext(),
                                    "Quiz not found.",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        String status = dataSnapshot.child("status").getValue(String.class);

                        // Check if team can be created
                        DataSnapshot team = dataSnapshot.child("teams").child(teamName);
                        if (team.exists()) {
                            // Compare registered phone id to current phone id
                            if (team.getValue().toString().equals(getPhoneId())) {
                                // Is okay team is re-connecting
                                setQuiz(quizId, teamName);
                                openQuiz(status);

                            } else {
                                // Oh no, can't have the same name :(
                                Toast.makeText(RegisterTeamFragment.this.getContext(),
                                        "Team name already registered, sorry.",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            // Create the team and set the phone id
                            DatabaseReference newTeamRef = dataSnapshot.child("teams").child(teamName).getRef();
                            newTeamRef.setValue(getPhoneId());

                            setQuiz(quizId, teamName);
                            openQuiz(status);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(RegisterTeamFragment.this.getContext(),
                                "Connection cancelled, please try again.",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        } );
        return v;
    }

    // Get the phone id, used to authenticate teams for reconnection
    private String getPhoneId() {
        return Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    // Set the current quiz saved in memory as a singleton
    private void setQuiz(String quizId, String teamName) {
        QuizHolder quiz = QuizHolder.getInstance();
        quiz.setQuizId(quizId);
        quiz.setTeamName(teamName);
    }

    // Move over to the question activity
    private void openQuiz(String status) {
        Intent quizIntent;
        if (status == null) {
            quizIntent = new Intent(this.getActivity(), QuestionPagerActivity.class);
        } else if (status.equals("not started")) {
            quizIntent = new Intent(this.getActivity(), WaitActivity.class);
        } else if (status.equals("review")) {
            quizIntent = new Intent(this.getActivity(), ReviewPagerActivity.class);
        } else if (status.equals("in progress")) {
            quizIntent = new Intent(this.getActivity(), QuestionPagerActivity.class);
        } else if (status.equals("finished")) {
            quizIntent = new Intent(this.getActivity(), ScoreboardActivity.class);
        } else {
            quizIntent = new Intent(this.getActivity(), WaitActivity.class);
        }

        startActivity(quizIntent);
        //this.getActivity().finish();
    }

    //move over to Qr activity and send team name over
    private void openQr(){

         Intent QrCodeIntent = new Intent(RegisterTeamFragment.this.getActivity(), QRCodeActivity.class);
        QrCodeIntent.putExtra( "teamName", teamNameTextView.getText().toString());
        QrCodeIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(QrCodeIntent);

    }
}

