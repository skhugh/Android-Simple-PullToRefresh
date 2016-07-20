# Simple Pull To Refresh for Android

**SimplePullToRefresh** is an Android library containing **PullToRefreshLayout** which provides a simple pull-to-refresh function.  
I started this project because other pull-to-refresh libraries I tried didn't work well with the [CoordinatorLayout](https://developer.android.com/reference/android/support/design/widget/CoordinatorLayout.html).

Supports SDK version 14 and above.  

This should work with any views including the [CoordinatorLayout](https://developer.android.com/reference/android/support/design/widget/CoordinatorLayout.html). If there are any problems with certain views please let me know.


## Samples
You can try the sample app from Google Play:  

[![Get it on Google Play](https://developer.android.com/images/brand/en_generic_rgb_wo_45.png)](https://play.google.com/store/apps/details?id=com.skhugh.simplepulltorefresh.samples)

Or see the sample source code [here](https://github.com/skhugh/Android-Simple-PullToRefresh/tree/master/app/src/main/java/com/skhugh/simplepulltorefresh/samples).

### Screenshots

**Sample Using RecyclerView With Default Refresh Icon**  

![Screenshot](https://github.com/skhugh/Android-Simple-PullToRefresh/blob/master/screencapture/sample-recyclerview.gif?raw=true)  

**Sample Using CoordinatorLayout With Custom Image As Refresh Icon**  

![Screenshot](https://github.com/skhugh/Android-Simple-PullToRefresh/blob/master/screencapture/sample-coordinatorlayout.gif?raw=true)  

**Sample Using ImageView & WebView With Custom Refresh Icon**  

![Screenshot](https://github.com/skhugh/Android-Simple-PullToRefresh/blob/master/screencapture/sample-imageview_webview.gif?raw=true)  

## Download
### Gradle
Make sure you have **jcenter** added to your **project**'s *build.gradle* file.

```
allprojects {
  repositories {
    jcenter()
  }
}
```

Next, add the dependency to your **app**'s *build.gradle* file.

```
dependencies {
  compile 'com.skhugh.simplepulltorefresh:simplepulltorefresh:1.0.2'
}
```

### Source Files
If you want, you can download the source code and add [simplepulltorefresh](https://github.com/skhugh/Android-Simple-PullToRefresh/tree/master/simplepulltorefresh) module to your project.


## Usage
### Wrap Your View
Just simply wrap any view you want to add the pull-to-refresh function to with [PullToRefreshLayout](https://github.com/skhugh/Android-Simple-PullToRefresh/blob/master/simplepulltorefresh/src/main/java/com/skhugh/simplepulltorefresh/PullToRefreshLayout.java) in layout XML as below.

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

Now we are done with the layout. Next, we need to add [PullToRefreshListener](https://github.com/skhugh/Android-Simple-PullToRefresh/blob/master/simplepulltorefresh/src/main/java/com/skhugh/simplepulltorefresh/PullToRefreshListener.java) to **PullToRefreshLayout**.

### Set The Interface
[PullToRefreshListener](https://github.com/skhugh/Android-Simple-PullToRefresh/blob/master/simplepulltorefresh/src/main/java/com/skhugh/simplepulltorefresh/PullToRefreshListener.java) has **onStartRefresh** method that tells you when you should start refreshing.

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
If you want some styles changed, please check the [Customization](https://github.com/skhugh/Android-Simple-PullToRefresh#customization) seciton.


## Customization
I tried to keep things simple for this project, but there are still some customization options available.  
You change color, size, speed, etc or use your own image for the refresh icon.

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

You can also check [attrs.xml](https://github.com/skhugh/Android-Simple-PullToRefresh/blob/master/simplepulltorefresh/src/main/res/values/attrs.xml) for the list of attributes, and example of using these attributes at [activity_list.xml](https://github.com/skhugh/Android-Simple-PullToRefresh/blob/master/app/src/main/res/layout/activity_list.xml).

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

You can also check [PullToRefreshStyler](https://github.com/skhugh/Android-Simple-PullToRefresh/blob/master/simplepulltorefresh/src/main/java/com/skhugh/simplepulltorefresh/PullToRefreshStyler.java) interface for the list of methods, and example of usage at [ScrollingActivity.java](https://github.com/skhugh/Android-Simple-PullToRefresh/blob/master/app/src/main/java/com/skhugh/simplepulltorefresh/samples/ScrollingActivity.java).


## Advanced Customization
You can use your own view for the refresh icon.  
Just simply pass the implementation of [RefreshIcon](https://github.com/skhugh/Android-Simple-PullToRefresh/blob/master/simplepulltorefresh/src/main/java/com/skhugh/simplepulltorefresh/refreshicon/RefreshIcon.java) to [PullToRefreshLayout](https://github.com/skhugh/Android-Simple-PullToRefresh/blob/master/simplepulltorefresh/src/main/java/com/skhugh/simplepulltorefresh/PullToRefreshLayout.java)'s **setRefreshIcon** method.  
**RefreshIcon** interface has a **getIconView** method which returns a **View** type.   
Here, you can return your own view to be used as the refresh icon.  
Also, there are methods you can implement to define the refresh icon's behavior and styles.

A simple example of using a TextView for the refresh icon is shown below.

```java
private class CustomRefreshIcon implements RefreshIcon {
  boolean isSpinning = false;
  TextView textView;

  CustomRefreshIcon() {
      textView = new TextView(MultiplePullToRefreshActivity.this);
      RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
              ViewGroup.LayoutParams.WRAP_CONTENT,
              ViewGroup.LayoutParams.WRAP_CONTENT);
      layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
      textView.setBackgroundColor(Color.TRANSPARENT);
      textView.setLayoutParams(layoutParams);
  }

  @NonNull
  @Override
  public View getIconView() {
      return textView;
  }

  @Override
  public void setProgress(float progress) {
      isSpinning = false;
      textView.setText((int) (progress * 100) + "%");
  }

  @Override
  public void spin() {
      isSpinning = true;
      textView.setText("Loading...");
  }

  @Override
  public void setSpinSpeed(int spinSpeed) {
  }

  @Override
  public boolean isSpinning() {
      return isSpinning;
  }
}

...

pullToRefreshLayout.setRefreshIcon(new CustomRefreshIcon());

```

Or look at [MultiplePullToRefreshActivity.java](https://github.com/skhugh/Android-Simple-PullToRefresh/blob/master/app/src/main/java/com/skhugh/simplepulltorefresh/samples/MultiplePullToRefreshActivity.java) file.


## Acknowledgement
This project uses [Material-ish Progress](https://github.com/pnikosis/materialish-progress) for the refresh icon.


## License
```
The MIT License (MIT)

Copyright (c) 2016 Hyowoo Kim

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
