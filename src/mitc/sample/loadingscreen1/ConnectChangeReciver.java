package mitc.sample.loadingscreen1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * @author Javen
 * 
 */
public class ConnectChangeReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo  mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetInfo.isConnected()) {
          Toast toast =  Toast.makeText(context, "ʹ��wifi����", 4000);
          toast.show();
          return ;
        }
        if(mobNetInfo.isConnected())
        {
         Toast toast =  Toast.makeText(context, "ʹ���ƶ���������", 4000);
         toast.show();  
         return ;
        }
        Toast toast =  Toast.makeText(context, "��������", 4000);
        toast.show();  
    }

    
}