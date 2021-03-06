package is.hi.hbv601.pubquiz.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Model used to store a question
 * Created by viktoralex on 14.3.2018.
 */
@IgnoreExtraProperties
public class Question {
    private String questionId;
    private String question;
    private boolean isPrivate;
    private String type;
    private long number;

    public Question()
    {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
