package com.picbit.info.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Category_list extends AppCompatActivity {

    private DatabaseReference mDatabaseCategory;
    private ListView mCategoryList;
    private AdView mAdView;
    private CircleImageView mPhoto;
    private FirebaseUser mCurrentUser;
    private InterstitialAd mInterstitialAd6;
    private DatabaseReference mSampleDatabase;
    private FirebaseAuth mAuth;
    private ArrayList<String> mCategoryName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("PicBit");
        //  getActionBar().setIcon(R.drawable.my_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (AppStatus.getInstance(this).isOnline()) {
            mDatabaseCategory = FirebaseDatabase.getInstance().getReference().child("Category");
            mCategoryList = (ListView) findViewById(R.id.CategoryMenu_list);
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mCategoryName);

            mCategoryList.setAdapter(arrayAdapter);


            mAuth = FirebaseAuth.getInstance();
            mCurrentUser = mAuth.getCurrentUser();

            mPhoto = (CircleImageView) findViewById(R.id.CategoryUserPhoto);
            mSampleDatabase = FirebaseDatabase.getInstance().getReference();
            mSampleDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String pImage = (String) dataSnapshot.child("Users").child(mCurrentUser.getUid()).child("image").getValue();
                    Picasso.with(Category_list.this).load(pImage).into(mPhoto);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mDatabaseCategory.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String value = dataSnapshot.getValue(String.class);
                    mCategoryName.add(value);
                    arrayAdapter.notifyDataSetChanged();

                    mCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            String link = ((TextView) view).getText().toString();
                            Intent i = new Intent(getApplicationContext(), Category_wise_post.class);
                            i.putExtra("link", link);
                            startActivity(i);
                        }
                    });
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Category_list.this);
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


    @Override
    public void onBackPressed() {
finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}