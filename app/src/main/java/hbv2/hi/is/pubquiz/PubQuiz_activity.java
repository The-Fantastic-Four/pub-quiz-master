package hbv2.hi.is.pubquiz;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import android.widget.Button;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class PubQuiz_activity extends AppCompatActivity implements OnClickListener {

    EditText etTeamName,etRoomName;
    Button btTeamReg;
    Team team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_quiz_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find variables in UI
        etTeamName = (EditText) findViewById(R.id.etTeamName);
        etRoomName = (EditText) findViewById(R.id.etRoomName);
        btTeamReg = (Button) findViewById(R.id.btTeamReg);

        btTeamReg.setOnClickListener(this);

        // Auto generated
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void JsTeamPOST(String url, Team team){
        //server fra eid
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        String jsTeam = "";
        JSONObject jsObject = new JSONObject();

        try {
            jsObject.accumulate("teamName", team.getTeamName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsObject.accumulate("roomName", team.getRoomName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsTeam = jsObject.toString();
        StringEntity se = null;
        try {
            se = new StringEntity(jsTeam);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPost.setEntity(se);
        httpPost.setHeader("Content-type", "application/json");
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.btTeamReg:
                if(!validate())
                    Toast.makeText(getBaseContext(), "Fylltu i reitina!", Toast.LENGTH_LONG).show();
        }

    }
    private boolean validate(){
        if(etRoomName.getText().toString().trim().equals(""))
            return false;
        else if(etTeamName.getText().toString().trim().equals(""))
            return false;
        else
            return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pub_quiz_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
