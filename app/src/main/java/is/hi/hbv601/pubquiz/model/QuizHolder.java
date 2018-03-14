package is.hi.hbv601.pubquiz.model;

/**
 * Created by viktoralex on 13.3.2018.
 */

public class QuizHolder {

    private String quizId;
    private String teamName;

    private static final QuizHolder holder = new QuizHolder();
    public static QuizHolder getInstance() {
        return holder;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
