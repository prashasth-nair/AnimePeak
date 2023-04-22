package com.example.animepeak.Functions;

import com.google.android.exoplayer2.text.Cue;

import java.util.List;

public class Subtitle implements com.google.android.exoplayer2.text.Subtitle {


    @Override
    public int getNextEventTimeIndex(long timeUs) {
        return 0;
    }

    @Override
    public int getEventTimeCount() {
        return 0;
    }

    @Override
    public long getEventTime(int index) {
        return 0;
    }

    @Override
    public List<Cue> getCues(long timeUs) {
        return null;
    }
}

