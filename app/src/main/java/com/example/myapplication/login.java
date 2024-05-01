package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity {


    EditText username , password;
    Button loginbtn;
    DBHelper Db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);


       loginbtn = (Button) findViewById(R.id.loginbtn);
       username = (EditText) findViewById(R.id.editTextTextEmailAddress2);
       password = (EditText) findViewById(R.id.editTextTextPassword3);
       Db = new DBHelper(this);

       loginbtn.setOnClickListener(new View.OnClickListener(){

           @Override
           public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if(user.equals("") || pass.equals("")){
                    Toast.makeText(login.this,"Fill All The Fields",Toast.LENGTH_SHORT).show();
                }
                else{
                    Boolean checkuser = Db.checkusernamepassword(user,pass);
                    if(checkuser){
                        Toast.makeText(login.this,"Login Successful",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(login.this,"Invalid Credentials",Toast.LENGTH_SHORT).show();
                    }
                }
           }
       });

    }
}
