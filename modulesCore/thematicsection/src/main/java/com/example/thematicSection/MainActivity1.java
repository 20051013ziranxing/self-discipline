package com.example.thematicSection;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alibaba.android.arouter.facade.annotation.Route;

@Route(path = "/thematicsection/MainActivity1")
public class MainActivity1 extends AppCompatActivity {

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
    }
}