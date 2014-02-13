package nl.frankkie.antiautoupdate;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 *
 * @author Gebruiker
 */
public class AccessibilityServiceSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView tv = new TextView(this);
        tv.setText("AntiAutoUpdate Accessibility Service Settings.\nThere are no settings");
        setContentView(tv);
    }
}
