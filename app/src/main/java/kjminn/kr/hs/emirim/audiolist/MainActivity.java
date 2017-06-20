package kjminn.kr.hs.emirim.audiolist;

import android.media.MediaPlayer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {
    ListView list;
    Button butPlay, butStop, butPause;
    TextView textMusic, textTime;
    SeekBar progress;
    String[] musics={"Moves like jagger","Sunday morning","This love"};
    int[] musicResIds={R.raw.moves_like_jagger, R.raw.sunday_morning, R.raw.this_love};
    int selectedMusiId;
    MediaPlayer mediaPlayer;
    boolean selectePauseButton;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list=(ListView)findViewById(R.id.list_music);
        butPlay=(Button)findViewById(R.id.but_play);
        butStop=(Button)findViewById(R.id.but_stop);
        butPause=(Button)findViewById(R.id.but_pause);
        textMusic=(TextView)findViewById(R.id.text_music);
        textTime=(TextView)findViewById(R.id.text_time);
        progress=(SeekBar) findViewById(R.id.progress_music);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, musics);
        list.setAdapter(adapter);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list.setItemChecked(0, true);
        selectedMusiId=musicResIds[0];
        mediaPlayer=MediaPlayer.create(this, selectedMusiId);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectePauseButton=false;
                mediaPlayer.stop();
                selectedMusiId=musicResIds[i];
                MainActivity.this.i=i;
            }
        });

        butPlay.setOnClickListener(new View.OnClickListener() {
            SimpleDateFormat timeFormat=new SimpleDateFormat("mm:ss");
            @Override
            public void onClick(View view) {
                textMusic.setText(musics[i]);
                if(selectePauseButton) {
                    selectePauseButton=false;
                }else
                    mediaPlayer=MediaPlayer.create(MainActivity.this, selectedMusiId);
                mediaPlayer.start();
                Thread musicThread=new Thread(){
                    @Override
                    public void run() {
                        if(mediaPlayer==null)
                            return;
                        progress.setMax(mediaPlayer.getDuration());
                        while (mediaPlayer.isPlaying()){
                            progress.setProgress(mediaPlayer.getCurrentPosition());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textTime.setText("진행시간: "+timeFormat.format(mediaPlayer.getCurrentPosition()));
                                }
                            });
                            SystemClock.sleep(200);
                        }
                    }
                };
                musicThread.start();
            }
        });

        butStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectePauseButton=false;
                mediaPlayer.stop();
            }
        });

        butPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectePauseButton=true;
                mediaPlayer.pause();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
    }
}
