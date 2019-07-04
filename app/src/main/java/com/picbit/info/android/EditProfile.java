package com.picbit.info.android;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
    private CircleImageView mEditProfileImageView;
    private EditText mEditProfileNameField;
    private Button mEditProfileSubmitBtn;

    private ProgressDialog mProgress;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseCmnt;
    private DatabaseReference mDatabaseUserPost;
    private DatabaseReference mDatabaseUsers;
    private Uri mImageUri = null;
    private FirebaseAuth mAuth;
    private StorageReference mDeleteProfileImage;
    private StorageReference mStorageImage;
    private static final int GALLERY_REQUEST = 1;

    private EditText FbLink;
    private ImageView FbImageInsert;
    private EditText InstaLink;
    private ImageView InstaImageInsert;
    private EditText TwitterLink;
    private ImageView TwitterImageInsert;
    private EditText BloggerLink;
    private ImageView BloggerImageInsert;
    private EditText LinkedinLink;
    private ImageView LinkedinImageInsert;
    private EditText YoutubeLink;
    private ImageView YoutubeImageInsert;
    private EditText WebLink;
    private ImageView WebImageInsert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //------- Auth/Storage/Database Location access Start ----------------------------------------------------------------------
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("UserData_Images_value");
       // mDatabaseCmnt = FirebaseDatabase.getInstance().getReference().child("UserData_Images_value").orderByChild("uid").equalTo(mCurrentUser.getUid()).getRef().child("All_Comments");
        mStorageImage = FirebaseStorage.getInstance().getReference().child("Profile_images");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUserPost = FirebaseDatabase.getInstance().getReference().child("Users_post").child(mCurrentUser.getUid());
        //------- Auth/Storage/Database Location access End -------------------------------------------------------------------------

        //------- Type Casting Object Start -----------------------------------------------------------------------------------------
        mProgress = new ProgressDialog(this);
        mEditProfileImageView = (CircleImageView) findViewById(R.id.EditProfilePhoto);
        mEditProfileNameField = (EditText) findViewById(R.id.EditProfileName);
        mEditProfileSubmitBtn = (Button) findViewById(R.id.EditProfileSubmit);
        FbLink = (EditText) findViewById(R.id.fbLink);
        FbImageInsert = (ImageView) findViewById(R.id.fbImageInsert);
        InstaLink = (EditText) findViewById(R.id.instaLink);
        InstaImageInsert = (ImageView) findViewById(R.id.instaImageInsert);
        TwitterLink = (EditText) findViewById(R.id.twitterLink);
        TwitterImageInsert = (ImageView) findViewById(R.id.twitterImageInsert);
        BloggerLink = (EditText) findViewById(R.id.bloggerLink);
        BloggerImageInsert = (ImageView) findViewById(R.id.bloggerImageInsert);
        LinkedinLink = (EditText) findViewById(R.id.linkedinLink);
        LinkedinImageInsert = (ImageView) findViewById(R.id.linkedinImageInsert);
        YoutubeLink = (EditText) findViewById(R.id.youtubeLink);
        YoutubeImageInsert = (ImageView) findViewById(R.id.youtubeImageInsert);
        WebLink = (EditText) findViewById(R.id.webLink);
        WebImageInsert = (ImageView) findViewById(R.id.webImageInsert);
        //------- Type Casting Object End --------------------------------------------------------------------------------------------

        //------- Calling Updation Method Start --------------------------------------------------------------------------------------
        mEditProfileSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditProfile();
            }
        });
        //------- Calling Updation Method End -----------------------------------------------------------------------------------------

        //------- Opening Gallery to select image Start -------------------------------------------------------------------------------
        mEditProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
        //------- Opening Gallery to select image End -------------------------------------------------------------------------------

        FbLink.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (TextUtils.isEmpty(FbLink.getText())) {
                        FbImageInsert.setImageResource(R.drawable.facebook);
                        return;
                    } else if (!TextUtils.isEmpty(FbLink.getText())) {
                        FbImageInsert.setImageResource(R.drawable.fillfacebook);

                        return;
                    }
                } else {
                    if (TextUtils.isEmpty(FbLink.getText())) {
                        FbImageInsert.setImageResource(R.drawable.facebook);
                        return;
                    } else if (!TextUtils.isEmpty(FbLink.getText())) {
                        FbImageInsert.setImageResource(R.drawable.fillfacebook);
                        return;
                    }
                }
            }
        });

        InstaLink.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (TextUtils.isEmpty(InstaLink.getText())) {
                        InstaImageInsert.setImageResource(R.drawable.instagram);
                        return;
                    } else if (!TextUtils.isEmpty(InstaLink.getText())) {
                        InstaImageInsert.setImageResource(R.drawable.fillinstagram);
                        return;
                    }
                } else {
                    if (TextUtils.isEmpty(InstaLink.getText())) {
                        InstaImageInsert.setImageResource(R.drawable.instagram);
                        return;
                    } else if (!TextUtils.isEmpty(InstaLink.getText())) {
                        InstaImageInsert.setImageResource(R.drawable.fillinstagram);
                        return;
                    }
                }
            }
        });

        TwitterLink.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (TextUtils.isEmpty(TwitterLink.getText())) {
                        TwitterImageInsert.setImageResource(R.drawable.twitter);
                        return;
                    } else if (!TextUtils.isEmpty(TwitterLink.getText())) {
                        TwitterImageInsert.setImageResource(R.drawable.filltwitter);
                        return;
                    }
                } else {
                    if (TextUtils.isEmpty(TwitterLink.getText())) {
                        TwitterImageInsert.setImageResource(R.drawable.twitter);
                        return;
                    } else if (!TextUtils.isEmpty(TwitterLink.getText())) {
                        TwitterImageInsert.setImageResource(R.drawable.filltwitter);
                        return;
                    }
                }
            }
        });

        BloggerLink.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (TextUtils.isEmpty(BloggerLink.getText())) {
                        BloggerImageInsert.setImageResource(R.drawable.blogger);
                        return;
                    } else if (!TextUtils.isEmpty(BloggerLink.getText())) {
                        BloggerImageInsert.setImageResource(R.drawable.fillblogger);
                        return;
                    }
                } else {
                    if (TextUtils.isEmpty(BloggerLink.getText())) {
                        BloggerImageInsert.setImageResource(R.drawable.blogger);
                        return;
                    } else if (!TextUtils.isEmpty(BloggerLink.getText())) {
                        BloggerImageInsert.setImageResource(R.drawable.fillblogger);
                        return;
                    }
                }
            }
        });

        LinkedinLink.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (TextUtils.isEmpty(LinkedinLink.getText())) {
                        LinkedinImageInsert.setImageResource(R.drawable.linkedin);
                        return;
                    } else if (!TextUtils.isEmpty(LinkedinLink.getText())) {
                        LinkedinImageInsert.setImageResource(R.drawable.filllinkedin);
                        return;
                    }
                } else {
                    if (TextUtils.isEmpty(LinkedinLink.getText())) {
                        LinkedinImageInsert.setImageResource(R.drawable.linkedin);
                        return;
                    } else if (!TextUtils.isEmpty(LinkedinLink.getText())) {
                        LinkedinImageInsert.setImageResource(R.drawable.filllinkedin);
                        return;
                    }
                }
            }
        });

        YoutubeLink.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (TextUtils.isEmpty(YoutubeLink.getText())) {
                        YoutubeImageInsert.setImageResource(R.drawable.youtube);
                        return;
                    } else if (!TextUtils.isEmpty(YoutubeLink.getText())) {
                        YoutubeImageInsert.setImageResource(R.drawable.fillyoutube);
                        return;
                    }
                } else {
                    if (TextUtils.isEmpty(YoutubeLink.getText())) {
                        YoutubeImageInsert.setImageResource(R.drawable.youtube);
                        return;
                    } else if (!TextUtils.isEmpty(YoutubeLink.getText())) {
                        YoutubeImageInsert.setImageResource(R.drawable.fillyoutube);
                        return;
                    }
                }
            }
        });


        WebLink.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (TextUtils.isEmpty(WebLink.getText())) {
                        WebImageInsert.setImageResource(R.drawable.web);
                        return;
                    } else if (!TextUtils.isEmpty(WebLink.getText())) {
                        WebImageInsert.setImageResource(R.drawable.fillweb);
                        return;
                    }
                } else {
                    if (TextUtils.isEmpty(WebLink.getText())) {
                        WebImageInsert.setImageResource(R.drawable.web);
                        return;
                    } else if (!TextUtils.isEmpty(WebLink.getText())) {
                        WebImageInsert.setImageResource(R.drawable.fillweb);
                        return;
                    }
                }
            }
        });
    }



    //---------------------------------------------------------------------------------------------------------------
    //----------- Updation Goes here Regarding Every Entry ----------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------
    private void startEditProfile() {

        if (AppStatus.getInstance(this).isOnline()) {

            final String name = mEditProfileNameField.getText().toString().trim();
            final String fblink = FbLink.getText().toString().trim();
            final String instalink = InstaLink.getText().toString().trim();
            final String twitterlink = TwitterLink.getText().toString().trim();
            final String bloggerlink = BloggerLink.getText().toString().trim();
            final String linkedinlink = LinkedinLink.getText().toString().trim();
            final String youtubelink = YoutubeLink.getText().toString().trim();
            final String weblink = WebLink.getText().toString().trim();
            final String user_id = mAuth.getCurrentUser().getUid();

            if (mImageUri != null) {
                mProgress.setMessage("Updating Your Profile!! Please Wait...");
                mProgress.setCancelable(false);
                mProgress.show();

                StorageReference filepath = mStorageImage.child(System.currentTimeMillis() + mImageUri.getLastPathSegment());
                //Compression Algorithem start ------------------------
                mEditProfileImageView.setDrawingCacheEnabled(true);
                mEditProfileImageView.buildDrawingCache();
                Bitmap bitmap = mEditProfileImageView.getDrawingCache();
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

                        final String downloadUri = taskSnapshot.getDownloadUrl().toString();
                        DeletePreviousProfile();


                        mDatabaseUsers.child(user_id).child("name").setValue(name);
                        mDatabaseUsers.child(user_id).child("image").setValue(downloadUri);
                        mDatabaseUsers.child(user_id).child("fblink").setValue(fblink);
                        mDatabaseUsers.child(user_id).child("instalink").setValue(instalink);
                        mDatabaseUsers.child(user_id).child("twitterlink").setValue(twitterlink);
                        mDatabaseUsers.child(user_id).child("bloggerlink").setValue(bloggerlink);
                        mDatabaseUsers.child(user_id).child("linkedinlink").setValue(linkedinlink);
                        mDatabaseUsers.child(user_id).child("youtubelink").setValue(youtubelink);
                        mDatabaseUsers.child(user_id).child("weblink").setValue(weblink);

                        mDatabaseUserPost.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String key = snapshot.getKey();
                                    Map newUserData = new HashMap();
                                    newUserData.put("username", name);
                                    newUserData.put("profileimage", downloadUri);
                                    mDatabaseUserPost.child(key).updateChildren(newUserData);
                                    //Toast.makeText(EditProfile.this, key, Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Logger.error(TAG, ">>> Error:" + "find onCancelled:" + databaseError);
                            }
                        });

           /*             mDatabaseCmnt.orderByChild("random_uid").equalTo(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String key = snapshot.getKey();
                                    Map newPublicData = new HashMap();
                                    newPublicData.put("username", name);
                                    newPublicData.put("profileimage", downloadUri);
                                   // mDatabaseCmnt.child(key).updateChildren(newPublicData);
                                    //Toast.makeText(EditProfile.this, key, Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Logger.error(TAG, ">>> Error:" + "find onCancelled:" + databaseError);
                            }
                        });
*/
                        mDatabase.orderByChild("uid").equalTo(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String key = snapshot.getKey();
                                    Map newPublicData = new HashMap();
                                    newPublicData.put("username", name);
                                    newPublicData.put("profileimage", downloadUri);
                                    mDatabase.child(key).updateChildren(newPublicData);
                                    //Toast.makeText(EditProfile.this, key, Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Logger.error(TAG, ">>> Error:" + "find onCancelled:" + databaseError);
                            }
                        });

                        mProgress.dismiss();
                        finish();
                        Intent mainIntent = new Intent(EditProfile.this, ProfilePage.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }
                });

            }

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
            builder.setTitle("Network Unavilable!!");
            builder.setMessage("Click 'Go Back' To Return to Previous Page!!").setCancelable(false);
            builder.setPositiveButton("Go Back",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            finish();
                        }
                    });
            builder.show();

        }
    }

    private void DeletePreviousProfile() {
        //------------------- for Removing Stored Photo --------------------------------------
        mDatabaseUsers.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot RefdataSnapshot) {
                String deleteImage = (String) RefdataSnapshot.child("image").getValue();
                mDeleteProfileImage = FirebaseStorage.getInstance().getReferenceFromUrl(deleteImage);
                mDeleteProfileImage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditProfile.this, "Previous User Profile Delete is a Success!!", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(EditProfile.this, "Previous User Profile is not Deleted from Server.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //------------------- for Removing Stored Photo ----------------------------------------
    }

    //---------------------------------------------------------------------------------------------------------------
    //----------- Updation End here Regarding Every Entry -----------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------

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
                mEditProfileImageView.setImageURI(mImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    //----------- Opening Gallery / Selecting Image / cropping End ---------------------------------------------------

    //-------------- Back Button Action Bar Code Start -----------------------------------------------------------------------------------------
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    //-------------- Back Button Action Bar Code End -------------------------------------------------------------------------------------------

}
