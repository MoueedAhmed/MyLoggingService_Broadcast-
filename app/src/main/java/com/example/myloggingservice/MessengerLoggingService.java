package com.example.myloggingservice;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MessengerLoggingService extends Service {

    public MessengerLoggingService(){}

    final static int SAVE_TO_FILE = 1;
    String passedString;

    //Target we publish for clients to send messages to IncomingHandler.
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SAVE_TO_FILE:
                    Bundle bundle = (Bundle) msg.obj;
                    passedString = bundle.getString("msg");
                    SaveInputToFile(passedString);
                    Toast.makeText(getApplicationContext(), passedString, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    public void SaveInputToFile(String timeStampedInput){
        String filename = "LoggingServiceLog.txt";
        File externalFile;

        externalFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), filename);
        try{
            FileOutputStream fOutput = new FileOutputStream(externalFile, true);
            OutputStreamWriter wOutput = new OutputStreamWriter(fOutput);
            wOutput.append(timeStampedInput +"\n");
            wOutput.flush();
            wOutput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
