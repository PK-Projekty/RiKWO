package com.pkprojekty.rikwo.Sms;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;

import com.pkprojekty.rikwo.Entities.SmsData;

import java.util.ArrayList;
import java.util.List;

public class BackupSms {
    private final Context context;

    public BackupSms(Context context) {
        this.context = context;
    }

    private List<SmsData> getAllSmsFromInbox() {
        List<SmsData> smsInboxDataList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor query = contentResolver.query(
                Telephony.Sms.Inbox.CONTENT_URI,
                null,
                null,
                null,
                Telephony.Sms.Inbox.DEFAULT_SORT_ORDER
        );
        if (query != null) {
            try {
                while (query.moveToNext()) {
                    SmsData smsData = new SmsData();

                    smsData.Address = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Inbox.ADDRESS
                            )
                    );
                    smsData.Body = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Inbox.BODY
                            )
                    );
                    smsData.Creator = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Inbox.CREATOR
                            )
                    );
                    smsData.Date = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Inbox.DATE
                            )
                    );
                    smsData.DateSent = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Inbox.DATE_SENT
                            )
                    );
                    smsData.ErrorCode = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Inbox.ERROR_CODE
                            )
                    );
                    smsData.Locked = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Inbox.LOCKED
                            )
                    );
                    smsData.Person = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Inbox.PERSON
                            )
                    );
                    smsData.Protocol = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Inbox.PROTOCOL
                            )
                    );
                    smsData.Read = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Inbox.READ
                            )
                    );
                    smsData.ReplayPathPresent = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Inbox.REPLY_PATH_PRESENT
                            )
                    );
                    smsData.Seen = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Inbox.SEEN
                            )
                    );
                    smsData.ServiceCenter = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Inbox.SERVICE_CENTER
                            )
                    );
                    smsData.Status = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Inbox.STATUS
                            )
                    );
                    smsData.Subject = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Inbox.SUBJECT
                            )
                    );
                    smsData.SubscriptionId = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Inbox.SUBSCRIPTION_ID
                            )
                    );
                    smsData.ThreadId = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Inbox.THREAD_ID
                            )
                    );
                    smsData.Type = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Inbox.TYPE
                            )
                    );
                    smsData.MessageTypeAll = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Inbox.MESSAGE_TYPE_ALL)
                            )
                    );
                    smsData.MessageTypeDraft = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Inbox.MESSAGE_TYPE_DRAFT)
                            )
                    );
                    smsData.MessageTypeFailed = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Inbox.MESSAGE_TYPE_FAILED)
                            )
                    );
                    smsData.MessageTypeInbox = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Inbox.MESSAGE_TYPE_INBOX)
                            )
                    );
                    smsData.MessageTypeOutbox = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Inbox.MESSAGE_TYPE_OUTBOX)
                            )
                    );
                    smsData.MessageTypeQueded = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Inbox.MESSAGE_TYPE_QUEUED)
                            )
                    );
                    smsData.MessageTypeSent = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Inbox.MESSAGE_TYPE_SENT)
                            )
                    );
                    smsData.StatusComplete = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Inbox.STATUS_COMPLETE)
                            )
                    );
                    smsData.StatusFailed = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Inbox.STATUS_FAILED)
                            )
                    );
                    smsData.StatusNone = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Inbox.STATUS_NONE)
                            )
                    );
                    smsData.StatusPending = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Inbox.STATUS_PENDING)
                            )
                    );

                    smsInboxDataList.add(smsData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                query.close();
            }
        }

        return smsInboxDataList;
    }

    private List<SmsData> getAllSmsFromDraft() {
        List<SmsData> smsDraftDataList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor query = contentResolver.query(
                Telephony.Sms.Draft.CONTENT_URI,
                null,
                null,
                null,
                Telephony.Sms.Draft.DEFAULT_SORT_ORDER
        );
        if (query != null) {
            try {
                while (query.moveToNext()) {
                    SmsData smsData = new SmsData();

                    smsData.Address = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Draft.ADDRESS
                            )
                    );
                    smsData.Body = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Draft.BODY
                            )
                    );
                    smsData.Creator = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Draft.CREATOR
                            )
                    );
                    smsData.Date = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Draft.DATE
                            )
                    );
                    smsData.DateSent = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Draft.DATE_SENT
                            )
                    );
                    smsData.ErrorCode = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Draft.ERROR_CODE
                            )
                    );
                    smsData.Locked = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Draft.LOCKED
                            )
                    );
                    smsData.Person = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Draft.PERSON
                            )
                    );
                    smsData.Protocol = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Draft.PROTOCOL
                            )
                    );
                    smsData.Read = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Draft.READ
                            )
                    );
                    smsData.ReplayPathPresent = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Draft.REPLY_PATH_PRESENT
                            )
                    );
                    smsData.Seen = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Draft.SEEN
                            )
                    );
                    smsData.ServiceCenter = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Draft.SERVICE_CENTER
                            )
                    );
                    smsData.Status = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Draft.STATUS
                            )
                    );
                    smsData.Subject = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Draft.SUBJECT
                            )
                    );
                    smsData.SubscriptionId = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Draft.SUBSCRIPTION_ID
                            )
                    );
                    smsData.ThreadId = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Draft.THREAD_ID
                            )
                    );
                    smsData.Type = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Draft.TYPE
                            )
                    );
                    smsData.MessageTypeAll = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Draft.MESSAGE_TYPE_ALL)
                            )
                    );
                    smsData.MessageTypeDraft = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Draft.MESSAGE_TYPE_DRAFT)
                            )
                    );
                    smsData.MessageTypeFailed = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Draft.MESSAGE_TYPE_FAILED)
                            )
                    );
                    smsData.MessageTypeInbox = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Draft.MESSAGE_TYPE_INBOX)
                            )
                    );
                    smsData.MessageTypeOutbox = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Draft.MESSAGE_TYPE_OUTBOX)
                            )
                    );
                    smsData.MessageTypeQueded = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Draft.MESSAGE_TYPE_QUEUED)
                            )
                    );
                    smsData.MessageTypeSent = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Draft.MESSAGE_TYPE_SENT)
                            )
                    );
                    smsData.StatusComplete = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Draft.STATUS_COMPLETE)
                            )
                    );
                    smsData.StatusFailed = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Draft.STATUS_FAILED)
                            )
                    );
                    smsData.StatusNone = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Draft.STATUS_NONE)
                            )
                    );
                    smsData.StatusPending = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Draft.STATUS_PENDING)
                            )
                    );

                    smsDraftDataList.add(smsData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                query.close();
            }
        }

        return smsDraftDataList;
    }

    private List<SmsData> getAllSmsFromOutbox() {
        List<SmsData> smsOutboxDataList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor query = contentResolver.query(
                Telephony.Sms.Outbox.CONTENT_URI,
                null,
                null,
                null,
                Telephony.Sms.Outbox.DEFAULT_SORT_ORDER
        );
        if (query != null) {
            try {
                while (query.moveToNext()) {
                    SmsData smsData = new SmsData();

                    smsData.Address = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Outbox.ADDRESS
                            )
                    );
                    smsData.Body = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Outbox.BODY
                            )
                    );
                    smsData.Creator = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Outbox.CREATOR
                            )
                    );
                    smsData.Date = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Outbox.DATE
                            )
                    );
                    smsData.DateSent = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Outbox.DATE_SENT
                            )
                    );
                    smsData.ErrorCode = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Outbox.ERROR_CODE
                            )
                    );
                    smsData.Locked = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Outbox.LOCKED
                            )
                    );
                    smsData.Person = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Outbox.PERSON
                            )
                    );
                    smsData.Protocol = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Outbox.PROTOCOL
                            )
                    );
                    smsData.Read = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Outbox.READ
                            )
                    );
                    smsData.ReplayPathPresent = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Outbox.REPLY_PATH_PRESENT
                            )
                    );
                    smsData.Seen = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Outbox.SEEN
                            )
                    );
                    smsData.ServiceCenter = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Outbox.SERVICE_CENTER
                            )
                    );
                    smsData.Status = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Outbox.STATUS
                            )
                    );
                    smsData.Subject = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Outbox.SUBJECT
                            )
                    );
                    smsData.SubscriptionId = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Outbox.SUBSCRIPTION_ID
                            )
                    );
                    smsData.ThreadId = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Outbox.THREAD_ID
                            )
                    );
                    smsData.Type = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Outbox.TYPE
                            )
                    );
                    smsData.MessageTypeAll = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Outbox.MESSAGE_TYPE_ALL)
                            )
                    );
                    smsData.MessageTypeDraft = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Outbox.MESSAGE_TYPE_DRAFT)
                            )
                    );
                    smsData.MessageTypeFailed = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Outbox.MESSAGE_TYPE_FAILED)
                            )
                    );
                    smsData.MessageTypeInbox = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Outbox.MESSAGE_TYPE_INBOX)
                            )
                    );
                    smsData.MessageTypeOutbox = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Outbox.MESSAGE_TYPE_OUTBOX)
                            )
                    );
                    smsData.MessageTypeQueded = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Outbox.MESSAGE_TYPE_QUEUED)
                            )
                    );
                    smsData.MessageTypeSent = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Outbox.MESSAGE_TYPE_SENT)
                            )
                    );
                    smsData.StatusComplete = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Outbox.STATUS_COMPLETE)
                            )
                    );
                    smsData.StatusFailed = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Outbox.STATUS_FAILED)
                            )
                    );
                    smsData.StatusNone = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Outbox.STATUS_NONE)
                            )
                    );
                    smsData.StatusPending = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Outbox.STATUS_PENDING)
                            )
                    );

                    smsOutboxDataList.add(smsData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                query.close();
            }
        }

        return smsOutboxDataList;
    }

    private List<SmsData> getAllSmsFromSent() {
        List<SmsData> smsSentDataList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor query = contentResolver.query(
                Telephony.Sms.Sent.CONTENT_URI,
                null,
                null,
                null,
                Telephony.Sms.Sent.DEFAULT_SORT_ORDER
        );
        if (query != null) {
            try {
                while (query.moveToNext()) {
                    SmsData smsData = new SmsData();

                    smsData.Address = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Sent.ADDRESS
                            )
                    );
                    smsData.Body = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Sent.BODY
                            )
                    );
                    smsData.Creator = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Sent.CREATOR
                            )
                    );
                    smsData.Date = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Sent.DATE
                            )
                    );
                    smsData.DateSent = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Sent.DATE_SENT
                            )
                    );
                    smsData.ErrorCode = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Sent.ERROR_CODE
                            )
                    );
                    smsData.Locked = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Sent.LOCKED
                            )
                    );
                    smsData.Person = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Sent.PERSON
                            )
                    );
                    smsData.Protocol = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Sent.PROTOCOL
                            )
                    );
                    smsData.Read = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Sent.READ
                            )
                    );
                    smsData.ReplayPathPresent = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Sent.REPLY_PATH_PRESENT
                            )
                    );
                    smsData.Seen = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Sent.SEEN
                            )
                    );
                    smsData.ServiceCenter = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Sent.SERVICE_CENTER
                            )
                    );
                    smsData.Status = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Sent.STATUS
                            )
                    );
                    smsData.Subject = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Sent.SUBJECT
                            )
                    );
                    smsData.SubscriptionId = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Sent.SUBSCRIPTION_ID
                            )
                    );
                    smsData.ThreadId = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Sent.THREAD_ID
                            )
                    );
                    smsData.Type = query.getString(
                            query.getColumnIndexOrThrow(
                                    Telephony.Sms.Sent.TYPE
                            )
                    );
                    smsData.MessageTypeAll = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Sent.MESSAGE_TYPE_ALL)
                            )
                    );
                    smsData.MessageTypeDraft = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Sent.MESSAGE_TYPE_DRAFT)
                            )
                    );
                    smsData.MessageTypeFailed = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Sent.MESSAGE_TYPE_FAILED)
                            )
                    );
                    smsData.MessageTypeInbox = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Sent.MESSAGE_TYPE_INBOX)
                            )
                    );
                    smsData.MessageTypeOutbox = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Sent.MESSAGE_TYPE_OUTBOX)
                            )
                    );
                    smsData.MessageTypeQueded = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Sent.MESSAGE_TYPE_QUEUED)
                            )
                    );
                    smsData.MessageTypeSent = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Sent.MESSAGE_TYPE_SENT)
                            )
                    );
                    smsData.StatusComplete = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Sent.STATUS_COMPLETE)
                            )
                    );
                    smsData.StatusFailed = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Sent.STATUS_FAILED)
                            )
                    );
                    smsData.StatusNone = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Sent.STATUS_NONE)
                            )
                    );
                    smsData.StatusPending = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(Telephony.Sms.Sent.STATUS_PENDING)
                            )
                    );

                    smsSentDataList.add(smsData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                query.close();
            }
        }

        return smsSentDataList;
    }

    public List<List<SmsData>> getAllSms() {
        List<List<SmsData>> smsDataList = new ArrayList<>();

        smsDataList.add(getAllSmsFromDraft());
        smsDataList.add(getAllSmsFromInbox());
        smsDataList.add(getAllSmsFromOutbox());
        smsDataList.add(getAllSmsFromSent());

        return smsDataList;
    }

    public int countMessagesInDraft() {
        int messagesCount = 0;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor query = contentResolver.query(
                Telephony.Sms.Draft.CONTENT_URI,
                null,
                null,
                null,
                Telephony.Sms.Draft.DEFAULT_SORT_ORDER
        );
        if (query != null) {
            try {
                messagesCount = query.getCount();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                query.close();
            }
        }

        return messagesCount;
    }

    public int countMessagesInInbox() {
        int messagesCount = 0;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor query = contentResolver.query(
                Telephony.Sms.Inbox.CONTENT_URI,
                null,
                null,
                null,
                Telephony.Sms.Inbox.DEFAULT_SORT_ORDER
        );
        if (query != null) {
            try {
                messagesCount = query.getCount();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                query.close();
            }
        }

        return messagesCount;
    }

    public int countMessagesInOutbox() {
        int messagesCount = 0;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor query = contentResolver.query(
                Telephony.Sms.Outbox.CONTENT_URI,
                null,
                null,
                null,
                Telephony.Sms.Outbox.DEFAULT_SORT_ORDER
        );
        if (query != null) {
            try {
                messagesCount = query.getCount();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                query.close();
            }
        }

        return messagesCount;
    }

    public int countMessagesInSent() {
        int messagesCount = 0;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor query = contentResolver.query(
                Telephony.Sms.Sent.CONTENT_URI,
                null,
                null,
                null,
                Telephony.Sms.Sent.DEFAULT_SORT_ORDER
        );
        if (query != null) {
            try {
                messagesCount = query.getCount();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                query.close();
            }
        }

        return messagesCount;
    }
}
