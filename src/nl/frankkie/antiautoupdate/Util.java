package nl.frankkie.antiautoupdate;

import android.app.ActivityManager;
import android.app.Notification;
import android.content.Context;
import static android.content.Context.ACTIVITY_SERVICE;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.RemoteViews;
import android.widget.Toast;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 *
 * @author FrankkieNL
 */
public class Util {

    public static void processNotification(Context c, StatusBarNotification notification) {
        String packagename = notification.getPackageName().toString();
        if (packagename.equalsIgnoreCase("com.android.vending")) {
            String s = notification.getNotification().tickerText.toString();
            Log.e("AntiAutoUpdate", s);
            String currently = getAppNameByPackagename(c, getForegroundAppPackagename(c));
            if (s.contains(currently)) {
                //Google Play is trying to update the app in the foreground.
                //KILL IT WITH FIRE.
                Toast.makeText(c, "Killing Google Play, for updating " + s, Toast.LENGTH_LONG).show();
                kill(c, "com.android.vending");
                killThisApp(c, "com.android.vending"); //just to be sure, kill it twice
            }
        }

    }

    public static void processNotification(Context c, AccessibilityEvent notification) {        
        String packagename = notification.getPackageName().toString();
        if (packagename.equalsIgnoreCase("com.android.vending")) {
            String s = getNotificationText(notification);
            Log.e("AntiAutoUpdate", s);
            String currently = getAppNameByPackagename(c, getForegroundAppPackagename(c));
            if (s.contains(currently)) {
                //Google Play is trying to update the app in the foreground.
                //KILL IT WITH FIRE.
                Toast.makeText(c, "Killing Google Play, for updating " + s, Toast.LENGTH_LONG).show();
                kill(c, "com.android.vending");
                killThisApp(c, "com.android.vending"); //just to be sure, kill it twice
            }
        }
    }

    public static String getNotificationText(AccessibilityEvent event) {
        //http://stackoverflow.com/questions/9292032/extract-notification-text-from-parcelable-contentview-or-contentintent
        String answer = "";
        try {
            Notification notification = (Notification) event.getParcelableData();
            RemoteViews views = notification.contentView;
            Class secretClass = views.getClass();

            Field outerFields[] = secretClass.getDeclaredFields();
            for (int i = 0; i < outerFields.length; i++) {
                if (!outerFields[i].getName().equals("mActions")) {
                    continue;
                }

                outerFields[i].setAccessible(true);

                ArrayList<Object> actions
                        = (ArrayList<Object>) outerFields[i].get(views);
                for (Object action : actions) {
                    Field innerFields[] = action.getClass().getDeclaredFields();

                    Object value = null;
                    String methodName = null;
                    for (Field field : innerFields) {
                        field.setAccessible(true);
                        if (field.getName().equals("value")) {
                            value = field.get(action);
                        } else if (field.getName().equals("methodName")) {
                            methodName = field.get(action).toString();
                        }
                    }
                    if (methodName.equals("setText")) {
                        if (!value.toString().equals("")) {
                            answer += value.toString() + "\n";
                        }
                    }
                }
                return answer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getForegroundAppPackagename(Context context) {
        return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1).get(0).topActivity.getPackageName();
    }

    public static String getAppNameByPackagename(Context c, String packagename) {
        final PackageManager pm = c.getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(packagename, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : packagename);
        return applicationName;
    }

    public static void kill(Context context, String packagename) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            am.killBackgroundProcesses(packagename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //////
    public static void killThisApp(Context context, String packagename) {
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        am.restartPackage(packagename); //KILL
        try {
            killMethod = ActivityManager.class.getMethod("killBackgroundProcesses", String.class);
            killMethod.invoke(am, packagename);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static Method killMethod = null;
    ////// 
}
