package com.github.yamill.orientation;
import android.app.Activity;
import android.content.*;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.OrientationEventListener;
import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.*;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import java.util.*;
import javax.annotation.Nullable;

public class OrientationModule extends ReactContextBaseJavaModule implements LifecycleEventListener{

    /**
     * @param reactContext
     */
    public OrientationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        final ReactApplicationContext ctx = reactContext;

//		
        OrientationEventListener listener = new OrientationEventListener(reactContext, SensorManager.SENSOR_DELAY_NORMAL) {
            public void onOrientationChanged (int orientation) {
//				Trigger "orientationDidChange" Event
                WritableMap params = Arguments.createMap();
                params.putString("orientation", getOrientationString(orientation));
                if (ctx.hasActiveCatalystInstance()) {
                    ctx.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("orientationDidChange", params);
                }

//				Trigger "specificOrientationDidChange" Event
                WritableMap params = Arguments.createMap();
                params.putString("orientation", getSpecificOrientationString(orientation));
                if (ctx.hasActiveCatalystInstance()) {
                    ctx.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("specificOrientationDidChange", params);
                }
            }
        };
        listener.enable();
    }

    @Override
    public String getName() {
        return "Orientation";
    }

    @ReactMethod
    public void getOrientation (Callback callback) {
        final int orientationInt = getReactApplicationContext().getResources().getConfiguration().orientation;

        callback.invoke(null, this.getOrientationString(orientationInt));
    }

    @ReactMethod
    public void lockToPortrait() {
        final Activity activity = getCurrentActivity();
        if (activity == null) {
            return;
        }
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @ReactMethod
    public void lockToLandscape() {
        final Activity activity = getCurrentActivity();
        if (activity == null) {
            return;
        }
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }

    @ReactMethod
    public void lockToLandscapeLeft() {
        final Activity activity = getCurrentActivity();
        if (activity == null) {
            return;
        }
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @ReactMethod
    public void lockToLandscapeRight() {
        final Activity activity = getCurrentActivity();
        if (activity == null) {
            return;
        }
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
    }

    @ReactMethod
    public void unlockAllOrientations() {
        final Activity activity = getCurrentActivity();
        if (activity == null) {
            return;
        }
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public @Nullable Map<String, Object> getConstants() {
        HashMap<String, Object> constants = new HashMap<String, Object>();
        int orientationInt = getReactApplicationContext().getResources().getConfiguration().orientation;

        String orientation = this.getOrientationString(orientationInt);
        if (orientation == "null") {
            constants.put("initialOrientation", null);
        } else {
            constants.put("initialOrientation", orientation);
        }

        return constants;
    }

    /**
     * @param orientation
     */
    private String getOrientationString (int orientation) {
    	switch ( orientation ) {
    		case Configuration.ORIENTATION_LANDSCAPE: {
    			return "LANDSCAPE";
    		}
    		case Configuration.ORIENTATION_PORTRAIT: {
    			return "PORTRAIT";
    		}
    		default {
    			return "UNKNOWN";
    		}
    	}
    }

    /**
     * @param orientation
     */
    private String getSpecificOrientationString (int orientation) {
    	switch ( orientation ) {
    		case Configuration.ORIENTATION_PORTRAIT: {
    			return "PORTRAIT";
    		}
    		case 90: {
    			return "LANDSCAPE-LEFT";
    		}
    		case 180: {
    			return "PORTRAITUPSIDEDOWN";
    		}
    		case 270: {
    			return "LANDSCAPE-RIGHT";
    		}
    		default {
    			return "UNKNOWN";
    		}
    	}
    }

    /**
     * @param orientation
     */
    private String getOrientationString (int orientation) {
    	switch ( orientation ) {
    		case Configuration.: {
    			return "LANDSCAPE";
    		}
    		case Configuration.ORIENTATION_PORTRAIT: {
    			return "PORTRAIT";
    		}
    		default {
    			return "UNKNOWN";
    		}
    	}
    }

    @Override
    public void onHostResume () {
        final Activity activity = getCurrentActivity();

        assert activity != null;
        activity.registerReceiver(receiver, new IntentFilter("onConfigurationChanged"));
    }

    @Override
    public void onHostPause() {
        final Activity activity = getCurrentActivity();
        if (activity == null) return;
        try
        {
            activity.unregisterReceiver(receiver);
        }
        catch (java.lang.IllegalArgumentException e) {
            FLog.e(ReactConstants.TAG, "receiver already unregistered", e);
        }
    }

    @Override
    public void onHostDestroy() {
        final Activity activity = getCurrentActivity();
        if (activity == null) return;
        try
        {
            activity.unregisterReceiver(receiver);
        }
        catch (java.lang.IllegalArgumentException e) {
            FLog.e(ReactConstants.TAG, "receiver already unregistered", e);
        }}
    }
