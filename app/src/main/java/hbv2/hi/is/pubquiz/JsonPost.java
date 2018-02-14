package hbv2.hi.is.pubquiz;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.lang.AutoCloseable;
/**
 * Created by ${Fannar} on 13.2.2018.
 */

public class JsonPost {
         public static final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient tclient = new OkHttpClient();
        Response response = null;



        String post(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            try (Response response = tclient.newCall( request).execute()){
                return response.body().string();
            }
        }

    public String serverPost() {
        return "{'id':'5',"
                + "'room_name':'keppni',"
                + "'team_name':lid1,"
                + "}";

    }


        public static void main(String[] args) throws IOException {
            JsonPost example = new JsonPost();
            String json = example.serverPost();
            String response = example.post("localhost:3000", json);
            System.out.println(response);
        }
}
