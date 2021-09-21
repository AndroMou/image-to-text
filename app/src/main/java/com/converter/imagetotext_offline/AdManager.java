package com.converter.imagetotext_offline;


import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;



public class AdManager{

    // Your Admob Interstitial ID here !!
    private static final String ADMOB_INTERSTITIAL_ID = "ca-app-pub-5963817566033623/8009727049";
    private Context mContext;
    private InterstitialAd interstitialAd;


    public AdManager(Context context){
        mContext = context;
    }



    public void LoadAdsBannerWithInterstitial(final AdView adView){
        if(adView != null) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
        // AdView will be hidden from the activity until it loads.
   //     adView.setVisibility(View.INVISIBLE);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.d("==> Banner Ad:", " Closed by user!");
            }


            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.d("==> Banner Ad:", " Clicked by user!");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("==> Banner Ad:", " Loaded successfully!");
                adView.setVisibility(View.VISIBLE);
            }

        });
        // Just load INTERSTITIAL Ads
        LoadInterstitial();

    }


    public void LoadAdsNativeWithInterstitial(final NativeExpressAdView nativeExpressAdView){
        if(nativeExpressAdView != null){
            nativeExpressAdView.loadAd(new AdRequest.Builder().build());
        }
        nativeExpressAdView.setVisibility(View.GONE);
        nativeExpressAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }


            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                nativeExpressAdView.setVisibility(View.VISIBLE);
            }
        });

        LoadInterstitial();


    }


    public void LoadInterstitial(){
        interstitialAd = new InterstitialAd(mContext);
        interstitialAd.setAdUnitId(ADMOB_INTERSTITIAL_ID);
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.d("==> Interstitial Ad:", " Closed by user");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.d("==> Interstitial Ad:", " Opened by user");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("==> Interstitial Ad:", " Loaded successfully");
            }
        });
    }


    public void ShowInterstitial(){
        if(interstitialAd != null && interstitialAd.isLoaded()){
            interstitialAd.show();
        }
        LoadInterstitial();
    }

}
