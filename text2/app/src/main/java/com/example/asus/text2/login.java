package com.example.asus.text2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


public class login extends AppCompatActivity {

    @BindView(R.id.name)
    EditText name0;
    @BindView(R.id.password)
    EditText password0;
    @BindView(R.id.tv_info)
    TextView mTvInfo;

    BmobQuery<Newbean> newbeanBmobQuery = new BmobQuery<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.button_regist,R.id.button_login1})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.button_regist:
                startActivity(new Intent(this,regist.class));
                break;
            case R.id.button_login1:
                final String name1 = name0.getText().toString().trim();
                final String password1 = password0.getText().toString().trim();
                newbeanBmobQuery.addWhereEqualTo("mobilePhoneNumber",name1);
                newbeanBmobQuery.findObjects(new FindListener<Newbean>() {
                    @Override
                    public void done(List<Newbean> list, BmobException e) {
                        if(list.size()==0){
                            Toast.makeText(login.this, "此手机号尚未注册！", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            Newbean newbean = new Newbean();
                            newbean.setUsername(name1);
                            newbean.setPassword(password1);
                            newbean.login(new SaveListener<BmobUser>() {
                                @Override
                                public void done(BmobUser bmobUser, BmobException e){
                                    if (e == null) {
                                        Toast.makeText(login.this, "密码正确！", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(login.this, "账户名或密码不正确！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
                break;
        }

    }

}
