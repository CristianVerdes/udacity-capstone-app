package com.example.cristianverdes.mylolhelper.widgets.matches;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.cristianverdes.mylolhelper.R;
import com.example.cristianverdes.mylolhelper.data.repositories.MatchesRepository;
import com.example.cristianverdes.mylolhelper.domain.models.DomainMatch;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.example.cristianverdes.mylolhelper.ui.championdetails.ChampionDetailsActivity.KEY_ADD_FAVORITE_MENU;
import static com.example.cristianverdes.mylolhelper.ui.matchdetails.MatchDetailsActivity.KEY_MATCH;

public class FavoriteMatchesRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private List<DomainMatch> matches = new ArrayList<>();
    private int matchesSize = matches.size();
    private Disposable matchesDisposable;

    public FavoriteMatchesRemoteViewFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        matchesDisposable = MatchesRepository.getInstance(context).getFavoriteMatches()
                .subscribe(new Consumer<List<DomainMatch>>() {
                    @Override
                    public void accept(List<DomainMatch> domainMatches) {
                        matches = new ArrayList<>();
                        FavoriteMatchesRemoteViewFactory.this.matches.addAll(domainMatches);
                        matchesSize = matches.size();
                    }
                });

    }

    @Override
    public void onDestroy() {
        matchesDisposable.dispose();
    }

    @Override
    public int getCount() {
        return matchesSize;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        DomainMatch match = matches.get(position);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_fav_matches_list_item);

        // WON / LOST
        if (match.isWin()) {
            views.setTextViewText(R.id.tv_match_status, context.getText(R.string.won));
        } else {
            views.setTextViewText(R.id.tv_match_status, context.getText(R.string.lost));
        }

        // Date
        views.setTextViewText(R.id.tv_match_date, match.getDate());

        // Click event Listener
        Intent fillInIntent = new Intent();

        fillInIntent.putExtra(KEY_ADD_FAVORITE_MENU, false);
        fillInIntent.putExtra(KEY_MATCH, match);

        views.setOnClickFillInIntent(R.id.parent, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
