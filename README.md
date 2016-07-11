# Simple Pull To Refresh for Android

SimplePullToRefresh is an Android library containing PullToRefreshLayout which provides a simple pull-to-refresh function.  
I started this project because other pull-to-refresh libraries I tried didn't work well with the [CoordinatorLayout](https://developer.android.com/reference/android/support/design/widget/CoordinatorLayout.html).

Supports SDK version 14 and above.  
This should work with any views including the [CoordinatorLayout](https://developer.android.com/reference/android/support/design/widget/CoordinatorLayout.html). If there are any problems with certain views please let me know.

## Adding SimplePullToRefresh to your project
### Gradle
working on it.

### Source files
working on it.

## Usage
### Wrap Your View
Just simply wrap any view you want to add the pull-to-refresh function to with [PullToRefreshLayout](https://github.com/skhugh/Android-SimplePullToRefresh/blob/master/simplepulltorefresh/src/main/java/com/skhugh/simplepulltorefresh/PullToRefreshLayout.java) in layout XML as below.

```xml
<com.skhugh.simplepulltorefresh.PullToRefreshLayout
        android:id="@+id/pulltorefresh"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <android.support.v7.widget.RecyclerView
            android:id="@+id/recycler_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent">
        </android.support.v7.widget.RecyclerView>
</com.skhugh.simplepulltorefresh.PullToRefreshLayout>
```

You can also add the view as a child programmatically as so,

```java
PullToRefreshLayout pullToRefreshLayout = new PullToRefreshLayout(getApplicationContext());
// Set layout params for pullToRefreshLayout...
View viewYouWantToHavePullToRefreshFunction = new View(getApplicationContext());
// Set layout params for viewYouWantToHavePullToRefreshFunction...
pullToRefreshLayout.addView(viewYouWantToHavePullToRefreshFunction);
```

**PullToRefreshLayout** automatically adds the pull-to-refresh function to the first view that is added to it as a child view. If PullToRefreshLayout already has a child view, later added views are simply ignored.  

Now we are done with the layout. Next we need to add [PullToRefreshListener](https://github.com/skhugh/Android-SimplePullToRefresh/blob/master/simplepulltorefresh/src/main/java/com/skhugh/simplepulltorefresh/PullToRefreshListener.java) to **PullToRefreshLayout**.

### Set The Interface
[PullToRefreshListener](https://github.com/skhugh/Android-SimplePullToRefresh/blob/master/simplepulltorefresh/src/main/java/com/skhugh/simplepulltorefresh/PullToRefreshListener.java) has **onStartRefresh** method that tells you when you should start refreshing.

```java
PullToRefreshLayout pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pulltorefresh);
pullToRefreshLayout.setPullToRefreshListener(new PullToRefreshListener() {
  // Start refreshing stuff
  @Override
  public void onStartRefresh(@Nullable View view) {
    doSomeTask();
  }
});
```

Now **doSomeTask** method will be executed when the user pulls down the view enough to start refreshing.  
Lastly, we need to tell **PullToRefreshLayout** when the refreshing task is finished.

### Tell PullToRefreshLayout When Refreshing Is Done
Just simply call **PullToRefreshLayout**'s **refreshDone** method.

```java
private class DoSomeTask extends AsyncTask<Void, Void, Void> {
  // Do some refreshing stuff
  ...
  @Override
  protected void onPostExecute(Void result) {
    super.onPostExecute(result);
    pullToRefreshLayout.refreshDone();
  }
}
```

That's it. Now we have a fully functional pull-to-refresh function added to your view.  
If you want some styles changed, please check the [Customization](https://github.com/skhugh/Android-SimplePullToRefresh/#Customization) section.

## Customization
I tried to keep things simple for this project, but there are some customization attributes available.

### XML Attributes
There are some XML attributes you can use to customize **PullToRefreshLayout**. The attributes are,
* **blockScrollWhileRefreshing** - Whether scrolling should be blocked while it is refreshing. Default value is **true**.
* **refreshIconDrawable** - Drawable to be used as the refresh icon. Default is **null**.
* **refreshIconSpinDuration** - Duration for one full spin of the refresh icon in MS. Default value is **800** ms.
* **refreshIconColor** - Color to be used for the refresh icon. This is ignored if **refreshIconDrawable** is set. Default value is **Color.DKGRAY**.
* **refreshIconSize** - Size of the refresh icon. Default value is **24** dp.
* **refreshLayoutBackgroundColor** - Color for the refresh layout background. Default value is **Color.LTGRAY**.
* **refreshLayoutPadding** - Padding for the refresh layout. Default value is **4** dp.
* **refreshLayoutMaxHeight** - Max height of the refresh layout. Default value is **500** dp.
* **refreshLayoutThresholdHeight** - Threshold height of the refresh layout to start refreshing in DP. Default value is **300** dp.

You can also check [attrs.xml](https://github.com/skhugh/Android-SimplePullToRefresh/blob/master/simplepulltorefresh/src/main/res/values/attrs.xml).

### JAVA methods
All XML attributes have its corresponding Setters. You can call these methods programmatically at any time.
* **setBlockScrollWhileRefreshing**(boolean blockScrollWhileRefreshing)
* **setRefreshIconDrawable**(@Nullable Drawable refreshIconDrawable)
* **setRefreshIconSpinDuration**(int refreshIconSpinDuration)
* **setRefreshIconColor**(int refreshIconColor)
* **setRefreshIconSize**(int refreshIconSizeInPx)
* **setRefreshLayoutBackgroundColor**(int refreshLayoutBackgroundColor)
* **setRefreshLayoutPadding**(int refreshLayoutPadding)
* **setRefreshLayoutMaxHeight**(int refreshLayoutThresholdHeightInPx)
* **setRefreshLayoutThresholdHeight**(int refreshLayoutThresholdHeightInPx)

You can also check [PullToRefreshStyler](https://github.com/skhugh/Android-SimplePullToRefresh/blob/master/simplepulltorefresh/src/main/java/com/skhugh/simplepulltorefresh/PullToRefreshStyler.java) interface.

## Advanced Customization
working on it.
