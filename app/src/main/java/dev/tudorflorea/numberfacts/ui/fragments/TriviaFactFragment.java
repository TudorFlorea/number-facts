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
import android.widget.TextView;

import dev.tudorflorea.numberfacts.R;
import dev.tudorflorea.numberfacts.data.Fact;
import dev.tudorflorea.numberfacts.data.FactFactory;
import dev.tudorflorea.numberfacts.utilities.InternetUtils;
import dev.tudorflorea.numberfacts.utilities.InterfaceUtils.FactListener;

/**
 * Created by Tudor on 2/10/2018.
 */

public class TriviaFactFragment extends Fragment implements LoaderManager.LoaderCallbacks<Fact>{

    private FactListener lisetner;

    public TriviaFactFragment() {

    }

    private final int TRIVIA_LOADER_ID = 1;
    private final String CURRENT_RANDOM_FACT = "current_trivia_fact";

    //private Button mRandomFactButton;
    private TextView mRandomFactTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trivia_fact, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //mRandomFactButton = (Button) view.findViewById(R.id.btn_random_fact);
        mRandomFactTextView = (TextView) view.findViewById(R.id.random_fact_tv);

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/lobster.ttf");
        mRandomFactTextView.setTypeface(face);

        if (!InternetUtils.isNetworkAvailable(getActivity())) {
            mRandomFactTextView.setText("No network available!");
        } else {
            Bundle args = getArguments();
            if (args == null) {
                getActivity().getSupportLoaderManager().restartLoader(TRIVIA_LOADER_ID, null, TriviaFactFragment.this);
            } else {
                getActivity().getSupportLoaderManager().restartLoader(TRIVIA_LOADER_ID, args, TriviaFactFragment.this);
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
        if (mRandomFactTextView != null) {
            outState.putString(CURRENT_RANDOM_FACT, mRandomFactTextView.getText().toString());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FactListener) {
            lisetner = (FactListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FactListener");
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
            mRandomFactTextView.setText("\u02EE" + fact.getText() + "\u02EE");
            Log.v("FROM RANDOM FRAGMENT: ",  fact.getText());
            lisetner.onFactRetrieved(fact);
        }
    }

    @Override
    public void onLoaderReset(Loader<Fact> loader) {

    }


}
