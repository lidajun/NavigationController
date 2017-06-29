[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)

# NavigationController
Android Library imitating iOS NavigationController

![Demo Drawer](https://raw.github.com/lidajun/NavigationController/master/art/test.gif)

Download
--------

Download or grab via Maven:
```xml
<dependency>
  <groupId>com.lurenshuo.android</groupId>
  <artifactId>navigation-controller</artifactId>
  <version>1.0.2</version>
  <type>pom</type>
</dependency>
```
or Gradle:
```groovy
compile 'com.lurenshuo.android:navigation-controller:1.0.2'
```

------------------------------------------------------------------------------
Suitable for use with fragment projects, fragment need extends to NavigationFragment or NavigationFragmentV4;Activity that contains fragment needs to inherit NavigationActivity or NavigationActivityV4.
1. when adding fragment, Need fragmentTransaction.addToBackStack(null); 
2. cannot use fragmentTransaction.setTransition(XXX), otherwise there is no animation of Navigation view 
3. add the switch animation with two parameters, the two parameters are only to do the start of the animation, popBackTack animation by NavigationActivity, transaction.setCustomAnimations(R.animator.fragment_slide_left_enter,R.animator.fragment_slide_left_exit);

If you use toolbar, you need to use NavigationToolbar for example:

```xml
<com.lurenshuo.android.navigationcontroller.widget.NavigationToolbar
    android:id="@+id/toolbar"
    android:layout_width="match_parent"
    android:layout_height="50dp"
    android:background="@color/colorPrimaryDark"/>
```
--------------------------------------------------------------------------------

适合使用Fragment的项目，Fragment需要继承NavigationFragment或NavigationFragmentV4;包含Fragment的Activity需要继承NavigationActivity或NavigationActivityV4
1. 添加fragment时，需要 fragmentTransaction.addToBackStack(null);
2. 不能使用 fragmentTransaction.setTransition(XXX) ,否则没有导航view的动画
3. 添加切换动画时使用两个参数的，这两个参数的是只做开始的动画，popBackTack的动画由NavigationActivity做 transaction.setCustomAnimations(R.animator.fragment_slide_left_enter,R.animator.fragment_slide_left_exit);

如果使用Toolbar，需要使用NavigationToolbar 例如：

```xml
<com.lurenshuo.android.navigationcontroller.widget.NavigationToolbar
    android:id="@+id/toolbar"
    android:layout_width="match_parent"
    android:layout_height="50dp"
    android:background="@color/colorPrimaryDark"/>
```

License
-------

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
