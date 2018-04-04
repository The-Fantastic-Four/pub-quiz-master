package is.hi.hbv601.pubquiz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;


/**
 * Activity for the user to register a team
 * Created by viktoralex on 14.3.2018.
 */
public class RegisterTeamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_team);

        FragmentManager fm = getSupportFragmentManager();


        //String qr = getIntent().getStringExtra( "roomName" );
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new RegisterTeamFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment) // FrameLayout
                    .commit();
        }

    }

}
