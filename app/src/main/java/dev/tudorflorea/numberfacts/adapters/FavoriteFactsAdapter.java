package dev.tudorflorea.numberfacts.adapters;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dev.tudorflorea.numberfacts.data.Fact;
import dev.tudorflorea.numberfacts.database.FactContract.FactEntry;
import dev.tudorflorea.numberfacts.R;
import java.util.ArrayList;

/**
 * Created by tudor on 26.02.2018.
 */

public class FavoriteFactsAdapter extends CursorRecyclerViewAdapter<FavoriteFactsAdapter.FavoriteFactViewHolder>{

    //private ArrayList<Fact> mValues;
    private Context mContext;
    private Cursor mCursor;

    public FavoriteFactsAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
        mCursor = cursor;
    }


    @Override
    public FavoriteFactsAdapter.FavoriteFactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.favorite_facts_item, parent, false);

        return new FavoriteFactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteFactViewHolder viewHolder, Cursor cursor) {

        cursor.moveToPosition(cursor.getPosition());

        viewHolder.setData(cursor);


    }

    //    @Override
//    public void onBindViewHolder(FavoriteFactsAdapter.FavoriteFactViewHolder holder, int position) {
//
//        mCursor.moveToPosition(position);
//
//        String factText = mCursor.getString(mCursor.getColumnIndex(FactEntry.COLUMN_FACT));
//        Integer factNumber = mCursor.getInt(mCursor.getColumnIndex(FactEntry.COLUMN_NUMBER));
//        boolean factFound = mCursor.getInt(mCursor.getColumnIndex(FactEntry.COLUMN_NUMBER)) > 0;
//        String factType = mCursor.getString(mCursor.getColumnIndex(FactEntry.COLUMN_TYPE));
//        String factTimeStamp = mCursor.getString(mCursor.getColumnIndex(FactEntry.COLUMN_TIMESTAMP));
//
//        Fact fact = new Fact(factNumber, factText, factFound, factType, factTimeStamp);
//
//        holder.setData(fact);
//    }




    public class FavoriteFactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mFavoriteNumberTextView;
        public TextView mFavoriteNumberTypeTextView;

        //private Fact mFavoriteFact;

        public FavoriteFactViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            mFavoriteNumberTextView = (TextView) view.findViewById(R.id.favorite_number_tv);
            mFavoriteNumberTypeTextView = (TextView) view.findViewById(R.id.favorite_number_type_tv);

        }

        public void setData(Cursor cursor) {
            //this.mFavoriteFact = fact;

            String factText = cursor.getString(cursor.getColumnIndex(FactEntry.COLUMN_FACT));
            Integer factNumber = cursor.getInt(cursor.getColumnIndex(FactEntry.COLUMN_NUMBER));
            boolean factFound = cursor.getInt(cursor.getColumnIndex(FactEntry.COLUMN_NUMBER)) > 0;
            String factType = cursor.getString(cursor.getColumnIndex(FactEntry.COLUMN_TYPE));
            String factTimeStamp = cursor.getString(cursor.getColumnIndex(FactEntry.COLUMN_TIMESTAMP));

            mFavoriteNumberTextView.setText(factText);
            mFavoriteNumberTypeTextView.setText(factType);

        }

        @Override
        public void onClick(View view) {

        }
    }
}