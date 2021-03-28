package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_TASKS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalTasks.CS1010E;
import static seedu.address.testutil.TypicalTasks.CS2030;
import static seedu.address.testutil.TypicalTasks.CS2040;
import static seedu.address.testutil.TypicalTasks.CS2100;
import static seedu.address.testutil.TypicalTasks.CS2103;
import static seedu.address.testutil.TypicalTasks.CS3243;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskTracker;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.DeadlineDate;
import seedu.address.model.person.DeadlineDateInRangePredicate;
import seedu.address.model.person.DeadlineTime;
import seedu.address.model.person.Task;

/**
 * Contains integration tests (interaction with the Model) for {@code DueInCommand}.
 */
public class DueInCommandTest {
    private static long numberOfDays = 9;
    private static long numberOfWeeks = 1;

    private Model model = new ModelManager(getTypicalTaskTracker(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalTaskTracker(), new UserPrefs());

    @Test
    public void equals() {
        DeadlineDateInRangePredicate firstPredicate =
                new DeadlineDateInRangePredicate(numberOfDays);
        DeadlineDateInRangePredicate secondPredicate =
                new DeadlineDateInRangePredicate(numberOfWeeks * 7);

        DueInCommand dueInFirstCommand = new DueInCommand(firstPredicate);
        DueInCommand dueInSecondCommand = new DueInCommand(secondPredicate);

        // same object -> returns true
        assertTrue(dueInFirstCommand.equals(dueInFirstCommand));
        assertTrue(dueInSecondCommand.equals(dueInSecondCommand));

        // same values -> returns true
        DueInCommand dueInFirstCommandCopy = new DueInCommand(firstPredicate);
        assertTrue(dueInFirstCommand.equals(dueInFirstCommandCopy));
        DueInCommand dueInSecondCommandCopy = new DueInCommand(secondPredicate);
        assertTrue(dueInSecondCommand.equals(dueInSecondCommandCopy));

        // different types -> returns false
        assertFalse(dueInFirstCommand.equals(1)); //integer
        assertFalse(dueInSecondCommand.equals(1));
        assertFalse(dueInFirstCommand.equals("aiue")); //string
        assertFalse(dueInSecondCommand.equals("aiue"));
        assertFalse(dueInFirstCommand.equals(new Object())); //object
        assertFalse(dueInSecondCommand.equals(new Object()));
        assertFalse(dueInFirstCommand.equals(new DeadlineDate("10-11-2022"))); //DeadlineDate
        assertFalse(dueInSecondCommand.equals(new DeadlineDate("10-11-2022")));
        assertFalse(dueInFirstCommand.equals(new DeadlineTime("10:11"))); //DeadlineTime
        assertFalse(dueInSecondCommand.equals(new DeadlineTime("10:11")));

        // null -> returns false
        assertFalse(dueInFirstCommand.equals(null));
        assertFalse(dueInSecondCommand.equals(null));

        // different values -> returns false
        assertFalse(dueInFirstCommand.equals(dueInSecondCommand));
    }

    @Test
    public void execute_noParamsGiven_noTaskFound() {
        DeadlineDateInRangePredicateStub predicate = new DeadlineDateInRangePredicateStub(); //stub
        String expectedMessage = String.format(MESSAGE_TASKS_LISTED_OVERVIEW, 0);
        DueInCommand command = new DueInCommand(predicate);
        expectedModel.updateFilteredTaskList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredTaskList());
    }

    @Test
    public void execute_numberOfDaysGiven_multipleTasksFound() {
        DeadlineDateInRangePredicate predicate =
                new DeadlineDateInRangePredicate(numberOfDays);
        String expectedMessage = String.format(MESSAGE_TASKS_LISTED_OVERVIEW, 6);
        DueInCommand command = new DueInCommand(predicate);
        expectedModel.updateFilteredTaskList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CS2103, CS2040, CS1010E, CS2030, CS3243, CS2100), model.getFilteredTaskList());
    }

    @Test
    public void execute_numberOfWeeksGiven_multipleTasksFound() {
        DeadlineDateInRangePredicate predicate =
                new DeadlineDateInRangePredicate(numberOfWeeks * 7);
        String expectedMessage = String.format(MESSAGE_TASKS_LISTED_OVERVIEW, 6);
        DueInCommand command = new DueInCommand(predicate);
        expectedModel.updateFilteredTaskList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CS2103, CS2040, CS1010E, CS2030, CS3243, CS2100), model.getFilteredTaskList());
    }

    private class DeadlineDateInRangePredicateStub extends DeadlineDateInRangePredicate {

        public DeadlineDateInRangePredicateStub() {
            super(1); // Random number yields same result
        };

        @Override
        public boolean test(Task task) {
            return false;
        }
    }
}
