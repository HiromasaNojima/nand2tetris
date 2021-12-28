package nand2tetris;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class ParserTest {

	@Test
	void trimTest() throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Parser parser = new Parser("/Users/hiromasa/Desktop/nand2tetris/projects/06/max/Max.asm");
		Method method = Parser.class.getDeclaredMethod("trim");
		method.setAccessible(true);
		List<String> trimed = (List<String>) method.invoke(parser);
		trimed.stream().forEach(l -> System.out.println(l));
	}

	@Test
	void getLabelTest() throws Exception {
		Parser parser = new Parser("/Users/hiromasa/Desktop/nand2tetris/projects/06/max/Max.asm");
		Method method = Parser.class.getDeclaredMethod("getLabel", String.class);
		method.setAccessible(true);
		String label = (String) method.invoke(parser, "(hoge)");
		assertEquals("hoge", label);
	}

	@Test
	void putSymbolsTest() throws Exception {
		Parser parser = new Parser("/Users/hiromasa/Desktop/nand2tetris/projects/06/max/Max.asm");

		List<String> lines = List.of("@R0", "D=M", "(hoge)", "@R1", "D=M");

		Method method = Parser.class.getDeclaredMethod("putSymbols", List.class);
		method.setAccessible(true);
		method.invoke(parser, lines);

		Field field = Parser.class.getDeclaredField("symbolTable");
		field.setAccessible(true);

		Map<String, Integer> symbolTable = (Map<String, Integer>) field.get(parser);
		assertEquals(2, symbolTable.get("hoge"));
	}

	@Test
	void toSymbolResolvedLineTest() throws Exception {
		Parser parser = new Parser("/Users/hiromasa/Desktop/nand2tetris/projects/06/max/Max.asm");

		Field field = Parser.class.getDeclaredField("symbolTable");
		field.setAccessible(true);
		field.set(parser, new HashMap<String, Integer>() {
			{
				put("hoge", 4);
			}
		});

		Method method = Parser.class.getDeclaredMethod("toSymbolResolvedLine", String.class);
		method.setAccessible(true);

		assertEquals("@4", method.invoke(parser, "@hoge"));
		assertEquals("@16", method.invoke(parser, "@aa"));
		assertEquals("@17", method.invoke(parser, "@bb"));
		assertEquals("@16", method.invoke(parser, "@aa"));
	}
}
