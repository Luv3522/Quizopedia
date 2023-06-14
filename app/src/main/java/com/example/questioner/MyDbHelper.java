package com.example.questioner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyDbHelper extends SQLiteOpenHelper {

    int result;
    long res;

    public static final String DATABASE_NAME = "Quizopedia_DB";
    public static final String TABLE_TOPIC = "TOPICS";
    public static final String TABLE_USERS = "USERS";
    public static final String TABLE_USERDATA = "USERDATA";
    public static final String TABLE_QUESTIONS = "QUESTIONS_";

    public MyDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE USERS (username text PRIMARY KEY, password text);");
        sqLiteDatabase.execSQL("CREATE TABLE TOPICS (topicname text PRIMARY KEY);");
        sqLiteDatabase.execSQL("CREATE TABLE USERDATA (id integer PRIMARY KEY autoincrement, username text, date text, marks text , topic text);");
        sqLiteDatabase.execSQL("CREATE TABLE QUESTIONS_ (id integer PRIMARY KEY autoincrement, question text, a text, b text, c text, d text, answer text, topic text,explanation text);");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public void createUserAccount(String username,String password){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users").child(username);

        Users user = new Users(username,password);

        ref.setValue(user);


    }


    public int loginUser(String username,String password){
        result=0;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");
        Users user;
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(username)){
                    if(snapshot.child(username).child("password").toString().equalsIgnoreCase(password)){
                        result=1;
                    }
                }
                else{
                    result=-1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return result;
    }

    public long createTopic(String topicName) {
        res=1;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Topics").child(topicName);
        ref.setValue(topicName);
        return res;
    }

    public ArrayList<String> getAllTopics(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Topics");
        ArrayList<String> result = new ArrayList<>();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Log.d("getAllTopics","" + ds.getValue());
                    result.add(ds.getValue().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        try (Cursor cursor = database.rawQuery("SELECT * FROM TOPICS", null)) {
//            if(cursor.moveToFirst()){
//                do {
//                    result.add(cursor.getString(0));
//                }while (cursor.moveToNext());
//            }
//        }

        return result;
    }

    public ArrayList<String> getAllTopics(ValueEventListener callbacks){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Topics");
        ArrayList<String> result = new ArrayList<>();

        ref.addValueEventListener(callbacks);
//        try (Cursor cursor = database.rawQuery("SELECT * FROM TOPICS", null)) {
//            if(cursor.moveToFirst()){
//                do {
//                    result.add(cursor.getString(0));
//                }while (cursor.moveToNext());
//            }
//        }

        return result;
    }

    public int deleteTopic(String topic) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TABLE_TOPIC,"topicname='"+topic+"';",null);
    }

    public long addQuestion(String question, String optionA, String optionB, String optionC, String optionD, String correctOption, String topic, String explanation) {
        res=1;
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("Questions").child(question);

            Question ques = new Question(question, optionA, optionB, optionC, optionD, correctOption, topic, explanation);
            ref.setValue(ques);
        }catch(Exception e){
            Log.d("ABC",e.toString());
        }


        return  res;
    }

    public void getQuestions(String topic,OnCompleteListener callback){
        ArrayList<Question> result = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
         database.getReference().child("Questions").get().addOnCompleteListener(callback);
//        DatabaseReference ref =
//        ref.addValueEventListener(callback);

    }

    public ArrayList<Question> getQuestions(String topic){
        ArrayList<Question> result = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Questions");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    if(ds.child("question").child("topic").toString().equalsIgnoreCase(topic)){

                        Question q= ds.getValue(Question.class);
                        result.add(q);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        SQLiteDatabase database = this.getReadableDatabase();
//        ArrayList<Question> result = new ArrayList<>();
//        try (Cursor cursor = database.rawQuery("SELECT * FROM QUESTIONS_ WHERE topic='"+ topic +"'", null)) {
//            if(cursor.moveToFirst()){
//                do {
//                    //result.add(new Question(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6), cursor.getString(8)));
//                }while (cursor.moveToNext());
//            }
//        }
        return result;
    }
    public void addResult(String username, String date, String marks, String topic) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Result").child(username);
        Result res = new Result(username,date,marks,topic);


//        SQLiteDatabase database = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("username",username);
//        values.put("date",date);
//        values.put("marks",marks);
//        values.put("topic",topic);
//        database.insert(TABLE_USERDATA,null,values);
//        database.close();
    }


    public void createDemoQuiz(){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("topicname","Java");
        database.insert(TABLE_TOPIC,null,values);
        values.clear();
        values.put("question","Who invented Java programming?");
        values.put("a","Guido van Rossum");
        values.put("b","James Gosling");
        values.put("c","Dennis Ritchie");
        values.put("d","Bjarne Stroustrup");
        values.put("answer","B");
        values.put("topic","Java");
        values.put("explanation","No explanation needed.");
        database.insert(TABLE_QUESTIONS,null,values);
        values.clear();
        values.put("question","What is the extension of Java code files?");
        values.put("a",".js");
        values.put("b",".text");
        values.put("c",".class");
        values.put("d",".java");
        values.put("answer","D");
        values.put("topic","Java");
        values.put("explanation","Explanation 2");
        database.insert(TABLE_QUESTIONS,null,values);
        values.clear();
        values.put("question","What is the extension of compiled Java classes?");
        values.put("a",".js");
        values.put("b",".text");
        values.put("c",".class");
        values.put("d",".java");
        values.put("answer","C");
        values.put("topic","Java");
        values.put("explanation","Explanation 3");
        database.insert(TABLE_QUESTIONS,null,values);
        values.clear();
        values.put("question","Which one of the following is not an access modifier?");
        values.put("a","protected");
        values.put("b","void");
        values.put("c","public");
        values.put("d","private");
        values.put("answer","B");
        values.put("topic","Java");
        values.put("explanation","Explanation 4");
        database.insert(TABLE_QUESTIONS,null,values);
        values.clear();
        values.put("question","Which of these cannot be used for a variable name in Java?");
        values.put("a","identifier & keyword");
        values.put("b","identifier");
        values.put("c","keyword");
        values.put("d","none of the mentioned");
        values.put("answer","C");
        values.put("topic","Java");
        values.put("explanation","Explanation 5");
        database.insert(TABLE_QUESTIONS,null,values);
    }
}
