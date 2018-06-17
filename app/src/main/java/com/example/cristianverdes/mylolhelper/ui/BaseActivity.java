package com.example.cristianverdes.mylolhelper.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.example.cristianverdes.mylolhelper.R;
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseActivity extends AppCompatActivity {
    private Disposable networkDisposable;
    private Snackbar snackbar;
    private View parentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(BaseActivity.class.getSimpleName(),"This is on Create base");

        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
    }


    public void addCheckInternetObservables(final View parentView) {
        this.parentView = parentView;

        networkDisposable = ReactiveNetwork.observeNetworkConnectivity(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Connectivity>() {
                    @Override public void accept(final Connectivity connectivity) {
                        // do something with connectivity
                        // you can call connectivity.getState();
                        // connectivity.getType(); or connectivity.toString();
                        if (!connectivity.isAvailable()) {
                            snackbar = Snackbar.make(parentView, "No Internet connection. :(", Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();
                        } else {
                            if (snackbar != null) {
                                snackbar.dismiss();
                            }
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() == null) {
            snackbar = Snackbar.make(parentView, "No Internet connection. :(", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networkDisposable != null) {
            networkDisposable.dispose();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionExit();
    }

    // Start Activity
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransitionEnter();
    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        super.startActivity(intent, options);
        overridePendingTransitionEnter();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransitionEnter();
    }

    /*
     * Perform enter animation
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /*
     * Perform Exit animation
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
