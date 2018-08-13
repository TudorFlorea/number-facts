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
import dev.tudorflorea.numberfacts.data.DisplayFactBuilder;
import dev.tudorflorea.numberfacts.data.Fact;
import dev.tudorflorea.numberfacts.data.FactFactory;
import dev.tudorflorea.numberfacts.utilities.Constants;
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
    private final String CURRENT_FACT_STATE = "state";

    @BindView(R.id.fact_tv) TextView mFactTextView;
    @BindView(R.id.fact_pb) ProgressBar mProgressBar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fact, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                getString(R.string.font_lobster_path));
        mFactTextView.setTypeface(face);

        if (!InternetUtils.isNetworkAvailable(getActivity())) {
            mFactTextView.setText(R.string.no_internet_text);
        } else {
            if (savedInstanceState != null) {
                mFact = savedInstanceState.getParcelable(CURRENT_FACT_STATE);
                if (mFact != null) mFactTextView.setText(mFact.getText());
            } else {
                Bundle args = getArguments();
                DisplayFactBuilder builder = args.getParcelable(getString(R.string.fragment_arg_fact_builder));

                if (builder.hasFact()) {
                    mFact = builder.getFact();
                    mFactTextView.setText(mFact.getText());
                } else if (builder.hasQuery()) {
                    getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, args, FactFragment.this);
                } else {
                    getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, FactFragment.this);
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
                    + getResources().getString(R.string.err_no_fact_listener));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

            outState.putParcelable(CURRENT_FACT_STATE, mFact);

    }

    @Override
    public Loader<Fact> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Fact>(getContext()) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                mProgressBar.setVisibility(View.VISIBLE);
                mFactTextView.setVisibility(View.GONE);
                forceLoad();
            }

            @Override
            public Fact loadInBackground() {
                if (args == null) {
                    return FactFactory.RandomTriviaFact();
                } else {

                    DisplayFactBuilder builder = args.getParcelable(Constants.FRAGMENT_ARGS_FACT_BUILDER);
                    int queryType = builder.getQueryType();
                    int number;

                    switch (queryType) {
                        case DisplayFactBuilder.QUERY_RANDOM_TRIVIA:
                            return FactFactory.RandomTriviaFact();

                        case DisplayFactBuilder.QUERY_RANDOM_MATH:
                            return FactFactory.RandomMathFact();

                        case DisplayFactBuilder.QUERY_RANDOM_YEAR:
                            return FactFactory.RandomYearFact();

                        case DisplayFactBuilder.QUERY_RANDOM_DATE:
                            return FactFactory.RandomDateFact();

                        case DisplayFactBuilder.QUERY_TRIVIA_NUMBER:
                            number = builder.getNumber();
                            return FactFactory.TriviaFact(number);

                        case DisplayFactBuilder.QUERY_MATH_NUMBER:
                            number = builder.getNumber();
                            return FactFactory.MathFact(number);

                        case DisplayFactBuilder.QUERY_YEAR_NUMBER:
                            number = builder.getNumber();
                            return FactFactory.YearFact(number);

                        case DisplayFactBuilder.QUERY_DATE_NUMBER:
                            int day = builder.getDay();
                            int month = builder.getMonth();
                            return FactFactory.DateFact(month, day);

                        default:
                            return FactFactory.RandomTriviaFact();
                    }

                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Fact> loader, Fact fact) {
        if (fact != null) {
            mFactTextView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mFact = fact;
            mFactTextView.setText(fact.getText());
            mListener.onFactRetrieved(fact);

        }
    }

    @Override
    public void onLoaderReset(Loader<Fact> loader) {

    }


}
