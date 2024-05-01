package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;
import java.util.Locale;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;


public class home extends AppCompatActivity {
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button buttonConfirmNext;
    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCountdown;
    private TextView textViewCorrect, textViewWrong;
    private ArrayList<Questions> questionList;
    private int questionCounter;
    private int questionTotalCount;
    private Questions currentQuestion;
    private boolean answered;
    private Handler handler = new Handler();
    private int correctAns = 0, wrongAns = 0;
    private TimerDialog timerDialog;
    private CorrectDialog correctDialog;
    private WrongDialog wrongDialog;
    private textTospeech tts;
    private Button btnnd;
    int score = 0;
    private int totalSizeofQuiz = 0;
    private static final long COUNTDOWN_IN_MILLIS = 30000;
    private CountDownTimer countDownTimer;
    private long timeleftinMillis;
    private long backPressedTime;
    private int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupUI();
        fetchDb();
        Intent intentCategory = getIntent();
        value = intentCategory.getIntExtra("val", 0);
        Log.i("BUGBUG", String.valueOf(value));
        timerDialog = new TimerDialog(this);
        correctDialog = new CorrectDialog(this);
        wrongDialog = new WrongDialog(this);
        tts = new textTospeech(this);
    }

    public void setupUI() {
        textViewCorrect = findViewById(R.id.textCorrect);
        textViewWrong = findViewById(R.id.textWrong);
        textViewCountdown = findViewById(R.id.textTime);
        textViewQuestionCount = findViewById(R.id.textViewQuestionNumber);
        textViewScore = findViewById(R.id.textScore);
        textViewQuestion = findViewById(R.id.textView2);
        buttonConfirmNext = findViewById(R.id.buttonNext);
        rbGroup = findViewById(R.id.radioGroup);
        rb1 = findViewById(R.id.radioButton);
        rb2 = findViewById(R.id.radioButton2);
        rb3 = findViewById(R.id.radioButton3);
        rb4 = findViewById(R.id.radioButton4);
    }

    private void fetchDb() {
        questionDbHelper dbHelper = new questionDbHelper(this);
        questionList = dbHelper.getAllQuestions();
        quizStart();
    }

    private void quizStart() {
        questionTotalCount = questionList.size();
        Collections.shuffle(questionList);
        setQuestion();

        rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton) {
                    rb1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    rb2.setTextColor(Color.BLACK);
                    rb3.setTextColor(Color.BLACK);
                    rb4.setTextColor(Color.BLACK);

                    rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.when_option_selected));
                    rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.option_default_background));
                    rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.option_default_background));
                    rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.option_default_background));
                    // Set other radio button text colors and backgrounds accordingly
                }
                else if (checkedId == R.id.radioButton2) {
                    rb2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    rb1.setTextColor(Color.BLACK);
                    rb3.setTextColor(Color.BLACK);
                    rb4.setTextColor(Color.BLACK);
                    rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.when_option_selected));
                    rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.option_default_background));
                    rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.option_default_background));
                    rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.option_default_background));
                    // Set other radio button text colors and backgrounds accordingly
                }
                else if (checkedId == R.id.radioButton3) {
                    rb3.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    rb2.setTextColor(Color.BLACK);
                    rb1.setTextColor(Color.BLACK);
                    rb4.setTextColor(Color.BLACK);

                    rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.when_option_selected));
                    rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.option_default_background));
                    rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.option_default_background));
                    rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.option_default_background));
                    // Set other radio button text colors and backgrounds accordingly
                }
                else if (checkedId == R.id.radioButton4) {
                    rb4.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    rb2.setTextColor(Color.BLACK);
                    rb3.setTextColor(Color.BLACK);
                    rb1.setTextColor(Color.BLACK);
                    rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.when_option_selected));
                    rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.option_default_background));
                    rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.option_default_background));
                    rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.option_default_background));
                    // Set other radio button text colors and backgrounds accordingly
                }
            }
        });

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                        quizOperation();
                    } else {
                        Toast.makeText(home.this, "please select option", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void quizOperation()
    {
        answered = true;
        countDownTimer.cancel();
        RadioButton rbselected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbselected) + 1;
        checkSolution(answerNr, rbselected);
    }
    private void checkSolution(int answerNr, RadioButton rbselected)
    {
        switch (currentQuestion.getAnswer())
        {
            case 1:
                if (currentQuestion.getAnswer() == answerNr)
                {
                    rb1.setBackground(ContextCompat.getDrawable(this, R.drawable.correct_option_background));
                    rb1.setTextColor(Color.BLACK);
                    correctAns++;
                    score += 10;
                    textViewScore.setText("Score: " + String.valueOf(score));
                    correctDialog.correctDialog(score, this);
                    textViewCorrect.setText("Correct : " + correctAns);
                }
                else
                {
                    changetoIncorrectColor(rbselected);
                    wrongAns++;
                    String correctAnswer = (String) rb1.getText();
                    wrongDialog.wrongDialog(correctAnswer, this);
                    textViewWrong.setText("Wrong : " + wrongAns);
                }
                break;
            case 2:
                if (currentQuestion.getAnswer() == answerNr)
                {
                    rb2.setBackground(ContextCompat.getDrawable(this, R.drawable.correct_option_background));
                    rb2.setTextColor(Color.BLACK);
                    correctAns++;
                    score += 10;
                    textViewScore.setText("Score: " + String.valueOf(score));
                    correctDialog.correctDialog(score, this);
                    textViewCorrect.setText("Correct : " + correctAns);
                }
                else
                {
                    changetoIncorrectColor(rbselected);
                    wrongAns++;
                    String correctAnswer = (String) rb2.getText();
                    wrongDialog.wrongDialog(correctAnswer, this);
                    textViewWrong.setText("Wrong : " + wrongAns);
                }
                break;
            case 3:
                if (currentQuestion.getAnswer() == answerNr)
                {
                    rb3.setBackground(ContextCompat.getDrawable(this, R.drawable.correct_option_background));
                    rb3.setTextColor(Color.BLACK);
                    correctAns++;
                    score += 10;
                    textViewScore.setText("Score: " + String.valueOf(score));
                    correctDialog.correctDialog(score, this);
                    textViewCorrect.setText("Correct : " + correctAns);
                }
                else
                {
                    changetoIncorrectColor(rbselected);
                    wrongAns++;
                    String correctAnswer = (String) rb3.getText();
                    wrongDialog.wrongDialog(correctAnswer, this);
                    textViewWrong.setText("Wrong : " + wrongAns);
                }
                break;
            case 4:
                if (currentQuestion.getAnswer() == answerNr)
                {

                    rb4.setBackground(ContextCompat.getDrawable(this, R.drawable.correct_option_background));
                    rb4.setTextColor(Color.BLACK);
                    correctAns++;
                    score += 10;
                    textViewScore.setText("Score: " + String.valueOf(score));
                    correctDialog.correctDialog(score, this);
                    textViewCorrect.setText("Correct : " + correctAns);
                }
                else
                {
                    changetoIncorrectColor(rbselected);
                    wrongAns++;
                    String correctAnswer = (String) rb4.getText();
                    wrongDialog.wrongDialog(correctAnswer, this);
                    textViewWrong.setText("Wrong : " + wrongAns);
                }
                break;
        }
        if (questionCounter == questionTotalCount)
        {
            buttonConfirmNext.setText("Confirm and Finish");
        }
    }

    void changetoIncorrectColor(RadioButton rbselected)
    {
        rbselected.setBackground(ContextCompat.getDrawable(this, R.drawable.wrong_answer_background));
        rbselected.setTextColor(Color.BLACK);
    }


    public void setQuestion()
    {
        rbGroup.clearCheck();
        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.option_default_background));
        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.option_default_background));
        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.option_default_background));
        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.option_default_background));
        rb1.setTextColor(Color.BLACK);
        rb2.setTextColor(Color.BLACK);
        rb3.setTextColor(Color.BLACK);
        rb4.setTextColor(Color.BLACK);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(value == 1)
                {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            speakSomething(currentQuestion.getQuestion());
                        }
                    }, 1000);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            speakSomething(currentQuestion.getOption1());
                        }
                    }, 5000);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            speakSomething(currentQuestion.getOption2());
                        }
                    }, 7000);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            speakSomething(currentQuestion.getOption3());
                        }
                    }, 9000);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            speakSomething(currentQuestion.getOption4());
                        }
                    }, 11000);
                }
            }
        }, 3000);

        if (questionCounter < questionTotalCount) {

            currentQuestion = questionList.get(questionCounter);
            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            rb4.setText(currentQuestion.getOption4());

            questionCounter++;
            answered = false;
            buttonConfirmNext.setText("Confirm");
            textViewQuestionCount.setText("Questions: " + questionCounter + "/" + questionTotalCount);
            timeleftinMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        }
        else
        {
            // If Number of Questions Finishes then we need to finish the Quiz and Shows the User Quiz Performance
            Toast.makeText(this, "Quiz Finshed", Toast.LENGTH_SHORT).show();
            rb1.setClickable(false);
            rb2.setClickable(false);
            rb3.setClickable(false);
            rb4.setClickable(false);
            buttonConfirmNext.setClickable(false);
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    finalResult();
                }
            }, 2000);
        }
    }

    public void speakSomething(String text)
    {
        if (tts != null)
        {
            tts.speak(text);
        }
        else
        {
            // Handle the case where textToSpeechManager is null (optional)
            Log.e("TextToSpeech", "textToSpeechManager is null");
        }
    }
    private void startCountDown()
    {
        countDownTimer = new CountDownTimer(timeleftinMillis, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                timeleftinMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish()
            {
                timeleftinMillis = 0;
                updateCountDownText();
            }
        }.start();
    }
    private void updateCountDownText() {
        int minutes = (int) (timeleftinMillis / 1000) / 60;
        int seconds = (int) (timeleftinMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewCountdown.setText(timeFormatted);
        if (timeleftinMillis < 10000)
        {
            textViewCountdown.setTextColor(ContextCompat.getColor(this, R.color.red));
        }
        else
        {
            textViewCountdown.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
        if (timeleftinMillis == 0)
        {
            Toast.makeText(this, "Times Up!", Toast.LENGTH_SHORT).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    timerDialog.timerDialog();

                }
            }, 1000);
        }
    }

    public void speak(String text, TextToSpeech textToSpeech) {
        if (textToSpeech.isSpeaking())
        {
            // Interrupt any ongoing speech
            textToSpeech.stop();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("BUGBUG", "onRestart() in QuizActivity");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("BUGBUG", "onStop() in QuizActivity");
        finish();
    }
    @Override
    protected void onPause() {
        super.onPause();

        Log.i("BUGBUG", "onPause() in QuizActivity");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("BUGBUG", "onResume() in QuizActivity");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("BUGBUG", "onStart() in QuizActivity");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null)
        {
            countDownTimer.cancel();
        }
        if (tts != null)
        {
            tts.shutDown();
        }
        Log.i("BUGBUG", "onDestroy() in QuizActivity");
    }
    private void finalResult() {
        Intent resultData = new Intent(home.this, ResultActivity.class);
        resultData.putExtra("UserScore", score);
        resultData.putExtra("TotalQuestion", questionTotalCount);
        resultData.putExtra("CorrectQues", correctAns);
        resultData.putExtra("WrongQues", wrongAns);
        startActivity(resultData);
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            Intent intent = new Intent(home.this, PlayActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Press Again to Exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}