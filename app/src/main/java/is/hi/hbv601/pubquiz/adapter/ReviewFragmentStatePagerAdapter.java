package is.hi.hbv601.pubquiz.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import is.hi.hbv601.pubquiz.fragment.ReviewFragment;
import is.hi.hbv601.pubquiz.model.QuestionReference;

public class ReviewFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private final List<QuestionReference> questions;
    private final String reviewTeam;

    public ReviewFragmentStatePagerAdapter(String reviewTeam, List<QuestionReference> questions, FragmentManager manager)
    {
        super(manager);

        this.questions = questions;
        this.reviewTeam = reviewTeam;
    }

    @Override
    public Fragment getItem(int position) {
        QuestionReference question = questions.get(position);
        ReviewFragment rf = new ReviewFragment();
        rf.setReviewQuestion(reviewTeam, question.getQuestionId(), question.getQuestionNumber());
        return rf;
    }

    @Override
    public int getCount() {
        return questions.size();
    }
}
