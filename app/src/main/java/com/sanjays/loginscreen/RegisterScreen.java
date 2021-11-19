package com.sanjays.loginscreen;

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
import com.google.firebase.database.FirebaseDatabase;

public class RegisterScreen extends AppCompatActivity implements View.OnClickListener {


    private TextView banner, registerUser;
    private EditText editTextFullName,editTextNumber,editTextEmail,editTextPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.banner);

         banner.setOnClickListener( this);

//         if we click register user the page goes to next page
        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

//        also edit text full name password number email
        editTextFullName = (EditText) findViewById(R.id.fullName);
        editTextNumber = (EditText) findViewById(R.id.phoneNumber);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);


//        to initialize progress bar
        progressBar = (ProgressBar)  findViewById(R.id.progressBar);

    }

    @Override
    public void onClick(View v) {
//        to have switch to access each button
        switch(v.getId()){
            case R.id.banner:
//                if its banner we have to redirect the page to homepage
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.registerUser:
//                it will add elements to the firebase if we call the method functions
                registerUser();
                break;


        }

    }

    private void registerUser() {
//        convert each variables to string
        String email = editTextEmail.getText().toString().trim();
        String mobile = editTextNumber.getText().toString().trim();
        String name = editTextFullName.getText().toString().trim();
        String passcode = editTextPassword.getText().toString().trim();

//        to validate the statements
        if(name.isEmpty()){
//            if the user forgets to write his full name it will return a error
            editTextFullName.setError("Full name is required");
            editTextFullName.requestFocus();
            return;
        }
        if(mobile.isEmpty()){
            editTextNumber.setError("Phone number is required");
            editTextNumber.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if(passcode.isEmpty()){
            editTextPassword.setError("password is required");
            editTextPassword.requestFocus();
            return;
        }
//        if the user enter correct email pattern or not to check this
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide Valid email!");
            editTextEmail.requestFocus();
            return;
        }

//        we have to code one more time for password because firebase doent expect password less than 6 characrets
        if(passcode.length()<6){
            editTextPassword.setError("Minimum password length should be six characters!");
            editTextPassword.requestFocus();
            return;
        }

//        to set the visibility of a progress bar
        progressBar.setVisibility(View.VISIBLE);

//        for firebase idf the email and passwords are registered are not


        mAuth.createUserWithEmailAndPassword(email,passcode)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
//                            to create a object
                            User user = new User(name,mobile,email);

//                            to send the objects to real time databases
//                            go to the tools and in firebase go to real time databse click real time databse and activcate real time database
//                            and then enter
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                     .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

//                                    again to check if the task is sucessfull
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterScreen.this, "User has been registered sucessfully", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.VISIBLE);

//                                        once the user is registered we have to make sure it redirected to its profile
//                                        redirect to login layout
                                    }else{
//                                        if user is not registered sucessfully
                                        Toast.makeText(RegisterScreen.this, "Failed to register!Try Again", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }


                                }
                            });


                        }else {
//                            if user is not registered successfully
                            Toast.makeText(RegisterScreen.this, "Failed to register!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                        }

                    }
                });
    }
}