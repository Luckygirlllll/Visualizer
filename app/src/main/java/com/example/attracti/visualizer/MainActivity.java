package com.example.attracti.visualizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;


public class MainActivity extends ActionBarActivity {

    VisualizerView mVisualizerView;

    private MediaPlayer mMediaPlayer;
    private Visualizer mVisualizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVisualizerView = (VisualizerView) findViewById(R.id.myvisualizerview);

        initAudio();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing() && mMediaPlayer != null) {
            mVisualizer.release();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void initAudio() {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mMediaPlayer = MediaPlayer.create(this, R.raw.test1);

        setupVisualizerFxAndUI();
        // Make sure the visualizer is enabled only when you actually want to
        // receive data, and
        // when it makes sense to receive data.
        mVisualizer.setEnabled(true);
        // When the stream ends, we don't need to collect any more data. We
        // don't do this in
        // setupVisualizerFxAndUI because we likely want to have more,
        // non-Visualizer related code
        // in this callback.
        mMediaPlayer
                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mVisualizer.setEnabled(false);
                    }
                });

        mMediaPlayer.start();

      // start playing song from the certain position
      //  mMediaPlayer.seekTo(60000);


    }

    private void setupVisualizerFxAndUI() {

        // Create the Visualizer object and attach it to our media player.
        mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {
                        mVisualizerView.updateVisualizer(bytes);
                    }

                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] bytes, int samplingRate) {
                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, false);
    }

}
