# Project Kinsler:KiliKili-A BiliBili Client For J2ME

## J2ME平台的哔哩哔哩客户端

使用JDK1.8+Eclipse2.2开发，实机为Nokia E72测试，目前仍处于开发阶段

### 感谢

* [SocialSisterYi/bilibili-API-collect: 哔哩哔哩-API收集整理【不断更新中....】](https://github.com/SocialSisterYi/bilibili-API-collect)
* [有关bilibili免登录获取高画质mp4直链的研究 - 赵苦瓜のBlog](https://blog.jixiaob.cn/?post=113)

希望各位给出建议，可以在dospy论坛上找到我：[jiehuan的个人资料 - DOSPY论坛 诺亚方舟号 - 塞班论坛|固件下载|hmd诺基亚安卓|智能手机|数码科技 -](https://www.dospy.wang/space-uid-45511.html)
或者给我发邮件：jiehuan233[AT]outlook.com jiehuan233[AT]gmail.com

## TODO

- [x] 由bvid跳转视频页面

- [x] 视频标题+up+封面+简介

- [x] 视频数据

- [ ] 视频评论

- [x] 视频下载

- [x] 推荐视频

- [ ] 视频伴音转码

- [ ] 多p视频获取

- [x] up主个人信息

- [ ] 个人空间视频获取

## 测试

#### 模拟器

WTK2.2 获取信息和封面正常 视频下载链接解析错误

KEmulator-v1.0.5 功能完全正常

#### 真机

Nokia E72 v0.2正式版 功能完全正常

Nokia C5-03 功能完全正常 v0.1 感谢dospy@sky161

Nokia N95 无法直接下载视频 v0.2beta3 感谢dospy@家养的大白鹅

索尼爱立信 M1i（WM6.5）功能正常 v0.2beta2 感谢DCMS@8192Bit

Nokia E7 功能完全正常 v0.2正式版 感谢dospy@肆玖叁拾陆

### 关于播放器

#### S60V3

经开发者测试：

测试发现coreplayer播放仅有图像，流畅；xplore自带的播放功能仅有声音；rushplayer有声音和图像，图像卡顿严重；UC影音无法播放

#### S60V5

经dospy@lisiqi2021测试：

UC影音似乎可以播放声音和图像，略微卡顿（该项目前不确定）

#### Symbian^3

经dospy@肆玖叁拾陆测试：

UC影音正常播放图像和声音；自带播放器只有音频

## 开发环境部署

采用的JAVA版本为JDK1.8_202，采用的IDE是Eclipse，版本为Juno Service Release 2，配合EclipseME1.7.9使用，模拟器采用的是WTK2.2和S60 FP2 SDK中的模拟器，具体开发环境的安装和配置请参阅[Windows系统搭建塞班Java开发环境教程 - 塞班论坛 (独立讨论区) - DOSPY论坛 诺亚方舟号 - 塞班论坛|固件下载|hmd诺基亚安卓|智能手机|数码科技 -](https://www.dospy.wang/thread-15819-1-1.html)

该教程中的eclipse版本与本项目采用的不同，本项目的Eclipse下载地址为[Juno R | Eclipse Packages](https://www.eclipse.org/downloads/packages/release/juno/r)中的Eclipse classic
