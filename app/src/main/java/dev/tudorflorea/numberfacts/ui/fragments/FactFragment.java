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
import dev.tudorflorea.numberfacts.utilities.InterfaceUtils;
import dev.tudorflorea.numberfacts.utilities.InternetUtils;

/**
 * TODO - handle Fact as argument when it comes from the widget or from a notification and display that fact
 * TODO - handle all types of facts?
 * TODO - handle configuration changes to keep the fact between configuration changes
 *
 */
public class FactFragment extends Fragment implements LoaderManager.LoaderCallbacks<Fact>{

    private InterfaceUtils.FactListener mListener;
    private Fact mFact;


    public FactFragment() {

    }

    private final int LOADER_ID = 1;
    private final String CURRENT_FACT = "current_trivia_fact";
    private final String ARGUMENT_TYPE_TAG = "argument_type";
    private final String ARGUMENT_FACT_ATTACHED = "fact_attached";
    private final String ARGUMENT_QUERY_API = "query_api";
    private final String QUERY_API_TAG = "query_api_tag";
    private final String QUERY_API_RANDOM_TRIVIA = "random_trivia";
    private final String QUERY_API_RANDOM_YEAR = "random_year";
    private final String QUERY_API_RANDOM_MATH = "random_math";
    private final String QUERY_API_RANDOM_DATE = "random_date";


    private TextView mFactTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trivia_fact, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFactTextView = (TextView) view.findViewById(R.id.random_fact_tv);

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/lobster.ttf");
        mFactTextView.setTypeface(face);

        if (!InternetUtils.isNetworkAvailable(getActivity())) {
            mFactTextView.setText(R.string.no_internet_text);
        } else {
            if (savedInstanceState != null) {
                mFact = savedInstanceState.getParcelable(CURRENT_FACT);
                mFactTextView.setText(mFact.getText());
            } else {

                Bundle args = getArguments();

                if (args == null) {
                    getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, FactFragment.this);
                } else {
                    getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, args, FactFragment.this);
                }
            }

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof InterfaceUtils.FactListener) {
            mListener = (InterfaceUtils.FactListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FactListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

            outState.putParcelable(CURRENT_FACT, mFact);

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
            mFact = fact;
            mFactTextView.setText(fact.getText());
            mListener.onFactRetrieved(fact);
        }
    }

    @Override
    public void onLoaderReset(Loader<Fact> loader) {

    }


}
