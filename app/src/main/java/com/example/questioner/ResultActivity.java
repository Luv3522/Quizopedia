package com.example.questioner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView textView = findViewById(R.id.marks);

//        int score = Integer.parseInt(getIntent().getAction());
        textView.setText(getIntent().getAction());
       ImageView img = findViewById(R.id.firePng);
       img.setImageResource(R.drawable.firepng);
//        if(score<3){
//            img.setImageResource(R.id.leets);
//        }
//        else if (score>=3 &7 )
        findViewById(R.id.backResult).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResultActivity.this,TopicActivity.class));
                ResultActivity.this.finish();
            }
        });
    }
}