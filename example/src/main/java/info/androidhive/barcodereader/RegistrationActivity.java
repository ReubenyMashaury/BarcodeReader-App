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

import info.androidhive.barcodereader.SQLiteDatabaseFolder.DatabaseHandler;

public class RegistrationActivity extends AppCompatActivity {

    Button register_btn;
    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        final TextView login_txt = findViewById(R.id.login_txt);
        register_btn = findViewById(R.id.register_btn);
        final EditText username = findViewById(R.id.username_login);
        final EditText password = findViewById(R.id.password);
        final EditText confirm_password = findViewById(R.id.confirm_password);

        login_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(username.getText().toString().isEmpty() || password.getText().toString().isEmpty() || confirm_password.getText().toString().isEmpty()){
                    Toast.makeText(RegistrationActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                }else{
                    if(password.getText().toString().equals(confirm_password.getText().toString())){
                        if(db.addUser(new User("1", username.getText().toString(), password.getText().toString()))){
                            String user_name = db.userLogin(username.getText().toString(), password.getText().toString()).username;
                            Intent intent = new Intent(RegistrationActivity.this, ActivityMain.class);
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RegistrationActivity.this);

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("full_name", user_name);

                            editor.apply();
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            Toast.makeText(RegistrationActivity.this, "Successfully registered!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegistrationActivity.this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}