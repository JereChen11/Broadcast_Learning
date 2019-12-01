# Broadcast_Learning
about Broadcast &amp; BroadcastReceiver Learning

# 1. 什么是Broadcast & BroadcastReceiver
翻译一下，Broadcast中译为广播，BroadcastReceiver中译为广播接收器。这是什么？有什么作用？
说起广播，我脑海中第一时间浮现出来的就是以前上学时学校用来通知我们上下课，吃饭，放学的广播，它通过播放不同的铃声来提示我们上下课，还可以播放广播操和眼保健操，在午休晚休时播放音乐等等。那广播接收器对应的应该就是我们学校里的全校师生了吧，我们接收广播传递给我们的信息，如铃声，音乐，我们接收了广播传递给我们的信息，然后做出相对应的一系列行为，如听到下课铃声马上去小卖部买零食，听到眼保健操开始做眼保健操，听到放学铃声拎起书包回家....

但Android系统中的Broadcast(广播) & BroadcastReceiver(广播接收器)到底是什么？有什么作用呢？

## 1.1 Broadcast是什么？有什么作用？
Android app 可以从Android系统和其他Android app发送或接收广播消息，类似于发布-订阅设计模式，当你感兴趣的事情发生时就会发送广播。比如：当各种系统事件发生时(例如当系统启动或设备开始充电时)，Android系统就会发送广播，传递相对应的信息给app(将系统启动和设备开始充电的信息传递给app)。当然app也可以发送自定义广播，例如，通知其他app他们可能感兴趣的东西(例如，我们的app刚刚新下载了一些新的数据)。

app可以注册接收特定广播(例如关注订阅UP主，接收他们的动态信息)。当发送广播时，Android系统会自动地将广播路由到已订阅接收该特定类型广播的app。

一般来说，广播可以作为跨app和正常用户流之外的消息传递系统使用。但是必须小心，不能滥用在后台响应广播和运行作业的机会，因为这会导致系统性能变慢。(即不要在BroadcastReceiver中的onReceiver()方法中添加过多的逻辑或者进行任何的耗时操作)
## 1.2 BroadcastReceiver是什么？什么作用？
BroadcastReceiver(广播接收器)：是一个Android组件，允许你注册系统或App事件。一旦发生此事件，Android运行时将通知所有已注册该事件的广播接收器。

例如，应用程序可以注册ACTION_BOOT_COMPLETED系统事件，该事件在Android系统完成引导过程后触发。
# 2. Broadcast的类型
广播主要分为两种类型：标准广播和有序广播。
## 2.1 标准广播
标准广播是完全异步执行的广播，在广播发出之后，所有的广播接收器会在几乎同一时刻接收到这条广播，没有任何接收顺序。传播的效率很高，但是无法控制广播传递的顺序，也无法截断广播。
```java
//发送标准广播：
sendBroadcast(Intent intent)
```
## 2.2 有序广播
有序广播是同步执行的广播，在广播发出之后，同一时刻只会有一个广播接收器接收到该广播，并且只有等该广播接收器执行完接收后的响应逻辑后，广播才会继续往下传递。可以通过设置广播接收器的优先级，优先级高的广播接收器会优先收到广播，并且可以截断该广播，从而控制该广播不往下继续传递。
```java
//发送有序广播
sendOrderedBroadcast(Intent intent, String receiverPermission)
```
## 2.3 本地广播
前面介绍的标准广播和有序广播都是可以跨app进行广播，所以我们广播传递的信息可以被其他app接收到，反之我们也可以接收到来自其他app的广播，所以这就存在安全性问题了。当我们要让广播只在我们的app内部进行传递，并且我们注册的广播接收器也只能接收到来自我们自己app发送的广播时，就需要使用本地广播。
```java
//通过LocalBroadcastManager来发送本地广播
LocalBroadcastManager localBroadcastManager = new LocalBroadcastManager(Context context);
localBroadcastManager.sendBroadcast(Intent intent);
```
# 3. BroadcastReceiver的注册方式
广播接收器的注册方式分为两种：静态注册和动态注册。
## 3.1 静态注册
在AndroidManifest.xml(清单文件)中进行声明广播。比如：
```java
<!--系统开机启动完成后-->
<receiver android:name=".BootCompletedReceiver"
    android:enabled="true"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED"/>
    </intent-filter>
</receiver>
```
注意：从Android 8.0（API level 26），Android系统增加了对清单声明接收器的额外限制， 如果app是Android 8.0 及以上的版本，你不能在清单文件中为大多数隐式广播声明接收器。但一些隐式广播例外，你还可以继续在AndroidManifest中声明，不管是App是什么Android版本。
## 3.2 动态注册
在Activity中通过registerReceiver()来动态注册广播接收器，以及调用unregisterReceiver()来注销广播接收器
```java
@Override
protected void onResume() {
    super.onResume();
    myBroadcastReceiver = new MyBroadcastReceiver();
    //动态注册广播接收器来监听系统是否启动完成
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
    registerReceiver(myBroadcastReceiver, intentFilter);
}

@Override
protected void onPause() {
    super.onPause();
    //注销广播接收器
    unregisterReceiver(myBroadcastReceiver);
}
```
# 4. 案例Demo
静态注册 android.intent.action.BOOT_COMPLETED 开机启动广播，及动态注册自定义广播。
## 4.1 静态注册 BOOT_COMPLETED 广播
当我们完成了BOOT_COMPLETED 广播的静态注册后，当我们的手机开机后，我们的 app 就能收到该广播， 如下图所示：
![重启手机](https://github.com/JereChen11/Broadcast_Learning/raw/master/readmeImage/restart.png)
![手机启动完成app收到该广播](https://github.com/JereChen11/Broadcast_Learning/raw/master/readmeImage/onreceiver.png)

## 4.2 动态注册我们的自定义广播
在 MainActivity 中注册我们的广播，然后点击 Send MyCustomReceiver 按钮，发送该广播，并包含发送者信息，收到广播后，利用 Toast 将收到的发送者信息显示出来，效果如下：
![发送自定义广播](https://github.com/JereChen11/Broadcast_Learning/raw/master/readmeImage/sendcustomreceiver.gif)
