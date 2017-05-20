package com.example.saathi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;
    private EditText userFullName, userEmailID, userMobile,userBatch, userMajor,userRollNo;
    private Button updateUserInfo,saveUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userFullName=(EditText) findViewById(R.id.personName);
        userEmailID=(EditText) findViewById(R.id.personEmail);
        userBatch=(EditText) findViewById(R.id.personBatchYear);
        userMajor=(EditText) findViewById(R.id.personMajor);
        userMobile=(EditText) findViewById(R.id.personContact);
        userRollNo=(EditText) findViewById(R.id.personRollNo);
        updateUserInfo=(Button) findViewById(R.id.updateUserData);
        saveUserInfo=(Button) findViewById(R.id.saveUserData);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        final TextView userName = (TextView) headerView.findViewById(R.id.textViewNameDisplay);
        final TextView userEmail = (TextView) headerView.findViewById(R.id.textViewEmailAddress);

            mUserId = mFirebaseUser.getUid();
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int k = 0;
                    for (DataSnapshot d : dataSnapshot.child("Private User Data").child(mUserId).getChildren()) {
                        if(k==0)
                        {
                            Student currentStudent = d.getValue(Student.class);
                            String nameVal = currentStudent.getName();
                            String emailVal = mFirebaseAuth.getCurrentUser().getEmail();
                            String batch=currentStudent.getBatch();
                            String rollNo=currentStudent.getRollNo();
                            String major=currentStudent.getMajor();
                            String mobile=currentStudent.getMobile();
                            userName.setText(nameVal);
                            userEmail.setText(emailVal);
                            userFullName.setText(nameVal);
                            userEmailID.setText(emailVal);
                            userBatch.setText(batch);
                            userRollNo.setText(rollNo);
                            userMajor.setText(major);
                            userMobile.setText(mobile);
                            k++;

                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        updateUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int k = 0;
                        for (DataSnapshot d : dataSnapshot.child("Private User Data").child(mUserId).getChildren()) {
                            if(k==0)
                            {
                                String userDetailsKey=d.getKey();
                                Student updatedStudent=new Student();
                                updatedStudent.setBatch(userBatch.getText().toString());
                                updatedStudent.setEmail(userEmailID.getText().toString());
                                updatedStudent.setMajor(userMajor.getText().toString());
                                updatedStudent.setMobile(userMobile.getText().toString());
                                updatedStudent.setName(userFullName.getText().toString());
                                updatedStudent.setRollNo(userRollNo.getText().toString());
                                updatedStudent.setImage(d.getValue(Student.class).getImage());
                                mDatabase.child("Private User Data").child(mUserId).child(userDetailsKey).setValue(updatedStudent);
                                k++;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent intent=new Intent(Profile.this,Main2Activity.class);
            startActivity(intent);
        } else if (id == R.id.courseRegister) {
            Intent intent=new Intent(Profile.this,RegisterCourses.class);
            startActivity(intent);
        } else if (id == R.id.timetable) {
            Intent intent=new Intent(Profile.this,Timetable.class);
            startActivity(intent);
        } else if (id == R.id.toDo) {
            Intent intent=new Intent(Profile.this,ToDo.class);
            startActivity(intent);
        } else if (id == R.id.profile) {

        } else if (id == R.id.friends) {
            Intent intent=new Intent(Profile.this,Friends.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadLogInView() {
        Intent intent = new Intent(this, Startup.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
