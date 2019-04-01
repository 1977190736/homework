package com.example.asus.text2;

import android.app.Person;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class regist extends AppCompatActivity {


    @BindView(R.id.edt_phone)
    EditText mEdtPhone;             //手机
    @BindView(R.id.edt_name)
    TextView name0;                 //昵称
    @BindView(R.id.edt_password)
    TextView password0;             //密码
    @BindView(R.id.edt_code)
    EditText mEdtCode;
    @BindView(R.id.tv_info)
    TextView mTvInfo;

    private Button Message_btn;
    //注册需要加东西
    BmobQuery<Newbean> newbeanBmobQuery = new BmobQuery<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist);
        ButterKnife.bind(this);
        Message_btn = findViewById(R.id.btn_send);

        Bmob.resetDomain("http://open-vip.bmob.cn/8/");

        BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
            @Override
            public void done(BmobInstallation bmobInstallation, BmobException e) {

            }
        });

    }

    @OnClick({R.id.btn_send,R.id.btn_verify})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.btn_send:{
                final String phone = mEdtPhone.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }if(phone.length() != 11){
                    Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                newbeanBmobQuery.addWhereEqualTo("mobilePhoneNumber",phone);
                newbeanBmobQuery.findObjects(new FindListener<Newbean>() {
                    @Override
                    public void done(List<Newbean> list, BmobException e) {
                        if(list.size()==1){
                            Toast.makeText(regist.this, "此手机号已注册！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if(e == null&&list.size()==0){
                            BmobSMS.requestSMSCode(phone, "make it", new QueryListener<Integer>() {
                                @Override
                                public void done(Integer integer, BmobException e) {
                                    if (e == null){
                                        Message_btn.setClickable(false);
                                        Message_btn.setBackgroundColor(Color.GRAY);
                                        mTvInfo.append("发送验证码成功，短信ID：" + integer + "\n");
                                        new CountDownTimer(60000,1000){

                                            @Override
                                            public void onTick(long millisUntilFinished) {
                                                Message_btn.setBackgroundResource(R.drawable.button_shape02);
                                                Message_btn.setText(millisUntilFinished / 1000 + "秒");

                                            }

                                            @Override
                                            public void onFinish() {
                                                Message_btn.setClickable(true);
                                                Message_btn.setBackgroundResource(R.drawable.button_shape);
                                                Message_btn.setText("重新发送");

                                            }
                                        }.start();

                                    }else {
                                        mTvInfo.append("发送验证码失败：" + e.getErrorCode() + "-" + e.getMessage() + "\n");
                                    }
                                }
                            });
                        }else{
                            mTvInfo.append("手机查询失败！：" + e.getMessage() + "\n");
                        }
                    }
                });
                break;
            }
            case R.id.btn_verify:{
                final String phone = mEdtPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String code = mEdtCode.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String password3 = password0.getText().toString().trim();
                if (TextUtils.isEmpty(password3)) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    String name5 = name0.getText().toString().trim();
                    final Newbean newbean = new Newbean();
                    newbean.setMobilePhoneNumber(phone);
                    newbean.setUsername(name5);
                    newbean.setPassword(password3);
                    newbean.signOrLogin(code, new SaveListener<Newbean>() {
                        @Override
                        public void done(Newbean bmobUser, BmobException e) {
                            if (e == null) {
                                //判断是否注册成功成功则跳转到登陆的页面
                                Toast.makeText(regist.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                newbean.signUp(new SaveListener<Newbean>() {
                                    @Override
                                    public void done (Newbean newbean,BmobException e){
                                        if(e == null){
                                            mTvInfo.append("保存成功！");
                                        }else{
                                            mTvInfo.append("创建失败："+e.getMessage());
                                        }
                                    }
                                });
                            } else {
                                mTvInfo.append("创建失败："+e.getMessage());
                            }
                        }
                    });
                }

                break;
            }
            default:
                break;
        }
    }

}
 /*String name5 = name0.getText().toString().trim();
                            String password5 = password0.getText().toString().trim();
                            String phone5 = mEdtPhone.getText().toString().trim();
                            final Newbean p2 = new Newbean();
                            p2.setName(name5);
                            p2.setPassword(password5);
                            p2.setPhone(phone5);
                            p2.signUp(new SaveListener<Newbean>() {
                                @Override
                                public void done (Newbean newbean,BmobException e){
                                    if(e == null){
                                        Toast.makeText(regist.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                    }else{
                                        mTvInfo.append("创建失败："+e.getMessage());
                                    }
                                }

                            });*/