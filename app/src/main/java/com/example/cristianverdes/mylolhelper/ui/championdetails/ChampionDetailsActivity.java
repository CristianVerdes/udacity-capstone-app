package com.example.cristianverdes.mylolhelper.ui.championdetails;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cristianverdes.mylolhelper.R;
import com.example.cristianverdes.mylolhelper.data.local.AddFavoriteChampionTask;
import com.example.cristianverdes.mylolhelper.data.model.champions.ChampionListItem;
import com.example.cristianverdes.mylolhelper.data.remote.NetworkConstants;
import com.example.cristianverdes.mylolhelper.domain.models.DomainChampion;
import com.example.cristianverdes.mylolhelper.ui.BaseActivity;
import com.example.cristianverdes.mylolhelper.utils.Injection;
import com.example.cristianverdes.mylolhelper.widgets.champions.FavoriteChampsWidget;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChampionDetailsActivity extends BaseActivity {
    private static final String TAG = ChampionDetailsActivity.class.getSimpleName();
    public static final String KEY_CHAMPION = "key_champion";
    public static final String KEY_CHAMPION_ID = "key_champion_id";
    public static final String KEY_ADD_FAVORITE_MENU = "key_add_favorite_menu";
    private ChampionDetailsViewModel championDetailsViewModel;

    // Views
    @BindView(R.id.iv_splash) ImageView splash;
    @BindView(R.id.tv_champion_name) TextView name;
    @BindView(R.id.tv_lore) TextView lore;

    @BindView(R.id.tv_attack) TextView attack;
    @BindView(R.id.tv_defence) TextView defence;
    @BindView(R.id.tv_magic) TextView magic;
    @BindView(R.id.tv_difficulty) TextView difficulty;

    @BindView(R.id.tv_passive) TextView passive;
    @BindView(R.id.tv_q) TextView q;
    @BindView(R.id.tv_w) TextView w;
    @BindView(R.id.tv_e) TextView e;
    @BindView(R.id.tv_r) TextView r;

    @BindView(R.id.tv_play) TextView play;
    @BindView(R.id.tv_defeat) TextView defeat;

    @BindView(R.id.bt_items)
    Button items;


    public static void start(Context context, ChampionListItem championListItem, boolean addFavoriteMenu) {
        Intent starter = new Intent(context, ChampionDetailsActivity.class);
        starter.putExtra(KEY_CHAMPION, championListItem);
        starter.putExtra(KEY_CHAMPION_ID, championListItem.getId());
        starter.putExtra(KEY_ADD_FAVORITE_MENU, addFavoriteMenu);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_details);
        ButterKnife.bind(this);

        createViewModel();

        subscribeToDataStream();

        // Add internet check
        addCheckInternetObservables(findViewById(R.id.parent));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        championDetailsViewModel.disposeObservables();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            // We do this if to see if we need to add the favorite button or not
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
            // Add Champion to favorite here
            AddFavoriteChampionTask addFavoriteChampionTask = new AddFavoriteChampionTask(this);
            ChampionListItem championListItem = getIntent().getParcelableExtra(KEY_CHAMPION);
            addFavoriteChampionTask.execute(championListItem);

            // Update widget
            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.setComponent(new ComponentName(this, FavoriteChampsWidget.class));
            sendBroadcast(intent);
        }

        return super.onOptionsItemSelected(item);
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
                    hideProgressBar();
                    updateUI(champion);
                } else {
                    Log.e(TAG, "Champion model is null");
                }

                final String proBuildsString = "http://www.probuilds.net/champions/details/" + champion.getName();
                // Set Items Button listener
                items.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Open browser here
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(proBuildsString));
                        startActivity(browserIntent);
                    }
                });
            }
        };

        // Observe the LiveData, pass the Activity as the LifecycleOwner and the observer.
        championDetailsViewModel
                .getChampionMutableLiveData(
                        getIntent().getExtras().getInt(KEY_CHAMPION_ID))
                .observe(this, championObserver);
    }

    private void updateUI(DomainChampion champion) {
        ChampionListItem championListItem = getIntent().getParcelableExtra(KEY_CHAMPION);
        Picasso.get()
                .load(NetworkConstants.championSplashBaseUrl + championListItem.getChampionImage().getPhotoPath().replace(".png", "") + "_0.jpg")
                .into(splash);

        name.append(champion.getName());
        name.append(": ");
        name.append(champion.getTitle());
        lore.setText(champion.getLore());

        attack.setText(String.valueOf(champion.getAttack()));
        defence.setText(String.valueOf(champion.getDefence()));
        magic.setText(String.valueOf(champion.getMagic()));
        difficulty.setText(String.valueOf(champion.getDifficulty()));

        passive.setText(champion.getPassive());
        q.setText(champion.getQ());
        w.setText(champion.getW());
        e.setText(champion.getE());
        r.setText(champion.getR());

        play.setText(champion.getAllyTips());
        defeat.setText(champion.getDefeatTips());
    }

    private void hideProgressBar() {
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        ImageView splash = findViewById(R.id.iv_splash);
        TextView name = findViewById(R.id.tv_champion_name);
        View lore = findViewById(R.id.cv_lore);
        View info = findViewById(R.id.cv_info);
        View spells = findViewById(R.id.cv_spells);
        View tips = findViewById(R.id.cv_tips);
        View items = findViewById(R.id.cv_items);

        splash.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        lore.setVisibility(View.VISIBLE);
        info.setVisibility(View.VISIBLE);
        spells.setVisibility(View.VISIBLE);
        tips.setVisibility(View.VISIBLE);
        items.setVisibility(View.VISIBLE);
    }

}
















