package dev.tudorflorea.numberfacts.ui.fragments;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import dev.tudorflorea.numberfacts.R;
import dev.tudorflorea.numberfacts.data.Fact;
import dev.tudorflorea.numberfacts.data.FactFactory;
import dev.tudorflorea.numberfacts.utilities.InterfaceUtils;
import dev.tudorflorea.numberfacts.utilities.InternetUtils;

/**
 * Created by Tudor on 2/10/2018.
 */

public class YearFactFragment extends Fragment implements LoaderManager.LoaderCallbacks<Fact> {

    private InterfaceUtils.FactListener mListener;

    public YearFactFragment() {

    }

    private final int YEAR_LOADER_ID = 3;
    private final String CURRENT_Year_FACT = "current_year_fact";

    //private Button mRandomFactButton;
    private TextView mYearFactTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_year_fact, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //mRandomFactButton = (Button) view.findViewById(R.id.btn_random_fact);
        mYearFactTextView = (TextView) view.findViewById(R.id.year_fact_tv);

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/lobster.ttf");
        mYearFactTextView.setTypeface(face);

        if (!InternetUtils.isNetworkAvailable(getActivity())) {
            mYearFactTextView.setText("No network available!");
        } else {
            Bundle args = getArguments();
            if (args == null) {
                getActivity().getSupportLoaderManager().restartLoader(YEAR_LOADER_ID, null, YearFactFragment.this);
            } else {
                getActivity().getSupportLoaderManager().restartLoader(YEAR_LOADER_ID, args, YearFactFragment.this);
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
                    return FactFactory.RandomYearFact();
                } else {
                    int number = args.getInt("number");
                    return FactFactory.YearFact(number);
                }

            }
        };
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof InterfaceUtils.FactListener) {
            mListener = (InterfaceUtils.FactListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + getResources().getString(R.string.err_no_fact_listener));
        }
    }

    @Override
    public void onLoadFinished(Loader<Fact> loader, Fact fact) {
        if (fact != null) {
            mYearFactTextView.setText(fact.getText());
            mListener.onFactRetrieved(fact);
        }
    }

    @Override
    public void onLoaderReset(Loader<Fact> loader) {

    }
}
