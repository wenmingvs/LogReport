# 崩溃日志上传框架

当App崩溃的时，把崩溃信息保存到本地的同时，自动给GitHub提交崩溃issue，你只需要几句，就能完成所有配置，更多细节请了解下方

另外，崩溃信息支持邮件上传和HTTP上传，自动提交到issue使用的是邮件上传的方式。如果你拥有私人服务器，你也可以使用HTTP上传
   
![enter image description here](http://ww1.sinaimg.cn/mw690/691cc151gw1f5zb0qor9nj208p092gm1.jpg)
   
特性介绍  
    
| 特性|简介|
| ------ | ------ |
|自定义日志保存路径 |默认保存在Android/data/com.xxxx.xxxx/log中|
|自定义日志缓存大小|默认大小为30M，超出后会自动清空文件夹|
|支持多种上传方式|目前支持邮件上传与HTTP上传，会一并把文件夹下的所有日志打成压缩包作为附件上传|
|日志加密保存|提供AES，DES两种加密解密方式支持，默认不加密|
|日志按天保存|目前崩溃日志和Log信息是按天保存，你可以继承接口来实现更多的保存样式|
|携带设备与OS信息|在创建日志的时候，会一并记录OS版本号，App版本，手机型号等信息，方便还原崩溃|
|自定义日志上传的时机|默认只在Wifi状态下上传支持，也支持在Wifi和移动网络下上传|
|支持保存Log日志|在打印Log的同时，把Log写入到本地（保存的时候会附带线程名称，线程id，打印时间），还原用户操作路径，为修复崩溃提供更多细节信息|
|GitHub自动提交issue|使用邮件发送的形式，把接受崩溃日志的邮箱和GitHub特定的开源项目绑定在一起即可，更多细节请看下面介绍|

## 依赖添加
在你的项目根目录下的build.gradle文件中加入依赖
``` java
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
添加依赖
``` java
dependencies {
    compile 'com.github.wenmingvs:LogReport:1.0.3'
}
```

## 初始化
在自定义Application文件加入以下几行代码即可，默认使用email发送。如果您只需要在本地存储崩溃信息，不需要发送出去，请把initEmailReport（）删掉即可。
``` java
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initCrashReport();
    }

    private void initCrashReport() {
        LogReport.getInstance()
                .setCacheSize(30 * 1024 * 1024)//支持设置缓存大小，超出后清空
                .setLogDir(getApplicationContext(), "sdcard/" + this.getString(this.getApplicationInfo().labelRes) + "/")//定义路径为：sdcard/[app name]/
                .setWifiOnly(true)//设置只在Wifi状态下上传，设置为false为Wifi和移动网络都上传
                .setLogSaver(new CrashWriter(getApplicationContext()))//支持自定义保存崩溃信息的样式
                //.setEncryption(new AESEncode()) //支持日志到AES加密或者DES加密，默认不开启
                .init(getApplicationContext());
        initEmailReporter();
    }

    /**
     * 使用EMAIL发送日志
     */
    private void initEmailReporter() {
        EmailReporter email = new EmailReporter(this);
        email.setReceiver("wenmingvs@gmail.com");//收件人
        email.setSender("wenmingvs@163.com");//发送人邮箱
        email.setSendPassword("apptest1234");//邮箱的客户端授权码，注意不是邮箱密码
        email.setSMTPHost("smtp.163.com");//SMTP地址
        email.setPort("465");//SMTP 端口
        LogReport.getInstance().setUploadType(email);
    }
}

``` 

## 上传
在任意地方，调用以下方法即可，崩溃发生后，会在下一次App启动的时候使用Service异步打包日志，然后上传日志，发送成功与否，Service都会自动退出释放内存
``` java
LogReport.getInstance().upload(context);
```

## 发往服务器

如果您有自己的服务器，想往服务器发送本地保存的日志文件，而不是通过邮箱发送。请使用以下方法替换initEmailReporter方法
``` java

    /**
     * 使用HTTP发送日志
     */
    private void initHttpReporter() {
        HttpReporter http = new HttpReporter(this);
        http.setUrl("http://crashreport.jd-app.com/your_receiver");//发送请求的地址
        http.setFileParam("fileName");//文件的参数名
        http.setToParam("to");//收件人参数名
        http.setTo("你的接收邮箱");//收件人
        http.setTitleParam("subject");//标题
        http.setBodyParam("message");//内容
        LogReport.getInstance().setUploadType(http);
    }
```
## 保存Log到本地
使用以下方法，打印Log的同时，把Log信息保存到本地（保存的时候会附带线程名称，线程id，打印时间），并且随同崩溃日志一起，发送到特定的邮箱或者服务器上。帮助开发者还原用户的操作路径，更好的分析崩溃产生的原因
``` java
LogWriter.writeLog("wenming", "打Log测试！！！！");
```


## 关于使用邮件上传的注意事项
- 强烈建议使用163邮箱作为发送崩溃的邮箱，并且此邮箱务必要开启SMTP服务，如下图所示，才能发送邮件成功！
![enter image description here](http://ww1.sinaimg.cn/mw690/691cc151gw1f5zafbkamrj20fl05kaa8.jpg)
- 邮箱的密码，请输入163邮箱的客户端授权码，而不要输入邮箱的登录密码
- 请不要使用QQ邮箱作为发件人的邮箱，因为QQ邮箱的多重验证原因，并不支持这种形式的邮件发送
- 如果你需要自动提交崩溃日志到GitHub中，您需要使用Gmail邮箱作为收件人邮箱
- 提供以下测试邮箱给大家体验，此邮箱可以发送邮箱   
测试帐号   
帐号：wenmingvs@163.com   
登录密码：apptest123   
客户端授权码：apptest1234   

## GitHub自动提交issue的配置
请保证崩溃日志的邮件能到达您设定的Gmail邮箱中。如下图所示，才继续下去
![enter image description here](http://ww2.sinaimg.cn/mw690/691cc151gw1f5zg1m39koj212f06ydh9.jpg)

通过发送邮件来创建issue，在没有GitHub API的支持下，是不可能实现的。但是我们可以通过一些已经得到GitHub授权的第三方应用来实现。通过设置邮件监听，一旦邮箱收到了包含特定关键字的邮件，就会读取邮件的内容和标题，自动帮你提交issue。我们需要做的，就是把邮箱和GitHub的开源项目绑定起来，设置触发器即可，每次新邮件到达的时候，会检查此邮件是否满足触发器要求，如果满足，就会帮你自动提交issue。

[快速教程](https://zapier.com/zapbook/zaps/10314/create-github-issues-from-new-emails-on-gmail/)

1. 首先去注册一个帐号，如下图所示 [链接](https://zapier.com/app/min/10314/start)   
![enter image description here](http://ww1.sinaimg.cn/mw690/691cc151gw1f605xtdfudj20fg0gmab5.jpg)
2. 绑定Gmail邮箱，设置监听的邮件的类型，我们选择unread，即监听所有未读邮件（后面可以再根据个人要求修改）
![enter image description here](http://ww1.sinaimg.cn/mw690/691cc151gw1f6085s90r3j20hi0nwwge.jpg)
3. 绑定GitHub
4. 设置创建issue的样式，在3个输入框中指定以下内容
指定在哪个项目下创建issue
issue的标题与邮件的标题相同
issue的内容和邮件的内容相同
![enter image description here](http://ww4.sinaimg.cn/mw690/691cc151gw1f6085rjwcnj20hi0jrmy6.jpg)
5. 设定成功！！   
![enter image description here](http://ww3.sinaimg.cn/mw690/691cc151gw1f6085qy072j20gl0cv0tb.jpg)
6.  进入个人面板管理[链接](https://zapier.com/app/dashboard)，面板管理的Home标题下的每一个框框，都代表一个触发器，其中每个触发器都绑定了一个GitHub的开源项目   
![enter image description here](http://ww3.sinaimg.cn/mw1024/691cc151gw1f608iv7j2zj20kl04pwen.jpg)

后面我们还需要调整一下触发器，点击触发器的Edit，进入下面界面，做如下设置

1. 设定监听的邮件类型为New Email Matching Search
![enter image description here](http://ww1.sinaimg.cn/mw1024/691cc151gw1f609h9ptsnj21280hmdj6.jpg)
2. 设定按照邮件的标题，对邮件做进一步筛选。设定的subject:LogReport，意思是对标题包含有LogReport关键字的邮件做捕获。通过设定不同的关键字，我们就可以给不同的GitHub上的开源项目提交issue了
![enter image description here](http://ww3.sinaimg.cn/mw690/691cc151gw1f609mtbm4cj21290d6go6.jpg)
3. 对触发的动作做指示。我们需要自动提交issue，所以应该按照以下来设定，不再赘述了，看图：
![enter image description here](http://ww4.sinaimg.cn/mw690/691cc151gw1f609uz3aswj21220jpq6c.jpg)
![enter image description here](http://ww2.sinaimg.cn/mw690/691cc151gw1f609uy5bjbj211v0jk0w6.jpg)

8. 最后，我们返回到个人面板，如果触发器如下图所示是On状态，说明已经启动了！至此，自动提交崩溃issue的配置就完成了  
![enter image description here](http://ww3.sinaimg.cn/mw690/691cc151gw1f60cy7ndtfj20ku04eweo.jpg)

最后附上我的测试帐号，大家可以根据我的触发器的配置作为参考~   
帐号：wenmingvs@163.com   
密码：apptest1234   

Gradle 构建
------
- 版本
	- 最新 Android SDK
	- 最新 Gradle
- 环境变量
	- ANDROID_HOME
	- GRADLE_HOME，同时把bin放入path变量
	- Android SDK 安装，都更新到最新
	- Android SDK Build-tools更新到最新
	- Google Repository更新到最新
	- Android Support Repository更新到最新
	- Android Support Library更新到最新


相信未来
-----
当蜘蛛网无情地查封了我的炉台   
当灰烬的余烟叹息着贫困的悲哀   
我依然固执地铺平失望的灰烬   
用美丽的雪花写下：相信未来   

当我的紫葡萄化为深秋的露水   
当我的鲜花依偎在别人的情怀   
我依然固执地用凝霜的枯藤   
在凄凉的大地上写下：相信未来   

我要用手指那涌向天边的排浪  
我要用手掌那托住太阳的大海  
摇曳着曙光那枝温暖漂亮的笔杆   
用孩子的笔体写下：相信未来   

我之所以坚定地相信未来  
是我相信未来人们的眼睛  
她有拨开历史风尘的睫毛  
她有看透岁月篇章的瞳孔  

不管人们对于我们腐烂的皮肉  
那些迷途的惆怅、失败的苦痛  
是寄予感动的热泪、深切的同情   
还是给以轻蔑的微笑、辛辣的嘲讽   

我坚信人们对于我们的脊骨  
那无数次的探索、迷途、失败和成功   
一定会给予热情、客观、公正的评定   
是的，我焦急地等待着他们的评定  

朋友，坚定地相信未来吧  
相信不屈不挠的努力  
相信战胜死亡的年轻  
相信未来、热爱生命  
