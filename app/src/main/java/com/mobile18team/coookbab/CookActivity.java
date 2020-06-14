package com.mobile18team.coookbab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CookActivity extends AppCompatActivity implements SensorEventListener {
    Button btn_timer;
    private SensorManager sensorManager;
    TextView tv_proximity;
    int i=1;
    int recipe_num;
    ImageView iv_cook;
    int photo_num;
    FirebaseStorage storage;
    StorageReference storageReference, cookstorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook);

        btn_timer=(Button)findViewById(R.id.btn_timer);

        //tv_proximity = (TextView)findViewById(R.id.tv_proximity);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        iv_cook=findViewById(R.id.iv_cook);

        storage = FirebaseStorage.getInstance("gs://cook-bab.appspot.com/");
        storageReference = storage.getReference();

        Toast.makeText(CookActivity.this, "화면을 가로로 놓고 터치해주세요!", Toast.LENGTH_LONG).show();
        Intent intent = getIntent();

        recipe_num = intent.getExtras().getInt("recipe_num");
        photo_num = intent.getExtras().getInt("photo_num");
        Log.e(this.getClass().getName(), String.valueOf(photo_num));

        Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(proximitySensor==null){
            //tv_proximity.setText("근접센서가 없어 모션 전환을 사용할 수 없습니다.");
        }
    }
    public void mOnClick(View v){
        Intent intent=new Intent();
        switch (v.getId()) {
            case R.id.btn_timer:
                Intent intent_timer=new Intent(CookActivity.this, Timer.class);
                startActivity(intent_timer);
                break;
        }

    }
    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorevent){
        if(sensorevent.sensor.getType()==Sensor.TYPE_PROXIMITY){
            if(sensorevent.values[0]==0){
                String photo = String.valueOf(recipe_num)+"-"+String.valueOf(i)+".png";
                Log.e(this.getClass().getName(), photo);
                cookstorage = storageReference.child(String.valueOf(recipe_num)).child(photo);
                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(cookstorage)
                        .into(iv_cook);
                i++;
                //tv_proximity.setText(String.valueOf(sensorevent.values[0]));
            }else{
                //tv_proximity.setText(String.valueOf(sensorevent.values[0]));
            }
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        if(action==MotionEvent.ACTION_DOWN && i<=photo_num){
            String photo = String.valueOf(recipe_num)+"-"+String.valueOf(i)+".png";
            Log.e(this.getClass().getName(), photo);
            cookstorage = storageReference.child(String.valueOf(recipe_num)).child(photo);
            Glide.with(this)
                    .using(new FirebaseImageLoader())
                    .load(cookstorage)
                    .into(iv_cook);
            i++;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        switch (accuracy){
            case SensorManager.SENSOR_STATUS_UNRELIABLE:
                //Toast.makeText(this, "UNRELIABLE", Toast.LENGTH_SHORT).show();
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                //Toast.makeText(this, "LOW", Toast.LENGTH_SHORT).show();
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                //Toast.makeText(this, "MEDIUM", Toast.LENGTH_SHORT).show();
                break;
            case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                //Toast.makeText(this, "HIGH", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
