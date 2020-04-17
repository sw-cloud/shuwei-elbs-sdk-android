# 1 概述

数位场景识别Android SDK是一个提供精准室内位置的数据服务应用程序接口。该接口可提供无需额外硬件部署的室内位置识别功能，使移动设备应用快速具备获取用户室内精准位置的能力。

# 2 获取AppId与AppKey

场景识别Android SDK需要数位授权的AppId、AppKey，每个Key仅且唯一对于1个应用验证有效，即对该Key配置环节中使用的包名匹配的应用有效；

AppId和AppKey的获取方式：

1.注册[数位云](https://cloud.papakaka.com/flash/#/dashboard)账号；

2.创建一个“我的应用”，即可获取；

3.联系数位商务同事激活此应用，即可正常使用。

# 3 开发指南

此文档适配Android SDK V4.2.0+版本。

## 3.1 导入库文件

库文件从数位云“下载”菜单中的“数位场景识别_Android_SDK”链接处获取；

请根据所用IDE选择导入方式：



Eclipse ADT：

将SDK中的 jar 文件拷贝到工程的libs文件夹中。

 

Android Studio：

在Android studio 项目的需要添加SDK的模块，新建libs文件夹；

将SDK中的jar包拷贝到刚才建立的libs文件夹中；

修改需要添加模块的build.gradle文件，添加放置SDK开发包jar到应用的lib目录，在需要集成SDK的module 中bulid.gradle文件中添加相关的依赖（具体jar包以实际提供的jar文件为准）。

```
dependencies {
    implementation files('libs/ShuweiELBS_Android_SDK_vx.x.x.jar')
}
```

## 3.2 忽略混淆（重要）

```
-keep class com.szshuwei.x.** { *; }
## 或者
-libraryjars libs/ShuweiELBS_Android_SDK _vx.x.x.jar
```

## 3.3 设置AndroidManifest.xml

在Application标签中声明SERVICE组件,每个APP拥有自己独立的定位service，此service可以单独运行在另一个进程中：

```xml
<service android:name="com.szshuwei.x.collect.service.SWLocationService"/>
or
<service android:name="com.szshuwei.x.collect.service.SWLocationService" android:process=":YourProcessName" />
```

**声明使用权限：**

```xml
<!-- 网络访问的权限 -->
<uses-permission android:name="android.permission.INTERNET"/>
<!-- 获取网络状态权限 -->
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<!-- 获取wifi列表、蓝牙列表，基站列表等定位信息的权限 -->
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<!-- 获取手机IMEI号相关信息的权限 -->
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
<!-- 允许程序获取wifi状态和变化的权限 -->
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<!-- 获取蓝牙状态的权限 -->
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
```

**权限说明：**

| 权限                                      | 用途                                                 |
| ----------------------------------------- | :--------------------------------------------------- |
| android.permission.INTERNET               | 允许程序网络访问的权限                               |
| android.permission.ACCESS_NETWORK_STATE   | 允许程序获取网络状态权限                             |
| android.permission.ACCESS_COARSE_LOCATION | 允许程序获取wifi列表、蓝牙列表，基站等定位信息的权限 |
| android.permission.ACCESS_FINE_LOCATION   | 允许程序获取wifi列表、蓝牙列表，基站等定位信息的权限 |
| android.permission.READ_PHONE_STATE       | 允许程序获取手机IMEI号相关信息的权限                 |
| android.permission.CHANGE_WIFI_STATE      | 允许程序获取wifi变化的权限                           |
| android.permission.ACCESS_WIFI_STATE      | 允许程序获取wifi状态的权限                           |
| android.permission.BLUETOOTH              | 允许程序获取蓝牙变化的权限                           |
| android.permission.BLUETOOTH_ADMIN        | 允许程序获取蓝牙变化的权限                           |

设置AppId，AppKey：

在Mainfest.xml正确设置AppId和AppKey，如果设置错误将会导致场景识别SDK服务无法正常使用。需在Application标签中加入以下代码，填入开发者自己的AppId和AppKey：

```xml
<meta-data
  android:name="com.shuwei.location.APP_ID"
  android:value="your app id"/>
<meta-data
  android:name="com.shuwei.location.APP_KEY"
  android:value="your app key"/>
```

## 3.4 初始化SDK

在应用程序的 Application 类的onCreate方法中调用SWLocationClient.initialization(this)

```java
public class MainApplication extends Application{
  @Override
  public void onCreate() {
​    super.onCreate();
​      SWLocationClient.initialization(this);
   }
}
// 如果需要开启开发者日志功能，可以调用：SWLocationClient.initialization(this, true);
```

为了保证用户能够顺利的进行数据调试，增加了数据调试模式，通过注册插值器来模拟一段有定位结果的信号，使用方法如下：

```java
SWLocationClient.initialization(this);

//...
//注册插值器，模拟输入信号，使用正常输入信号
if(DEBUG) {
​    SWLocationClient.getInstance().registerWifiInterpolater(new WifiInterpolater.Stub() {
​      @Override
​      public List<WifiItem2> interpolate() throws RemoteException {
​        return DebugUtils.mockResults();
​      }
​    });
}

//...
//取消插值器
SWLocationClient.getInstance().unregisterWifiInterpolater();
```

## 3.5 动态申请权限

对于Android 6.0以上版本，设备的部分权限使用是需要动态进行申请的，场景识别Android SDK涉及的权限有“获取手机基本信息”、“获取WiFi、基站、蓝牙等定位权限”：

获取手机基本信息的权限：

```
android.permission.READ_PHONE_STATE
```

获取WiFi、基站、蓝牙等定位需要用到的权限：

```
android.permission.ACCESS_COARSE_LOCATION
android.permission.ACCESS_FINE_LOCATION
android.permission.CHANGE_WIFI_STATE
android.permission.ACCESS_WIFI_STATE
android.permission.BLUETOOTH // 部分手机蓝牙权限要动态获取
```

**重要提示**：

**为了不影响APP的用户体验，SDK不会主动申请权限（申请权限会弹对话框），SDK只会检查APP有没有相关的权限，如果有就正常回发数据，如果没有就保持静默或者不启动Service服务。**

 

为了保证SDK的正常运行，如果您的APP 使用的SDK版本高于6.0（API >= 23） 请动态申请相关的权限，通常是在APP的启动页面去申请检查。


## 3.6 启动Service服务

在需要使用SDK中的任何功能之前，都需要先启动Service服务，启动Service服务必须放在SDK初始化之后。
建议在onCreate()生命周期中启动Service服务，代码如下：

```java
SWLocationClient.getInstance().start();
```

如果需要监听Service服务启动是否成功了，可以注册监听回调接口，代码如下：
```java
SWLocationClient.getInstance()
	.setOnClientStartListener(new SWLocationClient.OnClientStartListener() {
                    @Override
                    public void onStartSuccess() {
                        // 服务启动成功了
                    }

                    @Override
                    public void onStartFail() {
                        // 服务启动失败了
                    }
                });
```


## 3.7 停止Service服务
在不再需要使用SDK中的任何功能之后，可以停止Service服务。
建议在onDestory()生命周期中停止Service服务，代码如下：

```java
SWLocationClient.getInstance().stop();
```
**如果需要使用周期触发功能，请不要停止Service服务。**


## 3.8 周期触发的使用

所谓周期触发，就是SDK每隔一定周期进行一次场景识别触发，开发者不需要执行任何触发操作，如果需要使用周期触发功能，需要先注册周期触发回调接口，然后启动周期触发功能。
注册周期触发回调接口如下：

```java
SWLocationClient.getInstance().registerCycleLocationListener(new CycleLocationListener() {
  @Override
  public void onCycleLocationSuccess(int retCode, String msg, LocationData locationData) {
​    //定位成功时会回调这个方法
  }
  @Override
  public void onCycleError(int retCode, String msg) {
​    //定位失败时会回调这个方法
  }
});
```

启动周期触发功能如下：

```java
boolean start = SWLocationClient.getInstance().startCycleSceneRecognizeUI();
// true 表示启动成功，false 表示启动失败
```

在不需要周期触发功能的时候可以进行先停止周期触发功能，然后注销回调接口。
停止周期触发功能如下：

```java
boolean stop = SWLocationClient.getInstance().stopCycleSceneRecognizeUI();
// true 表示停止成功，false 表示停止失败
```

注销周期触发回调接口如下：

```java
SWLocationClient.getInstance().unregisterCycleLocationListener();
```

## 3.9 主动触发的使用

通过主动触发的方式，可以获取到当前位置点的一些位置相关的信息。

**这种方式默认是不开通的，如果贵方是需要在C端用户某些行为的时候进行一些埋点操作的话，可以在商务洽谈的时候进行沟通并且申请开通。如果是没开通的情况下，主动触发接口sceneRecognizeUI ()的调用会始终返回一个false，这点请知悉。**

在需要获取定位信息的地方注册对定位结果的监听器：

```java
SWLocationClient.getInstance().registerLocationListener(new LocationListener() {
​    @Override
​    public void onLocationSuccess(int code, String msg, LocationData locationData) {
​        //定位成功时会回调这个方法
​    }
​    @Override
​    public void onError(int retCode, String msg) {
​      //定位失败时会回调这个方法
   }
});
```

主动申请位置信息，通过调用SWLocationClient 的实例方法sceneRecognizeUI能够发起一次主动调用。为了防止重复的申请，我们默认会做一定大小的缓存时间，从上一次申请之后的五分钟（时间可以在后台配置）内都会返回同一个位置信息，如果客户想要更短间隔的缓存时间请联系我们进行配置。返回值为true则表示成功启动一次扫描，为false则表示未注册回调或者上一次正在扫描中，或者服务处于未启动状态。

```java
boolean isCanReqLocation = SWLocationClient.getInstance().sceneRecognizeUI();
if (isCanReqLocation) {
   //如果能正常进行获取定位，则返回true，
} else {
   //当上一次主动定位没完成、后台没配置允许主动定位或者是定位服务没启动，不能进行定位，这时候会返回false
}
```

当不需要监听回调结果的时候（如Activity的onDestroy()方法内），请及时注销监听器，以免造成内存泄露，注销方式如下：

```java
SWLocationClient.getInstance().unregisterLocationListener();
```

## 3.10 扩展参数的使用

有些应用场景或者业务下，集成方需要在场景识别的时候加上自己的标识或者数据，因此我们开放了一个扩展参数的功能。扩展参数可以根据客户的要求来配置是否在对应的回调接口中返回。如果配置为返回，则会原样返回；如果配置为不返回，则不会返回该字段。具体示例参考代码如下：

主动触发的扩展参数设置：

```java
SWLocationClient.getInstance().sceneRecognizeUI("任意自定义字符串");
//设置之后，当次的主动触发就会带有这个参数
```

周期触发的扩展参数设置：

```java
SWLocationClient.getInstance().setExtendParameterCycle("任意自定义字符串");
//设置之后，下一次的周期触发就会带有这个参数
```

定位结果中扩展参数的获取：

```java
//主动触发的定位成功回调
@Override
public void onLocationSuccess(int code, String msg, LocationData locationData) {
  //上一次请求传入的扩展参数
  String extendParameter = locationData.getExtendParameter();
  ...
}
 
...
 
//周期触发的定位成功回调
@Override
public void onCycleLocationSuccess(int retCode, String msg, LocationData locationData) {
  //上一次请求传入的扩展参数
  String extendParameter = locationData.getExtendParameter();
  ...
}
```

**特别注意：**

**扩展参数请尽量勿传入过长的字符串，以减少无谓的的带宽流量消耗，目前暂时没有对此扩展参数长度进行限制，但是不保证后期前/后端不会进行的截取操作，如有任何问题，请务必和我们进行沟通协商。**

## 3.11 透传字段的使用

透传字段和扩展字段的使用类似，不过透传字段是不需要通过后台配置是否返回的，而是一定会在对应的回调接口中原样返回的。主要用来区分不同触发请求的。具体示例参考代码如下：

主动触发的透传参数设置：

```java
SWLocationClient.getInstance().setPassThroughPassive("任意自定义字符串");
//设置之后，下一次的主动触发就会带有这个参数
```

周期触发的扩展参数设置：

```java
SWLocationClient.getInstance().setPassThroughCycle("任意自定义字符串");
//设置之后，下一次的周期触发就会带有这个参数
```

定位结果中透传参数的获取：

```java
//主动触发的定位成功回调
@Override
public void onLocationSuccess(int code, String msg, LocationData locationData) {
  //上一次请求传入的透传参数
  String passThrough= locationData.getPassthrough();
  ...
}

...

//周期触发的定位成功回调
@Override
public void onCycleLocationSuccess(int retCode, String msg, LocationData locationData) {
  //上一次请求传入的透传参数
  String passThrough= locationData.getPassthrough();
  ...
}
```

**特别注意：**

**透传参数请尽量勿传入过长的字符串，以减少无谓的的带宽流量消耗，目前暂时没有对此透传参数长度进行限制，但是不保证后期前/后端不会进行截取操作，如有任何问题，请务必和我们进行沟通协商。**

 

 **<span style='color:red'>以下章节 3.12, 3.13, 3.14是需要使用采集功能才需要使用到的接口和文档说明，如不需要采集功能，可跳过这三个小节的文档说明。</span>**

 

## 3.12 采集功能的使用

在需要使用采集功能的地方先注册采集回调接口，如果没有注册采集回调接口，是无法调用采集功能的，注册回调接口的代码是：

```java
SWLocationClient.getInstance().registerCollectListener(new CollectListener()
{
  @Override
  public void onCollectSuccess(int retCode, String msg, CollectResponseData data) {
​    // 采集接口调用成功的回调
  }
 
  @Override
  public void onCollectError(int retCode, String msg) {
​    // 采集接口调用失败的回调
  }
});
```

在需要进行数据采集的地方，通过调用以下接口，采集定位信号和数据：

```java
CollectData collectData = new CollectData();
collectData.setName(poiName); //其他数据请进行对应的设置
boolean isCanSubmit = SWLocationClient.getInstance().submitCollectDataUI(collectData);
if (isCanSubmit) {
   //如果能正常进行数据采集，则返回true，
} else {
   //当上一次定位或者采集还没完成、后台没配置允许主动定位或者是定位服务没启动，不能进行采集，这时候会返回false
}
```

**3.10.1 采集数据中的扩展字段**

采集数据支持一个自定义扩展字段(**必须是json格式的字符串**)的传递，对应的代码是：

```java
collectData. setCollectExt (collectExt);
```

通过在定位回调接口的LocationData对象中获取collectExt字段，或者在采集查询回调接口的CollectQueryData对象中获取collectExt字段，可以获取到这个采集的自定义扩展字段。

**3.10.2** **采集数据中的透传字段**

采集数据支持一个自定义的扩展字段的传递，对应的代码是：

```java
collectData.setPassThrough(passThrough);
```

透传字段在采集调用成功后，会直接在 CollectResponseData 中原样返回。

**3.10.3 采集数据中的采集点id字段**

该字段是用来支持“单店多点采集”用的，即在同一个店铺的多个位置进行采集，如：采集同一个店铺的门口、收银台、试衣间、卫生间等多个点位。一个店铺对应一个 poiId，一个采集点对应一个 collectPoint，poiId和collectPoint是一对多的关系。对应的代码：

```java
collectData.setPoiId(poiId);
```

每个店铺第一次采集时不用传入 poiId，当第一个采集点采集成功后，CollectResponseData 中会返回一个新生成的 poiId 字段的值，以后采集的点，如果想关联到同一个 poiId 下，只需要在采集的时候带上 poiId 的值即可。

**3.10.4 采集数据中的采集者id字段**

采集数据支持一个采集者id字段（非必传字段）的传递，通过设置这个字段，可以指明该条采集数据是由哪个采集者上传的，之后在查询或删除对应的采集信息时，也需要带上同样的采集者id字段。如果没有传递这个字段，则默认使用设备的唯一标识作为采集者id字段。建议开发者传递用户id或其他能唯一识别用户的字符串到这个字段以便进行用户账号对于采集数据的权限控制。对应的代码是：

```java
collectData.setCollectorId(collortorId);
```

**特别注意：请根据商务合同中确定的需要采集的数据进行传递。省、市、区、街道、楼宇、楼层不得超过64个字符长度，商圈、name字段不得超过128个字符长度，自定义扩展字段必须是json格式的字符串且不得超过255个字符长度，自定义透传字段可以是任意字符串且不得超过255个字符长度，采集者id字段是任意字符串且不得超过255个字符长度。**

## 3.13 采集查询功能的使用

在需要查询采集信息的地方先注册采集查询回调接口，如果没有注册采集查询回调接口，是无法调用采集查询功能的，注册回调接口的代码是：

```java
SWLocationClient.getInstance().registerQueryListener(new QueryListener()
{
  @Override
  public void onQuerySuccess(int retCode, String msg, QueryResponseData data) {
​    // 采集查询接口调用成功的回调
  }

  @Override
  public void onQueryError(int retCode, String msg) {
​    // 采集查询接口调用失败的回调
  }
});
```

在需要查询采集信息的地方，通过调用以下接口，查询采集数据：

```java
QueryData data =new QueryData();
data.setCurrentPage(currentPage); //设置当前页
data.setPageSize(pageSize); //设置每一页的数据数量
data.setPassThrough(ptParam); //设置透传字段
data.setCollectorId(coParam); //设置采集者id
 
boolean isCanQuery = SWLocationClient.getInstance().queryCollectDataUI(data);
if (isCanQuery ) {
   //如果能正常进行采集数据查询，则返回true，
} else {
   //当上一次采集数据查询还没完成、或者网络不可用而不能进行采集查询时，会返回false
}
```

其中设置透传字段和采集者id的信息，请参考**“3.12采集功能的使用”**中的说明。

## 3.14 采集删除功能的使用

在需要删除采集信息的地方先注册采集删除回调接口，如果没有注册采集删除回调接口，是无法调用采集删除功能的，注册回调接口的代码是：

```java
SWLocationClient.getInstance().registerDeleteListener(new DeleteListener()
{
  @Override
  public void onDeleteSuccess(int retCode, String msg, DeleteResponseData　data) {
​    // 采集删除接口调用成功的回调
  }

  @Override
  public void onDeleteError(int retCode, String msg) {
​    // 采集删除接口调用失败的回调
  }
});
```

在需要删除采集信息的地方，通过调用以下接口，删除采集数据：

```java
DeleteData data = new DeleteData();
data.setPoiIdList(pois); //设置需要删除的POI点的编码集合
data.setCpCodeList(cpCodes); //设置需要删除的采集点的编码集合
data.setPassThrough(ptParam); //设置透传字段
data.setCollectorId(coParam); //设置采集者id

boolean isCanDelete = SWLocationClient.getInstance().deleteCollectDataUI(deleteData);
if (isCanDelete ) {
   //如果能正常进行采集数据删除，则返回true，
} else {
   //当上一次定位采集数据删除还没完成、或者网络不可用而不能进行采集删除时，会返回false
}
```

其中设置透传字段和采集者id的信息，请参考**“3.12采集功能的使用”**中的说明。

## 3.15 SDK性能数据

影响SDK的电量的主要因素为定位周期和WiFi列表扫描，其中定位周期影响因素较大。下表中“满负荷工作”状态为“扫描周期5min、定位周期5min，设备位置持续变化”状态。

| SDK适用说明                  | Android4.0（API Level 14）及以上版本 |
| :--------------------------- | :----------------------------------- |
| 大小                         | 305.6 KB                             |
| 流量消耗（满负荷工作）       | ≈2.60096KB/次                        |
| 耗电量（满负荷工作）         | ≈0.092875mAh                         |
| 静默时CPU占用                | ≈0%                                  |
| 触发时CPU峰值占用            | ≈16.575%                             |
| 触发时CPU峰值占用持续时长    | ≤160ms                               |
| 静默时平均内存占用           | 3.5M                                 |
| 触发时内存波动的峰值         | ≈4MB                                 |
| 触发时内存波动的峰值持续时长 | ≈460ms                               |

## 3.16 工作原理

SDK通过上传用户采集的数据和手机获取到的当前位置的信号信息（主要是必传的WiFi信号，同时也包括基站、蓝

牙、光强、地磁、方向等非必传信号）到服务器来定位；

定位运行过程中会通过定时器设定每隔一定周期会在设备权限允许的前提下扫描设备的WiFi列表；

扫描列表通过与最近一次的扫描结果进行比较，上一次信号变化超过设定的阈值，调用定位接口进行定位；

应用列表、设备信息等其他的上报数据通过设定周期，定期在定位接口中同时上报数据到服务器。

# 4 Android Q的适配

## 4.1 设备唯一标示IMEI的获取

在Android Q上将无法获取设备的IMEI 信息，我们已经对此获取容错处理，在Android Q手机上不会报错。

## 4.2 MAC地址的获取

在Android Q上获取到的MAC地址将是随机的，导致无法通过MAC追踪用户，目前对这个接口做了容错处理，不会报错，对于获取到的MAC是随机的，暂时没有处理。

## 4.3 定位权限的变化

在Android Q上，如果应用想要在后台获取定位权限，需要申请ACCESS_BACKGROUND_LOCATION 权限。对应APP应用，如果应用的 targetSdkVersion=Q，必须动态申请 ACCESS_BACKGROUND_LOCATION 权限；如果应用的 targetSdkVersion<Q，并且申请了 ACCESS_FINE_LOCATION 和 ACCESS_COARSE_LOCATION 权限，系统会自动授予 ACCESS_BACKGROUND_LOCATION 权限。对于希望在后台获取定位功能的客户，请适配 ACCESS_BACKGROUND_LOCATION 的权限判断。

## 4.4 无线信号的获取

在Android Q上想要扫描WiFi、蓝牙及使用 WLAN API 都必须拥有 ACCESS_FINE_LOCATION 权限才行。所以请客户的应用在针对Android Q做适配的时候，务必申请到 ACCESS_FINE_LOCATION 权限，否则会影响定位功能的结果。

## 4.5 外部存储设备权限的变化

每个应用将只能访问自己应用创建的文件，且替换并取消了原来的 READ_EXTERNAL_STORAGE 和 WRITE_EXTERNAL_STORAGE 权限，改而使用 READ_MEDIA_IMAGES, READ_MEDIA_VIDEO, READ_MEDIA_AUDIO 等权限，由于我们SDK中并不需要读取外部存储设备的权限，所以对于我们 SDK 的影响不大。

## 4.6 非Android SDK接口限制

Android Q对于通过反射获取系统中隐藏接口功能的API做了限制，目前我们SDK中只会通过反射获取应用的Activity任务栈中Activity的个数，这个API接口目前在Android Q限制的灰名单中，暂时没有影响。

## 4.7 WiFi信号扫描限制

从Android P开始，系统对于WIFI信号的扫描做了限制，前台应用 2 分钟只能获取4次WiFi，所有后台应用30分钟内一共只能获取到1次WiFi信号，所以请客户控制好主动定位的频率。

**特别注意：**

**我们SDK已经针对Android Q做了适配，关于SDK中需要使用到的权限，需要客户的应用自己申请。**

# 5 上架问题

SDK的场景识别功能目前适用的范围为中国境内，在Google Play上架包含SDK的应用，请开发者注意Google Play的隐私政策，请按照隐私政策，书写隐私文档。

# 6 定位结果(LocationData)的说明

| 序号 | 字段             | 数据类型 | 说明                                                         |
| :--- | :--------------- | :------- | ------------------------------------------------------------ |
| 1    | retCode          | Integer  | 业务码。  0：定位成功，包含结果;  >0：参见业务码说明。       |
| 2    | msg              | String   | 业务码辅助提示信息。  有定位结果："OK";  无定位结果或其他：参见业务码说明。 |
| 3    | requestSign      | String   | 请求签名                                                     |
| 4    | data             |          | 返回数据实体                                                 |
| 4.1  | timestamp        | String   | 接口响应时间戳（ms）                                         |
| 4.2  | name             | String   | POI名称                                                      |
| 4.3  | floorName        | String   | 楼层信息                                                     |
| 4.4  | building         | String   | 楼宇名                                                       |
| 4.5  | area             | String   | 商圈信息                                                     |
| 4.6  | address          | String   | 地址                                                         |
| 4.7  | street           | String   | 街道                                                         |
| 4.8  | region           | String   | 行政区                                                       |
| 4.9  | city             | String   | 城市                                                         |
| 4.10 | brand            | String   | 品牌                                                         |
| 4.11 | areaCategory     | String   | 商圈分类                                                     |
| 4.12 | areaAttribute    | String   | 商圈属性                                                     |
| 4.13 | poiCategory      | String   | POI分类                                                      |
| 4.14 | poiAttribute     | String   | POI属性                                                      |
| 4.15 | coordinateSystem | String   | 经纬度坐标系类型：  BD09-百度坐标系  GCJ02-火星坐标系  WGS84-地球坐标系 |
| 4.16 | longitude        | Double   | 经度                                                         |
| 4.17 | latitude         | Double   | 纬度                                                         |
| 4.18 | collectExt       | String   | 采集的自定义扩展字段                                         |
| 4.19 | passThrough      | String   | 透传参数                                                     |
| 4.20 | poiId            | String   | POI唯一标识                                                  |

**字段示例**：

```json
{
	"retCode": 0,
	"msg": "OK",
	"requestSign": "439d8190a0ac977306ae436859bd4a9b25a64ada",
	"data": {
		"timestamp": 1552374907740,
		"name": "zara",
		"floorName": "1",
		"buliding": "海岸城购物中心",
		"area": "海岸城购物中心",
		"address": "文心四路14",
		"street": "文心四路14",
		"region": "南山区",
		"city": "深圳市",
		"brand": "zara",
		"areaCategory": "购物中心;商场",
		"areaAttribute": "档次:高档",
		"poiCategory": "购物;服装",
		"poiAttribute": "风格:潮流时尚|品牌来源:欧美",
		"coordinateSystem": "GCJ02",
		"longitude": 113.936074,
		"latitude": 22.51703,
		"collectExt ": "asdf",
		"passThrough": "passThrough_example",
		"poiId": "fdae3e2cd316bb8a"
	}
}
```

# 7 采集字段(CollectData)的说明

| 字段        | 类型   | 说明                 | 备注                               |
| ----------- | ------ | -------------------- | ---------------------------------- |
| timestamp   | Long   | 采集的时间戳（ms）   |                                    |
| name        | String | POI名称              |                                    |
| floorName   | String | 楼层信息             |                                    |
| building    | String | 楼宇名               |                                    |
| area        | String | 商圈信息             |                                    |
| address     | String | 地址                 |                                    |
| street      | String | 街道                 |                                    |
| region      | String | 行政区               |                                    |
| city        | String | 城市                 |                                    |
| province    | String | 省份                 |                                    |
| collectExt  | String | 采集的自定义扩展字段 | 必须是json格式的字符串             |
| passThrough | String | 采集的自定义透传字段 | 可以是任意类型的字符串             |
| collectorId | String | 采集者id             | 建议传递用户唯一标识以进行权限控制 |

**字段示例：**

```json
    "timestamp": 1552374907740,
    "name": "zara",
    "floorName": "1",
    "buliding": "海岸城购物中心",
    "area": "海岸城购物中心",
    "address": "文心四路14",
    "street": "文心四路14",
    "region": "南山区",
    "city": "深圳市",
    "province": "广东省",
    "collectExt": "ext",
    "passThrough": "pt",
    "collectorId": "xxx"
```

# 8 采集结果(CollectResponseData)的说明

| 序号  | 字段          | 数据类型 | 说明                                                         |
| ----- | ------------- | -------- | ------------------------------------------------------------ |
| 1     | retCode       | Integer  | 业务码。  0：定位成功，包含结果;  >0：参见业务码说明。       |
| 2     | msg           | String   | 业务码辅助提示信息。  有定位结果："OK";  无定位结果或其他：参见业务码说明。 |
| ３    | data          |          | 返回数据实体                                                 |
| 3.1   | requestSign   | String   | 请求签名                                                     |
| 3.2   | timestamp     | String   | 接口响应时间戳（ms）                                         |
| 3.3   | passThrough   | String   | 透传字段，可以是任意类型的字符串                             |
| 3.4   | poiId         | String   | POI点编码                                                    |
| 3.5   | collectPoints |          | 采集点列表                                                   |
| 3.5.1 | cpCode        | String   | 采集点的编码                                                 |
| 3.5.2 | timestamp     | String   | 采集点的采集时间                                             |
| 3.5.3 | latitude      | double   | 纬度                                                         |
| 3.5.4 | longitude     | double   | 经度                                                         |
| 3.5.5 | passThrough   | String   | 透传字段，可以是任意类型的字符串                             |

**字段示例**

```json
{
	"retCode": 0,
	"msg": "OK",
	"data": {
		"timestamp": 1565937976253,
		"passThrough": "pt",
		"requstSign": "d27b6101beaf0e0cf2898c938472a26b1005dc77",
		"poiCode": "95rol0ye1ldldrk1",
		"collectPoints": [{
			"cpCode": "64rlglqelvncx38o",
			"timestamp": 1565937973382,
            "latitude": 22.549247,
            "longitude": 113.943889,
            "passThrough": "pt"
		}]
	}
}
```

# 9 采集查询结果(QueryResponseData)的说明

| 序号     | 字段          | 数据类型 | 说明                                                         |
| -------- | ------------- | -------- | ------------------------------------------------------------ |
| 1        | retCode       | Integer  | 业务码。  0：定位成功，包含结果;  >0：参见业务码说明。       |
| 2        | msg           | String   | 业务码辅助提示信息。  有定位结果："OK";  无定位结果或其他：参见业务码说明。 |
| ３       | data          |          | 返回数据实体                                                 |
| 3.1      | requestSign   | String   | 请求签名                                                     |
| 3.2      | timestamp     | String   | 接口响应时间戳（ms）                                         |
| 3.3      | passThrough   | String   | 透传字段                                                     |
| 3.4      | recordsCount  | Integer  | 该collectorId总的采集数量                                    |
| 3.5      | data          |          | 采集数据实体                                                 |
| 3.5.1    | poiId         | String   | POI点编码                                                    |
| 3.5.2    | timestamp     | String   | 采集点的采集时间                                             |
| 3.5.3    | poiName       | String   | POI名称                                                      |
| 3.5.4    | floorName     | String   | 楼层信息                                                     |
| 3.5.5    | building      | String   | 楼宇名                                                       |
| 3.5.6    | area          | String   | 商圈信息                                                     |
| 3.5.7    | address       | String   | 地址                                                         |
| 3.5.8    | street        | String   | 街道                                                         |
| 3.5.9    | region        | String   | 区域                                                         |
| 3.5.10   | city          | String   | 城市                                                         |
| 3.5.11   | province      | String   | 省份                                                         |
| 3.5.12   | collectExt    | String   | 扩展字段                                                     |
| 3.5.13   | collectPoints |          | 采集点列表                                                   |
| 3.5.13.1 | cpCode        | String   | 采集点编码                                                   |
| 3.5.13.2 | timestamp     | String   | 时间戳                                                       |
| 3.5.13.3 | latitude      | double   | 纬度                                                         |
| 3.5.13.4 | longitude     | double   | 经度                                                         |
| 3.5.13.5 | passThrough   | String   | 透传字段，可以是任意类型的字符串                             |

**字段示例**：

```json
{
	"retCode": 0,
	"msg": "OK",
	"data": {
		"timestamp": 1565944012784,
		"requestSign": "c120897a9ada4ddd0d9e8fa4ba30e8e2e7421f2a",
		"passThrough": "pt",
		"recordsCount": 1,
		"data": [{
			"poiId": "74rn9xyz223453od",
			"timestamp": 1565937855879,
			"poiName": "zara",
			"floorName": "1",
			"bulid": "海岸城购物中心",
			"area": "海岸城购物中心",
			"address": "文心四路14",
			"street": "文心四路14",
			"region": "南山区",
			"city": "深圳市",
			"province": "广东省",
			"collectExt": "ext",
			"collectorId": "xxx",
			"collectPoints": [{
			    "cpCode": "64rlglqelvncx38o",
			    "timestamp": 1565937973382,
                "latitude": 22.549247,
                "longitude": 113.943889,
                "passThrough": "pt"
		}]
		}]
	}
}
```

# 10 采集删除结果(DeleteResponseData)的说明

| 序号 | 字段        | 数据类型 | 说明                                                         |
| :--- | ----------- | -------- | :----------------------------------------------------------- |
| 1    | retCode     | Integer  | 业务码。  0：定位成功，包含结果;  >0：参见业务码说明。       |
| 2    | msg         | String   | 业务码辅助提示信息。  有定位结果："OK";  无定位结果或其他：参   见业务码说明。 |
| ３   | data        |          | 返回数据实体                                                 |
| 3.1  | requestSign | String   | 请求签名                                                     |
| 3.2  | timestamp   | String   | 接口响应时间戳（ms）                                         |
| 3.3  | passThrough | String   | 透传字段                                                     |

**字段示例**：
```json
{
	"retCode": 0,
	"msg": "OK",
	"data": {
		"requstSign": "d27b6101beaf0e0cf2898c938472a26b1005dc77",
		"timestamp": 1565937976253,
		"passThrough": "pt"
	}
}
```

# 11 采集和定位结果码说明

| retCode  | msg                                               | remark                            |
| -------- | ------------------------------------------------- | --------------------------------- |
| 0        | OK                                                | 成功                              |
| 100001   | System error                                      | 服务器错误                        |
| 100002   | Environment error                                 | 环境错误                          |
| 100003   | Miss required parameter                           | 缺少必传参数                      |
| 100004   | Parameter value invalid                           | 参数格式错误                      |
| 100005   | Sign error                                        | 签名错误                          |
| 100006   | Package name error                                | 包名错误                          |
| 100007   | Request data is null                              | 请求数据为空                      |
| 100008   | Lack authorization                                | 限制访问。缺少头部授权信息        |
| 100009   | Restricted access.App TPS reach limit             | 限制访问。应用每秒事务数达到上限  |
| 100010   | Restricted access.Transaction reach daily limit   | 限制访问。达到每天事务数配额上限  |
| 100011   | Restricted access.TPS reach limit                 | 限制访问。Server tps 达到上限     |
| 10002001 | Illegal oid.Fake oid                              | 疑似虚假设备号                    |
| 10002002 | Illegal oid.Oid request frequency is too high     | 高频请求                          |
| 201001   | No result                                         | 定位失败                          |
| 203004   | AppId invalid                                     | AppId无效                         |
| 203005   | App is disabled                                   | App不可用                         |
| 203006   | App type error                                    | App类型错误                       |
| 206001   | Lack of required POI fields                       | 缺少必须的poi字段                 |
| 206002   | POI field too long                                | poi字段太长                       |
| 206003   | Containing sensitive words                        | 包含敏感词                        |
| 206004   | Collect is turned off                             | UGC采集未开启                     |
| 206005   | Collect reach daily limit                         | UGC采集每日配额达到上限           |
| 206006   | PoiCode value invalid                             | poiCode无效                       |
| 206007   | PoiCode value invalid                             | poiCode无效                       |
| 206008   | PoiCode and collector do not match                | poiCode和collectId不匹配          |
| 206009   | Data does not exist or has been deleted           | POI不存在或已经删除               |
| 206010   | The value of CP should be greater than 0          | 当前页应该大于0                   |
| 206011   | The value of PS should be greater than 0          | 每页页大小应该大于0               |
| 206012   | The value of province containing sensitive words  | 省包含敏感词                      |
| 206013   | The value of city containing sensitive words      | 城市包含敏感词                    |
| 206014   | The value of region containing sensitive words    | 区域包含敏感词                    |
| 206015   | The value of street containing sensitive words    | 街道包含敏感词                    |
| 206016   | The value of address containing sensitive words   | 地址包含敏感词                    |
| 206017   | The value of area containing sensitive words      | 商圈包含敏感词                    |
| 206018   | The value of building containing sensitive words  | 楼宇包含敏感词                    |
| 206019   | The value of floorName containing sensitive words | 楼层名称包含敏感词                |
| 206020   | The value of poi name containing sensitive words  | POI名称包含敏感词                 |
| 206021   | The value of ext containing sensitive words       | 扩展字段包含敏感词                |
| 206023   | The value of ext must be Json                     | 扩展字段必须是Json格式的          |
| 300001   | Prepare launch resources error                    | SDK准备请求资源失败               |
| 300002   | The location response parse error                 | SDK解析定位结果数据失败           |
| 300004   | No http message                                   | SDK检测定位结果中没有msg信息      |
| 300005   | No available network                              | SDK检测没有可用的网络             |
| 300006   | Data verify failed                                | SDK检测定位请求数据不符合要求     |
| 300007   | Inner error                                       | SDK内部执行流程出现异常           |
| 300008   | Http header is error                              | SDK检测Http请求头错误             |
| 300009   | Sign error                                        | SDK检测签名错误                   |
| 300010   | Http request timeout                              | SDK请求定位超时                   |
| 300011   | Http other error                                  | SDK 请求http 出错                 |
| 300015   | the collect response parse error                  | SDK检测采集请求数据不符合要求     |
| 300016   | the query request json error                      | SDK检测采集查询请求数据不符合要求 |
| 300017   | the query request other error                     | SDK检测采集查询请求数据其他错误   |
| 300018   | the query response parse error                    | SDK检测采集查询响应数据解析失败   |
| 300019   | the delete request json error                     | SDK检测采集删除请求数据不符合要求 |
| 300020   | the delete request other error                    | SDK检测采集删除请求数据其他错误   |
| 300021   | the delete response parse error                   | SDK检测采集删除响应数据解析失败   |