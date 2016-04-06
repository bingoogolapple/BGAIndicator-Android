:running:BGAIndicator-Android:running:
============

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/cn.bingoogolapple/bga-indicator/badge.svg)](https://maven-badges.herokuapp.com/maven-central/cn.bingoogolapple/bga-indicator)

支持Indicator和ViewPager宽度不相等时也能正确运行的滑动指示器

### 效果图
![Image of BGAFixedIndicatorDemo](http://bingoshare.u.qiniudn.com/BGAFixedIndicatorDemo.gif)

### Gradle依赖

```groovy
dependencies {
    compile 'com.android.support:support-v4:latestVersion'
    compile 'cn.bingoogolapple:bga-indicator:latestVersion@aar'
}
```

### 自定义属性说明

```xml
<declare-styleable name="BGAIndicator">
    <attr name="indicator_textColor" format="color|reference" />
    <attr name="indicator_textSizeNormal" format="dimension|reference" />
    <attr name="indicator_textSizeSelected" format="dimension|reference" />
    <attr name="indicator_triangleHorizontalMargin" format="dimension|reference" />
    <attr name="indicator_triangleColor" format="color|reference" />
    <attr name="indicator_triangleHeight" format="dimension|reference" />
    <attr name="indicator_hasDivider" format="boolean" />
    <attr name="indicator_dividerColor" format="color|reference" />
    <attr name="indicator_dividerWidth" format="dimension|reference" />
    <attr name="indicator_dividerVerticalMargin" format="dimension|reference" />
</declare-styleable>
```

### 详细用法请查看[demo](https://github.com/bingoogolapple/BGAIndicator-Android/tree/master/demo):feet:

### 关于我

| 新浪微博 | 个人主页 | 邮箱 | BGA系列开源库QQ群 |
| ------------ | ------------- | ------------ | ------------ |
| <a href="http://weibo.com/bingoogol" target="_blank">bingoogolapple</a> | <a  href="http://www.bingoogolapple.cn" target="_blank">bingoogolapple.cn</a>  | <a href="mailto:bingoogolapple@gmail.com" target="_blank">bingoogolapple@gmail.com</a> | ![BGA_CODE_CLUB](http://7xk9dj.com1.z0.glb.clouddn.com/BGA_CODE_CLUB.png?imageView2/2/w/200) |

## License

    Copyright 2015 bingoogolapple

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
