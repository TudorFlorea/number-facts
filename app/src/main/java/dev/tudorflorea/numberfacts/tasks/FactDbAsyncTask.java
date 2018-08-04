package dev.tudorflorea.numberfacts.tasks;

import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;

import dev.tudorflorea.numberfacts.database.FactContract;

public class FactDbAsyncTask extends AsyncTask<Void, Void, Boolean> {

    @Override
    protected Boolean doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPostExecute(Boolean factInserted) {
        super.onPostExecute(factInserted);
    }
}
