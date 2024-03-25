package com.example.sign_up;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

// This is the main page that shows to the user after login
public class Home extends AppCompatActivity {


    ImageView imgLogout;

    TabLayout tabLayout;
    ViewPager2 viewPager;
    MyViewPageAdapter myViewPageAdapter;

    Button btnInsta;


    // used this to track if the user is already logged-in or out
    SharedPreferences sf;


    private static final String SHARED_PF_NAME = "user_pf";
    private static final String KEY_FIRSTNAME = "firstName";
    private static final String KEY_LASTNAME = "lastName";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        init();


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }

        });


        // after pressing the instagram screen button
        // it will direct the user to the instagram page

        btnInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Home.this, Instagram.class));

            }
        });


        // if the user has already made an id, show them the login panel
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               SharedPreferences.Editor editor = sf.edit();
               editor.putBoolean(KEY_IS_LOGGED_IN,false);
               editor.apply();

                startActivity(new Intent(Home.this, MainActivity.class));
                finish();
            }
        });

    }

    private void init(){


        imgLogout = findViewById(R.id.imgLogout);

        tabLayout= findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);


        myViewPageAdapter = new MyViewPageAdapter(this);
        viewPager.setAdapter(myViewPageAdapter);

        btnInsta = findViewById(R.id.btnInsta);

        // SHARED PREFERENCES
        sf = getSharedPreferences(SHARED_PF_NAME,MODE_PRIVATE);

    }
}