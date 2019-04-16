package com.example.asus.chatoffice;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.chatoffice.FireBase.Reference_Fire_Base;
import com.example.asus.chatoffice.Fragmentos.fragment_trabajo;
import com.example.asus.chatoffice.Objetos.Organizacion;
import com.example.asus.chatoffice.Objetos.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.navigation_chat:

                        Listado_Proyectos cht = new Listado_Proyectos();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fm_principal,cht).commit();
                        return true;
                    case R.id.navigation_trabajo:
                        fragment_trabajo in = new fragment_trabajo();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fm_principal,in).commit();
                        return true;
                    case R.id.navigation_reunion:
                        MainReuniones mr = new MainReuniones();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fm_principal,mr).commit();
                        return true;
                }
                return false;

            }
        };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        //-------------------------------------------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View vw_header = navigationView.getHeaderView(0);

        final TextView tv_nombre = vw_header.findViewById(R.id.tv_nombre_activity_drawer);
        final TextView tv_correo = vw_header.findViewById(R.id.tv_correo_activiti_drawer);


        tv_correo.setText(mAuth.getCurrentUser().getEmail());

        DatabaseReference dataRef = database.getReference(Reference_Fire_Base.REFERENCE_DATABASE_GUARDAR_USUARIO+"/"+FirebaseAuth.getInstance().getUid());
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                tv_nombre.setText(usuario.getSt_apallido()+" "+usuario.getSt_nombre());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        //-------------------------------------------------------------------------------------

        BottomNavigationView navigation =  findViewById(R.id.navegationBottom);


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Listado_Proyectos lm = new Listado_Proyectos();
        getSupportFragmentManager().beginTransaction().replace(R.id.fm_principal,lm).commit();

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
        getMenuInflater().inflate(R.menu.main_activity_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
         if(id == R.id.action_settings){

             Intent intent = new Intent(getApplicationContext(),MainConfiguracion.class);
             startActivityForResult(intent,0);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent;
        switch (id){
            case R.id.nav_camera:break;
            case R.id.nav_gallery:break;
            case R.id.nav_slideshow:break;
            case R.id.nav_manage:                   intent = new Intent(getApplicationContext(),MainConfiguracion.class);
                                                    startActivityForResult(intent,0);
                                                    break;
            case R.id.nav_share:break;
            case R.id.nav_cerrar_sesion:            mAuth.signOut();
                                                    intent = new Intent(getApplicationContext(),ActivityLogeoUsuario.class);
                                                    startActivity(intent);
                                                    finish();
                                                    break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
