package ly.warp.jitpublishtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ly.warp.jitpublishtestlib.PublicUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PublicUtils.dialNumber(this, "+248672345445");
    }
}
