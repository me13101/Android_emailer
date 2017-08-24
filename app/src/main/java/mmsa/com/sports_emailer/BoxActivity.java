package mmsa.com.sports_emailer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
                final Game game = Controller.populateGameFromList((List<JSONObject>)daysGames, i);

                TableRow descriptor = new TableRow(this);
                TableRow awayRow = new TableRow(this);
                TableRow homeRow = new TableRow(this);
                TableRow seperator = new TableRow(this);

                homeRow.setBackgroundColor(Color.parseColor("#E8EAF6"));
                awayRow.setBackgroundColor(Color.parseColor("#E8EAF6"));
                seperator.setBackgroundColor(Color.parseColor("#CFD8DC"));

                descriptor.addView(createTableTextView("Name",params1));
                descriptor.addView(createTableTextView("Runs",params1));
                descriptor.addView(createTableTextView("Hits",params1));
                descriptor.addView(createTableTextView("Errors",params1));

                if(game.getStatus().equals("inprogress")) {
                    seperator.addView(createTableTextView(game.getStatus() + " " + ((BaseballGame) game).getInningHalf() + ((BaseballGame) game).getInning(),params1));
                }
                else{
                    seperator.addView(createTableTextView(game.getStatus(),params1));
                }
                //====================================================================================

                awayRow.addView(createTableTextView((game.getAway().getMarket()+" "+game.getAway().getName()+" ("+game.getAway().getWins()+"-"+game.getAway().getLosses()+") "), params1));
                awayRow.addView(createTableTextView(((BaseballTeam)game.getAway()).getRuns(), params1));
                awayRow.addView(createTableTextView(((BaseballTeam)game.getAway()).getHits(), params1));
                awayRow.addView(createTableTextView(((BaseballTeam)game.getAway()).getErrors(), params1));

                //====================================================================================
                homeRow.addView(createTableTextView((game.getHome().getMarket()+" "+game.getHome().getName()+" ("+game.getHome().getWins()+"-"+game.getHome().getLosses()+") "), params1));
                homeRow.addView(createTableTextView(((BaseballTeam)game.getHome()).getRuns(), params1));
                homeRow.addView(createTableTextView(((BaseballTeam)game.getHome()).getHits(), params1));
                homeRow.addView(createTableTextView(((BaseballTeam)game.getHome()).getErrors(), params1));

                //====================================================================================
                tbl.addView(descriptor);
                tbl.addView(awayRow);
                tbl.addView(homeRow);
                tbl.addView(seperator);

                homeRow.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent viewGame = new Intent(getApplicationContext(), GameActivity.class);
                        Bundle b = new Bundle();
                        b.putString("gameID", game.getGameID());
                        viewGame.putExtras(b);
                        try {
                            startActivity(viewGame);
                        }catch (Exception e){
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }

    TextView createTableTextView(String text, TableRow.LayoutParams params){
        TextView cell=new TextView(this);
        cell.setLayoutParams(params);
        cell.setText(text);
        return cell;
    }
}

