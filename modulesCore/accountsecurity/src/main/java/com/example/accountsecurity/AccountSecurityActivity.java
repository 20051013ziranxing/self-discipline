package com.example.accountsecurity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.activitymanager.ActivityManager;
import com.example.selectanimage.ImageHelper;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Route(path = "/accountsecurity/AccountSecurityActivity")
public class AccountSecurityActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSIONS_CAMERA = 100;
    private static final int REQUEST_CODE_PERMISSIONS_ALBUM = 10;
    private static final int REQUEST_CODE_TAKE_PHOTO = 200;
    private static final int REQUEST_CODE_OPEN_ALBUM = 300;

    private ImageHelper imageHelper;
    private ImageView imageView;
    private static String TAG = "TestTT_AccountSecurityActivity";
    Toolbar toolbar;
    TextView textView;
    EditText editText;
    TextView textView_sign_out;
    TextView cancel_your_account;
    private static final int CHOOSE_PHOTO = 2;
    AccountSecurityPresenter accountSecurityPresenter;
    ConstraintLayout constraintLayout_change_Icon;
    CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_security);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EventBus.getDefault().register(this);
        ActivityManager.getInstance().addActivity(this);
        //此处的邮箱地址应该根据数据进行进行改变
        imageHelper = new ImageHelper(this);
        imageView = findViewById(R.id.imageView_userIcon);
        accountSecurityPresenter = new AccountSecurityPresenter(this, "2858678706");
        constraintLayout_change_Icon = findViewById(R.id.change_Icon);
        /*circleImageView = findViewById(R.id.imageView_userIcon);*/
        textView = findViewById(R.id.textView_save);
        editText = findViewById(R.id.textView_userName);
        cancel_your_account = findViewById(R.id.cancel_your_account);
        cancel_your_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/cancelyouraccount/SignOutPromptActivity").navigation();
            }
        });
        accountSecurityPresenter.initData();
        //根据获取到的进行图片的设置
        /*displayImage("content://media/external_primary/images/media/1000031793");*/
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        constraintLayout_change_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*imageHelper.choicePhoto();*/
                imageHelper.takePhoto();
                /*if (ContextCompat.checkSelfPermission(v.getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(v.getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(v.getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AccountSecurityActivity.this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
                } else {
                    openAlbum();
                }*/
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //按下保存的时候要改变存储的数据
                Log.d(TAG, editText.getText().toString());
                accountSecurityPresenter.saveMessage();
                finish();
            }
        });
        //退出登录按钮
        textView_sign_out = findViewById(R.id.sign_out);
        textView_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountSecurityPresenter.signOut();
            }
        });
    }


    /*public void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openAlbum();
            } else {
                Toast.makeText(AccountSecurityActivity.this, "访问相册权限受限制", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) {
                if (Build.VERSION.SDK_INT >= 19) {
                    handleImageOnKitKat(data);
                } else {
                    handleImageBeforeKitKat(data);
                }
            }
        }
    }*/

    private void handleImageOnKitKat(Intent data) {
        String imagepath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagepath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("Content://downloads/public_downloads"), Long.valueOf(docId));
                imagepath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagepath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagepath = uri.getPath();
        }
        Log.d(TAG, String.valueOf(uri) + "  url");
        accountSecurityPresenter.changeIcon(String.valueOf(uri));
        Log.d(TAG, "来自handleImageOnKitKat的url");
        getPathFromUri(uri);
        displayImage1(imagepath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        Log.d(TAG, "来自handleImageBeforeKitKat的url");
        getPathFromUri(uri);
        String imagepath = getImagePath(uri, null);
        Log.d(TAG, imagepath + "777");
        displayImage1(imagepath);
    }

    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null,null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /*public void displayImage(String imagePath) {
        if (imagePath != null) {
            Log.d(TAG, imagePath + "999");
            Bitmap bitmap = null;
            try {
                bitmap = getBitmapFromUri(Uri.parse(imagePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            circleImageView.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }*/
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        return BitmapFactory.decodeStream(inputStream);
    }
    private void displayImage1(String imagePath) {
        if (imagePath != null) {
            Log.d(TAG, imagePath + "999");
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            circleImageView.setImageBitmap(bitmap);
            accountSecurityPresenter.changeIcon(imagePath);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    public String getPathFromUri(Uri uri) {
        Cursor cursor = null;
        String path = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = getContentResolver().query(uri, proj, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.d(TAG,"psth+++：" + path);
        return path;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                imageHelper.dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "需要开启相机和存储权限才能拍照！", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_PERMISSIONS_ALBUM) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                imageHelper.openAlbum();
            } else {
                Toast.makeText(this, "需要开启相册和存储权限才能选择图片！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            String imagePath = imageHelper.getCurrentPhotoPath();
            displayImage(imagePath);
            /*uploadImage(imagePath);*/
        } else if (requestCode == REQUEST_CODE_OPEN_ALBUM && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            String imagePath = imageHelper.getPathFromUri(selectedImageUri);
            displayImage(imagePath);
            /*uploadImage(imagePath);*/
        }
    }

    // 显示图片到 ImageView
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        } else {
            Toast.makeText(this, "无法获取图片路径！", Toast.LENGTH_SHORT).show();
        }
    }

    // 上传图片到后端
    /*private void uploadImage(String imagePath) {
        if (imagePath == null) {
            Toast.makeText(this, "无法获取图片路径！", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(imagePath);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("image/jpeg")))
                .build();

        Request request = new Request.Builder()
                .url("https://your-backend-server.com/upload") // 替换为你的后端接口地址
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> Toast.makeText(AccountSecurityActivity.this, "上传失败：" + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(AccountSecurityActivity.this, "上传成功！", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(AccountSecurityActivity.this, "上传失败：" + response.body().toString(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }*/
}