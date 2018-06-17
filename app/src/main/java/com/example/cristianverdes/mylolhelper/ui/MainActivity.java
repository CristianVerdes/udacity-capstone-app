package com.example.cristianverdes.mylolhelper.ui;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.cristianverdes.mylolhelper.R;
import com.example.cristianverdes.mylolhelper.data.model.champions.ChampionListItem;
import com.example.cristianverdes.mylolhelper.ui.allchampions.AllChampionsFragment;
import com.example.cristianverdes.mylolhelper.ui.championdetails.ChampionDetailsActivity;
import com.example.cristianverdes.mylolhelper.ui.favchampions.FavChampionsFragment;
import com.example.cristianverdes.mylolhelper.ui.favmatches.FavMatchesFragment;
import com.example.cristianverdes.mylolhelper.ui.findsummoner.FindSummonerFragment;
import com.example.cristianverdes.mylolhelper.ui.home.HomeFragment;
import com.example.cristianverdes.mylolhelper.ui.searchchamp.SearchChampionViewModel;
import com.example.cristianverdes.mylolhelper.ui.searchchamp.SearchChampionViewModelFactory;
import com.example.cristianverdes.mylolhelper.utils.Injection;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static String KEY_LOGIN_REQUIRED = "key_login_required";
    private static final int RC_SIGN_IN = 123;
    private SearchChampionViewModel searchChampionViewModel;

    private NavigationView navigationView;

    // Choose authentication providers
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
            new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build());

    // Activity Starer
    public static void start(Context context, boolean loginRequired) {
        Intent starter = new Intent(context, MainActivity.class);
        starter.putExtra(KEY_LOGIN_REQUIRED, loginRequired);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verify if login is needed
        if (getIntent().getExtras().getBoolean(KEY_LOGIN_REQUIRED)) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();

            if (currentUser == null) {
                loginFirebaseUI();
            }
        }

        setContentView(R.layout.activity_home);

        addNavigationDrawer();

        // Highlight Home option
        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));

        // Internet check
        addCheckInternetObservables(findViewById(R.id.drawer_layout));

        createSearchViewModel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setQueryHint(getResources().getString(R.string.search_champion));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        // Search Listener
        final Observer<ChampionListItem> championObserver = new Observer<ChampionListItem>() {
            @Override
            public void onChanged(@Nullable ChampionListItem championListItem) {
                if (!championListItem.equals(ChampionListItem.nullValue)) {
                    // Display the champion found
                    ChampionDetailsActivity.start(MainActivity.this, championListItem, true);
                } else {
                    // Show no champion is found
                    Toast.makeText(MainActivity.this, "Champion NOT found", Toast.LENGTH_SHORT).show();
                    System.out.println("WHY IT IS NOT WORKING???");
                }
            }
        };

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchChampionViewModel.getChampionMutableLiveData(query)
                        .observe(MainActivity.this, championObserver);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    private void createSearchViewModel() {
        // Create Factory to be passed to the ViewModel
        SearchChampionViewModelFactory searchChampionViewModelFactory =
                new SearchChampionViewModelFactory(Injection.provideChampionsRepository(this));

        // Create ViewModel
        searchChampionViewModel = ViewModelProviders
                .of(this, searchChampionViewModelFactory)
                .get(SearchChampionViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoginOrLogOutViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchChampionViewModel.disposeObservables();
    }

    private void showLoginOrLogOutViews() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            navigationView.getMenu().findItem(R.id.login).setVisible(false);
            navigationView.getMenu().findItem(R.id.log_out).setVisible(true);
        } else {
            navigationView.getMenu().findItem(R.id.login).setVisible(true);
            navigationView.getMenu().findItem(R.id.log_out).setVisible(false);
        }
    }

    private void addNavigationDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            // Create Home Fragment
            HomeFragment homeFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, homeFragment)
                    .commit();
        } else if (id == R.id.all_champions) {
            // Create All Champions Fragment
            AllChampionsFragment allChampionsFragment = new AllChampionsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, allChampionsFragment)
                    .commit();

        } else if (id == R.id.favorite_champions) {
            // Create Favorite Champions Fragment
            FavChampionsFragment favChampionsFragment = new FavChampionsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, favChampionsFragment)
                    .commit();

        } else if (id == R.id.find_summoner) {
            // Create Find Summoner Fragment
            FindSummonerFragment findSummonerFragment = new FindSummonerFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, findSummonerFragment)
                    .commit();

        } else if (id == R.id.favorite_matches) {
            // Create Favorite Matches Fragment
            FavMatchesFragment favMatchesFragment = new FavMatchesFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, favMatchesFragment)
                    .commit();

        } else if (id == R.id.login) {
            loginFirebaseUI();
        } else if (id == R.id.log_out) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this, "Log Out: Successful", Toast.LENGTH_LONG).show();
                            // Show Login Option
                            navigationView.getMenu().findItem(R.id.login).setVisible(true);
                            navigationView.getMenu().findItem(R.id.log_out).setVisible(false);
                        }
                    });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Firebase Login
    private void loginFirebaseUI() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(this, "Login: Successful", Toast.LENGTH_SHORT).show();
            } else {
                // Sign in failed, check response for error code
                Toast.makeText(this, "Login: Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
