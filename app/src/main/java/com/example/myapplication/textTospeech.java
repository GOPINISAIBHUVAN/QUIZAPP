package com.example.myapplication;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class textTospeech implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private Context context;

    public textTospeech(Context context) {
        this.context = context;
        textToSpeech = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Set language (optional)
            textToSpeech.setLanguage(Locale.US); // Example: Set to US English
        } else {
            // Handle initialization failure
            Log.e("TextToSpeech", "Initialization failed");
        }
    }

    public void speak(String text) {
        if (textToSpeech != null && textToSpeech.isSpeaking()) {
            // Interrupt any ongoing speech
            textToSpeech.stop();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    // Call shutdown() in your activity's onDestroy() to release resources
    public void shutDown() {
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
    }
}