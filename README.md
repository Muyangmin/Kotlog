## Kotlog [![](https://jitpack.io/v/Muyangmin/Kotlog.svg)](https://jitpack.io/#Muyangmin/Kotlog) [![](https://img.shields.io/badge/Dokka-Full-brightgreen.svg)](https://muyangmin.github.io/Kotlog/org.mym.kotlog/index.html)

A log library specially designed for Kotlin scenarios in Android development. It's inspired by [Android-PLog](https://github.com/JumeiRdGroup/Android-PLog).

#### [Full Dokka docs](https://muyangmin.github.io/Kotlog/org.mym.kotlog/index.html)

#### Dependency
##### Using jcenter
```groovy
	dependencies {
		implementation 'org.mym.kotlog:kotlog:$latest_version'
	}
```

##### Using [jitPack](https://jitpack.io)
```groovy
	dependencies {
		implementation 'com.github.Muyangmin:Kotlog:1.1.0'
	}
```

#### Usage
##### Initializing
In you Application#onCreate():
```
    L.install();
```
If you forget to call `install()` method, you will receive an Exception at runtime.
##### Hello World
```kotlin
L.v("Hello World!")
```
#### Advanced
##### Extend L class api
For example to simplify group usage:
```kotlin
fun L.logLifecycle(msg: String?) = d(msg) {
    group = "Lifecycle"
    stackOffset = 1
}
```

##### Add Interceptor
There are 2 types of interceptors: appInterceptor and logInterceptor. Both of them are `Interceptor` interface, but usage is different: appInterceptor only proceed origin log request, while logInterceptor only proceed decorated(e.g. added log tag, or line number) log request.
```kotlin
L.addApplicationInterceptor({ !BuildConfig.DEBUG })
``` 
##### Customize Printers
A builtin `DebugPrinter` can be used to print to logcat. If you wants another output, just implement `Printer` interface, add call `L.addPrinter(it)`.

**Warning: calling `L.v/d/i/w/e/objects()` methods is a common bug, it leads to infinite loops.**