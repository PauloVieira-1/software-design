import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Iterator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test ClassDivider.
 */
public class ClassDividerTest {

    static final ClassDivider SUT = new ClassDivider();

    private void check(List<String> expected, List<String> actual) {
        assertEquals(expected.size(), actual.size(), "size");
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i), "element " + i);
        }
    }

    private Group<Student> createGroup(int count) {
    Group<Student> group = new Group<>();
    for (int i = 0; i < count; i++) {
        String name = "Student" + i;
        group.add(new Student(name, "Lastname" + i, "ID" + i));
    }
    return group;
}


    private List<String> extractGroupSizes(Set<Group<Student>> groups) {
        List<String> result = new ArrayList<>();
        int i = 1;
        for (Group<Student> group : groups) {
            result.add("Group " + i++ + ": " + group.size() + " students");
        }
        return result;
    }

    @Test
    void divide_empty() {
        Group<Student> klas = createGroup(0);
        Set<Group<Student>> actualGroups = SUT.divide(klas, 5, 10);
        List<String> actual = extractGroupSizes(actualGroups);

        List<String> expected = new ArrayList<>();
        check(expected, actual);
    }

    @Test
    void divide_exactGroups() {
        Group<Student> klas = createGroup(10);
        Set<Group<Student>> actualGroups = SUT.divide(klas, 5, 0);
        List<String> actual = extractGroupSizes(actualGroups);

        List<String> expected = List.of(
            "Group 1: 5 students",
            "Group 2: 5 students"
        );
        check(expected, actual);
    }

    @Test
    void divide_withDeviation() {
        Group<Student> klas = createGroup(10);
        Set<Group<Student>> actualGroups = SUT.divide(klas, 3, 1);
        List<String> actual = extractGroupSizes(actualGroups);

        List<String> expected = List.of(
            "Group 1: 4 students",
            "Group 2: 3 students",
            "Group 3: 3 students"
        );
        check(expected, actual);
    }

    @Test
    void divide_notDividable() {
        Group<Student> klas = createGroup(10);
        Set<Group<Student>> actualGroups = SUT.divide(klas, 4, 0);
        List<String> actual = extractGroupSizes(actualGroups);

        List<String> expected = new ArrayList<>();
        check(expected, actual);
    }
}
