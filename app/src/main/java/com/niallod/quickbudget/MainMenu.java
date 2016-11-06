package com.niallod.quickbudget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenu extends AppCompatActivity {

    private EditText password;
    private TextView errorMessage;
    private Button clearButton;
    private Button loginButton;

    private String passwordEntered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        setPassword((EditText) findViewById(R.id.password_input));
        setErrorMessage((TextView) findViewById(R.id.error_message));
        setClearButton((Button) findViewById(R.id.clear_button));
        setLoginButton((Button) findViewById(R.id.login_button));

        getClearButton().setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getPassword().setText("");

                        Toast.makeText(MainMenu.this, "Text has been cleared", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        getLoginButton().setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        passwordEntered = getPassword().getText().toString();
                        Toast.makeText(MainMenu.this, passwordEntered, Toast.LENGTH_SHORT).show();
                        //TODO Need to make a call to the database to verify the password

                        //-------------------------------------------------------------
                        Intent intent = new Intent(MainMenu.this, QuickBudget.class);
                        startActivity(intent);
                        //--------------------------------------------------------------
                    }
                }
        );

    }

    public EditText getPassword() {
        return password;
    }

    public void setPassword(EditText password) {
        this.password = password;
    }

    public TextView getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(TextView errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Button getClearButton() {
        return clearButton;
    }

    public void setClearButton(Button clearButton) {
        this.clearButton = clearButton;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(Button loginButton) {
        this.loginButton = loginButton;
    }
}
