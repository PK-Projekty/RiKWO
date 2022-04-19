package com.pkprojekty.rikwo.Entities;

public class SmsData {
    /**
     * Struktura danych zawierająca informacje o wiadomości sms
     * developer.android.com/reference/android/provider/Telephony.TextBasedSmsColumns
     */
    public String Address;              // API level 19 The address of the other party.
    public String Body;                 // API level 19 The body of the message.
    public String Creator;              // API level 21 The identity of the sender of a sent message.
    public String Date;                 // API level 19 The date the message was received.
    public String DateSent;             // API level 19 The date the message was sent.
    public String ErrorCode;            // API level 19 Error code associated with sending or receiving this message Type: INTEGER
    public String Locked;               // API level 19 Is the message locked? Type: INTEGER (boolean)
    public String Person;               // API level 19 The ID of the sender of the conversation, if present.
    public String Protocol;             // API level 19 The protocol identifier code.
    public String Read;                 // API level 19 Has the message been read? Type: INTEGER (boolean)
    public String ReplayPathPresent;    // API level 19 Is the TP-Reply-Path flag set? Type: BOOLEAN
    public String Seen;                 // API level 19 Has the message been seen by the user? The "seen" flag determines whether we need to show a notification.
    public String ServiceCenter;        // API level 19 The service center (SC) through which to send the message, if present.
    public String Status;               // API level 19 TP-Status value for the message, or -1 if no status has been received.
    public String Subject;              // API level 19 The subject of the message, if present.
    public String SubscriptionId;       // API level 19 The subscription to which the message belongs to.
    public String ThreadId;             // API level 22 The thread ID of the message.
    public String Type;                 // API level 19 The type of message.

    public int MessageTypeAll;          // API level 19 Message type: all messages.
    public int MessageTypeDraft;        // API level 19 Message type: drafts.
    public int MessageTypeFailed;       // API level 19 Message type: failed outgoing message.
    public int MessageTypeInbox;        // API level 19 Message type: inbox.
    public int MessageTypeOutbox;       // API level 19 Message type: outbox.
    public int MessageTypeQueded;       // API level 19 Message type: queued to send later.
    public int MessageTypeSent;         // API level 19 Message type: sent messages.
    public int StatusComplete;          // API level 19 TP-Status: complete.
    public int StatusFailed;            // API level 19 TP-Status: failed.
    public int StatusNone;              // API level 19 TP-Status: no status received.
    public int StatusPending;           // API level 19 TP-Status: pending.

}
