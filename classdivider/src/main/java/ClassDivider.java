
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

        if (groupSize <= 0) {
            return false;
        }

        if (deviation >= groupSize || deviation < 0) {
            return false;
        }

        if (klas.size() % groupSize != 0) {
            return false;
        }

        if (klas.size() < groupSize) {
            return false;
        }

        if (klas.size() - groupSize > deviation) {
            return false;
        }

        if (groupSize - deviation > klas.size()) {
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

        Iterator<Student> students = klas.iterator();
        List<Group<Student>> groupSet = new ArrayList<>();

        if (!isDividable(klas, groupSize, deviation)) {
            return groupSet;
        }

        for (int g = 0; g < nrOfGroups; g++) {
            Group<Student> group = new Group<>();

            for (int size = 0; size < groupSize; size++) {
                group.add(students.next());
            }

            groupSet.add(group);
        }
        
        return groupSet;
    }

}