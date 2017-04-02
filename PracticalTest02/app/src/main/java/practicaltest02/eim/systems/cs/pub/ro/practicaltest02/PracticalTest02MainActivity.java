package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    private static int SECONDARY_ACTIVITY_REQUEST_CODE = 2017;

    private Button leftButton = null;
    private Button rightButton = null;
    private TextView leftText = null;
    private TextView rightText = null;
    private Button navigate = null;

    private boolean serviceStatus = false;

    private IntentFilter intentFilter = new IntentFilter();
    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        serviceStatus = Constants.SERVICE_STOPPED;

        for (int index = 0; index < Constants.actionTypes.length; index++) {
            intentFilter.addAction(Constants.actionTypes[index]);
        }

        leftText = (TextView) findViewById(R.id.leftText);
        rightText = (TextView) findViewById(R.id.rightText);
        rightButton = (Button) findViewById(R.id.rightButton);
        leftButton = (Button) findViewById(R.id.leftButton);
        navigate = (Button) findViewById(R.id.navigate);

        if ((savedInstanceState != null) && (savedInstanceState.getString("leftClicks") != null)) {
            leftText.setText(savedInstanceState.getString(Constants.leftClicks));
        }else{
            leftText.setText(String.valueOf(0));
        }

        if ((savedInstanceState != null) && (savedInstanceState.getString("rightClicks") != null)) {
            rightText.setText(savedInstanceState.getString(Constants.rightClicks));
        }else{
            rightText.setText(String.valueOf(0));
        }


        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftText = (TextView) findViewById(R.id.leftText);
                int leftClicks = Integer.valueOf(leftText.getText().toString());
                leftClicks++;
                leftText.setText(String.valueOf(leftClicks));

                int leftNumber = leftClicks;
                int rightNumber = Integer.parseInt(rightText.getText().toString());

                if(leftNumber + rightNumber > Constants.NUMBER_OF_CLICKS_THREASHOLD && serviceStatus == Constants.SERVICE_STOPPED){
                    Intent intent = new Intent(getApplicationContext(), PracticalTest02Service.class);
                    intent.putExtra("firstNumber",leftNumber);
                    intent.putExtra("secondNumber",rightNumber);
                    getApplicationContext().startService(intent);
                    serviceStatus = Constants.SERVICE_STARTED;
                }
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightText = (TextView) findViewById(R.id.rightText);
                int rightClicks = Integer.valueOf(rightText.getText().toString());
                rightClicks++;
                rightText.setText(String.valueOf(rightClicks));

                int leftNumber = Integer.parseInt(rightText.getText().toString());
                int rightNumber = rightClicks;

                if(leftNumber + rightNumber > Constants.NUMBER_OF_CLICKS_THREASHOLD && serviceStatus == Constants.SERVICE_STOPPED){
                    Intent intent = new Intent(getApplicationContext(), PracticalTest02Service.class);
                    intent.putExtra("firstNumber",leftNumber);
                    intent.putExtra("secondNumber",rightNumber);
                    getApplicationContext().startService(intent);
                    serviceStatus = Constants.SERVICE_STARTED;
                }
            }
        });

        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PracticalTest02SecondaryActivity.class);
                int counter = Integer.parseInt(leftText.getText().toString()) +
                        Integer.parseInt(rightText.getText().toString());
                intent.putExtra("numberOfClicks",counter);
                startActivityForResult(intent,SECONDARY_ACTIVITY_REQUEST_CODE);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent intent){
        if(requestCode == SECONDARY_ACTIVITY_REQUEST_CODE){
            Toast.makeText(this,"The activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("leftClicks", leftText.getText().toString());

        savedInstanceState.putString("rightClicks", rightText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("leftClicks")) {
            leftText.setText(savedInstanceState.getString("leftClicks"));
        }else{
            leftText.setText(String.valueOf(0));
        }
        if (savedInstanceState.containsKey("rightClicks")) {
            rightText.setText(savedInstanceState.getString("rightClicks"));
        }else{
            rightText.setText(String.valueOf(0));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PracticalTest02Service.class);
        stopService(intent);
        super.onDestroy();
    }
}
