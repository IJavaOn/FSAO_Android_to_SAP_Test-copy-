package com.sap.copy.mdui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sap.cloud.mobile.foundation.authentication.BasicAuthDialogAuthenticator;
import com.sap.cloud.mobile.foundation.common.SettingsParameters;
import com.sap.cloud.mobile.foundation.networking.AppHeadersInterceptor;
import com.sap.cloud.mobile.foundation.networking.WebkitCookieJar;
import com.sap.cloud.mobile.foundation.user.UserInfo;
import com.sap.cloud.mobile.foundation.user.UserRoles;
import com.sap.copy.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DisplayNotification extends AppCompatActivity {

    private OkHttpClient myOkHttpClient;
    private String deviceID;
    //    private final String serviceURL = "https://mobile-a7c995ebb.dispatcher.hana.ondemand.com";
    private final String serviceURL = "https://oauthasservices-a7c995ebb.hana.ondemand.com" ;

    private final String appID = "com.sap.fsao";
    private final String connectionID = "https://frcis4u.sap.apps2cap.com:44300/sap/opu/odata/sap/EAM_NTF_CREATE";
    private String messageToToast;
    private Toast toast;
    private String currentUser;
    private Integer numberOfPresses = 0;
    private final String myTag = "myDebuggingTag";
    private OkHttpClient basicAuthOkHttpClient;
    private Button b_uploadLog,b_odata;

    private TextView reporter;
    private TextView equipment;
    private TextView description;

    private ImageButton backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notification);

        deviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);


        reporter = findViewById(R.id.reporter1);
        equipment = findViewById(R.id.equipment1);
        description = findViewById(R.id.description1);

        backbutton = findViewById(R.id.backbutton);


        String text = getIntent().getStringExtra("reporter");
        String text1 = getIntent().getStringExtra("equipment");
        String text2 = getIntent().getStringExtra("description");

        reporter.setText(text);
        equipment.setText(text1);
        description.setText(text2);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayNotification.this, NotificationList.class);
                startActivity(intent);
            }
        });


    }



    private void toastAMessage(String msg) {
        if (toast != null && toast.getView().isShown()) {
            msg = messageToToast + "\n" + msg;
        }
        else {  //clear any previously shown toasts that have since stopped being displayed
            messageToToast = "";
        }
        messageToToast = msg;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(getApplicationContext(),
                        messageToToast,
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    private void getUser() {
        Log.d(myTag, "In getUser");
        SettingsParameters sp = null;
        try {
            sp = new SettingsParameters(serviceURL, appID, deviceID, "1.0");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        UserRoles roles = new UserRoles(myOkHttpClient, sp);
        UserRoles.CallbackListener callbackListener = new UserRoles.CallbackListener() {
            @Override
            public void onSuccess(@NonNull UserInfo ui) {
                Log.d(myTag, "User Name: " + ui.getUserName());
                Log.d(myTag, "User Id: " + ui.getId());
                String[] roleList = ui.getRoles();
                Log.d(myTag, "User has the following Roles");
                for (int i=0; i < roleList.length; i++) {
                    Log.d(myTag, "Role Name " + roleList[i]);
                }
                currentUser = ui.getId();
                toastAMessage("Currently logged with " + ui.getId());
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                toastAMessage("UserRoles onFailure " + throwable.getMessage());
            }
        };

        roles.load(callbackListener);
    }

    private void enableButtonsOnRegister(final boolean enable) {
        final Button uploadLogButton = (Button) findViewById(R.id.b_uploadLog);
        final Button onlineODataButton = (Button) findViewById(R.id.b_odata);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                uploadLogButton.setEnabled(enable);
                onlineODataButton.setEnabled(enable);
            }
        });
    }

    public void onRegister(View view) {
        Log.d(myTag, "In onRegister");
        myOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AppHeadersInterceptor(appID, deviceID, "1.0"))
                .authenticator(new BasicAuthDialogAuthenticator())
                .cookieJar(new WebkitCookieJar())
                .build();

        Request request = new Request.Builder()
                .get()
                .url(serviceURL + "/" + connectionID + "/")
                .build();

        Callback updateUICallback = new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) { //called if there is no network
                Log.d(myTag, "onFailure called during authentication " + e.getMessage());
                toastAMessage("Registration failed " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d(myTag, "Successfully authenticated");
                    toastAMessage("Successfully authenticated");
                    enableButtonsOnRegister(true);
                    getUser();
                }
                else { //called if the credentials are incorrect
                    Log.d(myTag, "Registration failed " + response.networkResponse());
                    toastAMessage("Registration failed " + response.networkResponse());
                }
            }
        };

        myOkHttpClient.newCall(request).enqueue(updateUICallback);

    }
}