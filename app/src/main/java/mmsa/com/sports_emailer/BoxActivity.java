package mmsa.com.sports_emailer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class BoxActivity extends AppCompatActivity {
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
            setContentView(R.layout.activity_box);
            Object reader = Controller.iterateBoxscore(boxscore, "init");
            Object league = Controller.iterateBoxscore(reader, "league");
            Object games = Controller.iterateBoxscore(league, "games");
            Object daysGames = Controller.iterateBoxscore(games, "game");

            TableRow.LayoutParams  params1=new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT,1.0f);
            TableRow.LayoutParams params2=new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

            TableLayout tbl = (TableLayout)findViewById(R.id.gameTable);


            for(int i = 0; i < ((List<JSONObject>)daysGames).size(); i++){
                Game game = Controller.populateGameFromList((List<JSONObject>)daysGames, i);

                TableRow descriptor = new TableRow(this);
                TableRow awayRow = new TableRow(this);
                TableRow homeRow = new TableRow(this);
                TableRow seperator = new TableRow(this);

                homeRow.setBackgroundColor(Color.parseColor("#E8EAF6"));
                awayRow.setBackgroundColor(Color.parseColor("#E8EAF6"));
                seperator.setBackgroundColor(Color.parseColor("#CFD8DC"));

                TextView descName=new TextView(this);
                descName.setLayoutParams(params1);
                descName.setText("Name");

                TextView descRuns=new TextView(this);
                descRuns.setLayoutParams(params1);
                descRuns.setText("Runs");

                TextView descHits=new TextView(this);
                descHits.setLayoutParams(params1);
                descHits.setText("Hits");

                TextView descErrors=new TextView(this);
                descErrors.setLayoutParams(params1);
                descErrors.setText("Errors");

                descriptor.addView(descName);
                descriptor.addView(descRuns);
                descriptor.addView(descHits);
                descriptor.addView(descErrors);

                TextView txtSep=new TextView(this);
                txtSep.setLayoutParams(params1);
                if(game.getStatus().equals("inprogress")) {
                    txtSep.setText(game.getStatus() + " " + ((BaseballGame) game).getInningHalf() + ((BaseballGame) game).getInning());
                }
                else{
                    txtSep.setText(game.getStatus());
                }
                seperator.addView(txtSep);

                //====================================================================================

                TextView awayNameCell=new TextView(this);
                awayNameCell.setLayoutParams(params1);
                awayNameCell.setText(game.getAway().getMarket()+" "+game.getAway().getName()+" ("+game.getAway().getWins()+"-"+game.getAway().getLosses()+") ");
                awayRow.addView(awayNameCell);

                TextView awayScoreCell=new TextView(this);
                awayScoreCell.setLayoutParams(params1);
                awayScoreCell.setText(((BaseballTeam)game.getAway()).getRuns());
                awayRow.addView(awayScoreCell);

                TextView awayHitsCell=new TextView(this);
                awayHitsCell.setLayoutParams(params1);
                awayHitsCell.setText(((BaseballTeam)game.getAway()).getHits());
                awayRow.addView(awayHitsCell);

                TextView awayErrorsCell=new TextView(this);
                awayErrorsCell.setLayoutParams(params1);
                awayErrorsCell.setText(((BaseballTeam)game.getHome()).getErrors());
                awayRow.addView(awayErrorsCell);

                //====================================================================================

                TextView homeNameCell=new TextView(this);
                homeNameCell.setLayoutParams(params1);
                homeNameCell.setText(game.getHome().getMarket()+" "+game.getHome().getName()+" ("+game.getHome().getWins()+"-"+game.getHome().getLosses()+") ");
                homeRow.addView(homeNameCell);

                TextView homeScoreCell=new TextView(this);
                homeScoreCell.setLayoutParams(params1);
                homeScoreCell.setText(((BaseballTeam)game.getHome()).getRuns());
                homeRow.addView(homeScoreCell);

                TextView homeHitsCell=new TextView(this);
                homeHitsCell.setLayoutParams(params1);
                homeHitsCell.setText(((BaseballTeam)game.getHome()).getHits());
                homeRow.addView(homeHitsCell);

                TextView homeErrorsCell=new TextView(this);
                homeErrorsCell.setLayoutParams(params1);
                homeErrorsCell.setText(((BaseballTeam)game.getHome()).getErrors());
                homeRow.addView(homeErrorsCell);

                //====================================================================================
                tbl.addView(descriptor);
                tbl.addView(awayRow);
                tbl.addView(homeRow);
                tbl.addView(seperator);
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }
}

