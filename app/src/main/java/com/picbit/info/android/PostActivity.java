
package com.picbit.info.android;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;


public class PostActivity extends AppCompatActivity {//YouTubeBaseActivity {// YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener { //AppCompatActivity

    private ImageView mSelectimage1;
    private ImageView mSelectimage2;
    private ImageView mSelectimage3;
    private ImageView mSelectimage4;
    private ImageView mSelectimage5;

    private RadioGroup radioGroup;
    private RadioButton mRadioVideo;
    private RadioButton mRadioImage;
    private EditText mPostTitle;
    private EditText mPostYoutubeLink;
    private TextView mPostYoutubeLinkHint;
    private TextView mPostImageLinkHint;
    private EditText mPostDesc;
    private Uri mImageUri = null;
    private ImageView mPlayVideo;
    private Uri mImageUri2 = null;
    private LinearLayout mVideoLinkGrid;
    private LinearLayout mImagePickGrid;
    private ProgressDialog mProgress;
    private CircleImageView mPhoto;
    private FirebaseAuth mAuth;
    private YouTubePlayer mPlayer;
    private YouTubePlayerSupportFragment fragmentPlayer;
    private String youtubeLink_val = null;
    private String mFinalCategoryname;
    private FirebaseUser mCurrentUser;
    // LinearLayout screen;
    private DatabaseReference mDatabaseUsers;
    //private DatabaseReference mDatabaseCategory;
    private DatabaseReference mSampleDatabase;
    private DatabaseReference mDatabaseUserPost;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    public String VIDEO = null;
    private DatabaseReference mDatabase;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private static final int GALLERY_REQUEST = 1;
    private InterstitialAd mInterstitialAd;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide the title bar in activity

        setContentView(R.layout.activity_post);
        // Toast.makeText(this, "You are online!!!!", Toast.LENGTH_LONG).show();
        setTitle("PicBit");
        //getSupportActionBar().setIcon(R.drawable.my_icon);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRadioVideo = (RadioButton) findViewById(R.id.VideoCheckBtn);
        mRadioImage = (RadioButton) findViewById(R.id.imageCheckBtn);
        // youTubePlayerView = (YouTubePlayerView) findViewById(R.id.videoPlay);
        fragmentPlayer = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.videoPlay);
        mPostYoutubeLink = (EditText) findViewById(R.id.youtube_link);
        mPostYoutubeLinkHint = (TextView) findViewById(R.id.youtube_link_text);
        mPostImageLinkHint = (TextView) findViewById(R.id.imageView_link_text);
        mImagePickGrid = (LinearLayout) findViewById(R.id.imagePickGrid);

        mVideoLinkGrid = (LinearLayout) findViewById(R.id.VideoUrlBoxLayout);
        mPlayVideo = (ImageView) findViewById(R.id.playVideo);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.clearCheck();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mRadioVideo.isChecked()) {
                    mImagePickGrid.setVisibility(View.GONE);
                    mPostImageLinkHint.setVisibility(View.GONE);
                    mVideoLinkGrid.setVisibility(View.VISIBLE);
                    // Toast.makeText(getBaseContext(), "Video", Toast.LENGTH_LONG).show();
                    return;
                } else if (mRadioImage.isChecked()) {
                    mPlayer = null;
                    mImagePickGrid.setVisibility(View.VISIBLE);
                    mVideoLinkGrid.setVisibility(View.GONE);
                    mPostImageLinkHint.setVisibility(View.VISIBLE);
                    // Toast.makeText(getBaseContext(), "Image", Toast.LENGTH_LONG).show();
                    return;

                }
            }
        });


        //youTubePlayerView.initialize(API_KEY, this);


        mPostYoutubeLink.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mPostYoutubeLinkHint.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(mPostYoutubeLink.getText())) {
                        Toast.makeText(PostActivity.this, "Video code Field is blank", Toast.LENGTH_LONG).show();
                        return;
                    } else if (!TextUtils.isEmpty(mPostYoutubeLink.getText())) {
                        Toast.makeText(getBaseContext(), VIDEO, Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    mPostYoutubeLinkHint.setVisibility(View.GONE);
                    if (TextUtils.isEmpty(mPostYoutubeLink.getText())) {
                        Toast.makeText(PostActivity.this, "Video code Field is blank [Lost Focus]", Toast.LENGTH_LONG).show();
                        return;
                    } else if (!TextUtils.isEmpty(mPostYoutubeLink.getText())) {
                        Toast.makeText(getBaseContext(), VIDEO + " [Lost Focus]", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        });

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
                mPlayer = youTubePlayer;
                //System controls will appear automatically
                //mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
                mPlayer.setShowFullscreenButton(false);
                VIDEO = mPostYoutubeLink.getText().toString().trim();
                Toast.makeText(getBaseContext(), VIDEO + "OnSuccess", Toast.LENGTH_LONG).show();

                if (!wasRestored) {
                    mPlayer.loadVideo(String.valueOf(VIDEO));
                } else {
                    mPlayer.play();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(getBaseContext(), "Some Error Appererd", Toast.LENGTH_LONG).show();
                mPlayer = null;
            }
        };


        mPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
                    builder.setTitle("Please Read!!");
                    builder.setMessage("Youtube Player Require v5.0[Lollipop] or above to Run But you can easily post youtube video without playing them.").setCancelable(false);
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                }
                            });
                    builder.show();

                } else { */
                Toast.makeText(getBaseContext(), "Youtube Video's can only be played on v5.0[Lollipop] or above", Toast.LENGTH_LONG).show();
                VIDEO = mPostYoutubeLink.getText().toString().trim();
                Toast.makeText(getBaseContext(), VIDEO + "OnClick", Toast.LENGTH_LONG).show();
                fragmentPlayer.initialize(Api_Key_class.API_KEY, onInitializedListener);
                //  }
            }
        });


        //To Show Current USer image in circle Image View in post Activiy--------------------------
        //----------------------------------------------------------------------------------------------------------------------------------------
        //SampleDatabase is only createad because databaseUsers is already been used in this page, and to avoid overwrite that database..  i created a sample database.

        mPhoto = (CircleImageView) findViewById(R.id.currentUserPhoto);
        mSampleDatabase = FirebaseDatabase.getInstance().getReference();
        mSampleDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String pImage = (String) dataSnapshot.child("Users").child(mCurrentUser.getUid()).child("image").getValue();
                Picasso.with(PostActivity.this).load(pImage).into(mPhoto);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //------------------------------------------------------------------------------------------------------------------------------------------

        mFinalCategoryname = getIntent().getExtras().getString("category_name_final");
        TextView getCategory = (TextView) findViewById(R.id.getCategoryName);
        getCategory.setText(mFinalCategoryname);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        //For Storage
        mStorage = FirebaseStorage.getInstance().getReference();
        //For database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("UserData_Images_value");
        mDatabaseUserPost = FirebaseDatabase.getInstance().getReference().child("Users_post").child(mCurrentUser.getUid());

        mSelectimage1 = (ImageView) findViewById(R.id.imageSelect1);

        mPostTitle = (EditText) findViewById(R.id.titleField);
        mPostDesc = (EditText) findViewById(R.id.descField);
        FloatingActionButton FlotingSubmitBtn = (FloatingActionButton) findViewById(R.id.submitBtn);

        mProgress = new ProgressDialog(this);

        mSelectimage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mPostYoutubeLink.getText())) {
                    Toast.makeText(PostActivity.this, "Video Url is not Blank!!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, GALLERY_REQUEST);
                }
            }
        });

        FlotingSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });
    }

    private void startPosting() {

        if (AppStatus.getInstance(this).isOnline()) {
            mProgress.setMessage("Posting in Process...");
            final String title_val = mPostTitle.getText().toString().trim();
            final String desc_val = mPostDesc.getText().toString().trim();
            final String youtubeLink_val = mPostYoutubeLink.getText().toString().trim();

            if (mRadioVideo.isChecked()) {
                if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && !TextUtils.isEmpty(youtubeLink_val)) { //mImageUri != null) {
                    mProgress.setCancelable(false);
                    mProgress.show();
//-----------------for arranging new post wise post
                    final DatabaseReference newPost = mDatabase.push();

                    mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //For Time/Data of the Current Post --------------------------------------------------
                            Calendar calendar = Calendar.getInstance();
                            TimeZone tz = TimeZone.getDefault();
                            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
                            java.util.Date currenTimeZone = new java.util.Date((long) System.currentTimeMillis());
                            //End here ----------------------------------------------------------------------------
                            //-------------------------------------------------------------------------------------
                            newPost.child("time").setValue((sdf.format(currenTimeZone)).toString());
                            newPost.child("title").setValue(title_val);
                            newPost.child("descreption").setValue(desc_val);
                            newPost.child("category").setValue(mFinalCategoryname.toString());
                            newPost.child("profileimage").setValue(dataSnapshot.child("image").getValue());
                            newPost.child("username").setValue(dataSnapshot.child("name").getValue());
                            newPost.child("uid").setValue(mCurrentUser.getUid());
                            newPost.child("youtubelink").setValue(youtubeLink_val).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(PostActivity.this, MainActivity.class));

                    } else {
                        Toast.makeText(PostActivity.this, "Posting Failed.. Due to Unexpected Reason!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mProgress.dismiss();


    } else if (TextUtils.isEmpty(title_val) || TextUtils.isEmpty(desc_val) || TextUtils.isEmpty(youtubeLink_val)) {
        Toast.makeText(this, "Please Fill all the Blanks", Toast.LENGTH_LONG).show();
    }

} else if (mRadioImage.isChecked()) {

                if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mImageUri != null) {
                    mProgress.setCancelable(false);
                    mProgress.show();
                   //For Storage
                    StorageReference filepath = mStorage.child("UserData_Images").child(System.currentTimeMillis() + mImageUri.getLastPathSegment());
                    // StorageReference filepath2 = mStorage.child("UserData_Images").child(System.currentTimeMillis() + mImageUri2.getLastPathSegment());
                    //Compression Algorithem start ------------------------
                    mSelectimage1.setDrawingCacheEnabled(true);
                    mSelectimage1.buildDrawingCache();
                    Bitmap bitmap1 = mSelectimage1.getDrawingCache();
                    ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
                    bitmap1.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream1);
                    byte[] data1 = byteArrayOutputStream1.toByteArray();
                    UploadTask uploadTask1 = filepath.putBytes(data1);

                    //Compression Algorithem End ----------------------------
                    //If in Future, you want to Remove Compression Algorithem .. Follow Below Order ----
                    // Just Change ------------------ uploadTask2 to filepath.putFile(mImageUri) -----------------------------
                    uploadTask1.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final Uri downloadUrl = taskSnapshot.getDownloadUrl();

//-----------------for arranging new post wise post
                            final DatabaseReference newPost = mDatabase.push();
                            final String CopyKeyParent = newPost.getKey();
                            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //For Time/Data of the Current Post --------------------------------------------------
                                    Calendar calendar = Calendar.getInstance();
                                    TimeZone tz = TimeZone.getDefault();
                                    calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
                                    java.util.Date currenTimeZone = new java.util.Date((long) System.currentTimeMillis());
                                    //   Toast.makeText(PostActivity.this, sdf.format(currenTimeZone), Toast.LENGTH_SHORT).show();
                                    //End here ----------------------------------------------------------------------------
                                   //-------------------------------------------------------------------------------------
                                    newPost.child("time").setValue((sdf.format(currenTimeZone)).toString());
                                    newPost.child("title").setValue(title_val);
                                    newPost.child("descreption").setValue(desc_val);
                                    newPost.child("category").setValue(mFinalCategoryname.toString());
                                    newPost.child("profileimage").setValue(dataSnapshot.child("image").getValue());
                                    newPost.child("username").setValue(dataSnapshot.child("name").getValue());
                                    newPost.child("uid").setValue(mCurrentUser.getUid());
                                    newPost.child("image").setValue(downloadUrl.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                startActivity(new Intent(PostActivity.this, MainActivity.class));

                                            } else {
                                                Toast.makeText(PostActivity.this, "Posting Failed.. Due to Unexpected Reason!!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            mProgress.dismiss();
                        }
                    });

                } else if (TextUtils.isEmpty(title_val) || TextUtils.isEmpty(desc_val) || mImageUri == null) {
                    Toast.makeText(this, "Please Fill all the Blanks/Images", Toast.LENGTH_LONG).show();
                }
            } else if (!mRadioImage.isChecked() && !mRadioVideo.isChecked()) {
                Toast.makeText(this, "Please Select one Category from Video/Image", Toast.LENGTH_LONG).show();
            }

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
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

    //-----------------for arranging new post wise post


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(16, 9)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                mSelectimage1.setImageURI(mImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    //@Override
    // public boolean onSupportNavigateUp() {
    //     finish();
    //     return true;
    // }
}
