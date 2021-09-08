package info.androidhive.barcodereader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.prefs.Preferences;

import info.androidhive.barcodereader.SQLiteDatabaseFolder.DatabaseHandler;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login_btn = findViewById(R.id.login_btn);
        username = findViewById(R.id.username_login);
        password = findViewById(R.id.password);
        TextView register_txt = findViewById(R.id.register_txt);

        if (db.getUsersCount() < 1) {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        }

        register_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (db.getUsersCount() > 0) {
                    Toast.makeText(LoginActivity.this, "An account already exists in this phone!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                    startActivity(intent);
                }
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                } else {
                    if (db.userLogin(username.getText().toString(), password.getText().toString()) != null) {
                        String user_name = db.userLogin(username.getText().toString(), password.getText().toString()).username;
                        Intent intent = new Intent(LoginActivity.this, ActivityMain.class);
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("full_name", user_name);

                        editor.apply();

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, "Successfully logged in!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Incorrect username or password!", Toast.LENGTH_SHORT).show();
                    }
                }
//                ArrayList<Sale> sales = db.getAllSales();
            }
        });
    }
}