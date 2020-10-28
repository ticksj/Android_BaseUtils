[TOC]

## 编码相关：Utils-Code 

### MD5Utils

> - String MD5(String string)
> - String getFileMD5(File file)

## 文件相关：Utils-file

### FileUtils

> - 删除文件 boolean deleteFile(File file) 
> - 查找文件 List<File> getFileList(File file) 
> - 过滤查找文件 List<File> getFileList(File file, String endWith)

### ZipUtils

> 

## 网络相关：Utils-Network 

## Android系统相关：Utils-System 

### AndroidSystemUtils

> - int getCurrentSDKVersion(Application app) 判断当前App TargetVersion
> - int getCurrentSDKVersion() 判断当前设备SDK版本
> - boolean isApkInDebug(Application app)  判断当前应用是否是debug状态

## UI相关：Utils-UI

### ColorUtils

> - MyColor getRandomColor()
> - List<String> getColors() 
> - List<String> getColors(int length)

## 数值相关：Utils-Number

### DoubleUtils

> - String getFormatDouble(double d) 保留两位有效位数
> - String getFormatDouble(double d,int decimalLength) 保留固定位数

## 记录相关：Utils-Record

### LogUtils

> 1. Debug模式下输出
> 2. 可默认配置或不配置TAG
>
> - isDebug
> - void e(String msg)
> - v(String TAG, String msg) 

