package be.ehb.jsonplaceholderify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import be.ehb.jsonplaceholderify.model.PlaceholderPost;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvContent = findViewById(R.id.tv_content);

        Thread thisWillRunInTheBackground = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient mClient = new OkHttpClient();

                    Request mRequest = new Request.Builder()
                            .url("https://jsonplaceholder.typicode.com/posts")
                            .get()
                            .build();

                    Response mResponse = mClient.newCall(mRequest).execute();

                    //raw payload
                    String responseText = mResponse.body().string();
                    //convert to json
                    JSONArray postsJSON = new JSONArray(responseText);
                    //parse JSON
                    int postsJSONLength = postsJSON.length();
                    ArrayList<PlaceholderPost> parsedObjects = new ArrayList<>(postsJSONLength);

                    for (int i = 0; i < postsJSONLength; i++ ) {
                        JSONObject temp = postsJSON.getJSONObject(i);

                        PlaceholderPost currentPost = new PlaceholderPost(
                                temp.getLong("userId"),
                                temp.getLong("id"),
                                temp.getString("title"),
                                temp.getString("body")
                        );

                        parsedObjects.add(currentPost);
                    }

                    tvContent.setText(parsedObjects.toString());

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thisWillRunInTheBackground.start();

    }
}