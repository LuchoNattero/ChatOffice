package com.example.asus.chatoffice;


import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.asus.chatoffice.Fragmentos.fragment_trabajo;

public class MainActivity extends AppCompatActivity {



        private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.navigation_chat:
//                        Toast.makeText(MainActivity.this,"chat",Toast.LENGTH_LONG).show();
                        chat cht = new chat();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fm_principal,cht).commit();
                        return true;
                    case R.id.navigation_trabajo:
                      //  Toast.makeText(MainActivity.this,"configuracion",Toast.LENGTH_LONG).show();
                        fragment_trabajo in = new fragment_trabajo();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fm_principal,in).commit();
                        return true;
                    case R.id.navigation_reunion:
                        Toast.makeText(MainActivity.this,"reunion",Toast.LENGTH_LONG).show();
                        return true;

                }
                return false;

            }
        };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation =  findViewById(R.id.navegationBottom);
//
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.tb_main_activity);
//        setSupportActionBar(myToolbar);


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        chat lm = new chat();
        getSupportFragmentManager().beginTransaction().replace(R.id.fm_principal,lm).commit();

    }

}
