package mmsa.com.sports_emailer;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import com.mmsa.controller.BoxscoreController;
import com.mmsa.controller.URLMapper;
import com.mmsa.model.Game;

public class GameActivity extends AppCompatActivity {
    public String game;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        bundle = this.getIntent().getExtras();
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        Object o = bundle.get("gameID");
                        game = BoxscoreController.getBoxScores(URLMapper.getPBPURL(o.toString()));
                        Object pbpLITE = BoxscoreController.iterateBoxscore(game, "game");
                        Object reader = BoxscoreController.iterateBoxscore(pbpLITE, "init");
                        Object game = BoxscoreController.iterateBoxscore(reader, "game");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            thread.join();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
