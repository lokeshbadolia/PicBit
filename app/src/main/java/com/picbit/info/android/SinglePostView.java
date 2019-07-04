package com.picbit.info.android;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
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
import com.squareup.picasso.Picasso;

public class SinglePostView extends YouTubeBaseActivity {//extends ActionBarActivity implements TextToSpeech.OnInitListener {
    private String mPost_key = null;
    private DatabaseReference mDatabase;
    private ImageView mSinglePostImage;
    private TextView mSinglePostTitle;
    private FirebaseAuth mAuth;
    private FloatingActionButton mGoBack;
    private TextView mSinglePostDesc;
    private ProgressDialog mProgress;
    private FloatingActionButton mSinglePostRemoveBtn;
    private StorageReference mDeletePostImage;
    private DatabaseReference mDatabaseUserPost;
    private FirebaseUser mCurrentUser;
    private LinearLayout mSinglePostVideoLayout1;
    private YouTubePlayerView mPlayer;
    YouTubePlayer.OnInitializedListener onInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide the title bar in activity
        //getSupportActionBar().hide(); //----//
        setContentView(R.layout.activity_single_post_view);

        //setTitle("PicBit");
        //  getActionBar().setIcon(R.drawable.my_icon);
        // getSupportActionBar().setHomeButtonEnabled(true);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProgress = new ProgressDialog(this);
        mSinglePostRemoveBtn = (FloatingActionButton) findViewById(R.id.singlePostRemoveBtn);
        mSinglePostImage = (ImageView) findViewById(R.id.singlePostImage);
        mSinglePostTitle = (TextView) findViewById(R.id.singlePostTitle);
        mSinglePostDesc = (TextView) findViewById(R.id.singlePostDesc);
        //ParaRead = new TextToSpeech(this, SinglePostView.this);
        mPlayer = (YouTubePlayerView) findViewById(R.id.post_youtubeVideo);
        mSinglePostVideoLayout1 = (LinearLayout) findViewById(R.id.SinglePostVideoLayout1);
        mGoBack = (FloatingActionButton) findViewById(R.id.GoBackFloat);


        mAuth = FirebaseAuth.getInstance();
        mPost_key = getIntent().getExtras().getString("post_id");

//for main page means by new post view --------------------------------------------------------------------------------------------
        mDatabase = FirebaseDatabase.getInstance().getReference().child("UserData_Images_value");
        // mDatabaseUserPost = FirebaseDatabase.getInstance().getReference().child("Users_post").child(mCurrentUser.getUid());


        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("descreption").getValue();
                final String post_image = (String) dataSnapshot.child("image").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();
                final String post_videoUrl = (String) dataSnapshot.child("youtubelink").getValue();

                mSinglePostTitle.setText(post_title);
                mSinglePostDesc.setText(post_desc);

                Picasso.with(SinglePostView.this).load(post_image).into(mSinglePostImage);

                if (mAuth.getCurrentUser().getUid().equals(post_uid)) {
                    mSinglePostRemoveBtn.setVisibility(View.VISIBLE);
                }

                if (post_image != null) {
                    mSinglePostImage.setVisibility(View.VISIBLE);
                    mSinglePostVideoLayout1.setVisibility(View.GONE);
                } else if (post_videoUrl != null) {
                    mSinglePostImage.setVisibility(View.GONE);
                    mSinglePostVideoLayout1.setVisibility(View.VISIBLE);
                }

                //-----------------------------------------------------------------------

//-------------------------------------------------------

                //-------------------------
                onInitializedListener = new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayerMain, boolean wasRestored) {
                        youTubePlayerMain.setShowFullscreenButton(false);
                        // Toast.makeText(userdataViewHolder.this.getClass(), VIDEO + "OnSuccess", Toast.LENGTH_LONG).show();
                        if (!wasRestored) {
                            youTubePlayerMain.cueVideo(post_videoUrl);
                        } else {
                            youTubePlayerMain.play();
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                        //  Toast.makeText(getBaseContext(), "Some Error Appererd", Toast.LENGTH_LONG).show();
                    }
                };

                mPlayer.initialize(Api_Key_class.API_KEY, onInitializedListener);
//==========================
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//for main page means by new post view --------------------------------------------------------------------------------------------

        mSinglePostRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppStatus.getInstance(SinglePostView.this).isOnline()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(SinglePostView.this);
                    builder.setMessage("Do you want to delete this ?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mProgress.setMessage("Post Deletion in Process...");
                                    mProgress.setCancelable(false);
                                    mProgress.show();


                                    DeletePostImageMethod();
//---------------------------------------------------------------------------------------------------------------------
                                    //------------------
//----------------------------------------------------------------------------------------------------------------------
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.setTitle("Confirm");
                    dialog.show();


                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SinglePostView.this);
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
        });

    }


    public void DeletePostImageMethod() {
        //------------------- for Removing Stored Photo --------------------------------------
        mDatabase.child(mPost_key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot RefdataSnapshot) {
                String deleteImage = (String) RefdataSnapshot.child("image").getValue();
                if(deleteImage != null) {
                    mDeletePostImage = FirebaseStorage.getInstance().getReferenceFromUrl(deleteImage);
                    mDeletePostImage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            DeletePostMethod();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(SinglePostView.this, "Image Is Jumbled Up.. Need Few Moment!!.", Toast.LENGTH_LONG).show();
                            DeletePostMethod();
                        }
                    });
                }
                else
                {
                    DeletePostMethod();
                }
                //------------------- for Removing Stored Photo ----------------------------------------*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //--------
    }


    public void DeletePostMethod() {
        mCurrentUser = mAuth.getCurrentUser();
        //----------------------- For Removing Main Page Post ------------------------------------------
        mDatabase.child(mPost_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mProgress.dismiss();
                Toast.makeText(SinglePostView.this, "Post Successfully Deleted.", Toast.LENGTH_LONG).show();

            }
        });
        //----------------------- For Removing Main Page Post ------------------------------------------

        Intent mainSinglePostRemoveIntent = new Intent(SinglePostView.this, MainActivity.class);
        mainSinglePostRemoveIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainSinglePostRemoveIntent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
