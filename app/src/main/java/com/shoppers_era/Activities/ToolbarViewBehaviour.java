package com.shoppers_era.Activities;


import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ToolbarViewBehaviour extends CoordinatorLayout.Behavior<Toolbar>  {

    private int height;

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, Toolbar child, int layoutDirection) {
        height = child.getHeight();
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, Toolbar child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, Toolbar child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        if (dyConsumed >0 && child.getVisibility() == View.VISIBLE){
            slideDown(child);
        } else if (dyConsumed < 0) {
            slideUp(child);
        }

    }

    private void slideUp(Toolbar child) {
        child.clearAnimation();
        child.animate().translationY(0).setDuration(200);
    }

    private void slideDown(Toolbar child) {
        child.clearAnimation();
        child.animate().translationY(height).setDuration(200);
    }
}