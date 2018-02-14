package hbv2.hi.is.pubquiz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;


/**
 * Created by ${Fannar} on 13.2.2018.
 */

public class PubQuiz_Fragment extends Fragment{

    private EditText teamName;
    private EditText roomName;
    private Button sendTeam;
    private Team team;
    private JsonPost post;

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        team = new Team();

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.register_team, container, false);

        teamName = (EditText) v.findViewById(R.id.etTeamName);
        teamName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                team.setTeamName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        roomName = (EditText) v.findViewById(R.id.etRoomName);
        roomName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                team.setRoomName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sendTeam = (Button) v.findViewById(R.id.btTeamReg);
        return v;
    }


        public void onClick(View v) throws IOException {
            if (v == sendTeam)
            {
                if (team.getTeamName().equals("") || team.getRoomName().equals(""))
                {
                    alertMessage(v);
                }
                else
                {
                    String json = "23";
                    String url;
                    url = "www.fd.com";
                    post.post(url, json);                }
            }
            return;

        }




    public void alertMessage(View v) {
        Toast.makeText(getActivity(),
                " fylltu í alla reiti!",Toast.LENGTH_SHORT)
                .show();
    }

}
