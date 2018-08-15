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

public class FavoriteFactFragment extends Fragment {

    private InterfaceUtils.FactListener mListener;
    private Fact mFact;


    public FavoriteFactFragment() {

    }

    private final String CURRENT_FACT_STATE = "state";

    @BindView(R.id.fact_tv) TextView mFactTextView;


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

            if (savedInstanceState != null) {
                mFact = savedInstanceState.getParcelable(CURRENT_FACT_STATE);
                if (mFact != null) mFactTextView.setText(mFact.getText());
            } else {
                Bundle args = getArguments();
                DisplayFactBuilder builder = args.getParcelable(getString(R.string.fragment_arg_fact_builder));

                if (builder.hasFact()) {
                    mFact = builder.getFact();
                    mListener.onFactRetrieved(mFact);
                    mFactTextView.setText(mFact.getText());
                    mFactTextView.setContentDescription(mFact.getText());
                }
            }




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


}