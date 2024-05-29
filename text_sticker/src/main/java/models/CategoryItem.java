package models;

import android.graphics.drawable.Drawable;

public class CategoryItem {

    private Drawable mCategoryDrawable;
    private String mCategoryName;

    public CategoryItem(Drawable drawable, String categoryName) {
        mCategoryDrawable = drawable;
        mCategoryName = categoryName;
    }

    public Drawable getCategoryIconDrawable() {
        return mCategoryDrawable;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

}
