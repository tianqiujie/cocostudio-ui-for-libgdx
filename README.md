[![Build Status](https://travis-ci.org/varFamily/cocos-ui-libgdx.svg?branch=master)](https://travis-ci.org/varFamily/cocos-ui-libgdx)
[![](https://jitpack.io/v/varFamily/cocos-ui-libgdx.svg)](https://jitpack.io/#varFamily/cocos-ui-libgdx)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/b02254b3bc3e430bbe12eac913b75fb9)](https://www.codacy.com/app/htynkn/cocos-ui-libgdx?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=varFamily/cocos-ui-libgdx&amp;utm_campaign=Badge_Grade)
[![Coverage Status](https://codecov.io/github/varFamily/cocos-ui-libgdx/coverage.svg?branch=master)](https://codecov.io/gh/varFamily/cocos-ui-libgdx/branch/master)
[![Maintenance](https://img.shields.io/maintenance/yes/2017.svg)](https://github.com/varFamily/cocos-ui-libgdx)
[![Apache 2](http://img.shields.io/badge/license-Apache%202-red.svg)](http://www.apache.org/licenses/LICENSE-2.0)

## 存档状态
虽然Cocos Studio已经处于实质上被官方放弃的状态，但是本项目依然不定期维护。
注意:必须把cocostudio的"编辑"选择 "拖动改变尺寸"

##cocos-ui-libgdx
* 在libGDX中使用Cocos Studio
* 基于[cocostudio-ui-libgdx](https://github.com/121077313/cocostudio-ui-libgdx)修改而来。

## 展示

图片来自网络,学习之用,如有侵权,请通知删除
<img src="docs/screenshot-1.gif" width="600px"/>
<img src="docs/screenshot-3.gif" width="600px"/>
<img src="docs/screenshot-2.gif" width="600px"/>

##特性
* 支持最新版的[Cocos Studio](http://www.cocos.com/download/)进行开发，版本号v3.10
* 仅支持Cocos Studio的Json格式，请在发布项目时选择Json格式
* 支持Cocos Studio中大部分控件
* 部分支持Cocos Studio的粒子效果
* 部分支持Cocos Studio的动画效果，包括位移动画，缩放动画，帧动画（倾斜动画不支持）

## 快速入门

在`build.gradle`中添加

``` groovy
    repositories {
       mavenCentral()
       maven { url 'https://jitpack.io' }
    }

    dependencies {
       compile 'com.github.varFamily:cocos-ui-libgdx:0.1.4'
       compile 'com.github.tianqiujie:nativefont:2.5.0'
    }
```
在代码中

``` java
FileHandle defaultFont = defaultFont = Gdx.files.internal("share/MLFZS.TTF");;
CocoStudioUIEditor editor = new CocoStudioUIEditor(
    Gdx.files.internal("demo/MainScene.json"), null, null, defaultFont, null);
Group group = editor.createGroup();
```
使用AssetManager

```
//load
 assetManager = new AssetManager();
        assetManager.setLogger(new Logger("hehehe", Logger.DEBUG));
        assetManager.setLoader(CocosScene.class, new CocosLoader(new InternalFileHandleResolver()));
        assetManager.load("mainscene/MenuScene.json", CocosScene.class);
//render
if (!init) {
            if (assetManager.update()) {
                init = true;
                initUi();
            }
        }
//initUi
CocosScene cocosScene = assetManager.get("mainscene/MenuScene.json", CocosScene.class);
        root = cocosScene.getRoot();
        stage.addActor(cocosScene.getRoot(assetManager));
        Gdx.input.setInputProcessor(stage);
```
###详细demo请看AMScreen

## 源码构建
项目使用gradle管理，直接运行`./gradlew build`即可。

如果需要运行demo，执行`./gradlew demo`即可。

##有问题反馈
在使用中有任何问题，欢迎用以下方式进行反馈

* 在Github中直接创建一个issue
* QQ群：[187378034](http://shang.qq.com/wpa/qunwpa?idkey=bbd0f15c6ba62dae8479d69dfcdce3816c18c684521b84a6ba4b7ce03a70d126)
* QQ: 634416025

## 下一步计划
+ 完善cocos2dx的粒子系统
+ 添加更多单元测试
+ 检测所有Cocos Studio的demo，确保没有遗漏


##感谢
+ 群友@Hey贡献了粒子解析效果的代码
