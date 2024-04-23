package myapp.Translate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GoogleApi {

    private static String Json(String jsonResponse) {
        Gson gson = new Gson();

        JsonArray jsonArray = gson.fromJson(jsonResponse, JsonArray.class).get(0).getAsJsonArray();


        StringBuilder translation = new StringBuilder();
        for (JsonElement miniArray : jsonArray) {
            if (miniArray != null) {
                translation.append(miniArray.getAsJsonArray().get(0).getAsString());
            }
        }

        return translation.toString();
    }

    public static String translate(String text,String language) {

        try {
            String encode_text = URLEncoder.encode(text, StandardCharsets.UTF_8);
            String url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=" + language + "&dt=t&q=" + encode_text;
            URL apiUrl = URI.create(url).toURL();

            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");


            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder translated_text = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                translated_text.append(line);
            }
            reader.close();
            connection.disconnect();
            return Json(translated_text.toString());
        }catch (Exception e){

            System.out.println("Connection timeout");

        }
        return null;
    }

}
