package mmsa.com.sports_emailer;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mmsa.controller.BoxscoreController;
import com.mmsa.controller.URLMapper;
import com.mmsa.model.BaseballGame;
import com.mmsa.model.BaseballTeam;
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
                    boxscore = BoxscoreController.getBoxScores(URLMapper.getGameURL("MLB"));
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

                TableRow awayRow = new TableRow(this);
                TableRow homeRow = new TableRow(this);
                TableRow seperator = new TableRow(this);

                homeRow.setBackgroundColor(Color.parseColor("#E8EAF6"));
                awayRow.setBackgroundColor(Color.parseColor("#E8EAF6"));
                seperator.setBackgroundColor(Color.parseColor("#CFD8DC"));

                TextView txtSep=new TextView(this);
                txtSep.setLayoutParams(params1);
                txtSep.setText(game.getStatus());
                seperator.addView(txtSep);

                TextView awayNameCell=new TextView(this);
                awayNameCell.setLayoutParams(params1);
                awayNameCell.setText("|"+game.getAway().getMarket()+" "+game.getAway().getName()+"|");
                awayRow.addView(awayNameCell);

                TextView awayScoreCell=new TextView(this);
                awayScoreCell.setLayoutParams(params1);
                awayScoreCell.setText(((BaseballTeam)game.getAway()).getRuns()+"|");
                awayRow.addView(awayScoreCell);

                TextView homeNameCell=new TextView(this);
                homeNameCell.setLayoutParams(params1);
                homeNameCell.setText(game.getHome().getMarket()+" "+game.getHome().getName()+"|");
                homeRow.addView(homeNameCell);

                TextView homeScoreCell=new TextView(this);
                homeScoreCell.setLayoutParams(params1);
                homeScoreCell.setText(((BaseballTeam)game.getHome()).getRuns()+"|");
                homeRow.addView(homeScoreCell);

                tbl.addView(awayRow);
                tbl.addView(homeRow);
                tbl.addView(seperator);
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }
}
