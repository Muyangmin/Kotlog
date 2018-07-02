## Kotlog [![](https://jitpack.io/v/Muyangmin/Kotlog.svg)](https://jitpack.io/#Muyangmin/Kotlog) [![](https://img.shields.io/badge/Dokka-Full-brightgreen.svg)](https://muyangmin.github.io/Kotlog/org.mym.kotlog/index.html)

### [中文文档](./README_zh.md)
A log library specially designed for Kotlin scenarios in Android development. It's inspired by [Android-PLog](https://github.com/JumeiRdGroup/Android-PLog), but thanks to Kotlin, the api becomes more graceful and effective.

#### [Full Dokka docs](https://muyangmin.github.io/Kotlog/org.mym.kotlog/index.html)

#### Guide
##### Step 1: Dependency
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
		implementation 'com.github.Muyangmin:Kotlog:1.0.0'
	}
```
##### Step 1: Initializing
In you Application#onCreate():
```
    L.install();
```
It is strongly recommended to add extra options all you need here, though you can add them at any time.
##### Hello World
```kotlin
L.v("Hello World!")
```
#### Advanced
##### Add Interceptor
There are 2 types of interceptors: appInterceptor and logInterceptor. Both of them are `Interceptor` interface, but usage is different: appInterceptor only proceed origin log request, while logInterceptor only proceed decorated(e.g. added log tag, or line number) log request.
```kotlin
L.addApplicationInterceptor({ !BuildConfig.DEBUG })
``` 
##### Customize Printers
A builtin `DebugPrinter` can be used to print to logcat. If you wants another output, just implement `Printer` interface, add call `L.addPrinter(it)`.

**Warning: calling `L.v/d/i/w/e/objects()` methods is a common bug, it leads to infinite loops.**