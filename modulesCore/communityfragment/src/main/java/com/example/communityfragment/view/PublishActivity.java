package com.example.communityfragment.view;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.example.communityfragment.R;
import com.example.communityfragment.contract.IPublishContract;
import com.example.communityfragment.databinding.ActivityPublishBinding;
import com.example.communityfragment.presenter.PublishPresenter;
import com.example.communityfragment.utils.FileUtils;
import com.example.eventbus.UserBaseMessageEventBus;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

@Route(path = "/communityPageView/PublishActivity")
public class PublishActivity extends AppCompatActivity implements IPublishContract.View {
    public static final String TAG = "PublishFunctionTAG";

    private ActivityPublishBinding binding;
    private PublishPresenter mPresenter;

    private Uri cameraImageUri;
    private ActivityResultLauncher<Uri> cameraLauncher;
    private ActivityResultLauncher<String> cameraPermissionLauncher;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private AlertDialog.Builder builder;
    private AlertDialog dialogPick;
    private UserBaseMessageEventBus userBaseMessageEventBus;

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onMoonStickyEvent(UserBaseMessageEventBus userBaseMessageEventBus) {
        this.userBaseMessageEventBus = userBaseMessageEventBus;
    }

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
        EventBus.getDefault().register(this);

        if (userBaseMessageEventBus.getUserName() != null) {
            binding.tvPublishUsername.setText(userBaseMessageEventBus.getUserName());
        } else {
            binding.tvPublishUsername.setText("用户");
        }
        if (userBaseMessageEventBus.getUserPictureURL() != null) {
            Glide.with(this)
                    .load(userBaseMessageEventBus.getUserPictureURL())
                    .error(R.drawable.ic_default)
                    .into(binding.imgPublishAvatar);
        }

        binding.imgPublishSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = binding.etPublishContent.getText().toString();
                if (content.trim().isEmpty()) {
                    Toast.makeText(PublishActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PublishActivity.this, "上传中", Toast.LENGTH_SHORT).show();
                    binding.imgPublishSend.setImageResource(R.drawable.ic_publish_gray);
                    binding.imgPublishSend.setEnabled(false);
                    String userId = "2";
                    if (userBaseMessageEventBus != null && userBaseMessageEventBus.getUserId() != null)
                        userId = userBaseMessageEventBus.getUserId();
                    if (cameraImageUri != null)
                        mPresenter.publish(content, userId, FileUtils.getRealPathFromUri(PublishActivity.this, cameraImageUri));
                    else
                        mPresenter.publish(content, userId, null);
                }
            }
        });

        binding.cvPublishAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectImageSourceDialog();
            }
        });

        binding.imgPublishExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                Log.d(TAG, "选中的图片 URI: " + uri);
                ContentResolver contentResolver = getApplicationContext().getContentResolver();
                contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                // 配置uCrop
                UCrop.Options options = new UCrop.Options();
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG); // 压缩格式
                options.setCompressionQuality(100);

                // 构造裁剪后图片的保存地址
                Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropped_" + System.currentTimeMillis() + ".jpg"));

                UCrop.of(uri, destinationUri)
                        .withAspectRatio(16, 9)
                        .withOptions(options)
                        .start(PublishActivity.this);
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

        // 注册拍照权限请求
        cameraPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (result) {
                launchCamera();
            } else {
                Toast.makeText(PublishActivity.this, "请允许摄像头权限以拍照", Toast.LENGTH_SHORT).show();
            }
        });

        // 注册拍照Launcher
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            if (result) {
                // 裁剪
                UCrop.Options options = new UCrop.Options();
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                options.setCompressionQuality(100);
                Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropped_" + System.currentTimeMillis() + ".jpg"));
                UCrop.of(cameraImageUri, destinationUri)
                        .withAspectRatio(16, 9)
                        .withOptions(options)
                        .start(PublishActivity.this);
            }
        });


    }

    // 拍照和相册裁剪选择回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                cameraImageUri = resultUri;

                binding.imgPublishImage.setImageURI(resultUri);
                binding.cvPublishPhoto.setVisibility(View.VISIBLE);
                binding.tvAddPhoto.setText("更换照片");
                binding.imgPublishImage.setImageURI(resultUri);
                binding.imgPublishDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraImageUri = null;
                        binding.cvPublishPhoto.setVisibility(View.GONE);
                        binding.tvAddPhoto.setText("添加照片");
                    }
                });

                if (dialogPick != null && dialogPick.isShowing()) {
                    dialogPick.dismiss();
                }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            if (dialogPick != null && dialogPick.isShowing()) {
                dialogPick.dismiss();
            }
            Throwable cropError = UCrop.getError(data);
            Toast.makeText(this, "裁剪出错：图片格式不支持", Toast.LENGTH_SHORT).show();
            Log.e("MyInfoActivityTAG", "onActivityResult: " + cropError);
        }
    }

    @Override
    public void publishSuccess(int id) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PublishActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                binding.imgPublishSend.setEnabled(true);
                finish();
            }
        });
    }

    @Override
    public void publishFailure() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.imgPublishSend.setEnabled(true);
                binding.imgPublishSend.setImageResource(R.drawable.ic_publish);
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

    // 选择更换图片方式
    private void showSelectImageSourceDialog() {
        builder = new AlertDialog.Builder(PublishActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_pick_layout, null);
        Button btnCamera = dialogView.findViewById(R.id.btn_camera);
        Button btnGallery = dialogView.findViewById(R.id.btn_gallery);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PublishActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
                } else {
                    launchCamera();
                }
            }
        });
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions();
            }
        });

        builder.setView(dialogView);
        dialogPick = builder.create();
        dialogPick.getWindow().setBackgroundDrawableResource(R.drawable.default_dialog_background);
        dialogPick.show();
    }

    // 打开相机
    private void launchCamera() {
        File imageFile = new File(getCacheDir(), "camera_" + System.currentTimeMillis() + ".jpg");
        cameraImageUri = androidx.core.content.FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", imageFile);
        cameraLauncher.launch(cameraImageUri);
    }

    // 根据版本判断请求权限来打开相册
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

    // 从相册选择图片
    private void launchImagePicker() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userBaseMessageEventBus != null) {
            EventBus.getDefault().unregister(userBaseMessageEventBus);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}