package com.example.cristianverdes.mylolhelper.ui.matchdetails;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.cristianverdes.mylolhelper.R;
import com.example.cristianverdes.mylolhelper.data.local.AddFavoriteMatchTask;
import com.example.cristianverdes.mylolhelper.data.remote.NetworkConstants;
import com.example.cristianverdes.mylolhelper.databinding.ActivityMatchDetailsBinding;
import com.example.cristianverdes.mylolhelper.domain.models.DomainChampion;
import com.example.cristianverdes.mylolhelper.domain.models.DomainMatch;
import com.example.cristianverdes.mylolhelper.ui.BaseActivity;
import com.example.cristianverdes.mylolhelper.ui.championdetails.ChampionDetailsViewModel;
import com.example.cristianverdes.mylolhelper.ui.championdetails.ChampionDetailsViewModelFactory;
import com.example.cristianverdes.mylolhelper.utils.Injection;
import com.example.cristianverdes.mylolhelper.widgets.champions.FavoriteChampsWidget;
import com.example.cristianverdes.mylolhelper.widgets.matches.FavoriteMatchesWidget;
import com.squareup.picasso.Picasso;

public class MatchDetailsActivity extends BaseActivity {
    private static final String TAG = MatchDetailsActivity.class.getSimpleName();
    public static String KEY_MATCH = "key_match";
    public static String KEY_ADD_FAVORITE_MENU = "add_favorite_menu";
    private static final String KEY_CHAMPION_ID = "key_champion_id";
    private DomainMatch match;
    private ChampionDetailsViewModel championDetailsViewModel;
    private ActivityMatchDetailsBinding activityMatchDetailsBinding;

    public static void start(Context context, DomainMatch domainMatch, boolean addFavoriteMenu) {
        Intent starter = new Intent(context, MatchDetailsActivity.class);
        starter.putExtra(KEY_ADD_FAVORITE_MENU, addFavoriteMenu);
        starter.putExtra(KEY_MATCH, domainMatch);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details);

        updateUI();

        createViewModel();

        subscribeToDataStream();

        createButtonClickListener();

        addCheckInternetObservables(findViewById(R.id.parent));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            if (getIntent().getExtras().getBoolean(KEY_ADD_FAVORITE_MENU)) {
                getMenuInflater().inflate(R.menu.menu_favorite_button, menu);
            }
        } catch (NullPointerException e) {
            Log.e(TAG, e.toString());
            return false;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.it_favorite) {
            // Add Match to favorite here
            AddFavoriteMatchTask addFavoriteMatchTask = new AddFavoriteMatchTask(this);
            addFavoriteMatchTask.execute(match);

            // Update widget
            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.setComponent(new ComponentName(this, FavoriteMatchesWidget.class));
            sendBroadcast(intent);

        }

        return super.onOptionsItemSelected(item);
    }


    private void createButtonClickListener() {
        activityMatchDetailsBinding.setListener(new ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open browser here
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NetworkConstants.matchBaseUrl + match.getGameId() + "/" + match.getAccountId() + "?tab=overview"));
                startActivity(browserIntent);
            }
        }));
    }

    private void createViewModel() {
        // Create the Factory to be passed to the ViewModel
        ChampionDetailsViewModelFactory championDetailsViewModelFactory =
                new ChampionDetailsViewModelFactory(Injection.provideChampionDetailsRepository(this));

        // Create ViewModel
        championDetailsViewModel = ViewModelProviders
                .of(this, championDetailsViewModelFactory)
                .get(ChampionDetailsViewModel.class);
    }

    private void subscribeToDataStream() {
        // Create the observer which updates the UI
        final Observer<DomainChampion> championObserver = new Observer<DomainChampion>() {
            @Override
            public void onChanged(@Nullable DomainChampion champion) {
                if (champion != null) {
                    activityMatchDetailsBinding.setChampion(champion);
                } else {
                    Log.e(TAG, "Champion model is null");
                }
            }
        };

        // Observe the LiveData, pass the Activity as the LifecycleOwner and the observer.
        championDetailsViewModel
                .getChampionMutableLiveData(Integer.valueOf(match.getChampionId()))
                .observe(this, championObserver);
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String icon) {
        Picasso.get()
                .load("file:///android_asset/" + icon)
                .into(view);
    }

    private void updateUI() {
        activityMatchDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_match_details);
        activityMatchDetailsBinding.setMatch(match = getIntent().getParcelableExtra(KEY_MATCH));
    }
}
