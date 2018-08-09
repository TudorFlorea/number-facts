package dev.tudorflorea.numberfacts.services;

import com.firebase.jobdispatcher.JobParameters;
import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobService;

import dev.tudorflorea.numberfacts.data.Fact;
import dev.tudorflorea.numberfacts.data.FactFactory;
import dev.tudorflorea.numberfacts.utilities.NotificationUtils;

public class NotificationFirebaseJobService extends JobService {

    private AsyncTask mBackgroundTask;


    @Override
    public boolean onStartJob(final JobParameters job) {


        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = NotificationFirebaseJobService.this;
                Fact randomFact = FactFactory.RandomTriviaFact();
                NotificationUtils.notificationTest(context, randomFact.getText());
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
            }
        };

        mBackgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true;
    }
}
