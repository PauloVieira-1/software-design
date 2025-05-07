import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test ClassDivider.
 * 
 */
public class ClassDividerTest {

    static final ClassDivider SUT = new ClassDivider();

    private void check(List<String> expected, List<String> actual) {
        assertEquals(expected.size(), actual.size(), "size");
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i), "element " + i);
        }
    }

    @Test
    void divide_empty() {
        List<String> expected = new ArrayList<>();
        List<String> actual = SUT.divide(5, 10); // Presumably returns empty result for invalid input
        check(expected, actual);
    }

    @Test
    void divide_exactGroups() {
        List<String> expected = List.of(
            "Group 1: 5 students",
            "Group 2: 5 students"
        );
        List<String> actual = SUT.divide(5, 0); // assuming total students = 10 inside ClassDivider
        check(expected, actual);
    }

    @Test
    void divide_withDeviation() {
        List<String> expected = List.of(
            "Group 1: 4 students",
            "Group 2: 3 students",
            "Group 3: 3 students"
        );
        List<String> actual = SUT.divide(3, 1); // assuming 10 students split with ±1 deviation
        check(expected, actual);
    }

    @Test
    void divide_notDividable() {
        List<String> expected = new ArrayList<>();
        List<String> actual = SUT.divide(4, 0); // e.g. 10 students can't be split in 4s
        check(expected, actual);
    }
}
