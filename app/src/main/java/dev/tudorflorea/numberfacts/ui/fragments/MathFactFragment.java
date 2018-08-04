package dev.tudorflorea.numberfacts.ui.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import dev.tudorflorea.numberfacts.R;
import dev.tudorflorea.numberfacts.data.Fact;
import dev.tudorflorea.numberfacts.data.FactFactory;
import dev.tudorflorea.numberfacts.utilities.InternetUtils;
import dev.tudorflorea.numberfacts.utilities.InterfaceUtils.FactListener;

/**
 * Created by Tudor on 2/10/2018.
 */


public class MathFactFragment extends Fragment implements LoaderManager.LoaderCallbacks<Fact> {

    public MathFactFragment() {

    }

    private final int MATH_LOADER_ID = 2;
    private final String CURRENT_MATH_FACT = "current_math_fact";

    private Button mRandomFactButton;
    private TextView mMathFactTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_math_fact, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //mRandomFactButton = (Button) view.findViewById(R.id.btn_random_fact);
        mMathFactTextView = (TextView) view.findViewById(R.id.math_fact_tv);

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/lobster.ttf");
        mMathFactTextView.setTypeface(face);

        if (!InternetUtils.isNetworkAvailable(getActivity())) {
            mMathFactTextView.setText("No network available!");
        } else {
            Bundle args = getArguments();
            if (args == null) {
                getActivity().getSupportLoaderManager().restartLoader(MATH_LOADER_ID, null, MathFactFragment.this);
            } else {
                getActivity().getSupportLoaderManager().restartLoader(MATH_LOADER_ID, args, MathFactFragment.this);
            }
        }
    }


    @Override
    public Loader<Fact> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Fact>(getContext()) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public Fact loadInBackground() {
                if (args == null) {
                    return FactFactory.RandomMathFact();
                } else {
                    int number = args.getInt("number");
                    return FactFactory.MathFact(number);
                }

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Fact> loader, Fact fact) {
        if (fact != null) {
            mMathFactTextView.setText(fact.getText());
        }
    }

    @Override
    public void onLoaderReset(Loader<Fact> loader) {

    }
}
