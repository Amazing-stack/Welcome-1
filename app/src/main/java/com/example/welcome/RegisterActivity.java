package com.example.welcome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText mEmail, mPassword, mDuty;
    private Button mSignIn,mSignup;
    private ProgressBar progressbar;
    private FirebaseUser user;
    private ImageView user_profile;
    Uri image_uri;
    public static final String TAG = "CRM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mSignup = (Button) findViewById(R.id.signup);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mDuty = (EditText) findViewById(R.id.dutyid);
        user_profile = (ImageView)findViewById(R.id.add_pic);
        progressbar = (ProgressBar) findViewById(R.id.progresbar);
       user_profile.setOnClickListener(this);
        mDuty.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();

    }




    public void onClick(View v) {

        if(v.getId() == R.id.login_btn_on_signup){
            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
        }
        else if (v.getId() == R.id.add_pic) { Toast.makeText(RegisterActivity.this, "Select Profile!!",
                    Toast.LENGTH_SHORT).show();
            SelectProfilePic();
        }

    }
        @Override
        public void onStart () {
            super.onStart();
            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);




            mSignup.setOnClickListener(new View.OnClickListener() {


                public void onClick(final View v) {
                    String email = mEmail.getText().toString();
                    String password = mPassword.getText().toString();

                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplicationContext(), "Enter email address !", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getApplicationContext(), "Enter password ! ", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (password.length() < 6) {
                        mPassword.setError("Password must be >= 6 Characters");
                        return;
                    }

                    if (image_uri == null) {
                        Toast.makeText(RegisterActivity.this, "Select Profile!!",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }



                    progressbar.setVisibility(View.VISIBLE);

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Registration Successful." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Registration failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            progressbar.setVisibility(View.GONE);
                        }
                    });


                }
            });


        }


                private void SelectProfilePic() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")){
                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                        if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                            String [] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permission,1000);
                        }
                        else {
                            openCamera();
                        }
                    }
                    else {
                        openCamera();
                    }
                }
                else if (options[item].equals("Choose from Gallery")){

                    Intent intent = new   Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 2);

                }
                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }
            }
        });
        builder.show();

    }

    private void openCamera() {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE,"New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION,"From Camera");
            image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

            //Camera intent
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
            startActivityForResult(takePictureIntent, 1);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1000:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }
                else {
                    //permisiion from pop up was denied.
                    Toast.makeText(RegisterActivity.this,"Permission Denied...",Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    user_profile.setImageURI(image_uri);
                    break;
                case 2:
                    //data.getData returns the content URI for the selected Image
                    image_uri = data.getData();
                    user_profile.setImageURI(image_uri);
                    break;
            }
        }
    }
    private void updateUI(FirebaseUser user) {

            user = mAuth.getCurrentUser();
        if(user != null){
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(image_uri).build();

            if(user.isEmailVerified()){
                Toast.makeText(RegisterActivity.this, "Login Success.",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            }
            else {
                Toast.makeText(RegisterActivity.this, "Your Email is not verified.",
                        Toast.LENGTH_SHORT).show();
                final FirebaseUser finalUser = user;
                user.updateProfile(profileUpdates).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    verifyEmailRequest();
            /*-------- Check if user is already logged in or not--------*/



            }





    private void verifyEmailRequest() {

        finalUser.sendEmailVerification()
        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<Void>() {
            @Override
           public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
        Toast.makeText(RegisterActivity.this, "Email verification sent on\n" + mEmail.getText().toString(), Toast.LENGTH_LONG).show();
        mAuth.signOut();
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        } else {
        Toast.makeText(RegisterActivity.this, "Sign up Success But Failed to send verification email.", Toast.LENGTH_LONG).show();

    }

    }

            });
             }
        });
     }
   }
 }
}
