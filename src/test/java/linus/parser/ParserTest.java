package linus.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import linus.invalidtaskexception.InvalidTaskException;

public class ParserTest {
    @Test
    public void parse_listCommand_listReturned() throws InvalidTaskException {
        Parser parser = new Parser();
        assertEquals(List.of("list"), parser.parse("list"));
    }

    @Test
    public void parse_invalidCommand_exceptionThrown() throws InvalidTaskException {
        Parser parser = new Parser();
        assertThrows(InvalidTaskException.class, () -> parser.parse("deadline! finish increment /by 2026-08-28"));
    }
}
