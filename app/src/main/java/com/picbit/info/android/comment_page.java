package com.picbit.info.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class comment_page extends AppCompatActivity {
    private String mPost_key = null;
    private String mPost_UserKey = null;
    private String mPost_ChildKey = null;
    private String mParentCmntKey;
    private String mChildCmntKey;
    private EditText mCmnt_Box;
    private FloatingActionButton mSendCmnt;
    private DatabaseReference mDatabaseComments_location;
    private DatabaseReference mDatabaseComments_ProfilePostWise;
    private DatabaseReference mDatabaseComments_DeletePostWise;
    private DatabaseReference mDatabaseComments;
    private DatabaseReference mDatabaseAdminCmnt;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private LinearLayoutManager mLayoutManager;
    private CircleImageView mCommentUserPhoto;
    private DatabaseReference mSampleDatabase;
    private RecyclerView mCmntList;
    public String RandomCmntUser = null;
    public String AdminCmntUser = null;
    //  private String random_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("PicBit");
        //  getActionBar().setIcon(R.drawable.my_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPost_key = getIntent().getExtras().getString("post_id");
        mPost_UserKey = getIntent().getExtras().getString("uidFinal");
        mPost_ChildKey = getIntent().getExtras().getString("Child_key");
      //  Toast.makeText(this, mPost_key, Toast.LENGTH_LONG).show();
      //  Toast.makeText(this, mPost_UserKey, Toast.LENGTH_LONG).show();
      //  Toast.makeText(this, mPost_ChildKey, Toast.LENGTH_LONG).show();

        mCmnt_Box = (EditText) findViewById(R.id.Cmnt_TextBox);
        mSendCmnt = (FloatingActionButton) findViewById(R.id.send_Cmnt);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mCmntList = (RecyclerView) findViewById(R.id.cmnt_list);
        mCmntList.setLayoutManager(new LinearLayoutManager(this));
        mCmntList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        mDatabaseComments_location = FirebaseDatabase.getInstance().getReference().child("UserData_Images_value").child(mPost_key).child("All_Comments").push();
        mParentCmntKey = mDatabaseComments_location.getKey();
        mDatabaseComments_ProfilePostWise = FirebaseDatabase.getInstance().getReference().child("Users_post").child(mPost_UserKey).child(mPost_ChildKey).child("All_Comments").push();
        mChildCmntKey = mDatabaseComments_ProfilePostWise.getKey();
        mDatabaseComments_DeletePostWise = FirebaseDatabase.getInstance().getReference().child("Users_post").child(mPost_UserKey).child(mPost_ChildKey).child("All_Comments");
        mDatabaseComments = FirebaseDatabase.getInstance().getReference().child("UserData_Images_value").child(mPost_key).child("All_Comments");
        mDatabaseAdminCmnt = FirebaseDatabase.getInstance().getReference().child("UserData_Images_value").child(mPost_key);


        //  mDatabaseCommentUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(random_uid);


        mSendCmnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCmnt();
            }
        });


        mCommentUserPhoto = (CircleImageView) findViewById(R.id.CurrentUserImage_Comment);
        mSampleDatabase = FirebaseDatabase.getInstance().getReference();
        mSampleDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String pImage = (String) dataSnapshot.child("Users").child(mCurrentUser.getUid()).child("image").getValue();
                Picasso.with(comment_page.this).load(pImage).into(mCommentUserPhoto);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }



    private void addCmnt() {
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Calendar calendar = Calendar.getInstance();
                TimeZone tz = TimeZone.getDefault();
                calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
                SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy HH:mm", Locale.getDefault());
                java.util.Date  currenTimeZone = new java.util.Date((long) System.currentTimeMillis());

                String finalCmnt = mCmnt_Box.getText().toString().trim();


                mDatabaseComments_location.child("profileimage").setValue(dataSnapshot.child("image").getValue());
                mDatabaseComments_location.child("username").setValue(dataSnapshot.child("name").getValue());
                mDatabaseComments_location.child("random_uid").setValue(mCurrentUser.getUid());

                Toast.makeText(comment_page.this, RandomCmntUser, Toast.LENGTH_LONG).show();
                mDatabaseComments_location.child("ChildCmntKey").setValue(mChildCmntKey);
                mDatabaseComments_location.child("Comment").setValue(finalCmnt);
                mDatabaseComments_location.child("Comment_time").setValue((sdf.format(currenTimeZone)).toString());

                mDatabaseComments_ProfilePostWise.child("profileimage").setValue(dataSnapshot.child("image").getValue());
                mDatabaseComments_ProfilePostWise.child("username").setValue(dataSnapshot.child("name").getValue());
                mDatabaseComments_ProfilePostWise.child("random_uid").setValue(mCurrentUser.getUid());
                mDatabaseComments_ProfilePostWise.child("Comment").setValue(finalCmnt);
                mDatabaseComments_ProfilePostWise.child("ParentCmntKey").setValue(mParentCmntKey);
                mDatabaseComments_ProfilePostWise.child("Comment_time").setValue((sdf.format(currenTimeZone)).toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //==-----------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onStart() {
        super.onStart();

/* //For Reversing the order of Posts--------------------
        mLayoutManager = new LinearLayoutManager(new ProfilePage());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mCmntList.setLayoutManager(mLayoutManager);
//-----------------------------------------------------
*/

        FirebaseRecyclerAdapter<User_Data, CommentViewHolder> CmntAdapter = new FirebaseRecyclerAdapter<User_Data, CommentViewHolder>(
                User_Data.class,
                R.layout.comment_row,
                CommentViewHolder.class,
                mDatabaseComments
        ) {
            @Override
            protected void populateViewHolder(final CommentViewHolder viewHolder, User_Data model, int position) {

            final String Cmnt_key = getRef(position).getKey();

/*
                mDatabaseComments.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String random_uid = dataSnapshot.child("random_uid").getValue(String.class);
                        Toast.makeText(comment_page.this, random_uid, Toast.LENGTH_LONG).show();

                        mDatabaseUsers.child(random_uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                viewHolder.setUsername(dataSnapshot.child("name").getValue(String.class));
                                viewHolder.setProfileImage(getApplicationContext() , dataSnapshot.child("image").getValue(String.class));
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
*/
                mDatabaseComments.child(Cmnt_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       RandomCmntUser = dataSnapshot.child("random_uid").getValue(String.class);
                        if(mCurrentUser.getUid().equals(RandomCmntUser)){
                         //   Toast.makeText(comment_page.this,RandomCmntUser,Toast.LENGTH_LONG).show();
                            viewHolder.mDeleteComment.setVisibility(View.VISIBLE);
                        }else
                        {
                           //Toast.makeText(comment_page.this,"Value 1"+RandomCmntUser,Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mDatabaseAdminCmnt.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(mCurrentUser.getUid().equals(mPost_UserKey)){
                              //Toast.makeText(comment_page.this,mPost_UserKey,Toast.LENGTH_LONG).show();
                            viewHolder.mDeleteComment.setVisibility(View.VISIBLE);
                        }else
                        {
                          // Toast.makeText(comment_page.this,"Value 2"+RandomCmntUser,Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.setComment_time(model.getComment_time());
                viewHolder.setComment(model.getComment());
                viewHolder.setUsername(model.getUsername());
                viewHolder.setProfileImage(getApplicationContext(), model.getProfileimage());


                viewHolder.mDeleteComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(comment_page.this);
                        builder.setMessage("Clicking 'Yes' will Delete this Comment, Are you Sure?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mDatabaseComments.child(Cmnt_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot DeleteCmntdataSnapshot) {
                                                for (DataSnapshot dataSnapshot : DeleteCmntdataSnapshot.getChildren()) {
                                                    dataSnapshot.getRef().removeValue();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        Query ParentCmntKey_Value = mDatabaseComments_DeletePostWise.orderByChild("ParentCmntKey").equalTo(Cmnt_key);

                                        ParentCmntKey_Value.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot DeleteCmntProfilePostWisedataSnapshot) {
                                                for (DataSnapshot dataSnapshot : DeleteCmntProfilePostWisedataSnapshot.getChildren()) {
                                                    dataSnapshot.getRef().removeValue();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.setTitle("Delete Comment?");
                        dialog.show();
                    }
                });



                // You have set edit and delete button visibility to 'gone' ...
                // so next time you have to give privilages.. so that appropiate users can only view thse 2 button.
                //Also check if.. app is deleting cmnts from both post wise method and profilewise method.



            }
        };
        mCmntList.setAdapter(CmntAdapter);





    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        View mView;
        private TextView mDeleteComment;

        public CommentViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mDeleteComment = (TextView)mView.findViewById(R.id.DeleteComment);

        }

        public void setComment(String comment) {
            TextView cmnt = (TextView) mView.findViewById(R.id.list_cmnt_box);
            cmnt.setText(comment);
        }

        public void setComment_time(String commentTime) {
            TextView cmnt_time = (TextView) mView.findViewById(R.id.list_cmnt_time);
            cmnt_time.setText(commentTime);
        }

        public void setProfileImage(Context ctx, String pimage) {
            ImageView profileImage = (ImageView) mView.findViewById(R.id.list_cmnt_profileImage);
            Picasso.with(ctx).load(pimage).into(profileImage);
        }

        public void setUsername(String uname) {
            TextView username = (TextView) mView.findViewById(R.id.list_cmnt_username);
            username.setText(uname);
        }


    }

    public void DeleteComment(){

        mCurrentUser = mAuth.getCurrentUser();



    }

    @Override
    public void onBackPressed() {
     /*   if (mInterstitialAd6.isLoaded()) {
            mInterstitialAd6.show();
        } else {
            finish();
        }
    */
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}