package cortez.archie.dev.rescuedroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    class SyncTask extends AsyncTask<Void, Void, Center> {

        @Override
        protected Center doInBackground(Void... params) {

            Gson gsonParser = new Gson();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

            String user_id = sharedPreferences.getString("user_id", "");
            String server_ip = sharedPreferences.getString("server_ip", "");
            String server_port = sharedPreferences.getString("server_port", "8000");

            if (TextUtils.isEmpty(server_ip)
                    || TextUtils.isEmpty(user_id))
                return null;

            String remoteUrl = String.format("http://%s:%s/api/", server_ip, server_port);
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(remoteUrl)
                    .build();

            RescueService rescueService = retrofit.create(RescueService.class);
            Call<Person> call = rescueService.getUserInfo(user_id);

            try {
                Person person = call.execute().body();
                if (person != null) {

                    Call<Center> caller = rescueService.getCenterInfo("" + person.get_Center());
                    Center center = caller.execute().body();
                    if (center != null) {

                        String lat = center.getLatitude();
                        String longi = center.getLongitude();
                        String inCharge = center.getInChargeCellphone();

                        sharedPreferences.edit().putString("latitude", lat)
                                .putString("longitude", longi)
                                .putString("evacuation_contact", inCharge)
                                .commit();

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Center center) {
            Toast.makeText(MainActivity.this, "Operation Done!", Toast.LENGTH_LONG).show();
        }
    }

    public void doSync(View view) {
        new SyncTask().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openCheckIns(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void findMyWay(View view) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        String latitude = sharedPref.getString("latitude", "0,0");
        String longitude = sharedPref.getString("longitude", "0,0");

        String locationRequest = String.format("https://www.google.com/maps/dir/?api=1%s&destination=%s&travelmode=walking&dir_action=navigate",
                "", latitude + ", " + longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri
                .parse(locationRequest));
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
