package com.example.thematicSection;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.activitymanager.ActivityManager;
import com.example.clockinfragment.ClockInFragmentModule;
import com.example.clockinfragment.ClockInFragmentPresenter;
import com.example.clockinfragment.ClockInFragment_1;
import com.example.myfragment.MyFragmentModule;
import com.example.myfragment.MyFragmentPresenter;
import com.example.myfragment.MyFragment_1;
import com.example.todofragment.ToDoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

@Route(path = "/thematicsection/MainActivity1")
public class MainActivity1 extends AppCompatActivity {
    private static final String TAG = "TestTT_MainActivity1";
    BottomNavigationView bottomNavigationView;
    FragmentContainerView fragmentContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("pppppp", "onCreate");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main1);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
        ActivityManager.getInstance().addActivity(this);
        fragmentContainerView = findViewById(R.id.fragment_content);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ToDoFragment toDoFragment = new ToDoFragment();
        fragmentTransaction.add(R.id.fragment_content, toDoFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Log.d(TAG, "我点击了");
                if (menuItem.getItemId() == R.id.my) {
                    Log.d(TAG, "我点击了");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    MyFragment_1 fragment = new MyFragment_1();
                    Bundle args = new Bundle();
                    args.putString("param1", "Hello World");
                    fragment.setArguments(args);
                    fragmentTransaction.replace(R.id.fragment_content, fragment).commit();
                    Log.d(TAG,"我提交了");
                    MyFragmentModule myFragmentModule = new MyFragmentModule();
                    MyFragmentPresenter presenter = new MyFragmentPresenter(myFragmentModule, fragment);
                    fragment.setPresenter(presenter);
                } else if (menuItem.getItemId() == R.id.clock) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    ClockInFragment_1 clockInFragment1 = new ClockInFragment_1();
                    fragmentTransaction.replace(R.id.fragment_content, clockInFragment1).commit();
                    ClockInFragmentModule clockInFragmentModule = new ClockInFragmentModule();
                    ClockInFragmentPresenter clockInFragmentPresenter = new ClockInFragmentPresenter(clockInFragment1, clockInFragmentModule);
                    clockInFragment1.setClockInFragmentPresenter(clockInFragmentPresenter);
                } else if (menuItem.getItemId() == R.id.toDo) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    ToDoFragment toDoFragment1 = new ToDoFragment();
                    fragmentTransaction.replace(R.id.fragment_content, toDoFragment1).commit();
                } else if (menuItem.getItemId() == R.id.community) {
                    /*FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.fragment_content, toDoFragment1).commit();*/
                }
                return true;
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().finishAllActivities();
    }
}