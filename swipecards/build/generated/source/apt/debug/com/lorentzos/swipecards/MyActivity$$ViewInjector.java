// Generated code from Butter Knife. Do not modify!
package com.lorentzos.swipecards;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MyActivity$$ViewInjector {
  public static void inject(Finder finder, final com.lorentzos.swipecards.MyActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165184, "field 'flingContainer'");
    target.flingContainer = (com.lorentzos.flingswipe.SwipeFlingAdapterView) view;
    view = finder.findRequiredView(source, 2131165186, "method 'right'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.right();
        }
      });
    view = finder.findRequiredView(source, 2131165185, "method 'left'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.left();
        }
      });
  }

  public static void reset(com.lorentzos.swipecards.MyActivity target) {
    target.flingContainer = null;
  }
}
