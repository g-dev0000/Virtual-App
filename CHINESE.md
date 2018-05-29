[![VA banner](https://raw.githubusercontent.com/asLody/VirtualApp/master/Logo.png)](https://github.com/asLody/VirtualApp)

简介
---
**VirtualApp**是一个**App虚拟化引擎**（简称`VA`）。

**VirtualApp已兼容Android 0(8.0)。**

VirtualApp在你的App内创建一个`虚拟空间`，你可以在虚拟空间内任意的`安装`、`启动`和`卸载`APK，这一切都与外部隔离，如同一个`沙盒`。

运行在`VA`中的APK无需在外部安装，即VA支持**免安装运行APK**。

VA目前被广泛应用于插件化开发、无感知热更新、APP多开、APP云加载、移动办公安全、军队政府保密、手机模拟信息、隐私保护、脚本自动化、自动化测试、游戏手柄免激活等技术领域，但它决不仅限于此，Android本身就是一个极其开放的平台，免安装运行APK这一Feature打开了无限可能--------这都取决于您的想象力。

申明
---
VirtualApp是罗盒科技开发运营，罗盒科技在深圳及山东设有公司，于2015年至2018年陆续申请多项VirtualApp知识产权。当您需要将VirtualApp用于商业用途时，请购买商业授权。
<br/>
您如果未经授权将VirtualApp的代码作为您自己的代码用于内部使用、商业牟利或上传应用市场，我们将直接报警（侵犯著作权罪）或起诉，这将对您所属公司造成刑事责任及法律诉讼，影响到您公司的商誉和投资。<br/>
<br/>
目前VirtualApp拥有各行业众多授权客户，集成VirtualApp代码的APP日启动量超过2亿次。购买商业授权为您节省大量开发、测试和完善时间，让您有更多时间用于创新及盈利。获取VirtualApp商业授权后您将得到：商业版代码、说明接入文档、微信和QQ群技术支持。<br/>
<br/>
负责人：张总<br/>
手机：130-321-77777<br/>
Q Q：10890<br/>
<br/>
商业版代码保持每月1-2次以上频率持续更新，并对Android 8.0进行大量适配，保证了兼容性。<br/>


5月24日最新商业版更新：<br/>
1、适配vivo一个自定义权限（后台弹窗）VA是把一个历史acitivty返回前台，vivo需要这个权限。<br/>
2、如果没有蓝牙权限，返回默认值（海外用）<br/>
3、修复uid权限检查问题<br/>


5月22日最新商业版更新：<br/>
1、安全性更新，内部应用的文件权限控制<br/>
2、提高内部app调用的兼容性，第三方登录，分享<br/>
3、自动过滤没权限的外部ContentProvider<br/>
4、增加功能：内部app的权限检查（默认关闭）<br/>
5、机型模拟:Build类和build.prop<br/>
6、提高对乐固加固的app兼容性<br/>
7、适配三星wifimanager<br/>

5月8日最新商业版更新：<br/>
1、修复ipc框架一个参数传递问题（IPCMethod这个类必须更新）<br/>
2、补全7.0通知栏的hook<br/>
3、修正8.0动态快捷菜单的hook<br/>
4、SettingHandler新增一个适配接口，主要适配各种游戏<br/>


4月27日最新商业版更新：<br/>
1、功能改动：google自动安装改为手动安装，避免第一次启动时间过久<br/>
2、可以禁止访问外部某个ContentProvider<br/>
3、适配华为桌面图标数量<br/>

4月25日最新商业版更新：<br/>
1、权限分类注释，标注可删除权限。<br/>
2、增加双开模式的app跟随外部升级的开关。<br/>
3、提高app的jni兼容性。<br/>
4、提高对app集成其他插件框架的兼容性。<br/>
5、增加设置接口，根据包名进行设置。<br/>
6、增加Uri的适配范围，支持通过Uri分享和查看文件。<br/>
7、修复一个在三星8.0的问题。<br/>


4月20日最新商业版更新：<br/>
1、提高对系统自带的app组件兼容性，更好兼容chrome webview，google service。<br/>

4月17日最新商业版更新：<br/>
1、提高ART稳定性<br/>
2、增加相机适配范围<br/>
3、支持内部App在8.0下的快捷方式管理<br/>
4、修复exec异常<br/>

4月13日最新商业版更新：<br/>
1、提高稳定性（修复微信登录闪退）<br/>

4月10日最新商业版更新：<br/>
1、解决微信数据库崩溃问题<br/>
2、修复部分4.4设备崩溃问题<br/>

4月4日最新商业版更新：<br/>
1、修复后台应用易被杀死，土豆视频黑屏，新浪微博无法打开，优酷两次返回无法退出。<br/>
2、增加应用的保活机制，双开APP更不易被杀死。<br/>
3、优化虚拟引擎启动性能。<br/>
4、兼容了大部分的加固，第三方APP兼容性对比上一版提升40%+。<br/>

商业版代码历史更新：<br/>
38、修复某些rom下，快捷方式图标不正确<br/>
37、兼容以前组件StubFileProvider<br/>
36、适配部分新ROM的虚拟IMEI<br/>
35、改善进程初始化代码，增加稳定性<br/>
34、添加内部发送Intent.ACTION_BOOT_COMPLETED的广播，可以设置开关<br/>
33、适配关联google play游戏，支持游戏使用google登录<br/>
32、适配android O的google service框架<br/>
31、适配android O 快捷方式<br/>
30、适配耳机模式<br/>
29、某些rom对intent的大小限制，demo添加缩放快捷方式图标代码<br/>
28、修复多开情况下一个bug<br/>
27、修复某些情况下MediaController的bug<br/>
26、修复4.1.2的StubFileProvider报错<br/>
25、分享的uri处理<br/>
24、修复跨app调用Activity的回调<br/>
23、前台服务的通知栏拦截开关<br/>
22、附带doc<br/>
21、完善VA内部的intent的CHOOSE回调<br/>
20、Android O的通知栏适配2<br/>
19、ipc框架优化, 提高判断binder的存活准确性<br/>
18、jni的log开关 Android.mk:LOCAL_CFLAGS += -DLOG_ENABLE<br/>
17、混淆配置<br/>
16、Android O的通知栏适配<br/>
15、修复部分app网络卡的问题<br/>
14、适配 android 8.0的dl_open（jni加载）<br/>
13、修复华为emui8.0的一个bug<br/>
12、完善定位<br/>
11、设置手机信息，imei伪装算法<br/>
10、适配8.0某个功能（主要app：whatsapp）<br/>
9、修复内部微信等应用，无法更新图片，视频<br/>
8、demo增加安装监听，自动升级克隆模式的应用<br/>
7、7.0的file provider适配<br/>
6、增加了定位代码<br/>
5、代码进行了架构优化<br/>
4、与开源版不同的特征<br/>
3、解决了微信被封的一些问题<br/>
2、修复了部分机型兼容性<br/>
1、修复了12个小BUG<br/>


已支持的加固(不断更新)
----------
* 360加固
* 腾讯加固
* 梆梆加固
* 梆梆企业版(12306客户端 Pass)
* 爱加密
* 百度加固
* 娜迦加固
* 乐变加固
* 网易易盾
* 通付盾
* (已支持的加固均可通过VA来脱壳，本技术不公开)


Google Play Framework
-----------
在google service新版本，存在兼容适配问题，暂时屏蔽


使用说明
----------

**前往你的Application并添加如下代码:**
```java
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            VirtualCore.getCore().startup(base);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
```
**安装App:**
```java
    VirtualCore.getCore().installApp({APK PATH}, flags);
```
**启动App:**
```java
    VirtualCore.getCore().launchApp({PackageName});
```
**移除App:**
```java
    VirtualCore.getCore().uninstallApp({PackageName});
```
**该App的基本信息:**
```java
    VirtualCore.getCore().findApp({PackageName});
```

BUG反馈
------------
zl@aluohe.com
