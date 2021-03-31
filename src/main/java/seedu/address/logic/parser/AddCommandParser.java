package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITYTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEIGHTAGE;

import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
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
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap =
            ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_CODE, PREFIX_WEIGHTAGE,
                PREFIX_DEADLINE_DATE, PREFIX_DEADLINE_TIME, PREFIX_TAG, PREFIX_PRIORITYTAG);

        // weightage is compulsory for now
        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_CODE,
                PREFIX_DEADLINE_DATE, PREFIX_DEADLINE_TIME, PREFIX_WEIGHTAGE)
            || !argMultimap.getPreamble().isEmpty()) {


            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        TaskName taskName = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        ModuleCode moduleCode = ParserUtil.parseCode(argMultimap.getValue(PREFIX_CODE).get());
        Weightage weightage = ParserUtil.parseWeightage(argMultimap.getValue(PREFIX_WEIGHTAGE).get());
        DeadlineDate deadlineDate = ParserUtil.parseDeadlineDate(argMultimap
            .getValue(PREFIX_DEADLINE_DATE).get());
        DeadlineTime deadlineTime = ParserUtil.parseDeadlineTime(argMultimap
            .getValue(PREFIX_DEADLINE_TIME).get());
        Status status = new Status();
        Notes notes = new Notes(""); // add command does not allow adding remarks straightaway
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        // if ptag is present
        if (arePrefixesPresent(argMultimap, PREFIX_PRIORITYTAG)) {
            PriorityTag priorityTag = ParserUtil.parsePriorityTag(argMultimap.getValue(PREFIX_PRIORITYTAG).get());
            System.out.println(priorityTag.getTagName());
            Task task = new Task(taskName, moduleCode, deadlineDate,
                    deadlineTime, status, weightage, notes, tagList, priorityTag);
            return new AddCommand(task);
        } else {
            Task task = new Task(taskName, moduleCode, deadlineDate,
                    deadlineTime, status, weightage, notes, tagList);
            return new AddCommand(task);
        }

    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
