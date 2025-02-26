package com.example.pomodorotechnique;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SelectTheSongBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private final static String TAG = "TestTT_SelectTheSongBottomSheetDialogFragment";
    RadioButton radio_insectsChirpOnSummerNights;
    RadioButton radio_summerRains;
    private OnSongSelectedListener listener;
    Button button_SureTheWhiteNoiseSelection;
    RadioGroup radioGroup_aToggleCollectionOfMusic;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_the_song_bottom_fragment, container, false);
        radio_insectsChirpOnSummerNights = view.findViewById(R.id.radio_insectsChirpOnSummerNights);
        radio_summerRains = view.findViewById(R.id.radio_summerRains);
        radioGroup_aToggleCollectionOfMusic = view.findViewById(R.id.radioGroup_aToggleCollectionOfMusic);
        view.findViewById(R.id.button_CancelTheWhiteNoiseSelection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        button_SureTheWhiteNoiseSelection = view.findViewById(R.id.button_SureTheWhiteNoiseSelection);
        button_SureTheWhiteNoiseSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = radioGroup_aToggleCollectionOfMusic.getCheckedRadioButtonId();
                if (ID == R.id.radio_insectsChirpOnSummerNights) {
                    listener.onSongSelected(String.valueOf(R.raw.music1));
                    Log.d(TAG, "我点击了，并执行了");
                } else if (ID == R.id.radio_summerRains) {
                    listener.onSongSelected(String.valueOf(R.raw.music2));
                    Log.d(TAG, "我点击了，并执行了");
                } else {
                    listener.onSongSelected("123");
                }
                dismiss();
            }
        });
        return view;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnSongSelectedListener) {
            listener = (OnSongSelectedListener) context;
        } else {
            Log.d(TAG, context.toString() + " must implement OnSongSelectedListener");
            throw new RuntimeException(context.toString() + " must implement OnSongSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnSongSelectedListener {
        void onSongSelected(String songName);
    }
}
