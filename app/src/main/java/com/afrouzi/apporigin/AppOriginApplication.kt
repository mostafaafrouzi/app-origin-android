package com.afrouzi.apporigin

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.afrouzi.apporigin.data.prefs.AppLanguage
import com.afrouzi.apporigin.data.prefs.SettingsRepository
import com.afrouzi.apporigin.data.repository.PackageRepositoryImpl
import com.afrouzi.apporigin.data.source.PackageManagerDataSource
import com.afrouzi.apporigin.domain.repository.PackageRepository
import com.afrouzi.apporigin.domain.usecase.FilterAndSortAppsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AppOriginApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    lateinit var settingsRepository: SettingsRepository
        private set

    lateinit var packageRepository: PackageRepository
        private set

    val filterAndSortAppsUseCase = FilterAndSortAppsUseCase()

    override fun onCreate() {
        super.onCreate()
        settingsRepository = SettingsRepository(this)
        val dataSource = PackageManagerDataSource(this)
        packageRepository = PackageRepositoryImpl(dataSource, applicationScope)

        // Initialize application locale from settings
        applicationScope.launch {
            val settings = settingsRepository.settings.first()
            applyLanguage(settings.language)
        }
    }

    fun applyLanguage(language: AppLanguage) {
        val localeList = when (language) {
            AppLanguage.SYSTEM -> LocaleListCompat.getEmptyLocaleList()
            AppLanguage.PERSIAN -> LocaleListCompat.forLanguageTags("fa")
            AppLanguage.ENGLISH -> LocaleListCompat.forLanguageTags("en")
        }
        AppCompatDelegate.setApplicationLocales(localeList)
    }

    companion object {
        fun from(context: Context): AppOriginApplication {
            return context.applicationContext as AppOriginApplication
        }
    }
}
