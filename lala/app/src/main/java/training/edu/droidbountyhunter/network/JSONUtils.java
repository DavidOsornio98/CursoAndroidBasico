package training.edu.droidbountyhunter.network;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import training.edu.droidbountyhunter.data.DatabaseBountyHunter;
import training.edu.droidbountyhunter.models.Fugitivo;

public class JSONUtils {
    public static void parseFugitivos(String response, Context context) {
        DatabaseBountyHunter database = new DatabaseBountyHunter(context);
        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String nameFugitive = object.optString("name", "");
                database.InsertFugitivo(new Fugitivo(0, nameFugitive, "0"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
