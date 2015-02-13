# Interceptor
Android debugging area for silent sms detection

Simple android logcat monitor to check if unique low level sms data to detect TYPE 0 silent sms etc.

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
	
	
	I be suprised if this worked on other sony models but its something to work off.
