# XPay个人收款支付系统
[![AUR](https://img.shields.io/aur/license/yaourt.svg)](https://github.com/Exrick/xpay/blob/master/License)
[![](https://img.shields.io/badge/Author-Exrick-orange.svg)](http://blog.exrick.cn)
[![](https://img.shields.io/badge/version-1.1-brightgreen.svg)](https://github.com/Exrick/xpay)
[![GitHub stars](https://img.shields.io/github/stars/Exrick/xpay.svg?style=social&label=Stars)](https://github.com/Exrick/xpay)
[![GitHub forks](https://img.shields.io/github/forks/Exrick/xpay.svg?style=social&label=Fork)](https://github.com/Exrick/xpay)
### 项目已部署，在线Demo
- 本项目运行后效果：http://xpay.exrick.cn
- 本项目Swagger接口文档：http://xpay.exrick.cn/swagger-ui.html
- 实际综合应用商城项目：http://xmall.exrick.cn/
### 声明：此系统只针对个人开发收款支付，实际可应用到实现自动维护捐赠表等业务，无法商用！当然你还可将此项目当作入门级的SpringBoot练习项目
### 个人申请支付接口现状
- 原生网银支付

    - `以银行网银为代表。此类方式涉及到承诺、合同、不菲的保证金，对个人来说不现实。`

    - 结论：不可行

- 原生支付宝，微信支付

    - `支付宝只服务于有营业执照、个体工商户的商户。就算你有钱但没实体店铺在某宝上也是买不到的。截止目前（2018-01-01）无法以个人身份（或以个人为主体）直接申请API。网上那些 “个人申请支付宝xx接口” 的文章就不要看了，节约时间。微信同支付宝，不支付个人申请。`

    - 结论：不可行

- 关联企业支付宝账号

    - `即新建企业账户，然后采用已经实名认证了的企业账户关联该账户，用其实名主体完成新账户的实名认证。一系列操作完成后，新的账户具有和企业账户一样的资质可以申请API。`

    - 结论：如果条件允许，推荐此方案
- 购买企业支付宝账号

    - `购买一个企业支付宝账号，然后信息变更，完成过户。但这种“非法”的方式，毕竟涉及到钱的问题，安全性无保障。另外，企业账户价格比较高。`

    - 结论：不推荐
- 企业账号分发

    - `支付宝和微信提供的API都有企业付款的功能，即企业账户向单个账户付款。网站使用企业账号调用接口，用户通过接口完成付款，并接收反馈，然后企业账户收款后，继续调用“付款接口”给目的账户打款。 有条件的用户可采用此种方式，但对于个人用户来讲，愿意合作的企业账号难找，合作方式待定。`

    - 结论：不推荐
- 挂机监听软件

    - `通过在软件内部打开支付宝账号，监听账单记录。然后在此基础上，继续深度定制API等接口服务。这种方式需要在软件内部登录本人支付宝及微信账户，安全性存在极大的隐患。`

    - 结论：不推荐
- 第四方聚合支付，Ping++

    - `以Ping++为代表的第四方聚合支付工具，简化并了多平台接入流程，解决了很多能力稍低或者需求较急的开发者对多系统对接的需求。但是，Ping++这类第四方聚合支付工具，解决的也只是接入流程，没有从根本上解决主体资质和权限的问题。以支付宝为例，个人开发者仍然需要去申请所需接口的使用权限。`

    - 结论：不可行
- 第四方聚合支付，云通付Passpay

    - `支付资金进入官方账号，自己再进行提现操作。需要开通域名，提现手续费较高，支付页面不支持自定义。另外，对于此种类型的聚合支付平台，隐藏着极高的平台风险。`

    - 结论：不推荐
- 其他挂机监听软件，PaysApi、绿点支付

    - `本质上依然是采用挂机监听的策略，但针对的是移动端支付宝或微信的收款通知消息，需根据你的商品定价手动配置上传固定金额收款二维码，较为麻烦`
    - 结论：不免费
- 国外支付，PayPal、Strip：不可用
### XPay
- `支持支付宝、微信、QQ钱包等任意收款码，资金直接到达本人账号，个人一键审核即时回调，不需提现，不需备案，完全免费`
- 结论：个人收款较少、见不得人的支付业务推荐使用
### 使用开发流程
- 用户确认订单，需填写邮箱地址(用于邮件通知)、手机号(用于短信通知)等信息

    ![](http://oweupqzdv.bkt.clouddn.com/QQ%E6%88%AA%E5%9B%BE20171230234533.png)
- 配置你的个人收款码，二维码图片配置在 `src\main\resources\static\assets\qr` 文件夹中，已有 alipay（对应支付宝收款码）、wechat（微信）、qqpay（QQ钱包）文件夹存放相应收款码。可设置固定金额二维码（文件名为“收款金额.png”，例如“1.00.png”）或自定义金额二维码（文件名为“custom.png”），建议分辨率>=180px比例1:1，推荐[二维码美化工具](http://www.wwei.cn/)，对应页面在 `src\main\resources\templates` 中 `alipay.html、wechat.html、qqpay.html`。当然聪明的你还可以在 `pay.html` 和这些中自定义业务逻辑，修改JS代码即可。

    ![](http://oweupqzdv.bkt.clouddn.com/QQ%E6%88%AA%E5%9B%BE20171230234548.png)
- `application.properties` 配置文件中修改你的管理员邮箱等
- 建议下载对应邮箱App，打开支付宝、微信收款语言通知等提醒，收到支付邮箱通知后，确认是否收款到账，在邮件中进行该交易任意审核操作(业务逻辑回调完全由你自定义)

    ![](http://oweupqzdv.bkt.clouddn.com/drag1min.png) ![](http://oweupqzdv.bkt.clouddn.com/drag2min.png)
### 疑问
- 如何确定收款来自哪笔交易订单？
    - 实际业务中审核邮件会包含用户、订单号、支付金额等信息，请根据这些信息进行核对
    - 如果短时间内收款过多仍无法判断，em？你做的什么交易收款能这么多？这只是针对个人的OK？请去申请支付接口！
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
- 依赖：[Redis](https://github.com/Exrick/xmall/blob/master/study/Redis.md)(非必须，但影响部分功能)
- 新建数据库，见xpay.sql文件
- 在 `application.properties` 中修改你的配置，例如端口、数据库、Redis、邮箱配置等，其中有详细注释
- 运行 `XpayApplication.java`
- 访问默认端口8888：http://localhost:8888
### Linux后台运行示例
`nohup java -jar xpay-1.0-SNAPSHOT.jar -Xmx128m &`
### X系列开源项目推荐
- [XMall：基于SOA架构的分布式电商购物商城](https://github.com/Exrick/xmall)

    ![](http://oweupqzdv.bkt.clouddn.com/QQ%E6%88%AA%E5%9B%BE20171022183906.jpg)
- 以下项目开发中，敬请期待！
    - XMall App

    ![](http://oweupqzdv.bkt.clouddn.com/QQ%E6%88%AA%E5%9B%BE20171231170920.png)
    - XMall开放平台(仿微信开放平台)

    ![](http://oweupqzdv.bkt.clouddn.com/QQ%E6%88%AA%E5%9B%BE20171231172014.png)
### 技术疑问交流
- 给作者项目Star或捐赠后可加入交流群 `475743731`，还可免费获取最新源码和 [UI框架](https://github.com/Exrick/xmall/blob/master/study/FlatLab.md) [![](http://pub.idqqimg.com/wpa/images/group.png)](http://shang.qq.com/wpa/qunwpa?idkey=7b60cec12ba93ebed7568b0a63f22e6e034c0d1df33125ac43ed753342ec6ce7)
- 作者博客：[http://blog.exrick.cn](http://blog.exrick.cn)
### 捐赠
![](http://oweupqzdv.bkt.clouddn.com/FgwHSk1Rnd-8FKqNJhFSSdcq2QVB.png)

