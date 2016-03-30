package roboniania.com.roboniania_android;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextView loginLink;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initialize components
        registerBtn = (Button) findViewById(R.id.register_button);
        registerBtn.setOnClickListener(this);

        loginLink = (TextView) findViewById(R.id.login_link);
        loginLink.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.register_button:
                sendOkForLoginActivity();
                break;
            case R.id.login_link:
                sendCancelForLoginActivity();
                break;
        }
    }

    private void sendOkForLoginActivity() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void sendCancelForLoginActivity() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

//    private void startLoginActivity() {
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
//        finish();
//    }
}
