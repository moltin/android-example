package moltin.example_moltin.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import moltin.example_moltin.R;

public class SplashActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ((TextView)findViewById(R.id.txtActivityTitle)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), getString(R.string.font_bold)));
        ((TextView)findViewById(R.id.txtCollectionName)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), getString(R.string.font_bold)));
        ((Button)findViewById(R.id.btnGo)).setTypeface(Typeface.createFromAsset(getResources().getAssets(), getString(R.string.font_bold)));
    }

    public void onClickHandler(View view) {

        try
        {
            switch (view.getId())
            {
                case R.id.btnGo:
                    Intent intent = new Intent(this, CollectionActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
