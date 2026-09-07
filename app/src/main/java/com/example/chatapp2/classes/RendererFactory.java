package com.example.chatapp2.classes;

import androidx.annotation.ColorInt;

import com.example.chatapp2.views.chatBox;
import com.example.chatapp2.views.messagesenderButton;

public class RendererFactory {

    public chatBox.WaveformRenderer createSimpleWaveformRenderer(@ColorInt int foreground, @ColorInt int background) {
        return SimpleWaveformRenderer.newInstance(background, foreground);
    }

}
