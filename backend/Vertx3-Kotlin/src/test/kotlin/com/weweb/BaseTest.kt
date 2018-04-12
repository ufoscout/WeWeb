package com.weweb

import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import java.io.File
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestName

abstract class BaseTest {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Rule @JvmField
    val name = TestName()

    private var testStartDate: Long = 0

    protected val tempDirectory: String
        get() {
            File(TEMP_DIR).mkdirs()
            return TEMP_DIR
        }

    @Before
    fun setUpBeforeTest() {
        testStartDate = System.currentTimeMillis()
        logger.info("===================================================================")
        logger.info("BEGIN TEST " + name.methodName)
        logger.info("===================================================================")

    }

    @After
    fun tearDownAfterTest() {
        val executionTime = System.currentTimeMillis() - testStartDate
        logger.info("===================================================================")
        logger.info("END TEST " + name.methodName)
        logger.info("execution time: " + TIME_FORMAT.format(executionTime) + " ms")
        logger.info("===================================================================")
    }

    protected fun getLogger(): Logger {
        return logger
    }

    companion object {

        protected val TIME_FORMAT = DecimalFormat("####,###.###",
                DecimalFormatSymbols(Locale.US))

        private val TEMP_DIR = "./target/junit-temp/" + System.currentTimeMillis()
    }

}
