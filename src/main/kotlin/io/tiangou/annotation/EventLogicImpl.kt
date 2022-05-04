package io.tiangou.annotation


/**
 * EventLogic接口子类都应该被此注解标记
 * 被标记的类会自动注册到EventLogic接口伴生对象中的eventLogicList里
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class EventLogicImpl
