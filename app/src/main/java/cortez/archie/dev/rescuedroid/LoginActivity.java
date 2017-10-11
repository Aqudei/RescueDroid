package cortez.archie.dev.rescuedroid;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.SEND_SMS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_SEND_SMS = 0;
    private String user_id;
    private String server_contact;
    private String server_ip;

    String SENT = "SMS_SENT";
    private ProgressBar progress;
    private LinearLayout progress_container;
    private CheckBox withInjury;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        withInjury = (CheckBox) findViewById(R.id.checkBoxInjured);

        progress = (ProgressBar) findViewById(R.id.login_progress);
        progress_container = (LinearLayout) findViewById(R.id.login_progress_container);

        requestSendSms();

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
                hideProgress();
            }
        }, new IntentFilter(SENT));
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        user_id = sharedPref.getString("user_id", "0");
        server_contact = sharedPref.getString("server_contact", "");
        server_ip = sharedPref.getString("server_ip", "");
    }

    private void requestSendSms() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (checkSelfPermission(SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(null, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{SEND_SMS}, REQUEST_SEND_SMS);
                        }
                    });
        } else {
            requestPermissions(new String[]{SEND_SMS}, REQUEST_SEND_SMS);
        }
        return;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    public void doIndividualCheckIn(View view) {

        String status = withInjury.isChecked() ? "injured" : "safe";
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);
        String outgoing = String.format("{\"Id\":%s, \"scope\":\"self\", \"status\":\"%s\"}", user_id, status);
        showProgress();
        SmsManager.getDefault().sendTextMessage(server_contact, null, outgoing, sentPI, null);
    }

    public void doFamilyCheckIn(View view) {

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);
        String outgoing = String.format("{\"Id\":%s, \"scope\":\"family\"}", user_id);
        showProgress();
        SmsManager.getDefault().sendTextMessage(server_contact, null, outgoing, sentPI, null);
    }

    private void showProgress() {
        progress_container.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progress_container.setVisibility(View.GONE);
    }
}

