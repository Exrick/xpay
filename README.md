
# XPay个人收款支付系统
[![AUR](https://img.shields.io/aur/license/yaourt.svg)](https://github.com/Exrick/xpay/blob/master/License)
[![](https://img.shields.io/badge/Author-Exrick-orange.svg)](http://blog.exrick.cn)
[![](https://img.shields.io/badge/version-1.1-brightgreen.svg)](https://github.com/Exrick/xpay)
[![GitHub stars](https://img.shields.io/github/stars/Exrick/xpay.svg?style=social&label=Stars)](https://github.com/Exrick/xpay)
[![GitHub forks](https://img.shields.io/github/forks/Exrick/xpay.svg?style=social&label=Fork)](https://github.com/Exrick/xpay)
> 作者大四作品 期待您的捐赠支持！
### [宣传视频](https://www.bilibili.com/video/av23121122/)
- 作者亲自制作 [点我观看](https://www.bilibili.com/video/av23121122/)
### 项目已部署，在线Demo
- 本项目运行后效果：http://xpay.exrick.cn
- 本项目Swagger接口文档：http://xpay.exrick.cn/swagger-ui.html
- 实际综合应用商城项目：http://xmall.exrick.cn/

### v3.0版本已发布！官方接口 自动回调！ [立即获取源码和文档](http://xpay.exrick.cn/pay)
- v3.0 新增支付宝当面付 官方接口 自动回调 提供个人申请教程
- v2.0 2019-03-16 支付宝新增扫码点单收款，暂无风控，支持一键打开支付宝APP
- v1.9 新增云闪付收款（收款直达银行卡），支付宝新增一键红包支付模式
- v1.8 支付宝新增多种支付方式(含银行转账)
- v1.7 支付宝无法修改金额备注 新增支付宝扫码检测
- v1.6 支付宝使用转账码方案 v1.5 解决近期支付宝史上最严风控问题 v1.3 实现轮询回调
- 实现订单支付标识 解决无法识别支付人问题
- ~~实现一键打开支付宝APP支付(支持安卓浏览器、IOS应用，不支持微信中打开)~~（支付宝已封禁）
- 实现移动端支付方案 支持H5、APP、小程序
- 轻松支持多账户收款 赶快使用手机扫码体验吧

    ![](http://xpay.exrick.cn/assets/images/mobiletest.png)

#### 注：v3.0源码（含原理及详细文档）获取方式 
- 进入 [XPay官网](http://xpay.exrick.cn/pay) 成功支付测试后 将自动发至你所填写的邮箱中

### 声明
> 此系统只针对个人开发收款支付，实际可应用到实现~~自动维护捐赠表等业务，无法商用~~！最新版本可已实现充值、发卡、发货等业务，可勉强供真正个人商用！日入百万千万的请绕道！当然你还可将此项目当作入门级的SpringBoot练习项目

### 个人申请支付接口现状
- 原生网银支付

    - `以银行网银为代表。此类方式涉及到承诺、合同、不菲的保证金，对个人来说不现实。`

    - 结论：不可行

- 原生支付宝，微信支付

    - `支付宝微信只服务于有营业执照、个体工商户的商户。就算你有钱但没实体店铺在某宝上也是买不到的。截止目前（2019-01-01）无法以个人身份（或以个人为主体）直接申请API。`

    - 结论：不可行

- 关联企业支付宝账号

    - `即新建企业账户，然后采用已经实名认证了的企业账户关联该账户，用其实名主体完成新账户的实名认证。一系列操作完成后，新的账户具有和企业账户一样的资质可以申请API。`

    - 结论：如果条件允许，推荐此方案
- 购买企业支付宝账号

    - `购买一个企业支付宝账号，然后信息变更，完成过户。但这种“非法”的方式，毕竟涉及到钱的问题，安全性无保障。另外，企业账户价格比较高。`

    - 结论：不推荐

- 聚合支付工具，Ping++等

    - `就是个第四方聚合支付工具，简化了接入开发流程而已，个人开发者仍然需要去申请所需接口的使用权限。`

    - 结论：不可行

- 第四方聚合支付

    - `支付资金进入官方账号，自己再进行提现操作。需要开通域名，提现手续费较高，支付页面不支持自定义。另外，对于此种类型的聚合支付平台，隐藏着极高的跑路风险。`

    - 结论：不推荐
- 美团 & 支付宝口碑
    - `通过美团商家码收款，通过支付宝口碑掌柜收款。`

    - 结论：不推荐，支付转化率并不高，可能随时被风控。
- 有赞
    - `通过有赞微商城支付接口收款。`

    - 结论：不推荐，需手动提现，不免费，费用6800/年起，一旦风控资金很难取出。
- 挂机监听软件，PaysApi、绿点支付等

    - `本质上依然是采用挂机监听的策略，但针对的是移动端支付宝或微信的收款通知消息`
    - 结论：成本高，配置麻烦，需24小时挂台安卓手机，不免费
- 其他基于Xposed挂机监听软件
    - `基于virtual xposed hook相关技术，可自动生成任意备注金额收款码 参考抢红包外挂`
    - 结论：成本高，配置麻烦，需24小时挂台安卓手机，量大易触发风控、不免费，黑产适用
- Payjs （疑使用[微信小微商户](https://pay.weixin.qq.com/index.php/core/affiliate/micro_intro)）
    - 结论：仅支持微信、不免费、使用官方接口收取代开费用
- 国外支付，PayPal、Strip：不可用
### XPay
- `支持支付宝、微信、QQ钱包、翼支付、云闪付等任意收款码，资金直接到达本人账号，个人一键审核即时回调，不需提现，不需备案，完全免费，不干涉监听任何支付数据，个人收款0风险方案`
- `由于xpay天生“人工智障”检测方案，个人收款使用无须担心风控问题`
- 结论：个人收款较少、见不得人的支付业务推荐使用
### 使用开发流程
- 用户确认订单，需填写邮箱地址(用于邮件通知)、手机号(用于短信通知)等信息

    ![QQ截图20171230234533.png](https://i.loli.net/2018/07/21/5b52e0e68eb55.png)
- 配置你的个人收款码，二维码图片配置在 `src\main\resources\static\assets\qr` 文件夹中，已有 alipay（对应支付宝收款码）、wechat（微信）、qqpay（QQ钱包）文件夹存放相应收款码。可设置固定金额二维码（文件名为“收款金额.png”，例如“1.00.png”）或自定义金额二维码（文件名为“custom.png”），建议分辨率>=180px比例1:1，推荐[二维码美化工具](http://www.wwei.cn/)，对应页面在 `src\main\resources\templates` 中 `alipay.html、wechat.html、qqpay.html`。当然聪明的你还可以在 `pay.html` 和这些中自定义业务逻辑，修改JS代码即可。

    ![QQ截图20171230234548.png](https://i.loli.net/2018/07/21/5b52e1108ab66.png)
- `application.properties` 配置文件中修改你的管理员邮箱等
- 建议下载对应邮箱App，打开支付宝、微信收款语言通知等提醒，收到支付邮箱通知后，确认是否收款到账，在邮件中进行该交易任意审核操作(业务逻辑回调完全由你自定义)，未到账的不用管，系统定时自动处理

    ![drag1min.png](https://i.loli.net/2018/07/21/5b52e14f9fc08.png) ![drag2min.png](https://i.loli.net/2018/07/21/5b52e14fa3e61.png)
### 疑问
- 如何确定收款来自哪笔交易订单？
    - ~~实际业务中审核邮件会包含用户、订单号、支付金额等信息，请根据这些信息进行核对~~
    - ~~如果短时间内收款过多仍无法判断，em？你做的什么交易收款能这么多？这只是针对个人的OK？请去申请支付接口！~~
    - v1.2版本起已解决该问问题 [点我获取最新版本源码](http://xpay.exrick.cn/pay)
- 半夜怎么办？
    - 最新版系统已加定时任务默认01:00-08:00自动关闭系统，审核邮件中也提供手动开关链接（当面付7*24自动回调）
- 为什么不做自动监听？
    - 迟早被封的为什么要做？...XPay主要面向真正个人如学生群体，也目前成本最低最稳定的方案，免费！
    - 不想受限制、不想用APP监听、还想免费？请用我这套，哈哈![QQ截图20180721215515.png](https://i.loli.net/2018/07/21/5b533b90bd23b.png)
### 市面上一些自动监听回调方案
- 爬支付宝官网
- 监听微信网页版接口
- 安卓APP监听通知栏
- 高级安卓APP监听 使用xposed框架 可实现自动生成自定义备注任意金额收款码
> 温馨（jing）提示（gao）：大家做这种监听软件之前请先仔细阅读微信、支付宝使用协议，如果他们想追究你责任你肯定是赢不了的，毕竟用这些的都是些什么业务我也不用多说。

### 前端所用技术
- [MUI](http://dev.dcloud.net.cn/mui/)：原生前端框架
- [jQuery](http://jquery.com/)
- [BootStrap](http://www.bootcss.com/)
- [DataTables](http://www.datatables.club/)：jQuery表格插件
### 后端所用技术
##### 各框架依赖版本皆使用目前最新版本
- SpringBoot
- [SpringMVC](https://github.com/Exrick/xmall/blob/master/study/SpringMVC.md)
- Spring Data Jpa
- MySQL
- Spring Data Redis
- [Druid](http://druid.io/)
- Thymeleaf：模版引擎
- [Swagger2](https://github.com/Exrick/xmall/blob/master/study/Swagger2.md)
- [Maven](https://github.com/Exrick/xmall/blob/master/study/Maven.md)
- 其它小实现：
    - @Async 异步调用
    - @Scheduled 定时任务
    - JavaMailSender发送模版邮件
- 第三方插件
    - [hotjar](https://github.com/Exrick/xmall/blob/master/study/hotjar.md)：一体化分析和反馈
- 其它开发工具
    - [JRebel](https://github.com/Exrick/xmall/blob/master/study/JRebel.md)：开发热部署
    - [阿里JAVA开发规约插件](https://github.com/alibaba/p3c)

### 文件说明
- 数据库文件：xpay.sql(仅一张表)
### 本地开发运行部署
- 依赖：[Redis](https://github.com/Exrick/xmall/blob/master/study/Redis.md)(必须)
- 新建数据库，见xpay.sql文件
- 在 `application.properties` 中修改你的配置，例如端口、数据库、Redis、邮箱配置等，其中有详细注释
- 运行 `XpayApplication.java`
- 访问默认端口8888：http://localhost:8888
### Linux后台运行示例
`nohup java -jar xpay-1.0-SNAPSHOT.jar -Xmx128m &`
### 作者其他项目推荐
- [XMall：基于SOA架构的分布式电商购物商城](https://github.com/Exrick/xmall)

    ![QQ截图20171022183906.jpg](https://i.loli.net/2018/07/21/5b52e192366a0.jpg)

- [X-Boot前后端分离开发平台](https://github.com/Exrick/x-boot)

    ![](https://i.loli.net/2018/07/21/5b52e274d2085.png)

- 微信小程序APP 
    - [前台源码点我提前获取](http://xpay.exrick.cn/pay) [预览视频](https://v.qq.com/x/page/f0627kf4x1e.html)

    ![](https://i.loli.net/2018/07/21/5b52e1de385e7.png)

    
### 技术疑问交流
- QQ交流群 `475743731(付费)`，可获取各项目详细图文文档、疑问解答 [![](http://pub.idqqimg.com/wpa/images/group.png)](http://shang.qq.com/wpa/qunwpa?idkey=7b60cec12ba93ebed7568b0a63f22e6e034c0d1df33125ac43ed753342ec6ce7)
- 免费交流群 `562962309` [![](http://pub.idqqimg.com/wpa/images/group.png)](http://shang.qq.com/wpa/qunwpa?idkey=52f6003e230b26addeed0ba6cf343fcf3ba5d97829d17f5b8fa5b151dba7e842)

- 作者博客：[http://blog.exrick.cn](http://blog.exrick.cn)
### 捐赠
![](http://xpay.exrick.cn/assets/images/mobiletest.png)

