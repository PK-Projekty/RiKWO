package com.pkprojekty.rikwo.Sms;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.provider.Telephony;

import com.pkprojekty.rikwo.Entities.SmsData;

import java.util.List;

public class RestoreSms {
    private final Context context;

    public RestoreSms(Context context) {
        this.context = context;
    }

    public void setAllSms(List<List<SmsData>> smsDataList) {
        setAllSmsToInbox(smsDataList.get(0));
        setAllSmsToSent(smsDataList.get(1));
        setAllSmsToDraft(smsDataList.get(2));
        setAllSmsToOutbox(smsDataList.get(3));

    }

    public void setAllSmsToInbox(List<SmsData> smsInboxDataList) {
        for (SmsData smsData : smsInboxDataList) {
            ContentResolver contentResolver = context.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(Telephony.Sms.ADDRESS, smsData.Address);
            values.put(Telephony.Sms.BODY, smsData.Body);
            values.put(Telephony.Sms.CREATOR, smsData.Creator);
            values.put(Telephony.Sms.DATE, smsData.Date);
            values.put(Telephony.Sms.DATE_SENT, smsData.DateSent);
            values.put(Telephony.Sms.ERROR_CODE, smsData.ErrorCode);
            values.put(Telephony.Sms.LOCKED, smsData.Locked);
            values.put(Telephony.Sms.PERSON, smsData.Person);
            values.put(Telephony.Sms.PROTOCOL, smsData.Protocol);
            values.put(Telephony.Sms.READ, smsData.Read);
            values.put(Telephony.Sms.REPLY_PATH_PRESENT, smsData.ReplayPathPresent);
            values.put(Telephony.Sms.SEEN, smsData.Seen);
            values.put(Telephony.Sms.SERVICE_CENTER, smsData.ServiceCenter);
            values.put(Telephony.Sms.STATUS, smsData.Status);
            values.put(Telephony.Sms.SUBJECT, smsData.Subject);
            values.put(Telephony.Sms.SUBSCRIPTION_ID, smsData.SubscriptionId);
            values.put(Telephony.Sms.THREAD_ID, smsData.ThreadId);
            values.put(Telephony.Sms.TYPE, smsData.Type);
            contentResolver.insert(Telephony.Sms.Inbox.CONTENT_URI, values);
        }
    }
    public void setAllSmsToSent(List<SmsData> smsSentDataList) {
        ContentResolver contentResolver = context.getContentResolver();
        for (SmsData smsData : smsSentDataList) {
            ContentValues values = new ContentValues();
            values.put(Telephony.Sms.ADDRESS, smsData.Address);
            values.put(Telephony.Sms.BODY, smsData.Body);
            values.put(Telephony.Sms.CREATOR, smsData.Creator);
            values.put(Telephony.Sms.DATE, smsData.Date);
            values.put(Telephony.Sms.DATE_SENT, smsData.DateSent);
            values.put(Telephony.Sms.ERROR_CODE, smsData.ErrorCode);
            values.put(Telephony.Sms.LOCKED, smsData.Locked);
            values.put(Telephony.Sms.PERSON, smsData.Person);
            values.put(Telephony.Sms.PROTOCOL, smsData.Protocol);
            values.put(Telephony.Sms.READ, smsData.Read);
            values.put(Telephony.Sms.REPLY_PATH_PRESENT, smsData.ReplayPathPresent);
            values.put(Telephony.Sms.SEEN, smsData.Seen);
            values.put(Telephony.Sms.SERVICE_CENTER, smsData.ServiceCenter);
            values.put(Telephony.Sms.STATUS, smsData.Status);
            values.put(Telephony.Sms.SUBJECT, smsData.Subject);
            values.put(Telephony.Sms.SUBSCRIPTION_ID, smsData.SubscriptionId);
            values.put(Telephony.Sms.THREAD_ID, smsData.ThreadId);
            values.put(Telephony.Sms.TYPE, smsData.Type);
            contentResolver.insert(Telephony.Sms.Inbox.CONTENT_URI, values);
        }
    }
    public void setAllSmsToDraft(List<SmsData> smsDraftDataList) {
        ContentResolver contentResolver = context.getContentResolver();
        for (SmsData smsData : smsDraftDataList) {
            ContentValues values = new ContentValues();
            values.put(Telephony.Sms.ADDRESS, smsData.Address);
            values.put(Telephony.Sms.BODY, smsData.Body);
            values.put(Telephony.Sms.CREATOR, smsData.Creator);
            values.put(Telephony.Sms.DATE, smsData.Date);
            values.put(Telephony.Sms.DATE_SENT, smsData.DateSent);
            values.put(Telephony.Sms.ERROR_CODE, smsData.ErrorCode);
            values.put(Telephony.Sms.LOCKED, smsData.Locked);
            values.put(Telephony.Sms.PERSON, smsData.Person);
            values.put(Telephony.Sms.PROTOCOL, smsData.Protocol);
            values.put(Telephony.Sms.READ, smsData.Read);
            values.put(Telephony.Sms.REPLY_PATH_PRESENT, smsData.ReplayPathPresent);
            values.put(Telephony.Sms.SEEN, smsData.Seen);
            values.put(Telephony.Sms.SERVICE_CENTER, smsData.ServiceCenter);
            values.put(Telephony.Sms.STATUS, smsData.Status);
            values.put(Telephony.Sms.SUBJECT, smsData.Subject);
            values.put(Telephony.Sms.SUBSCRIPTION_ID, smsData.SubscriptionId);
            values.put(Telephony.Sms.THREAD_ID, smsData.ThreadId);
            values.put(Telephony.Sms.TYPE, smsData.Type);
            contentResolver.insert(Telephony.Sms.Inbox.CONTENT_URI, values);
        }
    }
    public void setAllSmsToOutbox(List<SmsData> smsOutboxDataList) {
        ContentResolver contentResolver = context.getContentResolver();
        for (SmsData smsData : smsOutboxDataList) {
            ContentValues values = new ContentValues();
            values.put(Telephony.Sms.ADDRESS, smsData.Address);
            values.put(Telephony.Sms.BODY, smsData.Body);
            values.put(Telephony.Sms.CREATOR, smsData.Creator);
            values.put(Telephony.Sms.DATE, smsData.Date);
            values.put(Telephony.Sms.DATE_SENT, smsData.DateSent);
            values.put(Telephony.Sms.ERROR_CODE, smsData.ErrorCode);
            values.put(Telephony.Sms.LOCKED, smsData.Locked);
            values.put(Telephony.Sms.PERSON, smsData.Person);
            values.put(Telephony.Sms.PROTOCOL, smsData.Protocol);
            values.put(Telephony.Sms.READ, smsData.Read);
            values.put(Telephony.Sms.REPLY_PATH_PRESENT, smsData.ReplayPathPresent);
            values.put(Telephony.Sms.SEEN, smsData.Seen);
            values.put(Telephony.Sms.SERVICE_CENTER, smsData.ServiceCenter);
            values.put(Telephony.Sms.STATUS, smsData.Status);
            values.put(Telephony.Sms.SUBJECT, smsData.Subject);
            values.put(Telephony.Sms.SUBSCRIPTION_ID, smsData.SubscriptionId);
            values.put(Telephony.Sms.THREAD_ID, smsData.ThreadId);
            values.put(Telephony.Sms.TYPE, smsData.Type);
            contentResolver.insert(Telephony.Sms.Inbox.CONTENT_URI, values);
        }
    }
}
