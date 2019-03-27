package com.example.asus.chatoffice;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ActivityLogeoUsuario extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    EditText et_email,et_pass;
    Button bt_ingresar,bt_registrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logeo_usuario);

        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usr = firebaseAuth.getCurrentUser();

                if(usr != null){

                    if(usr.isEmailVerified()) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {

                        Toast.makeText(getApplicationContext(),"Usted debe verificar el correo",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        };


        et_email = findViewById(R.id.et_usuario_logeo_usuario);
        et_pass = findViewById(R.id.et_contrasenia_logeo_usuario);
        bt_ingresar = findViewById(R.id.bt_iniciar_sesion_logeo_usuario);
        bt_registrar = findViewById(R.id.bt_registrar_logeo_usuario);


        bt_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityLogeoUsuario.this,MainRegistraUsuario.class);
                startActivity(intent);
                finish();

            }
        });


        bt_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String st_email = et_email.getText().toString();
                String st_pass = et_pass.getText().toString();
                if(!camposCompletos()) {
                    Toast.makeText(getApplicationContext(),"Complete ambos campos",Toast.LENGTH_SHORT).show();
                }

                else{
                    mAuth.signInWithEmailAndPassword(st_email, st_pass).addOnCompleteListener(ActivityLogeoUsuario.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {

                                Toast.makeText(getApplicationContext(), "Correo o Contraseña incorrecta", Toast.LENGTH_SHORT).show();

                            } /*else {


                                Toast.makeText(getApplicationContext(), "usted accedio", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra(Reference.USUARIO, usuario);
                                startActivity(intent);
                                finish();

                            }*/
                        }
                    });
                }
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (authStateListener != null){
            mAuth.removeAuthStateListener(authStateListener);
        }

    }

    boolean camposCompletos(){

       boolean correcto = true;

       if(et_email.getText().toString().isEmpty()){

            correcto = false;
       }
       else if(et_pass.getText().toString().isEmpty()){

           correcto = false;
       }
        return correcto;
    }
}
