package nl.frankkie.antiautoupdate;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 *
 * @author FrankkieNL
 */
public class MyAccessibilityService extends AccessibilityService {

    @Override
    protected void onServiceConnected() {
        //api 16+
        //getServiceInfo().eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent arg0) {
        if (arg0 == null) {
            return;
        }
        if (arg0.getEventType() != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            return;
        }

        //Process Notification
        Util.processNotification(this, arg0);
    }

    @Override
    public void onInterrupt() {
        //do nothin'
    }

}
