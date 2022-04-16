package com.pkprojekty.rikwo.sms;

public class Sms {
    /**
     * Struktura danych zawierająca informacje o wiadomości sms
     * https://developer.android.com/reference/android/provider/Telephony.TextBasedSmsColumns
     */
    public String Address;              // The address of the other party.
    public String Body;                 // The body of the message.
    public String Creator;              // The identity of the sender of a sent message.
    public String Date;                 // The date the message was received.
    public String DateSent;             // The date the message was sent.
    public String ErrorCode;            // Error code associated with sending or receiving this message Type: INTEGER
    public String Locked;               // Is the message locked? Type: INTEGER (boolean)
    public String Person;               // The ID of the sender of the conversation, if present.
    public String Protocol;             // The protocol identifier code.
    public String Read;                 // Has the message been read? Type: INTEGER (boolean)
    public String ReplayPathPresent;    // Is the TP-Reply-Path flag set? Type: BOOLEAN
    public String Seen;                 // Has the message been seen by the user? The "seen" flag determines whether we need to show a notification.
    public String ServiceCenter;        // The service center (SC) through which to send the message, if present.
    public String Status;               // TP-Status value for the message, or -1 if no status has been received.
    public String Subject;              // The subject of the message, if present.
    public String SubscriptionId;       // The subscription to which the message belongs to.
    public String ThreadId;             // The thread ID of the message.
    public String Type;                 // The type of message.
}
