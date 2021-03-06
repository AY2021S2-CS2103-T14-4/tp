package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITYTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.DeadlineDate;
import seedu.address.model.person.DeadlineTime;
import seedu.address.model.person.ModuleCode;
import seedu.address.model.person.Notes;
import seedu.address.model.person.Status;
import seedu.address.model.person.Task;
import seedu.address.model.person.TaskName;
import seedu.address.model.person.Weightage;
import seedu.address.model.tag.PriorityTag;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing task in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the task identified "
        + "by the index number used in the All tasks list. "
        + "Existing values will be overwritten by the input values.\n"
        + "Parameters: INDEX (must be a positive integer) "
        + "[" + PREFIX_NAME + "NAME] "
        + "[" + PREFIX_CODE + "CODE] "
        + "[" + PREFIX_DEADLINE_DATE + "DEADLINE_DATE] "
        + "[" + PREFIX_DEADLINE_TIME + "DEADLINE_TIME] "
        + "[" + PREFIX_NOTES + "NOTES] "
        + "[" + PREFIX_PRIORITYTAG + "PRIORITY_TAG] "
        + "[" + PREFIX_TAG + "TAG]...\n"
        + "Example: " + COMMAND_WORD + " 1 "
        + PREFIX_NAME + "CS2103 Assignment";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Edited Task: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the address book.";

    private final Index index;
    private final EditTaskDescriptor editTaskDescriptor;

    /**
     * @param index              of the task in the filtered task list to edit
     * @param editTaskDescriptor details to edit the task with
     */
    public EditCommand(Index index, EditTaskDescriptor editTaskDescriptor) {
        requireNonNull(index);
        requireNonNull(editTaskDescriptor);

        this.index = index;
        this.editTaskDescriptor = new EditTaskDescriptor(editTaskDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();
        List<Task> lastShownDailyTaskList = model.getDailyTaskList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskToEdit = lastShownList.get(index.getZeroBased());
        int dailyTaskIndex = lastShownDailyTaskList.indexOf(taskToEdit);

        Task editedTask = createEditedTask(taskToEdit, editTaskDescriptor);

        if (!taskToEdit.isSameTask(editedTask) && model.hasTask(editedTask)) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }

        model.setTask(taskToEdit, editedTask);
        model.refreshDailyTasks(taskToEdit, editedTask);

        return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, editedTask));
    }

    /**
     * Creates and returns a {@code Task} with the details of {@code taskToEdit}
     * edited with {@code editTaskDescriptor}.
     */
    private static Task createEditedTask(Task taskToEdit, EditTaskDescriptor editTaskDescriptor) {
        assert taskToEdit != null;

        TaskName updatedTaskName = editTaskDescriptor.getTaskName().orElse(taskToEdit.getTaskName());
        ModuleCode updatedModuleCode = editTaskDescriptor.getModuleCode().orElse(taskToEdit.getModuleCode());
        DeadlineDate updatedDeadlineDate = editTaskDescriptor.getDeadlineDate()
            .orElse(taskToEdit.getDeadlineDate());
        DeadlineTime updatedDeadlineTime = editTaskDescriptor.getDeadlineTime()
            .orElse(taskToEdit.getDeadlineTime());
        Status updatedStatus = taskToEdit.getStatus();
        Weightage updatedWeightage = editTaskDescriptor.getWeightage()
                .orElse(taskToEdit.getWeightage());
        Notes updatedNotes = editTaskDescriptor.getNotes().orElse(taskToEdit.getNotes());
        PriorityTag priorityTag = editTaskDescriptor.getPriorityTag().orElse(taskToEdit.getPriorityTag());
        Set<Tag> updatedTags = editTaskDescriptor.getTags().orElse(taskToEdit.getTags());

        return new Task(updatedTaskName, updatedModuleCode, updatedDeadlineDate,
            updatedDeadlineTime, updatedStatus, updatedWeightage,
                updatedNotes, updatedTags, priorityTag);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
            && editTaskDescriptor.equals(e.editTaskDescriptor);
    }

    /**
     * Stores the details to edit the task with. Each non-empty field value will replace the
     * corresponding field value of the task.
     */
    public static class EditTaskDescriptor {
        // descriptors should not be allowed to have a notes field, since editing of remarks is not supported for now
        private TaskName taskName;
        private ModuleCode moduleCode;
        private DeadlineDate deadlineDate;
        private DeadlineTime deadlineTime;
        private Weightage weightage;
        private Notes notes;
        private Set<Tag> tags;
        private Status status;
        private PriorityTag priorityTag;

        public EditTaskDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditTaskDescriptor(EditTaskDescriptor toCopy) {
            setTaskName(toCopy.taskName);
            setModuleCode(toCopy.moduleCode);
            setDeadlineDate(toCopy.deadlineDate);
            setDeadlineTime(toCopy.deadlineTime);
            setWeightage(toCopy.weightage);
            setNotes(toCopy.notes);
            setTags(toCopy.tags);
            setPriorityTag(toCopy.priorityTag);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(taskName, moduleCode,
                deadlineDate, deadlineTime, weightage, notes, tags, priorityTag);
        }

        public void setTaskName(TaskName taskName) {
            this.taskName = taskName;
        }

        public Optional<TaskName> getTaskName() {
            return Optional.ofNullable(taskName);
        }

        public void setModuleCode(ModuleCode moduleCode) {
            this.moduleCode = moduleCode;
        }

        public Optional<ModuleCode> getModuleCode() {
            return Optional.ofNullable(moduleCode);
        }

        public void setDeadlineDate(DeadlineDate deadlineDate) {
            this.deadlineDate = deadlineDate;
        }

        public Optional<DeadlineDate> getDeadlineDate() {
            return Optional.ofNullable(deadlineDate);
        }

        public void setDeadlineTime(DeadlineTime deadlineTime) {
            this.deadlineTime = deadlineTime;
        }

        public Optional<DeadlineTime> getDeadlineTime() {
            return Optional.ofNullable(deadlineTime);
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public Optional<Status> getStatus() {
            return Optional.ofNullable(status);
        }

        public void setWeightage(Weightage weightage) {
            this.weightage = weightage;
        }

        public Optional<Weightage> getWeightage() {
            return Optional.ofNullable(weightage);
        }

        public void setNotes(Notes notes) {
            this.notes = notes;
        }

        public Optional<Notes> getNotes() {
            return Optional.ofNullable(notes);
        }

        public void setPriorityTag(PriorityTag priorityTag) {
            this.priorityTag = priorityTag;
        }

        public Optional<PriorityTag> getPriorityTag() {
            return Optional.ofNullable(priorityTag);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditTaskDescriptor)) {
                return false;
            }

            // state check
            EditTaskDescriptor e = (EditTaskDescriptor) other;

            return getTaskName().equals(e.getTaskName())
                && getModuleCode().equals(e.getModuleCode())
                && getWeightage().equals(e.getWeightage())
                && getNotes().equals(e.getNotes())
                && getDeadlineDate().equals(e.getDeadlineDate())
                && getDeadlineTime().equals(e.getDeadlineTime())
                && getStatus().equals(e.getStatus())
                && getTags().equals(e.getTags())
                && getPriorityTag().equals(e.getPriorityTag());
        }

    }
}
