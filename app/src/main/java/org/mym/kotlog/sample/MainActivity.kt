package org.mym.kotlog.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.mym.kotlog.L

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        L.v("Hello World!")
        L.d("Hello World!")
        L.i("Hello World!")
        L.w("Hello World!")
        L.e("Hello World!")

        L.d("A complicated log in ${MainActivity::class.simpleName} a")

        L.objects(123, "String", null, StubEntity())

        L.uiLog("This is a log printed via a wrapper method")
    }
}
