package com.example.communityfragment.view;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.communityfragment.FileUtils;
import com.example.communityfragment.R;
import com.example.communityfragment.bean.Post;
import com.example.communityfragment.contract.IPublishContract;
import com.example.communityfragment.databinding.ActivityPublishBinding;
import com.example.communityfragment.presenter.PublishPresenter;

@Route(path = "/communityPageView/PublishActivity")
public class PublishActivity extends AppCompatActivity implements IPublishContract.View {
    public static final String TAG = "PublishFunctionTAG";

    private ActivityPublishBinding binding;
    private PublishPresenter mPresenter;
    private Uri imgURI;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPublishBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.publish_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mPresenter = new PublishPresenter(this);

        binding.imgPublishSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = binding.etPublishContent.getText().toString();
                if (content.trim().isEmpty()) {
                    Toast.makeText(PublishActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                } else {
                    if (imgURI != null)
                        mPresenter.publish(content, FileUtils.getRealPathFromUri(PublishActivity.this, imgURI));
                    else
                        mPresenter.publish(content, null);
                }
            }
        });

        binding.cvPublishAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions();
            }
        });

        binding.imgPublishExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            Log.d(TAG, "选中的图片 URI: " + uri);

            if (uri != null) {
                imgURI = uri;
                binding.cvPublishPhoto.setVisibility(View.VISIBLE);
                binding.tvAddPhoto.setText("更换照片");
                binding.imgPublishImage.setImageURI(uri);
                binding.imgPublishDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imgURI = null;
                        binding.cvPublishPhoto.setVisibility(View.GONE);
                        binding.tvAddPhoto.setText("添加照片");
                    }
                });
                // 获取 ContentResolver 实例
                ContentResolver contentResolver = getApplicationContext().getContentResolver();

                // 持久化URI访问权限
                contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        });

        binding.etPublishContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1000) {
                    Toast.makeText(PublishActivity.this, "字数已达上限", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void publishSuccess(int id) {
        finish();
    }

    @Override
    public void publishFailure() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PublishActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ActivityResultLauncher<String[]> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                boolean granted = true;
                for (Boolean value : result.values()) {
                    if (!value) {
                        granted = false;
                        break;
                    }
                }
                if (granted) {
                    launchImagePicker();
                } else {
                    Toast.makeText(PublishActivity.this, "请允许权限以选择图片", Toast.LENGTH_SHORT).show();
                }
            });

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            permissionLauncher.launch(new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            });
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
            });
        } else {
            permissionLauncher.launch(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            });
        }
    }

    private void checkPermissionResult() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED)) {
            launchImagePicker();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED) {
            launchImagePicker();
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            launchImagePicker();
        }
    }

    private void launchImagePicker() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

}
