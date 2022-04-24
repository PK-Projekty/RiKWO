package com.pkprojekty.rikwo.CallLog;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Build;
import android.provider.CallLog;
import android.telecom.Call;

import com.pkprojekty.rikwo.Entities.CallData;

import java.util.List;

public class RestoreCallLog {
    private final Context context;

    public RestoreCallLog(Context context) {
        this.context = context;
    }

    public void setAllCallLog(List<CallData> callDataList) {
        for (CallData callData : callDataList) {
            ContentResolver contentResolver = context.getContentResolver();
            ContentValues values = new ContentValues();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.put(CallLog.Calls.BLOCK_REASON, callData.BlockReason);
            }
            values.put(CallLog.Calls.CACHED_FORMATTED_NUMBER, callData.CachedFormatedNumber);
            values.put(CallLog.Calls.CACHED_LOOKUP_URI, callData.CachedLookupUri);
            values.put(CallLog.Calls.CACHED_MATCHED_NUMBER, callData.CachedMatchedNumber);
            values.put(CallLog.Calls.CACHED_NAME, callData.CachedName);
            values.put(CallLog.Calls.CACHED_NORMALIZED_NUMBER, callData.CachedNormalizedNumber);
            values.put(CallLog.Calls.CACHED_NUMBER_LABEL, callData.CachedNumberLabel);
            values.put(CallLog.Calls.CACHED_NUMBER_TYPE, callData.CachedNumberType);
            values.put(CallLog.Calls.CACHED_PHOTO_ID, callData.CachedPhotoId);
            values.put(CallLog.Calls.CACHED_PHOTO_URI, callData.CachedPhotoUri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.put(CallLog.Calls.CALL_SCREENING_APP_NAME, callData.CallScreeningAppName);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.put(CallLog.Calls.CALL_SCREENING_COMPONENT_NAME, callData.CallScreeningComponentName);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                values.put(CallLog.Calls.COMPOSER_PHOTO_URI, callData.ComposerPhotoUri);
            }
//            values.put(CallLog.Calls.CONTENT_ITEM_TYPE, callData.ContentItemType);
//            values.put(CallLog.Calls.CONTENT_TYPE, callData.ContentType);
            values.put(CallLog.Calls.COUNTRY_ISO, callData.CountryIso);
            values.put(CallLog.Calls.DATA_USAGE, callData.DataUsage);
            values.put(CallLog.Calls.DATE, callData.Date);
//            values.put(CallLog.Calls.DEFAULT_SORT_ORDER, callData.DefaultSortOrder);
            values.put(CallLog.Calls.DURATION, callData.Duration);
//            values.put(CallLog.Calls.EXTRA_CALL_TYPE_FILTER, callData.ExtraCallTypeFilter);
            values.put(CallLog.Calls.FEATURES, callData.Features);
            values.put(CallLog.Calls.GEOCODED_LOCATION, callData.GeocodedLocation);
            values.put(CallLog.Calls.IS_READ, callData.IsRead);
            values.put(CallLog.Calls.LAST_MODIFIED, callData.LastModified);
//            values.put(CallLog.Calls.LIMIT_PARAM_KEY, callData.LimitParamKey);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                values.put(CallLog.Calls.LOCATION, callData.Location);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                values.put(CallLog.Calls.MISSED_REASON, callData.MissedReason);
            }
            values.put(CallLog.Calls.NEW, callData.New);
            values.put(CallLog.Calls.NUMBER, callData.Number);
            values.put(CallLog.Calls.NUMBER_PRESENTATION, callData.NumberPresentation);
//            values.put(CallLog.Calls.OFFSET_PARAM_KEY, callData.OffsetParamKey);
            values.put(CallLog.Calls.PHONE_ACCOUNT_COMPONENT_NAME, callData.PhoneAccountComponentName);
            values.put(CallLog.Calls.PHONE_ACCOUNT_ID, callData.PhoneAccountId);
            values.put(CallLog.Calls.POST_DIAL_DIGITS, callData.PostDialDigits);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                values.put(CallLog.Calls.PRIORITY, callData.Priority);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                values.put(CallLog.Calls.SUBJECT, callData.Subject);
            }
            values.put(CallLog.Calls.TRANSCRIPTION, callData.Transcription);
            values.put(CallLog.Calls.TYPE, callData.Type);
            values.put(CallLog.Calls.VIA_NUMBER, callData.ViaNumber);
            values.put(CallLog.Calls.VOICEMAIL_URI, callData.VoicemailUri);
            contentResolver.insert(CallLog.Calls.CONTENT_URI,values);
        }
    }
}
