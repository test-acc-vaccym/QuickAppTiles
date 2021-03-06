package me.manasrawat.quickapptiles;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Base64;
import cyanogenmod.app.CMStatusBarManager;
import cyanogenmod.app.CustomTile;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

class CMTileBuilder {

    CMTileBuilder(Context context, PackageManager packMan) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String pack = sharedPreferences.getString("pack", context.getPackageName());

        byte[] bits = Base64.decode(sharedPreferences.getString("icon", "icon").getBytes(), Base64.DEFAULT);
        Bitmap decodee = BitmapFactory.decodeByteArray(bits, 0, bits.length);

        PendingIntent launchPend = PendingIntent.getActivity(context, 0,
                packMan.getLaunchIntentForPackage(pack), FLAG_UPDATE_CURRENT);
        PendingIntent settingsPend = PendingIntent.getActivity(context, 0,
                new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .setData(Uri.parse("package:" + pack)), FLAG_UPDATE_CURRENT);

        CMStatusBarManager.getInstance(context)
                .publishTile(context.getString(R.string.app_name), 0, new CustomTile.Builder(context)
                        .setLabel(sharedPreferences.getString("label", context.getString(R.string.app_name)))
                        .setOnClickIntent(launchPend)
                        .setOnLongClickIntent(settingsPend)
                        .setIcon(decodee)
                        .build());
    }
}
