package com.example.cristianverdes.mylolhelper.ui.allchampions;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristianverdes.mylolhelper.R;
import com.example.cristianverdes.mylolhelper.data.model.champions.ChampionListItem;
import com.example.cristianverdes.mylolhelper.data.model.champions.Champions;
import com.example.cristianverdes.mylolhelper.domain.models.DomainChampions;
import com.example.cristianverdes.mylolhelper.ui.championdetails.ChampionDetailsActivity;
import com.example.cristianverdes.mylolhelper.ui.favchampions.FavChampionsViewModel;
import com.example.cristianverdes.mylolhelper.widgets.champions.FavoriteChampsWidget;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChampionsAdapter extends RecyclerView.Adapter<ChampionsAdapter.ChampionsViewHolder>{
    private List<ChampionListItem> championListItems;
    private boolean addFavoriteMenu;
    private FavChampionsViewModel favChampionsViewModel;

    public ChampionsAdapter(boolean addFavoriteMenu) {
        championListItems = new ArrayList<>();
        this.addFavoriteMenu = addFavoriteMenu;
    }

    @NonNull
    @Override
    public ChampionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_champions, parent, false);
        return new ChampionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChampionsViewHolder holder, int position) {
        holder.bind(championListItems.get(position));
    }

    // Setters
    public void setChampionListItems(DomainChampions championListItems) {
        this.championListItems.addAll(championListItems.getChampionsArrayList());
        notifyDataSetChanged();
    }

    public void setFavChampionsViewModel(FavChampionsViewModel favChampionsViewModel) {
        this.favChampionsViewModel = favChampionsViewModel;
    }

    @Override
    public int getItemCount() {
        return championListItems.size();
    }

    public class ChampionsViewHolder extends RecyclerView.ViewHolder {

        public ChampionsViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(final ChampionListItem championListItem) {
            // Title
            TextView championName = itemView.findViewById(R.id.tv_champion_name);
            championName.setText(championListItem.getName());

            // Image
            ImageView imageView = itemView.findViewById(R.id.iv_champion);

            Picasso.get()
                    .load("file:///android_asset/" + championListItem.getChampionImage().getPhotoPath())
                    .into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChampionDetailsActivity.start(itemView.getContext(), championListItem, addFavoriteMenu);
                }
            });

            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!addFavoriteMenu) {
                        if (championListItems.size() == 1) {
                            Toast.makeText(itemView.getContext(), "You can't delete your only Favorite Champion. :P", Toast.LENGTH_LONG).show();

                        } else {
                            favChampionsViewModel.deleteFavoriteChampion(championListItem);

                            // Update UI
                            championListItems.remove(championListItem);
                            notifyDataSetChanged();

                            // Update widget
                            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                            intent.setComponent(new ComponentName(itemView.getContext(), FavoriteChampsWidget.class));
                            itemView.getContext().sendBroadcast(intent);
                        }
                    }
                    return true;
                }
            });

        }
    }
}
