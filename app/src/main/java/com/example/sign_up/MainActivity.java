package com.example.sign_up;

import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButtonToggleGroup;

public class MainActivity extends AppCompatActivity {


    // getting the view of the fragments here
    FragmentManager fragmentManager;

    Fragment loginFragment, registerFragment;

    View loginView, registerView;


    // getting all the views in the fragments for hooks
    Button btnExit_r, btnRegister_r, btnLogin_l, btnRegister_l;

    RadioGroup radioGroup_r;

    EditText etEmail_l, etPassword_l, etFirstName_r , etLastName_r,  etEmail_r, etPassword_r, etConfirmPasswrod_r;

    TextView tvLogin_r;


    // to check the login part
    SharedPreferences sharedPreferences;

    int gender;


    // keys to store them in a hash map
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
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        // checking if the user is already logged-in (hasn't logout)
        if(sharedPreferences.getBoolean(KEY_IS_LOGGED_IN,false))
        {
            startActivity(new Intent(MainActivity.this, Home.class));
            finish();
        }

        // register page will show to the user first
        // when the app opens or the user logs-out
        fragmentManager.beginTransaction()
                .hide(loginFragment)
                .commit();

        // checking the radio button
        radioGroup_r.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.btnMale_r)
                    gender = 1;
                else if (checkedId == R.id.btnFemale_r)
                    gender = 2;
                else
                    gender = -1;

            }
        });

        // when user presses the login button
        btnLogin_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // taking the email and password
                String email = etEmail_l.getText().toString().trim();
                String password = etPassword_l.getText().toString().trim();

                if ( email.isEmpty() || password.isEmpty() ) {

                    Toast.makeText(MainActivity.this, "Please fill out the form first", Toast.LENGTH_SHORT).show();
                    return;

                }

                // checking the email and password from our database
                // matching that with the user given

                if(sharedPreferences.getString(KEY_EMAIL,null).equals(email) && sharedPreferences.getString(KEY_PASSWORD,null).equals(password)) {

                    editor.putBoolean(KEY_IS_LOGGED_IN, true);
                    editor.apply();

                    Intent intent = new Intent(MainActivity.this, Home.class);
                    startActivity(intent);

                    finish();

                }
                else {

                    Toast.makeText(MainActivity.this, "Invalid Email Or Password", Toast.LENGTH_SHORT).show();
                    clear();
                    return;

                }

                clear();

            }
        });

        // when user will press the register button
        btnRegister_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // simply show them the register panel
                fragmentManager.beginTransaction()
                        .hide(loginFragment)
                        .show(registerFragment)
                        .commit();

            }
        });

        // when user will press the register button in the register panel
        btnRegister_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting all the information from the usee
                String firstName = etFirstName_r.getText().toString().trim();
                String lastName = etLastName_r.getText().toString().trim();
                String email = etEmail_r.getText().toString().trim();
                String password = etPassword_r.getText().toString();
                String rePassword = etConfirmPasswrod_r.getText().toString();

                if (  firstName.isEmpty() || lastName.isEmpty() ||email.isEmpty() || password.isEmpty() || rePassword.isEmpty() ){
                    Toast.makeText(MainActivity.this, "Please fill out the form first", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(rePassword)){
                    Toast.makeText(MainActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                    return;
                }

                String userGender = "";
                if(gender != -1) {
                    if(gender == 1){
                        userGender += "male";
                    }
                    else if(gender == 2){
                        userGender += "female";
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Select your gender", Toast.LENGTH_SHORT).show();
                    return;
                }



                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(KEY_FIRSTNAME,firstName);
                editor.putString(KEY_LASTNAME,lastName);
                editor.putString(KEY_EMAIL,email);
                editor.putString(KEY_PASSWORD,password);
                editor.putString(KEY_GENDER,userGender);
                editor.putBoolean(KEY_IS_LOGGED_IN,false);
                editor.apply();


                Toast.makeText(MainActivity.this, "Registered!", Toast.LENGTH_SHORT).show();
                clear();

                // after registering, show them the login panel
                fragmentManager.beginTransaction()
                        .hide(registerFragment)
                        .show(loginFragment)
                        .commit();

            }
        });

        // when user will press the exit button
        btnExit_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //close the main activity, hence the application itself
                finish();

            }
        });

        // if user have already an ID, show them the login panel
        tvLogin_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .hide(registerFragment)
                        .show(loginFragment)
                        .commit();

            }
        });


    }

    private void init(){

        fragmentManager = getSupportFragmentManager();

        loginFragment = fragmentManager.findFragmentById(R.id.loginFragment);
        registerFragment = fragmentManager.findFragmentById(R.id.registerFragment);

        loginView = loginFragment.requireView();
        registerView = registerFragment.requireView();

        // LOGIN PAGE

        btnLogin_l = loginView.findViewById(R.id.btnLogin_l);
        btnRegister_l = loginView.findViewById(R.id.btnRegister_l);

        etEmail_l = loginView.findViewById(R.id.etEmail_l);
        etPassword_l = loginView.findViewById(R.id.etPassword_l);

        // REGISTER PAGE

        btnRegister_r = registerView.findViewById(R.id.btnRegister_r);
        btnExit_r = registerView.findViewById(R.id.btnExit_r);
        radioGroup_r = registerView.findViewById(R.id.radioGroup_r);


        etFirstName_r = registerView.findViewById(R.id.etFirstName_r);
        etLastName_r = registerView.findViewById(R.id.etLastName_r);
        etEmail_r = registerView.findViewById(R.id.etEmail_r);
        etPassword_r = registerView.findViewById(R.id.etPassword_r);
        etConfirmPasswrod_r = registerView.findViewById(R.id.etConfirmPassword_r);

        tvLogin_r = registerView.findViewById(R.id.tvLogin_r);

        gender =-1;

        // SHARED PREFERENCES
        sharedPreferences = getSharedPreferences(SHARED_PF_NAME,MODE_PRIVATE);

    }

    private void clear(){
        etEmail_l.setText("");
        etPassword_l.setText("");

        etFirstName_r.setText("");
        etLastName_r.setText("");
        etEmail_r.setText("");
        etPassword_r.setText("");
        etConfirmPasswrod_r.setText("");
    }
}