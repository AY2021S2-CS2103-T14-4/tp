package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.model.person.Task;
import seedu.address.model.person.UniqueTaskList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSameTask comparison)
 */

public class TaskTracker implements ReadOnlyTaskTracker {


    private final UniqueTaskList tasks;
    private final UniqueTaskList dailyTasks;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        tasks = new UniqueTaskList();
        dailyTasks = new UniqueTaskList();
    }

    public TaskTracker() {
    }

    /**
     * Creates an TaskTracker using the Tasks in the {@code toBeCopied}
     */


    public TaskTracker(ReadOnlyTaskTracker toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the task list with {@code tasks}.
     * {@code tasks} must not contain duplicate tasks.
     */
    public void setTasks(List<Task> tasks) {
        this.tasks.setTasks(tasks);
    }

    public void setDailyTasks(List<Task> dailyTasks) {
        this.dailyTasks.setTasks(dailyTasks);
    }

    /**
     * Resets the existing data of this {@code TaskTracker} with {@code newData}.
     */
    public void resetData(ReadOnlyTaskTracker newData) {
        requireNonNull(newData);

        setTasks(newData.getTaskList());
        setDailyTasks(newData.getDailyTaskList());
    }

    //// sorting operation

    /**
     * Sorts the task list with the given {@code comparator}.
     */
    public void sortTasks(Comparator<Task> comparator) {
        tasks.sort(comparator);
    }

    //// task-level operations

    /**
     * Returns true if a task with the same identity as {@code task} exists in the task tracker.
     */
    public boolean hasTask(Task task) {
        requireNonNull(task);
        return tasks.contains(task);
    }

    /**
     * Returns true if a task with the same identity as {@code task} exists in the daily task list.
     */
    public boolean hasDailyTask(Task task) {
        requireNonNull(task);
        return dailyTasks.contains(task);
    }

    /**
     * Adds a task to the address book.
     * The task must not already exist in the address book.
     */
    public void addTask(Task p) {
        tasks.add(p);
    }

    /**
     * Adds a task to the daily task list.
     * The task must not already exist in the task tracker.
     */
    public void addDailyTask(Task taskToAdd) {
        dailyTasks.add(taskToAdd);
    }

    /**
     * Replaces the given task {@code target} in the list with {@code editedTask}.
     * {@code target} must exist in the address book.
     * The task identity of {@code editedTask} must not be the same as another existing task in the address book.
     */
    public void setTask(Task target, Task editedTask) {
        requireNonNull(editedTask);

        tasks.setTask(target, editedTask);
    }

    /**
     * Replaces the given task {@code target} in the list with {@code editedTask} in the daily task list.
     * {@code target} must exist in the address book.
     * The task identity of {@code editedTask} must not be the same as another existing task in the address book.
     */
    public void setDailyTask(Task target, Task editedTask) {
        requireNonNull(editedTask);

        dailyTasks.setTask(target, editedTask);
    }


    /**
     * Removes {@code key} from this {@code TaskTracker}.
     * {@code key} must exist in the address book.
     */
    public void removeTask(Task key) {
        tasks.remove(key);
    }

    /**
     * Removes {@code key} from the daily task list.
     * {@code key} must exist in the task tracker.
     */
    public void removeDailyTask(Task key) {
        dailyTasks.remove(key);
    }
    /**
     * Finishes {@code task} from this {@code TaskTracker}.
     * {@code task} must exist in the task tracker.
     */
    public void finishTask(Task task) {
        tasks.finish(task);
    }

    public void finishDailyTask(Task task) {
        dailyTasks.finish(task);
    }

    /**
     * Refreshes the daily task list after any changes have been made.
     * {@code task} must exist in the task tracker.
     */
    public void refreshDailyTasks(Task target, Task editedTask) {
        if (dailyTasks.contains(target)) {
            dailyTasks.setTask(target, editedTask);
        }
    }

    //// util methods

    @Override
    public String toString() {
        return tasks.asUnmodifiableObservableList().size() + " tasks";
        // TODO: refine later
    }

    @Override
    public ObservableList<Task> getTaskList() {
        return tasks.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Task> getDailyTaskList() {
        return dailyTasks.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskTracker // instanceof handles nulls
                && tasks.equals(((TaskTracker) other).tasks));
    }

    @Override
    public int hashCode() {
        return tasks.hashCode();
    }


}
