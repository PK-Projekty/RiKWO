package com.pkprojekty.rikwo.Xml;

import android.content.Context;
import android.os.Environment;
import android.util.Xml;
import android.widget.Toast;

import com.pkprojekty.rikwo.Entities.SmsData;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FileHandler {
    private final Context context;

    public FileHandler(Context context) {
        this.context = context;
    }

    public void storeSmsInXml(List<List<SmsData>> smsData) {
        ZoneId z = ZoneId.of("Europe/Warsaw");
        ZonedDateTime zdt = ZonedDateTime.now(z);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String filename = zdt.format(formatter) + "-sms.xml";
        //System.out.println(context.getFilesDir() + filename);
        //System.out.println(Environment.getExternalStorageDirectory() + "/Documents/" + filename);
        try {
            XmlSerializer serializer = Xml.newSerializer();
            File file = new File(
                    Environment.getExternalStorageDirectory() + "/Documents/",
                    filename
            );
            FileOutputStream os = new FileOutputStream(file);
            serializer.setOutput(os, "UTF-8");
            serializer.startDocument("UTF-8", true);

            int countBox = 0;
            for (List<SmsData> smsListData: smsData) {
                switch (countBox){
                    case 0:
                        serializer.startTag(null, "Inbox");
                        break;
                    case 1:
                        serializer.startTag(null, "Sent");
                        break;
                    case 2:
                        serializer.startTag(null, "Draft");
                        break;
                    case 3:
                        serializer.startTag(null, "Outbox");
                        break;
                }

                int countMessage = 0;
                for (SmsData sms: smsListData) {
                    serializer.startTag(null, "Message");
                    serializer.attribute(null, "id", countMessage + "");

                    serializer.startTag(null, "Address");
                    serializer.text(String.valueOf(sms.Address));
                    serializer.endTag(null, "Address");

                    serializer.startTag(null, "Body");
                    serializer.text(String.valueOf(sms.Body));
                    serializer.endTag(null, "Body");

                    serializer.startTag(null, "Creator");
                    serializer.text(String.valueOf(sms.Creator));
                    serializer.endTag(null, "Creator");

                    serializer.startTag(null, "Date");
                    serializer.text(String.valueOf(sms.Date));
                    serializer.endTag(null, "Date");

                    serializer.startTag(null, "DateSent");
                    serializer.text(String.valueOf(sms.DateSent));
                    serializer.endTag(null, "DateSent");

                    serializer.startTag(null, "ErrorCode");
                    serializer.text(String.valueOf(sms.ErrorCode));
                    serializer.endTag(null, "ErrorCode");

                    serializer.startTag(null, "Locked");
                    serializer.text(String.valueOf(sms.Locked));
                    serializer.endTag(null, "Locked");

                    serializer.startTag(null, "Person");
                    serializer.text(String.valueOf(sms.Person));
                    serializer.endTag(null, "Person");

                    serializer.startTag(null, "Protocol");
                    serializer.text(String.valueOf(sms.Protocol));
                    serializer.endTag(null, "Protocol");

                    serializer.startTag(null, "Read");
                    serializer.text(String.valueOf(sms.Read));
                    serializer.endTag(null, "Read");

                    serializer.startTag(null, "ReplayPathPresent");
                    serializer.text(String.valueOf(sms.ReplayPathPresent));
                    serializer.endTag(null, "ReplayPathPresent");

                    serializer.startTag(null, "Seen");
                    serializer.text(String.valueOf(sms.Seen));
                    serializer.endTag(null, "Seen");

                    serializer.startTag(null, "ServiceCenter");
                    serializer.text(String.valueOf(sms.ServiceCenter));
                    serializer.endTag(null, "ServiceCenter");

                    serializer.startTag(null, "Status");
                    serializer.text(String.valueOf(sms.Status));
                    serializer.endTag(null, "Status");

                    serializer.startTag(null, "Subject");
                    serializer.text(String.valueOf(sms.Subject));
                    serializer.endTag(null, "Subject");

                    serializer.startTag(null, "SubscriptionId");
                    serializer.text(String.valueOf(sms.SubscriptionId));
                    serializer.endTag(null, "SubscriptionId");

                    serializer.startTag(null, "ThreadId");
                    serializer.text(String.valueOf(sms.ThreadId));
                    serializer.endTag(null, "ThreadId");

                    serializer.startTag(null, "Type");
                    serializer.text(String.valueOf(sms.Type));
                    serializer.endTag(null, "Type");

                    serializer.startTag(null, "MessageTypeAll");
                    serializer.text(String.valueOf(sms.MessageTypeAll));
                    serializer.endTag(null, "MessageTypeAll");

                    serializer.startTag(null, "MessageTypeDraft");
                    serializer.text(String.valueOf(sms.MessageTypeDraft));
                    serializer.endTag(null, "MessageTypeDraft");

                    serializer.startTag(null, "MessageTypeFailed");
                    serializer.text(String.valueOf(sms.MessageTypeFailed));
                    serializer.endTag(null, "MessageTypeFailed");

                    serializer.startTag(null, "MessageTypeInbox");
                    serializer.text(String.valueOf(sms.MessageTypeInbox));
                    serializer.endTag(null, "MessageTypeInbox");

                    serializer.startTag(null, "MessageTypeOutbox");
                    serializer.text(String.valueOf(sms.MessageTypeOutbox));
                    serializer.endTag(null, "MessageTypeOutbox");

                    serializer.startTag(null, "MessageTypeQueded");
                    serializer.text(String.valueOf(sms.MessageTypeQueded));
                    serializer.endTag(null, "MessageTypeQueded");

                    serializer.startTag(null, "MessageTypeSent");
                    serializer.text(String.valueOf(sms.MessageTypeSent));
                    serializer.endTag(null, "MessageTypeSent");

                    serializer.startTag(null, "StatusComplete");
                    serializer.text(String.valueOf(sms.StatusComplete));
                    serializer.endTag(null, "StatusComplete");

                    serializer.startTag(null, "StatusFailed");
                    serializer.text(String.valueOf(sms.StatusFailed));
                    serializer.endTag(null, "StatusFailed");

                    serializer.startTag(null, "StatusNone");
                    serializer.text(String.valueOf(sms.StatusNone));
                    serializer.endTag(null, "StatusNone");

                    serializer.startTag(null, "StatusPending");
                    serializer.text(String.valueOf(sms.StatusPending));
                    serializer.endTag(null, "StatusPending");

                    serializer.endTag(null, "Message");
                    countMessage++;
                }

                switch (countBox){
                    case 0:
                        serializer.endTag(null, "Inbox");
                        break;
                    case 1:
                        serializer.endTag(null, "Sent");
                        break;
                    case 2:
                        serializer.endTag(null, "Draft");
                        break;
                    case 3:
                        serializer.endTag(null, "Outbox");
                        break;
                }
                countBox++;
            }
            serializer.endDocument();
            serializer.flush();
            os.close();
            Toast.makeText(context, "Operation successful", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            // Tutaj być może wrzut statusu operacji do logera?
        }
    }

}
