package com.example.thematicSection;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.activitymanager.ActivityManager;
import com.example.clockinfragment.ClockInFragment_1;
import com.example.communityfragment.view.CommunityFragment;
import com.example.myfragment.MyFragmentModule;
import com.example.myfragment.MyFragmentPresenter;
import com.example.myfragment.MyFragment_1;
import com.example.networkrequests.NetworkClient;
import com.example.thematicSection.adapter.RecyclerViewAdapterListing;
import com.example.thematicSection.bean.Listing;
import com.example.todofragment.ToDoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/thematicsection/MainActivity1")
public class MainActivity1 extends AppCompatActivity {
    List<Listing> listingList;
    public DrawerLayout drawableLayout;
    private static final String TAG = "TestTT_MainActivity1";
    BottomNavigationView bottomNavigationView;
    FragmentContainerView fragmentContainerView;
    NavigationView navigationView;
    //侧面导航栏里面的头布局
    View headerView;

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
        //进行侧面导航栏的设置
        drawableLayout = findViewById(R.id.DrawableLayout);
        navigationView = findViewById(R.id.navigationView);
        headerView = navigationView.getHeaderView(0);
        initHeadData();

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
                    MyFragmentModule myFragmentModule = new MyFragmentModule(MainActivity1.this, new NetworkClient());
                    MyFragmentPresenter presenter = new MyFragmentPresenter(myFragmentModule, fragment);
                    fragment.setPresenter(presenter);
                } else if (menuItem.getItemId() == R.id.clock) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    ClockInFragment_1 clockInFragment1 = new ClockInFragment_1();
                    fragmentTransaction.replace(R.id.fragment_content, clockInFragment1).commit();
                } else if (menuItem.getItemId() == R.id.toDo) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    ToDoFragment toDoFragment1 = new ToDoFragment();
                    fragmentTransaction.replace(R.id.fragment_content, toDoFragment1).commit();
                } else if (menuItem.getItemId() == R.id.community) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    CommunityFragment communityFragemnt = new CommunityFragment();
                    fragmentTransaction.replace(R.id.fragment_content, communityFragemnt).commit();

                }
                return true;
            }
        });
    }

    private void initHeadData() {
        RadioButton radio_categorized_by_tag = headerView.findViewById(R.id.radio_categorized_by_tag);
        radio_categorized_by_tag.setChecked(true);
        RadioGroup radioGroup = headerView.findViewById(R.id.radioGroup_selectAFilterCondition);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_categorized_by_tag) {
                    headerView.findViewById(R.id.include_classified_lists).setVisibility(View.VISIBLE);
                    headerView.findViewById(R.id.include_label).setVisibility(View.GONE);
                } else {
                    headerView.findViewById(R.id.include_classified_lists).setVisibility(View.GONE);
                    headerView.findViewById(R.id.include_label).setVisibility(View.VISIBLE);
                }
            }
        });
        //记得进行初始化
        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity1.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recyclerView_ClassifiedLists = headerView.findViewById(R.id.recyclerView_ClassifiedLists);
        RecyclerViewAdapterListing recyclerViewAdapterListing = new RecyclerViewAdapterListing(listingList, R.drawable.listing_icon, new RecyclerViewAdapterListing.RecyclerViewAdapterListingListen() {
            @Override
            public void hhh() {

            }
        });
        recyclerView_ClassifiedLists.setLayoutManager(linearLayoutManager);
        recyclerView_ClassifiedLists.setAdapter(recyclerViewAdapterListing);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(MainActivity1.this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recyclerView_label = headerView.findViewById(R.id.recyclerView_label);
        RecyclerViewAdapterListing recyclerViewAdapterListing1 = new RecyclerViewAdapterListing(null, R.drawable.label_icon, new RecyclerViewAdapterListing.RecyclerViewAdapterListingListen() {
            @Override
            public void hhh() {

            }
        });
        recyclerView_label.setLayoutManager(linearLayoutManager1);
        recyclerView_label.setAdapter(recyclerViewAdapterListing1);
    }
    public void init() {
        listingList = new ArrayList<>();
        Listing listing = new Listing("#222444", "课程作业");
        listingList.add(listing);
        listingList.add(listing);
        listingList.add(listing);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().finishAllActivities();
    }
}