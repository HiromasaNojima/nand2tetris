package nand2tetris.vm;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ParserTest {

	@Test
	void splitBySpaceTest() throws Exception {
		Parser parser = new Parser(
				"/Users/hiromasa/Desktop/nand2tetris/projects/07/StackArithmetic/SimpleAdd/SimpleAdd.vm");
		Method method = Parser.class.getDeclaredMethod("splitBySpace", String.class);
		method.setAccessible(true);
		String[] split = (String[]) method.invoke(parser, "push constant 7");
		assertEquals("push", split[0]);
		assertEquals("constant", split[1]);
		assertEquals("7", split[2]);
	}

	@Test
	void isSegmentConstantTest() throws Exception {
		Parser parser = new Parser(
				"/Users/hiromasa/Desktop/nand2tetris/projects/07/StackArithmetic/SimpleAdd/SimpleAdd.vm");
		Method method = Parser.class.getDeclaredMethod("isSegmentConstant", String.class);
		method.setAccessible(true);
		assertEquals(true, method.invoke(parser, "constant"));
		assertEquals(false, method.invoke(parser, "static"));
	}

	@Test
	void translatePushTest() throws Exception {
		Parser parser = new Parser(
				"/Users/hiromasa/Desktop/nand2tetris/projects/07/StackArithmetic/SimpleAdd/SimpleAdd.vm");
		Method method = Parser.class.getDeclaredMethod("translatePushLine", String[].class);
		method.setAccessible(true);

		String[] split = new String[] { "push", "constant", "7" };
		method.invoke(parser, new Object[] { split });

		Field field = Parser.class.getDeclaredField("parsedLines");
		field.setAccessible(true);
		List<String> parsedLines = (List<String>) field.get(parser);

		assertEquals("@7", parsedLines.get(0));

	}

}
