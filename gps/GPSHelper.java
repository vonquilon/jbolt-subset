package com.syas.jbolt.gps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageButton;

import com.syas.jbolt.R;
import com.syas.jbolt.main.MainFragment;
import com.syas.jbolt.server.PostCallback;
import com.syas.jbolt.server.Server;
import com.syas.jbolt.server.UpdateCoordinates;

public class GPSHelper {

    private static final int MAX_TIME = 1000 * 10; // 10 seconds
    private static final int TIME_THRESHOLD = 1000 * 60 * 15; //15 minutes
    private static final int DISTANCE_THRESHOLD = 1610; // 1610 meters, about a mile
    private static Location bestLastLocation;
    private static MainLocationListener listener;
    private final static Handler handler = new Handler();
    private static Runnable stopLocating;

    public static boolean startLocationService(final Context context, final MainFragment main, ImageButton button) {
        Log.e(null, "START LOCATION SERVICE");
        stopLocationService(context);

        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        listener = new MainLocationListener(main, button);

        final boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        final boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGpsEnabled && !isNetworkEnabled) {
            Log.e(null, "NO LOCATION SERVICES TRUE");
            return false;
        }

        if (isNetworkEnabled) {
            Log.e(null, "NETWORK ENABLED");
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, listener, null);
            stopLocating = new Runnable() {
                @Override
                public void run() {
                    Log.e(null, "NETWORK STOPPED");
                    stopLocationService(context);
                    if (isGpsEnabled) {
                        Log.e(null, "GPS IS ENABLED");
                        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener, null);
                        stopLocating = new Runnable() {
                            @Override
                            public void run() {
                                stopLocationService(context);
                                if (main != null) {
                                    main.refreshList();
                                }
                            }
                        };
                        handler.postDelayed(stopLocating, MAX_TIME);
                    }
                }
            };
            handler.postDelayed(stopLocating, MAX_TIME);
        } else {
            Log.e(null, "GPS ENABLED");
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener, null);
            stopLocating = new Runnable() {
                @Override
                public void run() {
                    stopLocationService(context);
                    if (main != null) {
                        main.refreshList();
                    }
                }
            };
            handler.postDelayed(stopLocating, MAX_TIME);
        }

        return true;
    }

    public static void stopLocationService(Context context) {
        Log.e(null, "STOP LOCATION SERVICE");
        if (listener != null) {
            ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE))
                    .removeUpdates(listener);
            handler.removeCallbacks(stopLocating);
        }
    }

    public static Location getLastLocation(Context context) {
        Log.e(null, "GETTING BEST LAST LOCATION");
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return null;

        Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (gpsLocation == null && networkLocation == null) {
            return null;
        }
        if (gpsLocation == null) {
            return bestLastLocation = networkLocation;
        }
        if (networkLocation == null) {
            return bestLastLocation = gpsLocation;
        }

        long currTime = System.currentTimeMillis();
        long gpsTimeAge = currTime - gpsLocation.getTime();
        long networkTimeAge = currTime - networkLocation.getTime();

        return bestLastLocation = gpsTimeAge > networkTimeAge ? networkLocation : gpsLocation;
    }

    private static boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            Log.e(null, "CURRENT BEST LOCATION NULL");
            // A new location is always better than no location
            return true;
        }

        if (location == currentBestLocation) {
            Log.e(null, "LOCATION EQUALS");
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TIME_THRESHOLD;
        boolean isSignificantlyOlder = timeDelta < -TIME_THRESHOLD;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            Log.e(null, "SIGNIFICANTLY NEWER");
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            Log.e(null, "SIGNIFICANTLY OLDER");
            return false;
        }

        int distanceDelta = (int) location.distanceTo(currentBestLocation);
        boolean isNear = distanceDelta < DISTANCE_THRESHOLD;
        boolean isFar = distanceDelta > DISTANCE_THRESHOLD;

        if (isNear) {
            Log.e(null, "NEAR");
            return false;
        } else if (isFar) {
            Log.e(null, "ffar");
            return true;
        }
        Log.e(null, "HERE FALSE!!!!");
        return false;
    }

    public static void servicesAlert(final Context context) {
        Dialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(R.string.location_is_off)
                .setPositiveButton(R.string.enable_location, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }

                })
                .create();

        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    public static void sendLocationToServer(Location location, MainFragment main, ImageButton button) {
        if (isBetterLocation(location, bestLastLocation) && main != null) {
            Server
                    .getJboltService()
                    .updateCoordinates(new UpdateCoordinates(
                            main.getAppUser().getAccessToken()
                            , main.getAppUser().getId()
                            , location.getLatitude()
                            , location.getLongitude()
                    ), new PostCallback(main.getActivity()));

            bestLastLocation = location;
            Log.e(null, "Sent GPS coords: " + location.getLatitude() + ", " + location.getLongitude());
        }

        handler.removeCallbacks(stopLocating);
        if (button != null && main != null) {
            main.refreshList();
        }
    }

    private static class MainLocationListener implements LocationListener {

        private MainFragment main;
        private ImageButton button;

        MainLocationListener(MainFragment main, ImageButton button) {
            this.main = main;
            this.button = button;
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(null, "ON LOCATION CHANGED LISTENER");
            sendLocationToServer(location, main, button);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}