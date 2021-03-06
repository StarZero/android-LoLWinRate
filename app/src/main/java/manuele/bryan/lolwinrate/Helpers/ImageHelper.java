package manuele.bryan.lolwinrate.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.IOError;
import java.io.IOException;

import manuele.bryan.lolwinrate.R;
import manuele.bryan.lolwinrate.UserStatistics.UsersLeagueInfo;

public class ImageHelper {

    public static Drawable getRoundedCornerBitmap(Drawable drawable, int pixels) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return new BitmapDrawable(output);
    }

    public static int getTierIcon(String tier) {
        switch (tier) {
            case UsersLeagueInfo.DIVISION_BRONZE:
                return R.drawable.bronze;
            case UsersLeagueInfo.DIVISION_SILVER:
                return R.drawable.silver;
            case UsersLeagueInfo.DIVISION_GOLD:
                return R.drawable.gold;
            case UsersLeagueInfo.DIVISION_PLAT:
                return R.drawable.plat;
            case UsersLeagueInfo.DIVISION_DIAMOND:
                return R.drawable.diamond;
            case UsersLeagueInfo.DIVISION_MASTERS:
                return R.drawable.silver;
            case UsersLeagueInfo.DIVISION_CHALLENGER:
                return R.drawable.silver;
            default:
                return R.drawable.silver;
        }

    }

    //todo: add challengour and master tier images
    public static Drawable getTierIcon(Context context, String tier) {
        try {
            switch (tier) {
                case UsersLeagueInfo.DIVISION_BRONZE:
                    return Drawable.createFromStream(context.getAssets().open("images/tiers/bronze.png"), null);
                case UsersLeagueInfo.DIVISION_SILVER:
                    return Drawable.createFromStream(context.getAssets().open("images/tiers/silver.png"), null);
                case UsersLeagueInfo.DIVISION_GOLD:
                    return Drawable.createFromStream(context.getAssets().open("images/tiers/gold.png"), null);
                case UsersLeagueInfo.DIVISION_PLAT:
                    return Drawable.createFromStream(context.getAssets().open("images/tiers/plat.png"), null);
                case UsersLeagueInfo.DIVISION_DIAMOND:
                    return Drawable.createFromStream(context.getAssets().open("images/tiers/diamond.png"), null);
                case UsersLeagueInfo.DIVISION_MASTERS:
                    return Drawable.createFromStream(context.getAssets().open("images/tiers/diamond.png"), null);
                case UsersLeagueInfo.DIVISION_CHALLENGER:
                    return Drawable.createFromStream(context.getAssets().open("images/tiers/diamond.png"), null);
                default:
                    return Drawable.createFromStream(context.getAssets().open("images/tiers/diamond.png"), null);
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return null;
    }
}
