package com.picbit.info.android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appszoom.appszoomsdk.Appszoom;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Category_wise_post extends AppCompatActivity {

    private String mlink = null;
    private TextView mCurrent_Category;
    private DatabaseReference mDatabase;
    private AdView mAdView;
    public String mCurrentUserUid;
    private FirebaseAuth mAuth;
    public String mCurrentUserUidEmail;
    private DatabaseReference mDatabaseSample;
    private RecyclerView mCategoryWise_postList;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_wise_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("PicBit");
        //  getActionBar().setIcon(R.drawable.my_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        mDatabaseSample = FirebaseDatabase.getInstance().getReference().child("UserData_Images_value");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("UserData_Images_value");
        mDatabase.keepSynced(true);

        mCategoryWise_postList = (RecyclerView) findViewById(R.id.CategoryWise_PostList);
        mCategoryWise_postList.setHasFixedSize(true);
        mCategoryWise_postList.setLayoutManager(new LinearLayoutManager(this));

        mlink = getIntent().getExtras().getString("link");

        mCurrent_Category = (TextView) findViewById(R.id.Current_Category);
        mCurrent_Category.setText(mlink);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (AppStatus.getInstance(this).isOnline()) {
            // Toast.makeText(this, "You are online!!!!", Toast.LENGTH_LONG).show();

            //For Reversing the order of Posts--------------------
            mLayoutManager = new LinearLayoutManager(new Category_wise_post());
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
            mCategoryWise_postList.setLayoutManager(mLayoutManager);
//-----------------------------------------------------

            FirebaseRecyclerAdapter<User_Data, MainActivity.userdataViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User_Data, MainActivity.userdataViewHolder>(
                    User_Data.class,
                    R.layout.post_row,
                    MainActivity.userdataViewHolder.class,
                    mDatabase.orderByChild("category").equalTo(mlink)
            ) {
                @Override
                protected void populateViewHolder(final MainActivity.userdataViewHolder viewHolder, User_Data model, int position) {

                    final String post_key = getRef(position).getKey();

                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setDesc(model.getDescreption());
                    viewHolder.setImage(getApplicationContext(), model.getImage());
                    viewHolder.setProfileImage(getApplicationContext(), model.getProfileimage());
                    viewHolder.setTime(model.getTime());
                    viewHolder.setYoutubeLink(model.getYoutubelink());
                    viewHolder.setTotalLike(model.getTotalLike());
                    viewHolder.setUsername(model.getUsername());

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent SinglePostViewIntent = new Intent(Category_wise_post.this, SinglePostView.class);
                            SinglePostViewIntent.putExtra("post_id", post_key);
                            // String UidKey = (String) mDatabase.child(post_key).child("uid").getKey().;
                            startActivity(SinglePostViewIntent);

                            //  Toast.makeText(MainActivity.this, UidKey, Toast.LENGTH_LONG).show();
                        }
                    });


                    viewHolder.post_username.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDatabaseSample.child(post_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot RefdataSnapshot) {
                                    String uidfinal = (String) RefdataSnapshot.child("uid").getValue();
                                    Intent RandomUserProfileIntent = new Intent(Category_wise_post.this, Random_Profile_Page.class);
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


                    viewHolder.post_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDatabaseSample.child(post_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot RefdataSnapshot) {
                                    String uidfinal = (String) RefdataSnapshot.child("uid").getValue();
                                    Intent RandomUserProfileIntent = new Intent(Category_wise_post.this, Random_Profile_Page.class);
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

                                    mCurrentUserUid = mAuth.getCurrentUser().getUid();
                                    mCurrentUserUidEmail = mAuth.getCurrentUser().getEmail();

                                   // Toast.makeText(Category_wise_post.this, "Phase 1", Toast.LENGTH_SHORT).show();

                                    DatabaseReference likeKeyRef = mDatabaseSample.child(post_key);
                                    likeKeyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {
                                          //  Toast.makeText(Category_wise_post.this, "Phase 2", Toast.LENGTH_SHORT).show();

                                            if (snapshot.hasChild("likeKey")) {

                                                final DatabaseReference AlreadylikeKeyRef = mDatabaseSample.child(post_key).child("likeKey");
                                                final DatabaseReference totalLikeRef = mDatabaseSample.child(post_key).child("totalLike");
                                                AlreadylikeKeyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot likeRefSnapshot) {
                                                        final String mRefLike = AlreadylikeKeyRef.child(mCurrentUserUid).getKey();
                                                        if (likeRefSnapshot.hasChild(mRefLike)) {
                                                            final Query RemoveLike = mDatabaseSample.child(post_key).child("likeKey").child(mCurrentUserUid).equalTo(mRefLike);
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


                                                         //   Toast.makeText(Category_wise_post.this, "You already liked it", Toast.LENGTH_SHORT).show();
                                                        } else if (!likeRefSnapshot.hasChild(mRefLike)) {
                                                            final DatabaseReference totalLikeRef = mDatabaseSample.child(post_key).child("totalLike");
                                                            final DatabaseReference likeEntered = mDatabaseSample.child(post_key).child("likeKey");
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
                                                final DatabaseReference totalLikeRef = mDatabaseSample.child(post_key).child("totalLike");
                                                final DatabaseReference likeEntered = mDatabaseSample.child(post_key).child("likeKey");
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
                            Toast.makeText(Category_wise_post.this, "This Service is under Development!! Soon be deployed!", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            };

            mCategoryWise_postList.setAdapter(firebaseRecyclerAdapter);

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Category_wise_post.this);
            builder.setTitle("Network Issue!!");
            builder.setMessage("Please Click 'Back' to Go Back!!").setCancelable(false);
            builder.setPositiveButton("Back",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            finish();
                        }
                    });
            builder.show();
        }
    }


    //----------------------------------------------------------------------------------------------------

    /*public static class userdataViewHolder5 extends RecyclerView.ViewHolder {

        View mView;
        FirebaseAuth mAuth;
        TextView post_username;
        CircleImageView post_image;

        public userdataViewHolder5(View itemView) {
            super(itemView);

            mView = itemView;
            mAuth = FirebaseAuth.getInstance();

            post_username = (TextView) mView.findViewById(R.id.post_username);
            post_image = (CircleImageView) mView.findViewById(R.id.post_profileimage);

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
*/
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
