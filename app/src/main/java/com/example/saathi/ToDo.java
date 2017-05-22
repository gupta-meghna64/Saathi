package com.example.saathi;

import android.content.Intent;
import android.media.Image;
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
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.w3c.dom.Text;

public class ToDo extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private TextView testTextView;
    private ImageView check;
    private Button but;
    private DatabaseReference mDatabase;
    private TextView taskView1, taskView2, taskView3, taskView4, taskView5, taskView6;
    private Button done1, done2, done3, done4, done5, done6;
    private String mUserId;
    //static int count;
    int num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final EditText taskName = (EditText) findViewById(R.id.addTask);
        final EditText taskNumber = (EditText) findViewById(R.id.editTextTaskNumber);
        Button buttonAddTask = (Button) findViewById(R.id.buttonAddTask);

        final int[] arrayTextViews = {R.id.textViewTask1, R.id.textViewTask2, R.id.textViewTask3, R.id.textViewTask4, R.id.textViewTask5, R.id.textViewTask6};
        final int[] arrayButtons = {R.id.buttonDoneTask1, R.id.buttonDoneTask2, R.id.buttonDoneTask3, R.id.buttonDoneTask4, R.id.buttonDoneTask5, R.id.buttonDoneTask6};
        final int[] arrayImageView = {R.id.assignment1, R.id.assignment2, R.id.assignment3, R.id.assignment4, R.id.assignment5, R.id.assignment6};
        Button done1 = (Button)findViewById(R.id.buttonDoneTask1);
        Button done2 = (Button)findViewById(R.id.buttonDoneTask2);
        Button done3 = (Button)findViewById(R.id.buttonDoneTask3);
        Button done4 = (Button)findViewById(R.id.buttonDoneTask4);
        Button done5 = (Button)findViewById(R.id.buttonDoneTask5);
        Button done6 = (Button)findViewById(R.id.buttonDoneTask6);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(android.R.drawable.ic_lock_power_off);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseAuth.signOut();
                loadLogInView();
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
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int k = 0;
                    for (DataSnapshot d : dataSnapshot.child("Private User Data").child(mUserId).getChildren()) {
                        if (k == 0) {
                            Student c = d.getValue(Student.class);
                            String nameVal = c.getName();
                            String emailVal = mFirebaseAuth.getCurrentUser().getEmail();
                            userName.setText(nameVal);
                            userEmail.setText(emailVal);
                            k++;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int k=0;
                    for(DataSnapshot d: dataSnapshot.child("Tasks").child(mUserId).getChildren())
                    {
                        Task c = d.getValue(Task.class);
                        testTextView = (TextView) findViewById(arrayTextViews[k]);
                        but = (Button) findViewById(arrayButtons[k]);
                        check = (ImageView) findViewById(arrayImageView[k]);
                        testTextView.setText(c.getTaskName());
                        but.setVisibility(View.VISIBLE);
                        check.setVisibility(View.VISIBLE);
                        k++;
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = taskName.getText().toString().trim();
                String number = taskNumber.getText().toString().trim();
                int numberInt = Integer.parseInt(number);
                Task ob = new Task();
                ob.setTaskName(name);

                ob.setTaskNumber(Integer.toString(numberInt));
                mDatabase.child("Tasks").child(mUserId).push().setValue(ob);
                //count++;
                taskName.setText("");
                taskNumber.setText("");
            }
        });

        View.OnClickListener myHandler1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 1;
                mUserId=mFirebaseUser.getUid();
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String user_input=taskName.getText().toString();
                        taskName.setText("");
                        boolean flag=true;
                        for(DataSnapshot d: dataSnapshot.child("Tasks").child(mUserId).getChildren())
                        {
                            Task c = d.getValue(Task.class);
                            String a = c.getTaskName();
                            int b = Integer.parseInt(c.getTaskNumber());
                            if(flag == false)
                            {
                                Task updatedPlant=new Task();
                                updatedPlant.setTaskName(a);
                                updatedPlant.setTaskNumber(Integer.toString(b-1));
                                String newKey=d.getKey();
                                mDatabase.child("Tasks").child(mUserId).child(newKey).setValue(updatedPlant);
                            }
                            if(b == num)
                            {
                                d.getRef().removeValue();
                                flag = false;
                            }


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        View.OnClickListener myHandler2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 2;
                mUserId=mFirebaseUser.getUid();
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String user_input=taskName.getText().toString();
                        taskName.setText("");
                        boolean flag=true;
                        for(DataSnapshot d: dataSnapshot.child("Tasks").child(mUserId).getChildren())
                        {
                            Task c = d.getValue(Task.class);
                            String a = c.getTaskName();
                            int b = Integer.parseInt(c.getTaskNumber());
                            if(flag == false)
                            {
                                Task updatedPlant=new Task();
                                updatedPlant.setTaskName(a);
                                updatedPlant.setTaskNumber(Integer.toString(b-1));
                                String newKey=d.getKey();
                                mDatabase.child("Tasks").child(mUserId).child(newKey).setValue(updatedPlant);
                            }
                            if(b == num)
                            {
                                d.getRef().removeValue();
                                flag = false;
                            }


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        View.OnClickListener myHandler3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 3;
                mUserId=mFirebaseUser.getUid();
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String user_input=taskName.getText().toString();
                        taskName.setText("");
                        boolean flag=true;
                        for(DataSnapshot d: dataSnapshot.child("Tasks").child(mUserId).getChildren())
                        {
                            Task c = d.getValue(Task.class);
                            String a = c.getTaskName();
                            int b = Integer.parseInt(c.getTaskNumber());
                            if(flag == false)
                            {
                                Task updatedPlant=new Task();
                                updatedPlant.setTaskName(a);
                                updatedPlant.setTaskNumber(Integer.toString(b-1));
                                String newKey=d.getKey();
                                mDatabase.child("Tasks").child(mUserId).child(newKey).setValue(updatedPlant);
                            }
                            if(b == num)
                            {
                                d.getRef().removeValue();
                                flag = false;
                            }


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        View.OnClickListener myHandler4 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 4;
                mUserId=mFirebaseUser.getUid();
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String user_input=taskName.getText().toString();
                        taskName.setText("");
                        boolean flag=true;
                        for(DataSnapshot d: dataSnapshot.child("Tasks").child(mUserId).getChildren())
                        {
                            Task c = d.getValue(Task.class);
                            String a = c.getTaskName();
                            int b = Integer.parseInt(c.getTaskNumber());
                            if(flag == false)
                            {
                                Task updatedPlant=new Task();
                                updatedPlant.setTaskName(a);
                                updatedPlant.setTaskNumber(Integer.toString(b-1));
                                String newKey=d.getKey();
                                mDatabase.child("Tasks").child(mUserId).child(newKey).setValue(updatedPlant);
                            }
                            if(b == num)
                            {
                                d.getRef().removeValue();
                                flag = false;
                            }


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        View.OnClickListener myHandler5 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 5;
                mUserId=mFirebaseUser.getUid();
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String user_input=taskName.getText().toString();
                        taskName.setText("");
                        boolean flag=true;
                        for(DataSnapshot d: dataSnapshot.child("Tasks").child(mUserId).getChildren())
                        {
                            Task c = d.getValue(Task.class);
                            String a = c.getTaskName();
                            int b = Integer.parseInt(c.getTaskNumber());
                            if(flag == false)
                            {
                                Task updatedPlant=new Task();
                                updatedPlant.setTaskName(a);
                                updatedPlant.setTaskNumber(Integer.toString(b-1));
                                String newKey=d.getKey();
                                mDatabase.child("Tasks").child(mUserId).child(newKey).setValue(updatedPlant);
                            }
                            if(b == num)
                            {
                                d.getRef().removeValue();
                                flag = false;
                            }


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        View.OnClickListener myHandler6 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 6;
                mUserId=mFirebaseUser.getUid();
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String user_input=taskName.getText().toString();
                        taskName.setText("");
                        boolean flag=true;
                        for(DataSnapshot d: dataSnapshot.child("Tasks").child(mUserId).getChildren())
                        {
                            Task c = d.getValue(Task.class);
                            String a = c.getTaskName();
                            int b = Integer.parseInt(c.getTaskNumber());
                            if(flag == false)
                            {
                                Task updatedPlant=new Task();
                                updatedPlant.setTaskName(a);
                                updatedPlant.setTaskNumber(Integer.toString(b-1));
                                String newKey=d.getKey();
                                mDatabase.child("Tasks").child(mUserId).child(newKey).setValue(updatedPlant);
                            }
                            if(b == num)
                            {
                                d.getRef().removeValue();
                                flag = false;
                            }


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        done1.setOnClickListener(myHandler1);
        done2.setOnClickListener(myHandler2);
        done3.setOnClickListener(myHandler3);
        done4.setOnClickListener(myHandler4);
        done5.setOnClickListener(myHandler5);
        done6.setOnClickListener(myHandler6);



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
            Intent intent=new Intent(ToDo.this,Main2Activity.class);
            startActivity(intent);
        } else if (id == R.id.courseRegister) {
            Intent intent=new Intent(ToDo.this,RegisterCourses.class);
            startActivity(intent);
        } else if (id == R.id.timetable) {
            Intent intent=new Intent(ToDo.this,Timetable.class);
            startActivity(intent);
        } else if (id == R.id.toDo) {

        } else if (id == R.id.profile) {
            Intent intent=new Intent(ToDo.this,Profile.class);
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
