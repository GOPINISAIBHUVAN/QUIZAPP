package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class signup extends AppCompatActivity {

    EditText name, password, repassword, phonenumber, email;
    DBHelper Db;
    Button signin,existinguser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        name = (EditText) findViewById(R.id.editTextText);
        email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        phonenumber = (EditText) findViewById(R.id.editTextPhone);
        password = (EditText) findViewById(R.id.editTextTextPassword);
        repassword = (EditText) findViewById(R.id.editTextTextPassword2);

        Db = new DBHelper(this);
        signin = (Button) findViewById(R.id.signup);
        existinguser = (Button) findViewById(R.id.existinguser1);

        existinguser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , login.class);
                startActivity(intent);
                // Finish MainActivity
                finish();
            }
        });


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = name.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();
                String mail = email.getText().toString();
                String number = phonenumber.getText().toString();

                if(user.equals("") || pass.equals("") || number.length() != 10){
                    Toast.makeText(signup.this,"Please enter the details properly",Toast.LENGTH_SHORT).show();

                }
                else{
                    if(pass.equals(repass)){
                        Boolean checkuser = Db.checkusername(user);
                        if(!checkuser){
                            Boolean insert = Db.insertData(user,pass);
                            if(insert){
                                Toast.makeText(signup.this,"Registered Successful",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext() , login.class);
                                startActivity(intent);
                                // Finish MainActivity
                                finish();
                            }
                            else{
                                Toast.makeText(signup.this,"Register Fail",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(signup.this,"User Exists ! Try Again ",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(signup.this,"Passwords Not Matched",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}