package com.example.dsc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    EditText emaildId, password;
    Button loginbtn;
    FirebaseAuth fbaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );


        emaildId = findViewById( R.id.email );
        password = findViewById( R.id.password );
        loginbtn = findViewById( R.id.button );
        fbaseAuth = FirebaseAuth.getInstance();


        loginbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emaildId.getText().toString();
                String pwd = password.getText().toString();
                if (email.isEmpty()){
                    emaildId.setError( "Please enter the email id" );
                    emaildId.requestFocus();
                }
                else if (pwd.isEmpty()){
                    password.setError( "Please enter your password" );
                    password.requestFocus();
                }
                else if (email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(MainActivity.this,"Fields are Empty",Toast.LENGTH_SHORT).show();
                }


                fbaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, Profile.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MainActivity.this, "Error Occurred!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } );

        textView = (TextView)findViewById( R.id.tv2 );
        textView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity( intent );
                overridePendingTransition( android.R.anim.fade_in,0 );

            }
        } );

    }


}
