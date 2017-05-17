package com.example.saathi;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Signup extends AppCompatActivity {

    protected EditText passwordEditText;
    protected EditText emailEditText;
    protected Button signUpButton;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private TextView a;
    private static int RESULT_LOAD_IMAGE = 1;
    private Uri selectedImage;
    private StorageReference mStorage;
    protected EditText name;
    protected EditText rollNumber;
    protected EditText contactNumber;
    protected EditText major;
    protected EditText batch;
    private String imgId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        passwordEditText = (EditText)findViewById(R.id.password);
        emailEditText = (EditText)findViewById(R.id.username);
        name = (EditText)findViewById(R.id.name);
        rollNumber =(EditText)findViewById(R.id.rollNumber);
        contactNumber = (EditText)findViewById(R.id.contactNumber);
        major =(EditText)findViewById(R.id.major);
        batch = (EditText)findViewById(R.id.batch);

        signUpButton = (Button)findViewById(R.id.signup);
        mStorage = FirebaseStorage.getInstance().getReference();
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = passwordEditText.getText().toString();
                String email = emailEditText.getText().toString();
                final String Name = name.getText().toString().trim();
                final String RollNumber = rollNumber.getText().toString().trim();
                final String ContactNumber = contactNumber.getText().toString().trim();
                final String Major = major.getText().toString().trim();
                final String Batch = batch.getText().toString().trim();



                password = password.trim();
                email = email.trim();

                if (password.isEmpty() || email.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                    builder.setMessage("Error")
                            .setTitle("Error")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Student student = new Student();
                                        student.setName(Name);
                                        student.setRollNo(RollNumber);
                                        student.setBatch(Batch);
                                        student.setImage(imgId);
                                        student.setMajor(Major);
                                        student.setMobile(ContactNumber);
                                        student.setEmail(emailEditText.getText().toString().trim());
                                        mDatabase.child("Private User Data").child(mFirebaseAuth.getCurrentUser().getUid()).push().setValue(student);
                                        Intent intent = new Intent(Signup.this, Main2Activity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                                        builder.setMessage(task.getException().getMessage())
                                                .setTitle("Error")
                                                .setPositiveButton(android.R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                }
                            });
                }



            }
        });
        Button loadImage = (Button) findViewById(R.id.imageUpload);
        loadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.singuphome);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadLogInView();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            ImageView imgView = (ImageView)findViewById(R.id.imageDisplay);
            imgView.setImageURI(selectedImage);
            //name.setText(selectedImage.getLastPathSegment());
            imgId = selectedImage.getLastPathSegment();
            StorageReference filepath = mStorage.child("Photos").child(selectedImage.getLastPathSegment());
            filepath.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Signup.this, "Upload Done! ", Toast.LENGTH_LONG).show();
                }
            });
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
//
//            ImageView imageView = (ImageView) findViewById(R.id.imageDisplay);
//            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }



    private void loadLogInView() {
        Intent intent = new Intent(this, Startup.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


}




 
