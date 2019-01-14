package com.example.scuber.mypage_fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scuber.R;

import io.reactivex.annotations.Nullable;

public class TakerHistory extends Fragment {
    private static final String TAG = "Taker History";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_taker_history_fragment, container, false);
        return view;
    }
}