package linus.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {
    @Test
    public void ValidCommandParserTest() {
        Parser parser = new Parser();
        assertEquals("todo", parser.parse("todo test code"));
    }

    @Test
    public void InvalidCommandParserTest() {
        Parser parser = new Parser();
        assertEquals("deadline!", parser.parse("deadline! finish increment /by 2026-08-28"));
    }
}
