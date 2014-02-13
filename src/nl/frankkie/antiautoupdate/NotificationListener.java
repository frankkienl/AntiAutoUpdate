package nl.frankkie.antiautoupdate;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

/**
 *
 * @author FrankkieNL
 */
public class NotificationListener extends NotificationListenerService {    

    @Override
    public void onNotificationPosted(StatusBarNotification arg0) {
        Util.processNotification(this, arg0);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification arg0) {
        //do nothing.
    }

}
