package ly.warp.jitpublishtestlib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PublicUtils {

    public static boolean hasHoneyComb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasIceCreamSandwich() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasIceCreamSandwichMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasJellyBeanMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasLollipopMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean hasNougat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    @SuppressWarnings("deprecation")
    public static int getDisplayHeight(Context context) {

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            wm.getDefaultDisplay().getSize(size);
            return size.y;
        } else {
            return display.getHeight();
        }
    }

    @SuppressWarnings("deprecation")
    public static int getDisplayWidth(Context context) {

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            wm.getDefaultDisplay().getSize(size);
            return size.x;
        } else {
            return display.getWidth();
        }
    }

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            if (hasLollipop()) {
                Network[] nets = connectivity.getAllNetworks();
                for (Network net : nets) {
                    NetworkInfo info = connectivity.getNetworkInfo(net);
                    if (info != null && info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo i : info) {
                        if (i.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void hideKeyboard(Activity activity) {

        if (activity != null && activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getWindowToken(), 0);
        }
    }

    public static boolean isAppInstalled(Context context, String packageName) {

        boolean installed;
        try {
            context.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    /**
     * Recursive helper, which will clear up app's memory from view's
     * backgrounds. <br/>
     * CONTRACT: a good way to call function at onDestroy() or fragment's
     * onDestroyView().
     *
     * @param view The root view of the layout.
     */
    public static void unbindDrawables(View view) {
        if (view != null) {
            if (view.getBackground() != null) {
                view.getBackground().setCallback(null);
            }
            if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    unbindDrawables(((ViewGroup) view).getChildAt(i));
                }
                ((ViewGroup) view).removeAllViews();
            }
        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        setListViewHeightBasedOnChildren(listView, 0);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView, int minHeight) {
        if (listView == null)
            return;

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem != null) {
                if (listItem.getLayoutParams() == null) {
                    listItem.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, 0));
                }
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();
            }
        }

        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        if (height < minHeight)
            height = minHeight;

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static void setGridViewHeight(GridView gridView, int height) {
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = height;
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }

    public static int uintBit() {
        return Byte.SIZE * Integer.SIZE / Byte.SIZE;
    }

    public static int uintRotate(int val, int howmuch) {
        return ((val << howmuch) | (val >> (uintBit() - howmuch)));
    }

    public static boolean equalObjects(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    public static boolean equalBundles(Bundle one, Bundle two) {

        if ((one == null && two != null)
                || (one != null && two == null))
            return false;

        if (one == null && two == null)
            return true;

        if (one.size() != two.size())
            return false;

        Set<String> setOne = one.keySet();
        Object valueOne;
        Object valueTwo;

        for (String key : setOne) {
            valueOne = one.get(key);
            valueTwo = two.get(key);
            if (valueOne instanceof Bundle && valueTwo instanceof Bundle &&
                    !equalBundles((Bundle) valueOne, (Bundle) valueTwo)) {
                return false;
            } else if (valueOne == null) {
                if (valueTwo != null || !two.containsKey(key))
                    return false;
            } else if (!valueOne.equals(valueTwo))
                return false;
        }

        return true;
    }

    public static int compareInt(int a, int b) {
        return (a < b ? -1 : (a == b ? 0 : 1));
    }

    public static String md5(final String key) {
        if (key != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] array = md.digest(key.getBytes());
                StringBuilder sb = new StringBuilder();
                for (byte anArray : array) {
                    sb.append(Integer.toHexString((anArray & 0xFF) | 0x100)
                            .substring(1, 3));
                }
                return sb.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String sha256(final String key) {

        String signature = "";
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(key.getBytes("UTF-8"));
            signature = //Base64.encodeToString(hash, Base64.NO_WRAP);
                    String.format("%0" + (hash.length * 2) + 'x', new BigInteger(1, hash));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return signature;
    }

    public static long timeParse(String timeFormatted) {
        long result = timeParse(timeFormatted, "yyyy-MM-dd' 'HH:mm:ss");
        if (result == -1) {
            result = timeParse(timeFormatted, "yyyy-MM-dd'T'HH:mm:ss.ss'Z'");
        }
        return result;
    }

    /**
     * parsing the string time based on current timezone (+/- current zone offset)
     * (converting to utc timestamp)
     */
    public static long timeParse(String timeFormatted, String format) {

        long time = -1;
        if (!TextUtils.isEmpty(timeFormatted)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
                time = sdf.parse(timeFormatted).getTime();
            } catch (ParseException e) {
                if (BuildConfig.DEBUG)
                    e.printStackTrace();
            }
        }
        return time;
    }

    public static String timeFormat(Context appContext, long time) {

        String format = DateFormat.is24HourFormat(appContext) ? "HH:mm" : "h:mm aa";
        return timeFormat(time, format);
    }

    /**
     * formatting the long time in the current device time zone
     * (format long utc and +/- current zone offset)
     */
    public static String timeFormat(long time, String format) {

        String timeFormatted = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format,
                    Locale.getDefault());
            timeFormatted = sdf.format(new Date(time));
        } catch (NullPointerException e) {
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
        return timeFormatted;
    }

    public static long getCurrentTimeForTimeZone(Context ctx, String timeZoneId) {

        Calendar c = Calendar.getInstance();

		/*	Log.i("time formating",
                    "local: "
							+ timeFormat(ctx, c.getTimeInMillis(),
									Constants.DATE_TIME_FORMAT));
		*/

        // get gmt time
        TimeZone z = c.getTimeZone();
        int offset = z.getRawOffset();
        if (z.inDaylightTime(new Date())) {
            offset = offset + z.getDSTSavings();
        }
        c.add(Calendar.MILLISECOND, (-offset));
        long gmtTime = c.getTimeInMillis();

		/*	Log.i("time formating",
                    "gmt: " + timeFormat(ctx, gmtTime, Constants.DATE_TIME_FORMAT));*/

        // add offset of timezone to gmt
        TimeZone newTimeZone = TimeZone.getTimeZone(timeZoneId);
        offset = newTimeZone.getRawOffset();
        if (newTimeZone.inDaylightTime(new Date())) {
            offset = offset + newTimeZone.getDSTSavings();
        }
        long newTime = gmtTime + offset;

		/*newTime = newTime != gmtTime ? newTime : Calendar.getInstance()
                .getTimeInMillis();*/

		/*Log.i("time formating",
                "newTime: "
						+ timeFormat(ctx, newTime, Constants.DATE_TIME_FORMAT));*/

        return newTime;
    }

    public static double random(double min, double max) {
        if (min > max) {
            return new Random().nextDouble() * (min - max) + max;
        }
        return new Random().nextDouble() * (max - min) + min;
    }

    public static int random(int min, int max) {
        if (min > max) {
            return new Random().nextInt((min - max) + 1) + max;
        }
        return new Random().nextInt((max - min) + 1) + min;
    }

    public static boolean isEmailValid(String text) {
        return !TextUtils.isEmpty(text)
                && android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }

    public static boolean isMobileTelephoneValid(String mobileTel) {
        boolean result = false;

        if (TextUtils.isEmpty(mobileTel))
            return result;

        Pattern pattern = Pattern.compile("[6]{1}[9]{1}[0-9]{8}");
        if (pattern.matcher(mobileTel).matches())
            result = true;

        return result;
    }

    public static String getMatchValue(String input, String pattern) {

        String result = null;
        if (!TextUtils.isEmpty(input) && !TextUtils.isEmpty(pattern)) {
            Matcher matcher = Pattern.compile(pattern).matcher(input);
            result = matcher.find() ? matcher.group(1) : null;
        }
        return result;
    }

    public static void setErrorIndicator(final TextView tv, final int textColor, final int bgrRes) {

        if (tv == null)
            return;

        tv.post(new Runnable() {
            @Override
            public void run() {

                if (bgrRes > 0) {
                    tv.setBackgroundResource(bgrRes);
                }
                if (textColor != -1) {
                    tv.setTextColor(textColor);
                    tv.setHintTextColor(textColor);
                }
            }
        });
    }

    public static void setErrorIndicator(final TextView tv, final int drwId) {

        if (tv == null)
            return;

        tv.post(new Runnable() {
            @Override
            public void run() {
                tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, drwId, 0);
            }
        });
    }

    @NonNull
    public static <T> List<T> parseJsonToList(JSONObject json, String arrayName, Class<T> targetClass) {

        JSONArray jArray = null;
        if (json != null) {
            jArray = json.optJSONArray(arrayName);
        }
        return parseJsonToList(jArray, targetClass);
    }

    @NonNull
    public static <T> List<T> parseJsonToList(JSONArray json, Class<T> targetClass) {

        if (json == null)
            return new ArrayList<>(0);

        List<T> values = new ArrayList<>(json.length());

        T model;
        for (int i = 0; i < json.length(); ++i) {
            try {
                model = targetClass.getConstructor(JSONObject.class)
                        .newInstance(json.optJSONObject(i));
                values.add(model);
            } catch (Exception e) {
                try {
                    values.add((T) json.opt(i));
                } catch (Exception e1) {
                    if (BuildConfig.DEBUG)
                        e1.printStackTrace();
                }
            }
        }
        return values;
    }

    @NonNull
    public static <T> List<T> parseCursorToList(Cursor cursor, Class<T> targetClass) {

        if (cursor == null)
            return new ArrayList<>(0);

        ArrayList<T> models = new ArrayList<>(cursor.getCount());
        T model;
        while (cursor.moveToNext()) {
            try {
                model = targetClass.getConstructor(Cursor.class)
                        .newInstance(cursor);
                models.add(model);
            } catch (Exception e) {
                if (BuildConfig.DEBUG)
                    e.printStackTrace();
            }
        }
        cursor.close();
        return models;
    }

    public static Map<String, String> splitUrlParams(String url) {

        Map<String, String> queryPairs = new LinkedHashMap<String, String>();

        if (!TextUtils.isEmpty(url)) {
            Uri uri = Uri.parse(url);
            String query = uri.getQuery();
            String[] pairs = query.split("&");
            for (String pair : pairs) {

                if (TextUtils.isEmpty(pair))
                    continue;

                int idx = pair.indexOf("=");
                if (idx != -1) {
                    try {
                        queryPairs.put(URLDecoder.decode(
                                pair.substring(0, idx), "UTF-8"), URLDecoder
                                .decode(pair.substring(idx + 1), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return queryPairs;
    }

    public static void dialNumber(Context context, String number) {

        if (!TextUtils.isEmpty(number)) {

            try {
                context.startActivity(new Intent(Intent.ACTION_DIAL).
                        setData(Uri.parse("tel:" + number)));
            } catch (ActivityNotFoundException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void openWebPage(Context context, String url) {

        Uri webPage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static void sendEmail(Context context, String subject, String[] recipients,
                                 String[] bccAddresses, String body) {

        if (!TextUtils.isEmpty(subject) ||
                !TextUtils.isEmpty(body) || (recipients != null && recipients.length > 0))
            try {
                Intent emailIntent = new Intent(
                        Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
                emailIntent.putExtra(Intent.EXTRA_BCC, bccAddresses);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, body);
                emailIntent.setType("message/rfc822");
                context.startActivity(Intent.createChooser(emailIntent, null));

            } catch (ActivityNotFoundException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
    }

    public static View getListItemViewByPosition(ListView listView, int pos) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        View itemView = null;
        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public static String getLineSeparator() {
        return System.getProperty("line.separator");
    }

    public static void animateVisibility(final View view, final int visibility) {

        if (view != null) {

            switch (visibility) {

                case View.VISIBLE:
                    if (view.getVisibility() != View.VISIBLE) {
                        view.setAlpha(0f);
                        view.animate()
                                .alpha(1f)
                                .setDuration(view.getResources().getInteger(
                                        android.R.integer.config_shortAnimTime))
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        view.setVisibility(visibility);
                                    }
                                });
                    }
                    break;

                case View.INVISIBLE:
                case View.GONE:

                    if (view.getVisibility() == View.VISIBLE) {
                        view.setAlpha(1f);
                        view.animate()
                                .alpha(0f)
                                .setDuration(view.getResources().getInteger(
                                        android.R.integer.config_shortAnimTime))
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        view.setVisibility(visibility);
                                    }
                                });
                    }
                    break;

                default:
                    break;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap getBlurBmp(Context context, Bitmap bmpToBlur, float radius) {

        if (bmpToBlur != null) {
            RenderScript rs = RenderScript.create(context);
            Allocation input = Allocation.createFromBitmap(rs, bmpToBlur,
                    Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius);
            script.setInput(input);
            script.forEach(input);
            input.copyTo(bmpToBlur);
            rs.destroy();
        }
        return bmpToBlur;
    }

    @Nullable
    public static Bitmap buildDrawingCache(@NonNull View view, int scaleFactor) {


        if (scaleFactor <= 0) {
            scaleFactor = 1;
        }

        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);

        Bitmap result;
        Bitmap drawingCache = result = view.getDrawingCache(true);
        if (drawingCache != null && scaleFactor > 1) {
            result = Bitmap.createScaledBitmap(drawingCache,
                    (drawingCache.getWidth() / scaleFactor),
                    (drawingCache.getHeight() / scaleFactor), false);
            drawingCache.recycle();
        }

        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);

        return result;
    }

    public static void throwIfMainThread() throws RuntimeException {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            throw new RuntimeException("must be called not on the UI thread");
        }
    }

    public static boolean isTimeUp(long timeStamp, long timeInterval) {
        return System.currentTimeMillis() - timeStamp > timeInterval;
    }
}
