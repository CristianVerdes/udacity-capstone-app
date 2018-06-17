package com.example.cristianverdes.mylolhelper.widgets.matches;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class FavoriteMatchesWidgetService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FavoriteMatchesRemoteViewFactory(this.getApplicationContext());
    }
}
