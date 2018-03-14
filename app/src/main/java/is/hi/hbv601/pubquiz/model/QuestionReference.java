package is.hi.hbv601.pubquiz.model;

/**
 * Created by viktoralex on 14.3.2018.
 */

public class QuestionReference {
    private String questionId;
    private long questionNumber;

    public QuestionReference(String questionId, long questionNumber)
    {
        this.questionId = questionId;
        this.questionNumber = questionNumber;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public long getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(long questionNumber) {
        this.questionNumber = questionNumber;
    }
}
