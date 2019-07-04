package com.picbit.info.android;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mRegisterBtn;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide the title bar in activity
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mProgress = new ProgressDialog(this);
        //mNameField = (EditText) findViewById(R.id.nameField);
        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);
        mRegisterBtn = (Button) findViewById(R.id.registerBtn);

      /*  TextView logo = (TextView)findViewById(R.id.logo_text2);
        Typeface typeFace = Typeface.createFromAsset(getAssets(),"fonts/CoveredByYourGrace.ttf");
        logo.setTypeface(typeFace);
*/
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();

            }
        });
    }

    public void openTermsPage2(View v){
        Toast.makeText(RegisterActivity.this,"Under Development",Toast.LENGTH_SHORT).show();
        /*Uri terms = Uri.parse("https://picbitprivacy.wordpress.com/home/terms-and-condition/"); // missing 'http://' will cause crashed
        Intent termsCondition = new Intent(Intent.ACTION_VIEW, terms);
        startActivity(termsCondition);
    */}

    public void openloginpage(View v) {
        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);

    }

    public void openloginpage2(View view) {
        Intent loginIntent2 = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent2);

    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void startRegister() {

        final String email = mEmailField.getText().toString().trim();
        final String password = mPasswordField.getText().toString().trim();

        if (/*!TextUtils.isEmpty(name) && */!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            if (!isValidEmail(email)) {
                // mEmailField.requestFocus();
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Invalid Email!!");
                builder.setMessage("Please Enter a Correct Email.").setCancelable(false);
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
            } else {
                mProgress.setMessage("Signing Up....");
                mProgress.show();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            String user_id = mAuth.getCurrentUser().getUid();

                            DatabaseReference current_user_db = mDatabase.child(user_id);
                            sendVerificationEmail();

                            mProgress.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setTitle("Verify Email!!");
                            builder.setMessage("Please Verify your identity By clicking the Link in mail.").setCancelable(false);
                            builder.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            Intent mainIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(mainIntent);
                                        }
                                    });
                            builder.show();
                        }
                    }
                });
            }
        }
    }


    public void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Signup successfull!! Verification email sent", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
}
