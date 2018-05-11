package ga.raja007.groupchat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.util.MAC;

public class MainActivity extends AppCompatActivity {


    static final String APP_ID = "70659";
    static final String AUTH_KEY = "U7jPCDDEG8Bx-rB";
    static final String AUTH_SECRET = "6npr-H3qUh-2ctX";
    static final String ACCOUNT_KEY = "z9GW_o5Stz5nLoLqYf4W";

    static final int REQUEST_CODE = 1000;

    Button btnLogin, btnSignup;
    EditText editUser, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestRuntimePermission();

        initializeFramework();

        btnLogin = (Button) findViewById(R.id.main_btnLogin);
        btnSignup = (Button) findViewById(R.id.main_btnSignup);

        editPassword = (EditText) findViewById(R.id.main_editPassword);
        editUser = (EditText) findViewById(R.id.main_editLogin);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user = editUser.getText().toString();
                final String password = editPassword.getText().toString();

                QBUser qbUser = new QBUser(user, password);

                QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {

                        Toast.makeText(getBaseContext(),"Login Success", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this, ChatDialogsActivity.class);
                        intent.putExtra("user", user);
                        intent.putExtra("password", password);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(QBResponseException e) {

                        Toast.makeText(getBaseContext(),""+e.getErrors(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void requestRuntimePermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case REQUEST_CODE:
            {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getBaseContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getBaseContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initializeFramework() {

        QBSettings.getInstance().init(getApplicationContext(), APP_ID, AUTH_KEY, AUTH_SECRET);

        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);

    }
}
