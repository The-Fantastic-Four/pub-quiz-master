package is.hi.hbv601.pubquiz.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import is.hi.hbv601.pubquiz.R;
import is.hi.hbv601.pubquiz.handler.AnswerHandler;
import is.hi.hbv601.pubquiz.handler.QuestionHandler;
import is.hi.hbv601.pubquiz.model.Question;
import is.hi.hbv601.pubquiz.model.QuizHolder;


/**
 * Fragment for asking the user to review another teams answer
 * Created by viktoralex 03.04.2018
 */
public class ReviewFragment extends Fragment implements QuestionListenerFragment, AnswerListenerFragment {

    private Question question;
    private String reviewTeamName;

    private TextView questionNumber;
    private TextView questionText;
    private TextView questionAnswer;
    private Button correctAnswerButton;
    private Button incorrectAnswerButton;

    private AnswerHandler answerHandler;

    private String answer = "";
    private Boolean isCorrect;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_review, container, false);

        // Find the view items for referencing later
        questionNumber = v.findViewById(R.id.questionNumber);
        questionAnswer = v.findViewById(R.id.questionAnswer);
        questionText = v.findViewById(R.id.questionText);
        correctAnswerButton = v.findViewById(R.id.correctButton);
        incorrectAnswerButton = v.findViewById(R.id.incorrectButton);

        // Set and event for the answer question button
        correctAnswerButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Click again to reset
                if (isCorrect != null && isCorrect.booleanValue() == true)
                    answerHandler.setAnswerIsCorrect(null);
                else
                    answerHandler.setAnswerIsCorrect(true);
            }
        });
        incorrectAnswerButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Click again to reset
                if (isCorrect != null && isCorrect.booleanValue() == false)
                    answerHandler.setAnswerIsCorrect(null);
                else
                    answerHandler.setAnswerIsCorrect(false);
            }
        });

        updateView();

        return v;
    }

    // Used to set which question and answer this fragment is supposed to show
    public void setReviewQuestion(final String teamName, final String questionId, final long questionNumber)
    {
        reviewTeamName = teamName;

        // Listen to question
        new QuestionHandler(questionId, questionNumber, this);

        // Listen to answers
        QuizHolder quiz = QuizHolder.getInstance();
        answerHandler = new AnswerHandler(questionId, quiz.getQuizId(), teamName, this);
    }

    @Override
    public void updateAnswer(String answer, Boolean isCorrect) {
        this.answer = answer;
        this.isCorrect = isCorrect;
        updateView();
    }

    @Override
    public void updateQuestion(Question question) {
        this.question = question;
        updateView();
    }

    // Update the view (set the question text etc.) if the question and the view items have been loaded
    private void updateView()
    {
        if (question == null || !isAdded())
            return;

        if (questionText != null && !question.getType().equals("blank")) {
            questionText.setText(question.getQuestion());
        } else {
            // If either the text is null or it is a blank question show an empty string
            questionText.setText("");
        }

        if (questionNumber != null) {
            questionNumber.setText(String.format(
                    getResources().getString(R.string.question_number),
                    question.getNumber()));
        }

        if (questionAnswer != null) {
            questionAnswer.setText(answer);
        }

        if (isCorrect == null) {
            correctAnswerButton.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_green_light));
            incorrectAnswerButton.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_red_light));
        } else if (isCorrect.booleanValue() == true) {
            correctAnswerButton.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_green_light));
            incorrectAnswerButton.setBackgroundTintList(getResources().getColorStateList(android.R.color.darker_gray));
        } else {
            correctAnswerButton.setBackgroundTintList(getResources().getColorStateList(android.R.color.darker_gray));
            incorrectAnswerButton.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_red_light));
        }
    }
}
