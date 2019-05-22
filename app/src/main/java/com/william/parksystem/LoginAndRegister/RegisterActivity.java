package com.william.parksystem.LoginAndRegister;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.william.parksystem.R;
import com.william.parksystem.SQLite.DBServerForU;
import com.william.parksystem.Information.User;

public class RegisterActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword, edtPhone, edtLicenseNumber;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();

                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                String licenseNumber = edtLicenseNumber.getText().toString().trim();

                /*
                Determine if the username, password, and phone number match the rules
                 */
                if (!username.matches("^[A-Za-z0-9]+$")) {
                    Toast.makeText(getApplicationContext(),
                            "Username must be alphanumeric!", Toast.LENGTH_SHORT).show();
                } else if (!username.matches("^.{6,16}$")) {
                    Toast.makeText(getApplicationContext(),
                            "Username must be at least 6 digits", Toast.LENGTH_SHORT).show();
                } else if (!password.matches("^[A-Za-z0-9]+$")) {
                    Toast.makeText(getApplicationContext(),
                            "Password must be alphanumeric!", Toast.LENGTH_SHORT).show();
                } else if (!password.matches("^.{6,16}$")) {
                    Toast.makeText(getApplicationContext(),
                            "Password must be at least 6 digits", Toast.LENGTH_SHORT).show();
                } else if (!phone.matches("^[1][3,5,6,7,8,9][0-9]{9}$")) {
                    Toast.makeText(getApplicationContext(),
                            "The phone number you entered is incorrect!", Toast.LENGTH_SHORT).show();
                } else if (!licenseNumber.matches("^[A-Z0-9]{5}$")) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter the correct license plate number!", Toast.LENGTH_SHORT).show();
                } else {
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setPhone(phone);
                    user.setLicenseNumber(licenseNumber);
                    try {
                        DBServerForU dbServerForU = new DBServerForU(getApplicationContext());
                        dbServerForU.open();
                        if (dbServerForU.selectByUsername(user.getUsername()).getCount() == 0) {    // Whether the user already exists in the database
                            /*
                            Once all the above rules are met, start writing to the database
                             */
                            if (dbServerForU.insert(user.getUsername(), user.getPassword(), user.getPhone(), user.getLicenseNumber())) {
                                Toast.makeText(getApplicationContext(), "Registration Successful!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, UserLoginActivity.class);
                                intent.putExtra("username", user.getUsername());
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "This user already exists!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Registration failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
    }

    /*
    Button to reset
     */
    public void reset(View view) {
        edtUsername.setText(null);
        edtPassword.setText(null);
        edtPhone.setText(null);
        edtLicenseNumber.setText(null);
    }

    /*
    Access controls
     */
    private void init() {
        edtUsername = findViewById(R.id.edtReUsername);
        edtPassword = findViewById(R.id.edtRePassword);
        edtPhone = findViewById(R.id.edtRePhone);
        btnRegister = findViewById(R.id.btnRegister);
        edtLicenseNumber = findViewById(R.id.edtLicenseNumber);
    }

    public void howToInput(View view) {
        showHowDialog();
    }

    private void showHowDialog(){
        AlertDialog.Builder howDialog = new AlertDialog.Builder(RegisterActivity.this);
        howDialog.setTitle("Input Rules");
        howDialog.setMessage("Username\nConsisting of more than six letters or Numbers.\n\n" +
                "Password\nConsisting of more than six letters or Numbers.\n\n" +
                "Phone\nSupport only phone Numbers beginning with 13, 15, 16, 17, 18, 19.\n\n" +
                "License Number\nIt must be a license plate number consisting of five uppercase letters or Numbers.");
        howDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        howDialog.show();
    }
}
