package com.weweb;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseTest {

    protected static final DecimalFormat TIME_FORMAT = new DecimalFormat("####,###.###",
            new DecimalFormatSymbols(Locale.US));

    private static final String TEMP_DIR = "./target/junit-temp/" + System.currentTimeMillis();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Rule
    public TestName name = new TestName();

    private long testStartDate;

    @Before
    public final void setUpBeforeTest() {
        testStartDate = System.currentTimeMillis();
        logger.info("===================================================================");
        logger.info("BEGIN TEST " + name.getMethodName());
        logger.info("===================================================================");

    }

    @After
    public final void tearDownAfterTest() {
        final long executionTime = System.currentTimeMillis() - testStartDate;
        logger.info("===================================================================");
        logger.info("END TEST " + name.getMethodName());
        logger.info("execution time: " + TIME_FORMAT.format(executionTime) + " ms");
        logger.info("===================================================================");
    }

    protected Logger getLogger() {
        return logger;
    }

    protected String getTempDirectory() {
        new File(TEMP_DIR).mkdirs();
        return TEMP_DIR;
    }

}
