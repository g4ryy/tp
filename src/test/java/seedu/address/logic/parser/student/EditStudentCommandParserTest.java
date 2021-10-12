package seedu.address.logic.parser.student;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMERGENCY_CONTACT_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.FORM_CLASS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.GENDER_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_FORM_CLASS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_GENDER_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_INVOLVEMENT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVOLVEMENT_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_MONITOR;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_REP;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMERGENCY_CONTACT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_FORM_CLASS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GENDER_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_INVOLVEMENT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_MONITOR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_REP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_STUDENT;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.descriptors.EditStudentDescriptor;
import seedu.address.logic.commands.student.EditStudentCommand;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.FormClass;
import seedu.address.model.person.Gender;
import seedu.address.model.person.Involvement;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditStudentDescriptorBuilder;

public class EditStudentCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditStudentCommand.MESSAGE_USAGE);

    private EditStudentCommandParser parser = new EditStudentCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 b/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS); // invalid phone
        assertParseFailure(parser, "1" + INVALID_EMAIL_DESC, Email.MESSAGE_CONSTRAINTS); // invalid email
        assertParseFailure(parser, "1" + INVALID_ADDRESS_DESC, Address.MESSAGE_CONSTRAINTS); // invalid address
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS); // invalid tag
        assertParseFailure(parser, "1" + INVALID_INVOLVEMENT_DESC,
                Involvement.MESSAGE_CONSTRAINTS); //invalid involvement
        assertParseFailure(parser, "1" + INVALID_FORM_CLASS_DESC,
                FormClass.MESSAGE_CONSTRAINTS); //invalid form class
        assertParseFailure(parser, "1" + INVALID_GENDER_DESC, Gender.MESSAGE_CONSTRAINTS); // invalid gender


        // invalid phone followed by valid email
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC + EMAIL_DESC_AMY, Phone.MESSAGE_CONSTRAINTS);

        // valid phone followed by invalid phone. The test case for invalid phone followed by valid phone
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, "1" + PHONE_DESC_BOB + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS);

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Person} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "1" + TAG_DESC_REP + TAG_DESC_MONITOR + TAG_EMPTY,
                Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_DESC_REP + TAG_EMPTY + TAG_DESC_MONITOR,
                Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_EMPTY + TAG_DESC_REP + TAG_DESC_MONITOR,
                Tag.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_NAME_DESC + INVALID_EMAIL_DESC + VALID_ADDRESS_AMY
                + VALID_PHONE_AMY, Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_STUDENT;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + TAG_DESC_MONITOR
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + NAME_DESC_AMY + TAG_DESC_REP + INVOLVEMENT_DESC_AMY
                + GENDER_DESC_AMY + FORM_CLASS_DESC_AMY + EMERGENCY_CONTACT_DESC_AMY;

        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withGender(VALID_GENDER_AMY).withInvolvement(VALID_INVOLVEMENT_AMY)
                .withEmergencyContact(VALID_EMERGENCY_CONTACT_AMY).withFormClass(VALID_FORM_CLASS_AMY)
                .withTags(VALID_TAG_MONITOR, VALID_TAG_REP).build();
        EditStudentCommand expectedCommand = new EditStudentCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_STUDENT;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + EMAIL_DESC_AMY;

        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_AMY).build();
        EditStudentCommand expectedCommand = new EditStudentCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_STUDENT;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY;
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditStudentCommand expectedCommand = new EditStudentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY;
        descriptor = new EditStudentDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new EditStudentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = targetIndex.getOneBased() + EMAIL_DESC_AMY;
        descriptor = new EditStudentDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build();
        expectedCommand = new EditStudentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = targetIndex.getOneBased() + ADDRESS_DESC_AMY;
        descriptor = new EditStudentDescriptorBuilder().withAddress(VALID_ADDRESS_AMY).build();
        expectedCommand = new EditStudentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // involvement
        userInput = targetIndex.getOneBased() + INVOLVEMENT_DESC_AMY;
        descriptor = new EditStudentDescriptorBuilder().withInvolvement(VALID_INVOLVEMENT_AMY).build();
        expectedCommand = new EditStudentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // form class
        userInput = targetIndex.getOneBased() + FORM_CLASS_DESC_AMY;
        descriptor = new EditStudentDescriptorBuilder().withFormClass(VALID_FORM_CLASS_AMY).build();
        expectedCommand = new EditStudentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // gender
        userInput = targetIndex.getOneBased() + GENDER_DESC_AMY;
        descriptor = new EditStudentDescriptorBuilder().withGender(VALID_GENDER_AMY).build();
        expectedCommand = new EditStudentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // emergency contact
        userInput = targetIndex.getOneBased() + EMERGENCY_CONTACT_DESC_AMY;
        descriptor = new EditStudentDescriptorBuilder().withEmergencyContact(VALID_EMERGENCY_CONTACT_AMY).build();
        expectedCommand = new EditStudentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + TAG_DESC_REP;
        descriptor = new EditStudentDescriptorBuilder().withTags(VALID_TAG_REP).build();
        expectedCommand = new EditStudentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST_STUDENT;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_AMY + ADDRESS_DESC_AMY + EMAIL_DESC_AMY
                + TAG_DESC_REP + PHONE_DESC_AMY + ADDRESS_DESC_AMY + EMAIL_DESC_AMY + TAG_DESC_REP
                + PHONE_DESC_BOB + ADDRESS_DESC_BOB + EMAIL_DESC_BOB + TAG_DESC_MONITOR;

        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_REP, VALID_TAG_MONITOR)
                .build();
        EditStudentCommand expectedCommand = new EditStudentCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_STUDENT;
        String userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + PHONE_DESC_BOB;
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withPhone(VALID_PHONE_BOB).build();
        EditCommand expectedCommand = new EditStudentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = targetIndex.getOneBased() + EMAIL_DESC_BOB + INVALID_PHONE_DESC + ADDRESS_DESC_BOB
                + PHONE_DESC_BOB;
        descriptor = new EditStudentDescriptorBuilder().withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).build();
        expectedCommand = new EditStudentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_STUDENT;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withTags().build();
        EditStudentCommand expectedCommand = new EditStudentCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}