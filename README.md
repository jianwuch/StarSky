## 带流星的星空动图
### 效果
![image](https://github.com/jianwuch/StarSky/blob/master/pictures/demo.gif?raw=true)
* 两种星星类型（远和近的效果），两种星星大小不一样，移动速度也不一样
* 流星划过的效果
>没有强制背景，这个可以让设计提供，使用场景灵活

## 如何使用/How to
### 1. 在项目build.gradle repositories中使用
Add it in your root build.gradle at the end of repositories:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
### 2. 增加依赖/Add the dependency
```
dependencies {
     implementation 'com.github.jianwuch:StarSky:v1.1.0'
}
```

### 3.XML布局中使用/ Use in layout xml
```xml
  <com.jove.starskylib.StarSkyView
      android:layout_width="match_parent"
      android:layout_height="200dp"
      android:background="@color/colorPrimary"
      android:visibility="visible"
      app:meteor_head_size="3"
      app:one_cycle_time="50000"
      app:star_nums="20" />
```
>注意：background必须要指定/background is needed

### 4.属性介绍
属性 | 解释
---|---
one_cycle_time_ms | 远点星星循环一趟的时间
star_nums | 星星的数量，分为远点星星和近点星星，都是您设置的数量
meteor_head_size | 流星头部火球的大小，不要太大，建议2或4

### 5.代码接口/interface
使用场景：页面切换过程中合理使用以下两个接口，避免页面切换动画造成流星视觉不连续
#### `pauseAnim()`--暂停动画
#### `resumeAnim`--继续动画
#### 接口使用示例
```java
    @Override
    protected void onPause() {
        super.onPause();
        skyView.canclerAnim();
    }

    @Override
    protected void onResume() {
        super.onResume();
        skyView.resumeAnim();
    }
```
