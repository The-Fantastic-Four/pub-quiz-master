package is.hi.hbv601.pubquiz;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import is.hi.hbv601.pubquiz.handler.QuizStatusHandler;
import is.hi.hbv601.pubquiz.model.QuizHolder;
import is.hi.hbv601.pubquiz.model.Score;
import is.hi.hbv601.pubquiz.utils.QuizIntent;

public class ScoreboardActivity extends Activity {

    private RecyclerView scoreboardView;
    private ScoreAdapter scoreAdapter;

    QuizStatusHandler quizStatusHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        scoreboardView = findViewById(R.id.scoreboardView);
        scoreboardView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch the questions in this quiz
        final QuizHolder quiz = QuizHolder.getInstance();

        // Switch to another activity if the quiz status changes
        quizStatusHandler = new QuizStatusHandler(quiz.getQuizId(), this);
        /*
        Query currentState = FirebaseDatabase.getInstance().getReference("quizzes/" + quiz.getQuizId() + "/status");
        currentState.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = dataSnapshot.getValue(String.class);

                if (status == null)
                    return;

                Intent nextIntent = QuizIntent.fromStatus(status, ScoreboardActivity.this);
                if (nextIntent != null) {
                    startActivity(nextIntent);
                    ScoreboardActivity.this.finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */

        // Switch to another activity if the quiz status changes
        Query quizAnswers = FirebaseDatabase.getInstance().getReference("answers/" + quiz.getQuizId());
        quizAnswers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Long> teamScores = new HashMap<>();

                for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot teamSnapshot : questionSnapshot.getChildren()) {
                        String teamName = teamSnapshot.getKey();

                        DataSnapshot correct = teamSnapshot.child("isCorrect");
                        if (correct.exists() && correct.getValue(Boolean.class)) {
                            if (teamScores.containsKey(teamName)) {
                                teamScores.put(teamName, teamScores.get(teamName) + 1);
                            } else {
                                teamScores.put(teamName, (long)1);
                            }
                        }
                    }
                }

                ArrayList<Long> uniqueScores = new ArrayList<>(new HashSet<>(teamScores.values()));
                Collections.sort(uniqueScores);
                Collections.reverse(uniqueScores);

                List<Score> scores = new ArrayList<>();
                for (String teamName : teamScores.keySet()) {
                    Long score = teamScores.get(teamName);
                    scores.add(new Score((long)uniqueScores.indexOf(score) + 1, teamName, score));
                }

                Collections.sort(scores, new Comparator<Score>() {
                    @Override
                    public int compare(Score s1, Score s2) {
                        return s1.getPlace().compareTo(s2.getPlace());
                    }
                });

                updateUI(scores);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void updateUI(List<Score> scores) {
        scoreAdapter = new ScoreAdapter(scores);
        scoreboardView.setAdapter(scoreAdapter);
    }

    private class ScoreHolder extends RecyclerView.ViewHolder {

        private Score score;

        private TextView teamPlace;
        private TextView teamName;
        private TextView teamScore;

        private ScoreHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_scoreboard, parent, false));

            teamPlace = itemView.findViewById(R.id.teamPlace);
            teamName = itemView.findViewById(R.id.teamName);
            teamScore = itemView.findViewById(R.id.teamScore);
        }

        public void bind(Score score) {
            this.score = score;

            teamPlace.setText(String.valueOf(this.score.getPlace()));
            teamName.setText(this.score.getTeamName());
            teamScore.setText(String.valueOf(this.score.getScore()));
        }
    }

    private class ScoreAdapter extends RecyclerView.Adapter<ScoreHolder> {

        private List<Score> scores;

        private ScoreAdapter(List<Score> scores) {
            this.scores = scores;
        }

        @Override
        public ScoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(ScoreboardActivity.this);
            return new ScoreHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ScoreHolder holder, int position) {
            Score score = scores.get(position);
            holder.bind(score);
        }

        @Override
        public int getItemCount() {
            return scores.size();
        }
    }
}
