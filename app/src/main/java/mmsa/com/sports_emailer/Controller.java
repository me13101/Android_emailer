package mmsa.com.sports_emailer;

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.ObjectConstructor;
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

    public static Game getGameFromList(List<JSONObject> gameList, int index){
        try {
            Game game = new Game();
            Team home = new Team();
            Object g = iterateBoxscore(gameList.get(index), "game");
            Object homeObj = iterateBoxscore(g,"home");
            Object name = iterateBoxscore(homeObj,"name");
            Object market = iterateBoxscore(homeObj,"market");
            home.setName(name.toString());
            home.setMarket(market.toString());
            game.setHome(home);
            return game;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
