package com.example.findpassword;

import android.util.Log;
import android.widget.Toast;

import com.example.networkrequests.NetworkClient;

import java.io.IOException;

public class FindPasswordPresenter {
    FindPasswordActivity findPasswordActivity;
    FindPasswordModule findPasswordModule;

    public FindPasswordPresenter(FindPasswordActivity findPasswordActivity) {
        NetworkClient networkClient = new NetworkClient();
        this.findPasswordActivity = findPasswordActivity;
        this.findPasswordModule = new FindPasswordModule(networkClient);
    }
    public void SendAVerificationCodeToResetYourPassword(String emailNumber) {
        findPasswordModule.sendEmailVerificationCode(emailNumber, new FindPasswordModule.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {
                findPasswordActivity.SendToast("发送验证码成功");
                return true;
            }

            @Override
            public Boolean onFailure(IOException e) {
                return false;
            }
        });
    }
    public void VerificationOfTheVerificationCode(String emailNumber, String code, String password) {
        findPasswordModule.VerificationOfTheVerificationCode(emailNumber, code, new FindPasswordModule.ModelCallback() {
            @Override
            public Boolean onSuccess(String response) {
                Log.d("TestTT_", "验证码验证成功");
                findPasswordModule.ResetOfPassword(password, new FindPasswordModule.ModelCallback() {
                    @Override
                    public Boolean onSuccess(String response) {
                        findPasswordActivity.SendToast("密码重置成功");
                        findPasswordActivity.finish();
                        return null;
                    }

                    @Override
                    public Boolean onFailure(IOException e) {
                        Log.d("TestTT_", "验证码验证成功，但");
                        Log.d("TestTT_", emailNumber + code + password);
                        findPasswordActivity.SendToast("密码重置错误");
                        return null;
                    }
                });
                return null;
            }

            @Override
            public Boolean onFailure(IOException e) {
                findPasswordActivity.SendToast("验证码验证失败");
                return null;
            }
        });
    }
}
