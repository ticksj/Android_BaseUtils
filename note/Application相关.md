判断当前App版本大于某版本

```java
if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
}
```

判断当前App版本TargetVersion

```java
int targetSdkVersion = getApplicationInfo().targetSdkVersion;
```
