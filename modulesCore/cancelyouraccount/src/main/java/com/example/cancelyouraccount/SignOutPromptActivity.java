package com.example.cancelyouraccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.cancelyouraccount.adapter.RecyclerCancelTipAdapter;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/cancelyouraccount/SignOutPromptActivity")
public class SignOutPromptActivity extends AppCompatActivity {
    List<String> stringList;
    RecyclerView recyclerView_reminderForCancelingYourAccount;
    CheckBox checkBox;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_out_prompt);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initData();
        recyclerView_reminderForCancelingYourAccount = findViewById(R.id.recyclerView_reminderForCancelingYourAccount);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_reminderForCancelingYourAccount.setLayoutManager(linearLayoutManager);
        RecyclerCancelTipAdapter recyclerCancelTipAdapter = new RecyclerCancelTipAdapter(stringList);
        recyclerView_reminderForCancelingYourAccount.setAdapter(recyclerCancelTipAdapter);
        checkBox = findViewById(R.id.checkBox_agree);
        button = findViewById(R.id.button_comeNext);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    button.setEnabled(true);
                } else {
                    button.setEnabled(false);
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/cancelyouraccount/LogOutPageActivity").navigation();
                finish();
                /*Intent intent = new Intent(SignOutPromptActivity.this, LogOutPageActivity.class);
                startActivity(intent);*/
            }
        });
    }
    public void initData() {
        stringList = new ArrayList<>();
        String s = "无法登录、使用小红书账号，并移除该账号下所有登录方式";
        String s1 = "账号头像重置为默认头像、昵称重置为“用户已注销”";
        String s2 = "移除该账号下的实名认证信息";
        String s3 = "该账号下的个人资料和历史信息都将无法找回";
        String s4 = "取消所有认证身份（如创作者、企业号）且无法恢复";
        stringList.add(s);
        stringList.add(s1);
        stringList.add(s2);
        stringList.add(s3);
        stringList.add(s4);
    }
}