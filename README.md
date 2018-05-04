## 带流星的星空动图
### 效果
！[](./pictures/demo.gif)
* 星星分远，近之分，移动速度不一样
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
    compile 'com.github.jianwuch:StarSky:v1.0.1'
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

### 属性介绍
属性 | 解释
---|---
one_cycle_time_ms | 远点星星循环一趟的时间
star_nums | 星星的数量，分为远点星星和近点星星，都是您设置的数量
meteor_head_size | 流星头部火球的大小，不要太大，建议2/4

