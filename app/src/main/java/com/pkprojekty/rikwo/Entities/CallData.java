package com.pkprojekty.rikwo.Entities;

public class CallData {
    /**
     * Struktura danych zawierająca informacje o wiadomości sms
     * developer.android.com/reference/android/provider/CallLog.Calls
     */
    public String BlockReason;                              // Where the CallLog.Calls#TYPE is CallLog.Calls#BLOCKED_TYPE, indicates the reason why a call is blocked.
    public String CachedFormatedNumber;                     // The cached phone number, formatted with formatting rules based on the country the user was in when the call was made or received.
    public String CachedLookupUri;                          // The cached URI to look up the contact associated with the phone number, if it exists.
    public String CachedMatchedUri;                         // The cached phone number of the contact which matches this entry, if it exists.
    public String CachedName;                               // The cached name associated with the phone number, if it exists.
    public String CachedNormalizedNumber;                   // The cached normalized(E164) version of the phone number, if it exists.
    public String CachedNumberLabel;                        // The cached number label, for a custom number type, associated with the phone number, if it exists.
    public String CachedNumberType;                         // The cached number type (Home, Work, etc) associated with the phone number, if it exists.
    public String CachedPhotoId;                            // The cached photo id of the picture associated with the phone number, if it exists.
    public String CachedPhotoUri;                           // The cached photo URI of the picture associated with the phone number, if it exists.
    public String CallScreeningAppName;                     // The name of the app which blocked a call.
    public String CallScreeningComponentName;               // The ComponentName of the CallScreeningService which blocked this call.
    public String ComposerPhotoUri;                         // A reference to the picture that was sent via call composer.
    public String ContentItemType;                          // The MIME type of a CONTENT_URI sub-directory of a single call.
    public String ContentType;                              // The MIME type of CONTENT_URI and CONTENT_FILTER_URI providing a directory of calls.
    public String CountryIso;                               // The ISO 3166-1 two letters country code of the country where the user received or made the call.
    public String DataUsage;                                // The data usage of the call in bytes.
    public String Date;                                     // The date the call occured, in milliseconds since the epoch Type: INTEGER (long)
    public String DefaultSortOrder;                         // The default sort order for this table
    public String Duration;                                 // The duration of the call in seconds Type: INTEGER (long)
    public String ExtraCallTypeFilter;                      // An optional extra used with Calls.CONTENT_TYPE and Intent#ACTION_VIEW to specify that the presented list of calls should be filtered for a particular call type.
    public String Features;                                 // Bit-mask describing features of the call (e.g. video).
    public String GeocodedLocation;                         // A geocoded location for the number associated with this call.
    public String IsRead;                                   // Whether this item has been read or otherwise consumed by the user.
    public String LastModified;                             // The date the row is last inserted, updated, or marked as deleted, in milliseconds since the epoch.
    public String LimitParamKey;                            // Query parameter used to limit the number of call logs returned.
    public String Location;                                 // A reference to the location that was sent via call composer.
    public String MissedReason;                             // Where the CallLog.Calls#TYPE is CallLog.Calls#MISSED_TYPE, indicates factors which may have lead the user to miss the call.
    public String New;                                      // Whether or not the call has been acknowledged Type: INTEGER (boolean)
    public String Number;                                   // The phone number as the user entered it.
    public String NumberPresentation;                       // The number presenting rules set by the network.
    public String OffsetParamKey;                           // Query parameter used to specify the starting record to return.
    public String PhoneAccountComponentName;                // The component name of the account used to place or receive the call; in string form.
    public String PhoneAccountId;                           // The identifier for the account used to place or receive the call.
    public String PostDialDigits;                           // The post-dial portion of a dialed number, including any digits dialed after a TelecomManager#DTMF_CHARACTER_PAUSE or a TelecomManager.DTMF_CHARACTER_WAIT and these characters themselves.
    public String Priority;                                 // The priority of the call, as delivered via call composer.
    public String Subject;                                  // The subject of the call, as delivered via call composer.
    public String Transcription;                            // Transcription of the call or voicemail entry.
    public String Type;                                     // The type of the call (incoming, outgoing or missed).
    public String ViaNumber;                                // For an incoming call, the secondary line number the call was received via.
    public String VoicemailUri;                             // URI of the voicemail entry.

    public int AnsweredExternallyType;                      // Call log type for a call which was answered on another device.
    public int BlockedType;                                 // Call log type for calls blocked automatically.
    public int BlockReasonBlockedNumber;                    // Value for CallLog.Calls#BLOCK_REASON, set when CallLog.Calls#TYPE is CallLog.Calls#BLOCKED_TYPE to indicate that a call was blocked because it is in the BlockedNumbers provider.
    public int BlockReasonCallScreeningService;             // Value for CallLog.Calls#BLOCK_REASON, set when CallLog.Calls#TYPE is CallLog.Calls#BLOCKED_TYPE to indicate that a call was blocked by a CallScreeningService.
    public int BlockReasonDirectToVoicemail;                // Value for CallLog.Calls#BLOCK_REASON, set when CallLog.Calls#TYPE is CallLog.Calls#BLOCKED_TYPE to indicate that a call was blocked because the user configured a contact to be sent directly to voicemail.
    public int BlockReasonNotBlocked;                       // Value for CallLog.Calls#BLOCK_REASON, set as the default value when a call was not blocked by a CallScreeningService or any other system call blocking method.
    public int BlockReasonNotInContacts;                    // Value for CallLog.Calls#BLOCK_REASON, set when CallLog.Calls#TYPE is CallLog.Calls#BLOCKED_TYPE to indicate that a call was blocked because the user has chosen to block all calls from numbers not in their contacts.
    public int BlockReasonPayPhone;                         // Value for CallLog.Calls#BLOCK_REASON, set when CallLog.Calls#TYPE is CallLog.Calls#BLOCKED_TYPE to indicate that a call was blocked because the user has chosen to block all calls from pay phones.
    public int BlockReasonRestrictedNumber;                 // Value for CallLog.Calls#BLOCK_REASON, set when CallLog.Calls#TYPE is CallLog.Calls#BLOCKED_TYPE to indicate that a call was blocked because the user has chosen to block all calls from restricted numbers.
    public int BlockReasonUnknownNumber;                    // Value for CallLog.Calls#BLOCK_REASON, set when CallLog.Calls#TYPE is CallLog.Calls#BLOCKED_TYPE to indicate that a call was blocked because the user has chosen to block all calls from unknown numbers.
    public int FeaturesAssistedDialingUsed;                 // Indicates the call underwent Assisted Dialing.
    public int FeaturesHdCall;                              // Call was HD.
    public int FeaturePulledExternally;                     // Call was pulled externally.
    public int FeatureRtt;                                  // Call was on RTT at some point
    public int FeaturesVideo;                               // Call had video.
    public int FeatureVolte;                                // Call was VoLTE
    public int FeatureWifi;                                 // Call was WIFI call.
    public int IncomingType;                                // Call log type for incoming calls.
    public int MissedType;                                  // Call log type for missed calls.
    public int OutgoingType;                                // Call log type for outgoing calls.
    public int PresentationAllowed;                         // Number is allowed to display for caller id.
    public int PresentationPayPhone;                        // Number is a pay phone.
    public int PresentationRestricted;                      // Number is blocked by user.
    public int PresentationUnavailable;                     // Number is unavailable.
    public int PresentationUnknown;                         // Number is not specified or unknown by network.
    public int PriorityNormal;                              // Used as a value in the PRIORITY column.
    public int PriorityUrgent;                              // Used as a value in the PRIORITY column.
    public int RejectedType;                                // Call log type for calls rejected by direct user action.
    public int VoicemailType;                               // Call log type for voicemails.

    public long AutoMissedEmergencyCall;                    // Value for CallLog.Calls#MISSED_REASON, set when CallLog.Calls#TYPE is CallLog.Calls#MISSED_TYPE to indicate that a call was automatically rejected by system because an ongoing emergency call.
    public long AutoMissedMaximumDialing;                   // Value for CallLog.Calls#MISSED_REASON, set when CallLog.Calls#TYPE is CallLog.Calls#MISSED_TYPE to indicate that a call was automatically rejected by system because the system cannot support any more dialing calls.
    public long AutoMissedMaximumRinging;                   // Value for CallLog.Calls#MISSED_REASON, set when CallLog.Calls#TYPE is CallLog.Calls#MISSED_TYPE to indicate that a call was automatically rejected by system because the system cannot support any more ringing calls.
    public long MissedReasonNotMissed;                      // Value for CallLog.Calls#MISSED_REASON, set as the default value when a call was not missed.
    public long UserMissedCallFiltersTimeout;               // When CallLog.Calls#TYPE is CallLog.Calls#MISSED_TYPE, set this bit when the call filters timed out.
    public long UserMissedCallScreeningServiceSilenced;     // When CallLog.Calls#TYPE is CallLog.Calls#MISSED_TYPE, set this bit when this call is silenced by the call screening service.
    public long UserMissedDndMode;                          // When CallLog.Calls#TYPE is CallLog.Calls#MISSED_TYPE, set this bit when this call is silenced because the phone is in 'do not disturb mode'.
    public long UserMissedLowRingVolume;                    // When CallLog.Calls#TYPE is CallLog.Calls#MISSED_TYPE, set this bit when this call rings with a low ring volume.
    public long UserMissedNoAnswer;                         // When CallLog.Calls#TYPE is CallLog.Calls#MISSED_TYPE, set this bit when the call was missed just because user didn't answer it.
    public long UserMissedNoVibrate;                        // When CallLog.Calls#TYPE is CallLog.Calls#MISSED_TYPE set this bit when this call rings without vibration.
    public long UserMissedShortRing;                        // When CallLog.Calls#TYPE is CallLog.Calls#MISSED_TYPE, set this bit when this call rang for a short period of time.
}
