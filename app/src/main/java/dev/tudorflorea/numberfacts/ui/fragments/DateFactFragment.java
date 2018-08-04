package dev.tudorflorea.numberfacts.ui.fragments;


import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dev.tudorflorea.numberfacts.R;
import dev.tudorflorea.numberfacts.data.Fact;
import dev.tudorflorea.numberfacts.data.FactFactory;
import dev.tudorflorea.numberfacts.utilities.InternetUtils;
import dev.tudorflorea.numberfacts.utilities.NetworkUtils;

/**
 * Created by Tudor on 2/15/2018.
 */

public class DateFactFragment extends Fragment implements LoaderManager.LoaderCallbacks<Fact> {

    public DateFactFragment() {

    }

    private final int DATE_LOADER_ID = 4;
    private final String CURRENT_RANDOM_FACT = "current_date_fact";

    private TextView mDateFactTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_fact, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //mRandomFactButton = (Button) view.findViewById(R.id.btn_random_fact);
        mDateFactTextView = (TextView) view.findViewById(R.id.date_fact_tv);

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/lobster.ttf");
        mDateFactTextView.setTypeface(face);

        if (!InternetUtils.isNetworkAvailable(getActivity())) {
            mDateFactTextView.setText("No network available!");
        } else {
            Bundle args = getArguments();
            if (args == null) {
                getActivity().getSupportLoaderManager().restartLoader(DATE_LOADER_ID, null, DateFactFragment.this);
            } else {
                getActivity().getSupportLoaderManager().restartLoader(DATE_LOADER_ID, args, DateFactFragment.this);
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
                    return FactFactory.RandomDateFact();
                } else {
                    int day = args.getInt("day");
                    int month = args.getInt("month");
                    return FactFactory.DateFact(month, day);
                }


            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Fact> loader, Fact fact) {
        if (fact != null) {
            mDateFactTextView.setText('"' + fact.getText() + '"');
            Log.v("FROM RANDOM FRAGMENT: ",  fact.getText());
        }
    }

    @Override
    public void onLoaderReset(Loader<Fact> loader) {

    }
}
