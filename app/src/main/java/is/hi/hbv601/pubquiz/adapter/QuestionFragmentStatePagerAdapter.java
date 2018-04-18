package is.hi.hbv601.pubquiz.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import is.hi.hbv601.pubquiz.fragment.QuestionFragment;
import is.hi.hbv601.pubquiz.model.QuestionReference;

public class QuestionFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private final List<QuestionReference> questions;

    public QuestionFragmentStatePagerAdapter(List<QuestionReference> questions, FragmentManager manager)
    {
        super(manager);

        this.questions = questions;
    }

    @Override
    public Fragment getItem(int position) {
        QuestionReference question = questions.get(position);

        QuestionFragment cf = new QuestionFragment();
        cf.setQuestion(question.getQuestionId(), question.getQuestionNumber());
        return cf;
    }

    @Override
    public int getCount() {
        return questions.size();
    }
}
