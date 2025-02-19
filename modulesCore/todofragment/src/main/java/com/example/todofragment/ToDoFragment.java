package com.example.todofragment;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todofragment.fragment.AddToDoFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.necer.calendar.NCalendar;
import com.necer.enumeration.CalendarState;
import com.necer.enumeration.CheckModel;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarStateChangedListener;
import com.necer.painter.InnerPainter;
import com.necer.utils.hutool.ChineseDate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToDoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToDoFragment extends Fragment {
    Toolbar toolbar;
    TextView textView;
    View view_gray;
    FloatingActionButton floatingActionButton_backDay;
    FloatingActionButton floatingActionButton_add;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ToDoFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ToDoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ToDoFragment newInstance(String param1, String param2) {
        ToDoFragment fragment = new ToDoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        textView = view.findViewById(R.id.data);
        NCalendar miui10Calendar = view.findViewById(R.id.miui10Calendar);
        floatingActionButton_backDay = view.findViewById(R.id.floatingButton_backNowDay);
        floatingActionButton_add = view.findViewById(R.id.floatingButton_add);
        view_gray = view.findViewById(R.id.gray_overlay);
        floatingActionButton_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*view_gray.setVisibility(View.VISIBLE);
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_addNewToDo, new AddToDoFragment()).commit();*/
                MyBottomSheetDialogFragment bottomSheetDialogFragment = new MyBottomSheetDialogFragment();
                bottomSheetDialogFragment.show(getChildFragmentManager(), "MyBottomSheetDialogFragment");
            }
        });
        miui10Calendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {
                if (localDate != null) {
                    ChineseDate chineseDate = new ChineseDate(localDate);
                    String s = localDate.toString();
                    String s2 = LocalDate.now().toString();
                    textView.setText(localDate.toString());
                    if (! s.equals(s2)){
                        Log.d("nvjbifgj", localDate.toString() + "pppppp" + LocalDate.now().toString());
                        floatingActionButton_backDay.setVisibility(View.VISIBLE);
                    } else {
                        floatingActionButton_backDay.setVisibility(View.GONE);
                    }
                }
            }
        });
        floatingActionButton_backDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miui10Calendar.toToday();
            }
        });

        //动画，对完成的事情进行化纤操作
        /*TextView textView = view.findViewById(R.id.textView);
        LineView lineView = view.findViewById(R.id.lineView);

        // 设置动画
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                // 动态设置线条长度
                lineView.setLineLength(interpolatedTime * textView.getWidth());
            }
        };
        animation.setDuration(1000); // 设置动画时长为 1000 毫秒

        // 开始动画
        lineView.startAnimation(animation);*/

        return view;
    }
}