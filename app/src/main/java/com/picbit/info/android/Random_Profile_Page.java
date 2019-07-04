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
import com.google.android.gms.ads.formats.NativeAd;
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

public class Random_Profile_Page extends AppCompatActivity {

    private String randomUserProfile_link = null;
    private DatabaseReference mDatabaseUsers;
    private CircleImageView photo;
    private DatabaseReference mDatabaseSample;
    //private TextView email;
    private LinearLayoutManager mLayoutManager;
    private TextView name;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    public String mCurrentUserUid;
    public String mCurrentUserUidEmail;
    private RecyclerView randomUserData_List;
    private DatabaseReference mDatabasePinnedProfile;
    private DatabaseReference mDatabaseUserPost;

  //  private String randomRecFbLink,randomRecInstaLink,randomRecTwitterLink,randomRecBloggerLink,randomRecLinkedInLink,randomRecYoutubeLink,randomRecWebLink;

  /*  private ImageView randomProfileFbLink;
    private ImageView randomProfileInstaLink;
    private ImageView randomProfileTwitterLink;
    private ImageView randomProfileBloggerLink;
    private ImageView randomProfileLinkedInLink;
    private ImageView randomProfileYoutubeLink;
    private ImageView randomProfileWebLink;

    private ImageView PinPostBtn; */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random__profile__page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("PicBit");
        //  getActionBar().setIcon(R.drawable.my_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //mCurrentUser = mAuth.getCurrentUser();
        randomUserProfile_link = getIntent().getExtras().getString("uidFinal");
        mDatabaseSample = FirebaseDatabase.getInstance().getReference().child("UserData_Images_value");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference();
       // mDatabaseUserPost = FirebaseDatabase.getInstance().getReference().child("Users_post").child(randomUserProfile_link);
      //  mDatabasePinnedProfile =  FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        name = (TextView) findViewById(R.id.randomUserName);
        ///email = (TextView) findViewById(R.id.randomUserEmail);
        photo = (CircleImageView) findViewById(R.id.randomUserPhoto);

       /* randomProfileFbLink = (ImageView)findViewById(R.id.randomProfileFbLink);
        randomProfileInstaLink = (ImageView)findViewById(R.id.randomProfileInstaLink);
        randomProfileTwitterLink = (ImageView)findViewById(R.id.randomProfileTwitterLink);
        randomProfileBloggerLink = (ImageView)findViewById(R.id.randomProfileBloggerLink);
        randomProfileLinkedInLink = (ImageView)findViewById(R.id.randomProfileLinkedInLink);
        randomProfileYoutubeLink = (ImageView)findViewById(R.id.randomProfileYoutubeLink);
        randomProfileWebLink = (ImageView)findViewById(R.id.randomProfileWebLink);
        PinPostBtn = (ImageView) findViewById(R.id.PinPost);

       /* PinPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        mDatabasePinnedProfile.child("Following").push().child(randomUserProfile_link);
            }
        });
*/



       mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String mName = dataSnapshot.child("Users").child(randomUserProfile_link).child("name").getValue(String.class);
                name.setText(mName);
                String pImage = (String) dataSnapshot.child("Users").child(randomUserProfile_link).child("image").getValue();
                Picasso.with(Random_Profile_Page.this).load(pImage).into(photo);

               /* String mRandomFbLink = dataSnapshot.child("Users").child(randomUserProfile_link).child("fblink").getValue(String.class);
                randomRecFbLink = mRandomFbLink.toString();
                String mRandomInstaLink = dataSnapshot.child("Users").child(randomUserProfile_link).child("instalink").getValue(String.class);
                randomRecInstaLink = mRandomInstaLink.toString();
                String mRandomTwitterLink = dataSnapshot.child("Users").child(randomUserProfile_link).child("twitterlink").getValue(String.class);
                randomRecTwitterLink = mRandomTwitterLink.toString();
                String mRandomBloggerLink = dataSnapshot.child("Users").child(randomUserProfile_link).child("bloggerlink").getValue(String.class);
                randomRecBloggerLink = mRandomBloggerLink.toString();
                String mRandomLinkedinLink = dataSnapshot.child("Users").child(randomUserProfile_link).child("linkedinlink").getValue(String.class);
                randomRecLinkedInLink = mRandomLinkedinLink.toString();
                String mRandomYoutubeLink = dataSnapshot.child("Users").child(randomUserProfile_link).child("youtubelink").getValue(String.class);
                randomRecYoutubeLink = mRandomYoutubeLink.toString();
                String mRandomWebLink = dataSnapshot.child("Users").child(randomUserProfile_link).child("weblink").getValue(String.class);
                randomRecWebLink = mRandomWebLink.toString();


                if(randomRecFbLink.isEmpty()){
                    randomProfileFbLink.setImageResource(R.drawable.facebook);
                    randomProfileFbLink.setClickable(false);
                } else
                {
                    randomProfileFbLink.setImageResource(R.drawable.fillfacebook);
                    randomProfileFbLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Pattern fbValidRandom = Patterns.WEB_URL;
                            Matcher fbTooValidRandom = fbValidRandom.matcher(randomRecFbLink);
                            if (fbTooValidRandom.matches()) {
                                Uri openFbLinkRandom = Uri.parse(randomRecFbLink); // missing 'http://' will cause crashed
                                Intent OpenFbLinkRandom = new Intent(Intent.ACTION_VIEW, openFbLinkRandom);
                                startActivity(OpenFbLinkRandom);
                            } else {
                                Toast.makeText(Random_Profile_Page.this, "Person Facebook Link is Broken", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                if(randomRecInstaLink.isEmpty()){
                    randomProfileInstaLink.setImageResource(R.drawable.instagram);
                    randomProfileInstaLink.setClickable(false);
                } else
                {
                    randomProfileInstaLink.setImageResource(R.drawable.fillinstagram);
                    randomProfileInstaLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Pattern instaValidRandom = Patterns.WEB_URL;
                            Matcher instaTooValidRandom = instaValidRandom.matcher(randomRecInstaLink);
                            if (instaTooValidRandom.matches()) {
                                Uri openInstaLinkRandom = Uri.parse(randomRecInstaLink); // missing 'http://' will cause crashed
                                Intent OpenInstaLinkRandom = new Intent(Intent.ACTION_VIEW, openInstaLinkRandom);
                                startActivity(OpenInstaLinkRandom);
                            } else {
                                Toast.makeText(Random_Profile_Page.this, "Person Instagram Link is Broken", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                if(randomRecTwitterLink.isEmpty()){
                    randomProfileTwitterLink.setImageResource(R.drawable.twitter);
                    randomProfileTwitterLink.setClickable(false);
                } else
                {
                    randomProfileTwitterLink.setImageResource(R.drawable.filltwitter);
                    randomProfileTwitterLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Pattern twitterValidRandom = Patterns.WEB_URL;
                            Matcher twitterTooValidRandom = twitterValidRandom.matcher(randomRecTwitterLink);
                            if (twitterTooValidRandom.matches()) {
                                Uri openTwitterLinkRandom = Uri.parse(randomRecTwitterLink); // missing 'http://' will cause crashed
                                Intent OpenTwitterLinkRandom = new Intent(Intent.ACTION_VIEW, openTwitterLinkRandom);
                                startActivity(OpenTwitterLinkRandom);
                            } else {
                                Toast.makeText(Random_Profile_Page.this, "Person Twitter Link is Broken", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                if(randomRecBloggerLink.isEmpty()){
                    randomProfileBloggerLink.setImageResource(R.drawable.blogger);
                    randomProfileBloggerLink.setClickable(false);
                } else
                {
                    randomProfileBloggerLink.setImageResource(R.drawable.fillblogger);
                    randomProfileBloggerLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Pattern bloggerValidRandom = Patterns.WEB_URL;
                            Matcher bloggerTooValidRandom = bloggerValidRandom.matcher(randomRecBloggerLink);
                            if (bloggerTooValidRandom.matches()) {
                                Uri openBloggerLinkRandom = Uri.parse(randomRecBloggerLink); // missing 'http://' will cause crashed
                                Intent OpenBloggerLinkRandom = new Intent(Intent.ACTION_VIEW, openBloggerLinkRandom);
                                startActivity(OpenBloggerLinkRandom);
                            } else {
                                Toast.makeText(Random_Profile_Page.this, "Person Blogger Link is Broken", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                if(randomRecLinkedInLink.isEmpty()){
                    randomProfileLinkedInLink.setImageResource(R.drawable.linkedin);
                    randomProfileLinkedInLink.setClickable(false);
                } else
                {
                    randomProfileLinkedInLink.setImageResource(R.drawable.filllinkedin);
                    randomProfileLinkedInLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Pattern linkedinValidRandom = Patterns.WEB_URL;
                            Matcher LinkedInTooValidRandom = linkedinValidRandom.matcher(randomRecLinkedInLink);
                            if (LinkedInTooValidRandom.matches()) {
                                Uri openLinkedInLinkRandom = Uri.parse(randomRecLinkedInLink); // missing 'http://' will cause crashed
                                Intent OpenLinkedInLinkRandom = new Intent(Intent.ACTION_VIEW, openLinkedInLinkRandom);
                                startActivity(OpenLinkedInLinkRandom);
                            } else {
                                Toast.makeText(Random_Profile_Page.this, "Person LinkedIn Link is Broken", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                if(randomRecYoutubeLink.isEmpty()){
                    randomProfileYoutubeLink.setImageResource(R.drawable.youtube);
                    randomProfileYoutubeLink.setClickable(false);
                } else
                {
                    randomProfileYoutubeLink.setImageResource(R.drawable.fillyoutube);
                    randomProfileYoutubeLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Pattern youtubeValidRandom = Patterns.WEB_URL;
                            Matcher YoutubeTooValidRandom = youtubeValidRandom.matcher(randomRecYoutubeLink);
                            if (YoutubeTooValidRandom.matches()) {
                                Uri openYoutubeLinkRandom = Uri.parse(randomRecYoutubeLink); // missing 'http://' will cause crashed
                                Intent OpenYoutubeLinkRandom = new Intent(Intent.ACTION_VIEW, openYoutubeLinkRandom);
                                startActivity(OpenYoutubeLinkRandom);
                            } else {
                                Toast.makeText(Random_Profile_Page.this, "Person Youtube Link is Broken", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                if(randomRecWebLink.isEmpty()){
                    randomProfileWebLink.setImageResource(R.drawable.web);
                    randomProfileWebLink.setClickable(false);
                } else
                {
                    randomProfileWebLink.setImageResource(R.drawable.fillweb);
                    randomProfileWebLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Pattern webValidRandom = Patterns.WEB_URL;
                            Matcher WebTooValidRandom = webValidRandom.matcher(randomRecWebLink);
                            if (WebTooValidRandom.matches()) {
                                Uri openWebLinkRandom = Uri.parse(randomRecWebLink); // missing 'http://' will cause crashed
                                Intent OpenWebLinkRandom = new Intent(Intent.ACTION_VIEW, openWebLinkRandom);
                                startActivity(OpenWebLinkRandom);
                            } else {
                                Toast.makeText(Random_Profile_Page.this, "Person Website Link is Broken", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       /*PinPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mDatabaseUsers.child("Users_post").child(mCurrentUser.getUid()).child("Pinned_Profile").push().setValue(randomUserProfile_link);
               PinPostBtn.setImageResource(R.drawable.filllike);
            }
        });
*/
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String mName = dataSnapshot.child("Users").child(randomUserProfile_link).child("name").getValue(String.class);
                name.setText(mName);

                String pImage = (String) dataSnapshot.child("Users").child(randomUserProfile_link).child("image").getValue();
                Picasso.with(Random_Profile_Page.this).load(pImage).into(photo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        randomUserData_List = (RecyclerView) findViewById(R.id.randomUserPost_List);
       // randomUserData_List.setHasFixedSize(true);
        randomUserData_List.setLayoutManager(new LinearLayoutManager(this));

     /*   randomUserData_List.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    */ }

    @Override
    protected void onStart() {
        super.onStart();

//For Reversing the order of Posts--------------------
        mLayoutManager = new LinearLayoutManager(new Random_Profile_Page());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        randomUserData_List.setLayoutManager(mLayoutManager);
//-----------------------------------------------------

        FirebaseRecyclerAdapter<User_Data, MainActivity.userdataViewHolder> randomfirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User_Data, MainActivity.userdataViewHolder>(
                User_Data.class,
                R.layout.post_row,
                MainActivity.userdataViewHolder.class,
                mDatabaseSample.orderByChild("uid").equalTo(randomUserProfile_link)
        ) {
            @Override
            protected void populateViewHolder(final MainActivity.userdataViewHolder viewHolder, User_Data model, int position) {

                final String Randompost_key = getRef(position).getKey();
                //final String RandomUserPost_link = randomUserProfile_link;

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDescreption());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.setProfileImage(getApplicationContext(), model.getProfileimage());
                viewHolder.setYoutubeLink(model.getYoutubelink());
                viewHolder.setTotalLike(model.getTotalLike());
                viewHolder.setUsername(model.getUsername());
                viewHolder.setTime(model.getTime());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent RandomUserPostViewIntent = new Intent(Random_Profile_Page.this, SinglePostView.class);
                        RandomUserPostViewIntent.putExtra("post_id", Randompost_key);
                        startActivity(RandomUserPostViewIntent);
                        //Toast.makeText(Random_Profile_Page.this, randomuUidfinal, Toast.LENGTH_LONG).show();
                        //Toast.makeText(Random_Profile_Page.this, Randompost_key, Toast.LENGTH_LONG).show();
                    }
                });


                viewHolder.mButtonLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDatabaseSample.child(Randompost_key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot RefdataSnapshot) {
                                final String uidfinal = (String) RefdataSnapshot.child("uid").getValue();

                                mCurrentUserUid = mAuth.getCurrentUser().getUid();
                                mCurrentUserUidEmail = mAuth.getCurrentUser().getEmail();

                               // Toast.makeText(Random_Profile_Page.this, "Phase 1", Toast.LENGTH_SHORT).show();

                                DatabaseReference likeKeyRef = mDatabaseSample.child(Randompost_key);
                                likeKeyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                     //   Toast.makeText(Random_Profile_Page.this, "Phase 2", Toast.LENGTH_SHORT).show();

                                        if (snapshot.hasChild("likeKey")) {

                                            final DatabaseReference AlreadylikeKeyRef = mDatabaseSample.child(Randompost_key).child("likeKey");
                                            final DatabaseReference totalLikeRef = mDatabaseSample.child(Randompost_key).child("totalLike");
                                            AlreadylikeKeyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot likeRefSnapshot) {
                                                    final String mRefLike = AlreadylikeKeyRef.child(mCurrentUserUid).getKey();
                                                    if (likeRefSnapshot.hasChild(mRefLike)) {
                                                        final Query RemoveLike = mDatabaseSample.child(Randompost_key).child("likeKey").child(mCurrentUserUid).equalTo(mRefLike);
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


                                                       // Toast.makeText(Random_Profile_Page.this, "You already liked it", Toast.LENGTH_SHORT).show();
                                                    } else if (!likeRefSnapshot.hasChild(mRefLike)) {
                                                        final DatabaseReference totalLikeRef = mDatabaseSample.child(Randompost_key).child("totalLike");
                                                        final DatabaseReference likeEntered = mDatabaseSample.child(Randompost_key).child("likeKey");
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
                                            final DatabaseReference totalLikeRef = mDatabaseSample.child(Randompost_key).child("totalLike");
                                            final DatabaseReference likeEntered = mDatabaseSample.child(Randompost_key).child("likeKey");
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
                        Toast.makeText(Random_Profile_Page.this, "This Service is under Development!! Soon be deployed!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        randomUserData_List.setAdapter(randomfirebaseRecyclerAdapter);
    }

/*
    public static class userdataViewHolder4 extends RecyclerView.ViewHolder {

        View mView;
        FirebaseAuth mAuth;

        public userdataViewHolder4(View itemView) {
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
*/
    //}

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
