/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ak.android.akplaza.qrcode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

import com.ak.android.akplaza.R;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;

/**
 * A base class for the Android-specific barcode handlers. These allow the app
 * to polymorphically suggest the appropriate actions for each data type. This
 * class also contains a bunch of utility methods to take common actions like
 * opening a URL. They could easily be moved into a helper object, but it can't
 * be static because the Activity instance is needed to launch an intent.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public abstract class ResultHandler {

    static final String TAG = ResultHandler.class.getSimpleName();

    private static final DateFormat DATE_FORMAT;
    static {
        DATE_FORMAT = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        // For dates without a time, for purposes of interacting with Android,
        // the resulting timestamp
        // needs to be midnight of that day in GMT. See:
        // http://code.google.com/p/android/issues/detail?id=8330
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public static final int MAX_BUTTON_COUNT = 4;
    public static final String KEY_CUSTOM_PRODUCT_SEARCH = "preferences_custom_product_search";

    private final ParsedResult result;
    private final Activity activity;
    private final Result rawResult;
    private final String customProductSearch;

    ResultHandler(Activity activity, ParsedResult result) {
        this(activity, result, null);
    }

    ResultHandler(Activity activity, ParsedResult result, Result rawResult) {
        this.result = result;
        this.activity = activity;
        this.rawResult = rawResult;
        this.customProductSearch = parseCustomSearchURL();

        // Make sure the Shopper button is hidden by default. Without this,
        // scanning a product followed
        // by a QR Code would leave the button on screen among the QR Code
        // actions.
        View shopperButton = activity.findViewById(R.id.shopper_button);
        shopperButton.setVisibility(View.GONE);
    }

    public ParsedResult getResult() {
        return result;
    }

    boolean hasCustomProductSearch() {
        return customProductSearch != null;
    }

    /**
     * Indicates how many buttons the derived class wants shown.
     * 
     * @return The integer button count.
     */
    public abstract int getButtonCount();

    /**
     * The text of the nth action button.
     * 
     * @param index From 0 to getButtonCount() - 1
     * @return The button text as a resource ID
     */
    public abstract int getButtonText(int index);

    /**
     * Execute the action which corresponds to the nth button.
     * 
     * @param index The button that was clicked.
     */
    public abstract void handleButtonPress(int index);

    /**
     * Some barcode contents are considered secure, and should not be saved to
     * history, copied to the clipboard, or otherwise persisted.
     * 
     * @return If true, do not create any permanent record of these contents.
     */
    public boolean areContentsSecure() {
        return false;
    }

    /**
     * The Google Shopper button is special and is not handled by the abstract
     * button methods above.
     * 
     * @param listener The on click listener to install for this button.
     */
    protected void showGoogleShopperButton(View.OnClickListener listener) {
        View shopperButton = activity.findViewById(R.id.shopper_button);
        shopperButton.setVisibility(View.VISIBLE);
        shopperButton.setOnClickListener(listener);
    }

    /**
     * Create a possibly styled string for the contents of the current barcode.
     * 
     * @return The text to be displayed.
     */
    public CharSequence getDisplayContents() {
        String contents = result.getDisplayResult();
        return contents.replace("\r", "");
    }

    /**
     * A string describing the kind of barcode that was found, e.g.
     * "Found contact info".
     * 
     * @return The resource ID of the string.
     */
    public abstract int getDisplayTitle();

    /**
     * A convenience method to get the parsed type. Should not be overridden.
     * 
     * @return The parsed type, e.g. URI or ISBN
     */
    public final ParsedResultType getType() {
        return result.getType();
    }

    private String parseCustomSearchURL() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String customProductSearch = prefs.getString(KEY_CUSTOM_PRODUCT_SEARCH,
                null);
        if (customProductSearch != null && customProductSearch.trim().length() == 0) {
            return null;
        }
        return customProductSearch;
    }

    String fillInCustomSearchURL(String text) {
        String url = customProductSearch.replace("%s", text);
        if (rawResult != null) {
            url = url.replace("%f", rawResult.getBarcodeFormat().toString());
        }
        return url;
    }

}
