package mmsa.com.sports_emailer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent viewLogin = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(viewLogin);
    }
}
