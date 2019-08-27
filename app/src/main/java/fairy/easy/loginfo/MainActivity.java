package fairy.easy.loginfo;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    public static final String TAG="%%%%%%%%";
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView=findViewById(R.id.test_demo_tv);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG,"testV");
                Log.i(TAG,"testI");
                Log.d(TAG,"testD");
                Log.w(TAG,"testW");
                Log.e(TAG,"testE");
            }
        });


    }
}
