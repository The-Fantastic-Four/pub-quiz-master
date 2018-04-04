package is.hi.hbv601.pubquiz.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Model used to the answer to a question
 * Created by viktoralex on 14.3.2018.
 */
@IgnoreExtraProperties
public class Answer {
    private String questionId;
    private String teamName;
    private String answer;
    private boolean isCorrect;

    public Answer()
    {
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
