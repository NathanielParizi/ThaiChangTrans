package com.jtanuki.go.thaichang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Button mUpdateProfileBtn;
    private TextInputLayout mStatus;

    private ProgressDialog mProgress;


    //Firebase

    private DatabaseReference mStatusDatabase;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        String status_value = getIntent().getStringExtra("status_value");

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = mCurrentUser.getUid();

        mProgress = new ProgressDialog(this);

        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid);

        mToolbar = (Toolbar) findViewById(R.id.statusAppBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStatus = (TextInputLayout) findViewById(R.id.status_input);
        mUpdateProfileBtn = (Button) findViewById(R.id.update_profile_btn);

        mStatus.getEditText().setText(status_value);

        mUpdateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgress = new ProgressDialog(StatusActivity.this);
                mProgress.setTitle("Updating Profile");
                mProgress.setMessage("Please wait while we update your profile.");
                mProgress.show();

                String status = mStatus.getEditText().getText().toString();
                mStatusDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            mProgress.dismiss();

                        } else {

                            mProgress.hide();
                            Toast.makeText(getApplicationContext(), "There was an error in updating your profile.", Toast.LENGTH_LONG).show();
                        }

                    }
                });


            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){

            Intent settingsIntent = new Intent(StatusActivity.this, SettingsActivity.class);
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
}
