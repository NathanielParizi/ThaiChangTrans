package com.jtanuki.go.thaichang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextInputLayout mLoginEmail, mLoginPassword;
    private Button loginBtn;
    private ProgressDialog mLoginProgress;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

        mLoginProgress = new ProgressDialog(this);

        mToolbar = (Toolbar) findViewById(R.id.loginPageToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login To Your Account");

        mLoginEmail = (TextInputLayout) findViewById(R.id.logEmail);
        mLoginPassword = (TextInputLayout) findViewById(R.id.logPassword);
        loginBtn = (Button) findViewById(R.id.LogInButton);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mLoginEmail.getEditText().getText().toString();
                String password = mLoginPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

                mLoginProgress.setTitle("Logging In");
                    mLoginProgress.setMessage("Please wait while we log into your account");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();
                    
                    loginUser(email, password);

                }
            }
        });




    }

    //Firebase assistant #5
    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {



                        if (task.isSuccessful()) {

                            mLoginProgress.dismiss();
                            Intent mainIntent = new Intent(LogInActivity.this,MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();

                        } else {

                            mLoginProgress.hide();
                            Toast.makeText(LogInActivity.this, "Please check information and try again",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      if(item.getItemId() == android.R.id.home) {

          Intent settingsIntent = new Intent(LogInActivity.this, SettingsActivity.class);

          settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
          startActivity(settingsIntent);
          finish();
      }
        return super.onOptionsItemSelected(item);
    }
}
