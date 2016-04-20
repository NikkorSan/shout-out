package com.mikarney.jernil.shoutout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Jernil on 3/28/2016.
 */
public class WidgetBroadcastReceiver extends BroadcastReceiver{

        public WidgetBroadcastReceiver(){
        }
        @Override
        public void onReceive(Context context, Intent arg1) {
            int widget = arg1.getIntExtra("Widget", -1);

            Toast.makeText(context, "Widget pressed: " + widget, Toast.LENGTH_SHORT).show();
        }
    }

