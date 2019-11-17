package net.muslu.seniorproject.Api.JSON;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DistanceParser {

    public List<HashMap<String, String>> parse(JSONObject jsonObject){
        List<HashMap<String, String>> routes = new ArrayList<>();
        JSONArray jRows;
        JSONArray jElements;
        try {
            jRows = jsonObject.getJSONArray("rows");
            /** Traversing all routes */
            for (int i = 0; i < jRows.length(); i++) {
                jElements = ((JSONObject) jRows.get(i)).getJSONArray("elements");
                /** Traversing all legs */
                for (int j = 0; j < jElements.length(); j++) {

                    int distance = 0, duration = 0;
                    distance = (int) ((JSONObject) ((JSONObject) jElements.get(j)).get("distance")).get("value");
                    duration = (int) ((JSONObject) ((JSONObject) jElements.get(j)).get("duration")).get("value");

                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("dis", Integer.toString(distance));
                    hm.put("dur", Integer.toString(duration));
                    routes.add(hm);
                }
            }

        } catch (Exception e) {
            Log.e("distance matrix error",e.getMessage());
        }
        return routes;
    }
}
