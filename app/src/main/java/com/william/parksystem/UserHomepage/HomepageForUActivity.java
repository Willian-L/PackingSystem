package com.william.parksystem.UserHomepage;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.william.parksystem.UserHomepage.Fragment.MyFragment;
import com.william.parksystem.UserHomepage.Fragment.PlaceFragment;
import com.william.parksystem.UserHomepage.Fragment.PayFragment;
import com.william.parksystem.R;
import com.william.parksystem.Information.User;

import org.w3c.dom.Text;

public class HomepageForUActivity extends AppCompatActivity implements View.OnClickListener {

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private RadioButton rb_pay, rb_place, rb_my;

    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_for_u);

        init();

        rb_place.setActivated(true);

        Intent intent = getIntent();
        user.setUsername(intent.getStringExtra("username"));
        Log.i("user",intent.getStringExtra("username"));

        manager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        PlaceFragment plFragment = new PlaceFragment();
        bundle.putString("username", user.getUsername());
        plFragment.setArguments(bundle);
        transaction = manager.beginTransaction();
        transaction.add(R.id.user_content_Layout, plFragment);
        transaction.commit();

    }

    public void init() {
        rb_place = findViewById(R.id.rad_place);
        rb_pay = findViewById(R.id.rad_pay);
        rb_my = findViewById(R.id.rad_my);

        rb_place.setOnClickListener(this);
        rb_pay.setOnClickListener(this);
        rb_my.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        transaction = manager.beginTransaction();
        switch (v.getId()) {
            case R.id.rad_place:
                Bundle bundle = new Bundle();
                PlaceFragment plFragment = new PlaceFragment();
                bundle.putString("username", user.getUsername());
                plFragment.setArguments(bundle);
                transaction.replace(R.id.user_content_Layout, plFragment);
                rb_place.setActivated(true);
                rb_pay.setActivated(false);
                rb_my.setActivated(false);
                break;
            case R.id.rad_pay:
                Bundle bundle1 = new Bundle();
                PayFragment payFragment = new PayFragment();
                bundle1.putString("username", user.getUsername());
                payFragment.setArguments(bundle1);
                transaction.replace(R.id.user_content_Layout, payFragment);
                rb_place.setActivated(true);
                rb_pay.setActivated(false);
                rb_my.setActivated(false);
                break;
            case R.id.rad_my:
                Bundle bundle2 = new Bundle();
                bundle2.putString("username", user.getUsername());
                MyFragment myFragment = new MyFragment();
                myFragment.setArguments(bundle2);
                transaction.replace(R.id.user_content_Layout, myFragment);
                rb_my.setActivated(true);
                rb_place.setActivated(false);
                rb_pay.setActivated(false);
                break;
        }
        transaction.commit();
    }


}

