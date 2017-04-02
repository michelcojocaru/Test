package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PracticalTest02SecondaryActivity extends AppCompatActivity {

    private TextView counter = null;
    private Button ok = null;
    private Button cancel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_secondary);

        counter = (TextView) findViewById(R.id.counter);
        cancel = (Button) findViewById(R.id.cancel);
        ok = (Button) findViewById(R.id.ok);

        Intent intent = getIntent();
        if(intent != null && intent.getExtras().containsKey("numberOfClicks")){
            int numberOfClicks = intent.getIntExtra("numberOfClicks",-1);
            counter.setText(String.valueOf(numberOfClicks));
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK,null);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED,null);
                finish();
            }

        });


    }
}
