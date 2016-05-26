package roboniania.com.roboniania_android;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import roboniania.com.roboniania_android.api.network.NetworkProvider;
import roboniania.com.roboniania_android.storage.SharedPreferenceStorage;

/**
 * Created by Mateusz on 04.05.2016.
 */
public class PairingRobot {

    private static String pairKeyString;

    public String getPairKeyString() {
        return pairKeyString;
    }

    public static void showPairDialog(final Context context, final SharedPreferenceStorage userLocalStorage, final Handler handler) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final EditText pairKey = new EditText(context);
        pairKey.setTextColor(Color.RED);
        pairKey.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setMessage("Enter robot's pair-key:");
        alert.setTitle("Connecting..");
        alert.setView(pairKey);


        alert.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                pairKeyString = pairKey.getText().toString();
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        startPairing(context, userLocalStorage, handler);
                    }
                }).start();
            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    private static void startPairing(final Context context, SharedPreferenceStorage userLocalStorage, final Handler handler) {
        final NetworkProvider networkProvider = new NetworkProvider(context, userLocalStorage);
        try {
            networkProvider.pair(userLocalStorage, pairKeyString, new NetworkProvider.OnResponseReceivedListener() {

                @Override
                public void onResponseReceived() {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            if (networkProvider.getRESPONSE_CODE() == 200 || networkProvider.getRESPONSE_CODE() == 202) {
                                Toast.makeText(context, R.string.successfully_paired, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, R.string.wrong_match, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
