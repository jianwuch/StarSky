## 带流星的星空动图
* 星星分远，近之分，移动速度不一样
* 每隔间3s有流星划过的效果
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
        android:layout_width="368dp"
        android:layout_height="200dp"/>
```
