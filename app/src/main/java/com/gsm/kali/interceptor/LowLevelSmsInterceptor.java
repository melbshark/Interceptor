/*
    Created by Paul Kinsella 2015 <kali.gsm.rtl.sdr@gmail.com>
    Use as you like.

    Low Level Sms Debugger for catching stuff a Broadcast Receiver wont get.

    This is a base class used for catching Low Level Sms Data
    which can then be used to identify.
        TYPE 0
        Silent C0
        Silent Wap Push

    Tested on a rooted Sony Xperia J
        Model ST26i
        Android 4.1.2
        Kernel Version 3.4.0 user@sibldsrv-03
        Baseband version 7x25A_M:2540.41.00.89.001.002_A:2540.41.00.89.001.047
        Build 11.2.A.0.21



     To Start Programmatically
                    LowLevelSmsInterceptor.setRUNNING(true);
                    new LowLevelSmsInterceptor().execute("");
     To Stop Programmatically
                    LowLevelSmsInterceptor.setRUNNING(false);


	Easy to modify this file if you can see the sms in the logcat -b radio
	add the string to the array below exactly as printed and it should output the data.	



 */

package com.gsm.kali.interceptor;

import android.os.AsyncTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class LowLevelSmsInterceptor extends AsyncTask<String, String, String> {
    private DataInputStream dis;
    private DataOutputStream dos;
    static Boolean RESEND_COMMAND = false;
    static Boolean RUNNING = false;


                                                   //text that comes after string
    static String NEW_SMS_TAGS[] = {"SMS SC address:",
                                    "SMS originating address:","SMS message body (raw):",
                                    "SMS TP-PID:",//0 data coding scheme: 192
                                    "SMS SC timestamp:",//1423743345000
                                    "MWI in DCS for Vmail. DCS =",//192 Dont store = true vmail count = 0"
                                    "SMS message body (raw):",//message body is here
                                        "[GsmSMSDispatcher]isTypeZero=",//true or false
                                        "[GsmSMSDispatcher]isUsimDataDownload=",//true or false
                                        "[GsmSMSDispatcher]isMWISetMessage=",//true or false
                                        "[GsmSMSDispatcher]isMWIClearMessage=",//true or false
                                        "[GsmSMSDispatcher]isStorageAvailable=",//true,MessageClass=UNKNOWN
                                        "[GsmSMSDispatcher]updateMessageWaitingIndicator:",//mwi=0
                                        "Received voice mail indicator clear SMS shouldStore=",//true or false
                                             "[SmsMessage]setSubId: subId=",
                                             "[SmsMessage]getProtocolIdentifier:",
                                             "[SmsMessage]isReplace:",
                                             "[SmsMessage]isCphsMwiMessage:",
                                             "[SmsMessage]getProtocolIdentifier:",
                                             "[SmsMessage]isReplyPathPresent:",
                                             "[SmsMessage]getServiceCenterAddress:SCAddress=",
                                                           "[WapPush]Rx:",
                                                           "[WapPush]transactionId=",//11,pduType=6
                                                           "[WapPush]headerStartIndex=",// int value
                                                           "[WapPush]mimeType=",//application/vnd.wap.sic,binaryContentType=46,index=7
                                                           "[WapPush]fall back to existing handler"//no data

                                    };

    public static Boolean getRUNNING() {
        return RUNNING;
    }

    public static void setRUNNING(Boolean RUNNING) {
        LowLevelSmsInterceptor.RUNNING = RUNNING;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Notify Before task starts

    }

    // Download Music File from Internet
    @Override
    protected String doInBackground(String... misc_data) {
        String  logcatstr = "logcat -b radio\n";

        try {
            Runtime r = Runtime.getRuntime();
            Process process = r.exec("su");
            dos = new DataOutputStream(process.getOutputStream());


            //silent sms code
            dos.writeBytes(logcatstr);
            dos.flush();

            dis = new DataInputStream(process.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String debugstring = "";


        while (getRUNNING()) {
            try {

                int bufferlen = dis.available();
                //System.out.println("DEBUG>> Buff Len " +bufferlen);

                if (bufferlen != 0) {
                    byte[] b = new byte[bufferlen];
                    dis.read(b);

                    debugstring = new String(b);
                    System.out.println("DEBUG>> " +debugstring);

                    //silent sms code
                    String split[] = debugstring.split("\n");

                    if(split.length < 150) {

                        publishProgress(split);
                    }

                } else {
                    Thread.sleep(100);
                }

            } catch (IOException e) {
                if (e.getMessage() != null)
                    System.out.println(e.getMessage());
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {
            dos.writeBytes("exit\n");
            dos.flush();
            dos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    // Talk to the main here
    protected void onProgressUpdate(String... progress) {

            for(int arrayindex = 0;arrayindex < NEW_SMS_TAGS.length;arrayindex++)
            {
                for(int x =0;x <progress.length;x++){//check all process buffer for strings
                    if (progress[x].contains(NEW_SMS_TAGS[arrayindex]))
                    {
                        Main.edit_debug.append(progress[x]+"\n");
                    }
                }
            }
    }


    @Override
    protected void onPostExecute(String file_url) {
        //All tasks finished Notify user here

    }
}