package mmsa.com.sports_emailer;

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.ObjectConstructor;
import com.mmsa.model.BaseballGame;
import com.mmsa.model.Game;
import com.mmsa.model.Team;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by micha on 8/3/2017.
 */

public class Controller {

    public static Object iterateBoxscore(Object boxscore, String key){
        try {
            if (boxscore instanceof JSONObject) {
                Object reader = ((JSONObject) boxscore).get(key);
                return reader;
            }else if(boxscore instanceof LinkedTreeMap){
                Object reader = ((Map)boxscore).get("key");
                return reader;
            }else if(boxscore instanceof JSONArray) {
                List<Object> list = new ArrayList();
                for (int i = 0; i < ((JSONArray)boxscore).length(); i++){
                    list.add(((JSONArray) boxscore).get(i));
                }
                return list;
            }else {
                JSONObject reader = new JSONObject(boxscore.toString());
                return reader;
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static Game populateGameFromList(List<JSONObject> gameList, int index){
        try {
            Game game = new BaseballGame();
            Team home = new Team();
            Team away = new Team();

            Object g = iterateBoxscore(gameList.get(index), "game");
            Object homeObj = iterateBoxscore(g,"home");
            Object awayObj = iterateBoxscore(g,"away");

            home.setName(iterateBoxscore(homeObj,"name").toString());
            home.setMarket(iterateBoxscore(homeObj,"market").toString());
            home.setRuns(iterateBoxscore(homeObj,"runs").toString());
            home.setHits(iterateBoxscore(homeObj,"hits").toString());

            away.setName(iterateBoxscore(awayObj,"name").toString());
            away.setMarket(iterateBoxscore(awayObj,"market").toString());
            away.setRuns(iterateBoxscore(awayObj,"runs").toString());
            away.setHits(iterateBoxscore(awayObj,"hits").toString());

            game.setHome(home);
            game.setAway(away);
            return game;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
