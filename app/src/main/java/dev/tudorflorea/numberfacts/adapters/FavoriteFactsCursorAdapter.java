package dev.tudorflorea.numberfacts.adapters;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.tudorflorea.numberfacts.data.FactFactory;
import dev.tudorflorea.numberfacts.ui.FavoriteFactDetailsActivity;
import dev.tudorflorea.numberfacts.utilities.InterfaceUtils.FavoriteFactListener;
import dev.tudorflorea.numberfacts.data.Fact;
import dev.tudorflorea.numberfacts.database.FactContract.FactEntry;
import dev.tudorflorea.numberfacts.R;


public class FavoriteFactsCursorAdapter extends CursorRecyclerViewAdapter<FavoriteFactsCursorAdapter.FavoriteFactViewHolder>{

    private Context mContext;
    private Cursor mCursor;
    private FavoriteFactListener mListener;

    public FavoriteFactsCursorAdapter(Context context, Cursor cursor, FavoriteFactListener listener) {
        super(context, cursor);
        mContext = context;
        mCursor = cursor;
        mListener = listener;
    }


    @Override
    public FavoriteFactsCursorAdapter.FavoriteFactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.favorite_facts_item, parent, false);

        return new FavoriteFactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteFactViewHolder viewHolder, Cursor cursor) {
        cursor.moveToPosition(cursor.getPosition());
        viewHolder.setData(cursor);
    }


    public class FavoriteFactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final String TRIVIA_TYPE = "trivia";
        private final String YEAR_TYPE = "year";
        private final String DATE_TYPE = "date";
        private final String MATH_TYPE = "math";

        @BindView(R.id.favorite_number_tv) TextView mFavoriteNumberTextView;
        @BindView(R.id.favorite_number_type_iv) ImageView mFavoriteNumberTypeImageView;

        private Fact mFavoriteFact;

        public FavoriteFactViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            ButterKnife.bind(this, view);

        }

        public void setData(Cursor cursor) {

            long id = cursor.getLong(cursor.getColumnIndex(FactEntry._ID));
            String factText = cursor.getString(cursor.getColumnIndex(FactEntry.COLUMN_FACT));
            Integer factNumber = cursor.getInt(cursor.getColumnIndex(FactEntry.COLUMN_NUMBER));
            boolean factFound = cursor.getInt(cursor.getColumnIndex(FactEntry.COLUMN_NUMBER)) > 0;
            String factType = cursor.getString(cursor.getColumnIndex(FactEntry.COLUMN_TYPE));
            String factTimeStamp = cursor.getString(cursor.getColumnIndex(FactEntry.COLUMN_TIMESTAMP));

            mFavoriteNumberTextView.setText(factText);
            mFavoriteNumberTextView.setContentDescription(factText);

            mFavoriteFact = FactFactory.fromCursor(cursor);

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
            mListener.onFavoriteFactClick(mFavoriteFact, FavoriteFactDetailsActivity.class);
        }
    }
}
