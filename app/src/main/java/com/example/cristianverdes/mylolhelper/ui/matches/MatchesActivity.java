package com.example.cristianverdes.mylolhelper.ui.matches;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.cristianverdes.mylolhelper.R;
import com.example.cristianverdes.mylolhelper.domain.models.DomainMatch;
import com.example.cristianverdes.mylolhelper.ui.BaseActivity;
import com.example.cristianverdes.mylolhelper.utils.Injection;

import java.util.List;

public class MatchesActivity extends BaseActivity {
    private static final String TAG = MatchesActivity.class.getSimpleName();
    private static final String KEY_ACCOUNT_ID = "accountId";
    private MatchesViewModel matchesViewModel;
    private String summonerId;

    private MatchesAdapter matchesAdapter;


    public static void start(Context context, String accountId) {
        Intent starter = new Intent(context, MatchesActivity.class);
        starter.putExtra(KEY_ACCOUNT_ID, accountId);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        // Intent Extra
        summonerId = getIntent().getStringExtra(KEY_ACCOUNT_ID);

        // RecyclerView
        RecyclerView matchesRV = findViewById(R.id.rv_matches);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        matchesAdapter = new MatchesAdapter(true);
        matchesRV.setLayoutManager(linearLayoutManager);
        matchesRV.setAdapter(matchesAdapter);

        // View Model and Data
        createViewModel();

        subscribeToDataStream();

        // Internet check
        addCheckInternetObservables(findViewById(R.id.parent));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        matchesViewModel.disposeObservables();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void createViewModel() {
        // Create Factory to be passed to the ViewModel
        MatchesViewModelFactory matchesViewModelFactory =
                new MatchesViewModelFactory(Injection.provideMatchesRepository(this));

        // Create ViewModel
        matchesViewModel = ViewModelProviders
                .of(this, matchesViewModelFactory)
                .get(MatchesViewModel.class);
    }

    private void subscribeToDataStream() {
        // Create observer which updates the UI
        final Observer<List<DomainMatch>> matchesObserver = new Observer<List<DomainMatch>>() {
            @Override
            public void onChanged(@Nullable List<DomainMatch> domainMatches) {
                if (domainMatches != null) {
                    // Set Data to Adapter Here
                    hideProgressBar();
                    matchesAdapter.setMatches(domainMatches);
                } else {
                    Log.e(TAG, "List of matches is empty.");
                }
            }
        };

        // Observe the LiveData, pass the Activity as the LifecycleOwner and the observer
        matchesViewModel.getMatchesMutableLiveData(summonerId).observe(this, matchesObserver);
    }

    private void hideProgressBar() {
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        RecyclerView recyclerView = findViewById(R.id.rv_matches);
        recyclerView.setVisibility(View.VISIBLE);
    }


}
