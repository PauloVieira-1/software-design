import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * A JUnit test case class.
 * Every method starting with the word "test" will be called when running
 * the test with JUnit.
 *
<!--//# BEGIN TODO: Name, student ID, and date-->
 
    Name: Paulo Vieira 
    Student ID: 1798618
    Date: 2025-05-05

<!--//# END TODO-->
 */
public class CandyTest {

    static final Candy SUT = new Candy(); 

    static final long MAX_VALUE = 999999999999999999L;

    private void check(long k, long c, boolean expected) {
        System.out.println("divide(" + k + ", " + c + ")");
        long result = SUT.divide(k, c);
        System.out.println("  result = " + result);
        assertEquals(expected, 0 <= result, "possible (0 <= result)");
        if (0 <= result) {
            assertTrue(result <= MAX_VALUE, "range (result <= MAX_VALUE)");
            assertEquals(result * k, c, "quotient (result * k == c)");
        }
    }

    // Test cases

    @Test
    public void testDivideGivenExample() {
        check(3, 15, true);
    }

//# BEGIN TODO: Additional test cases
    @Test
    public void testDivideOneKid() {
        check(1, MAX_VALUE, true);
    }

    @Test
    public void testDivideOneCandy() {
        check(MAX_VALUE, 1, false);
    }

    @Test
    public void testDivideZeroKids() {
        check(0, 1, false);
    }

    @Test
    public void testDivideZeroCandies() {
        check(1, 0, true);
    }
//# END TODO

}
