package serkan.com.musicplayerders;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button playBtn;
    SeekBar positionBar, volumeBar;
    TextView kalanSure,gecenSure;
    MediaPlayer mp;
    int toplamZaman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        playBtn=(Button)findViewById(R.id.button);
        kalanSure=(TextView)findViewById(R.id.kalanSureTxt);
        gecenSure=(TextView)findViewById(R.id.gecenSureTxt);

        //Media Player İşlemleri
        mp=MediaPlayer.create(this,R.raw.music); // MediaPlayer Oluşturduk
        mp.setLooping(true); // Media Player Loop Edilebiliyor.
        mp.seekTo(0); // ilk aşılış saniyesi
        mp.setVolume(0.5f,0.5f); //Sol Sağ Ses Seviyesi
        toplamZaman=mp.getDuration(); //Mp3 Toplam Süresini aldık.


        // Positio Bar İşlemleri
        positionBar=(SeekBar)findViewById(R.id.seekBar);
        positionBar.setMax(toplamZaman);
        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mp.seekTo(progress); // MediaPlayer Bar posiszyonu ayarladık
                    positionBar.setProgress(progress); // SeekBar Posizyonunu kullanıcının getirdiği değere set ettik.
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        // Ses Bar İşlemleri
        volumeBar=(SeekBar)findViewById(R.id.seekBar2);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                float volumeDegeri=progress/100f; // gelen değeri ses seviyesinin alabileciği bir değere çevirmek için 100 değerine bölüyoruz.
                mp.setVolume(volumeDegeri,volumeDegeri); //sol sağ hoparlörlerin ses değerini ayarlıyoruz.
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                while(mp!=null){


                    try {
                        Message msg=new Message();
                        msg.what=mp.getCurrentPosition(); // Geçen sürei alıyoruz.
                        handler.sendMessage(msg); // handler geçen süre değerini gönderip hesaplama yapıyoruz.
                        Thread.sleep(1000); // Bu işlemi her 1 sn de bir yapıyoruz.
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }


            }
        }).start();


    }


    Handler handler=new Handler(){ // Her zaman çalışan kod parçaları Handler lar Burada geçen süre ve kalan süre sürekli değiş
        //için handler bu işlemi sürekli yapıyoruz.
        @Override
        public void handleMessage(Message msg) {
            int gecenSureInt=msg.what;
            positionBar.setProgress(gecenSureInt);

            String gecenSureString=TimeLabel(gecenSureInt);
            gecenSure.setText(gecenSureString);

            String kalanSureString=TimeLabel(toplamZaman-gecenSureInt);
            kalanSure.setText("- "+kalanSureString);


            super.handleMessage(msg);
        }
    };


    public String TimeLabel(int time){
        String timeLbl="";
        int min=time/1000/60;
        int sec=time/1000%60;

        timeLbl=min+ ":";
        if(sec<10) timeLbl+="0";
        timeLbl+=sec;

        return timeLbl;

    }

    public void playTıkla(View view){
        if(!mp.isPlaying()){ // MediaPlayer Çalışmıyorsa
            mp.start();      // MediaPlayer i Çalıştır.
            playBtn.setBackgroundResource(R.drawable.stop); //Arka Planını Değiştir.
        }
        else{
            mp.pause(); // Çalışıyorsa durdur.
            playBtn.setBackgroundResource(R.drawable.play);
        }


    }



}
