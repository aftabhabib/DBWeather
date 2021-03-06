/*
 *  Copyright (C) 2017 Darel Bitsy
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.dbeginc.dbweather

import android.os.StrictMode
import com.crashlytics.android.Crashlytics
import com.dbeginc.dbweather.di.components.ApplicationComponent
import com.dbeginc.dbweather.di.components.DaggerApplicationComponent
import com.dbeginc.dbweather.utils.services.DBWeatherJobCreator
import com.dbeginc.dbweather.utils.services.NewsSyncJob
import com.dbeginc.dbweather.utils.services.WeatherSyncJob
import com.evernote.android.job.JobManager
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.DaggerApplication
import io.fabric.sdk.android.Fabric

/**
 * Created by Darel Bitsy on 24/04/17.
 *
 * Class representing the Application class
 */
class DBWeatherApp : DaggerApplication(), HasActivityInjector {
    companion object {
        @Volatile var applicationGraph: ApplicationComponent? = null
    }

    override fun onCreate() {
        super.onCreate()

        Fabric.with(this, Crashlytics())

        AndroidThreeTen.init(this)

        FirebaseApp.initializeApp(this)

        MobileAds.initialize(this, BuildConfig.MobileAds)

        if (BuildConfig.DEBUG) {
            StrictMode.noteSlowCall("dbweather")

            StrictMode.setThreadPolicy(
                    StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyLog()
                            .build()
            )

            StrictMode.setVmPolicy(
                    StrictMode.VmPolicy.Builder()
                            .setClassInstanceLimit(MainActivity::class.java, 2)
                            .detectAll()
                            .penaltyLog()
                            .build()
            )
        }

        JobManager.create(this)
                .addJobCreator(DBWeatherJobCreator()) /* Running after strict mode to catch mistakes */
                .also {
                    if (BuildConfig.DEBUG) {
                        WeatherSyncJob.scheduleForNow()
                        NewsSyncJob.scheduleForNow()

                    } else {
                        WeatherSyncJob.schedule()
                        NewsSyncJob.schedule()
                    }
                }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication>? {
        applicationGraph = DaggerApplicationComponent.builder().let {
            it.seedInstance(this)
            it.build()
        }

        return applicationGraph
    }
}
