package com.simplevoice;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.slice.Slice;
import androidx.slice.SliceProvider;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.ListBuilder.RowBuilder;
import androidx.slice.builders.SliceAction;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class MySliceProvider extends SliceProvider {

    @Override
    public boolean onCreateSliceProvider() {
        return true;
    }

    /**
     * Converts URL to content URI (i.e. content://com.energica...)
     */
    @Override
    @NonNull
    public Uri onMapIntentToUri(@Nullable Intent intent) {
        // Note: implementing this is only required if you plan on catching URL requests.
        // This is an example solution.
        Uri.Builder uriBuilder = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT);
        if (intent == null) return uriBuilder.build();
        Uri data = intent.getData();
        if (data != null && data.getPath() != null) {
            String path = data.getPath().replace("/", "");
            uriBuilder = uriBuilder.path(path);
        }
        Context context = getContext();
        if (context != null) {
            uriBuilder = uriBuilder.authority(context.getPackageName());
        }
        return uriBuilder.build();
    }

    /**
     * Construct the Slice and bind data if available.
     */
    public Slice onBindSlice(Uri sliceUri) {
        Context context = getContext();
        SliceAction activityAction = createActivityAction();
        if (context == null || activityAction == null) {
            return null;
        }
        if ("/command".equals(sliceUri.getPath())) {
            // Path recognized. Customize the Slice using the androidx.slice.builders API.
            // Note: ANRs and strict mode is enforced here so don"t do any heavy operations.
            // Only bind data that is currently available in memory.
            WritableMap params = Arguments.createMap(); // add here the data you want to send
            params.putString("event", "IntentListener"); // <- example
            params.putString("value", sliceUri.getQueryParameter("name")); // <- example

            //-------- here we receive command from google assistant
            //-------- we must send command to react-native
            //-------- then get response and set inside ListBuilder

            return new ListBuilder(getContext(), sliceUri, ListBuilder.INFINITY)
                    .addRow(
                            new RowBuilder()
                                    .setTitle("react-native response")
                                    .setPrimaryAction(activityAction)
                    )
                    .build();
        } else {
            // Error: Path not found.
            return new ListBuilder(getContext(), sliceUri, ListBuilder.INFINITY)
                    .addRow(
                            new RowBuilder()
                                    .setTitle("URI not found.")
                                    .setPrimaryAction(activityAction)
                    )
                    .build();
        }
    }

    public  SliceAction createActivityAction() {
        if (getContext() == null) {
            return null;
        }
        return SliceAction.create(
                PendingIntent.getActivity(
                        getContext(),
                        0,
                        new Intent(getContext(), MainActivity.class),
                        0
                ),
                null,
                ListBuilder.ICON_IMAGE,
                "simplevoice"
        );
    }

    /**
     * Slice has been pinned to external process. Subscribe to data source if necessary.
     */
    @Override
    public void onSlicePinned(Uri sliceUri) {
        // When data is received, call context.contentResolver.notifyChange(sliceUri, null) to
        // trigger MySliceProvider#onBindSlice(Uri) again.
    }

    /**
     * Unsubscribe from data source if necessary.
     */
    @Override
    public void onSliceUnpinned(Uri sliceUri) {
        // Remove any observers if necessary to avoid memory leaks.
    }


}