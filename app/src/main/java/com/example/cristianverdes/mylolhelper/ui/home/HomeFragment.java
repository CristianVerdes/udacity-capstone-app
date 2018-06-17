package com.example.cristianverdes.mylolhelper.ui.home;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cristianverdes.mylolhelper.R;

public class HomeFragment extends Fragment {
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);


        ImageView wall = rootView.findViewById(R.id.iv_wall);
        TextView moreDetails = rootView.findViewById(R.id.tv_more_details);
        Button playForFree = rootView.findViewById(R.id.bt_play_for_free);
        TextView trailer = rootView.findViewById(R.id.tv_trailer);

        wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchVideo("j5QahFFHv0I");
            }
        });

        moreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchVideo("BGtROJeMPeE");
            }
        });

        playForFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://eune.leagueoflegends.com/en/"));
                startActivity(browserIntent);
            }
        });

        trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchVideo("j5QahFFHv0I");
            }
        });

        return rootView;
    }

    private void watchVideo(String videoId){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + videoId));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }
}
