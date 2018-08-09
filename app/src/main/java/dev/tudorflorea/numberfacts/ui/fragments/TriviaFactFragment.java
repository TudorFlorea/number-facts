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
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.tudorflorea.numberfacts.R;
import dev.tudorflorea.numberfacts.data.Fact;
import dev.tudorflorea.numberfacts.data.FactFactory;
import dev.tudorflorea.numberfacts.utilities.InternetUtils;
import dev.tudorflorea.numberfacts.utilities.InterfaceUtils.FactListener;

/**
 * Created by Tudor on 2/10/2018.
 */

public class TriviaFactFragment extends Fragment implements LoaderManager.LoaderCallbacks<Fact>{

    private FactListener mListener;

    public TriviaFactFragment() {

    }

    private final int TRIVIA_LOADER_ID = 1;
    private final String CURRENT_RANDOM_FACT = "current_trivia_fact";


    @BindView(R.id.random_fact_tv) TextView mTriviaFactTextView;
    @BindView(R.id.fact_pb) ProgressBar mProgressBar;
    private Fact mFact;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trivia_fact, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgressBar = view.findViewById(R.id.fact_pb);

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/lobster.ttf");
        mTriviaFactTextView.setTypeface(face);

        if (!InternetUtils.isNetworkAvailable(getActivity())) {
            mTriviaFactTextView.setText(getResources().getString(R.string.err_no_network_available));
        } else {

            if (savedInstanceState != null) {
                mFact = savedInstanceState.getParcelable(CURRENT_RANDOM_FACT);
                mTriviaFactTextView.setText(mFact.getText());
            } else {
                Bundle args = getArguments();

                if (args == null) {
                    getActivity().getSupportLoaderManager().restartLoader(TRIVIA_LOADER_ID, null, TriviaFactFragment.this);
                } else {
                    getActivity().getSupportLoaderManager().restartLoader(TRIVIA_LOADER_ID, args, TriviaFactFragment.this);
                }
            }



        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
            outState.putParcelable(CURRENT_RANDOM_FACT, mFact);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FactListener) {
            mListener = (FactListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + getResources().getString(R.string.err_no_fact_listener));
        }
    }

    @Override
    public Loader<Fact> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Fact>(getContext()) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                mProgressBar.setVisibility(View.VISIBLE);
                mTriviaFactTextView.setVisibility(View.GONE);
                forceLoad();
            }

            @Override
            public Fact loadInBackground() {
                if (args == null) {
                    return FactFactory.RandomTriviaFact();
                } else {
                    int factNumber = args.getInt("number");
                    return FactFactory.TriviaFact(factNumber);
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Fact> loader, Fact fact) {
        if (fact != null) {
            mTriviaFactTextView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mTriviaFactTextView.setText(fact.getText());
            mFact = fact;
            mListener.onFactRetrieved(fact);
        }
    }

    @Override
    public void onLoaderReset(Loader<Fact> loader) {

    }


}
