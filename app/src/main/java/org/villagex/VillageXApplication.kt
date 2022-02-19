package org.villagex;

import android.app.Application
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapsSdkInitializedCallback

internal class VillageXApplication : Application(), OnMapsSdkInitializedCallback {
        override fun onCreate() {
                super.onCreate()
                MapsInitializer.initialize(applicationContext, MapsInitializer.Renderer.LATEST, this)
        }

        override fun onMapsSdkInitialized(p0: MapsInitializer.Renderer) {
        }
}
