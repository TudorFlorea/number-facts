package dev.tudorflorea.numberfacts.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.tudorflorea.numberfacts.R;
import dev.tudorflorea.numberfacts.data.Fact;
import dev.tudorflorea.numberfacts.ui.FavoriteFactDetailsActivity;
import dev.tudorflorea.numberfacts.utilities.InterfaceUtils.FavoriteFactListener;

public class FavoriteFactsListAdapter extends RecyclerView.Adapter<FavoriteFactsListAdapter.FavoriteFactsListViewHolder> {

    private Context mContext;
    private ArrayList<Fact> mFacts;
    private FavoriteFactListener mListener;


    public FavoriteFactsListAdapter(Context context, ArrayList<Fact> facts, FavoriteFactListener listener) {
        mContext = context;
        mFacts = facts;
        mListener = listener;
    }

    @Override
    public FavoriteFactsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.favorite_facts_item, parent, false);
        return new FavoriteFactsListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(FavoriteFactsListViewHolder holder, int position) {
        holder.setData(mFacts.get(position));
    }

    @Override
    public int getItemCount() {
        return mFacts != null ? mFacts.size() : 0;
    }

    public class FavoriteFactsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final String TRIVIA_TYPE = "trivia";
        private final String YEAR_TYPE = "year";
        private final String DATE_TYPE = "date";
        private final String MATH_TYPE = "math";

        @BindView(R.id.favorite_number_tv) TextView mFavoriteNumberTextView;
        @BindView(R.id.favorite_number_type_iv) ImageView mFavoriteNumberTypeImageView;

        private Fact mFavoriteFact;

        public FavoriteFactsListViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void setData(Fact fact) {
            mFavoriteFact = fact;

            mFavoriteNumberTextView.setText(mFavoriteFact.getText());
            mFavoriteNumberTextView.setContentDescription(mFavoriteFact.getText());

            String factType = mFavoriteFact.getType();

            switch (factType) {
                case TRIVIA_TYPE:
                    mFavoriteNumberTypeImageView.setImageResource(R.drawable.ic_brain_48);
                    mFavoriteNumberTypeImageView.setContentDescription(mContext.getString(R.string.content_description_favorite_facts_item_icon_trivia));
                    break;
                case YEAR_TYPE:
                    mFavoriteNumberTypeImageView.setImageResource(R.drawable.ic_hourglass_48);
                    mFavoriteNumberTypeImageView.setContentDescription(mContext.getString(R.string.content_description_favorite_facts_item_icon_year));
                    break;
                case DATE_TYPE:
                    mFavoriteNumberTypeImageView.setImageResource(R.drawable.ic_calendar_48);
                    mFavoriteNumberTypeImageView.setContentDescription(mContext.getString(R.string.content_description_favorite_facts_item_icon_date));
                    break;
                case MATH_TYPE:
                    mFavoriteNumberTypeImageView.setImageResource(R.drawable.ic_pi_48);
                    mFavoriteNumberTypeImageView.setContentDescription(mContext.getString(R.string.content_description_favorite_facts_item_icon_math));
                    break;
                default:
                    mFavoriteNumberTypeImageView.setVisibility(View.GONE);
            }

        }

        @Override
        public void onClick(View view) {
            if (mListener != null) mListener.onFavoriteFactClick(mFavoriteFact, FavoriteFactDetailsActivity.class);
        }
    }

}
