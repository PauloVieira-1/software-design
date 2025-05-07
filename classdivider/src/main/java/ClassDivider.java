import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.HashSet;


public class ClassDivider {

    /**
     * Is it possible to divide a group of students into groups of a given size
     * with a given deviation?
     *
     * @param klas the group of students to divide
     * @param groupSize target group size
     * @param deviation permitted difference of number of students in a group
     *   and the target group size.
     * @return {@code true} if the division is possible, {@code false} otherwise.
     */
    
    public boolean isDividable(Group<Student> klas, int groupSize, int deviation) { 
        
    if (groupSize <= 0 || klas.size() <= 0) {
        return false;
    }

    int totalStudents = klas.size();
    int minGroupSize = groupSize - deviation;
    int maxGroupSize = groupSize + deviation;

    if (totalStudents < minGroupSize || totalStudents > maxGroupSize) {
        return false;
    }

    return true;
}


    /**
     * Divide a group of students into groups of a given size with a given deviation.
     *
     * @param klas the group of students to divide
     * @param groupSize target group size
     * @param deviation permitted difference of number of students in a group
     *   and the target group size.
     * @return a set of groups of students.
     */

    public Set<Group<Student>> divide(Group<Student> klas, int groupSize, int deviation) {

        Set<Group<Student>> groupSet = new HashSet<>();

        if (!isDividable(klas, groupSize, deviation)) {
            return groupSet;
        }

        Iterator<Student> students = klas.iterator();
        int totalSize = klas.size();
        int nrOfGroups = totalSize / groupSize;
        int remainingStudents = totalSize % groupSize;

        for (int g = 0; g < nrOfGroups; g++) {
            int currentGroupSize = groupSize + (remainingStudents-- > 0 ? 1 : 0);
            Group<Student> group = new Group<>();

            for (int size = 0; size < currentGroupSize; size++) {
                if (students.hasNext()) {
                    group.add(students.next());
                }
            }
            groupSet.add(group);
        }

        return groupSet;
    }


}