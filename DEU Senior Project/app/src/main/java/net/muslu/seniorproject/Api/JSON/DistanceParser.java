package net.muslu.seniorproject.Api.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DistanceParser {

    public List<List<HashMap<String, String>>> parse(JSONObject jsonObject){
        List<List<HashMap<String, String>>> routes = new ArrayList<>();
        JSONArray jRows;
        JSONArray jElements;
        JSONArray jDistance; // returns metres
        try {
            jRows = jsonObject.getJSONArray("rows");
            /** Traversing all routes */
            for (int i = 0; i < jRows.length(); i++) {
                jElements = ((JSONObject) jRows.get(i)).getJSONArray("elements");
                List path = new ArrayList<>();
                /** Traversing all legs */
                for (int j = 0; j < jElements.length(); j++) {
                    jDistance = ((JSONObject) jElements.get(j)).getJSONArray("distance");

                    /** Traversing all steps */
                    for (int k = 0; k < jDistance.length(); k++) {
                        double distance = 0.0, duration = 0.0;
                        distance = (Double) ((JSONObject) ((JSONObject) jElements.get(k)).get("distance")).get("value");
                        duration = (Double) ((JSONObject) ((JSONObject) jElements.get(k)).get("duration")).get("value");

                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("dis", Double.toString(distance));
                        hm.put("dur", Double.toString(duration));
                        path.add(hm);
                    }
                    routes.add(path);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        return routes;
    }
}
