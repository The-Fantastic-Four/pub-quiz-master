package is.hi.hbv601.pubquiz;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
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

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ${Fannar} on 13.2.2018.
 */

/*
fragment for team registration
 */
public class RegisterTeamFragment extends Fragment{

    //instance variables
    private EditText teamName;
    private EditText roomName;
    private Button sendTeam;

    private DatabaseReference mDatabase;

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
        teamName = v.findViewById( R.id.etTeamName );

        // set textview for room name and setting listener
        roomName = v.findViewById( R.id.etRoomName );

        //initilize btn and set listener
        sendTeam = v.findViewById( R.id.btTeamReg );
        sendTeam.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("quizzes");

                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //
                        DataSnapshot quiz = dataSnapshot.child(getRoomName());
                        if (quiz.exists()) {
                            DataSnapshot team = quiz.child("teams").child(getTeamName());
                            if (team.exists()) {
                                if (team.getValue().toString().equals(getPhoneId())) {
                                    // Is okay team is re-connecting
                                    Toast.makeText(RegisterTeamFragment.this.getContext(),
                                            "Is ok, reconnect.",
                                            Toast.LENGTH_LONG).show();

                                } else {
                                    // Oh no, can't have the same name :(
                                    Toast.makeText(RegisterTeamFragment.this.getContext(),
                                            "Oh no, cannot use team name :( \n'" + team.getValue().toString() + "' != '" + getPhoneId() + "'",
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                DatabaseReference newTeamRef = quiz.child("teams").child(teamName.getText().toString()).getRef();
                                newTeamRef.setValue(getPhoneId());
                                Toast.makeText(RegisterTeamFragment.this.getContext(),
                                        "Created new team.",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(RegisterTeamFragment.this.getContext(),
                                    "Quiz not found.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        } );
        return v;
    }

    private String getTeamName() {
        return teamName.getText().toString();
    }

    private String getRoomName() {
        return roomName.getText().toString();
    }

    private String getPhoneId() {
        return Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}

