    package is.hi.hbv601.pubquiz;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import is.hi.hbv601.pubquiz.R;
import is.hi.hbv601.pubquiz.model.Question;

    /**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment {

    Question question;

    public QuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    public void setQuestion(Question q)
    {
        question = q;
        System.out.println("Got question " + question);
    }

}
