package com.pkprojekty.rikwo.CallLog;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.CallLog;

import androidx.annotation.RequiresApi;

import com.pkprojekty.rikwo.Entities.CallData;

import java.util.ArrayList;
import java.util.List;

public class BackupCallLog {
    private final Context context;

    public BackupCallLog(Context context) {
        this.context = context;
    }

    private List<CallData> getAllCalls() {
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
                    callData.ContentItemType = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.CONTENT_ITEM_TYPE
                            )
                    );
                    callData.ContentType = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.CONTENT_TYPE
                            )
                    );
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
                    callData.DefaultSortOrder = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.DEFAULT_SORT_ORDER
                            )
                    );
                    callData.Duration = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.DURATION
                            )
                    );
                    callData.ExtraCallTypeFilter = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.EXTRA_CALL_TYPE_FILTER
                            )
                    );
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
                    callData.LimitParamKey = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.LIMIT_PARAM_KEY
                            )
                    );
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
                    callData.OffsetParamKey = query.getString(
                            query.getColumnIndexOrThrow(
                                    CallLog.Calls.OFFSET_PARAM_KEY
                            )
                    );
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
                    callData.AnsweredExternallyType = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(CallLog.Calls.ANSWERED_EXTERNALLY_TYPE)
                            )
                    );
                    callData.BlockedType = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(CallLog.Calls.BLOCKED_TYPE)
                            )
                    );
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        callData.BlockReasonBlockedNumber = query.getInt(
                                query.getColumnIndexOrThrow(
                                        String.valueOf(CallLog.Calls.BLOCK_REASON_BLOCKED_NUMBER)
                                )
                        );
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        callData.BlockReasonCallScreeningService = query.getInt(
                                query.getColumnIndexOrThrow(
                                        String.valueOf(CallLog.Calls.BLOCK_REASON_CALL_SCREENING_SERVICE)
                                )
                        );
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        callData.BlockReasonDirectToVoicemail = query.getInt(
                                query.getColumnIndexOrThrow(
                                        String.valueOf(CallLog.Calls.BLOCK_REASON_DIRECT_TO_VOICEMAIL)
                                )
                        );
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        callData.BlockReasonNotBlocked = query.getInt(
                                query.getColumnIndexOrThrow(
                                        String.valueOf(CallLog.Calls.BLOCK_REASON_NOT_BLOCKED)
                                )
                        );
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        callData.BlockReasonNotInContacts = query.getInt(
                                query.getColumnIndexOrThrow(
                                        String.valueOf(CallLog.Calls.BLOCK_REASON_NOT_IN_CONTACTS)
                                )
                        );
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        callData.BlockReasonPayPhone = query.getInt(
                                query.getColumnIndexOrThrow(
                                        String.valueOf(CallLog.Calls.BLOCK_REASON_PAY_PHONE)
                                )
                        );
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        callData.BlockReasonRestrictedNumber = query.getInt(
                                query.getColumnIndexOrThrow(
                                        String.valueOf(CallLog.Calls.BLOCK_REASON_RESTRICTED_NUMBER)
                                )
                        );
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        callData.BlockReasonUnknownNumber = query.getInt(
                                query.getColumnIndexOrThrow(
                                        String.valueOf(CallLog.Calls.BLOCK_REASON_UNKNOWN_NUMBER)
                                )
                        );
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        callData.FeaturesAssistedDialingUsed = query.getInt(
                                query.getColumnIndexOrThrow(
                                        String.valueOf(CallLog.Calls.FEATURES_ASSISTED_DIALING_USED)
                                )
                        );
                    }
                    callData.FeaturesHdCall = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(CallLog.Calls.FEATURES_HD_CALL)
                            )
                    );
                    callData.FeaturePulledExternally = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(CallLog.Calls.FEATURES_PULLED_EXTERNALLY)
                            )
                    );
                    callData.FeatureRtt = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(CallLog.Calls.FEATURES_RTT)
                            )
                    );
                    callData.FeaturesVideo = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(CallLog.Calls.FEATURES_VIDEO)
                            )
                    );
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        callData.FeatureVolte = query.getInt(
                                query.getColumnIndexOrThrow(
                                        String.valueOf(CallLog.Calls.FEATURES_VOLTE)
                                )
                        );
                    }
                    callData.FeatureWifi = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(CallLog.Calls.FEATURES_WIFI)
                            )
                    );
                    callData.IncomingType = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(CallLog.Calls.INCOMING_TYPE)
                            )
                    );
                    callData.MissedType = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(CallLog.Calls.MISSED_TYPE)
                            )
                    );
                    callData.OutgoingType = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(CallLog.Calls.OUTGOING_TYPE)
                            )
                    );
                    callData.PresentationAllowed = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(CallLog.Calls.PRESENTATION_ALLOWED)
                            )
                    );
                    callData.PresentationPayPhone = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(CallLog.Calls.PRESENTATION_PAYPHONE)
                            )
                    );
                    callData.PresentationRestricted = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(CallLog.Calls.PRESENTATION_RESTRICTED)
                            )
                    );
                    //  Added in Android Tiramisu
//                    callData.PresentationUnavailable = query.getInt(
//                            query.getColumnIndexOrThrow(
//                                    String.valueOf(CallLog.Calls.PRESENTATION_UNAVAILABLE)
//                            )
//                    );
                    callData.PresentationUnknown = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(CallLog.Calls.PRESENTATION_UNKNOWN)
                            )
                    );
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.PriorityNormal = query.getInt(
                                query.getColumnIndexOrThrow(
                                        String.valueOf(CallLog.Calls.PRIORITY_NORMAL)
                                )
                        );
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.PriorityUrgent = query.getInt(
                                query.getColumnIndexOrThrow(
                                        String.valueOf(CallLog.Calls.PRIORITY_URGENT)
                                )
                        );
                    }
                    callData.RejectedType = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(CallLog.Calls.REJECTED_TYPE)
                            )
                    );
                    callData.VoicemailType = query.getInt(
                            query.getColumnIndexOrThrow(
                                    String.valueOf(CallLog.Calls.VOICEMAIL_TYPE)
                            )
                    );
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.AutoMissedEmergencyCall = query.getLong(
                                query.getColumnIndexOrThrow(
                                        String.valueOf(CallLog.Calls.AUTO_MISSED_EMERGENCY_CALL)
                                )
                        );
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.AutoMissedMaximumDialing = query.getLong(
                                query.getColumnIndexOrThrow(
                                        String.valueOf(CallLog.Calls.AUTO_MISSED_MAXIMUM_DIALING)
                                )
                        );
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.AutoMissedMaximumRinging = query.getLong(
                                query.getColumnIndexOrThrow(
                                        String.valueOf(CallLog.Calls.AUTO_MISSED_MAXIMUM_RINGING)
                                )
                        );
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.MissedReasonNotMissed = query.getLong(
                                query.getColumnIndexOrThrow(
                                        String.valueOf(CallLog.Calls.MISSED_REASON_NOT_MISSED)
                                )
                        );
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.UserMissedCallFiltersTimeout = query.getLong(
                                query.getColumnIndexOrThrow(
                                        String.valueOf(CallLog.Calls.USER_MISSED_CALL_FILTERS_TIMEOUT)
                                )
                        );
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.UserMissedCallScreeningServiceSilenced = query.getLong(
                                query.getColumnIndexOrThrow(
                                        String.valueOf(CallLog.Calls.USER_MISSED_CALL_SCREENING_SERVICE_SILENCED)
                                )
                        );
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.UserMissedDndMode = query.getLong(
                                query.getColumnIndexOrThrow(
                                        String.valueOf(CallLog.Calls.USER_MISSED_DND_MODE)
                                )
                        );
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.UserMissedLowRingVolume = query.getLong(
                                query.getColumnIndexOrThrow(
                                        String.valueOf(CallLog.Calls.USER_MISSED_LOW_RING_VOLUME)
                                )
                        );
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.UserMissedNoAnswer = query.getLong(
                                query.getColumnIndexOrThrow(
                                        String.valueOf(CallLog.Calls.USER_MISSED_NO_ANSWER)
                                )
                        );
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.UserMissedNoVibrate = query.getLong(
                                query.getColumnIndexOrThrow(
                                        String.valueOf(CallLog.Calls.USER_MISSED_NO_VIBRATE)
                                )
                        );
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        callData.UserMissedShortRing = query.getLong(
                                query.getColumnIndexOrThrow(
                                        String.valueOf(CallLog.Calls.USER_MISSED_SHORT_RING)
                                )
                        );
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
}
