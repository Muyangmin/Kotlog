### 2.0.0
* Fully Kotlin-oriented `L` class api, Java support is no longer in plan
* log groups now are also printed
* More concise and idiom Kotlin support

##### Breaking change
* `Decorator` are a real interface(not just type alias). Original `invoke` method changed to `decorate`.
* `L.addDecorator` now only accept single parameter. For adding multiple decorators please use `L.addDecorators()`
* To simplify setting options, `L.install()` method now has only 2 arguments.
* `L.v/d/i/w/e/objects` methods signature changed to Kotlin style with functional blocks.

### 1.1.0 
Stable release in 2018. Kotlin/Java mixed support.