package com.william.parksystem.LoginAndRegister;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.william.parksystem.R;
import com.william.parksystem.ResetPassword.ResetVerActivity;
import com.william.parksystem.SQLite.DBServerForU;
import com.william.parksystem.Information.User;
import com.william.parksystem.UserHomepage.HomepageForUActivity;

public class UserLoginActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnLogin;
    CheckBox checkRemember;

    // Define an identity to get the selected value of RdaioGroup
    public String type = "User";
    private SharedPreferences sp;

    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);


        init();

        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        if (sp.getBoolean("ISCHECK", false)) {
            checkRemember.setChecked(true);
            edtUsername.setText(sp.getString("USER_NAME", ""));
            edtPassword.setText(sp.getString("PASSWORD", ""));
            Intent intent = new Intent(UserLoginActivity.this, HomepageForUActivity.class);
            UserLoginActivity.this.startActivity(intent);
        }

        Log.i("edt", "edtPassword:" + edtPassword.getText() + "edtUsername:" + edtUsername.getText());

        try {
            Intent intent = getIntent();
            String username = intent.getStringExtra("username");
            edtUsername.setText(username);
        } catch (Exception e) {
        }

        checkRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkRemember.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Remember Password!", Toast.LENGTH_SHORT).show();
                    sp.edit().putBoolean("ISCHECK", true).commit();
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = 0;

                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                boolean hasUsernameValue = false;
                if (!username.equals("")) {
                    hasUsernameValue = true;
                }

                boolean hasPasswordValue = false;
                if (!password.equals("")) {
                    hasPasswordValue = true;
                }

                if (hasUsernameValue && hasPasswordValue) {
                    // Get the username and password
                    user.setUsername(edtUsername.getText().toString().trim());
                    user.setPassword(edtPassword.getText().toString().trim());
                    DBServerForU dbServerForU = new DBServerForU(getApplicationContext());
                    dbServerForU.open();
                    Log.i("eab", edtUsername.getText().toString() + edtPassword.getText().toString());
                        /*
                        Determine whether the username and password match the one in the database
                         */
                    result = dbServerForU.login(user.getUsername(), user.getPassword());
                    switch (result) {
                        case 2:
                            if (checkRemember.isChecked()) {
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("USER_NAME", user.getUsername());
                                editor.putString("PASSWORD", user.getPassword());
                                editor.commit();
                            }
                            Intent intent = new Intent(UserLoginActivity.this, HomepageForUActivity.class);
                            intent.putExtra("username", user.getUsername());
                            startActivity(intent);
                            dbServerForU.close();
                            break;
                        case 1:
                            showResetDialog();
                            break;
                        case 0:
                            showRegisterDialog();
                            break;
                        default:
                            break;
                    }
                    dbServerForU.close();

                } else if (hasUsernameValue && !hasPasswordValue) {
                    Toast.makeText(getApplicationContext(),
                            "Please input your password!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please input your username and password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showRegisterDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserLoginActivity.this);
        alertDialog.setTitle("This username does not exist!");
        alertDialog.setMessage("Do you need to register a new account?");
        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toRegister();
                    }
                });
        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog.show();
    }

    private void showResetDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserLoginActivity.this);
        alertDialog.setTitle("Password is wrong!");
        alertDialog.setMessage("Do you need to reset your password?");
        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toForgot();
                    }
                });
        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog.show();
    }

    public void register(View view) {
        toRegister();
    }

    public void forgot(View view) {
        toForgot();
    }

    private void toRegister() {
        Intent intent = new Intent(UserLoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void toForgot() {
        Intent intent = new Intent(UserLoginActivity.this, ResetVerActivity.class);
        if (edtUsername != null) {
            intent.putExtra("username", edtUsername.getText().toString().trim());
        }
        startActivity(intent);
    }

    /*
    Access controls
     */
    private void init() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        checkRemember = findViewById(R.id.loginCb_Remember);
    }
}
