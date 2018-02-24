package is.hi.hbv601.pubquiz.model;

/**
 * Created by viktoralex on 24.2.2018.
 */

public class Question {
    private long id;
    private String question;
    private int questionNumber;
    private int totalQuestions;
    private String type;

    public Question()
    {
    }

    public Question(long id, String question, int questionNumber, int totalQuestions, String type)
    {
        this.setId(id);
        this.setQuestion(question);
        this.setQuestionNumber(questionNumber);
        this.setTotalQuestions(totalQuestions);
        this.setType(type);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
