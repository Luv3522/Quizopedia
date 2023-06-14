package com.example.questioner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "My Channel";
    private static final int NOTIFICATION_ID = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(),R.drawable.intro_logo2,null);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap largeIcon =  bitmapDrawable.getBitmap();
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification;

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 300, 300, 300};
        v.vibrate(pattern,-1);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this)
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.intro_logo2)
                    .setContentText("Quizopedia_Result- \n You Scored - "+ getIntent().getAction())
                    .setSubText("Your Result")
                    .setChannelId(CHANNEL_ID)
                    .build();
            nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID,"New Channel",NotificationManager.IMPORTANCE_HIGH));
        } else{
            notification = new Notification.Builder(this)
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.intro_logo2)
                    .setContentText("Quizopedia_Result")
                    .setSubText("Your Result")
                    .build();

        }

        nm.notify(NOTIFICATION_ID,notification);


        TextView textView = findViewById(R.id.marks);

        String score =getIntent().getAction();
        String[] s=score.split("/",0);
        int re= Integer.parseInt(s[0]);

        textView.setText(getIntent().getAction());
        ImageView img = findViewById(R.id.firePng);

        if(re<3)
        {
            img.setImageResource(R.drawable.pp);
        }
        else if(re<6 && re>=3)
        {
            img.setImageResource(R.drawable.vv);
        }
        else if(re<9 && re>=6){
            img.setImageResource(R.drawable.vvt);
        }
        else
        {
            img.setImageResource(R.drawable.firepng);
        }
        MediaPlayer mp=new MediaPlayer();
        findViewById(R.id.backResult).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    AssetFileDescriptor afd = getAssets().openFd("back.wav");

                    mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());//Write your location here
                    mp.prepare();
                    mp.start();

                }catch(Exception e){Log.e("145145EE",e.toString());}

                startActivity(new Intent(ResultActivity.this,TopicActivity.class));
                ResultActivity.this.finish();
            }
        });
    }
}