package linus.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ParserTest {
    @Test
    public void parse_validCommand_commandReturned() {
        Parser parser = new Parser();
        assertEquals("todo", parser.parse("todo test code"));
    }

    @Test
    public void parse_invalidCommand_invalidcommandReturned() {
        Parser parser = new Parser();
        assertEquals("deadline!", parser.parse("deadline! finish increment /by 2026-08-28"));
    }
}
