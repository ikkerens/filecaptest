package com.ikkerens.filecaptest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/")
public class FilecapSendingApplication extends Application {
    public static String FILECAP_HOSTNAME = "centcom.filecap.net";

    // Under absolutely no circumstances would I put this api key in the code.
    // I would usually have this set up through environment variables.
    // My usage here is STRICTLY for the sake of demonstration.
    public static String FILECAP_APIKEY = "";
}