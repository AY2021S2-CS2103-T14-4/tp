package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.ReadOnlyTaskTracker;
import seedu.address.model.TaskTracker;
import seedu.address.model.person.DeadlineDate;
import seedu.address.model.person.DeadlineTime;
import seedu.address.model.person.ModuleCode;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Status;
import seedu.address.model.person.Task;
import seedu.address.model.person.TaskName;
import seedu.address.model.person.Weightage;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code TaskTracker} with sample data.
 */
public class SampleDataUtil {

    public static final Remark EMPTY_REMARK = new Remark("");

    public static Task[] getSampleTasks() {
        return new Task[]{
            new Task(new TaskName("Week 10 Quiz"), new ModuleCode("CS2103"),
                new DeadlineDate("01-04-2021"), new DeadlineTime("10:10"),
                new Status(), new Weightage(0),
                EMPTY_REMARK, getTagSet("core")),
            new Task(new TaskName("Take Home lab 1"), new ModuleCode("CS2040"),
                new DeadlineDate("10-04-2021"), new DeadlineTime("10:10"),
                new Status(), new Weightage(10),
                EMPTY_REMARK, getTagSet("core", "difficult")),
            new Task(new TaskName("Tutorial 6"), new ModuleCode("CS1010"),
                new DeadlineDate("31-03-2021"), new DeadlineTime("10:10"),
                new Status("Finished"), new Weightage(20),
                EMPTY_REMARK, getTagSet("core")),
            new Task(new TaskName("Tutorial 5"), new ModuleCode("CS2030"),
                new DeadlineDate("30-04-2021"), new DeadlineTime("10:10"),
                new Status(), new Weightage(10),
                EMPTY_REMARK, getTagSet("core")),
            new Task(new TaskName("Weekly Readings"), new ModuleCode("CS3243"),
                new DeadlineDate("12-05-2021"), new DeadlineTime("10:10"),
                new Status(), new Weightage(20),
                EMPTY_REMARK, getTagSet("specialization")),
            new Task(new TaskName("Write Tests"), new ModuleCode("CS3244"),
                new DeadlineDate("07-04-2021"), new DeadlineTime("10:10"),
                new Status("Finished"), new Weightage(15),
                EMPTY_REMARK, getTagSet("specialization"))
        };
    }


    public static ReadOnlyTaskTracker getSampleTaskTracker() {
        TaskTracker sampleAb = new TaskTracker();

        for (Task sampleTask : getSampleTasks()) {
            sampleAb.addTask(sampleTask);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
            .map(Tag::new)
            .collect(Collectors.toSet());
    }

}
