package com.picbit.info.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePage extends AppCompatActivity {


    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseChildByBase;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private CircleImageView photo;
    //  private TextView email;
    private LinearLayoutManager mLayoutManager;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private TextView name;

    public String mCurrentUserUidEmail;
    public String mCurrentUserUid;

    private RecyclerView mDataList2;
    private DatabaseReference mDatabaseUserPost;
   // private String RecFbLink, RecInstaLink, RecTwitterLink, RecBloggerLink, RecLinkedInLink, RecYoutubeLink, RecWebLink;

   /* private ImageView profileFbLink;
    private ImageView profileInstaLink;
    private ImageView profileTwitterLink;
    private ImageView profileBloggerLink;
    private ImageView profileLinkedInLink;
    private ImageView profileYoutubeLink;
    private ImageView profileWebLink; */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("PicBit");
        //  getActionBar().setIcon(R.drawable.my_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseChildByBase = FirebaseDatabase.getInstance().getReference().child("UserData_Images_value");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference();
        mDatabaseUserPost = FirebaseDatabase.getInstance().getReference().child("Users_post").child(mCurrentUser.getUid());

        name = (TextView) findViewById(R.id.navName);
        //email = (TextView) findViewById(R.id.navEmail);
        photo = (CircleImageView) findViewById(R.id.navProfileImage);

      /*  profileFbLink = (ImageView) findViewById(R.id.profileFbLink);
        profileInstaLink = (ImageView) findViewById(R.id.profileInstaLink);
        profileTwitterLink = (ImageView) findViewById(R.id.profileTwitterLink);
        profileBloggerLink = (ImageView) findViewById(R.id.profileBloggerLink);
        profileLinkedInLink = (ImageView) findViewById(R.id.profileLinkedInLink);
        profileYoutubeLink = (ImageView) findViewById(R.id.profileYoutubeLink);
        profileWebLink = (ImageView) findViewById(R.id.profileWebLink);
*/

        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String mName = dataSnapshot.child("Users").child(mCurrentUser.getUid()).child("name").getValue(String.class);
                name.setText(mName);
                String pImage = (String) dataSnapshot.child("Users").child(mCurrentUser.getUid()).child("image").getValue();
                Picasso.with(ProfilePage.this).load(pImage).into(photo);

                //if(RecFbLink.isEmpty()||RecInstaLink.isEmpty()||RecTwitterLink.isEmpty()||RecBloggerLink.isEmpty()||RecLinkedInLink.isEmpty()||RecYoutubeLink.isEmpty()||RecWebLink.isEmpty()) {

           /*     String mFbLink = dataSnapshot.child("Users").child(mCurrentUser.getUid()).child("fblink").getValue(String.class);
                RecFbLink = mFbLink.toString();
                String mInstaLink = dataSnapshot.child("Users").child(mCurrentUser.getUid()).child("instalink").getValue(String.class);
                RecInstaLink = mInstaLink.toString();
                String mTwitterLink = dataSnapshot.child("Users").child(mCurrentUser.getUid()).child("twitterlink").getValue(String.class);
                RecTwitterLink = mTwitterLink.toString();
                String mBloggerLink = dataSnapshot.child("Users").child(mCurrentUser.getUid()).child("bloggerlink").getValue(String.class);
                RecBloggerLink = mBloggerLink.toString();
                String mLinkedinLink = dataSnapshot.child("Users").child(mCurrentUser.getUid()).child("linkedinlink").getValue(String.class);
                RecLinkedInLink = mLinkedinLink.toString();
                String mYoutubeLink = dataSnapshot.child("Users").child(mCurrentUser.getUid()).child("youtubelink").getValue(String.class);
                RecYoutubeLink = mYoutubeLink.toString();
                String mWebLink = dataSnapshot.child("Users").child(mCurrentUser.getUid()).child("weblink").getValue(String.class);
                RecWebLink = mWebLink.toString();

                if (RecFbLink.isEmpty()) {
                    profileFbLink.setImageResource(R.drawable.facebook);
                    profileFbLink.setClickable(false);
                } else {
                    profileFbLink.setImageResource(R.drawable.fillfacebook);
                    profileFbLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Pattern fbValid = Patterns.WEB_URL;
                            Matcher fbTooValid = fbValid.matcher(RecFbLink);
                            if (fbTooValid.matches()) {
                                Uri openFbLink = Uri.parse(RecFbLink); // missing 'http://' will cause crashed
                                Intent OpenFbLink = new Intent(Intent.ACTION_VIEW, openFbLink);
                                startActivity(OpenFbLink);
                            } else {
                                Toast.makeText(ProfilePage.this, "Facebook Link is Broken.. Please fill it again!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                if (RecInstaLink.isEmpty()) {
                    profileInstaLink.setImageResource(R.drawable.instagram);
                    profileInstaLink.setClickable(false);
                } else {
                    profileInstaLink.setImageResource(R.drawable.fillinstagram);
                    profileInstaLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Pattern instaValid = Patterns.WEB_URL;
                            Matcher instaTooValid = instaValid.matcher(RecInstaLink);
                            if (instaTooValid.matches()) {
                                Uri openInstaLink = Uri.parse(RecInstaLink); // missing 'http://' will cause crashed
                                Intent OpenInstaLink = new Intent(Intent.ACTION_VIEW, openInstaLink);
                                startActivity(OpenInstaLink);
                            } else {
                                Toast.makeText(ProfilePage.this, "Instagram Link is Broken.. Please fill it again!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                if (RecTwitterLink.isEmpty()) {
                    profileTwitterLink.setImageResource(R.drawable.twitter);
                    profileTwitterLink.setClickable(false);
                } else {
                    profileTwitterLink.setImageResource(R.drawable.filltwitter);
                    profileTwitterLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Pattern twitterValid = Patterns.WEB_URL;
                            Matcher twitterTooValid = twitterValid.matcher(RecTwitterLink);
                            if (twitterTooValid.matches()) {
                                Uri openTwitterLink = Uri.parse(RecTwitterLink); // missing 'http://' will cause crashed
                                Intent OpenTwitterLink = new Intent(Intent.ACTION_VIEW, openTwitterLink);
                                startActivity(OpenTwitterLink);
                            } else {
                                Toast.makeText(ProfilePage.this, "Twitter Link is Broken.. Please fill it again!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                if (RecBloggerLink.isEmpty()) {
                    profileBloggerLink.setImageResource(R.drawable.blogger);
                    profileBloggerLink.setClickable(false);
                } else {
                    profileBloggerLink.setImageResource(R.drawable.fillblogger);
                    profileBloggerLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Pattern bloggerValid = Patterns.WEB_URL;
                            Matcher bloggerTooValid = bloggerValid.matcher(RecBloggerLink);
                            if (bloggerTooValid.matches()) {
                                Uri openBloggerLink = Uri.parse(RecBloggerLink); // missing 'http://' will cause crashed
                                Intent OpenBloggerLink = new Intent(Intent.ACTION_VIEW, openBloggerLink);
                                startActivity(OpenBloggerLink);
                            } else {
                                Toast.makeText(ProfilePage.this, "Blogger Link is Broken.. Please fill it again!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                if (RecLinkedInLink.isEmpty() == true) {
                    profileLinkedInLink.setImageResource(R.drawable.linkedin);
                    profileLinkedInLink.setClickable(false);
                } else {
                    profileLinkedInLink.setImageResource(R.drawable.filllinkedin);
                    profileLinkedInLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Pattern linkedinValid = Patterns.WEB_URL;
                            Matcher LinkedInTooValid = linkedinValid.matcher(RecLinkedInLink);
                            if (LinkedInTooValid.matches()) {
                                Uri openLinkedInLink = Uri.parse(RecLinkedInLink); // missing 'http://' will cause crashed
                                Intent OpenLinkedInLink = new Intent(Intent.ACTION_VIEW, openLinkedInLink);
                                startActivity(OpenLinkedInLink);
                            } else {
                                Toast.makeText(ProfilePage.this, "LinkedIn Link is Broken.. Please fill it again!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                if (RecYoutubeLink.isEmpty()) {
                    profileYoutubeLink.setImageResource(R.drawable.youtube);
                    profileYoutubeLink.setClickable(false);
                } else {
                    profileYoutubeLink.setImageResource(R.drawable.fillyoutube);
                    profileYoutubeLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Pattern youtubeValid = Patterns.WEB_URL;
                            Matcher YoutubeTooValid = youtubeValid.matcher(RecYoutubeLink);
                            if (YoutubeTooValid.matches()) {
                                Uri openYoutubeLink = Uri.parse(RecYoutubeLink); // missing 'http://' will cause crashed
                                Intent OpenYoutubeLink = new Intent(Intent.ACTION_VIEW, openYoutubeLink);
                                startActivity(OpenYoutubeLink);
                            } else {
                                Toast.makeText(ProfilePage.this, "Youtube Link is Broken.. Please fill it again!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                if (RecWebLink.isEmpty()) {
                    profileWebLink.setImageResource(R.drawable.web);
                    profileYoutubeLink.setClickable(false);
                } else {
                    profileWebLink.setImageResource(R.drawable.fillweb);
                    profileWebLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Pattern webValid = Patterns.WEB_URL;
                            Matcher WebTooValid = webValid.matcher(RecWebLink);
                            if (WebTooValid.matches()) {
                                Uri openWebLink = Uri.parse(RecWebLink); // missing 'http://' will cause crashed
                                Intent OpenWebLink = new Intent(Intent.ACTION_VIEW, openWebLink);
                                startActivity(OpenWebLink);
                            } else {
                                Toast.makeText(ProfilePage.this, "Website Link is Broken.. Please fill it again!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
            //  */}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDataList2 = (RecyclerView) findViewById(R.id.Data_list2);
        // mDataList2.setHasFixedSize(true);
        mDataList2.setLayoutManager(new LinearLayoutManager(this));

       /* mDataList2.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
*/

      /*  FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setDataToView(user);
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    setDataToView(user);

                    String email = user.getEmail();

                } else {
                    //user auth state is not existed or closed, return to Login activity
                    startActivity(new Intent(ProfilePage.this, LoginActivity.class));
                    finish();
                }

            }
        }; */
//-----------------------------------------------------------------------------------------------------------------------------------------------


        mAuthStateListener = new FirebaseAuth.AuthStateListener()

        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(ProfilePage.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }

            }
        };
//==-----------------------------------------------------------------------------------------------------------------------------------------------
    }


    //==-----------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onStart() {
        super.onStart();

//For Reversing the order of Posts--------------------
        mLayoutManager = new LinearLayoutManager(new ProfilePage());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mDataList2.setLayoutManager(mLayoutManager);
//-----------------------------------------------------

        mAuth.addAuthStateListener(mAuthStateListener);

        FirebaseRecyclerAdapter<User_Data, MainActivity.userdataViewHolder> firebaseRecyclerAdapter2 = new FirebaseRecyclerAdapter<User_Data, MainActivity.userdataViewHolder>(
                User_Data.class,
                R.layout.post_row,
                MainActivity.userdataViewHolder.class,
                mDatabaseChildByBase.orderByChild("uid").equalTo(mCurrentUser.getUid())
        ) {
            @Override
            protected void populateViewHolder(final MainActivity.userdataViewHolder viewHolder, final User_Data model, int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDescreption());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.setProfileImage(getApplicationContext(), model.getProfileimage());
                //    viewHolder.setNavProfilePic(getApplicationContext(),model.getNavprofilepic());
                viewHolder.setYoutubeLink(model.getYoutubelink());
                viewHolder.setTotalLike(model.getTotalLike());
                viewHolder.setUsername(model.getUsername());
                viewHolder.setTime(model.getTime());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent SingleUserPostViewIntent2 = new Intent(ProfilePage.this, SinglePostView.class);
                        SingleUserPostViewIntent2.putExtra("post_id", post_key);
                        startActivity(SingleUserPostViewIntent2);

                        // Toast.makeText(ProfilePage.this, post_key, Toast.LENGTH_SHORT).show();
                    }
                });

                viewHolder.mButtonLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDatabaseChildByBase.child(post_key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot RefdataSnapshot) {
                                final String uidfinal = (String) RefdataSnapshot.child("uid").getValue();

                                mCurrentUserUid = mAuth.getCurrentUser().getUid();
                                mCurrentUserUidEmail = mAuth.getCurrentUser().getEmail();

                               // Toast.makeText(ProfilePage.this, "Phase 1", Toast.LENGTH_SHORT).show();

                                DatabaseReference likeKeyRef = mDatabaseChildByBase.child(post_key);
                                likeKeyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                       // Toast.makeText(ProfilePage.this, "Phase 2", Toast.LENGTH_SHORT).show();

                                        if (snapshot.hasChild("likeKey")) {

                                            final DatabaseReference AlreadylikeKeyRef = mDatabaseChildByBase.child(post_key).child("likeKey");
                                            final DatabaseReference totalLikeRef = mDatabaseChildByBase.child(post_key).child("totalLike");
                                            AlreadylikeKeyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot likeRefSnapshot) {
                                                    final String mRefLike = AlreadylikeKeyRef.child(mCurrentUserUid).getKey();
                                                    if (likeRefSnapshot.hasChild(mRefLike)) {
                                                        final Query RemoveLike = mDatabaseChildByBase.child(post_key).child("likeKey").child(mCurrentUserUid).equalTo(mRefLike);
                                                        RemoveLike.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                dataSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        AlreadylikeKeyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                long value = dataSnapshot.getChildrenCount();
                                                                                String StringValue = String.valueOf(value);
                                                                                totalLikeRef.setValue(StringValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                                                            viewHolder.mButtonLike.setImageDrawable(getResources().getDrawable(R.drawable.like, getApplicationContext().getTheme()));
                                                                                        } else {
                                                                                            viewHolder.mButtonLike.setImageDrawable(getResources().getDrawable(R.drawable.like));
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(DatabaseError databaseError) {

                                                                            }
                                                                        });
                                                                    }
                                                                });

                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });


                                                    //    Toast.makeText(ProfilePage.this, "You already liked it", Toast.LENGTH_SHORT).show();
                                                    } else if (!likeRefSnapshot.hasChild(mRefLike)) {
                                                        final DatabaseReference totalLikeRef = mDatabaseChildByBase.child(post_key).child("totalLike");
                                                        final DatabaseReference likeEntered = mDatabaseChildByBase.child(post_key).child("likeKey");
                                                        likeEntered.child(mCurrentUserUid).setValue(mCurrentUserUidEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                likeEntered.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        long value = dataSnapshot.getChildrenCount();
                                                                        String StringValue = String.valueOf(value);
                                                                        totalLikeRef.setValue(StringValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                                                    viewHolder.mButtonLike.setImageDrawable(getResources().getDrawable(R.drawable.filllike, getApplicationContext().getTheme()));
                                                                                } else {
                                                                                    viewHolder.mButtonLike.setImageDrawable(getResources().getDrawable(R.drawable.filllike));
                                                                                }
                                                                            }
                                                                        });
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });
                                                            }
                                                        });

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        } else if (!snapshot.hasChild("likeKey")) {
                                            final DatabaseReference totalLikeRef = mDatabaseChildByBase.child(post_key).child("totalLike");
                                            final DatabaseReference likeEntered = mDatabaseChildByBase.child(post_key).child("likeKey");
                                            likeEntered.child(mCurrentUserUid).setValue(mCurrentUserUidEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    likeEntered.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            long value = dataSnapshot.getChildrenCount();
                                                            String StringValue = String.valueOf(value);
                                                            totalLikeRef.setValue(StringValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                                        viewHolder.mButtonLike.setImageDrawable(getResources().getDrawable(R.drawable.filllike, getApplicationContext().getTheme()));
                                                                    } else {
                                                                        viewHolder.mButtonLike.setImageDrawable(getResources().getDrawable(R.drawable.filllike));
                                                                    }
                                                                }
                                                            });

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }

                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });


                viewHolder.mButtonCmnt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ProfilePage.this, "This Service is under Development!! Soon be deployed!", Toast.LENGTH_LONG).show();
                    }
                });


            }
        };

        mDataList2.setAdapter(firebaseRecyclerAdapter2);

    }

//-----------------
/*
    public static class userdataViewHolder2 extends RecyclerView.ViewHolder {

        View mView;
        FirebaseAuth mAuth;

        public userdataViewHolder2(View itemView) {
            super(itemView);

            mView = itemView;
            mAuth = FirebaseAuth.getInstance();

        }


        public void setTitle(String title) {
            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public void setDesc(String desc) {
            TextView post_Desc = (TextView) mView.findViewById(R.id.post_desc);
            post_Desc.setText(desc);
        }

        public void setUsername(String username) {
            TextView post_username = (TextView) mView.findViewById(R.id.post_username);
            post_username.setText(username);
        }

        public void setProfileImage(Context ctx, String pimage) {
            ImageView post_profileimage = (ImageView) mView.findViewById(R.id.post_profileimage);
            Picasso.with(ctx).load(pimage).into(post_profileimage);
        }

        public void setImage(Context ctx, String image) {
            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }

        public void setTime(String ptime) {
            TextView post_timecreated = (TextView) mView.findViewById(R.id.post_Date_time);
            post_timecreated.setText(ptime);
        }

        public void setYoutubeLink(final String youtube_link) {

            ImageView videoIcon = (ImageView) mView.findViewById(R.id.videoIcon);
            if (youtube_link != null) {
                videoIcon.setImageResource(R.drawable.videoicon);
            } else {
                videoIcon.setVisibility(View.GONE);
            }

            YouTubeThumbnailView ThumbnailView = (YouTubeThumbnailView) mView.findViewById(R.id.MainPageVideoPlayer);

            ThumbnailView.initialize(Api_Key_class.API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    youTubeThumbnailLoader.setVideo(youtube_link);
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                        @Override
                        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                            youTubeThumbnailLoader.release();
                            // Toast.makeText(MainActivity.this, "It's a valid youtube url.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                            try {
                                // Toast.makeText(getActivity(), "Not a valid youtube url.", Toast.LENGTH_SHORT).show();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                }
            });
        }

    }
//==-----------------------------------------------------------------------------------------------------------------------------------------------
*/

    //  @SuppressLint("SetTextI18n")
    // private void setDataToView(FirebaseUser user) {
    //     email.setText(user.getEmail());
    //  }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add_Refresh) {
            Intent intent = getIntent();
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}