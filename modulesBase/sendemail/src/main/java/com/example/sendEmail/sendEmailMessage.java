package com.example.sendEmail;

import android.util.Log;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class sendEmailMessage {
    public static String myEmailSMTPHost = "smtp.qq.com";
    public static String account = "2858678706";//自己的邮箱
    public static String password = "scqnwgoqkrvodgfi";//密码

    //发送邮件代码
    //发邮件函数：
    //to代表发送人的邮箱
    //code代表验证码
    public static String sendMail(String to) {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        Log.d("进入了邮件发送函数", "sendEmail: ");
// 1.创建连接对象，链接到邮箱服务器
        final Properties props = new Properties(); // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");// 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", myEmailSMTPHost);// 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true"); // 需要请求认证
        props.put("mail.smtp.port", "465"); // 指定SMTP服务器的SSL端口
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // 使用SSL
        props.put("mail.smtp.socketFactory.port", "465"); // SSL端口号


// 2.根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(account, password);
            }
        });

        try {
// 3.创建邮件对象
            final Message message = new MimeMessage(session);
// 3.1设置发件人
            message.setFrom(new InternetAddress(account + "@qq.com"));
// 3.2设置收件人
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to+"@qq.com"));
// 3.3设置邮件的主题
            message.setSubject("欢迎您");
// 3.4设置邮件的正文
            message.setContent("<h1>您的验证码是：" + code, "text/html;charset=UTF-8");
// 4.发送邮件
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(message);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            //## 重点：新版本的安卓http操作必须都要放到线程中去，否则可能会报错，或者不执行
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return String.valueOf(code);
    }
}
