package ru.egoncharovsky.words

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import mu.KotlinLogging

object ActivityLifecycleTracer :
    FragmentManager.FragmentLifecycleCallbacks(), 
    Application.ActivityLifecycleCallbacks {

    private val logger = KotlinLogging.logger { }

    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
        logger.trace("$activity ActivityCreated")
    }

    override fun onActivityStarted(activity: Activity) {
        logger.trace("$activity ActivityStarted")
    }

    override fun onActivityResumed(activity: Activity) {
        logger.trace("$activity ActivityResumed")
    }

    override fun onActivityPaused(activity: Activity) {
        logger.trace("$activity ActivityPaused")
    }

    override fun onActivityStopped(activity: Activity) {
        logger.trace("$activity ActivityStopped")
    }

    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
        logger.trace("$activity ActivitySaveInstanceState")
    }

    override fun onActivityDestroyed(activity: Activity) {
        logger.trace("$activity ActivityDestroyed")
    }

}

object FragmentLifecycleTracer : FragmentManager.FragmentLifecycleCallbacks() {

    private val logger = KotlinLogging.logger { }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        logger.trace("$f FragmentCreated")
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        logger.trace("$f FragmentStarted")
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        logger.trace("$f FragmentResumed")
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        logger.trace("$f FragmentPaused")
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        logger.trace("$f FragmentStopped")
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        logger.trace("$f FragmentSaveInstanceState")
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        logger.trace("$f FragmentDestroyed")
    }
}