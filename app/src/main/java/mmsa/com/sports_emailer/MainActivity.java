package mmsa.com.sports_emailer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mmsa.controller.BoxscoreController;
import com.mmsa.model.Game;

import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    String boxscore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    BoxscoreController bc = new BoxscoreController("mlb-t6","3xbzsfssc3e275uy9r33pvm4");
                    boxscore = bc.getBoxScores(bc.getBoxscoreURL());
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

            TableRow.LayoutParams  params1=new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT,1.0f);
            TableRow.LayoutParams params2=new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

            TableLayout tbl = (TableLayout)findViewById(R.id.gameTable);


            for(int i = 0; i < ((List<JSONObject>)daysGames).size();i++){
                Game game = Controller.populateGameFromList((List<JSONObject>)daysGames, i);

                TableRow row=new TableRow(this);

                TextView txt1=new TextView(this);
                txt1.setLayoutParams(params1);
                txt1.setText("| "+game.getAway().getMarket()+" "+game.getAway().getName()+"| "+game.getAway().getRuns()+" |");
                row.addView(txt1);

                TextView txt2=new TextView(this);
                txt2.setLayoutParams(params1);
                txt2.setText("| "+game.getHome().getMarket()+" "+game.getHome().getName()+"| "+game.getHome().getRuns()+" |");
                row.addView(txt2);

                row.setLayoutParams(params2);
                tbl.addView(row);

            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }
}
