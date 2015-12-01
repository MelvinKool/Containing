package org.nhl.containing;

import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Basic empty test template for unit tests.
 */
public class TemplateTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Code executed before the first test method
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        // Code executed after the last test method
    }

    @Before
    public void setUp() throws Exception {
        // Code executed before each test
    }

    @Test
    public void testThing() {
        // Test a method
    }

    @After
    public void tearDown() throws Exception {
        // Code executed after each test
    }
}
