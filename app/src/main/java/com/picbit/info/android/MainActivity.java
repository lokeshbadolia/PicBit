package com.picbit.info.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.DashPathEffect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {//YouTubeBaseActivity {//AppCompatActivity {

    private RecyclerView mDataList;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseSample;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    public String mCurrentUser;
    public String mCurrentUserEmail;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private LinearLayoutManager mLayoutManager;
    private NavigationView navigation;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    boolean doubleBackToExitPressedOnce = false;
    private Toolbar toolbar;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("PicBit");
        // getActionBar().setIcon(R.mipmap.ic_launcher);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Side drawer Start ----------===========================
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        navigation = (NavigationView) findViewById(R.id.navigationDrawer);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.nav_MainPage:
                        Intent CategoryPageIntent = new Intent(MainActivity.this, Category_list.class);
                        CategoryPageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(CategoryPageIntent);
                        //Toast.makeText(MainActivity.this,"Soon Available",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_Profile:
                        Intent profilePageIntent = new Intent(MainActivity.this, ProfilePage.class);
                        profilePageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(profilePageIntent);
                        break;
             /*       case R.id.nav_EditProfile:
                        Intent editProfilePageIntent = new Intent(MainActivity.this, EditProfile.class);
                        editProfilePageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(editProfilePageIntent);
                        break;
               */
                    case R.id.nav_ContactUs:
                        Intent EmailIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "lokeshbadolia7@gmail.com"));
                        EmailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                        EmailIntent.putExtra(Intent.EXTRA_TEXT, "");
                        startActivity(EmailIntent);
                        break;

                  /*  case R.id.nav_DevProfile:
                        Intent developerPageIntent = new Intent(MainActivity.this, PostActivity.class);
                        developerPageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(developerPageIntent);
                        break;
*/
                    case R.id.nav_LogOut:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Do you Really wanna Continue?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        logout();

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.setTitle("Log Out");
                        dialog.show();

                        break;

                    case R.id.nav_Paypal:
                       /* Uri urlPaypal = Uri.parse("https://www.paypal.me/droidgamer/"); // missing 'http://' will cause crashed
                        Intent PaypalLink = new Intent(Intent.ACTION_VIEW, urlPaypal);
                        startActivity(PaypalLink);*/
                        Toast.makeText(MainActivity.this, "Under Development", Toast.LENGTH_SHORT).show();
                        break;


                    case R.id.nav_privacy:
                      /*  Uri privacy = Uri.parse("https://picbitprivacy.wordpress.com/"); // missing 'http://' will cause crashed
                        Intent privacyPolicy = new Intent(Intent.ACTION_VIEW, privacy);
                        startActivity(privacyPolicy);
                        */
                        Toast.makeText(MainActivity.this, "Under Development", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_terms:
                      /*  Uri terms = Uri.parse("https://picbitprivacy.wordpress.com/home/terms-and-condition/"); // missing 'http://' will cause crashed
                        Intent termsCondition = new Intent(Intent.ACTION_VIEW, terms);
                        startActivity(termsCondition);
                        */
                        Toast.makeText(MainActivity.this, "Under Development", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_KnowFirebase:
                        Uri urlFirebase = Uri.parse("https://firebase.google.com/products/"); // missing 'http://' will cause crashed
                        Intent FirebaseLink = new Intent(Intent.ACTION_VIEW, urlFirebase);
                        startActivity(FirebaseLink);
                        break;

                    case R.id.nav_icon8:
                        Uri urlicon8 = Uri.parse("https://icons8.com/"); // missing 'http://' will cause crashed
                        Intent icon8Link = new Intent(Intent.ACTION_VIEW, urlicon8);
                        startActivity(icon8Link);
                        break;

                }
                mDrawerLayout.closeDrawer(GravityCompat.START);//Close Drawer after item select
                return true;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Side drawer End ----------===========================

//-------------------------------------------------------------------------------------------------------------------------------

        // Blog Posting category select ----------===========================
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, categoryselectlist.class);
                startActivity(intent);

            }
        });
        // Blog Posting category select End ----------===========================

//-------------------------------------------------------------------------------------------------------------------------------
        //mCurrentUser = mAuth.getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        mDatabaseSample = FirebaseDatabase.getInstance().getReference().child("UserData_Images_value");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users_post");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabase.keepSynced(true);
        mDatabaseUsers.keepSynced(true);
        mProgress = new ProgressDialog(this);


        mAuthListner = new FirebaseAuth.AuthStateListener()

        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {


                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }

            }
        };

        mDataList = (RecyclerView) findViewById(R.id.Data_list);
        mDataList.setHasFixedSize(true);
        mDataList.setLayoutManager(new LinearLayoutManager(this));


        checkUserExist();
    }


    //----------------------------
    @Override
    protected void onStart() {
        super.onStart();

        if (AppStatus.getInstance(this).isOnline()) {
            // Toast.makeText(this, "You are online!!!!", Toast.LENGTH_LONG).show();

            //For Reversing the order of Posts--------------------
            mLayoutManager = new LinearLayoutManager(new MainActivity());
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
            mDataList.setLayoutManager(mLayoutManager);
            //-----------------------------------------------------

            mAuth.addAuthStateListener(mAuthListner);

            FirebaseRecyclerAdapter<User_Data, userdataViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User_Data, userdataViewHolder>(
                    User_Data.class,
                    R.layout.post_row,
                    userdataViewHolder.class,
                    mDatabaseSample
            ) {
                @Override
                protected void populateViewHolder(final userdataViewHolder viewHolder, User_Data model, final int position) {

                    final String post_key = getRef(position).getKey();


                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setDesc(model.getDescreption());
                    viewHolder.setImage(getApplicationContext(), model.getImage());
                    viewHolder.setProfileImage(getApplicationContext(), model.getProfileimage());
                    viewHolder.setYoutubeLink(model.getYoutubelink());
                    viewHolder.setTotalLike(model.getTotalLike());
                    viewHolder.setTime(model.getTime());
                    viewHolder.setUsername(model.getUsername());


                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent SinglePostViewIntent1 = new Intent(MainActivity.this, SinglePostView.class);
                            SinglePostViewIntent1.putExtra("post_id", post_key);
                            startActivity(SinglePostViewIntent1);
                        }
                    });


                    viewHolder.post_username.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDatabaseSample.child(post_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot RefdataSnapshot) {
                                    String uidfinal = (String) RefdataSnapshot.child("uid").getValue();
                                    Intent RandomUserProfileIntent = new Intent(MainActivity.this, Random_Profile_Page.class);
                                    RandomUserProfileIntent.putExtra("uidFinal", uidfinal);
                                    startActivity(RandomUserProfileIntent);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    });


                    viewHolder.post_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDatabaseSample.child(post_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot RefdataSnapshot) {
                                    String uidfinal = (String) RefdataSnapshot.child("uid").getValue();
                                    Intent RandomUserProfileIntent = new Intent(MainActivity.this, Random_Profile_Page.class);
                                    RandomUserProfileIntent.putExtra("uidFinal", uidfinal);
                                    startActivity(RandomUserProfileIntent);
                                    // Toast.makeText(MainActivity.this, uidfinal, Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    });

                    viewHolder.mButtonLike.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDatabaseSample.child(post_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot RefdataSnapshot) {
                                    final String uidfinal = (String) RefdataSnapshot.child("uid").getValue();

                                    mCurrentUser = mAuth.getCurrentUser().getUid();
                                    mCurrentUserEmail = mAuth.getCurrentUser().getEmail();
                                    DatabaseReference likeKeyRef = mDatabaseSample.child(post_key);
                                    likeKeyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {
                                            if (snapshot.hasChild("likeKey")) {
                                                final DatabaseReference AlreadylikeKeyRef = mDatabaseSample.child(post_key).child("likeKey");
                                                final DatabaseReference totalLikeRef = mDatabaseSample.child(post_key).child("totalLike");
                                                AlreadylikeKeyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot likeRefSnapshot) {
                                                        final String mRefLike = AlreadylikeKeyRef.child(mCurrentUser).getKey();
                                                        if (likeRefSnapshot.hasChild(mRefLike)) {
                                                            final Query RemoveLike = mDatabaseSample.child(post_key).child("likeKey").child(mCurrentUser).equalTo(mRefLike);
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


                                                        } else if (!likeRefSnapshot.hasChild(mRefLike)) {
                                                            final DatabaseReference totalLikeRef = mDatabaseSample.child(post_key).child("totalLike");
                                                            final DatabaseReference likeEntered = mDatabaseSample.child(post_key).child("likeKey");
                                                            likeEntered.child(mCurrentUser).setValue(mCurrentUserEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                                                final DatabaseReference totalLikeRef = mDatabaseSample.child(post_key).child("totalLike");
                                                final DatabaseReference likeEntered = mDatabaseSample.child(post_key).child("likeKey");
                                                likeEntered.child(mCurrentUser).setValue(mCurrentUserEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                            Toast.makeText(MainActivity.this, "This Service is under Development!! Soon be deployed!", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            };

            mDataList.setAdapter(firebaseRecyclerAdapter);

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Network Issue!!");
            builder.setMessage("Please Connect to Internet Or Press 'OK' to Close the App.").setCancelable(false);
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            finish();
                        }
                    });
            builder.show();
        }
    }


    //-------------------------------------------
    private void checkUserExist() {

        if (mAuth.getCurrentUser() != null) {

            final String user_id = mAuth.getCurrentUser().getUid();

            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (!dataSnapshot.hasChild(user_id)) {

                        Intent setupIntent = new Intent(MainActivity.this, AccountSetup.class);
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(setupIntent);
                        finish();

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    //-------------------------------------------

//---------------------------- [For displaying the data on main page]

    public static class userdataViewHolder extends RecyclerView.ViewHolder {

        View mView;
        FirebaseAuth mAuth;
        TextView post_username;
        CircleImageView post_image;
        //   EditText mWriteCmnt;
        //   ImageView mSendCmnt;
        ImageView mButtonCmnt;
        ImageView mButtonLike;
        // TextView likeCount;

        public userdataViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mAuth = FirebaseAuth.getInstance();

            mButtonCmnt = (ImageView) mView.findViewById(R.id.button_cmnt);
            mButtonLike = (ImageView) mView.findViewById(R.id.button_like);
            // likeCount = (TextView) mView.findViewById(R.id.likeCount);
            // mWriteCmnt = (EditText) mView.findViewById(R.id.Cmnt_TextBox);
            // mSendCmnt = (ImageView)mView.findViewById(R.id.send_Cmnt);

            post_username = (TextView) mView.findViewById(R.id.post_username);
            post_image = (CircleImageView) mView.findViewById(R.id.post_profileimage);

            // mSendCmnt = (ImageView) mView.findViewById(R.id.send_Cmnt);
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

        public void setTime(String ptime) {
            TextView post_timecreated = (TextView) mView.findViewById(R.id.post_Date_time);
            post_timecreated.setText(ptime);
        }


        public void setImage(Context ctx, String image) {
            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }

        public void setTotalLike(String totalLike) {
            TextView post_totalLike = (TextView) mView.findViewById(R.id.likeCount);
            post_totalLike.setText(totalLike);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.action_add_Refresh) {
            Intent intent = getIntent();
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    //---------------------------------
    // End here -----------------------
    //---------------------------------
    /*@Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }*/


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            //super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
            startActivity(intent);
            finish();
            //System.exit(0);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click 'BACK' again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    private void logout() {
        mAuth.signOut();
    }

}
