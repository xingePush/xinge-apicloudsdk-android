# XG-ApiCloudSDK-Android

# 概述
- 本项目是信鸽在ApiCloudSDK发布的模块[tencentPush](https://docs.apicloud.com/Client-API/Open-SDK/tencentPush)的Android版本源码。
- 基于的信鸽SDK版本是3.2.3。

# APICloud模块开发说明

本工程是基于Eclipse开发的，请先了解APICloud的模块开发流程，参见APICloud的[模块开发指南_Android_Eclipse](https://docs.apicloud.com/Module-Dev/module-dev-guide-for-android)

## src目录
信鸽tencentPush模块的java源码，最终编译成tencentPush.jar

## assets目录
其中index.html和 html/xinge-con.html是经过修改的，就是一个简单的demo界面，不参与生成最后的模块

## libs目录
ApiCloudSDK相关的libs已经信鸽推送用到的libs

## exportmodule目录
目录下的tencentPush.zip是最后发布的模块，是tencentPush的压缩

# 运行
该工程直接运行就是一个使用APICloud的 Android App Demo。

# 导出模块

导出模块包操作关键步骤：

1. 导出所有代码与你模块相关的代码文件到jar包里。操作步骤：File -> Export ->JAR file ->勾选需要导出代码及目录，选择src目录，导出tencentPush.jar放到tencentPush的source目录下
2. 复制libs文件夹下信鸽相关的jar包（jg、mid、wup、Xg_sdk这4个jar）到source目录下
3. 从工程AndroidManifest文件中分离出你的模块所定义的任何activity、service、provider、permission等, 复制到tencentPush的res_tencentPush目录;
4. 把信鸽相关的so包复制到tencentPush的target目录下
5. 将tencentPush目录压缩成zip格式包;
