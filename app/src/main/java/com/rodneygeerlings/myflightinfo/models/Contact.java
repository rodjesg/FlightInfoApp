package com.rodneygeerlings.myflightinfo.models;

import android.net.Uri;

import java.util.List;

public class Contact {
    private long contactId;
    private String ContactlookupKey;
    private String ContactdisplayName;
    private String ContactType;
    private List<String> ContactphoneNumbers;
    private Uri ContactphotoUri;

    public Contact() {
    }
}