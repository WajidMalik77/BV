package com.xiaopo.flying.sticker;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.bumptech.glide.load.resource.gif.GifDrawable;

/**
 * @author wupanjie
 */
public class DrawableStickerNew extends StickerNew {

  private GifDrawable drawable;
  private Rect realBounds;

  public DrawableStickerNew(GifDrawable drawable) {
    this.drawable = drawable;
    realBounds = new Rect(0, 0, getWidth(), getHeight());
  }

  @NonNull
  @Override public Drawable getDrawable() {
    return drawable;
  }

  @Override public DrawableStickerNew setDrawable(@NonNull GifDrawable drawable) {
    this.drawable = drawable;
    return this;
  }

  @Override public void draw(@NonNull Canvas canvas) {
    canvas.save();
    canvas.concat(getMatrix());
    drawable.setBounds(realBounds);
    drawable.draw(canvas);
    canvas.restore();
  }

  @NonNull @Override public DrawableStickerNew setAlpha(@IntRange(from = 0, to = 255) int alpha) {
    drawable.setAlpha(alpha);
    return this;
  }

  @Override public int getWidth() {
    return drawable.getIntrinsicWidth();
  }

  @Override public int getHeight() {
    return drawable.getIntrinsicHeight();
  }

  @Override
  public StickerNew setDrawable(@NonNull Drawable drawable) {
    return null;
  }


  @Override public void release() {
    super.release();
    if (drawable != null) {
      drawable = null;
    }
  }
}
