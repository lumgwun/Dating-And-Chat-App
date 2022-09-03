package com.lahoriagency.cikolive.Classes;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.RequiresApi;

import com.lahoriagency.cikolive.R;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;

@SuppressWarnings("deprecation")
public class UiUtils {
    private static final int RANDOM_COLOR_START_RANGE = 0;
    private static final int RANDOM_COLOR_END_RANGE = 9;

    private static final int COLOR_MAX_VALUE = 255;
    private static final float COLOR_ALPHA = 0.8f;
    private static Map<Integer, Integer> colorsMap = new HashMap<>();

    private static final Random random = new Random();
    private static int previousColor;

    private UiUtils() {
    }
    public static Drawable getColorCircleDrawable(Context context, int colorPosition) {
        return getColoredCircleDrawable(context, getCircleColor(context, colorPosition % RANDOM_COLOR_END_RANGE));
    }

    private static Drawable getColoredCircleDrawable(Context context, @ColorInt int color) {
        GradientDrawable drawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.shape_progressbar_circle);
        drawable.setColor(color);
        return drawable;
    }

    private static int getCircleColor(Context context, @IntRange(from = RANDOM_COLOR_START_RANGE, to = RANDOM_COLOR_END_RANGE)
            int colorPosition) {
        String colorIdName = String.format("random_color_%d", colorPosition + 1);
        int colorId = context.getResources().getIdentifier(colorIdName, "color", context.getPackageName());
        return context.getResources().getColor(colorId);
    }

    public static Drawable getGreyCircleDrawable() {
        return getColoredCircleDrawable(ResourceUtils.getColor(R.color.color_grey));
    }

    public static Drawable getRandomColorCircleDrawable() {
        return getColoredCircleDrawable(getRandomCircleColor());
    }

    public static Drawable getColorCircleDrawable(int colorPosition) {
        return getColoredCircleDrawable(getCircleColor(colorPosition % RANDOM_COLOR_END_RANGE));
    }

    public static Drawable getColoredCircleDrawable(@ColorInt int color) {
        GradientDrawable drawable = (GradientDrawable) ResourceUtils.getDrawable(R.drawable.rect6h);
        drawable.setColor(color);
        return drawable;
    }

    public static int getRandomCircleColor() {
        int randomNumber = random.nextInt(RANDOM_COLOR_END_RANGE) + 1;

        int generatedColor = getCircleColor(randomNumber);
        if (generatedColor != previousColor) {
            previousColor = generatedColor;
            return generatedColor;
        } else {
            do {
                generatedColor = getRandomCircleColor();
            } while (generatedColor != previousColor);
        }
        return previousColor;
    }

    public static int getCircleColor(@IntRange(from = RANDOM_COLOR_START_RANGE, to = RANDOM_COLOR_END_RANGE)
                                             int colorPosition) {
        String colorIdName = String.format("random_color_%d", colorPosition + 1);
        int colorId = App.getInstance().getResources()
                .getIdentifier(colorIdName, "color", App.getInstance().getPackageName());

        return ResourceUtils.getColor(colorId);
    }

    public static int getRandomTextColorById(Integer senderId) {
        if (colorsMap.get(senderId) != null) {
            return colorsMap.get(senderId);
        } else {
            int colorValue = getRandomColor();
            colorsMap.put(senderId, colorValue);
            return colorsMap.get(senderId);
        }
    }

    public static int getRandomColor() {
        float[] hsv = new float[3];
        int color = Color.argb(COLOR_MAX_VALUE, random.nextInt(COLOR_MAX_VALUE), random.nextInt(
                COLOR_MAX_VALUE), random.nextInt(COLOR_MAX_VALUE));
        Color.colorToHSV(color, hsv);
        hsv[2] *= COLOR_ALPHA;
        color = Color.HSVToColor(hsv);
        return color;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getAuthData(String version, String pan, String pin, String expiryDate, String cvv2) throws Exception {
        String authData = "";
        String authDataCipher = version + "Z" + pan + "Z" + pin + "Z" + expiryDate + "Z" + cvv2;
        // The Modulus and Public Exponent will be supplied by Interswitch. please ask for one
        String modulus = "XXXXXXX";
        String publicExponent = "XXXXXXX";
        //Security.addProvider(new BouncyCastleProvider());
        RSAPublicKeySpec publicKeyspec = new RSAPublicKeySpec(new BigInteger(modulus, 16), new BigInteger(publicExponent, 16));
        KeyFactory factory = KeyFactory.getInstance("RSA"); //, "JHBCI");
        PublicKey publicKey = factory.generatePublic(publicKeyspec);
        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] authDataBytes = encryptCipher.doFinal(authDataCipher.getBytes("UTF8"));
        authData = Base64.getEncoder().encodeToString(authDataBytes).replaceAll("\\r|\\n", "");
        return authData;
    }

}
