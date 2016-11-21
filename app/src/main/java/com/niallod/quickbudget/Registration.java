package com.niallod.quickbudget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.niallod.quickbudget.database.DatabaseManager;

public class Registration extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText re_entered_password;

    private Button toLoginPage;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        username = (EditText) findViewById(R.id.username_reg);
        password = (EditText) findViewById(R.id.password_reg);
        re_entered_password = (EditText) findViewById(R.id.re_enter_password_reg);

        toLoginPage = (Button) findViewById(R.id.to_login_page);
        register = (Button) findViewById(R.id.register);

        toLoginPage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Registration.this, MainMenu.class);
                        startActivity(i);
                    }
                }
        );

        register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uname = username.getText().toString();
                        String pword = password.getText().toString();
                        String re_enter_pword = re_entered_password.getText().toString();

                        if(pword.equals(re_enter_pword)) {
                            DatabaseManager dbManager = new DatabaseManager(Registration.this);
                            dbManager.openDatabase();

                            if(dbManager.registerUser(uname, pword)) {
                                Toast.makeText(Registration.this, "Switching Activity", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(Registration.this, QuickBudget.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(Registration.this, getString(R.string.registration_username_already_exists), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(Registration.this, getString(R.string.registration_error_passwords), Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
}
