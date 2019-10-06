# XPay个人收款支付系统

> 基于Java开发 新手请先百度Spring Boot教程

### XPay原理流程见 https://github.com/Exrick/xpay

### 支付宝官方获取userId方式
- 支付宝内打开链接：`https://render.alipay.com/p/f/fd-ixpo7iia/index.html`
- 或使用支付宝扫描该文件夹根目录下中的二维码
- userId获取：请进入 蚂蚁金服开放平台官网 登陆后 点击右上角进入账户管理或账户信息 在合作伙伴管理下方即可找到你的角色身份PID那串数字即为你的userId，其他方法请百度

### v2.0新增支付宝扫码点单模式
- 借助支付宝“扫码点单”小程序，只需开通商家收款码即可开通
- 备注号对应桌号
- 详细使用说明见word图文文档

### v1.9新增支付宝一键红包支付模式以及云闪付说明
- 红包模式正规个人业务没必要使用，量大怕风控者用的
- 需先执行加好友，支付宝需在设置-隐私-常用隐私设置中关闭加好友需要验证，实例：`alipays://platformapi/startapp?appId=20000186&actionType=addfriend&userId=支付宝userId&loginId=支付宝账号&source=by_f_v&alert=true`
- 仅支持普通红包，目前h5中可一键拉起，同样url的中文（如支付宝昵称）需经过encode编码，实例：`alipays://platformapi/startapp?appId=88886666&appLaunchMode=3&canSearch=false&chatLoginId=支付宝账号&chatUserId=支付宝userId&chatUserName=支付宝昵称&chatUserType=1&entryMode=personalStage&prevBiz=chat&schemaMode=portalInside&target=personal&money=金额&amount=金额&remark=备注`
- 云闪付由于官方风控生成的固码具有时效性（一定时间后无法扫码）
    - 解决方案：请务必使用商家收款码（app中申请即可），由于商家收款吗无法添加备注，匹配支付标识采用不同优惠价格实现，优惠规则详见pay.html中487行js代码
- v1.9需要替换的地方：
```
alipay.html中你的访问域名前缀 替换xpay.exrick.cn
openAlipay.html中支付宝userId、银行卡转账信息、红包模式所需支付宝账号相关信息
qr/unipay二维码文件夹只需配置云闪付商家收款码
```
### v1.8新增银行卡转账模式
- 转银行卡模式正规个人业务没必要使用，量大怕风控者用的
- 支付宝内打开该web应用，实例：`https://ds.alipay.com/?from=pc&appId=09999988&actionType=toCard&sourceId=bill&cardNo=银行卡卡号&bankAccount=持卡人姓名&money=金额&amount=金额&bankMark=银行缩写简写&bankName=银行完整名称&tdsourcetag=s_pctim_aiomsg` url中文记得需经过encode编码
- v1.8增加需要替换的地方：
```
alipay.html中你的访问域名前缀 替换xpay.exrick.cn
openAlipay.html中的银行卡转账信息
```
### v1.7支付宝转账码原理

- 拉起支付宝APP借助[支付宝H5 JSAPI](http://myjsapi.alipay.com/jsapi/index.html)，先打开指定网页，url需经过encode编码，例如 alipays://platformapi/startapp?appId=20000067&url=http%3A%2F%2Fm.taobao.com
- 无法修改金额转账应用，实例：`alipays://platformapi/startapp?appId=20000123&actionType=scan&biz_data={"s": "money","u": "你的支付宝userId","a": "金额","m": "备注"}`,
- v1.7需要替换的地方：
```
alipay.html中的访问域名如xpay.exrick.cn和支付宝userId
openAlipay.html中的自定义金额收款码
```

### v1.6支付宝转账码原理
- 示例： `alipays://platformapi/startapp?appId=09999988&actionType=toAccount&goBack=NO&userId=你的支付宝userId&amount=金额&memo=备注`
- userId获取：请进入 蚂蚁金服开放平台官网 登陆后 点击右上角进入账户管理或账户信息 在合作伙伴管理下方即可找到你的角色身份PID那串数字即为你的userId
- 通过scheme启动 scheme可以理解为一种特殊的URI，格式与URI相同 支付宝客户端的标准scheme为：alipays://platformapi/startapp?appId=
即为H5App自身的appId，但如果是某些运营页之类的单独页面，没有自己的appId，可以使用Nebula容器的通用浏览器模式appId=20000067 来启动，同时将需要打开的H5页面url经过encode编码后设置到url参数内，例如：alipays://platformapi/startapp?appId=20000067&url=http%3A%2F%2Fm.taobao.com
- 因此其中`appId=09999988`为支付宝内转账码H5应用，不得修改
- <a href='alipays://platformapi/startapp?appId=09999988&actionType=toAccount&goBack=NO&userId=2088012242122163&amount=66.66&memo=测试' target='_blank' class='btn btn-danger m-top-20'>测试一键打开支付宝APP支付</a>
- 若转账码被封将退回v1.5版本 请各位做好被封心理准备

### v1.5支付宝风控解决方案
- 固码收款将非常容易触发风控，因此废弃固码，仅支持自定义金额输入，由于xpay天生的“人工智障”检测优势，支持用户自定义金额输入(要求用户输入订单备注)

### v1.2升级原理说明
- 创建多张同金额不同备注的收款码，支付时挨个递增选取，实现订单支付标识，添加的越多，越能实现多人短时间内同时支付。
    - 配置二维码数量数在`application.properties`中修改，二维码配置在`src\main\resources\static\assets\qr`文件内，具体支付宝支付为"alipay"文件夹，"1.00元"分为单个"1.00"文件夹，其中多个图片命名由"1"递增，订单备注需和"1"相同或者设置为你能识别对应的，图格式为".png"，图片数不得少于你在`application.properties`中配置的
- 自定义金额收款需用户输入系统自动生成的四位数随机码，实现订单支付标识，图片名为"custom.png"
- 一键打开支付宝App配置（支持安卓浏览器、不支持微信）【固码已凉 仅支持自定义码】
    - 将生成的支付宝收款码解析链接后放入href即可 例如：HTTPS://QR.ALIPAY.COM/FKX05348YGHADA5W9JJV66，具体见下面
    - `<a href='HTTPS://QR.ALIPAY.COM/FKX05348YGHADA5W9JJV66' target='_blank'>一键打开支付宝APP支付</a>`
    - 具体页面中取链接配置参考`alipay.html`页面js代码

### 本地开发运行部署
- Maven项目，不会请百度。安装完Maven后根目录下执行  mvn install
- 中间件依赖：[Redis](https://github.com/Exrick/xmall/blob/master/study/Redis.md)(必须)
- 新建`xpay`数据库，已开启自动生成数据库表，运行项目后即自动创建，也可以导入sql文件
- 在 `application.properties` 中修改你的配置，例如端口、数据库、Redis、邮箱配置等，其中有详细注释
- 运行 `XpayApplication.java`
- 访问默认端口8888：http://localhost:8888
### 部署
- 根目录下执行 mvn package 命令 找到target文件夹下生成的jar文件 windows下双击即可运行 
### Linux后台运行示例
`nohup java -jar xpay-1.0-SNAPSHOT.jar -Xmx128m &`

### 技术疑问交流
- 交流群 `475743731`
- 作者博客：[http://blog.exrick.cn](http://blog.exrick.cn)

