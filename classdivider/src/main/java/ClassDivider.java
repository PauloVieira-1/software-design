import java.util.Set;
import java.util.Iterator;
import java.util.HashSet;

/**
 * Provides functionality to divide a group of students into smaller groups
 * with specified size and allowed deviation.
 */
public class ClassDivider {

    /**
     * Checks if it is possible to divide a group of students into groups of a given size
     * with a given deviation.
     *
     * @param klas      the group of students to divide
     * @param groupSize target group size
     * @param deviation permitted difference of number of students in a group and 
     * the target group size
     * @pre {@code 0 < groupSize && 0 <= deviation && 0 < klas.size()}
     * @return {@code true} if the division is possible, {@code false} otherwise.
     */
    public boolean isDividable(Group<Student> klas, int groupSize, int deviation) {
        if (groupSize <= 0 || klas.size() <= 0) {
            return false;
        }

        int totalStudents = klas.size();
        int minSize = groupSize - deviation;
        int maxSize = groupSize + deviation;

        for (int groupCount = 1; groupCount <= totalStudents; groupCount++) {
            int minTotal = getMinTotal(groupCount, minSize);
            int maxTotal = getMaxTotal(groupCount, maxSize);

            if (totalStudents >= minTotal && totalStudents <= maxTotal) {
                return true;
            }
        }

        return false;
    }

    private int getMinTotal(int groupCount, int minSize) {
        return groupCount * minSize;
    }

    private int getMaxTotal(int groupCount, int maxSize) {
        return groupCount * maxSize;
    }

    /**
     * Divides a group of students into groups of a given size with a given deviation.
     *
     * @param klas      the group of students to divide
     * @param groupSize target group size
     * @param deviation permitted difference of number of students in a group 
     * and the target group size
     * @pre {@code 0 < groupSize && 0 <= deviation && 0 < klas.size()}
     * @return a set of groups of students.
     */
    public Set<Group<Student>> divide(Group<Student> klas, int groupSize, int deviation) {
        Set<Group<Student>> groupSet = createEmptyGroup();

        if (!isDividable(klas, groupSize, deviation)) {
            return groupSet;
        }

        Iterator<Student> students = klas.iterator();
        int totalSize = klas.size();
        int nrOfGroups = totalSize / groupSize;
        int remainingStudents = totalSize % groupSize;

        for (int g = 0; g < nrOfGroups; g++) {
            int currentGroupSize = groupSize;
            if (remainingStudents-- > 0) {
                currentGroupSize += 1;
            }

            Group<Student> group = new Group<>();
            for (int size = 0; size < currentGroupSize; size++) {
                group.add(students.next());
            }

            groupSet.add(group);
        }

        return groupSet;
    }

    /**
     * Creates an empty set of student groups.
     *
     * @return an empty set of groups.
     */
    private Set<Group<Student>> createEmptyGroup() {
        return new HashSet<>();
    }
}
