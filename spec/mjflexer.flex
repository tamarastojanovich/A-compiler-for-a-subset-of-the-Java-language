package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{

	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}

	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}


program = "program"
break = "break"
return = "return"
class = "class"
else = "else"
const = "const"
if = "if"
while = "while"
new = "new"
print = "print"
read = "read"
void = "void"
extends = "extends"
continue = "continue"
foreach = "foreach"
findAny = "findAny"
findAndReplace = "findAndReplace"

ident = ([a-z] | [A-Z]) [a-z|A-Z|0-9|"_"]*
numConst = (0?| [1-9][0-9]*)
charConst = '[ -~]{0,1}'
boolConst = ("true" | "false")

assignOper = "="
mulOper = "*" | "/" | "%"
relOper = "<" | ">" | "<=" | ">=" | "==" | "!="
addOper = "+" | "-"


%%

" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }

{program}   { return new_symbol(sym.PROG, yytext());}
{print}	{ return new_symbol(sym.PRINT, yytext()); }
{return} 	{ return new_symbol(sym.RETURN, yytext()); }
{void} 		{ return new_symbol(sym.VOID, yytext()); }
{break}     {return new_symbol(sym.BREAK,yytext());}
{class}     {return new_symbol(sym.CLASS,yytext());}
{else}     {return new_symbol(sym.ELSE,yytext());}
{const}     {return new_symbol(sym.CONST,yytext());}
{if}     {return new_symbol(sym.IF,yytext());}
{while}     {return new_symbol(sym.WHILE,yytext());}
{new}     {return new_symbol(sym.NEW,yytext());}
{read}     {return new_symbol(sym.READ,yytext());}
{extends}     {return new_symbol(sym.EXTENDS,yytext());}
{continue}     {return new_symbol(sym.CONTINUE,yytext());}
{foreach}     {return new_symbol(sym.FOREACH,yytext());}
{findAny}        {return new_symbol(sym.FINDANY,yytext());}
{findAndReplace}        {return new_symbol(sym.FINDANDREPLACE,yytext());}


"true"  {return new_symbol(sym.BOOL,new Boolean(true));}
"false" {return new_symbol(sym.BOOL,new Boolean(false));}
{numConst}     {return new_symbol(sym.NUMBER,new Integer(yytext()));}
{ident}     {return new_symbol(sym.IDENT,yytext());}

"=" 		{ return new_symbol(sym.EQUALS, yytext()); }
"+" 		{ return new_symbol(sym.PLUS, yytext()); }
"-" 		{ return new_symbol(sym.MINUS, yytext()); }
"=="         { return new_symbol(sym.ISEQUAL, yytext()); }
"!=" 		{ return new_symbol(sym.DIFFERENT, yytext()); }
">" 		{ return new_symbol(sym.GT, yytext()); }
">=" 		{ return new_symbol(sym.GTE, yytext()); }
"<"			{ return new_symbol(sym.LS, yytext()); }
"<="         { return new_symbol(sym.LSE, yytext()); }
"*"         { return new_symbol(sym.MUL, yytext()); }
"/"         { return new_symbol(sym.DIV, yytext()); }
"%"         { return new_symbol(sym.MOD, yytext()); }
"=>"        { return new_symbol(sym.ARROW, yytext()); }
"<=>"       { return new_symbol(sym.MODIFOP, yytext()); }


"&&" 		{ return new_symbol(sym.AND, yytext()); }
"||" 		{ return new_symbol(sym.OR, yytext()); }
";" 		{ return new_symbol(sym.SEMI, yytext()); }
","         { return new_symbol(sym.COMMA, yytext()); }
"(" 		{ return new_symbol(sym.LPAREN, yytext()); }
")" 		{ return new_symbol(sym.RPAREN, yytext()); }
"{" 		{ return new_symbol(sym.LBRACE, yytext()); }
"}"			{ return new_symbol(sym.RBRACE, yytext()); }
"["         { return new_symbol(sym.LBRACKET, yytext()); }
"]"         { return new_symbol(sym.RBRACKET, yytext()); }
"."         { return new_symbol(sym.DOT, yytext()); }
":"         { return new_symbol(sym.DOUBLEDOT, yytext()); }
"++"        { return new_symbol(sym.INC, yytext()); }
"--"        { return new_symbol(sym.DEC, yytext()); }

"//" {yybegin(COMMENT);}
<COMMENT> . {yybegin(COMMENT);}
<COMMENT> "\r\n" { yybegin(YYINITIAL); }


{charConst}     {return new_symbol(sym.CHAR,new Character(yytext().charAt(1)));}

. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1)); }


