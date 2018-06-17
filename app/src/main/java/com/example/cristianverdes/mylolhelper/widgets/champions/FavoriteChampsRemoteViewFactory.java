package com.example.cristianverdes.mylolhelper.widgets.champions;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.cristianverdes.mylolhelper.R;
import com.example.cristianverdes.mylolhelper.data.model.champions.ChampionListItem;
import com.example.cristianverdes.mylolhelper.data.repositories.ChampionsRepository;
import com.example.cristianverdes.mylolhelper.domain.models.DomainChampions;
import com.example.cristianverdes.mylolhelper.ui.championdetails.ChampionDetailsActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.example.cristianverdes.mylolhelper.ui.championdetails.ChampionDetailsActivity.KEY_ADD_FAVORITE_MENU;
import static com.example.cristianverdes.mylolhelper.ui.championdetails.ChampionDetailsActivity.KEY_CHAMPION;
import static com.example.cristianverdes.mylolhelper.ui.championdetails.ChampionDetailsActivity.KEY_CHAMPION_ID;

public class FavoriteChampsRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory{
    private Context context;
    private List<ChampionListItem> champions = new ArrayList<>();
    private Disposable champsDisposable;

    public FavoriteChampsRemoteViewFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        champsDisposable = ChampionsRepository.getInstance(context).getFavoriteChampions().subscribe(
                new Consumer<DomainChampions>() {
            @Override
            public void accept(DomainChampions domainChampions) {
                champions = new ArrayList<>();
                if (domainChampions.getChampionsArrayList().size() != 0) {
                    FavoriteChampsRemoteViewFactory.this.champions.addAll(domainChampions.getChampionsArrayList());
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        champsDisposable.dispose();
    }

    @Override
    public int getCount() {
        return champions.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        ChampionListItem championListItem = champions.get(position);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_fav_champ_list_item);

        // Champion Name
        views.setTextViewText(R.id.tv_name, championListItem.getName());

        // Champion Icon
        try {
            InputStream is = context.getAssets().open(championListItem.getChampionImage().getPhotoPath());
            Bitmap bmp = BitmapFactory.decodeStream(is);

            views.setImageViewBitmap(R.id.iv_icon, bmp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Click event Listener
        Intent fillInIntent = new Intent();

        fillInIntent.putExtra(KEY_CHAMPION, championListItem);
        fillInIntent.putExtra(KEY_CHAMPION_ID, championListItem.getId());
        fillInIntent.putExtra(KEY_ADD_FAVORITE_MENU, false);

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
