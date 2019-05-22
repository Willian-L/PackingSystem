package com.william.parksystem.ResetPassword;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.william.parksystem.R;
import com.william.parksystem.SQLite.DBServerForU;
import com.william.parksystem.Information.User;
import com.william.parksystem.LoginAndRegister.UserLoginActivity;

import static com.orhanobut.logger.Logger.init;

public class ResetPasswordActivity extends AppCompatActivity {

    private String username;

    TextView txtUser;
    Button btnReset;
    EditText editPsw;

    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        init();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUsername(username);
                String password = editPsw.getText().toString().trim();
                if (!password.matches("^[A-Za-z0-9]+$")) {
                    Toast.makeText(getApplicationContext(),
                            "Password must be alphanumeric!", Toast.LENGTH_SHORT).show();
                }else {
                    user.setPassword(editPsw.getText().toString().trim());
                    DBServerForU dbServerForU = new DBServerForU(getApplicationContext());
                    dbServerForU.open();
                    if (dbServerForU.ResetPsw(user.getUsername(), user.getPassword())){
                        Toast.makeText(getApplicationContext(),"Reset successful!",Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(ResetPasswordActivity.this, UserLoginActivity.class);
                        intent1.putExtra("username",username);
                        startActivity(intent1);
                    }
                }
            }
        });
    }

    private void init() {
        btnReset = findViewById(R.id.btnNewPSW);
        editPsw = findViewById(R.id.editNewPSW);
        txtUser = findViewById(R.id.txtUsername);
    }
}
