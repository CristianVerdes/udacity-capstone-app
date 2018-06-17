package com.example.cristianverdes.mylolhelper.utils;

import android.content.Context;

import com.example.cristianverdes.mylolhelper.data.repositories.ChampionRepository;
import com.example.cristianverdes.mylolhelper.data.repositories.ChampionsRepository;
import com.example.cristianverdes.mylolhelper.data.repositories.MatchesRepository;
import com.example.cristianverdes.mylolhelper.data.repositories.SummonerRepository;

import static com.example.cristianverdes.mylolhelper.utils.Preconditions.*;

public class Injection {
    public static ChampionsRepository provideChampionsRepository(Context context) {
        checkNotNull(context);
        return ChampionsRepository.getInstance(context);
    }

    public static ChampionRepository provideChampionDetailsRepository(Context context) {
        checkNotNull(context);
        return ChampionRepository.getInstance(context);
    }

    public static SummonerRepository provideSummonerRepository() {
        return SummonerRepository.getInstance();
    }

    public static MatchesRepository provideMatchesRepository(Context context) {
        checkNotNull(context);
        return MatchesRepository.getInstance(context);
    }


}
