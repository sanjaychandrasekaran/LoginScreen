package com.sanjays.loginscreen;

import static com.sanjays.loginscreen.R.id.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener {

    private TextView register;
    private EditText editTextEmail, editTextpassword;
    private Button signIn;
//    private View v;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);
//        initialze signin button
        signIn = (Button) findViewById(R.id.signin);
        signIn.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextpassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

//        to initialize mAuth
        mAuth = FirebaseAuth.getInstance();

    }
//    to implements register id

    public void onClick(View v) {

//      if the user clicks register it will show this
        switch (v.getId()) {
            case R.id.register:
                startActivity(new Intent(this, RegisterScreen.class));
                break;


            case R.id.signin:
//              we have to create a method
                userLogin();
                break;

//                once the user is sign in


        }
    }

    private void userLogin() {
//        it will create a method and click
        String email = editTextEmail.getText().toString().trim();
        String password = editTextpassword.getText().toString().trim();


//        validation
//        if user is not provided any details
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

//              if user type wrong email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextpassword.setError("Password is required");
            editTextpassword.requestFocus();
            return;
        }
//        we need to check minimum paqsswrod length
        if (password.length() < 6) {
            editTextpassword.setError("your password must contains less characters");
            editTextpassword.requestFocus();
            return;
        }

//        set progress bar
        progressBar.setVisibility(View.VISIBLE);

//        to get the data in firebase
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()) {

//                    redirect to user profile
//                    create one profile activity
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));

                    }
//                  if not sent the veriofication link to the user
                    else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check your email to verify your account", Toast.LENGTH_LONG).show();
                    }
                }
                    else {
                    Toast.makeText(MainActivity.this, "Failed to login!Please check your credentials", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    }




