package com.pkprojekty.rikwo.CallLog;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.CallLog;

import com.pkprojekty.rikwo.Entities.CallData;

import java.util.ArrayList;
import java.util.List;

public class BackupCallLog {
    private final Context context;

    public BackupCallLog(Context context) {
        this.context = context;
    }

    public List<CallData> getAllCalls() {
        List<CallData> callsDataList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor query = contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (query != null) {
            try {
                while (query.moveToNext()) {
                    CallData callData = new CallData();

                    callData.Id = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls._ID
                            )
                    );
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        callData.BlockReason = query.getString(
                                query.getColumnIndexOrThrow(
                                        CallLog.Calls.BLOCK_REASON
                                )
                        );
                    }
                    callData.CachedFormatedNumber = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.CACHED_FORMATTED_NUMBER
                            )
                    );
                    callData.CachedLookupUri = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.CACHED_LOOKUP_URI
                            )
                    );
                    callData.CachedMatchedNumber = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.CACHED_MATCHED_NUMBER
                            )
                    );
                    callData.CachedName = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.CACHED_NAME
                            )
                    );
                    callData.CachedNormalizedNumber = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.CACHED_NORMALIZED_NUMBER
                            )
                    );
                    callData.CachedNumberLabel = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.CACHED_NUMBER_LABEL
                            )
                    );
                    callData.CachedNumberType = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.CACHED_NUMBER_TYPE
                            )
                    );
                    callData.CachedPhotoId = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.CACHED_PHOTO_ID
                            )
                    );
                    callData.CachedPhotoUri = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.CACHED_PHOTO_URI
                            )
                    );
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        callData.CallScreeningAppName = query.getString(
                                query.getColumnIndexOrThrow(
                                        CallLog.Calls.CALL_SCREENING_APP_NAME
                                )
                        );
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        callData.CallScreeningComponentName = query.getString(
                                query.getColumnIndexOrThrow(
                                        CallLog.Calls.CALL_SCREENING_COMPONENT_NAME
                                )
                        );
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.ComposerPhotoUri = query.getString(
                                query.getColumnIndexOrThrow(
                                        CallLog.Calls.COMPOSER_PHOTO_URI
                                )
                        );
                    }
                    callData.ContentItemType = CallLog.Calls.CONTENT_ITEM_TYPE;
                    callData.ContentType = CallLog.Calls.CONTENT_TYPE;
                    callData.CountryIso = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.COUNTRY_ISO
                            )
                    );
                    callData.DataUsage = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.DATA_USAGE
                            )
                    );
                    callData.Date = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.DATE
                            )
                    );
                    callData.DefaultSortOrder = CallLog.Calls.DEFAULT_SORT_ORDER;
                    callData.Duration = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.DURATION
                            )
                    );
                    callData.ExtraCallTypeFilter = CallLog.Calls.EXTRA_CALL_TYPE_FILTER;
                    callData.Features = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.FEATURES
                            )
                    );
                    callData.GeocodedLocation = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.GEOCODED_LOCATION
                            )
                    );
                    callData.IsRead = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.IS_READ
                            )
                    );
                    callData.LastModified = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.LAST_MODIFIED
                            )
                    );
                    callData.LimitParamKey = CallLog.Calls.LIMIT_PARAM_KEY;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.Location = query.getString(
                                query.getColumnIndexOrThrow(
                                        CallLog.Calls.LOCATION
                                )
                        );
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.MissedReason = query.getString(
                                query.getColumnIndexOrThrow(
                                        CallLog.Calls.MISSED_REASON
                                )
                        );
                    }
                    callData.New = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.NEW
                            )
                    );
                    callData.Number = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.NUMBER
                            )
                    );
                    callData.NumberPresentation = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.NUMBER_PRESENTATION
                            )
                    );
                    callData.OffsetParamKey = CallLog.Calls.OFFSET_PARAM_KEY;
                    callData.PhoneAccountComponentName = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.PHONE_ACCOUNT_COMPONENT_NAME
                            )
                    );
                    callData.PhoneAccountId = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.PHONE_ACCOUNT_ID
                            )
                    );
                    callData.PostDialDigits = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.POST_DIAL_DIGITS
                            )
                    );
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.Priority = query.getString(
                                query.getColumnIndexOrThrow(
                                        CallLog.Calls.PRIORITY
                                )
                        );
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.Subject = query.getString(
                                query.getColumnIndexOrThrow(
                                        CallLog.Calls.SUBJECT
                                )
                        );
                    }
                    callData.Transcription = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.TRANSCRIPTION
                            )
                    );
                    callData.Type = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.TYPE
                            )
                    );
                    callData.ViaNumber = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.VIA_NUMBER
                            )
                    );
                    callData.VoicemailUri = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.VOICEMAIL_URI
                            )
                    );
                    callData.AnsweredExternallyType = CallLog.Calls.ANSWERED_EXTERNALLY_TYPE;
                    callData.BlockedType = CallLog.Calls.BLOCKED_TYPE;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        callData.BlockReasonBlockedNumber = CallLog.Calls.BLOCK_REASON_BLOCKED_NUMBER;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        callData.BlockReasonCallScreeningService = CallLog.Calls.BLOCK_REASON_CALL_SCREENING_SERVICE;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        callData.BlockReasonDirectToVoicemail = CallLog.Calls.BLOCK_REASON_DIRECT_TO_VOICEMAIL;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        callData.BlockReasonNotBlocked = CallLog.Calls.BLOCK_REASON_NOT_BLOCKED;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        callData.BlockReasonNotInContacts = CallLog.Calls.BLOCK_REASON_NOT_IN_CONTACTS;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        callData.BlockReasonPayPhone = CallLog.Calls.BLOCK_REASON_PAY_PHONE;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        callData.BlockReasonRestrictedNumber = CallLog.Calls.BLOCK_REASON_RESTRICTED_NUMBER;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        callData.BlockReasonUnknownNumber = CallLog.Calls.BLOCK_REASON_UNKNOWN_NUMBER;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        callData.FeaturesAssistedDialingUsed = CallLog.Calls.FEATURES_ASSISTED_DIALING_USED;
                    }
                    callData.FeaturesHdCall = CallLog.Calls.FEATURES_HD_CALL;
                    callData.FeaturePulledExternally = CallLog.Calls.FEATURES_PULLED_EXTERNALLY;
                    callData.FeatureRtt = CallLog.Calls.FEATURES_RTT;
                    callData.FeaturesVideo = CallLog.Calls.FEATURES_VIDEO;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        callData.FeatureVolte = CallLog.Calls.FEATURES_VOLTE;
                    }
                    callData.FeatureWifi = CallLog.Calls.FEATURES_WIFI;
                    callData.IncomingType = CallLog.Calls.INCOMING_TYPE;
                    callData.MissedType = CallLog.Calls.MISSED_TYPE;
                    callData.OutgoingType = CallLog.Calls.OUTGOING_TYPE;
                    callData.PresentationAllowed = CallLog.Calls.PRESENTATION_ALLOWED;
                    callData.PresentationPayPhone = CallLog.Calls.PRESENTATION_PAYPHONE;
                    callData.PresentationRestricted = CallLog.Calls.PRESENTATION_RESTRICTED;
                    //  Added in Android Tiramisu
//                    callData.PresentationUnavailable = CallLog.Calls.PRESENTATION_UNAVAILABLE;
                    callData.PresentationUnknown = CallLog.Calls.PRESENTATION_UNKNOWN;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.PriorityNormal = CallLog.Calls.PRIORITY_NORMAL;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.PriorityUrgent = CallLog.Calls.PRIORITY_URGENT;
                    }
                    callData.RejectedType = CallLog.Calls.REJECTED_TYPE;
                    callData.VoicemailType = CallLog.Calls.VOICEMAIL_TYPE;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.AutoMissedEmergencyCall = CallLog.Calls.AUTO_MISSED_EMERGENCY_CALL;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.AutoMissedMaximumDialing = CallLog.Calls.AUTO_MISSED_MAXIMUM_DIALING;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.AutoMissedMaximumRinging = CallLog.Calls.AUTO_MISSED_MAXIMUM_RINGING;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.MissedReasonNotMissed = CallLog.Calls.MISSED_REASON_NOT_MISSED;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.UserMissedCallFiltersTimeout = CallLog.Calls.USER_MISSED_CALL_FILTERS_TIMEOUT;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.UserMissedCallScreeningServiceSilenced = CallLog.Calls.USER_MISSED_CALL_SCREENING_SERVICE_SILENCED;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.UserMissedDndMode = CallLog.Calls.USER_MISSED_DND_MODE;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.UserMissedLowRingVolume = CallLog.Calls.USER_MISSED_LOW_RING_VOLUME;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.UserMissedNoAnswer = CallLog.Calls.USER_MISSED_NO_ANSWER;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.UserMissedNoVibrate = CallLog.Calls.USER_MISSED_NO_VIBRATE;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.UserMissedShortRing = CallLog.Calls.USER_MISSED_SHORT_RING;
                    }

                    callsDataList.add(callData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                query.close();
            }
        }

        return callsDataList;
    }

    public String countCallLog() {
        String count = "";
        ContentResolver contentResolver = context.getContentResolver();
        Cursor query = contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (query != null) {
            try {
                if(query.moveToLast())
                    count = String.valueOf(query.getPosition());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                query.close();
            }
        }
        return count;
    }
}
