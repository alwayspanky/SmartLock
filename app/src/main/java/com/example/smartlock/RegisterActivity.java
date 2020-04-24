package com.example.smartlock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText email, pass, cnfPass;
    private TextView account;
    private Button register;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressBar = new ProgressBar(this);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.txtemail);
        pass = findViewById(R.id.txtpass);
        cnfPass = findViewById(R.id.txtcnfpass);
        account = findViewById(R.id.txtAccount);
        register = findViewById(R.id.btnRegister);

        registerUser();
    }

    private void registerUser() {

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String Email = email.getText().toString().trim();
                String password = pass.getText().toString().trim();

                mAuth.createUserWithEmailAndPassword(Email,password).
                        addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this,"Registration Successfully", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }else{
                            Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }


}
