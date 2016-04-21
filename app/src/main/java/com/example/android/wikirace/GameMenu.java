package com.example.android.wikirace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Full-screen activity for the game menu.
 */
//TODO: Handle Screen rotation

public class GameMenu extends AppCompatActivity implements View.OnClickListener {
    private Button mStartButton;
    private Button mSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove the tool bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_menu);

        mStartButton = (Button) findViewById(R.id.start_button);
        mSettingsButton = (Button) findViewById(R.id.settings_button);

        mStartButton.setOnClickListener(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


    /**
     * Launches activity for the start of the game
     */
    private void startGameActivity() {
        Intent intent = new Intent(this,GamePlay.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.start_button:
                startGameActivity();
                break;
        }
    }
}
