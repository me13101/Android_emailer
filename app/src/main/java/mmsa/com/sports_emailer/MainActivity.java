package mmsa.com.sports_emailer;

import android.content.Context;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mmsa.controller.BoxscoreController;
import com.mmsa.model.Game;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    String boxscore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    boxscore = BoxscoreController.getBoxScores(BoxscoreController.getBoxscoreURL());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            thread.start();
            thread.join();
            setContentView(R.layout.activity_main);
            Object reader = Controller.iterateBoxscore(boxscore, "init");
            Object league = Controller.iterateBoxscore(reader, "league");
            Object games = Controller.iterateBoxscore(league, "games");
            Object daysGames = Controller.iterateBoxscore(games, "game");
//                    Object gameList = Controller.iterateBoxscore(daysGames, "game");

            TableRow.LayoutParams  params1=new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT,1.0f);
            TableRow.LayoutParams params2=new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

            TableLayout tbl = (TableLayout)findViewById(R.id.gameTable);


            for(int i = 0; i < ((List<JSONObject>)daysGames).size();i++){
                Game game = Controller.getGameFromList((List<JSONObject>)daysGames, i);

                TableRow row=new TableRow(getApplicationContext());
                TextView txt1=new TextView(getApplicationContext());
                TextView txt2=new TextView(getApplicationContext());
                TextView txt3=new TextView(getApplicationContext());

//                        txt1.setText(game.getDate());
                txt2.setText(game.getHome().getName());
                TextView textView = (TextView)findViewById(R.id.helloWorld);
                textView.setText(game.getHome().getName().toString());
                LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.activity_main, null);
                view.invalidate();
//                        txt3.setText(game.getAway().getName());
                txt1.setLayoutParams(params1);
                txt2.setLayoutParams(params1);
                txt3.setLayoutParams(params1);
                row.addView(txt1);
                row.addView(txt2);
                row.addView(txt3);
                row.setLayoutParams(params2);
                tbl.addView(row);

            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }
}
