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


        inputEmail = (EditText) findViewById(R.id.email);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnReset.setOnClickListener((View.OnClickListener) this);

        mauth = FirebaseAuth.getInstance();

    }


    public void onClick(View v) {
        if (v.getId() == R.id.btn_reset_password) {
            PasswordResetEmail(inputEmail.getText().toString());
        }

    }

    /*------------ Below Code is for reset password process user will get email on registered email-----------*/

    private void PasswordResetEmail(final String email) {
        if (email.equals("")) {
            Toast.makeText(ForgotPasswordActivity.this, "Enter Email!! ", Toast.LENGTH_LONG).show();
        } else {
            mauth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPasswordActivity.this, "We have sent a reset password link to email: " + email, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(ForgotPasswordActivity.this, "Email not found in database!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }
    }
}
