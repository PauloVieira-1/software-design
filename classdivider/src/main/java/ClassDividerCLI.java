import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;  
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;


/**
 * ClassDivider - Divide a class of students into groups.
 *
 * @version 0.6
 * @author Huub de Beer
 */
@Command(
        name = "classdivider",
        mixinStandardHelpOptions = true,
        version = "classdivider 0.6",
        description = "Divide a class of students into groups."
)
public class ClassDividerCLI implements Callable<Integer> {

    /*
     * Size of the groups to create.
     */
    @CommandLine.Option(
            names = {"-g", "--group-size"},
            description = "Target group size.",
            required = true
    )
    private int groupSize;

    /*
     * Number of students that a group can deviate from 
     * the target group size.
     */
    @CommandLine.Option(
            names = {"-d", "--deviation"},
            description = "Permitted difference in number of students in a group "
                    + "and the target group size. Defaults to ${DEFAULT-VALUE}.")
    private int deviation = 1;

    /*
     * Path to file containing student data
     */
    @Parameters(
            index = "0",
            description = "Path to file with students data in CSV format."
    )
    private Path studentsFile;

    @Spec
    CommandSpec commandSpec; // injected by picocli

    @Override
    public Integer call() {
        Group<Student> klas = readStudentData();

        validateInput(klas);

        List<Group<Student>> groupSet = divideStudents(klas);

        Map<String, Boolean> uniqueFirstName = getUniqueFirstNames(klas);

        printGroups(groupSet, uniqueFirstName);

        return 0;
    }

    /**
     * Read student data from the CSV file.
     */
    private Group<Student> readStudentData() {
        try {
            return StudentsFile.fromCSV(studentsFile);
        } catch (IOException e) {
            throw new ParameterException(
                    commandSpec.commandLine(),
                    "Unable to open or read students file '%s': %s."
                            .formatted(studentsFile, e));
        }
    }

    /**
     * Validate the user input for group size and deviation.
     */
    private void validateInput(Group<Student> klas) {
        if (groupSize <= 0) {
            throw new ParameterException(
                    commandSpec.commandLine(),
                    "Group size must be a positive integer.");
        }

        if (deviation >= groupSize || deviation < 0) {
            throw new ParameterException(
                    commandSpec.commandLine(),
                    "Deviation must be a positive number smaller than group size.");
        }

        if (!isDividable(klas.size(), groupSize, deviation)) {
            throw new ParameterException(
                    commandSpec.commandLine(),
                    "Unable to divide a class of %d into groups of %d+/-%d students."
                            .formatted(klas.size(), groupSize, deviation)
            );
        }
    }

    /**
     * Check if it is possible to divide 
     * the class into groups.
     */
    private boolean isDividable(int total, int groupSize, int deviation) {
        int nrOfGroups = total / groupSize;
        int overflow = total % groupSize;

        boolean condition1 = overflow <= nrOfGroups * deviation;
        boolean condition2 = overflow + nrOfGroups * deviation <= groupSize + deviation;
        boolean condition3 = overflow >= groupSize - deviation || overflow <= groupSize + deviation;

        return condition1 && condition2 && condition3;
    }

    /**
     * Divide the class into groups based on the given size and deviation.
     */
    private List<Group<Student>> divideStudents(Group<Student> klas) {
        Iterator<Student> students = klas.iterator();
        List<Group<Student>> groupSet = new ArrayList<>();
        int nrOfGroups = klas.size() / groupSize;
        int overflow = klas.size() % groupSize;

        // Fill the groups with the target group size
        fillGroups(students, groupSet, nrOfGroups);

        // Handle overflow
        if (nrOfGroups / deviation > overflow) {
            distributeOverflow(students, groupSet, nrOfGroups, overflow);
        } else {
            handleSeparateGroup(students, groupSet, overflow);
        }

        return groupSet;
    }

    /**
     * Fill the groups with the target group size.
     */
    private void fillGroups(Iterator<Student> students, 
                            List<Group<Student>> groupSet, int nrOfGroups) {
        for (int g = 0; g < nrOfGroups; g++) {
            Group<Student> group = new Group<>();
            for (int size = 0; size < groupSize; size++) {
                group.add(students.next());
            }
            groupSet.add(group);
        }
    } 
    
    /**
     * Distribute overflow students to existing groups.
     */
    private void distributeOverflow(Iterator<Student> students, 
                                    List<Group<Student>> groupSet, 
                                    int nrOfGroups, 
                                    int overflow) {
        for (int d = 0; d < deviation && overflow > 0; d++) {
            for (int g = 0; g < nrOfGroups && overflow > 0; g++) {
                groupSet.get(g).add(students.next());
                overflow--;
            }
        }
    }

    /**
     * Handle separate group when overflow students cannot be distributed evenly.
     */
    private void handleSeparateGroup(Iterator<Student> students, 
                                     List<Group<Student>> groupSet, 
                                     int overflow) {
        Group<Student> separateGroup = new Group<>();
        for (int i = 0; i < overflow; i++) {
            separateGroup.add(students.next());
        }

        redistributeStudentsToSeparateGroup(groupSet, separateGroup);

        groupSet.add(separateGroup);
    }

    /**
     * Redistribute students to the separate group if necessary.
     */
    private void redistributeStudentsToSeparateGroup(List<Group<Student>> groupSet, 
                                                     Group<Student> separateGroup) {
        for (int d = 0; d < deviation && separateGroup.size() < groupSize - deviation; d++) {
            int g = groupSet.size();

            while (separateGroup.size() < groupSize - deviation) {
                g--;
                Group<Student> group = groupSet.get(g);
                Student student = group.pick();
                separateGroup.add(student);
                group.remove(student);
            }
        }
    }

    /**
     * Get a map of unique first names.
     */
    private Map<String, Boolean> getUniqueFirstNames(Group<Student> klas) {
        Map<String, Boolean> uniqueFirstName = new HashMap<>();

        for (Student student : klas) {
            if (uniqueFirstName.containsKey(student.firstName())) {
                uniqueFirstName.put(student.firstName(), false);
            } else {
                uniqueFirstName.put(student.firstName(), true);
            }
        }

        return uniqueFirstName;
    }

    /**
     * Print the group set to standard output.
     */
    private void printGroups(List<Group<Student>> groupSet, Map<String, 
                                                              Boolean> uniqueFirstName) {
        int groupNr = 0;

        for (Group<Student> group : groupSet) {
            groupNr++;
            System.out.printf("Group %d:%n", groupNr);

            for (Student student : group) {
                String name = student.firstName();
                if (!uniqueFirstName.get(student.firstName())) {
                    name += " " + student.sortName().charAt(0);
                }
                System.out.println("- " + name);
            }

            System.out.println();
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new ClassDividerCLI()).execute(args);
        System.exit(exitCode);
    }
}
