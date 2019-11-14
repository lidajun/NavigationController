[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)

# NavigationController
Android Library imitating iOS NavigationController

![Demo Drawer](https://raw.github.com/lidajun/NavigationController/master/art/test.gif)

Download
--------

Download or grab via Maven:
```xml
<dependency>
  <groupId>com.github.lidajun.android</groupId>
  <artifactId>navigation-controller</artifactId>
  <version>2.0.0</version>
  <type>pom</type>
</dependency>
```
or Gradle:
```groovy
repositories {
    jcenter()
}

compile 'com.github.lidajun.android:navigation-controller:2.0.0'
```

------------------------------------------------------------------------------
Suitable for use with fragment projects, fragment need extends to NavigationFragment;Activity that contains fragment needs to inherit NavigationActivity.

If using Toolbar, you need to use NavigationToolbar. Default will increase the title of a switching animation. Please ignore if not used
```xml
<com.github.lidajun.android.navigationcontroller.widget.NavigationToolbar
    android:id="@+id/toolbar"
    android:layout_width="match_parent"
    android:layout_height="50dp"/>
```
  Toolbar Optional attributes
```
<attr name="title_view_text_size" format="float"/>
<attr name="back_view_text_size" format="float"/>
<attr name="title_view_text_color" format="color"/>
<attr name="back_view_text_color" format="color"/>
<attr name="title_view_text" format="string"/>
<attr name="back_view_text" format="string"/>
```
  You can add a title to your toolbar in the fragment
```
setToolbarTitle("FragmentA");
```
Let your activity inherit NavigationActivity
    The difference is that you are not using the v4 package fragment
    If you use the toolbar to return your toolbar in initNavigationToolbar, return null if not used;
```
@Override
protected NavigationToolbar initNavigationToolbar() {
   NavigationToolbar toolbar = (NavigationToolbar) findViewById(R.id.toolbar);
   setSupportActionBar(toolbar);
   return toolbar;
}
```
add fragment in activity：
```
((NavigationActivity) getActivity()).addAndCommitFragment(R.id.frameLayout, new FragmentB());
```

Let your fragment inherit NavigationFragment

other:
You can get the slide of fragment via viewChange (float page), starting with 0. Please ignore if not used
```
    public void viewChange(float page) {
    }
```
ps:
If you are not using the androidx version of Fragment, please use version 1.1.3
Let your activity inherit NavigationActivity or NavigationActivityV4
Let your fragment inherit NavigationFragment or NavigationFragmentV4
```
compile 'com.github.lidajun.android:navigation-controller:1.1.3'
```
--------------------------------------------------------------------------------

用过iOS都知道iOS大多app都可以划动界面回退到一下页面，这个是iOS官方提供的一个叫NavigationController的功能。想实现一个一样的，找github，没找到合适了。自己写一个分享出来吧。
    实现原理很简单，就是使用fragment和activty。如果你也是使用fragment来管理你的页面可以试试。

1:添加gradle
```groovy
repositories {
    jcenter()
}

compile 'com.github.lidajun.android:navigation-controller:2.0.0'
```
2:如果使用Toolbar，需要使用NavigationToolbar。默认会增加一个title的切换动画。不使用请忽略
```xml
<com.github.lidajun.android.navigationcontroller.widget.NavigationToolbar
    android:id="@+id/toolbar"
    android:layout_width="match_parent"
    android:layout_height="50dp"/>
```
  Toolbar可选属性
```
<attr name="title_view_text_size" format="float"/>
<attr name="back_view_text_size" format="float"/>
<attr name="title_view_text_color" format="color"/>
<attr name="back_view_text_color" format="color"/>
<attr name="title_view_text" format="string"/>
<attr name="back_view_text" format="string"/>
```
  可以在fragment中给你的toolbar添加一个title
```
setToolbarTitle("FragmentA");
```
3:让你的activity继承NavigationActivity
   如果使用toolbar就在initNavigationToolbar返回你的toolbar，不使用请忽略;
```
@Override
protected NavigationToolbar initNavigationToolbar() {
   NavigationToolbar toolbar = (NavigationToolbar) findViewById(R.id.toolbar);
   setSupportActionBar(toolbar);
   return toolbar;
}
```
在activity中添加fragment
例如：
```
((NavigationActivity) getActivity()).addAndCommitFragment(R.id.frameLayout, new FragmentB());
```

4:让你的fragment继承NavigationFragment
其它：
你可以通过viewChange(float page)方法获得fragment的滑动，从0开始。不使用请忽略
```
    public void viewChange(float page) {
    }
```

ps:
如果没有使用androidx版本的Fragment,请使用1.1.3版本
让你的activity继承NavigationActivity或NavigationActivityV4
让你的fragment继承NavigationFragment或NavigationFragmentV4
```
compile 'com.github.lidajun.android:navigation-controller:1.1.3'
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
