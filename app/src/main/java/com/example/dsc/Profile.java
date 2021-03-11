package com.example.dsc;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class Profile extends AppCompatActivity {
    ImageView coverPhoto, profile;
    EditText name, gender, location, phone;
    Button save;
    FirebaseFirestore fstore;
    FirebaseAuth fbaseAuth;
    String userID;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_profile );

        coverPhoto = findViewById( R.id.cover );
        profile = findViewById(R.id.picture);
        name = findViewById(R.id.name);
        gender = findViewById(R.id.gender);
        location = findViewById(R.id.location);
        phone = findViewById(R.id.phone);
        save = findViewById(R.id.save);

        fbaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        coverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 1000);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 1001);
            }
        });

        StorageReference coverRef = storageReference.child("users/cover/" + fbaseAuth.getCurrentUser().getUid()   + "coverphoto.jpg");
        coverRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(coverPhoto);

            }
        });


       StorageReference photoRef = storageReference.child("users/cover/" + fbaseAuth.getCurrentUser().getUid()   + "coverphoto.jpg");
        photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profile);

            }
        });




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString();
                String userGender = gender.getText().toString();
                String userLocation = location.getText().toString();
                String userPhone = phone.getText().toString();

                if (userName.isEmpty() || userGender.isEmpty() || userLocation.isEmpty() || userPhone.isEmpty()) {
                    Toast.makeText( Profile.this, "Please fill all the fields", Toast.LENGTH_SHORT ).show();
                }

                else{
                    userID = fbaseAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fstore.collection("users").document(String.valueOf(userID));
                    Map<String,Object> user = new HashMap<>();
                    user.put("fName", userName);
                    user.put("gender", userGender);
                    user.put("location", userLocation);
                    user.put("phone", userPhone);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText( Profile.this, "Profile Updated", Toast.LENGTH_SHORT ).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
          if(requestCode == 1000){
                if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                coverPhoto.setImageURI(imageUri);
                uploadCoverToFirebase(imageUri);
            }

          if(requestCode == 1001){
                if(resultCode == Activity.RESULT_OK) {
                    Uri imageUri = data.getData();
                    profile.setImageURI(imageUri);
                    uploadProfileToFirebase(imageUri);
                }
          }
       }
    }

   private void uploadCoverToFirebase(Uri imageUri) {
        final StorageReference fileRef = storageReference.child("users/cover/" +fbaseAuth.getCurrentUser().getUid()   + "coverphoto.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri){
                        Picasso.get().load(uri).into(coverPhoto);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText( Profile.this, "Failed", Toast.LENGTH_SHORT ).show();
            }
        });
    }

    private void uploadProfileToFirebase(Uri imageUri) {
        final StorageReference fileRef = storageReference.child("users/cover/" + fbaseAuth.getCurrentUser().getUid() + "coverphoto.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri){
                        Picasso.get().load(uri).into(profile);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText( Profile.this, "Failed", Toast.LENGTH_SHORT ).show();
            }
        });
    }
}