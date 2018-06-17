package com.example.cristianverdes.mylolhelper.ui.findsummoner;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristianverdes.mylolhelper.R;
import com.example.cristianverdes.mylolhelper.data.remote.NetworkConstants;
import com.example.cristianverdes.mylolhelper.domain.models.DomainSummoner;
import com.example.cristianverdes.mylolhelper.ui.matches.MatchesActivity;
import com.example.cristianverdes.mylolhelper.utils.Injection;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.squareup.picasso.Picasso;

import java.nio.file.Path;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FindSummonerFragment extends Fragment {
    private View rootView;
    private FindSummonerViewModel findSummonerViewModel;

    final Observer<DomainSummoner> summonerObserver = new Observer<DomainSummoner>() {
        @Override
        public void onChanged(@Nullable DomainSummoner domainSummoner) {
            updateUI(domainSummoner);
        }
    };

    @BindView(R.id.et_search_summoner)
    EditText searchSummoner;

    @BindView(R.id.iv_profile_icon)
    ImageView profileIcon;

    @BindView(R.id.tv_summoner_name)
    TextView summonerName;

    @BindView(R.id.tv_level)
    TextView level;

    @BindView(R.id.cv_summoner)
    View summonerCardView;

    @BindView(R.id.bt_match_history)
    Button matchHistory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_find_summoner, container, false);
        ButterKnife.bind(this, rootView);

        createViewModel();

        setViewsListeners();

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        findSummonerViewModel.disposeObservables();
    }

    private void setViewsListeners() {
        searchSummoner.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                if (cm.getActiveNetworkInfo() != null &&
                        cm.getActiveNetworkInfo().isConnectedOrConnecting()) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        // We call the ViewModel here and subscribe to the data stream provided.
                        findSummonerViewModel.searchSummoner(searchSummoner.getText().toString()).observe(FindSummonerFragment.this, summonerObserver);

                        // Hide Soft Keyboard on done
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        return true;
                    }
                    return false;
                }
                return false;
            }
        });
    }

    private void createViewModel() {
        // Create the Factory to be passed to the ViewModel
        FindSummonerViewModelFactory findSummonerViewModelFactory =
                new FindSummonerViewModelFactory(Injection.provideSummonerRepository());

        // Create ViewModel
        findSummonerViewModel = ViewModelProviders.of(this, findSummonerViewModelFactory)
                .get(FindSummonerViewModel.class);
    }


    private void updateUI(final DomainSummoner domainSummoner) {
        if (domainSummoner != null) {
            // Summoner found
            Picasso.get()
                    .load(NetworkConstants.iconBaseUrl + domainSummoner.getProfileIconId() + ".png")
                    .into(profileIcon);

            summonerCardView.setVisibility(View.VISIBLE);
            summonerName.setText(domainSummoner.getName());
            level.setText(getResources().getString(R.string.lvl));
            level.append(domainSummoner.getSummonerLevel());

            matchHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MatchesActivity.start(getActivity(), domainSummoner.getAccountId());
                }
            });

        } else {
            // Summoner NOT found
           Snackbar.make(getActivity().findViewById(R.id.parent),
                    "No Summoner found.", Snackbar.LENGTH_SHORT).show();
        }
    }
}
