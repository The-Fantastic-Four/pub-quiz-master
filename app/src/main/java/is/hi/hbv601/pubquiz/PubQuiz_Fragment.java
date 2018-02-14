package is.hi.hbv601.pubquiz;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
public class PubQuiz_Fragment extends Fragment{

    //instance variables
    private EditText teamName;
    private EditText roomName;
    private Button sendTeam;
    private Team team;

    //runs when the fragment is created
    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        team = new Team();

    }
    //runs when fragments is created and initializes UI objects
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.register_team, container, false );

        //Set textview for team name in UI and setting listeners.
        teamName = (EditText) v.findViewById( R.id.etTeamName );
        teamName.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                team.setTeamName( charSequence.toString() );
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        } );
        // set textview for room name and setting listener
        roomName = (EditText) v.findViewById( R.id.etRoomName );
        roomName.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                team.setRoomName( charSequence.toString() );
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        } );

        //initalize RegisterTeamHandler
        final RegisterTeamHandler registerTeamHandler = new RegisterTeamHandler();
        //initilize btn and set listener
        sendTeam = (Button) v.findViewById( R.id.btTeamReg );
        sendTeam.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerTeamHandler.execute();
            }
        } );
        return v;
    }

    //Class for sending json object to server
    public class RegisterTeamHandler extends AsyncTask {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Object doInBackground(Object[] objects) {

            //making a jsonObject with room name and team name
            JSONObject t = new JSONObject();
            try {
                t.accumulate( "id", "4" );
                t.accumulate( "room_name", roomName.getText() );
                t.accumulate( "team_name", teamName.getText() );
                t.accumulate( "phone_id", "2" );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //creating a post request
            MediaType json = MediaType.parse("application/json; charset=utf-8");
            Request request = new Request.Builder()
                    .url("https://pub-quiz-server.herokuapp.com/register_team")
                    .post(RequestBody.create(json, t.toString()))
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        //Handles sending team creation to server
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute( o );

            Intent answerQuestionIntent = new Intent(PubQuiz_Fragment.this.getActivity(), AnswerQuestionActivity.class);
            startActivity(answerQuestionIntent);
        }
    }
}

