package com.itsjeel01.finsiblefrontend

import android.app.Application
import com.itsjeel01.finsiblefrontend.common.LocaleProvider
import com.itsjeel01.finsiblefrontend.common.UserLocaleRegistry
import com.itsjeel01.finsiblefrontend.common.logging.DebugLogTree
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.common.logging.ReleaseLogTree
import com.itsjeel01.finsiblefrontend.data.repository.CurrencyRepository
import com.itsjeel01.finsiblefrontend.data.sync.IntegrityResolverService
import com.itsjeel01.finsiblefrontend.data.sync.ScopeManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class FinsibleApp : Application() {
    @Inject
    lateinit var scopeManager: ScopeManager

    @Inject
    lateinit var integrityResolverService: IntegrityResolverService

    @Inject
    lateinit var localeProvider: LocaleProvider

    @Inject
    lateinit var currencyRepository: CurrencyRepository

    override fun onCreate() {
        super.onCreate()
        UserLocaleRegistry.initialize(localeProvider)
        initializeLogging()
        logResolvedLocaleAndCurrency()
        currencyRepository.initialize()
        integrityResolverService.checkAndResolveOnLaunch()
    }

    override fun onTerminate() {
        super.onTerminate()
        scopeManager.shutdown()
    }

    private fun initializeLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugLogTree())
            Logger.App.d("Debug logging enabled")
        } else {
            Timber.plant(ReleaseLogTree())
        }
    }

    private fun logResolvedLocaleAndCurrency() {
        if (!BuildConfig.DEBUG) return

        val locale = UserLocaleRegistry.currentLocale()
        val currencyCode = UserLocaleRegistry.currentGeographicCurrencyCode() ?: "null"
        Logger.App.d(
            "Resolved locale/currency at startup: locale=${locale.toLanguageTag()}, country=${locale.country}, currency=$currencyCode"
        )
    }
}
