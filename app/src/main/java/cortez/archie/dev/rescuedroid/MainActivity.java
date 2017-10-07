package cortez.archie.dev.rescuedroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

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
