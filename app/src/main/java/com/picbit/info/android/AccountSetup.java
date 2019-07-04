package com.picbit.info.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSetup extends AppCompatActivity {

    private EditText mNameField;
    private Uri mImageUri = null;
    private Button mSubmitBtn;
    private ProgressDialog mProgress;
    private CircleImageView mSetupImageView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private StorageReference mStorageImage;
    private static final int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide the title bar in activity
        setContentView(R.layout.activity_account_setup);


        // ----------------- Firebase Path Start--------------------------------------------------------------------------------------------
        mAuth = FirebaseAuth.getInstance();
        mStorageImage = FirebaseStorage.getInstance().getReference().child("Profile_images");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        // ----------------- Firebase Path End--------------------------------------------------------------------------------------------
        // ----------------- Typecast variable Start --------------------------------------------------------------------------------------------
        mProgress = new ProgressDialog(this);
        mSetupImageView = (CircleImageView) findViewById(R.id.setupImageView);
        mNameField = (EditText) findViewById(R.id.setupNameField);
        mSubmitBtn = (Button) findViewById(R.id.setupSubmitBtn);
        // ----------------- Typecast variable End --------------------------------------------------------------------------------------------

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSetupAccount();
            }
        });

        mSetupImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

    }

    //----------------------
    //------------------ OnCreateMethod End --------------------------------------------------------------------------------------------

    // ---------------------------------------------------------------------------------------------------------------------------------
    // ----------------- Main Function "Setuping Account" Start ------------------------------------------------------------------------
    private void startSetupAccount() {

        final String name = mNameField.getText().toString().trim();
        final String user_id = mAuth.getCurrentUser().getUid();
        final String email = mAuth.getCurrentUser().getEmail();

        if (!TextUtils.isEmpty(name) && mImageUri != null) {
            mProgress.setMessage("Setup is Finishing....");
            mProgress.setCancelable(false);
            mProgress.show();

            StorageReference filepath = mStorageImage.child(System.currentTimeMillis() + mImageUri.getLastPathSegment());
            //Compression Algorithem start ------------------------
            mSetupImageView.setDrawingCacheEnabled(true);
            mSetupImageView.buildDrawingCache();
            Bitmap bitmap = mSetupImageView.getDrawingCache();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] data = byteArrayOutputStream.toByteArray();
            UploadTask uploadTask2 = filepath.putBytes(data);
            //Compression Algorithem End ----------------------------
            //If in Future, you want to Remove Compression Algorithem .. Follow Below Order ----
            // Just Change ------------------ uploadTask2 to filepath.putFile(mImageUri) -----------------------------
            uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String downloadUri = taskSnapshot.getDownloadUrl().toString();

                    mDatabaseUsers.child(user_id).child("name").setValue(name);
                    mDatabaseUsers.child(user_id).child("image").setValue(downloadUri);
                    mDatabaseUsers.child(user_id).child("email").setValue(email);
                  /*  mDatabaseUsers.child(user_id).child("fblink").setValue("");
                    mDatabaseUsers.child(user_id).child("instalink").setValue("");
                    mDatabaseUsers.child(user_id).child("twitterlink").setValue("");
                    mDatabaseUsers.child(user_id).child("bloggerlink").setValue("");
                    mDatabaseUsers.child(user_id).child("linkedinlink").setValue("");
                    mDatabaseUsers.child(user_id).child("youtubelink").setValue("");
                    mDatabaseUsers.child(user_id).child("weblink").setValue("");
*/
                    mProgress.dismiss();

                    Intent mainIntent = new Intent(AccountSetup.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            });
        }

    }


    // ----------------- Main Function "Setuping Account" End ------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------------------------------------------------


    //----------- Opening Gallery / Selecting Image / cropping Start ------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();

                mSetupImageView.setImageURI(mImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    //----------- Opening Gallery / Selecting Image / cropping End ------------------------------------------------
}

