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
import android.view.LayoutInflater;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.example.activitymanager.ActivityManager;
import com.example.eventbus.UserBaseMessageEventBus;
import com.example.localdatabase.bean.UserBaseMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private Uri imageUri;

    private ImageView imageView;
    private AlertDialog.Builder builder;
    private static String TAG = "TestTT_AccountSecurityActivity";
    Toolbar toolbar;
    TextView textView;
    EditText editText_textView_userName;
    TextView textView_sign_out;
    TextView cancel_your_account;
    AccountSecurityPresenter accountSecurityPresenter;
    ConstraintLayout constraintLayout_change_Icon;
    CircleImageView imageView_userIcon;
    UserBaseMessageEventBus userBaseMessageEventBus;

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
        imageView_userIcon = findViewById(R.id.imageView_userIcon);
        Glide.with(this)
                .load(userBaseMessageEventBus.getUserPictureURL())
                //.load("https://mmbiz.qpic.cn/mmbiz_jpg/50flWREUFnHqHqia20eqULiczW6UPOolbIucpDClrcnOc50C5zqRq9dfY7uzzTNNS46VUicibdMrkibgvXwzcRR4jWg/640?wx_fmt=jpeg&from=appmsg&tp=wxpic&wxfrom=5&wx_lazy=1&wx_co=1")
                .into(imageView_userIcon);
        accountSecurityPresenter = new AccountSecurityPresenter(this, "2858678706");
        constraintLayout_change_Icon = findViewById(R.id.change_Icon);
        textView = findViewById(R.id.textView_save);
        editText_textView_userName = findViewById(R.id.textView_userName);
        editText_textView_userName.setText(userBaseMessageEventBus.getUserName());
        cancel_your_account = findViewById(R.id.cancel_your_account);
        cancel_your_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/cancelyouraccount/SignOutPromptActivity").navigation();
            }
        });
        accountSecurityPresenter.initData();
        //根据获取到的进行图片的设置
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        constraintLayout_change_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialogPick;
                builder = new AlertDialog.Builder(AccountSecurityActivity.this);
                View viewDialog = getLayoutInflater().inflate(R.layout.dialog_choise, null);
                builder.setView(viewDialog);
                dialogPick = builder.create();
                dialogPick.show();
                Button button_takingPictures = viewDialog.findViewById(R.id.button_takingPictures);
                button_takingPictures.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(AccountSecurityActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    100);
                        } else {
                            take();
                        }
                        dialogPick.dismiss();
                    }
                });
                Button button_chooseFromTheAlbum = viewDialog.findViewById(R.id.button_chooseFromTheAlbum);
                button_chooseFromTheAlbum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(AccountSecurityActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    10);
                        } else {
                            openAlbum();
                        }
                        dialogPick.dismiss();
                    }
                });
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, editText_textView_userName.getText().toString());
                Log.d(TAG, String.valueOf(UriSave.getInstance().getUriImage()));
                accountSecurityPresenter.saveMessageNameAndIcon(userBaseMessageEventBus.getUserId(),
                        editText_textView_userName.getText().toString(), UriSave.getInstance().getUriImage());
            }
        });
        //退出登录按钮
        textView_sign_out = findViewById(R.id.sign_out);
        textView_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountSecurityPresenter.signOut();
                finish();
                ARouter.getInstance().build("/login/LoginUpActivity").navigation();
                ActivityManager.getInstance().finishAllActivities();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                take();
            } else {
                Toast.makeText(AccountSecurityActivity.this, "拍照权限受限制", Toast.LENGTH_SHORT).show();
            }
        }
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
        if (requestCode == TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    imageView_userIcon.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                ;
            }
        }
        if (requestCode == CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) {
                if (Build.VERSION.SDK_INT >= 19) {
                    handleImageOnKitKat(data);
                } else {
                    handleImageBeforeKitKat(data);
                }
            }
        }
    }

    public void take() {
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(AccountSecurityActivity.this,
                    "com.example.selfdiscipline.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        UriSave.getInstance().setUriImage(outputImage);
        Log.d(TAG, "take：" + outputImage.toString());
        startActivityForResult(intent, TAKE_PHOTO);
    }

    public void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

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
        UriSave.getInstance().setUriImage(new File(imagepath));
        Log.d(TAG, "take：" + UriSave.getInstance().getUriImage().toString());
        displayImage(imagepath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        Log.d(TAG, "take：" + uri.toString());
        String imagepath = getImagePath(uri, null);
        displayImage(imagepath);
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

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageView_userIcon.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onMoonStickyEvent(UserBaseMessageEventBus userBaseMessageEventBus) {
        this.userBaseMessageEventBus = userBaseMessageEventBus;
    }

    public void sendToast(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AccountSecurityActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().removeActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void sendEventBus(UserBaseMessage userBaseMessage) {
        UserBaseMessageEventBus userBaseMessageEventBus1;
        userBaseMessageEventBus1 = new UserBaseMessageEventBus(userBaseMessage.getUserName(), userBaseMessage.getUserPictureURL(),
                userBaseMessage.getUserEmail(), userBaseMessage.getUserToken(), userBaseMessage.getUserId());
        EventBus.getDefault().postSticky(userBaseMessageEventBus1);
    }
}