package com.example.saathi;

import android.content.Intent;
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

public class RegisterCourses extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button add = (Button) findViewById(R.id.buttonAdd);
        Button remove = (Button) findViewById(R.id.buttonRemove);
        final EditText addCourseNumber = (EditText) findViewById(R.id.editTextCourseNumber);
        final EditText addCourseName = (EditText) findViewById(R.id.editTextCourseName);
        final EditText addCourseCreadits = (EditText) findViewById(R.id.editTextCredits);
        final EditText removeNumber = (EditText)findViewById(R.id.editTextRemoveCourseNumber);
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

        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            loadLogInView();
        } else {
            mUserId = mFirebaseUser.getUid();
            //userName.setText(mUserId);
            //a = (TextView) findViewById(R.id.val);
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //int k = 0;
                    for (DataSnapshot d : dataSnapshot.child("Private User Data").child(mUserId).getChildren()) {

                        userName.setText("aa");
                        userEmail.setText("bb");
                        Student c = d.getValue(Student.class);
                        String nameVal = c.getName();
                        //Log.d("name: ", nameVal);
                        String emailVal = mFirebaseAuth.getCurrentUser().getEmail();
                        //Log.d("email id: ", emailVal);

                        userName.setText(nameVal);
                        userEmail.setText(emailVal);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = addCourseNumber.getText().toString().trim();
                String name = addCourseName.getText().toString().trim();
                String cred = addCourseCreadits.getText().toString().trim();
                Courses ob = new Courses();

                ob.setCourseName(name);
                ob.setcourseCode(number);
                ob.setcourseCredits(cred);

                mDatabase.child("Private User Data").child(mUserId).push().setValue(ob);
                addCourseName.setText("");
                addCourseNumber.setText("");
                addCourseCreadits.setText("");
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserId=mFirebaseUser.getUid();
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String user_input=removeNumber.getText().toString();
                        removeNumber.setText("");
                        //boolean flag=true;
                        int counter = 0;
                        for(DataSnapshot d: dataSnapshot.child("Private User Data").child(mUserId).getChildren())
                        {
                            if(counter == 0)
                            {
                                counter = counter + 1;
                            }
                            else {


                                Courses c = d.getValue(Courses.class);
                                String a = c.getcourseCode();

                                if (a.equals(user_input)) {
                                    d.getRef().removeValue();


                                }
                                counter = counter + 1;

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
            Intent intent=new Intent(RegisterCourses.this,Main2Activity.class);
            startActivity(intent);
        } else if (id == R.id.courseRegister) {

        } else if (id == R.id.timetable) {
            Intent intent=new Intent(RegisterCourses.this,Timetable.class);
            startActivity(intent);
        } else if (id == R.id.toDo) {
            Intent intent=new Intent(RegisterCourses.this,ToDo.class);
            startActivity(intent);
        } else if (id == R.id.profile) {
            Intent intent=new Intent(RegisterCourses.this,Profile.class);
            startActivity(intent);
        } else if (id == R.id.friends) {
            Intent intent=new Intent(RegisterCourses.this,Friends.class);
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
