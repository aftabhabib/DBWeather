package com.dbeginc.dbweather.models.provider.geoname;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.dbeginc.dbweather.DBWeatherApplication;
import com.dbeginc.dbweather.models.datatypes.geonames.GeoName;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Darel Bitsy on 04/04/17.
 * Location Suggestion provider
 */

public class LocationSuggestionProvider extends ContentProvider {
    @Inject
    GeoNameLocationInfoProvider mGeoNameLocationInfoProvider;
    @Inject
    List<GeoName> mListOfLocation;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull final Uri uri,
                        @Nullable final String[] projection,
                        @Nullable final String selection,
                        @Nullable final String[] selectionArgs,
                        @Nullable final String sortOrder) {

        if (mGeoNameLocationInfoProvider == null) { injectInstance(); }

        final String userQuery = uri.getLastPathSegment();

        final MatrixCursor matrixCursor = new MatrixCursor(new String[] {
                BaseColumns._ID,
                SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_TEXT_2
        }, mListOfLocation.size());

        if (userQuery != null && !userQuery.isEmpty()) {
            mCompositeDisposable.add(
                    mGeoNameLocationInfoProvider
                            .getLocation(userQuery)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<List<GeoName>>() {
                                @Override
                                public void onSuccess(@NonNull final List<GeoName> geoNames) {
                                    mListOfLocation.clear();
                                    mListOfLocation.addAll(geoNames);
                                }

                                @Override
                                public void onError(@NonNull final Throwable throwable) { Crashlytics.logException(throwable); }
                            }));

            final int listOfLocationSize = mListOfLocation.size();

            for (int index = 0; index < listOfLocationSize; index ++) {
                final GeoName location = mListOfLocation.get(index);
                matrixCursor.addRow(new Object[] {index, location.getName(), location.getCountryName()});
            }
        }

        return matrixCursor;
    }

    private void injectInstance() {
        DBWeatherApplication.getComponent().inject(this);
    }

    @Nullable
    @Override
    public String getType(@NonNull final Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull final Uri uri, @Nullable final ContentValues values) {

        if (mGeoNameLocationInfoProvider == null) { injectInstance(); }
        return null;
    }

    @Override
    public int delete(@NonNull final Uri uri,
                      @Nullable final String selection,
                      @Nullable final String[] selectionArgs) {

        if (mGeoNameLocationInfoProvider == null) { injectInstance(); }
        return 0;
    }

    @Override
    public int update(@NonNull final Uri uri,
                      @Nullable final ContentValues values,
                      @Nullable final String selection,
                      @Nullable final String[] selectionArgs) {

        if (mGeoNameLocationInfoProvider == null) { injectInstance(); }
        return 0;
    }
}