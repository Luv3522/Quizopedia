package com.example.questioner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class QuestionActivity extends AppCompatActivity {

    int backCount = 0, questionCount = 0 , correctAnswers = 0;

    boolean stopResponse = true;

    MyDbHelper myDbHelper;
    ArrayList<Question> questionArrayList;
    CircularProgressIndicator progressIndicator;
    TextView progressText, optionA, optionB, optionC, optionD, questionTextView;
    //Button btn_ex=findViewById(R.id.explanation_btn);
    AlertDialog.Builder builder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        myDbHelper = new MyDbHelper(this);
        questionArrayList = new ArrayList<>();
        myDbHelper.getQuestions(getIntent().getAction());
        String topic  = getIntent().getAction();
        Log.d("topic",""+topic);
        myDbHelper.getQuestions(topic,new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d("task.getResult().getValue()",""+task.getResult().getValue());

                if(task.getResult().exists() && task.getResult().getValue() != null){
                    Map<String,Object> mapData = (Map<String, Object>) task.getResult().getValue();
                    Log.d("task.getResult().getValue()",""+task.getResult().getValue());
                    Log.d("mapData",""+mapData);
                    for (Map.Entry<String,Object> entry : mapData.entrySet()){
                        Map<String,String> valueSet = (Map<String, String>) entry.getValue();
                        String topicKey = valueSet.get("topic");
                        if(topicKey.equalsIgnoreCase(topic)) {
                            Log.d("iteratedata",""+entry.getValue());
                            Gson gson = new Gson();
                            JsonElement jsonElement = gson.toJsonTree(entry.getValue());
                            Question q = gson.fromJson(jsonElement, Question.class);
                            questionArrayList.add(q);
                        }

                    }

                    reloadData();
                }
            }
        });

        progressIndicator = findViewById(R.id.progressBar);

        questionTextView = findViewById(R.id.questionDisplay);
        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);
        
        progressText = findViewById(R.id.progressTextView);
        builder = new AlertDialog.Builder(QuestionActivity.this);



       
    }

    private void reloadData() {
        Log.d("questionArrayList",""+questionArrayList.size());
        if(questionArrayList.isEmpty()){

            questionTextView.setVisibility(View.INVISIBLE);
            optionA.setVisibility(View.INVISIBLE);
            optionB.setVisibility(View.INVISIBLE);
            optionC.setVisibility(View.INVISIBLE);
            optionD.setVisibility(View.INVISIBLE);

            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "No Questions Available!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onBackPressed();
                            onBackPressed();
                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light,getTheme()))
                    .show();
        }else{
            loadQuestion();

            ObjectAnimator animator = (ObjectAnimator) AnimatorInflater.loadAnimator(this,R.animator.flip);
            animator.setDuration(500);

            optionA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(stopResponse) {
                        if (questionArrayList.get(questionCount).getCorrectOption().equalsIgnoreCase("A")) {
                            correctAnswers++;
                            animator.setTarget(view);
                            animator.start();
                        } else {
                            view.setBackgroundColor(Color.RED);
                        }
                        stopResponse = false;
                        String expl = questionArrayList.get(questionCount).explanation;
                        builder.setMessage(expl)
                                .setCancelable(false)
                                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();
                                        Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.setTitle("Explanation");
                        alert.show();
                        //Toast.makeText(QuestionActivity.this, "Explanation "+expl, Toast.LENGTH_SHORT).show();
//                        openDialog(expl);
                    }
                }
            });

            optionB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (stopResponse) {
                        if (questionArrayList.get(questionCount).getCorrectOption().equalsIgnoreCase("B")) {
                            correctAnswers++;
                            animator.setTarget(view);
                            animator.start();
                        } else {
                            view.setBackgroundColor(Color.RED);
                        }
                    }
                    stopResponse = false;
                    String expl=questionArrayList.get(questionCount).explanation;
                    builder.setMessage(expl)
                            .setCancelable(false)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                    Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.setTitle("Explanation");
                    alert.show();
                    Toast.makeText(QuestionActivity.this, "Explanation "+expl, Toast.LENGTH_SHORT).show();
                }
            });

            optionC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (stopResponse) {
                        if (questionArrayList.get(questionCount).getCorrectOption().equalsIgnoreCase("C")) {
                            correctAnswers++;
                            animator.setTarget(view);
                            animator.start();
                        } else {
                            view.setBackgroundColor(Color.RED);
                        }
                    }
                    stopResponse = false;
                    String expl=questionArrayList.get(questionCount).explanation;
                    builder.setMessage(expl)
                            .setCancelable(false)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                    Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.setTitle("Explanation");
                    alert.show();
                    Toast.makeText(QuestionActivity.this, "Explanation "+expl, Toast.LENGTH_SHORT).show();
                }
            });

            optionD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (stopResponse) {
                        if (questionArrayList.get(questionCount).getCorrectOption().equalsIgnoreCase("D")) {
                            correctAnswers++;
                            animator.setTarget(view);
                            animator.start();
                        } else {
                            view.setBackgroundColor(Color.RED);
                        }
                    }
                    String expl=questionArrayList.get(questionCount).explanation;
                    builder.setMessage(expl)
                            .setCancelable(false)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                    Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.setTitle("Explanation");
                    alert.show();
                    Toast.makeText(QuestionActivity.this, "Explanation "+expl, Toast.LENGTH_SHORT).show();
                    stopResponse = false;
                }
            });
        }
    }

//    private void openDialog(String expl) {
//        Dialog exampleDialog = new Dialog();
//        exampleDialog.show(getSupportFragmentManager(), "example dialog");
//    }

    protected void loadQuestion(){
        if(questionCount != questionArrayList.size()){
            stopResponse = true;
            startTimer(questionCount);
        }else{
            addResultToDatabase();
            Intent intent = new Intent(QuestionActivity.this,ResultActivity.class);
            intent.setAction(correctAnswers+"/"+questionArrayList.size());
            startActivity(intent);
            QuestionActivity.this.finish();
        }
    }

    private void addResultToDatabase() {
        SharedPreferences sharedPreferences = getSharedPreferences("QUESTIONER",MODE_PRIVATE);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        myDbHelper.addResult(sharedPreferences.getString("USER",""),dateFormat.format(new Date()),correctAnswers+"/"+questionArrayList.size(),getIntent().getAction());
    }

    private void startTimer(int questionNumber) {
        showQuestion(questionNumber);
        progressIndicator.setIndicatorColor(Color.GREEN);
        new CountDownTimer(30000,1000){
            @Override
            public void onTick(long l) {
                progressIndicator.setProgress((int)l / 1000);
                progressText.setText((l / 1000) + "");
                if((l / 1000) < 13 ){
                    progressIndicator.setIndicatorColor(Color.RED);
                }else if((l / 1000) < 20 ){
                    progressIndicator.setIndicatorColor(Color.parseColor("#BA883E"));
                }
                if(!stopResponse){
                    this.cancel();
                    questionCount++;
                    loadQuestion();
                }
            }

            @Override
            public void onFinish() {
                questionCount++;
                loadQuestion();
            }
        }.start();
    }

    private void showQuestion(int questionNumber) {
        Question question = questionArrayList.get(questionNumber);
        questionTextView.setText(question.getQuestion());
        optionA.setText(question.getOptionA());
        optionB.setText(question.getOptionB());
        optionC.setText(question.getOptionC());
        optionD.setText(question.getOptionD());
        optionA.setBackgroundResource(R.drawable.blur_white);
        optionB.setBackgroundResource(R.drawable.blur_white);
        optionC.setBackgroundResource(R.drawable.blur_white);
        optionD.setBackgroundResource(R.drawable.blur_white);
    }


    @Override
    public void onBackPressed() {
        backCount++;
        if(backCount == 2){
            super.onBackPressed();
        }
        new CountDownTimer(1500,1000){

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                backCount = 0;
            }
        }.start();
        if(backCount == 1){
            Toast.makeText(this, "Press back again to exit!", Toast.LENGTH_SHORT).show();
        }
    }



}