## Kotlog [![](https://jitpack.io/v/Muyangmin/Kotlog.svg)](https://jitpack.io/#Muyangmin/Kotlog) [![](https://img.shields.io/badge/Dokka-Full-brightgreen.svg)](https://muyangmin.github.io/Kotlog/org.mym.kotlog/index.html)

### [English version](./README.md)
专为 Kotlin 环境下开发 Android 应用设计的日志库。得益于 Kotlin 的各种实用特性，API 可以相当优雅和简洁。

#### [完整 Dokka 文档](https://muyangmin.github.io/Kotlog/org.mym.kotlog/index.html)

#### 使用指南
##### 第一步：添加依赖
In your root `build.gradle`:
```groovy
	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
```

In your `app/build.gradle`:

```groovy
	dependencies {
		implementation 'com.github.Muyangmin:Kotlog:1.1.0'
	}
```
##### 第二步：初始化
在你的 Application#onCreate() 中:
```
    L.install();
```
我们强烈推荐你在这里做必要的全部初始化动作，尽管你可以在任何时候完成。另外需要注意：如果没有调用过 `install` 方法，则对其他任何公有 api 的调用都会抛出一个异常。
##### Hello World
```kotlin
L.v("Hello World!")
```
#### 高级使用
##### 拦截器 （Interceptor）
拦截器分为两种：应用拦截器和日志拦截器。尽管它们都属于 `Interceptor` 接口，但它们的使用场景有区别：前者处理的是原始日志请求，后者处理的是被装饰（例如 tag 和 msg 被修改）后的日志请求。
```kotlin
L.addApplicationInterceptor({ !BuildConfig.DEBUG })
``` 
##### Customize Printers
内置的 `DebugPrinter` 可用于向控制台打印数据。如果你有其他打印需求，只需要实现 `Printer` 接口并调用 `L.addPrinter(it)` 即可。

**提示：在自定义 `Printer` 中调用 `L`类的日志打印系列方法是一个常见的 Bug：这会导致无限循环，因此请务必注意。**