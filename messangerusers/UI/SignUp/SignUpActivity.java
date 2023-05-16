package com.example.messangerusers.UI.SignUp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.messangerusers.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {

    EditText userName;
    EditText userEmail;
    EditText userPassword;
    EditText userReEnterPassword;
    Button signUpButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViews();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = userPassword.getText().toString().trim();
                String reEnterPassword = userReEnterPassword.getText().toString().trim();

                if(!password.equals(reEnterPassword)) {
                    userPassword.getText().clear();
                    userReEnterPassword.getText().clear();
                    userReEnterPassword.setError("The entered passwords are not the same. Please try again");
                    return;
                }
                else if(password.length() < 8)
                {
                    userPassword.getText().clear();
                    userReEnterPassword.getText().clear();
                    userReEnterPassword.setError("Password must be at least 8 characters long");
                    return;
                }
                String name = userName.getText().toString().trim();
                String email = userEmail.getText().toString().trim();
                signUp(name,email,password);
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    private void signUp(String name, String email,String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SignUpActivity.this, "Your account was created successfully!",
                                                        Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Failed to create your account ...",
                                    Toast.LENGTH_SHORT).show();
                            Toast.makeText(SignUpActivity.this, task.getResult().toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void findViews() {
        userName = findViewById(R.id.nameSignUp);
        userEmail = findViewById(R.id.emailSignUp);
        userPassword = findViewById(R.id.passwordSignUp);
        userReEnterPassword = findViewById(R.id.reEnterpasswordSignUp);
        signUpButton = findViewById(R.id.SignUpButton);
    }
}
