package nand2tetris.compiler;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.Test;

public class JackTokenizerTest {

	@Test
	void isCommentToEndTest() throws Exception {
		JackTokenizer tokenizer = new JackTokenizer("/Users/hiromasa/Desktop/nand2tetris/projects/06/max/Max.asm");

		Method method = JackTokenizer.class.getDeclaredMethod("isCommentToEnd", String.class);
		method.setAccessible(true);

		assertEquals(true, method.invoke(tokenizer, "//"));
		assertEquals(false, method.invoke(tokenizer, "/a"));
		assertEquals(false, method.invoke(tokenizer, "a"));
	}

	@Test
	void isCommentStartTest() throws Exception {
		JackTokenizer tokenizer = new JackTokenizer("/Users/hiromasa/Desktop/nand2tetris/projects/06/max/Max.asm");

		Method method = JackTokenizer.class.getDeclaredMethod("isCommentStart", String.class);
		method.setAccessible(true);

		assertEquals(true, method.invoke(tokenizer, "/*"));
		assertEquals(true, method.invoke(tokenizer, "/**"));
		assertEquals(false, method.invoke(tokenizer, "//"));
	}

	@Test
	void isCommentEndTest() throws Exception {
		JackTokenizer tokenizer = new JackTokenizer("/Users/hiromasa/Desktop/nand2tetris/projects/06/max/Max.asm");

		Method method = JackTokenizer.class.getDeclaredMethod("isCommentEnd", String.class);
		method.setAccessible(true);

		assertEquals(false, method.invoke(tokenizer, "/*"));
		assertEquals(true, method.invoke(tokenizer, "*/"));
		assertEquals(false, method.invoke(tokenizer, "//"));
	}

	@Test
	void getNeedLinesTest() throws Exception {
		JackTokenizer tokenizer = new JackTokenizer("/Users/hiromasa/Desktop/nand2tetris/projects/06/max/Max.asm");

		Field field = JackTokenizer.class.getDeclaredField("lines");
		field.setAccessible(true);
		field.set(tokenizer, List.of("//", "", "aa", "           test        "));

		Method method = JackTokenizer.class.getDeclaredMethod("getNeedLines");
		method.setAccessible(true);

		List<String> list = (List<String>) method.invoke(tokenizer);
		assertEquals(List.of("aa", "test"), list);
	}

	@Test
	void splitLineWithStringTest() throws Exception {
		JackTokenizer tokenizer = new JackTokenizer("/Users/hiromasa/Desktop/nand2tetris/projects/06/max/Max.asm");

		Method method = JackTokenizer.class.getDeclaredMethod("splitLineWithString", String.class);
		method.setAccessible(true);

		assertEquals(List.of("let", "length", "=", "Keyboard.readInt(", "\"HOW MANY NUMBERS?\"", ");"),
				method.invoke(tokenizer, "let length = Keyboard.readInt(\"HOW MANY NUMBERS?\");"));
	}
}
