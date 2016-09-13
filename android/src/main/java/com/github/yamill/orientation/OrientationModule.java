package com.github.yamill.orientation;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.OrientationEventListener;
import com.facebook.react.bridge.*;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import java.util.*;
import javax.annotation.Nullable;

public class OrientationModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    /**
     * @param reactContext
     */
    public OrientationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        final ReactApplicationContext ctx = reactContext;

//		
        OrientationEventListener listener = new OrientationEventListener(reactContext, SensorManager.SENSOR_DELAY_NORMAL) {
            public void onOrientationChanged(int orientation) {
                Log.d("onOrientationChanged", String.valueOf(orientation) );

//				Trigger "orientationDidChange" Event
                WritableMap params = Arguments.createMap();
                params.putString("orientation", getOrientationString(orientation));
                if (ctx.hasActiveCatalystInstance()) {
                    ctx.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("orientationDidChange", params);
                }

//				Trigger "specificOrientationDidChange" Event
                WritableMap specificParams = Arguments.createMap();
                specificParams.putString("specificOrientation", getSpecificOrientationString(orientation));
                if (ctx.hasActiveCatalystInstance()) {
                    ctx.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("specificOrientationDidChange", specificParams);
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
    public void getOrientation(Callback callback) {
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
     * @return LANDSCAPE|PORTRAIT|UNKNOWN
     */
    private String getOrientationString (int orientation) {
        switch (orientation) {
            case 0:
            case 180: {
                return "PORTRAIT";
            }
            case 90:
            case 270: {
                return "LANDSCAPE";
            }
            default: {
                return "UNKNOWN";
            }
        }
    }

    /**
     * @param orientation
     * @return PORTRAIT|LANDSCAPE-LEFT|PORTRAITUPSIDEDOWN|LANDSCAPE-RIGHT|UNKNOWN
     */
    private String getSpecificOrientationString (int orientation) {
        switch (orientation) {
            case 0: {
                return "PORTRAIT";
            }
            case 90: {
                return "LANDSCAPE-RIGHT";
            }
            case 180: {
                return "PORTRAITUPSIDEDOWN";
            }
            case 270: {
                return "LANDSCAPE-LEFT";
            }
            default: {
                return "UNKNOWN";
            }
        }
    }

    @Override
    public void onHostResume() {
        final Activity activity = getCurrentActivity();
    }

    @Override
    public void onHostPause() {
        final Activity activity = getCurrentActivity();
    }

    @Override
    public void onHostDestroy() {
        final Activity activity = getCurrentActivity();
        if (activity == null) return;
    }
}