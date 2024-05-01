package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import java.util.ArrayList;
import android.annotation.SuppressLint;

import org.w3c.dom.ls.LSOutput;

public class questionDbHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "questions.db";

    private SQLiteDatabase db;

    public questionDbHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        this.db = db;
        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                quizContract.quizTable.TABLE_NAME + " ( " +
                quizContract.quizTable.QUESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                quizContract.quizTable.COLUMN_QUESTION + " TEXT, " +
                quizContract.quizTable.COLUMN_OPTION1 + " TEXT, " +
                quizContract.quizTable.COLUMN_OPTION2 + " TEXT, " +
                quizContract.quizTable.COLUMN_OPTION3 + " TEXT, " +
                quizContract.quizTable.COLUMN_OPTION4 + " TEXT, " +
                quizContract.quizTable.COLUMN_ANSWER_NR + " INTEGER " +
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists quiz_questions");
        onCreate(db);

    }


    public void addQuestions(Questions questions){
        ContentValues cv = new ContentValues();

        cv.put(quizContract.quizTable.COLUMN_QUESTION,questions.getQuestion());
        cv.put(quizContract.quizTable.COLUMN_OPTION1,questions.getOption1());
        cv.put(quizContract.quizTable.COLUMN_OPTION2,questions.getOption2());
        cv.put(quizContract.quizTable.COLUMN_OPTION3,questions.getOption3());
        cv.put(quizContract.quizTable.COLUMN_OPTION4,questions.getOption4());
        cv.put(quizContract.quizTable.COLUMN_ANSWER_NR,questions.getAnswer());

        db.insert(quizContract.quizTable.TABLE_NAME,null,cv);

    }


    private void fillQuestionsTable(){

        Questions q1 = new Questions("What is Android ?","OS","Drivers","Software","Hardware",1);
        addQuestions(q1);
        Questions q2 = new Questions("_______ is the smallest unit of data in a computer ?","Gigabyte","Bit","Byte","Terabyte",2);
        addQuestions(q2);
        Questions q3 = new Questions("Which unit of the computer is considered as the brain of the computer ?","Memory unit","Input unit","CPU","Output unit",3);
        addQuestions(q3);
        Questions q4 = new Questions("What is the full form of PROM ?","Program read-only memory","Primary read-only memory","Programmable read-only memory","Program read-output memory",3);
        addQuestions(q4);
        Questions q5 = new Questions("a byte is equal to _____ bits ?","4","16","24","8",4);
        addQuestions(q5);
        Questions q6 = new Questions("Which of the following devices is NOT used to enter data into a computer ?","Mouse","Keyboard","Scanner","Monitor",4);
        addQuestions(q6);
        Questions q7 = new Questions("_____ is a diagrammatic representation of a program logic ?","Process","Flowchart","Data","Legend",2);
        addQuestions(q7);
        Questions q8 = new Questions("What is ' Roadeo'?","A robot that cleans city roads","A robot that fills potholes","A robot that drives taxis","A robot that helps maintain traffic",4);
        addQuestions(q8);
        Questions q9 = new Questions("AIX is the operating system of which company ?","Apple","Unisys","IBM","Microsoft",3);
        addQuestions(q9);
        Questions q10 = new Questions("what is the full form of RAM ?","Random Access Memory","Repeated Access Memory","Rapid Access Memory","Regular Access Memory",1);
        addQuestions(q10);

    }



    @SuppressLint("Range")
    public ArrayList<Questions> getAllQuestions() {

        ArrayList<Questions> questionList = new ArrayList<>();
        db = getReadableDatabase();



        String Projection[] = {

                quizContract.quizTable.QUESTION_ID,
                quizContract.quizTable.COLUMN_QUESTION,
                quizContract.quizTable.COLUMN_OPTION1,
                quizContract.quizTable.COLUMN_OPTION2,
                quizContract.quizTable.COLUMN_OPTION3,
                quizContract.quizTable.COLUMN_OPTION4,
                quizContract.quizTable.COLUMN_ANSWER_NR
        };



        Cursor c = db.query(quizContract.quizTable.TABLE_NAME,
                Projection,
                null,
                null,
                null,
                null,
                null);


        if (c.moveToFirst()) {
            do {

                Questions question = new Questions();
                question.setQuestion(c.getString(c.getColumnIndex(quizContract.quizTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(quizContract.quizTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(quizContract.quizTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(quizContract.quizTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(quizContract.quizTable.COLUMN_OPTION4)));
                question.setAnswer(c.getInt(c.getColumnIndex(quizContract.quizTable.COLUMN_ANSWER_NR)));

                questionList.add(question);

            } while (c.moveToNext());

        }
        c.close();
        return questionList;

    }


}
