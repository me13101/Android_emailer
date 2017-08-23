package mmsa.com.sports_emailer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_login);
        Button _loginButton = (Button)findViewById(R.id.loginButton);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (login()){
                    Intent viewBox = new Intent(getApplicationContext(), BoxActivity.class);
                    startActivity(viewBox);
                }
            }
        });
    }
    private boolean login(){
        return true;
    }
}
