package com.example.myfragment;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.networkrequests.NetworkClient;

public class MainActivityMyF extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_my_f);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MyFragmentModule myFragmentModule = new MyFragmentModule(this, new NetworkClient());
        MyFragment_1 fragment = new MyFragment_1();
        Bundle args = new Bundle();
        args.putString("param1", "Hello World");
        fragment.setArguments(args);
        fragmentTransaction.add(R.id.fragment, fragment).commit();
        MyFragmentPresenter presenter = new MyFragmentPresenter(myFragmentModule, fragment);
        fragment.setPresenter(presenter);
    }
}