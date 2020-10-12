package com.pinalopes.informationspositives.Home.Model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.pinalopes.informationspositives.R;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ImageView iconMenu = findViewById(R.id.icon_menu);
        iconMenu.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
    }
}