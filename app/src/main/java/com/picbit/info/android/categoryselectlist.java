package com.picbit.info.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
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

public class categoryselectlist extends AppCompatActivity {

    private DatabaseReference mDatabaseCategory;
    private ListView mUploadCategoryList;
    private CircleImageView mPhoto;
    private FirebaseUser mUploadCurrentUser;
    private com.google.firebase.database.DatabaseReference mSampleDatabase;
    private FirebaseAuth mAuth;
    private ArrayList<String> mUploadCategoryName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoryselectlist);
        if (AppStatus.getInstance(this).isOnline()) {

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            setTitle("PicBit");
            //  getActionBar().setIcon(R.drawable.my_icon);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            mDatabaseCategory = FirebaseDatabase.getInstance().getReference().child("Category");
            mUploadCategoryList = (ListView) findViewById(R.id.Upload_Category_list);
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mUploadCategoryName);

            mUploadCategoryList.setAdapter(arrayAdapter);

            //final ListView lv = mCategoryList;

            mAuth = FirebaseAuth.getInstance();
            mUploadCurrentUser = mAuth.getCurrentUser();

            mPhoto = (CircleImageView) findViewById(R.id.Upload_Category_UserImage);
            mSampleDatabase = FirebaseDatabase.getInstance().getReference();
            mSampleDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String pImage = (String) dataSnapshot.child("Users").child(mUploadCurrentUser.getUid()).child("image").getValue();
                    Picasso.with(categoryselectlist.this).load(pImage).into(mPhoto);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mDatabaseCategory.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String value = dataSnapshot.getValue(String.class);
                    mUploadCategoryName.add(value);
                    arrayAdapter.notifyDataSetChanged();

                    mUploadCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                            String passName = ((TextView) view).getText().toString();
                            Intent finalCategoryName = new Intent(getApplicationContext(), PostActivity.class);
                            finalCategoryName.putExtra("category_name_final", passName);
                            startActivity(finalCategoryName);
                            // Toast.makeText(Category_list.this,link,Toast.LENGTH_LONG).show();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(categoryselectlist.this);
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