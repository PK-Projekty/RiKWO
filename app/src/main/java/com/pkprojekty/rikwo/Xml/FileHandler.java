package com.pkprojekty.rikwo.Xml;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;
import android.widget.Toast;

import androidx.documentfile.provider.DocumentFile;

import com.pkprojekty.rikwo.Entities.CallData;
import com.pkprojekty.rikwo.Entities.SmsData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private final Context context;

    public FileHandler(Context context) {
        this.context = context;
    }

    public void storeSmsInXml(List<List<SmsData>> smsData, DocumentFile file) {
//        ZoneId z = ZoneId.of("Europe/Warsaw");
//        ZonedDateTime zdt = ZonedDateTime.now(z);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//        String filename = zdt.format(formatter) + "-sms.xml";
        //System.out.println(context.getFilesDir() + filename);
        //System.out.println(Environment.getExternalStorageDirectory() + "/Documents/" + filename);
        FileOutputStream os;
        try {
            XmlSerializer serializer = Xml.newSerializer();
//            File file = new File(
//                    Environment.getExternalStorageDirectory() + "/Documents/",
//                    filename
//            );
            //FileOutputStream os = new FileOutputStream(file);
            os = (FileOutputStream) context.getContentResolver().openOutputStream(file.getUri());
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
            // Tutaj by?? mo??e wrzut statusu operacji do logera?
        }
    }

    public void storeCallLogInXml(List<CallData> callData, DocumentFile file) {
//        ZoneId z = ZoneId.of("Europe/Warsaw");
//        ZonedDateTime zdt = ZonedDateTime.now(z);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//        String filename = zdt.format(formatter) + "-calls.xml";
        FileOutputStream os;
        try {
            XmlSerializer serializer = Xml.newSerializer();
//            File file = new File(
//                    Environment.getExternalStorageDirectory() + "/Documents/",
//                    filename
//            );
//            FileOutputStream os = new FileOutputStream(file);
            os = (FileOutputStream) context.getContentResolver().openOutputStream(file.getUri());
            serializer.setOutput(os, "UTF-8");
            serializer.startDocument("UTF-8", true);
            serializer.startTag(null, "CallLog");

            int countCall = 0;
            for (CallData call: callData) {
                serializer.startTag(null, "call");
                serializer.attribute(null, "id", countCall + "");

                serializer.startTag(null, "Id");
                serializer.text(String.valueOf(call.Id));
                serializer.endTag(null, "Id");

                serializer.startTag(null, "CachedFormatedNumber");
                serializer.text(String.valueOf(call.CachedFormatedNumber));
                serializer.endTag(null, "CachedFormatedNumber");

                serializer.startTag(null, "CachedLookupUri");
                serializer.text(String.valueOf(call.CachedLookupUri));
                serializer.endTag(null, "CachedLookupUri");

                serializer.startTag(null, "CachedMatchedNumber");
                serializer.text(String.valueOf(call.CachedMatchedNumber));
                serializer.endTag(null, "CachedMatchedNumber");

                serializer.startTag(null, "CachedName");
                serializer.text(String.valueOf(call.CachedName));
                serializer.endTag(null, "CachedName");

                serializer.startTag(null, "CachedNormalizedNumber");
                serializer.text(String.valueOf(call.CachedNormalizedNumber));
                serializer.endTag(null, "CachedNormalizedNumber");

                serializer.startTag(null, "CachedNumberLabel");
                serializer.text(String.valueOf(call.CachedNumberLabel));
                serializer.endTag(null, "CachedNumberLabel");

                serializer.startTag(null, "CachedNumberType");
                serializer.text(String.valueOf(call.CachedNumberType));
                serializer.endTag(null, "CachedNumberType");

                serializer.startTag(null, "CachedPhotoId");
                serializer.text(String.valueOf(call.CachedPhotoId));
                serializer.endTag(null, "CachedPhotoId");

                serializer.startTag(null, "CachedPhotoUri");
                serializer.text(String.valueOf(call.CachedPhotoUri));
                serializer.endTag(null, "CachedPhotoUri");

                serializer.startTag(null, "CallScreeningAppName");
                serializer.text(String.valueOf(call.CallScreeningAppName));
                serializer.endTag(null, "CallScreeningAppName");

                serializer.startTag(null, "CallScreeningComponentName");
                serializer.text(String.valueOf(call.CallScreeningComponentName));
                serializer.endTag(null, "CallScreeningComponentName");

                serializer.startTag(null, "ComposerPhotoUri");
                serializer.text(String.valueOf(call.ComposerPhotoUri));
                serializer.endTag(null, "ComposerPhotoUri");

                serializer.startTag(null, "ContentItemType");
                serializer.text(String.valueOf(call.ContentItemType));
                serializer.endTag(null, "ContentItemType");

                serializer.startTag(null, "ContentType");
                serializer.text(String.valueOf(call.ContentType));
                serializer.endTag(null, "ContentType");

                serializer.startTag(null, "CountryIso");
                serializer.text(String.valueOf(call.CountryIso));
                serializer.endTag(null, "CountryIso");

                serializer.startTag(null, "DataUsage");
                serializer.text(String.valueOf(call.DataUsage));
                serializer.endTag(null, "DataUsage");

                serializer.startTag(null, "Date");
                serializer.text(String.valueOf(call.Date));
                serializer.endTag(null, "Date");

                serializer.startTag(null, "DefaultSortOrder");
                serializer.text(String.valueOf(call.DefaultSortOrder));
                serializer.endTag(null, "DefaultSortOrder");

                serializer.startTag(null, "Duration");
                serializer.text(String.valueOf(call.Duration));
                serializer.endTag(null, "Duration");

                serializer.startTag(null, "ExtraCallTypeFilter");
                serializer.text(String.valueOf(call.ExtraCallTypeFilter));
                serializer.endTag(null, "ExtraCallTypeFilter");

                serializer.startTag(null, "Features");
                serializer.text(String.valueOf(call.Features));
                serializer.endTag(null, "Features");

                serializer.startTag(null, "GeocodedLocation");
                serializer.text(String.valueOf(call.GeocodedLocation));
                serializer.endTag(null, "GeocodedLocation");

                serializer.startTag(null, "IsRead");
                serializer.text(String.valueOf(call.IsRead));
                serializer.endTag(null, "IsRead");

                serializer.startTag(null, "LastModified");
                serializer.text(String.valueOf(call.LastModified));
                serializer.endTag(null, "LastModified");

                serializer.startTag(null, "LimitParamKey");
                serializer.text(String.valueOf(call.LimitParamKey));
                serializer.endTag(null, "LimitParamKey");

                serializer.startTag(null, "Location");
                serializer.text(String.valueOf(call.Location));
                serializer.endTag(null, "Location");

                serializer.startTag(null, "MissedReason");
                serializer.text(String.valueOf(call.MissedReason));
                serializer.endTag(null, "MissedReason");

                serializer.startTag(null, "New");
                serializer.text(String.valueOf(call.New));
                serializer.endTag(null, "New");

                serializer.startTag(null, "Number");
                serializer.text(String.valueOf(call.Number));
                serializer.endTag(null, "Number");

                serializer.startTag(null, "NumberPresentation");
                serializer.text(String.valueOf(call.NumberPresentation));
                serializer.endTag(null, "NumberPresentation");

                serializer.startTag(null, "OffsetParamKey");
                serializer.text(String.valueOf(call.OffsetParamKey));
                serializer.endTag(null, "OffsetParamKey");

                serializer.startTag(null, "PhoneAccountComponentName");
                serializer.text(String.valueOf(call.PhoneAccountComponentName));
                serializer.endTag(null, "PhoneAccountComponentName");

                serializer.startTag(null, "PhoneAccountId");
                serializer.text(String.valueOf(call.PhoneAccountId));
                serializer.endTag(null, "PhoneAccountId");

                serializer.startTag(null, "PostDialDigits");
                serializer.text(String.valueOf(call.PostDialDigits));
                serializer.endTag(null, "PostDialDigits");

                serializer.startTag(null, "Priority");
                serializer.text(String.valueOf(call.Priority));
                serializer.endTag(null, "Priority");

                serializer.startTag(null, "Subject");
                serializer.text(String.valueOf(call.Subject));
                serializer.endTag(null, "Subject");

                serializer.startTag(null, "Transcription");
                serializer.text(String.valueOf(call.Transcription));
                serializer.endTag(null, "Transcription");

                serializer.startTag(null, "Type");
                serializer.text(String.valueOf(call.Type));
                serializer.endTag(null, "Type");

                serializer.startTag(null, "ViaNumber");
                serializer.text(String.valueOf(call.ViaNumber));
                serializer.endTag(null, "ViaNumber");

                serializer.startTag(null, "VoicemailUri");
                serializer.text(String.valueOf(call.VoicemailUri));
                serializer.endTag(null, "VoicemailUri");

                serializer.startTag(null, "AnsweredExternallyType");
                serializer.text(String.valueOf(call.AnsweredExternallyType));
                serializer.endTag(null, "AnsweredExternallyType");
                serializer.startTag(null, "BlockedType");
                serializer.text(String.valueOf(call.BlockedType));
                serializer.endTag(null, "BlockedType");
                serializer.startTag(null, "BlockReasonBlockedNumber");
                serializer.text(String.valueOf(call.BlockReasonBlockedNumber));
                serializer.endTag(null, "BlockReasonBlockedNumber");
                serializer.startTag(null, "BlockReasonCallScreeningService");
                serializer.text(String.valueOf(call.BlockReasonCallScreeningService));
                serializer.endTag(null, "BlockReasonCallScreeningService");
                serializer.startTag(null, "BlockReasonDirectToVoicemail");
                serializer.text(String.valueOf(call.BlockReasonDirectToVoicemail));
                serializer.endTag(null, "BlockReasonDirectToVoicemail");
                serializer.startTag(null, "BlockReasonNotBlocked");
                serializer.text(String.valueOf(call.BlockReasonNotBlocked));
                serializer.endTag(null, "BlockReasonNotBlocked");
                serializer.startTag(null, "BlockReasonNotInContacts");
                serializer.text(String.valueOf(call.BlockReasonNotInContacts));
                serializer.endTag(null, "BlockReasonNotInContacts");
                serializer.startTag(null, "BlockReasonPayPhone");
                serializer.text(String.valueOf(call.BlockReasonPayPhone));
                serializer.endTag(null, "BlockReasonPayPhone");
                serializer.startTag(null, "BlockReasonRestrictedNumber");
                serializer.text(String.valueOf(call.BlockReasonRestrictedNumber));
                serializer.endTag(null, "BlockReasonRestrictedNumber");
                serializer.startTag(null, "BlockReasonUnknownNumber");
                serializer.text(String.valueOf(call.BlockReasonUnknownNumber));
                serializer.endTag(null, "BlockReasonUnknownNumber");
                serializer.startTag(null, "FeaturesAssistedDialingUsed");
                serializer.text(String.valueOf(call.FeaturesAssistedDialingUsed));
                serializer.endTag(null, "FeaturesAssistedDialingUsed");
                serializer.startTag(null, "FeaturesHdCall");
                serializer.text(String.valueOf(call.FeaturesHdCall));
                serializer.endTag(null, "FeaturesHdCall");
                serializer.startTag(null, "FeaturePulledExternally");
                serializer.text(String.valueOf(call.FeaturePulledExternally));
                serializer.endTag(null, "FeaturePulledExternally");
                serializer.startTag(null, "FeatureRtt");
                serializer.text(String.valueOf(call.FeatureRtt));
                serializer.endTag(null, "FeatureRtt");
                serializer.startTag(null, "FeaturesVideo");
                serializer.text(String.valueOf(call.FeaturesVideo));
                serializer.endTag(null, "FeaturesVideo");
                serializer.startTag(null, "FeatureVolte");
                serializer.text(String.valueOf(call.FeatureVolte));
                serializer.endTag(null, "FeatureVolte");
                serializer.startTag(null, "FeatureWifi");
                serializer.text(String.valueOf(call.FeatureWifi));
                serializer.endTag(null, "FeatureWifi");
                serializer.startTag(null, "IncomingType");
                serializer.text(String.valueOf(call.IncomingType));
                serializer.endTag(null, "IncomingType");
                serializer.startTag(null, "MissedType");
                serializer.text(String.valueOf(call.MissedType));
                serializer.endTag(null, "MissedType");
                serializer.startTag(null, "OutgoingType");
                serializer.text(String.valueOf(call.OutgoingType));
                serializer.endTag(null, "OutgoingType");
                serializer.startTag(null, "PresentationAllowed");
                serializer.text(String.valueOf(call.PresentationAllowed));
                serializer.endTag(null, "PresentationAllowed");
                serializer.startTag(null, "PresentationPayPhone");
                serializer.text(String.valueOf(call.PresentationPayPhone));
                serializer.endTag(null, "PresentationPayPhone");
                serializer.startTag(null, "PresentationRestricted");
                serializer.text(String.valueOf(call.PresentationRestricted));
                serializer.endTag(null, "PresentationRestricted");
                serializer.startTag(null, "PresentationUnknown");
                serializer.text(String.valueOf(call.PresentationUnknown));
                serializer.endTag(null, "PresentationUnknown");
                serializer.startTag(null, "PriorityNormal");
                serializer.text(String.valueOf(call.PriorityNormal));
                serializer.endTag(null, "PriorityNormal");
                serializer.startTag(null, "PriorityUrgent");
                serializer.text(String.valueOf(call.PriorityUrgent));
                serializer.endTag(null, "PriorityUrgent");
                serializer.startTag(null, "RejectedType");
                serializer.text(String.valueOf(call.RejectedType));
                serializer.endTag(null, "RejectedType");
                serializer.startTag(null, "VoicemailType");
                serializer.text(String.valueOf(call.VoicemailType));
                serializer.endTag(null, "VoicemailType");
                serializer.startTag(null, "AutoMissedEmergencyCall");
                serializer.text(String.valueOf(call.AutoMissedEmergencyCall));
                serializer.endTag(null, "AutoMissedEmergencyCall");
                serializer.startTag(null, "AutoMissedMaximumDialing");
                serializer.text(String.valueOf(call.AutoMissedMaximumDialing));
                serializer.endTag(null, "AutoMissedMaximumDialing");
                serializer.startTag(null, "AutoMissedMaximumRinging");
                serializer.text(String.valueOf(call.AutoMissedMaximumRinging));
                serializer.endTag(null, "AutoMissedMaximumRinging");
                serializer.startTag(null, "MissedReasonNotMissed");
                serializer.text(String.valueOf(call.MissedReasonNotMissed));
                serializer.endTag(null, "MissedReasonNotMissed");
                serializer.startTag(null, "UserMissedCallFiltersTimeout");
                serializer.text(String.valueOf(call.UserMissedCallFiltersTimeout));
                serializer.endTag(null, "UserMissedCallFiltersTimeout");
                serializer.startTag(null, "UserMissedCallScreeningServiceSilenced");
                serializer.text(String.valueOf(call.UserMissedCallScreeningServiceSilenced));
                serializer.endTag(null, "UserMissedCallScreeningServiceSilenced");
                serializer.startTag(null, "UserMissedDndMode");
                serializer.text(String.valueOf(call.UserMissedDndMode));
                serializer.endTag(null, "UserMissedDndMode");
                serializer.startTag(null, "UserMissedLowRingVolume");
                serializer.text(String.valueOf(call.UserMissedLowRingVolume));
                serializer.endTag(null, "UserMissedLowRingVolume");
                serializer.startTag(null, "UserMissedNoAnswer");
                serializer.text(String.valueOf(call.UserMissedNoAnswer));
                serializer.endTag(null, "UserMissedNoAnswer");
                serializer.startTag(null, "UserMissedNoVibrate");
                serializer.text(String.valueOf(call.UserMissedNoVibrate));
                serializer.endTag(null, "UserMissedNoVibrate");
                serializer.startTag(null, "UserMissedShortRing");
                serializer.text(String.valueOf(call.UserMissedShortRing));
                serializer.endTag(null, "UserMissedShortRing");

                serializer.endTag(null, "call");
                countCall++;
            }

            serializer.endTag(null, "CallLog");
            serializer.endDocument();
            serializer.flush();
            os.close();
            Toast.makeText(context, "Operation successful", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            // Tutaj by?? mo??e wrzut statusu operacji do logera?
        }
    }

    public List<List<SmsData>> restoreSmsFromXml(Context context,Uri uriFile) {
        List<List<SmsData>> smsDataList = new ArrayList<>();
        List<SmsData> smsInboxDataList = new ArrayList<>();
        List<SmsData> smsSentDataList = new ArrayList<>();
        List<SmsData> smsDraftDataList = new ArrayList<>();
        List<SmsData> smsOutboxDataList = new ArrayList<>();
        int countInbox = 0;
        int countSent = 0;
        int countDraft = 0;
        int countOutbox = 0;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            //FileInputStream is = new FileInputStream(file);
            InputStream is = context.getContentResolver().openInputStream(uriFile);
            xpp.setInput(is, null);
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
//                    System.out.println("========================================");
//                    System.out.println("Start document");
//                    System.out.println("========================================");
                } else if(eventType == XmlPullParser.START_TAG) {
                    //System.out.println("Start tag "+xpp.getName());
                    // Inbox Messages
                    if (xpp.getName().equalsIgnoreCase("Inbox")) {
                        String parentTag = xpp.getName();
                        eventType = xpp.next();
                        while (parentTag.equalsIgnoreCase("Inbox")) {
                            if(eventType == XmlPullParser.START_TAG) {
                                if (xpp.getName().equalsIgnoreCase("Message")) {
                                    String messageTag = xpp.getName();
                                    String messageId = xpp.getAttributeValue(null,"id");
//                                    System.out.println("Parent tag: " + parentTag + " => Start tag: " + messageTag + " => id: " + messageId);
                                    countInbox++;
                                    SmsData smsData = new SmsData();
                                    String tagName;
                                    String currentTag = "";
                                    String value;
                                    while (messageTag.equalsIgnoreCase("Message")) {
                                        eventType = xpp.next();
                                        tagName = xpp.getName();
                                        if (eventType == XmlPullParser.START_TAG) {
//                                            System.out.println("    => tag: " + tagName);
                                            currentTag = tagName;
                                        }
                                        if (eventType == XmlPullParser.TEXT) {
                                            value = String.valueOf(xpp.getText());
//                                            System.out.println("       value: " + value + " => Tag: " + currentTag);
                                            if ("Address".equalsIgnoreCase(currentTag)) smsData.Address = value;
                                            if ("Body".equalsIgnoreCase(currentTag)) smsData.Body = value;
                                            if ("Creator".equalsIgnoreCase(currentTag)) smsData.Creator = value;
                                            if ("Date".equalsIgnoreCase(currentTag)) smsData.Date = value;
                                            if ("DateSent".equalsIgnoreCase(currentTag)) smsData.DateSent = value;
                                            if ("ErrorCode".equalsIgnoreCase(currentTag)) smsData.ErrorCode = value;
                                            if ("Locked".equalsIgnoreCase(currentTag)) smsData.Locked = value;
                                            if ("Person".equalsIgnoreCase(currentTag)) smsData.Person = value;
                                            if ("Protocol".equalsIgnoreCase(currentTag)) smsData.Protocol = value;
                                            if ("Read".equalsIgnoreCase(currentTag)) smsData.Read = value;
                                            if ("ReplayPathPresent".equalsIgnoreCase(currentTag)) smsData.ReplayPathPresent = value;
                                            if ("Seen".equalsIgnoreCase(currentTag)) smsData.Seen = value;
                                            if ("ServiceCenter".equalsIgnoreCase(currentTag)) smsData.ServiceCenter = value;
                                            if ("Status".equalsIgnoreCase(currentTag)) smsData.Status = value;
                                            if ("Subject".equalsIgnoreCase(currentTag)) smsData.Subject = value;
                                            if ("SubscriptionId".equalsIgnoreCase(currentTag)) smsData.SubscriptionId = value;
                                            if ("ThreadId".equalsIgnoreCase(currentTag)) smsData.ThreadId = value;
                                            if ("Type".equalsIgnoreCase(currentTag)) smsData.Type = value;
                                            if ("MessageTypeAll".equalsIgnoreCase(currentTag)) smsData.MessageTypeAll = Integer.parseInt(value);
                                            if ("MessageTypeDraft".equalsIgnoreCase(currentTag)) smsData.MessageTypeDraft = Integer.parseInt(value);
                                            if ("MessageTypeFailed".equalsIgnoreCase(currentTag)) smsData.MessageTypeFailed = Integer.parseInt(value);
                                            if ("MessageTypeInbox".equalsIgnoreCase(currentTag)) smsData.MessageTypeInbox = Integer.parseInt(value);
                                            if ("MessageTypeOutbox".equalsIgnoreCase(currentTag)) smsData.MessageTypeOutbox = Integer.parseInt(value);
                                            if ("MessageTypeQueded".equalsIgnoreCase(currentTag)) smsData.MessageTypeQueded = Integer.parseInt(value);
                                            if ("MessageTypeSent".equalsIgnoreCase(currentTag)) smsData.MessageTypeSent = Integer.parseInt(value);
                                            if ("StatusComplete".equalsIgnoreCase(currentTag)) smsData.StatusComplete = Integer.parseInt(value);
                                            if ("StatusFailed".equalsIgnoreCase(currentTag)) smsData.StatusFailed = Integer.parseInt(value);
                                            if ("StatusNone".equalsIgnoreCase(currentTag)) smsData.StatusNone = Integer.parseInt(value);
                                            if ("StatusPending".equalsIgnoreCase(currentTag)) smsData.StatusPending = Integer.parseInt(value);
                                        }
                                        if (eventType == XmlPullParser.END_TAG) {
                                            if (xpp.getName().equalsIgnoreCase("Message")) {
//                                                System.out.println("--------------------");
//                                                System.out.println("smsData.Address: "+smsData.Address);
//                                                System.out.println("smsData.Body: "+smsData.Body);
//                                                System.out.println("smsData.Creator: "+smsData.Creator);
//                                                System.out.println("smsData.Date: "+smsData.Date);
//                                                System.out.println("smsData.DateSent: "+smsData.DateSent);
//                                                System.out.println("smsData.ErrorCode: "+smsData.ErrorCode);
//                                                System.out.println("smsData.Locked: "+smsData.Locked);
//                                                System.out.println("smsData.Person: "+smsData.Person);
//                                                System.out.println("smsData.Protocol: "+smsData.Protocol);
//                                                System.out.println("smsData.Read: "+smsData.Read);
//                                                System.out.println("smsData.ReplayPathPresent: "+smsData.ReplayPathPresent);
//                                                System.out.println("smsData.Seen: "+smsData.Seen);
//                                                System.out.println("smsData.ServiceCenter: "+smsData.ServiceCenter);
//                                                System.out.println("smsData.Status: "+smsData.Status);
//                                                System.out.println("smsData.Subject: "+smsData.Subject);
//                                                System.out.println("smsData.SubscriptionId: "+smsData.SubscriptionId);
//                                                System.out.println("smsData.ThreadId: "+smsData.ThreadId);
//                                                System.out.println("smsData.Type: "+smsData.Type);
//                                                System.out.println("smsData.MessageTypeAll: "+smsData.MessageTypeAll);
//                                                System.out.println("smsData.MessageTypeDraft: "+smsData.MessageTypeDraft);
//                                                System.out.println("smsData.MessageTypeFailed: "+smsData.MessageTypeFailed);
//                                                System.out.println("smsData.MessageTypeInbox: "+smsData.MessageTypeInbox);
//                                                System.out.println("smsData.MessageTypeOutbox: "+smsData.MessageTypeOutbox);
//                                                System.out.println("smsData.MessageTypeQueded: "+smsData.MessageTypeQueded);
//                                                System.out.println("smsData.MessageTypeSent: "+smsData.MessageTypeSent);
//                                                System.out.println("smsData.StatusComplete: "+smsData.StatusComplete);
//                                                System.out.println("smsData.StatusFailed: "+smsData.StatusFailed);
//                                                System.out.println("smsData.StatusNone: "+smsData.StatusNone);
//                                                System.out.println("smsData.StatusPending: "+smsData.StatusPending);
                                                smsInboxDataList.add(smsData);
//                                                System.out.println("--------------------");
//                                                System.out.println("Parent tag: " + parentTag + " => End tag: " + tagName + " => id: " + messageId);
                                                messageTag = "";
                                            }
                                        }
                                    }
                                }
                            }
                            if(eventType == XmlPullParser.END_TAG) {
                                //System.out.println("End tag "+xpp.getName());
                                //if (xpp.getName().equalsIgnoreCase("Message")) {
                                //    System.out.println("Parent tag:" + parentTag + " => End tag:" + xpp.getName());
                                //}
                                if (xpp.getName().equalsIgnoreCase("Inbox")) {
//                                    System.out.println("Parent tag:" + parentTag + " => End tag:" + xpp.getName());
//                                    System.out.println("====================");
                                    parentTag = "";
                                }
                            }
                            eventType = xpp.next();
                        }
                    }
                    // Sent Messages
                    if (xpp.getName().equalsIgnoreCase("Sent")) {
                        String parentTag = xpp.getName();
                        eventType = xpp.next();
                        while (parentTag.equalsIgnoreCase("Sent")) {
                            if(eventType == XmlPullParser.START_TAG) {
                                if (xpp.getName().equalsIgnoreCase("Message")) {
                                    String messageTag = xpp.getName();
                                    String messageId = xpp.getAttributeValue(null,"id");
//                                    System.out.println("Parent tag: " + parentTag + " => Start tag: " + messageTag + " => id: " + messageId);
                                    countSent++;
                                    SmsData smsData = new SmsData();
                                    String tagName;
                                    String currentTag = "";
                                    String value;
                                    while (messageTag.equalsIgnoreCase("Message")) {
                                        eventType = xpp.next();
                                        tagName = xpp.getName();
                                        if (eventType == XmlPullParser.START_TAG) {
//                                            System.out.println("    => tag: " + tagName);
                                            currentTag = tagName;
                                        }
                                        if (eventType == XmlPullParser.TEXT) {
                                            value = String.valueOf(xpp.getText());
//                                            System.out.println("       value: " + value + " => Tag: " + currentTag);
                                            if ("Address".equalsIgnoreCase(currentTag)) smsData.Address = value;
                                            if ("Body".equalsIgnoreCase(currentTag)) smsData.Body = value;
                                            if ("Creator".equalsIgnoreCase(currentTag)) smsData.Creator = value;
                                            if ("Date".equalsIgnoreCase(currentTag)) smsData.Date = value;
                                            if ("DateSent".equalsIgnoreCase(currentTag)) smsData.DateSent = value;
                                            if ("ErrorCode".equalsIgnoreCase(currentTag)) smsData.ErrorCode = value;
                                            if ("Locked".equalsIgnoreCase(currentTag)) smsData.Locked = value;
                                            if ("Person".equalsIgnoreCase(currentTag)) smsData.Person = value;
                                            if ("Protocol".equalsIgnoreCase(currentTag)) smsData.Protocol = value;
                                            if ("Read".equalsIgnoreCase(currentTag)) smsData.Read = value;
                                            if ("ReplayPathPresent".equalsIgnoreCase(currentTag)) smsData.ReplayPathPresent = value;
                                            if ("Seen".equalsIgnoreCase(currentTag)) smsData.Seen = value;
                                            if ("ServiceCenter".equalsIgnoreCase(currentTag)) smsData.ServiceCenter = value;
                                            if ("Status".equalsIgnoreCase(currentTag)) smsData.Status = value;
                                            if ("Subject".equalsIgnoreCase(currentTag)) smsData.Subject = value;
                                            if ("SubscriptionId".equalsIgnoreCase(currentTag)) smsData.SubscriptionId = value;
                                            if ("ThreadId".equalsIgnoreCase(currentTag)) smsData.ThreadId = value;
                                            if ("Type".equalsIgnoreCase(currentTag)) smsData.Type = value;
                                            if ("MessageTypeAll".equalsIgnoreCase(currentTag)) smsData.MessageTypeAll = Integer.parseInt(value);
                                            if ("MessageTypeDraft".equalsIgnoreCase(currentTag)) smsData.MessageTypeDraft = Integer.parseInt(value);
                                            if ("MessageTypeFailed".equalsIgnoreCase(currentTag)) smsData.MessageTypeFailed = Integer.parseInt(value);
                                            if ("MessageTypeInbox".equalsIgnoreCase(currentTag)) smsData.MessageTypeInbox = Integer.parseInt(value);
                                            if ("MessageTypeOutbox".equalsIgnoreCase(currentTag)) smsData.MessageTypeOutbox = Integer.parseInt(value);
                                            if ("MessageTypeQueded".equalsIgnoreCase(currentTag)) smsData.MessageTypeQueded = Integer.parseInt(value);
                                            if ("MessageTypeSent".equalsIgnoreCase(currentTag)) smsData.MessageTypeSent = Integer.parseInt(value);
                                            if ("StatusComplete".equalsIgnoreCase(currentTag)) smsData.StatusComplete = Integer.parseInt(value);
                                            if ("StatusFailed".equalsIgnoreCase(currentTag)) smsData.StatusFailed = Integer.parseInt(value);
                                            if ("StatusNone".equalsIgnoreCase(currentTag)) smsData.StatusNone = Integer.parseInt(value);
                                            if ("StatusPending".equalsIgnoreCase(currentTag)) smsData.StatusPending = Integer.parseInt(value);
                                        }
                                        if (eventType == XmlPullParser.END_TAG) {
                                            if (xpp.getName().equalsIgnoreCase("Message")) {
//                                                System.out.println("--------------------");
//                                                System.out.println("smsData.Address: "+smsData.Address);
//                                                System.out.println("smsData.Body: "+smsData.Body);
//                                                System.out.println("smsData.Creator: "+smsData.Creator);
//                                                System.out.println("smsData.Date: "+smsData.Date);
//                                                System.out.println("smsData.DateSent: "+smsData.DateSent);
//                                                System.out.println("smsData.ErrorCode: "+smsData.ErrorCode);
//                                                System.out.println("smsData.Locked: "+smsData.Locked);
//                                                System.out.println("smsData.Person: "+smsData.Person);
//                                                System.out.println("smsData.Protocol: "+smsData.Protocol);
//                                                System.out.println("smsData.Read: "+smsData.Read);
//                                                System.out.println("smsData.ReplayPathPresent: "+smsData.ReplayPathPresent);
//                                                System.out.println("smsData.Seen: "+smsData.Seen);
//                                                System.out.println("smsData.ServiceCenter: "+smsData.ServiceCenter);
//                                                System.out.println("smsData.Status: "+smsData.Status);
//                                                System.out.println("smsData.Subject: "+smsData.Subject);
//                                                System.out.println("smsData.SubscriptionId: "+smsData.SubscriptionId);
//                                                System.out.println("smsData.ThreadId: "+smsData.ThreadId);
//                                                System.out.println("smsData.Type: "+smsData.Type);
//                                                System.out.println("smsData.MessageTypeAll: "+smsData.MessageTypeAll);
//                                                System.out.println("smsData.MessageTypeDraft: "+smsData.MessageTypeDraft);
//                                                System.out.println("smsData.MessageTypeFailed: "+smsData.MessageTypeFailed);
//                                                System.out.println("smsData.MessageTypeInbox: "+smsData.MessageTypeInbox);
//                                                System.out.println("smsData.MessageTypeOutbox: "+smsData.MessageTypeOutbox);
//                                                System.out.println("smsData.MessageTypeQueded: "+smsData.MessageTypeQueded);
//                                                System.out.println("smsData.MessageTypeSent: "+smsData.MessageTypeSent);
//                                                System.out.println("smsData.StatusComplete: "+smsData.StatusComplete);
//                                                System.out.println("smsData.StatusFailed: "+smsData.StatusFailed);
//                                                System.out.println("smsData.StatusNone: "+smsData.StatusNone);
//                                                System.out.println("smsData.StatusPending: "+smsData.StatusPending);
                                                smsSentDataList.add(smsData);
//                                                System.out.println("--------------------");
//                                                System.out.println("Parent tag: " + parentTag + " => End tag: " + tagName + " => id: " + messageId);
                                                messageTag = "";
                                            }
                                        }
                                    }
                                }
                            }
                            if(eventType == XmlPullParser.END_TAG) {
                                //System.out.println("End tag "+xpp.getName());
                                //if (xpp.getName().equalsIgnoreCase("Message")) {
                                //    System.out.println("Parent tag:" + parentTag + " => End tag:" + xpp.getName());
                                //}
                                if (xpp.getName().equalsIgnoreCase("Sent")) {
//                                    System.out.println("Parent tag:" + parentTag + " => End tag:" + xpp.getName());
//                                    System.out.println("====================");
                                    parentTag = "";
                                }
                            }
                            eventType = xpp.next();
                        }
                    }
                    // Draft Messages
                    if (xpp.getName().equalsIgnoreCase("Draft")) {
                        String parentTag = xpp.getName();
                        eventType = xpp.next();
                        while (parentTag.equalsIgnoreCase("Draft")) {
                            if(eventType == XmlPullParser.START_TAG) {
                                if (xpp.getName().equalsIgnoreCase("Message")) {
                                    String messageTag = xpp.getName();
                                    String messageId = xpp.getAttributeValue(null,"id");
//                                    System.out.println("Parent tag: " + parentTag + " => Start tag: " + messageTag + " => id: " + messageId);
                                    countDraft++;
                                    SmsData smsData = new SmsData();
                                    String tagName;
                                    String currentTag = "";
                                    String value;
                                    while (messageTag.equalsIgnoreCase("Message")) {
                                        eventType = xpp.next();
                                        tagName = xpp.getName();
                                        if (eventType == XmlPullParser.START_TAG) {
//                                            System.out.println("    => tag: " + tagName);
                                            currentTag = tagName;
                                        }
                                        if (eventType == XmlPullParser.TEXT) {
                                            value = String.valueOf(xpp.getText());
//                                            System.out.println("       value: " + value + " => Tag: " + currentTag);
                                            if ("Address".equalsIgnoreCase(currentTag)) smsData.Address = value;
                                            if ("Body".equalsIgnoreCase(currentTag)) smsData.Body = value;
                                            if ("Creator".equalsIgnoreCase(currentTag)) smsData.Creator = value;
                                            if ("Date".equalsIgnoreCase(currentTag)) smsData.Date = value;
                                            if ("DateSent".equalsIgnoreCase(currentTag)) smsData.DateSent = value;
                                            if ("ErrorCode".equalsIgnoreCase(currentTag)) smsData.ErrorCode = value;
                                            if ("Locked".equalsIgnoreCase(currentTag)) smsData.Locked = value;
                                            if ("Person".equalsIgnoreCase(currentTag)) smsData.Person = value;
                                            if ("Protocol".equalsIgnoreCase(currentTag)) smsData.Protocol = value;
                                            if ("Read".equalsIgnoreCase(currentTag)) smsData.Read = value;
                                            if ("ReplayPathPresent".equalsIgnoreCase(currentTag)) smsData.ReplayPathPresent = value;
                                            if ("Seen".equalsIgnoreCase(currentTag)) smsData.Seen = value;
                                            if ("ServiceCenter".equalsIgnoreCase(currentTag)) smsData.ServiceCenter = value;
                                            if ("Status".equalsIgnoreCase(currentTag)) smsData.Status = value;
                                            if ("Subject".equalsIgnoreCase(currentTag)) smsData.Subject = value;
                                            if ("SubscriptionId".equalsIgnoreCase(currentTag)) smsData.SubscriptionId = value;
                                            if ("ThreadId".equalsIgnoreCase(currentTag)) smsData.ThreadId = value;
                                            if ("Type".equalsIgnoreCase(currentTag)) smsData.Type = value;
                                            if ("MessageTypeAll".equalsIgnoreCase(currentTag)) smsData.MessageTypeAll = Integer.parseInt(value);
                                            if ("MessageTypeDraft".equalsIgnoreCase(currentTag)) smsData.MessageTypeDraft = Integer.parseInt(value);
                                            if ("MessageTypeFailed".equalsIgnoreCase(currentTag)) smsData.MessageTypeFailed = Integer.parseInt(value);
                                            if ("MessageTypeInbox".equalsIgnoreCase(currentTag)) smsData.MessageTypeInbox = Integer.parseInt(value);
                                            if ("MessageTypeOutbox".equalsIgnoreCase(currentTag)) smsData.MessageTypeOutbox = Integer.parseInt(value);
                                            if ("MessageTypeQueded".equalsIgnoreCase(currentTag)) smsData.MessageTypeQueded = Integer.parseInt(value);
                                            if ("MessageTypeSent".equalsIgnoreCase(currentTag)) smsData.MessageTypeSent = Integer.parseInt(value);
                                            if ("StatusComplete".equalsIgnoreCase(currentTag)) smsData.StatusComplete = Integer.parseInt(value);
                                            if ("StatusFailed".equalsIgnoreCase(currentTag)) smsData.StatusFailed = Integer.parseInt(value);
                                            if ("StatusNone".equalsIgnoreCase(currentTag)) smsData.StatusNone = Integer.parseInt(value);
                                            if ("StatusPending".equalsIgnoreCase(currentTag)) smsData.StatusPending = Integer.parseInt(value);
                                        }
                                        if (eventType == XmlPullParser.END_TAG) {
                                            if (xpp.getName().equalsIgnoreCase("Message")) {
//                                                System.out.println("--------------------");
//                                                System.out.println("smsData.Address: "+smsData.Address);
//                                                System.out.println("smsData.Body: "+smsData.Body);
//                                                System.out.println("smsData.Creator: "+smsData.Creator);
//                                                System.out.println("smsData.Date: "+smsData.Date);
//                                                System.out.println("smsData.DateSent: "+smsData.DateSent);
//                                                System.out.println("smsData.ErrorCode: "+smsData.ErrorCode);
//                                                System.out.println("smsData.Locked: "+smsData.Locked);
//                                                System.out.println("smsData.Person: "+smsData.Person);
//                                                System.out.println("smsData.Protocol: "+smsData.Protocol);
//                                                System.out.println("smsData.Read: "+smsData.Read);
//                                                System.out.println("smsData.ReplayPathPresent: "+smsData.ReplayPathPresent);
//                                                System.out.println("smsData.Seen: "+smsData.Seen);
//                                                System.out.println("smsData.ServiceCenter: "+smsData.ServiceCenter);
//                                                System.out.println("smsData.Status: "+smsData.Status);
//                                                System.out.println("smsData.Subject: "+smsData.Subject);
//                                                System.out.println("smsData.SubscriptionId: "+smsData.SubscriptionId);
//                                                System.out.println("smsData.ThreadId: "+smsData.ThreadId);
//                                                System.out.println("smsData.Type: "+smsData.Type);
//                                                System.out.println("smsData.MessageTypeAll: "+smsData.MessageTypeAll);
//                                                System.out.println("smsData.MessageTypeDraft: "+smsData.MessageTypeDraft);
//                                                System.out.println("smsData.MessageTypeFailed: "+smsData.MessageTypeFailed);
//                                                System.out.println("smsData.MessageTypeInbox: "+smsData.MessageTypeInbox);
//                                                System.out.println("smsData.MessageTypeOutbox: "+smsData.MessageTypeOutbox);
//                                                System.out.println("smsData.MessageTypeQueded: "+smsData.MessageTypeQueded);
//                                                System.out.println("smsData.MessageTypeSent: "+smsData.MessageTypeSent);
//                                                System.out.println("smsData.StatusComplete: "+smsData.StatusComplete);
//                                                System.out.println("smsData.StatusFailed: "+smsData.StatusFailed);
//                                                System.out.println("smsData.StatusNone: "+smsData.StatusNone);
//                                                System.out.println("smsData.StatusPending: "+smsData.StatusPending);
                                                smsDraftDataList.add(smsData);
//                                                System.out.println("--------------------");
//                                                System.out.println("Parent tag: " + parentTag + " => End tag: " + tagName + " => id: " + messageId);
                                                messageTag = "";
                                            }
                                        }
                                    }
                                }
                            }
                            if(eventType == XmlPullParser.END_TAG) {
                                //System.out.println("End tag "+xpp.getName());
                                //if (xpp.getName().equalsIgnoreCase("Message")) {
                                //    System.out.println("Parent tag:" + parentTag + " => End tag:" + xpp.getName());
                                //}
                                if (xpp.getName().equalsIgnoreCase("Draft")) {
//                                    System.out.println("Parent tag:" + parentTag + " => End tag:" + xpp.getName());
//                                    System.out.println("====================");
                                    parentTag = "";
                                }
                            }
                            eventType = xpp.next();
                        }
                    }
                    // Outbox Messages
                    if (xpp.getName().equalsIgnoreCase("Outbox")) {
                        String parentTag = xpp.getName();
                        eventType = xpp.next();
                        while (parentTag.equalsIgnoreCase("Outbox")) {
                            if(eventType == XmlPullParser.START_TAG) {
                                if (xpp.getName().equalsIgnoreCase("Message")) {
                                    String messageTag = xpp.getName();
                                    String messageId = xpp.getAttributeValue(null,"id");
//                                    System.out.println("Parent tag: " + parentTag + " => Start tag: " + messageTag + " => id: " + messageId);
                                    countOutbox++;
                                    SmsData smsData = new SmsData();
                                    String tagName;
                                    String currentTag = "";
                                    String value;
                                    while (messageTag.equalsIgnoreCase("Message")) {
                                        eventType = xpp.next();
                                        tagName = xpp.getName();
                                        if (eventType == XmlPullParser.START_TAG) {
//                                            System.out.println("    => tag: " + tagName);
                                            currentTag = tagName;
                                        }
                                        if (eventType == XmlPullParser.TEXT) {
                                            value = String.valueOf(xpp.getText());
//                                            System.out.println("       value: " + value + " => Tag: " + currentTag);
                                            if ("Address".equalsIgnoreCase(currentTag)) smsData.Address = value;
                                            if ("Body".equalsIgnoreCase(currentTag)) smsData.Body = value;
                                            if ("Creator".equalsIgnoreCase(currentTag)) smsData.Creator = value;
                                            if ("Date".equalsIgnoreCase(currentTag)) smsData.Date = value;
                                            if ("DateSent".equalsIgnoreCase(currentTag)) smsData.DateSent = value;
                                            if ("ErrorCode".equalsIgnoreCase(currentTag)) smsData.ErrorCode = value;
                                            if ("Locked".equalsIgnoreCase(currentTag)) smsData.Locked = value;
                                            if ("Person".equalsIgnoreCase(currentTag)) smsData.Person = value;
                                            if ("Protocol".equalsIgnoreCase(currentTag)) smsData.Protocol = value;
                                            if ("Read".equalsIgnoreCase(currentTag)) smsData.Read = value;
                                            if ("ReplayPathPresent".equalsIgnoreCase(currentTag)) smsData.ReplayPathPresent = value;
                                            if ("Seen".equalsIgnoreCase(currentTag)) smsData.Seen = value;
                                            if ("ServiceCenter".equalsIgnoreCase(currentTag)) smsData.ServiceCenter = value;
                                            if ("Status".equalsIgnoreCase(currentTag)) smsData.Status = value;
                                            if ("Subject".equalsIgnoreCase(currentTag)) smsData.Subject = value;
                                            if ("SubscriptionId".equalsIgnoreCase(currentTag)) smsData.SubscriptionId = value;
                                            if ("ThreadId".equalsIgnoreCase(currentTag)) smsData.ThreadId = value;
                                            if ("Type".equalsIgnoreCase(currentTag)) smsData.Type = value;
                                            if ("MessageTypeAll".equalsIgnoreCase(currentTag)) smsData.MessageTypeAll = Integer.parseInt(value);
                                            if ("MessageTypeDraft".equalsIgnoreCase(currentTag)) smsData.MessageTypeDraft = Integer.parseInt(value);
                                            if ("MessageTypeFailed".equalsIgnoreCase(currentTag)) smsData.MessageTypeFailed = Integer.parseInt(value);
                                            if ("MessageTypeInbox".equalsIgnoreCase(currentTag)) smsData.MessageTypeInbox = Integer.parseInt(value);
                                            if ("MessageTypeOutbox".equalsIgnoreCase(currentTag)) smsData.MessageTypeOutbox = Integer.parseInt(value);
                                            if ("MessageTypeQueded".equalsIgnoreCase(currentTag)) smsData.MessageTypeQueded = Integer.parseInt(value);
                                            if ("MessageTypeSent".equalsIgnoreCase(currentTag)) smsData.MessageTypeSent = Integer.parseInt(value);
                                            if ("StatusComplete".equalsIgnoreCase(currentTag)) smsData.StatusComplete = Integer.parseInt(value);
                                            if ("StatusFailed".equalsIgnoreCase(currentTag)) smsData.StatusFailed = Integer.parseInt(value);
                                            if ("StatusNone".equalsIgnoreCase(currentTag)) smsData.StatusNone = Integer.parseInt(value);
                                            if ("StatusPending".equalsIgnoreCase(currentTag)) smsData.StatusPending = Integer.parseInt(value);
                                        }
                                        if (eventType == XmlPullParser.END_TAG) {
                                            if (xpp.getName().equalsIgnoreCase("Message")) {
//                                                System.out.println("--------------------");
//                                                System.out.println("smsData.Address: "+smsData.Address);
//                                                System.out.println("smsData.Body: "+smsData.Body);
//                                                System.out.println("smsData.Creator: "+smsData.Creator);
//                                                System.out.println("smsData.Date: "+smsData.Date);
//                                                System.out.println("smsData.DateSent: "+smsData.DateSent);
//                                                System.out.println("smsData.ErrorCode: "+smsData.ErrorCode);
//                                                System.out.println("smsData.Locked: "+smsData.Locked);
//                                                System.out.println("smsData.Person: "+smsData.Person);
//                                                System.out.println("smsData.Protocol: "+smsData.Protocol);
//                                                System.out.println("smsData.Read: "+smsData.Read);
//                                                System.out.println("smsData.ReplayPathPresent: "+smsData.ReplayPathPresent);
//                                                System.out.println("smsData.Seen: "+smsData.Seen);
//                                                System.out.println("smsData.ServiceCenter: "+smsData.ServiceCenter);
//                                                System.out.println("smsData.Status: "+smsData.Status);
//                                                System.out.println("smsData.Subject: "+smsData.Subject);
//                                                System.out.println("smsData.SubscriptionId: "+smsData.SubscriptionId);
//                                                System.out.println("smsData.ThreadId: "+smsData.ThreadId);
//                                                System.out.println("smsData.Type: "+smsData.Type);
//                                                System.out.println("smsData.MessageTypeAll: "+smsData.MessageTypeAll);
//                                                System.out.println("smsData.MessageTypeDraft: "+smsData.MessageTypeDraft);
//                                                System.out.println("smsData.MessageTypeFailed: "+smsData.MessageTypeFailed);
//                                                System.out.println("smsData.MessageTypeInbox: "+smsData.MessageTypeInbox);
//                                                System.out.println("smsData.MessageTypeOutbox: "+smsData.MessageTypeOutbox);
//                                                System.out.println("smsData.MessageTypeQueded: "+smsData.MessageTypeQueded);
//                                                System.out.println("smsData.MessageTypeSent: "+smsData.MessageTypeSent);
//                                                System.out.println("smsData.StatusComplete: "+smsData.StatusComplete);
//                                                System.out.println("smsData.StatusFailed: "+smsData.StatusFailed);
//                                                System.out.println("smsData.StatusNone: "+smsData.StatusNone);
//                                                System.out.println("smsData.StatusPending: "+smsData.StatusPending);
                                                smsOutboxDataList.add(smsData);
//                                                System.out.println("--------------------");
//                                                System.out.println("Parent tag: " + parentTag + " => End tag: " + tagName + " => id: " + messageId);
                                                messageTag = "";
                                            }
                                        }
                                    }
                                }
                            }
                            if(eventType == XmlPullParser.END_TAG) {
                                //System.out.println("End tag "+xpp.getName());
                                //if (xpp.getName().equalsIgnoreCase("Message")) {
                                //    System.out.println("Parent tag:" + parentTag + " => End tag:" + xpp.getName());
                                //}
                                if (xpp.getName().equalsIgnoreCase("Outbox")) {
//                                    System.out.println("Parent tag:" + parentTag + " => End tag:" + xpp.getName());
//                                    System.out.println("====================");
                                    parentTag = "";
                                }
                            }
                            eventType = xpp.next();
                        }
                    }
                } else if(eventType == XmlPullParser.TEXT) {
                    //System.out.println("Text "+xpp.getText());
                }
                eventType = xpp.next();
            }
//            System.out.println("========================================");
//            System.out.println("End document");
//            System.out.println("========================================");
//
//            System.out.println("Inbox: "+countInbox);
//            System.out.println("Sent: "+countSent);
//            System.out.println("Draft: "+countDraft);
//            System.out.println("Outbox: "+countOutbox);

            is.close();
            Toast.makeText(context, "Operation successful", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            // Tutaj by?? mo??e wrzut statusu operacji do logera?
        }

        smsDataList.add(smsInboxDataList);
        smsDataList.add(smsSentDataList);
        smsDataList.add(smsDraftDataList);
        smsDataList.add(smsOutboxDataList);

        return smsDataList;
    }

    public List<CallData> restoreCallLogFromXml(Context context, Uri uriFile) {
        List<CallData> callDataList = new ArrayList<>();
        int countCallLog = 0;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            //FileInputStream is = new FileInputStream(file);
            InputStream is = context.getContentResolver().openInputStream(uriFile);
            xpp.setInput(is, null);
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
//                    System.out.println("========================================");
//                    System.out.println("Start document");
//                    System.out.println("========================================");
                } else if(eventType == XmlPullParser.START_TAG) {
                    //System.out.println("Start tag "+xpp.getName());
                    // Inbox Messages
                    if (xpp.getName().equalsIgnoreCase("CallLog")) {
                        String parentTag = xpp.getName();
                        eventType = xpp.next();
                        while (parentTag.equalsIgnoreCase("CallLog")) {
                            if(eventType == XmlPullParser.START_TAG) {
                                if (xpp.getName().equalsIgnoreCase("call")) {
                                    String messageTag = xpp.getName();
                                    String messageId = xpp.getAttributeValue(null,"id");
//                                    System.out.println("Parent tag: " + parentTag + " => Start tag: " + messageTag + " => id: " + messageId);
                                    countCallLog++;
                                    CallData callData = new CallData();
                                    String tagName;
                                    String currentTag = "";
                                    String value;
                                    while (messageTag.equalsIgnoreCase("call")) {
                                        eventType = xpp.next();
                                        tagName = xpp.getName();
                                        if (eventType == XmlPullParser.START_TAG) {
//                                            System.out.println("    => tag: " + tagName);
                                            currentTag = tagName;
                                        }
                                        if (eventType == XmlPullParser.TEXT) {
                                            value = String.valueOf(xpp.getText());
//                                            System.out.println("       value: " + value + " => Tag: " + currentTag);
                                            if ("Id".equalsIgnoreCase(currentTag)) callData.Id = value;

                                            if ("BlockReason".equalsIgnoreCase(currentTag)) callData.BlockReason = value;
                                            if ("CachedFormatedNumber".equalsIgnoreCase(currentTag)) callData.CachedFormatedNumber = value;
                                            if ("CachedLookupUri".equalsIgnoreCase(currentTag)) callData.CachedLookupUri = value;
                                            if ("CachedMatchedNumber".equalsIgnoreCase(currentTag)) callData.CachedMatchedNumber = value;
                                            if ("CachedName".equalsIgnoreCase(currentTag)) callData.CachedName = value;
                                            if ("CachedNormalizedNumber".equalsIgnoreCase(currentTag)) callData.CachedNormalizedNumber = value;
                                            if ("CachedNumberLabel".equalsIgnoreCase(currentTag)) callData.CachedNumberLabel = value;
                                            if ("CachedNumberType".equalsIgnoreCase(currentTag)) callData.CachedNumberType = value;
                                            if ("CachedPhotoId".equalsIgnoreCase(currentTag)) callData.CachedPhotoId = value;
                                            if ("CachedPhotoUri".equalsIgnoreCase(currentTag)) callData.CachedPhotoUri = value;
                                            if ("CallScreeningAppName".equalsIgnoreCase(currentTag)) callData.CallScreeningAppName = value;
                                            if ("CallScreeningComponentName".equalsIgnoreCase(currentTag)) callData.CallScreeningComponentName = value;
                                            if ("ComposerPhotoUri".equalsIgnoreCase(currentTag)) callData.ComposerPhotoUri = value;
                                            if ("ContentItemType".equalsIgnoreCase(currentTag)) callData.ContentItemType = value;
                                            if ("ContentType".equalsIgnoreCase(currentTag)) callData.ContentType = value;
                                            if ("CountryIso".equalsIgnoreCase(currentTag)) callData.CountryIso = value;
                                            if ("DataUsage".equalsIgnoreCase(currentTag)) callData.DataUsage = value;
                                            if ("Date".equalsIgnoreCase(currentTag)) callData.Date = value;
                                            if ("DefaultSortOrder".equalsIgnoreCase(currentTag)) callData.DefaultSortOrder = value;
                                            if ("Duration".equalsIgnoreCase(currentTag)) callData.Duration = value;
                                            if ("ExtraCallTypeFilter".equalsIgnoreCase(currentTag)) callData.ExtraCallTypeFilter = value;
                                            if ("Features".equalsIgnoreCase(currentTag)) callData.Features = value;
                                            if ("GeocodedLocation".equalsIgnoreCase(currentTag)) callData.GeocodedLocation = value;
                                            if ("IsRead".equalsIgnoreCase(currentTag)) callData.IsRead = value;
                                            if ("LastModified".equalsIgnoreCase(currentTag)) callData.LastModified = value;
                                            if ("LimitParamKey".equalsIgnoreCase(currentTag)) callData.LimitParamKey = value;
                                            if ("Location".equalsIgnoreCase(currentTag)) callData.Location = value;
                                            if ("MissedReason".equalsIgnoreCase(currentTag)) callData.MissedReason = value;
                                            if ("New".equalsIgnoreCase(currentTag)) callData.New = value;
                                            if ("Number".equalsIgnoreCase(currentTag)) callData.Number = value;
                                            if ("NumberPresentation".equalsIgnoreCase(currentTag)) callData.NumberPresentation = value;
                                            if ("OffsetParamKey".equalsIgnoreCase(currentTag)) callData.OffsetParamKey = value;
                                            if ("PhoneAccountComponentName".equalsIgnoreCase(currentTag)) callData.PhoneAccountComponentName = value;
                                            if ("PhoneAccountId".equalsIgnoreCase(currentTag)) callData.PhoneAccountId = value;
                                            if ("PostDialDigits".equalsIgnoreCase(currentTag)) callData.PostDialDigits = value;
                                            if ("Priority".equalsIgnoreCase(currentTag)) callData.Priority = value;
                                            if ("Subject".equalsIgnoreCase(currentTag)) callData.Subject = value;
                                            if ("Transcription".equalsIgnoreCase(currentTag)) callData.Transcription = value;
                                            if ("Type".equalsIgnoreCase(currentTag)) callData.Type = value;
                                            if ("ViaNumber".equalsIgnoreCase(currentTag)) callData.ViaNumber = value;
                                            if ("VoicemailUri".equalsIgnoreCase(currentTag)) callData.VoicemailUri = value;

                                            if ("AnsweredExternallyType".equalsIgnoreCase(currentTag)) callData.AnsweredExternallyType = Integer.parseInt(value);
                                            if ("BlockedType".equalsIgnoreCase(currentTag)) callData.BlockedType = Integer.parseInt(value);
                                            if ("BlockReasonBlockedNumber".equalsIgnoreCase(currentTag)) callData.BlockReasonBlockedNumber = Integer.parseInt(value);
                                            if ("BlockReasonCallScreeningService".equalsIgnoreCase(currentTag)) callData.BlockReasonCallScreeningService = Integer.parseInt(value);
                                            if ("BlockReasonDirectToVoicemail".equalsIgnoreCase(currentTag)) callData.BlockReasonDirectToVoicemail = Integer.parseInt(value);
                                            if ("BlockReasonNotBlocked".equalsIgnoreCase(currentTag)) callData.BlockReasonNotBlocked = Integer.parseInt(value);
                                            if ("BlockReasonNotInContacts".equalsIgnoreCase(currentTag)) callData.BlockReasonNotInContacts = Integer.parseInt(value);
                                            if ("BlockReasonPayPhone".equalsIgnoreCase(currentTag)) callData.BlockReasonPayPhone = Integer.parseInt(value);
                                            if ("BlockReasonRestrictedNumber".equalsIgnoreCase(currentTag)) callData.BlockReasonRestrictedNumber = Integer.parseInt(value);
                                            if ("BlockReasonUnknownNumber".equalsIgnoreCase(currentTag)) callData.BlockReasonUnknownNumber = Integer.parseInt(value);
                                            if ("FeaturesAssistedDialingUsed".equalsIgnoreCase(currentTag)) callData.FeaturesAssistedDialingUsed = Integer.parseInt(value);
                                            if ("FeaturesHdCall".equalsIgnoreCase(currentTag)) callData.FeaturesHdCall = Integer.parseInt(value);
                                            if ("FeaturePulledExternally".equalsIgnoreCase(currentTag)) callData.FeaturePulledExternally = Integer.parseInt(value);
                                            if ("FeatureRtt".equalsIgnoreCase(currentTag)) callData.FeatureRtt = Integer.parseInt(value);
                                            if ("FeaturesVideo".equalsIgnoreCase(currentTag)) callData.FeaturesVideo = Integer.parseInt(value);
                                            if ("FeatureVolte".equalsIgnoreCase(currentTag)) callData.FeatureVolte = Integer.parseInt(value);
                                            if ("FeatureWifi".equalsIgnoreCase(currentTag)) callData.FeatureWifi = Integer.parseInt(value);
                                            if ("IncomingType".equalsIgnoreCase(currentTag)) callData.IncomingType = Integer.parseInt(value);
                                            if ("MissedType".equalsIgnoreCase(currentTag)) callData.MissedType = Integer.parseInt(value);
                                            if ("OutgoingType".equalsIgnoreCase(currentTag)) callData.OutgoingType = Integer.parseInt(value);
                                            if ("PresentationAllowed".equalsIgnoreCase(currentTag)) callData.PresentationAllowed = Integer.parseInt(value);
                                            if ("PresentationPayPhone".equalsIgnoreCase(currentTag)) callData.PresentationPayPhone = Integer.parseInt(value);
                                            if ("PresentationRestricted".equalsIgnoreCase(currentTag)) callData.PresentationRestricted = Integer.parseInt(value);
                                            if ("PresentationUnknown".equalsIgnoreCase(currentTag)) callData.PresentationUnknown = Integer.parseInt(value);
                                            if ("PriorityNormal".equalsIgnoreCase(currentTag)) callData.PriorityNormal = Integer.parseInt(value);
                                            if ("PriorityUrgent".equalsIgnoreCase(currentTag)) callData.PriorityUrgent = Integer.parseInt(value);
                                            if ("RejectedType".equalsIgnoreCase(currentTag)) callData.RejectedType = Integer.parseInt(value);
                                            if ("VoicemailType".equalsIgnoreCase(currentTag)) callData.VoicemailType = Integer.parseInt(value);

                                            if ("AutoMissedEmergencyCall".equalsIgnoreCase(currentTag)) callData.AutoMissedEmergencyCall = Long.parseLong(value);
                                            if ("AutoMissedMaximumDialing".equalsIgnoreCase(currentTag)) callData.AutoMissedMaximumDialing = Long.parseLong(value);
                                            if ("AutoMissedMaximumRinging".equalsIgnoreCase(currentTag)) callData.AutoMissedMaximumRinging = Long.parseLong(value);
                                            if ("MissedReasonNotMissed".equalsIgnoreCase(currentTag)) callData.MissedReasonNotMissed = Long.parseLong(value);
                                            if ("UserMissedCallFiltersTimeout".equalsIgnoreCase(currentTag)) callData.UserMissedCallFiltersTimeout = Long.parseLong(value);
                                            if ("UserMissedCallScreeningServiceSilenced".equalsIgnoreCase(currentTag)) callData.UserMissedCallScreeningServiceSilenced = Long.parseLong(value);
                                            if ("UserMissedDndMode".equalsIgnoreCase(currentTag)) callData.UserMissedDndMode = Long.parseLong(value);
                                            if ("UserMissedLowRingVolume".equalsIgnoreCase(currentTag)) callData.UserMissedLowRingVolume = Long.parseLong(value);
                                            if ("UserMissedNoAnswer".equalsIgnoreCase(currentTag)) callData.UserMissedNoAnswer = Long.parseLong(value);
                                            if ("UserMissedNoVibrate".equalsIgnoreCase(currentTag)) callData.UserMissedNoVibrate = Long.parseLong(value);
                                            if ("UserMissedShortRing".equalsIgnoreCase(currentTag)) callData.UserMissedShortRing = Long.parseLong(value);
                                        }
                                        if (eventType == XmlPullParser.END_TAG) {
                                            if (xpp.getName().equalsIgnoreCase("call")) {
//                                                System.out.println("--------------------");
//                                                System.out.println("callData.Id: " + callData.Id);

//                                                System.out.println("callData.BlockReason: " + callData.BlockReason);
//                                                System.out.println("callData.CachedFormatedNumber: " + callData.CachedFormatedNumber);
//                                                System.out.println("callData.CachedLookupUri: " + callData.CachedLookupUri);
//                                                System.out.println("callData.CachedMatchedNumber: " + callData.CachedMatchedNumber);
//                                                System.out.println("callData.CachedName: " + callData.CachedName);
//                                                System.out.println("callData.CachedNormalizedNumber: " + callData.CachedNormalizedNumber);
//                                                System.out.println("callData.CachedNumberLabel: " + callData.CachedNumberLabel);
//                                                System.out.println("callData.CachedNumberType: " + callData.CachedNumberType);
//                                                System.out.println("callData.CachedPhotoId: " + callData.CachedPhotoId);
//                                                System.out.println("callData.CachedPhotoUri: " + callData.CachedPhotoUri);
//                                                System.out.println("callData.CallScreeningAppName: " + callData.CallScreeningAppName);
//                                                System.out.println("callData.CallScreeningComponentName: " + callData.CallScreeningComponentName);
//                                                System.out.println("callData.ComposerPhotoUri: " + callData.ComposerPhotoUri);
//                                                System.out.println("callData.ContentItemType: " + callData.ContentItemType);
//                                                System.out.println("callData.ContentType: " + callData.ContentType);
//                                                System.out.println("callData.CountryIso: " + callData.CountryIso);
//                                                System.out.println("callData.DataUsage: " + callData.DataUsage);
//                                                System.out.println("callData.Date: " + callData.Date);
//                                                System.out.println("callData.DefaultSortOrder: " + callData.DefaultSortOrder);
//                                                System.out.println("callData.Duration: " + callData.Duration);
//                                                System.out.println("callData.ExtraCallTypeFilter: " + callData.ExtraCallTypeFilter);
//                                                System.out.println("callData.Features: " + callData.Features);
//                                                System.out.println("callData.GeocodedLocation: " + callData.GeocodedLocation);
//                                                System.out.println("callData.IsRead: " + callData.IsRead);
//                                                System.out.println("callData.LastModified: " + callData.LastModified);
//                                                System.out.println("callData.LimitParamKey: " + callData.LimitParamKey);
//                                                System.out.println("callData.Location: " + callData.Location);
//                                                System.out.println("callData.MissedReason: " + callData.MissedReason);
//                                                System.out.println("callData.New: " + callData.New);
                                                System.out.println("callData.Number: " + callData.Number);
//                                                System.out.println("callData.NumberPresentation: " + callData.NumberPresentation);
//                                                System.out.println("callData.OffsetParamKey: " + callData.OffsetParamKey);
//                                                System.out.println("callData.PhoneAccountComponentName: " + callData.PhoneAccountComponentName);
//                                                System.out.println("callData.PhoneAccountId: " + callData.PhoneAccountId);
//                                                System.out.println("callData.PostDialDigits: " + callData.PostDialDigits);
//                                                System.out.println("callData.Priority: " + callData.Priority);
//                                                System.out.println("callData.Subject: " + callData.Subject);
//                                                System.out.println("callData.Transcription: " + callData.Transcription);
//                                                System.out.println("callData.Type: " + callData.Type);
//                                                System.out.println("callData.ViaNumber: " + callData.ViaNumber);
//                                                System.out.println("callData.VoicemailUri: " + callData.VoicemailUri);

//                                                System.out.println("callData.AnsweredExternallyType: " + callData.AnsweredExternallyType);
//                                                System.out.println("callData.BlockedType: " + callData.BlockedType);
//                                                System.out.println("callData.BlockReasonBlockedNumber: " + callData.BlockReasonBlockedNumber);
//                                                System.out.println("callData.BlockReasonCallScreeningService: " + callData.BlockReasonCallScreeningService);
//                                                System.out.println("callData.BlockReasonDirectToVoicemail: " + callData.BlockReasonDirectToVoicemail);
//                                                System.out.println("callData.BlockReasonNotBlocked: " + callData.BlockReasonNotBlocked);
//                                                System.out.println("callData.BlockReasonNotInContacts: " + callData.BlockReasonNotInContacts);
//                                                System.out.println("callData.BlockReasonPayPhone: " + callData.BlockReasonPayPhone);
//                                                System.out.println("callData.BlockReasonRestrictedNumber: " + callData.BlockReasonRestrictedNumber);
//                                                System.out.println("callData.BlockReasonUnknownNumber: " + callData.BlockReasonUnknownNumber);
//                                                System.out.println("callData.FeaturesAssistedDialingUsed: " + callData.FeaturesAssistedDialingUsed);
//                                                System.out.println("callData.FeaturesHdCall: " + callData.FeaturesHdCall);
//                                                System.out.println("callData.FeaturePulledExternally: " + callData.FeaturePulledExternally);
//                                                System.out.println("callData.FeatureRtt: " + callData.FeatureRtt);
//                                                System.out.println("callData.FeaturesVideo: " + callData.FeaturesVideo);
//                                                System.out.println("callData.FeatureVolte: " + callData.FeatureVolte);
//                                                System.out.println("callData.FeatureWifi: " + callData.FeatureWifi);
//                                                System.out.println("callData.IncomingType: " + callData.IncomingType);
//                                                System.out.println("callData.MissedType: " + callData.MissedType);
//                                                System.out.println("callData.OutgoingType: " + callData.OutgoingType);
//                                                System.out.println("callData.PresentationAllowed: " + callData.PresentationAllowed);
//                                                System.out.println("callData.PresentationPayPhone: " + callData.PresentationPayPhone);
//                                                System.out.println("callData.PresentationRestricted: " + callData.PresentationRestricted);
//                                                System.out.println("callData.PresentationUnknown: " + callData.PresentationUnknown);
//                                                System.out.println("callData.PriorityNormal: " + callData.PriorityNormal);
//                                                System.out.println("callData.PriorityUrgent: " + callData.PriorityUrgent);
//                                                System.out.println("callData.RejectedType: " + callData.RejectedType);
//                                                System.out.println("callData.VoicemailType: " + callData.VoicemailType);

//                                                System.out.println("callData.AutoMissedEmergencyCall: " + callData.AutoMissedEmergencyCall);
//                                                System.out.println("callData.AutoMissedMaximumDialing: " + callData.AutoMissedMaximumDialing);
//                                                System.out.println("callData.AutoMissedMaximumRinging: " + callData.AutoMissedMaximumRinging);
//                                                System.out.println("callData.MissedReasonNotMissed: " + callData.MissedReasonNotMissed);
//                                                System.out.println("callData.UserMissedCallFiltersTimeout: " + callData.UserMissedCallFiltersTimeout);
//                                                System.out.println("callData.UserMissedCallScreeningServiceSilenced: " + callData.UserMissedCallScreeningServiceSilenced);
//                                                System.out.println("callData.UserMissedDndMode: " + callData.UserMissedDndMode);
//                                                System.out.println("callData.UserMissedLowRingVolume: " + callData.UserMissedLowRingVolume);
//                                                System.out.println("callData.UserMissedNoAnswer: " + callData.UserMissedNoAnswer);
//                                                System.out.println("callData.UserMissedNoVibrate: " + callData.UserMissedNoVibrate);
//                                                System.out.println("callData.UserMissedShortRing: " + callData.UserMissedShortRing);
                                                callDataList.add(callData);
//                                                System.out.println("--------------------");
//                                                System.out.println("Parent tag: " + parentTag + " => End tag: " + tagName + " => id: " + messageId);
                                                messageTag = "";
                                            }
                                        }
                                    }
                                }
                            }
                            if(eventType == XmlPullParser.END_TAG) {
                                //System.out.println("End tag "+xpp.getName());
                                //if (xpp.getName().equalsIgnoreCase("Message")) {
                                //    System.out.println("Parent tag:" + parentTag + " => End tag:" + xpp.getName());
                                //}
                                if (xpp.getName().equalsIgnoreCase("CallLog")) {
//                                    System.out.println("Parent tag:" + parentTag + " => End tag:" + xpp.getName());
//                                    System.out.println("====================");
                                    parentTag = "";
                                }
                            }
                            eventType = xpp.next();
                        }
                    }

                } else if(eventType == XmlPullParser.TEXT) {
                    //System.out.println("Text "+xpp.getText());
                }
                eventType = xpp.next();
            }
//            System.out.println("========================================");
//            System.out.println("End document");
//            System.out.println("========================================");
//
//            System.out.println("CallLog: "+countCallLog);

            is.close();
            Toast.makeText(context, "Operation successful", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            // Tutaj by?? mo??e wrzut statusu operacji do logera?
        }


        return callDataList;
    }



    public int countEntriesInSmsXml(Context context,Uri uriFile) {
        int countSms = 0;
        int countInbox = 0;
        int countSent = 0;
        int countDraft = 0;
        int countOutbox = 0;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            //FileInputStream is = new FileInputStream(file);
            InputStream is = context.getContentResolver().openInputStream(uriFile);
            xpp.setInput(is, null);
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
                } else if(eventType == XmlPullParser.START_TAG) {
                    // Inbox Messages
                    if (xpp.getName().equalsIgnoreCase("Inbox")) {
                        String parentTag = xpp.getName();
                        eventType = xpp.next();
                        while (parentTag.equalsIgnoreCase("Inbox")) {
                            if(eventType == XmlPullParser.START_TAG) {
                                if (xpp.getName().equalsIgnoreCase("Message")) {
                                    countInbox++;
                                }
                            }
                            if(eventType == XmlPullParser.END_TAG) {
                                if (xpp.getName().equalsIgnoreCase("Inbox")) {
                                    parentTag = "";
                                }
                            }
                            eventType = xpp.next();
                        }
                    }
                    // Sent Messages
                    if (xpp.getName().equalsIgnoreCase("Sent")) {
                        String parentTag = xpp.getName();
                        eventType = xpp.next();
                        while (parentTag.equalsIgnoreCase("Sent")) {
                            if(eventType == XmlPullParser.START_TAG) {
                                if (xpp.getName().equalsIgnoreCase("Message")) {
                                    countSent++;
                                }
                            }
                            if(eventType == XmlPullParser.END_TAG) {
                                if (xpp.getName().equalsIgnoreCase("Sent")) {
                                    parentTag = "";
                                }
                            }
                            eventType = xpp.next();
                        }
                    }
                    // Draft Messages
                    if (xpp.getName().equalsIgnoreCase("Draft")) {
                        String parentTag = xpp.getName();
                        eventType = xpp.next();
                        while (parentTag.equalsIgnoreCase("Draft")) {
                            if(eventType == XmlPullParser.START_TAG) {
                                if (xpp.getName().equalsIgnoreCase("Message")) {
                                    String messageTag = xpp.getName();
                                    countDraft++;
                                }
                            }
                            if(eventType == XmlPullParser.END_TAG) {
                                if (xpp.getName().equalsIgnoreCase("Draft")) {
                                    parentTag = "";
                                }
                            }
                            eventType = xpp.next();
                        }
                    }
                    // Outbox Messages
                    if (xpp.getName().equalsIgnoreCase("Outbox")) {
                        String parentTag = xpp.getName();
                        eventType = xpp.next();
                        while (parentTag.equalsIgnoreCase("Outbox")) {
                            if(eventType == XmlPullParser.START_TAG) {
                                if (xpp.getName().equalsIgnoreCase("Message")) {
                                    String messageTag = xpp.getName();
                                    countOutbox++;
                                }
                            }
                            if(eventType == XmlPullParser.END_TAG) {
                                if (xpp.getName().equalsIgnoreCase("Outbox")) {
                                    parentTag = "";
                                }
                            }
                            eventType = xpp.next();
                        }
                    }
                } else if(eventType == XmlPullParser.TEXT) {  }
                eventType = xpp.next();
            }
            System.out.println("Inbox: "+countInbox);
            System.out.println("Sent: "+countSent);
            System.out.println("Draft: "+countDraft);
            System.out.println("Outbox: "+countOutbox);
            is.close();
            Toast.makeText(context, "Operation successful", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            // Tutaj by?? mo??e wrzut statusu operacji do logera?
        }
        countSms = countInbox + countSent + countDraft + countOutbox;
        return countSms;
    }


    public int countEntriesInCallLogXml(Context context,Uri uriFile) {
        int countCallLog = 0;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            //FileInputStream is = new FileInputStream(file);
            InputStream is = context.getContentResolver().openInputStream(uriFile);
            xpp.setInput(is, null);
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
                } else if(eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("CallLog")) {
                        String parentTag = xpp.getName();
                        eventType = xpp.next();
                        while (parentTag.equalsIgnoreCase("CallLog")) {
                            if(eventType == XmlPullParser.START_TAG) {
                                if (xpp.getName().equalsIgnoreCase("call")) {
                                    String messageTag = xpp.getName();
                                    countCallLog++;
                                }
                            }
                            if(eventType == XmlPullParser.END_TAG) {
                                if (xpp.getName().equalsIgnoreCase("CallLog")) {
                                    parentTag = "";
                                }
                            }
                            eventType = xpp.next();
                        }
                    }
                } else if(eventType == XmlPullParser.TEXT) {  }
                eventType = xpp.next();
            }
            System.out.println("CallLog: "+countCallLog);
            is.close();
            Toast.makeText(context, "Operation successful", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            // Tutaj by?? mo??e wrzut statusu operacji do logera?
        }

        return countCallLog;
    }


}
