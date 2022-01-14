package nand2tetris.compiler;

import java.util.ArrayList;
import java.util.List;

public class CompilationEngine {

	List<Token> tokens;

	List<String> parsed = new ArrayList<>();

	int now = 0;

	public CompilationEngine(List<Token> tokens) {
		this.tokens = tokens;
	}

	public List<String> compileClass() {
		parsed.add("<class>");

		// class
		this.appendToken();

		// class name
		this.appendToken();

		// {
		this.appendToken();

		// classVarDec
		this.compileClassVarDec();

		// soubroutineDec
		this.compileSubroutineDesc();

		// }
		this.appendToken();
		parsed.add("</class>");
		return parsed;
	}

	private void compileSubroutineDesc() {
		if (KeywordType.of(nowToken().getToken()) != KeywordType.SUBROUTINE_DEC) {
			return;
		}

		parsed.add("<subroutineDec>");

		// constructor or function or method
		this.appendToken();

		// void or type
		this.appendToken();

		// subroutineName
		this.appendToken();

		// ( parameterList )
		this.appendToken();
		parsed.add("<parameterList>");
		this.compileParameterList();
		parsed.add("</parameterList>");
		this.appendToken();

		// { subroutineBody }
		parsed.add("<subroutineBody>");
		this.appendToken();
		this.compuleSubroutineBody();
		this.appendToken();
		parsed.add("</subroutineBody>");

		parsed.add("</subroutineDec>");

		this.compileSubroutineDesc();
	}

	private void compuleSubroutineBody() {
		this.compileVarDec();
		parsed.add("<statements>");
		this.compileStatements();
		parsed.add("</statements>");
	}

	private void compileStatements() {
		Statement statement = Statement.of(nowToken().getToken());
		if (statement == null) {
			return;
		}
		if (statement == Statement.LET) {
			this.compileLetStatement();
		} else if (statement == Statement.DO) {
			this.compileDoStatement();
		} else if (statement == Statement.IF) {
			this.compileIfStatement();
		} else if (statement == Statement.WHILE) {
			this.compileWhileStatement();
		} else if (statement == Statement.RETURN) {
			this.compileReturnStatemet();
		}

		this.compileStatements();
	}

	private void compileReturnStatemet() {
		parsed.add("<returnStatement>");
		// return
		this.appendToken();
		if (!nowToken().getToken().equals(";")) {
			this.compileExpression();
		}
		// ;
		this.appendToken();
		parsed.add("</returnStatement>");
	}

	private void compileWhileStatement() {
		parsed.add("<whileStatement>");
		// while
		this.appendToken();
		//(
		this.appendToken();
		// expression
		this.compileExpression();
		// )
		this.appendToken();
		// {
		this.appendToken();
		// statements
		parsed.add("<statements>");
		this.compileStatements();
		parsed.add("</statements>");
		// }
		this.appendToken();

		parsed.add("</whileStatement>");
	}

	private void compileIfStatement() {
		parsed.add("<ifStatement>");
		// if
		this.appendToken();
		// (
		this.appendToken();
		// expression
		this.compileExpression();
		// )
		this.appendToken();
		// {
		this.appendToken();
		// statements
		parsed.add("<statements>");
		this.compileStatements();
		parsed.add("</statements>");
		// }
		this.appendToken();
		if (nowToken().getToken().equals("else")) {
			// else
			this.appendToken();
			// {
			this.appendToken();
			// statements
			parsed.add("<statements>");
			this.compileStatements();
			parsed.add("</statements>");
			// }
			this.appendToken();
		}

		parsed.add("</ifStatement>");
	}

	private void compileDoStatement() {
		parsed.add("<doStatement>");
		// do
		this.appendToken();
		// soubroutineCall
		this.compileSubroutineCall();
		// ;
		this.appendToken();

		parsed.add("</doStatement>");
	}

	private void compileSubroutineCall() {
		// soubroutineName or classname or varname
		this.appendToken();

		if (nowToken().getToken().equals("(")) {
			// (
			this.appendToken();
			// expressionlist
			parsed.add("<expressionList>");
			this.compileExpressionList();
			parsed.add("</expressionList>");
			// )
			this.appendToken();
		} else {
			// .
			this.appendToken();
			// suborutineName
			this.appendToken();
			// (
			this.appendToken();
			// expressionlist
			parsed.add("<expressionList>");
			this.compileExpressionList();
			parsed.add("</expressionList>");
			// )
			this.appendToken();
		}
	}

	private void compileExpressionList() {
		if (nowToken().getToken().equals(")")) {
			return;
		}

		this.compileExpression();

		while (nowToken().getToken().equals(",")) {
			// ,
			this.appendToken();
			// expression
			this.compileExpression();
		}
	}

	private void compileLetStatement() {
		parsed.add("<letStatement>");
		// let
		this.appendToken();

		// varName
		this.appendToken();

		if (nowToken().getToken().equals("[")) {
			// [
			this.appendToken();
			// expression
			this.compileExpression();
			// ]
			this.appendToken();
		}

		// =
		this.appendToken();

		// expression
		this.compileExpression();

		// ;
		this.appendToken();

		parsed.add("</letStatement>");
	}

	private void compileExpression() {
		parsed.add("<expression>");
		this.compileTerm();

		while (KeywordType.of(nowToken().getToken()) == KeywordType.OP) {
			// op
			this.appendToken();

			// term
			this.compileTerm();
		}
		parsed.add("</expression>");
	}

	private void compileTerm() {
		parsed.add(("<term>"));
		if (nowToken().getTokenType() == TokenType.INTEGER_CONSTANT) {
			// integerConstant
			this.appendToken();
		} else if (nowToken().getTokenType() == TokenType.STRING_CONSTANT) {
			// stringConstant
			this.appendToken();
		} else if (KeywordType.of(nowToken().getToken()) == KeywordType.CONSTANT) {
			// KeywordConstant
			this.appendToken();
		} else if (nowToken().getTokenType() == TokenType.IDENTIFIER) {
			Token next = nextToken();
			if (next.getToken().equals("[")) {
				// varName
				this.appendToken();
				// [
				this.appendToken();
				// expressstion
				this.compileExpression();
				// ]
				this.appendToken();
			} else if (next.getToken().equals("(") || next.getToken().equals(".")) {
				this.compileSubroutineCall();
			} else {
				// varName
				this.appendToken();
			}
		} else if (nowToken().getToken().equals("(")) {
			// (
			this.appendToken();
			// expression
			this.compileExpression();
			// )
			this.appendToken();
		} else {
			// unaryOp
			this.appendToken();

			// term
			this.compileTerm();
		}
		parsed.add(("</term>"));
	}

	private void compileVarDec() {
		if (KeywordType.of(nowToken().getToken()) != KeywordType.VAR_DEC) {
			return;
		}

		parsed.add("<varDec>");
		// var
		this.appendToken();

		// type
		this.appendToken();

		// varName
		this.appendToken();

		while (this.nowToken().getToken().equals(",")) {
			// ,
			this.appendToken();

			// varName
			this.appendToken();
		}

		// ;
		this.appendToken();

		parsed.add("</varDec>");
		this.compileVarDec();
	}

	private void compileParameterList() {
		if (nowToken().getToken().equals(")")) {
			return;
		}

		// type
		this.appendToken();

		// varName
		this.appendToken();

		while (this.nowToken().getToken().equals(",")) {
			// ,
			this.appendToken();

			// type
			this.appendToken();

			// varName
			this.appendToken();
		}
	}

	private void compileClassVarDec() {
		if (KeywordType.of(nowToken().getToken()) != KeywordType.CLASS_VAR_DEC) {
			return;
		}

		parsed.add("<classVarDec>");

		// static or filed
		this.appendToken();

		// type
		this.appendToken();

		// varName
		this.appendToken();

		while (this.nowToken().getToken().equals(",")) {
			// ,
			this.appendToken();

			// varName
			this.appendToken();
		}

		// ;
		this.appendToken();

		parsed.add("</classVarDec>");

		// 再帰
		this.compileClassVarDec();
	}

	private void appendToken() {
		Token token = this.getToken();
		parsed.add(XmlConverter.convertTag(token.getTokenType().getName(), token.getTokenXmlValue()));
	}

	private Token getToken() {
		Token token = tokens.get(now);
		now++;
		return token;
	}

	private Token nowToken() {
		return tokens.get(now);
	}

	private Token nextToken() {
		return tokens.get(now + 1);
	}

}
