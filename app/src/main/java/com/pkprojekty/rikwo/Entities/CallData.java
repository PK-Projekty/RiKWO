package com.pkprojekty.rikwo.Entities;

public class CallData {
    /**
     * Struktura danych zawierająca informacje o wiadomości sms
     * developer.android.com/reference/android/provider/CallLog.Calls
     */
    public String Id;                                       // API level 1 The unique ID for a row.

    public String BlockReason;                              // API level 29 Where the CallLog.Calls#TYPE is CallLog.Calls#BLOCKED_TYPE, indicates the reason why a call is blocked.
    public String CachedFormatedNumber;                     // API level 21 The cached phone number, formatted with formatting rules based on the country the user was in when the call was made or received.
    public String CachedLookupUri;                          // API level 21 The cached URI to look up the contact associated with the phone number, if it exists.
    public String CachedMatchedNumber;                      // API level 21 The cached phone number of the contact which matches this entry, if it exists.
    public String CachedName;                               // API level 1 The cached name associated with the phone number, if it exists.
    public String CachedNormalizedNumber;                   // API level 21 The cached normalized(E164) version of the phone number, if it exists.
    public String CachedNumberLabel;                        // API level 1 The cached number label, for a custom number type, associated with the phone number, if it exists.
    public String CachedNumberType;                         // API level 1 The cached number type (Home, Work, etc) associated with the phone number, if it exists.
    public String CachedPhotoId;                            // API level 21 The cached photo id of the picture associated with the phone number, if it exists.
    public String CachedPhotoUri;                           // API level 23 The cached photo URI of the picture associated with the phone number, if it exists.
    public String CallScreeningAppName;                     // API level 29 The name of the app which blocked a call.
    public String CallScreeningComponentName;               // API level 29 The ComponentName of the CallScreeningService which blocked this call.
    public String ComposerPhotoUri;                         // API level 31 A reference to the picture that was sent via call composer.
    public String ContentItemType;                          // API level 1 The MIME type of a CONTENT_URI sub-directory of a single call.
    public String ContentType;                              // API level 1 The MIME type of CONTENT_URI and CONTENT_FILTER_URI providing a directory of calls.
    public String CountryIso;                               // API level 21 The ISO 3166-1 two letters country code of the country where the user received or made the call.
    public String DataUsage;                                // API level 21 The data usage of the call in bytes.
    public String Date;                                     // API level 1 The date the call occured, in milliseconds since the epoch Type: INTEGER (long)
    public String DefaultSortOrder;                         // API level 1 The default sort order for this table
    public String Duration;                                 // API level 1 The duration of the call in seconds Type: INTEGER (long)
    public String ExtraCallTypeFilter;                      // API level 21 An optional extra used with Calls.CONTENT_TYPE and Intent#ACTION_VIEW to specify that the presented list of calls should be filtered for a particular call type.
    public String Features;                                 // API level 21 Bit-mask describing features of the call (e.g. video).
    public String GeocodedLocation;                         // API level 21 A geocoded location for the number associated with this call.
    public String IsRead;                                   // API level 14 Whether this item has been read or otherwise consumed by the user.
    public String LastModified;                             // API level 24 The date the row is last inserted, updated, or marked as deleted, in milliseconds since the epoch.
    public String LimitParamKey;                            // API level 17 Query parameter used to limit the number of call logs returned.
    public String Location;                                 // API level 31 A reference to the location that was sent via call composer.
    public String MissedReason;                             // API level 31 Where the CallLog.Calls#TYPE is CallLog.Calls#MISSED_TYPE, indicates factors which may have lead the user to miss the call.
    public String New;                                      // API level 1 Whether or not the call has been acknowledged Type: INTEGER (boolean)
    public String Number;                                   // API level 1 The phone number as the user entered it.
    public String NumberPresentation;                       // API level 19 The number presenting rules set by the network.
    public String OffsetParamKey;                           // API level 17 Query parameter used to specify the starting record to return.
    public String PhoneAccountComponentName;                // API level 21 The component name of the account used to place or receive the call; in string form.
    public String PhoneAccountId;                           // API level 21 The identifier for the account used to place or receive the call.
    public String PostDialDigits;                           // API level 24 The post-dial portion of a dialed number, including any digits dialed after a TelecomManager#DTMF_CHARACTER_PAUSE or a TelecomManager.DTMF_CHARACTER_WAIT and these characters themselves.
    public String Priority;                                 // API level 31 The priority of the call, as delivered via call composer.
    public String Subject;                                  // API level 31 The subject of the call, as delivered via call composer.
    public String Transcription;                            // API level 21 Transcription of the call or voicemail entry.
    public String Type;                                     // API level 1 The type of the call (incoming, outgoing or missed).
    public String ViaNumber;                                // API level 24 For an incoming call, the secondary line number the call was received via.
    public String VoicemailUri;                             // API level 21 URI of the voicemail entry.

    public int AnsweredExternallyType;                      // API level 25 Call log type for a call which was answered on another device.
    public int BlockedType;                                 // API level 24 Call log type for calls blocked automatically.
    public int BlockReasonBlockedNumber;                    // API level 29 Value for CallLog.Calls#BLOCK_REASON, set when CallLog.Calls#TYPE is CallLog.Calls#BLOCKED_TYPE to indicate that a call was blocked because it is in the BlockedNumbers provider.
    public int BlockReasonCallScreeningService;             // API level 29 Value for CallLog.Calls#BLOCK_REASON, set when CallLog.Calls#TYPE is CallLog.Calls#BLOCKED_TYPE to indicate that a call was blocked by a CallScreeningService.
    public int BlockReasonDirectToVoicemail;                // API level 29 Value for CallLog.Calls#BLOCK_REASON, set when CallLog.Calls#TYPE is CallLog.Calls#BLOCKED_TYPE to indicate that a call was blocked because the user configured a contact to be sent directly to voicemail.
    public int BlockReasonNotBlocked;                       // API level 29 Value for CallLog.Calls#BLOCK_REASON, set as the default value when a call was not blocked by a CallScreeningService or any other system call blocking method.
    public int BlockReasonNotInContacts;                    // API level 29 Value for CallLog.Calls#BLOCK_REASON, set when CallLog.Calls#TYPE is CallLog.Calls#BLOCKED_TYPE to indicate that a call was blocked because the user has chosen to block all calls from numbers not in their contacts.
    public int BlockReasonPayPhone;                         // API level 29 Value for CallLog.Calls#BLOCK_REASON, set when CallLog.Calls#TYPE is CallLog.Calls#BLOCKED_TYPE to indicate that a call was blocked because the user has chosen to block all calls from pay phones.
    public int BlockReasonRestrictedNumber;                 // API level 29 Value for CallLog.Calls#BLOCK_REASON, set when CallLog.Calls#TYPE is CallLog.Calls#BLOCKED_TYPE to indicate that a call was blocked because the user has chosen to block all calls from restricted numbers.
    public int BlockReasonUnknownNumber;                    // API level 29 Value for CallLog.Calls#BLOCK_REASON, set when CallLog.Calls#TYPE is CallLog.Calls#BLOCKED_TYPE to indicate that a call was blocked because the user has chosen to block all calls from unknown numbers.
    public int FeaturesAssistedDialingUsed;                 // API level 30 Indicates the call underwent Assisted Dialing.
    public int FeaturesHdCall;                              // API level 26 Call was HD.
    public int FeaturePulledExternally;                     // API level 25 Call was pulled externally.
    public int FeatureRtt;                                  // API level 28 Call was on RTT at some point
    public int FeaturesVideo;                               // API level 21 Call had video.
    public int FeatureVolte;                                // API level 30 Call was VoLTE
    public int FeatureWifi;                                 // API level 26 Call was WIFI call.
    public int IncomingType;                                // API level 1 Call log type for incoming calls.
    public int MissedType;                                  // API level 1 Call log type for missed calls.
    public int OutgoingType;                                // API level 1 Call log type for outgoing calls.
    public int PresentationAllowed;                         // API level 19 Number is allowed to display for caller id.
    public int PresentationPayPhone;                        // API level 19 Number is a pay phone.
    public int PresentationRestricted;                      // API level 19 Number is blocked by user.
//    public int PresentationUnavailable;                     // Android Tiramisu Number is unavailable.
    public int PresentationUnknown;                         // API level 19 Number is not specified or unknown by network.
    public int PriorityNormal;                              // API level 31 Used as a value in the PRIORITY column.
    public int PriorityUrgent;                              // API level 31 Used as a value in the PRIORITY column.
    public int RejectedType;                                // API level 24 Call log type for calls rejected by direct user action.
    public int VoicemailType;                               // API level 21 Call log type for voicemails.

    public long AutoMissedEmergencyCall;                    // API level 31 Value for CallLog.Calls#MISSED_REASON, set when CallLog.Calls#TYPE is CallLog.Calls#MISSED_TYPE to indicate that a call was automatically rejected by system because an ongoing emergency call.
    public long AutoMissedMaximumDialing;                   // API level 31 Value for CallLog.Calls#MISSED_REASON, set when CallLog.Calls#TYPE is CallLog.Calls#MISSED_TYPE to indicate that a call was automatically rejected by system because the system cannot support any more dialing calls.
    public long AutoMissedMaximumRinging;                   // API level 31 Value for CallLog.Calls#MISSED_REASON, set when CallLog.Calls#TYPE is CallLog.Calls#MISSED_TYPE to indicate that a call was automatically rejected by system because the system cannot support any more ringing calls.
    public long MissedReasonNotMissed;                      // API level 31 Value for CallLog.Calls#MISSED_REASON, set as the default value when a call was not missed.
    public long UserMissedCallFiltersTimeout;               // API level 31 When CallLog.Calls#TYPE is CallLog.Calls#MISSED_TYPE, set this bit when the call filters timed out.
    public long UserMissedCallScreeningServiceSilenced;     // API level 31 When CallLog.Calls#TYPE is CallLog.Calls#MISSED_TYPE, set this bit when this call is silenced by the call screening service.
    public long UserMissedDndMode;                          // API level 31 When CallLog.Calls#TYPE is CallLog.Calls#MISSED_TYPE, set this bit when this call is silenced because the phone is in 'do not disturb mode'.
    public long UserMissedLowRingVolume;                    // API level 31 When CallLog.Calls#TYPE is CallLog.Calls#MISSED_TYPE, set this bit when this call rings with a low ring volume.
    public long UserMissedNoAnswer;                         // API level 31 When CallLog.Calls#TYPE is CallLog.Calls#MISSED_TYPE, set this bit when the call was missed just because user didn't answer it.
    public long UserMissedNoVibrate;                        // API level 31 When CallLog.Calls#TYPE is CallLog.Calls#MISSED_TYPE set this bit when this call rings without vibration.
    public long UserMissedShortRing;                        // API level 31 When CallLog.Calls#TYPE is CallLog.Calls#MISSED_TYPE, set this bit when this call rang for a short period of time.
}
