package dev.tudorflorea.numberfacts.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import dev.tudorflorea.numberfacts.R;

public class FavoriteFactDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_fact_details);


        //MobileAds.initialize(this, "ca-app-pub-8284733181380948~1262086728");

        AdView adView = findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("418203295DEFF5A970AA99210699B6F7")
                .build();
        adView.loadAd(adRequest);

    }
}
