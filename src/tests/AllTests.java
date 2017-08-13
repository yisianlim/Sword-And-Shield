package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * If you run this file as a JUnit test, it automatically runs all the tests.
 */

@RunWith(Suite.class)
@SuiteClasses({BoardTest.class, GameTest.class, PieceTest.class})
public class AllTests {

}
