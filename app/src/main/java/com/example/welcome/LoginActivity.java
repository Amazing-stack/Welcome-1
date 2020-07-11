package com.example.welcome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity<user> extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mEmail, mPassword;
    private TextView mSignIn, mSignup, forp;
    private ProgressBar progressbar;
    public static final String TAG = "CRM";
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSignIn = (TextView) findViewById(R.id.signin);
        mSignup = (TextView) findViewById(R.id.signup);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        forp = (TextView) findViewById(R.id.forgotpswd);

        mAuth = FirebaseAuth.getInstance();



      forp.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                             }
                         });
        mSignup.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                                       }
                                   });

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();{
                    if(email.equals("")){
                        Toast.makeText(LoginActivity.this, "Enter Email address!!",
                                Toast.LENGTH_SHORT).show();
                    }
                    else if(password.equals("")){
                        Toast.makeText(LoginActivity.this, "Enter Password!!",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "Login Successful");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Your email is not verified.",
                                            Toast.LENGTH_SHORT).show();

                                }

                            }


            });

                    }
                }

                            };

                    });
            }
}






