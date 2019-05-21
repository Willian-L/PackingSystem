package com.william.parksystem.ResetPassword;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.william.parksystem.R;
import com.william.parksystem.SQLite.DBServerForU;
import com.william.parksystem.Information.User;

public class ResetVerActivity extends AppCompatActivity {

    private String realCode;
    private String phoneInput;
    private int result;
    private String codeStr;
    private String fromLogin;

    EditText edtiReUser, edtCode;
    Button btnReset;
    ImageView imageView;

    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_ver);

        inti();

        renewal();

        final Intent intent = getIntent();
        fromLogin = intent.getStringExtra("username");

        if (fromLogin != null) {
            edtiReUser.setText(fromLogin);
        }

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeStr = edtCode.getText().toString().trim();
//                showResetDialog();
                if (edtiReUser != null) {
                    if (edtCode != null) {
                        codeStr = edtCode.getText().toString().trim();
                        if (realCode.equals(codeStr)) {
                            showResetDialog();
                            if (phoneInput != null) {
                                user.setUsername(edtiReUser.getText().toString().trim());
                                user.setPhone(phoneInput);
                                DBServerForU dbServerForU = new DBServerForU(getApplicationContext());
                                dbServerForU.open();
                                result = dbServerForU.resetVerify(user.getUsername(), user.getPhone());
                                switch (result) {
                                    case 2:
                                        Intent intent2 = new Intent(ResetVerActivity.this, ResetPasswordActivity.class);
                                        intent2.putExtra("username", fromLogin);
                                        startActivity(intent2);
                                    case 1:
                                        Toast.makeText(getApplicationContext(),
                                                "The input of the mobile phone number is wrong!", Toast.LENGTH_SHORT).show();
                                    case 0:
                                        Toast.makeText(getApplicationContext(),
                                                "This username does not exist!", Toast.LENGTH_SHORT).show();
                                    default:
                                        break;
                                }
                                dbServerForU.close();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Membercode!", Toast.LENGTH_SHORT).show();
                            edtCode.setText(null);
                            renewal();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please enter the verification code!", Toast.LENGTH_SHORT).show();
                        renewal();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your user name!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renewal();
            }
        });
    }

    private void renewal() {
        IdentifyingCode identifyingCode = IdentifyingCode.getInstance();
        Bitmap bitmap = identifyingCode.createBitmap();
        imageView.setImageBitmap(bitmap);
        realCode = identifyingCode.getCode().toLowerCase().toString();
        Log.i("real",realCode+"");
    }

    private void showResetDialog() {
        final View dialogView = LayoutInflater.from(ResetVerActivity.this).inflate(R.layout.dialog_phone, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResetVerActivity.this);
        alertDialog.setTitle("Please enter your full mobile phone number!");
        alertDialog.setView(dialogView);
        alertDialog.setPositiveButton("CONFIRM",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editPhone = dialogView.findViewById(R.id.edtDiophone);
                        phoneInput = editPhone.getText().toString().trim();
                        if (phoneInput != null) {
                            user.setUsername(edtiReUser.getText().toString().trim());
                            user.setPhone(phoneInput);
                            DBServerForU dbServerForU = new DBServerForU(getApplicationContext());
                            dbServerForU.open();
                            result = dbServerForU.resetVerify(user.getUsername(), user.getPhone());
                            Log.i("ver", "result" + result);
                            switch (result) {
                                case 2:
                                    Intent intent1 = new Intent(ResetVerActivity.this, ResetPasswordActivity.class);
                                    intent1.putExtra("username", fromLogin);
                                    startActivity(intent1);
                                    break;
                                case 1:
                                    Toast.makeText(getApplicationContext(),
                                            "The input of the mobile phone number is wrong!", Toast.LENGTH_SHORT).show();
                                    break;
                                case 0:
                                    Toast.makeText(getApplicationContext(),
                                            "This username does not exist!", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    break;
                            }
                            dbServerForU.close();
                        }
                    }
                });
        alertDialog.setNegativeButton("CLOSE",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog.show();
    }

    private void inti() {
        edtCode = findViewById(R.id.edtVerCode);
        edtiReUser = findViewById(R.id.edtVerUser);
        btnReset = findViewById(R.id.btnVerPassword);
        imageView = findViewById(R.id.imgVer);
    }
}
