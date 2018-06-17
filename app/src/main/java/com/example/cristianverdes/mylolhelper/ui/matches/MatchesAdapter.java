package com.example.cristianverdes.mylolhelper.ui.matches;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristianverdes.mylolhelper.R;
import com.example.cristianverdes.mylolhelper.domain.models.DomainMatch;
import com.example.cristianverdes.mylolhelper.ui.favmatches.FavMatchesViewModel;
import com.example.cristianverdes.mylolhelper.ui.matchdetails.MatchDetailsActivity;
import com.example.cristianverdes.mylolhelper.widgets.champions.FavoriteChampsWidget;
import com.example.cristianverdes.mylolhelper.widgets.matches.FavoriteMatchesWidget;

import java.util.ArrayList;
import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchViewHolder> {
    private List<DomainMatch> matches = new ArrayList<>();
    private boolean addFavoriteMenu;
    private FavMatchesViewModel favMatchesViewModel;

    public MatchesAdapter(boolean addFavoriteMenu) {
        this.addFavoriteMenu =  addFavoriteMenu;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_matches, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        holder.bind(matches.get(position));
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public void setMatches(List<DomainMatch> matches) {
        this.matches.addAll(matches);
        notifyDataSetChanged();
    }

    public void setFavMatchesViewModel(FavMatchesViewModel favMatchesViewModel) {
        this.favMatchesViewModel = favMatchesViewModel;
    }

    public class MatchViewHolder extends RecyclerView.ViewHolder {

        public MatchViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(final DomainMatch domainMatch) {
            TextView date = itemView.findViewById(R.id.tv_date);
            TextView matchStatus = itemView.findViewById(R.id.tv_match_status);

            date.setText(domainMatch.getDate());
            if (domainMatch.isWin()) {
                matchStatus.setText(R.string.won);
            } else {
                matchStatus.setText(R.string.lost);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MatchDetailsActivity.start(itemView.getContext(), domainMatch, addFavoriteMenu);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!addFavoriteMenu) {
                        if (matches.size() == 1) {
                            Toast.makeText(itemView.getContext(), "You can't delete your only Favorite Match. :P", Toast.LENGTH_LONG).show();
                        } else {
                            favMatchesViewModel.deleteFavoriteMatch(domainMatch);

                            // Update UI
                            matches.remove(domainMatch);
                            notifyDataSetChanged();

                            // Update widget
                            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                            intent.setComponent(new ComponentName(itemView.getContext(), FavoriteMatchesWidget.class));
                            itemView.getContext().sendBroadcast(intent);
                        }
                    }

                    return true;
                }
            });

        }
    }
}
