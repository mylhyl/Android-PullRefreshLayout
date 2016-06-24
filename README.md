#### Android-PullRefreshLayout
基于`SwipeRefreshLayout`下拉刷新、上拉加载。支持所有的`AbsListView`、`RecycleView`

####特点
 * 在`layout`中使用，支持`AbsListView`所有的xml属性
 * 支持自动下拉刷新，什么用呢？比如进入界面时，只需要调用`autoRefresh()`方法即可，同时下拉刷新回调函数将会被调用。
 * 上拉加载支持自定义`View`或设置加载文字、动画
 * 轻松设置`Adapter`空数据视图，默认为`TextView`支持更文字，也可自定义`View`
 * 对于简单的界面，如只有`ListView`可以继承 [app](pullrefreshlayout/src/main/java/com/mylhyl/prlayout/app)
   包中`Fragment`轻松搞定

####效果图
<img src="preview/gif.gif" width="240px"/>

####使用
  仔细看`listSelector`属性，效果见`sample`
```xml
<com.mylhyl.prlayout.SwipeRefreshListView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/swipeRefresh"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:listSelector="@drawable/selector_list"
    tools:context=".app.ListViewXmlFragment" />
```
 设置上拉加载，更多方法见 [IFooterLayout](pullrefreshlayout/src/main/java/com/mylhyl/prlayout/internal/IFooterLayout.java)
```java
        IFooterLayout footerLayout = swipeRefreshListView.getFooterLayout();
        footerLayout.setFooterText("set自定义加载");
        footerLayout.setIndeterminateDrawable(getResources().getDrawable(R.drawable.footer_progressbar));
```
或
```xml
        pr:footer_text="数据正在加载中"
        pr:footer_indeterminate_drawable="@drawable/footer_progressbar"
```

 自定义上拉加载
 
 * 方式一：注意此方法必须在`setOnListLoadListener`之前调用
 
```java
        getSwipeRefreshLayout().setFooterResource(R.layout.swipe_refresh_footer);
```
 * 方式二：xml属性
 
```xml
        pr:footer_layout="@layout/swipe_refresh_footer"
```
 * 方式三：继承重写`getFooterResource()`方法
 
```java
        public class MySwipeRefreshGridView extends SwipeRefreshGridView {
        
            @Override
            protected int getFooterResource() {
                return R.layout.swipe_refresh_footer;
            }
        }
```
设置adapter空数据视图文字
```java
        swipeRefreshListView.setEmptyText("数据呢？");
```
 自定义adapter空数据视图
```java
        ImageView emptyView = new ImageView(getContext());
        emptyView.setImageResource(R.mipmap.empty);
        swipeRefreshGridView.setEmptyView(emptyView);
```
调用`autoRefresh`自动刷新，那么注册`ListView`长按事件怎么办？好办提供了方法`getScrollView()`取出
又有问题了既然能取到`ListView`那`SwipeRefreshLayout`是不是也可以取到呢？
答案是肯定的，方法`getSwipeRefreshLayout`取出，你可以随心所欲了，设置下拉圆圈的颜色、大小等。
关于更多公开方法见 [ISwipeRefresh](pullrefreshlayout/src/main/java/com/mylhyl/prlayout/internal/ISwipeRefresh.java)

####使用Gradle构建时添加一下依赖即可:
```javascript
compile 'com.mylhyl:pullrefreshlayout:1.2.3'
```
#### 如果使用eclipse[可以点击这里下载jar包](preview/pullrefreshlayout-1.2.3.jar)
但是由于`jar`不能打包`res`原因，将影响`xml`属性的使用，手动拷贝`attrs`到自己的项目中.
也可以`clone`源码，然后在 eclipse 中用`library`方式引用
     
#### [下载APK体验](preview/sample-debug.apk)

### QQ交流群:435173211

#### 更新日志
##### 1.0.0
  * 初始版本

##### 1.1.0
  * 修改类访问权

##### 1.2.0
  * 优化内部业务逻辑

##### 1.2.1
  * 修复嵌套`ViewPage`引起的冲突
  
##### 1.2.2
  * 增加`attrs`属性 可在`xml`中配置`footer`参数

##### 1.2.3
  * 子视图上滑冲突方案由`OnScrollListener`改为重写`canChildScrollUp`

