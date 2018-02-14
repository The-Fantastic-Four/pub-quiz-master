package is.hi.hbv601.pubquiz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

// activity class to link with fragment container
public class PubQuiz_activity extends SingleFragmentActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_quiz_activity);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new PubQuiz_Fragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment) // FrameLayout
                    .commit();
        }

    }
    @Override
    protected Fragment createFragment() {
        return new PubQuiz_Fragment();
    }

}
