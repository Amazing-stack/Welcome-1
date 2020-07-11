package com.example.welcome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText inputEmail, changePassword;
    private Button btnReset;
    private FirebaseAuth mauth;
    private ProgressBar progressBar;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        inputEmail = (EditText)findViewById(R.id.email);
        changePassword = (EditText)findViewById(R.id.changepassword);
        btnReset= (Button)findViewById(R.id.btn_reset_password);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        mauth = FirebaseAuth.getInstance();



        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                        changePassword.setVisibility(View.GONE);

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Enter your registered email", Toast.LENGTH_LONG).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                mauth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgotPasswordActivity.this,"We have sent you instructions to reset your password!",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(ForgotPasswordActivity.this,"Failed to send reset email!",Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
        if (user != null && !changePassword.getText().toString().trim().equals("")) {
            if (changePassword.getText().toString().trim().length() < 6) {
                 changePassword.setError("Password too short, enter minimum 6 characters");
                progressBar.setVisibility(View.GONE);
            } else {
                user.updatePassword(changePassword.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPasswordActivity.this, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(ForgotPasswordActivity.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        } else if (changePassword.getText().toString().trim().equals("")) {
            changePassword.setError("Enter password");
            progressBar.setVisibility(View.GONE);
        }
    }
});
    }
    }
