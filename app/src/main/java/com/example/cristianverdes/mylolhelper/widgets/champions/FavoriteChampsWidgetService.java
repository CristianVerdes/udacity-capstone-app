package com.example.cristianverdes.mylolhelper.widgets.champions;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class FavoriteChampsWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FavoriteChampsRemoteViewFactory(this.getApplicationContext());
    }
}
