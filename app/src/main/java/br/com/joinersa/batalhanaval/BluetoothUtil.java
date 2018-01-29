package br.com.joinersa.batalhanaval;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;

/**
 * Created by joine on 09/09/2017.
 */

public class BluetoothUtil {

    public static void makeVisible(Context context, int time) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, time);
        context.startActivity(discoverableIntent);
    }
}
