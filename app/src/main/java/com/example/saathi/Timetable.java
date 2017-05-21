package com.example.saathi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.Arrays;
import java.util.Calendar;

public class Timetable extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId;
    private TextView greeting;
    private String[] userCourses;
    private String userTimeTable="";
    private String userInstructors="";
    private String userLocations="";
    private int numberOfCourses=0;
    private String mondayTT="";
    private String tuesdayTT="";
    private String wednesdayTT="";
    private String thursdayTT="";
    private String fridayTT="";
    private int mondayClassCounter=0;
    private int tuesdayClassCounter=0;
    private int wednesdayClassCounter=0;
    private int thursdayClassCounter=0;
    private int fridayClassCounter=0;
    private TextView dayClassCounter;
    private LinearLayout dayDetails;
    private String mondayClasses="";
    private String tuesdayClasses="";
    private String wednesdayClasses="";
    private String thursdayClasses="";
    private String fridayClasses="";
    private String mondayLocation="";
    private String tuesdayLocation="";
    private String wednesdayLocation="";
    private String thursdayLocation="";
    private String fridayLocation="";
    private String mondayTeacher="";
    private String tuesdayTeacher="";
    private String wednesdayTeacher="";
    private String thursdayTeacher="";
    private String fridayTeacher="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        greeting=(TextView) findViewById(R.id.timetableGreeting);
        dayClassCounter=(TextView) findViewById(R.id.timetableClasses);
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
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot d: dataSnapshot.child("Private User Data").child(mUserId).getChildren())
                    {
                        numberOfCourses++;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int k = 0;
                    userCourses = new String[numberOfCourses];
                    int courseCount = 0;
                    for (DataSnapshot d : dataSnapshot.child("Private User Data").child(mUserId).getChildren()) {
                        if (k == 0) {
                            Student c = d.getValue(Student.class);
                            String nameVal = c.getName();
                            String emailVal = mFirebaseAuth.getCurrentUser().getEmail();
                            userName.setText(nameVal);
                            userEmail.setText(emailVal);
                            greeting.setText("Hello, " + nameVal);
                            k++;
                        } else {
                            Courses userCourse = d.getValue(Courses.class);
                            String courseNumber = userCourse.getcourseCode();
                            userCourses[courseCount] = courseNumber;
                            courseCount++;
                        }
                    }

                    for (int i = 0; i < numberOfCourses; i++) {
                        for (DataSnapshot d : dataSnapshot.child("Course Description").getChildren()) {
                            Courses userCourse = d.getValue(Courses.class);
                            if (userCourse.getcourseCode().equalsIgnoreCase(userCourses[i])) {

                                userTimeTable=userTimeTable.concat(userCourse.getCourseTimeTable());
                                userTimeTable=userTimeTable.concat(";");
                                userInstructors=userInstructors.concat(userCourse.getCourseInstructor());
                                userInstructors=userInstructors.concat(";");
                                userLocations=userLocations.concat(userCourse.getCourseLocation());
                                userLocations=userLocations.concat(";");
                            }
                        }
                    }
                    if(!(userTimeTable.equals("")))
                        userTimeTable=userTimeTable.substring(0,userTimeTable.length()-1);
                    if(!(userLocations.equals("")))
                        userInstructors=userInstructors.substring(0,userInstructors.length()-1);
                    if(!(userLocations.equals("")))
                        userLocations=userLocations.substring(0,userLocations.length()-1);
                    String[] splittedArr=userTimeTable.split(";");
                    for(int x=0;x<splittedArr.length;x++)
                    {
                        String[] subSplittedArr=splittedArr[x].split(",");
                        for(int y=0;y<subSplittedArr.length;y++)
                        {
                            String[] finalArr=subSplittedArr[y].split(" ");
                            if(finalArr[0].equalsIgnoreCase("Monday"))
                            {
                                mondayTT=mondayTT.concat(finalArr[1]);
                                mondayTT=mondayTT.concat(";");
                                mondayClasses=mondayClasses.concat(userCourses[x]+" ");
                                mondayLocation=mondayLocation.concat(userLocations.split(";")[x]+"  ");
                                mondayTeacher=mondayTeacher.concat(userInstructors.split(";")[x]+"  ");
                                mondayClassCounter++;

                            }
                            else if(finalArr[0].equalsIgnoreCase("Tuesday"))
                            {
                                tuesdayTT=tuesdayTT.concat(finalArr[1]);
                                tuesdayTT=tuesdayTT.concat(";");
                                tuesdayClasses=tuesdayClasses.concat(userCourses[x]+" ");
                                tuesdayLocation=tuesdayLocation.concat(userLocations.split(";")[x]+"  ");
                                tuesdayTeacher=tuesdayTeacher.concat(userInstructors.split(";")[x]+"  ");
                                tuesdayClassCounter++;
                            }
                            else if(finalArr[0].equalsIgnoreCase("Wednesday"))
                            {
                                wednesdayTT=wednesdayTT.concat(finalArr[1]);
                                wednesdayTT=wednesdayTT.concat(";");
                                wednesdayClasses=wednesdayClasses.concat(userCourses[x]+" ");
                                wednesdayLocation=wednesdayLocation.concat(userLocations.split(";")[x]+"  ");
                                wednesdayTeacher=wednesdayTeacher.concat(userInstructors.split(";")[x]+"  ");
                                wednesdayClassCounter++;
                            }
                            else if(finalArr[0].equalsIgnoreCase("Thursday"))
                            {
                                thursdayTT=thursdayTT.concat(finalArr[1]);
                                thursdayTT=thursdayTT.concat(";");
                                thursdayClasses=thursdayClasses.concat(userCourses[x]+" ");
                                thursdayLocation=thursdayLocation.concat(userLocations.split(";")[x]+"  ");
                                thursdayTeacher=thursdayTeacher.concat(userInstructors.split(";")[x]+"  ");
                                thursdayClassCounter++;
                            }
                            else if(finalArr[0].equalsIgnoreCase("Friday"))
                            {
                                fridayTT=fridayTT.concat(finalArr[1]);
                                fridayTT=fridayTT.concat(";");
                                fridayClasses=fridayClasses.concat(userCourses[x]+" ");
                                fridayLocation=fridayLocation.concat(userLocations.split(";")[x]+"  ");
                                fridayTeacher=fridayTeacher.concat(userInstructors.split(";")[x]+"  ");
                                fridayClassCounter++;
                            }
                        }
                    }
                    if(!(mondayTT.equals("")))
                    {
                        mondayTT=mondayTT.substring(0,mondayTT.length()-1);
                    }
                    if(!(tuesdayTT.equals("")))
                    {
                        tuesdayTT=tuesdayTT.substring(0,tuesdayTT.length()-1);
                    }
                    if(!(wednesdayTT.equals("")))
                    {
                        wednesdayTT=wednesdayTT.substring(0,wednesdayTT.length()-1);
                    }
                    if(!(thursdayTT.equals("")))
                    {
                        thursdayTT=thursdayTT.substring(0,thursdayTT.length()-1);
                    }
                    if(!(fridayTT.equals("")))
                    {
                        fridayTT=fridayTT.substring(0,fridayTT.length()-1);
                    }


                    dayDetails=(LinearLayout) findViewById(R.id.timetableForTheDay);
                    int dayOfWeek=Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                    int ii=0;
                    if(dayOfWeek==Calendar.MONDAY)
                    {
                        dayClassCounter.setText("Today is Monday, you have "+mondayClassCounter+" classes today.");
                        String[] mondayClassNames=mondayClasses.split(" ");
                        String[] mondayClasstiming=mondayTT.split(";");
                        String[] mondayClassTeachers=mondayTeacher.split("  ");
                        String[] mondayClassLocations=mondayLocation.split("  ");
                        for(int i=0;i<mondayClassCounter;i++)
                        {

                            TextView tv=new TextView(Timetable.this);
                            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            tv.setTextSize(18);
                            tv.setTextColor(Color.parseColor("#303F9F"));
                            tv.setPadding(10,50,10,0);
                            tv.setText("You have "+mondayClassNames[i]+" from "+mondayClasstiming[i]+" taken by "+mondayClassTeachers[i]+" in "+mondayClassLocations[i]);
                            dayDetails.addView(tv);
                        }
                    }
                    else if(dayOfWeek==Calendar.TUESDAY)
                    {
                        dayClassCounter.setText("Today is Tuesday, you have "+tuesdayClassCounter+" classes today.");
                        String[] tuesdayClassNames=tuesdayClasses.split(" ");
                        String[] tuesdayClasstiming=tuesdayTT.split(";");
                        String[] tuesdayClassTeachers=tuesdayTeacher.split("  ");
                        String[] tuesdayClassLocations=tuesdayLocation.split("  ");
                        for(int i=0;i<tuesdayClassCounter;i++)
                        {

                            TextView tv=new TextView(Timetable.this);
                            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            tv.setText("You have "+tuesdayClassNames[i]+" from "+tuesdayClasstiming[i]+" taken by "+tuesdayClassTeachers[i]+" in "+tuesdayClassLocations[i]);
                            dayDetails.addView(tv);
                        }
                    }
                    else if(dayOfWeek==Calendar.WEDNESDAY)
                    {
                        dayClassCounter.setText("Today is Wednesday, you have "+wednesdayClassCounter+" classes today.");
                        String[] wednesdayClassNames=wednesdayClasses.split(" ");
                        String[] wednesdayClasstiming=wednesdayTT.split(";");
                        String[] wednesdayClassTeachers=wednesdayTeacher.split("  ");
                        String[] wednesdayClassLocations=wednesdayLocation.split("  ");
                        for(int i=0;i<wednesdayClassCounter;i++)
                        {

                            TextView tv=new TextView(Timetable.this);
                            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            tv.setText("You have "+wednesdayClassNames[i]+" from "+wednesdayClasstiming[i]+" taken by "+wednesdayClassTeachers[i]+" in "+wednesdayClassLocations[i]);
                            dayDetails.addView(tv);
                        }
                    }
                    else if(dayOfWeek==Calendar.THURSDAY)
                    {
                        dayClassCounter.setText("Today is Thursday, you have "+thursdayClassCounter+" classes today.");
                        String[] thursdayClassNames=thursdayClasses.split(" ");
                        String[] thursdayClasstiming=thursdayTT.split(";");
                        String[] thursdayClassTeachers=thursdayTeacher.split("  ");
                        String[] thursdayClassLocations=thursdayLocation.split("  ");
                        for(int i=0;i<thursdayClassCounter;i++)
                        {

                            TextView tv=new TextView(Timetable.this);
                            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            tv.setText("You have "+thursdayClassNames[i]+" from "+thursdayClasstiming[i]+" taken by "+thursdayClassTeachers[i]+" in "+thursdayClassLocations[i]);
                            dayDetails.addView(tv);
                        }
                    }
                    else if(dayOfWeek==Calendar.FRIDAY)
                    {
                        dayClassCounter.setText("Today is Friday, you have "+fridayClassCounter+" classes today.");
                        String[] fridayClassNames=fridayClasses.split(" ");
                        String[] fridayClasstiming=fridayTT.split(";");
                        String[] fridayClassTeachers=fridayTeacher.split("  ");
                        String[] fridayClassLocations=fridayLocation.split("  ");
                        for(int i=0;i<fridayClassCounter;i++)
                        {

                            TextView tv=new TextView(Timetable.this);
                            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            tv.setText("You have "+fridayClassNames[i]+" from "+fridayClasstiming[i]+" taken by "+fridayClassTeachers[i]+" in "+fridayClassLocations[i]);
                            dayDetails.addView(tv);
                        }
                    }
                    else if(dayOfWeek==Calendar.SATURDAY)
                    {
                        dayClassCounter.setText("Today is Saturday, enjoy the holiday.");
                    }
                    else if(dayOfWeek==Calendar.SUNDAY)
                    {
                        dayClassCounter.setText("Today is Sunday, enjoy the holiday.");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

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
            Intent intent=new Intent(Timetable.this,Main2Activity.class);
            startActivity(intent);
        } else if (id == R.id.courseRegister) {
            Intent intent=new Intent(Timetable.this,RegisterCourses.class);
            startActivity(intent);
        } else if (id == R.id.timetable) {

        } else if (id == R.id.toDo) {
            Intent intent=new Intent(Timetable.this,ToDo.class);
            startActivity(intent);
        } else if (id == R.id.profile) {
            Intent intent=new Intent(Timetable.this,Profile.class);
            startActivity(intent);
        } else if (id == R.id.friends) {
            Intent intent=new Intent(Timetable.this,Friends.class);
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
