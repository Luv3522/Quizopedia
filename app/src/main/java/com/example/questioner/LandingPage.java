package com.example.questioner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;

public class LandingPage extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    ViewPager viewPager;
    ArrayList<Integer> arraylist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        ImageView begin = findViewById(R.id.letsBegin);
        ImageView more=findViewById(R.id.moreLanding);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup=new PopupMenu(LandingPage.this,view);
                popup.setOnMenuItemClickListener(LandingPage.this);
                popup.inflate(R.menu.landingmenu);
                popup.show();
            }
        });



        viewPager=findViewById(R.id.viewPager);
        arraylist.add(R.drawable.f1);
        arraylist.add(R.drawable.f4);
        arraylist.add(R.drawable.f__);
        arraylist.add(R.drawable.f___);
        arraylist.add(R.drawable.q4);


        Adapter adapter=new Adapter(LandingPage.this,arraylist);
        viewPager.setAdapter(adapter);




        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(LandingPage.this,TopicActivity.class);
                startActivity(in);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.landingmenu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch(menuItem.getItemId()){
            case R.id.item1:
                startActivity(new Intent(LandingPage.this,About_New.class));
                return true;
            case R.id.item2:
                startActivity(new Intent(LandingPage.this,LoginActivity.class));
                return true;
            default:
                return false;
        }
    }




}