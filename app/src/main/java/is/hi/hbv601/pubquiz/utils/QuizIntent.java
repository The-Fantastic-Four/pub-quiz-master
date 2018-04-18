package is.hi.hbv601.pubquiz.utils;

import android.app.Activity;
import android.content.Intent;

import is.hi.hbv601.pubquiz.activity.QuestionPagerActivity;
import is.hi.hbv601.pubquiz.activity.ReviewPagerActivity;
import is.hi.hbv601.pubquiz.activity.ScoreboardActivity;
import is.hi.hbv601.pubquiz.activity.WaitActivity;

public class QuizIntent {
    public static Intent fromStatus(String status, Activity activity) {
        switch (status) {
            case "not started":
                if (activity instanceof WaitActivity)
                    return null;
                return new Intent(activity, WaitActivity.class);
            case "in progress":
                if (activity instanceof QuestionPagerActivity)
                    return null;
                return new Intent(activity, QuestionPagerActivity.class);
            case "review":
                if (activity instanceof ReviewPagerActivity)
                    return null;
                return new Intent(activity, ReviewPagerActivity.class);
            case "finished":
                if (activity instanceof ScoreboardActivity)
                    return null;
                return new Intent(activity, ScoreboardActivity.class);
            default:
                throw new IllegalArgumentException("Quiz status not defined");
        }
    }
}
