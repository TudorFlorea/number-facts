package dev.tudorflorea.numberfacts.tasks;

import android.os.AsyncTask;

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
