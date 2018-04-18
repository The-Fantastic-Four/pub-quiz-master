package is.hi.hbv601.pubquiz.handler;

import android.app.Activity;
import android.content.Intent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import is.hi.hbv601.pubquiz.utils.QuizIntent;

public class QuizStatusHandler {

    private Activity activityInstance;

    public QuizStatusHandler(String quizId, Activity activityInstance) {
        this.activityInstance = activityInstance;

        Query currentState = FirebaseDatabase.getInstance().getReference("quizzes/" + quizId + "/status");
        currentState.addValueEventListener(new QuizStatusListener());
    }

    private class QuizStatusListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            String status = dataSnapshot.getValue(String.class);

            if (status == null)
                return;

            Intent nextIntent = QuizIntent.fromStatus(status, activityInstance);
            if (nextIntent != null) {
                nextIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activityInstance.startActivity(nextIntent);
                activityInstance.finish();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
