package interfaces;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

public interface StickerOperation {
    void setStickerFont(Typeface typeface);
    void setStickerText(String text);
    void setStickerTextColor(String color);
    void setStickerTextAlpha(int opacity);
    void setStickerBackgroundColor(String color, int opacity);
    void setStickerGradientColor(String startColor, String endColor);
    void setStickerBorder(String color);
    void setBorderSize(int borderSize);
}
