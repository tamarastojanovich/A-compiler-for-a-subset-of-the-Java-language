package rs.ac.bg.etf.pp1;
import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.ast.ProgName;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.concepts.*;

import java.util.ArrayList;
import java.util.List;

public class SemanticPass extends VisitorAdaptor {

	boolean errorDetected = false;
	Obj currentMethod = null;
	boolean returnFound = false;
	int nVars;
	Struct currType = null;
	Struct methodType=null;
	int printCallCount=0;
	int numOfFormArgs=0;
	boolean enteredFor=false;
	Obj designatorExpr=null;
	Scope universe=new Scope(null);
	Logger log = Logger.getLogger(getClass());
	boolean condBool=true;
	boolean RelClassArray=false;
	int numOfArgs=0;
	Obj funcCall=null;
	boolean newFun=false;
	Obj currentClass=null;

	Struct boolType=null;
	Struct arrInt=null;
	Struct arrBool=null;
	Struct arrChar=null;

	static int whileForCnt=0;


	public void init(){
		universe=new Scope(null);
		Obj b=Tab.insert(Obj.Type,"bool",new Struct(Struct.Bool));
		boolType=b.getType();
		b=Tab.insert(Obj.Type,"int[]",new Struct(Struct.Array,Tab.intType));
		arrInt=b.getType();
		b=Tab.insert(Obj.Type,"char[]",new Struct(Struct.Array,Tab.charType));
		arrChar=b.getType();
		b=Tab.insert(Obj.Type,"bool[]",new Struct(Struct.Array,boolType));
		arrBool=b.getType();
	}

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	public void visit(Program program) {		
		nVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
	}

	public void visit(ProgName progName) {
		init();
		progName.obj = Tab.insert(Obj.Prog, progName.getPName(), Tab.noType);
		Tab.openScope();     	
	}

	public void visit(VarDecl varList){
		currType = varList.getType().struct;//struct stavimo
	}

	public void visit(Varr varDecl) {
		Obj varNode=Tab.find(varDecl.getVarName());
		if(varNode==Tab.noObj){
			if(currentClass!=null){
				Tab.insert(Obj.Fld, varDecl.getVarName(), currType);
			}
			else Tab.insert(Obj.Var, varDecl.getVarName(), currType);
			report_info("Deklarisana promenljiva "+ varDecl.getVarName(), varDecl);
		}
		else
		{

			report_error("Semanticka greska: Vec postoji promenljiva sa imenom"+ varDecl.getVarName(), varDecl);
		}

	}

	public void visit(VarrNoBrack varDecl) {
		Obj typeNode = Tab.find(currType.toString()+"[]");

		if(typeNode==Tab.noObj) {
			typeNode = Tab.insert(Obj.Type,currType.toString()+"[]",new Struct(Struct.Array, currType));
		}
		Obj varNode=Tab.find(varDecl.getVarName());
		if(varNode==Tab.noObj){
			Tab.insert(Obj.Var, varDecl.getVarName(),typeNode.getType());
			report_info("Deklarisana promenljiva "+ varDecl.getVarName(), varDecl);
		}
		else
		{
			report_error("Semanticka greska: Vec postoji promenljiva sa imenom"+ varDecl.getVarName(), varDecl);
		}

	}

	public void visit(ConstDecl constDecl){
		currType = constDecl.getType().struct;
	}

	public void visit(Num number){
		if(currType!=Tab.intType){
			report_error("Semanticka greska: Ne moze se dodeliti vrednost tipa Integer promenljivi tipa "+ currType.toString(),number);
		}
		Obj varNode=Tab.find(number.getConstName());
		if(varNode==Tab.noObj){
			Obj con=Tab.insert(Obj.Con, number.getConstName(),currType);
			con.setAdr(number.getN2());
			report_info("Semanticka greska: Deklarisana promenljiva "+ number.getConstName(), number);
		}
		else
		{
			report_error("Semanticka greska: Vec postoji promenljiva sa imenom"+ number.getConstName(), number);
		}

	}

	public void visit(Ch charConst){
		if(currType!=Tab.charType){
			report_error("Semanticka greska: Ne moze se dodeliti vrednost tipa Char promenljivi tipa "+ currType.getKind(),charConst);
		}
		Obj varNode=Tab.find(charConst.getConstName());
		if(varNode==Tab.noObj){
			Obj con=Tab.insert(Obj.Con, charConst.getConstName(),currType);
			con.setAdr(charConst.getC2());
			report_info("Semanticka greska: Deklarisana promenljiva "+ charConst.getConstName(), charConst);
		}
		else
		{
			report_error("Semanticka greska: Vec postoji promenljiva sa imenom"+ charConst.getConstName(), charConst);
		}
	}

	public void visit(Bool boolConst){
		if(currType!=boolType){
			report_error("Semanticka greska: Ne moze se dodeliti vrednost tipa Bool promenljivi tipa "+ currType.getKind(),boolConst);
		}
		Obj varNode=Tab.find(boolConst.getConstName());
		if(varNode==Tab.noObj){
			Obj con=Tab.insert(Obj.Con, boolConst.getConstName(),currType);
			con.setAdr(boolConst.getB2().toString()=="true"?1:0);
			report_info("Deklarisana promenljiva "+ boolConst.getConstName(), boolConst);
		}
		else
		{
			report_error("Semanticka greska: Vec postoji promenljiva sa imenom"+ boolConst.getConstName(), boolConst);
		}
	}

/*
	public void visit(ClassName className){
		Obj o=Tab.find(className.getClassName());
		if(o==Tab.noObj){
			currentClass=Tab.insert(Obj.Type,className.getClassName(),new Struct(Struct.Class));
			Tab.openScope();
            report_info("Obradjuje se klasa "+className.getClassName(),className);
		}
		else{
			report_error("Vec posoji promenljiva sa nazivom "+className.getClassName(),className);
		}
	}

	public void visit(ClassDecl classDecl){
		if(currentClass!=null){
			Tab.chainLocalSymbols(currentClass);
			Tab.closeScope();
		}
		currentClass=null;
	}

	public void visit(ClassInfoChild child){
		if(currType==Tab.noType){
			report_error("Klasa ne moze da prosiruje klasu koja ne postoji.",child);
		}
		else{
			if(currType.getKind()==Struct.Class){
				currentClass.getType().setElementType(currType);
			}
			else{
				report_error("Klasa mora da prosiruje drugu klasu!",child);
			}
		}
	}

	public void visit(ConstructorName name){
		if(!name.getI1().equals(currentClass.getName())){
			report_error("Ime konstruktora mora biti isto kao i ime klase u kojoj se nalazi.",name);
		}
		currentConstructor=Tab.insert(Obj.Meth,name.getI1(),Tab.noType);
		Tab.openScope();
	}


	public void visit(ConstructorDecl decl){

	}
*/

	public void visit(Type type) {
		Obj typeNode = Tab.find(type.getTypeName());
		if (typeNode == Tab.noObj) {
			report_error("Semanticka greska: Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola", null);
			type.struct = Tab.noType;
			currType = type.struct;

		} 
		else {
			if (Obj.Type == typeNode.getKind()) {
				type.struct = typeNode.getType();
				currType=type.struct;
			} 
			else {
				report_error("Semanticka greska: Greska: Ime " + type.getTypeName() + " ne predstavlja tip ", type);
				type.struct = Tab.noType;
				currType=type.struct;
			}
		}  
	}

	public void visit(MethodDecl methodDecl) {
		if(currentMethod!=null) {
			if (!returnFound && currentMethod.getType() != Tab.noType) {
				report_error("Semanticka greska na liniji " + methodDecl.getLine() + ": funkcija " + currentMethod.getName() + " nema return iskaz!", null);

			}


			Tab.chainLocalSymbols(currentMethod);

			Tab.closeScope();
			currentMethod.setLevel(numOfFormArgs);
			methodDecl.getMethodReturnType().obj=currentMethod;
			returnFound = false;
			currentMethod = null;
			methodType = null;
			numOfFormArgs = 0;
		}
	}

	public void visit(WithTypeRet type){
		methodType=type.getType().struct;
	}

	public void visit(VoidRet type){
		methodType=Tab.noType;
	}

	public void visit(MethodReturnType methodTypeName) {
		Obj o=Tab.find(methodTypeName.getMethName());
		Obj meth = Tab.find(methodTypeName.getMethName());
		if (o!=Tab.noObj){
			report_error("Semanticka greska: Vec je deklarisana funkcija sa nazivom "+methodTypeName.getMethName(),methodTypeName);
			return;
		}

		currentMethod = Tab.insert(Obj.Meth, methodTypeName.getMethName(),methodType);
		methodTypeName.obj = currentMethod;
		Tab.openScope();
		if(currentClass!=null){
			Tab.insert(Obj.Var,"this",currentClass.getType());
		}
		report_info("Obradjuje se funkcija " + methodTypeName.getMethName(), methodTypeName);
	}
	public void visit(FormNoBrack formArg){
		Obj arg=Tab.currentScope().findSymbol(formArg.getFormName());
		if(arg!=null){
			report_error("Semanticka greska: Vec je deklarisana promenljiva sa datim nazivom "+formArg.getFormName(),formArg);
			return;
		}
		arg=new Obj(Obj.Var,formArg.getFormName(),formArg.getType().struct);
		arg.setFpPos(numOfFormArgs);
		Tab.currentScope().addToLocals(arg);
		numOfFormArgs++;
	}

	public void visit(Formm formArg){
		Obj typeNode = Tab.find(currType+"[]");
		if(typeNode==Tab.noObj) {
			typeNode = Tab.insert(Obj.Type,currType+"[]",new Struct(Struct.Array, currType));
		}
		Obj arg=Tab.currentScope().findSymbol(formArg.getFormName());
		if(arg!=null){
			report_error("Semanticka greska: Vec je deklarisana promenljiva sa datim nazivom "+formArg.getFormName(),formArg);
			return;
		}
		arg=Tab.insert(Obj.Var,formArg.getFormName(),typeNode.getType());
		arg.setFpPos(numOfFormArgs);
		numOfFormArgs++;
	}

	public void visit(DesignatorOneIden dsgn){
			Obj o=Tab.find(dsgn.getI1());
			if(o!=Tab.noObj){
				dsgn.obj=o;
			}
			else{
				report_error("Semanticka greska: Ne postoji promenljiva " + dsgn.getI1(),dsgn);
			}

	}

	public  void visit(DesignatorExprr designatorExprr){
		if(designatorExprr.getParent().getClass()==DesignatorExprr.class){
			report_error("Semanticka greska: Nedozvoljen pristup: skaralni tip ne moze biti indeksiran.",designatorExprr);
		}
		else{
			Obj o=Tab.find(designatorExprr.getDesignator().obj.getName());
			if(o==Tab.noObj){
				report_error("Semanticka greska: Ne postoji dati element.",designatorExprr);
			}
			else{
				if(o.getType().getKind()!=Struct.Array){
					report_error("Semanticka greska: Nedozvoljen pristup: skaralni tip ne moze biti indeksiran.",designatorExprr);
				}else {
					designatorExpr=new Obj(Obj.Elem,designatorExprr.getDesignator().obj.getName(),designatorExprr.getDesignator().obj.getType().getElemType());
					designatorExprr.obj=new Obj(Obj.Elem,designatorExprr.getDesignator().obj.getName(),designatorExprr.getDesignator().obj.getType().getElemType());
				}
			}
		}

	}


	public void visit(DesignatorEquals stmt){
		if(newFun){
			if(stmt.getDesignator().obj.getType().getKind()!=Struct.Array){
				report_error("Semanticka greska: Ne moze da se alocira memorija za promenljivu koja nije niz!",stmt);
			}
		}
		if(stmt.getDesignator().obj.getKind()!=Obj.Var && stmt.getDesignator().obj.getKind()!=Obj.Elem && stmt.getDesignator().obj.getKind()!=Obj.Fld){
				report_error("Semanticka greska: Nedozvoljen tip podatka. Mora biti promenljiva,element niza ili polje klase.",stmt);
		}
		if(stmt.getExpr().struct!=null && stmt.getDesignator().obj.getType().getKind()!=stmt.getExpr().struct.getKind()){
			report_error("Semanticka greska: Leva i desna jednakost nisu kompatibilnog tipa.",stmt);
		}
		newFun=false;
	}



	public void visit(Expr exp){
		exp.struct=exp.getTermList().struct;
	}

	public void visit(RetExprExist ret_expr){
		int exprType=ret_expr.getExpr().struct.getKind();
		if(currentMethod!=null) {
			if ((currentMethod.getType().getKind() == Struct.Int && exprType!=Struct.Int) ||
					(currentMethod.getType().getKind() == Struct.Bool && exprType!=Struct.Bool) ||
					(currentMethod.getType().getKind() == Struct.Char && exprType!=Struct.Char)) {
				report_error("Semanticka greska: Return iskaz se ne podudara sa return tipom funkcije.", ret_expr);
			}
			returnFound = true;
		}
	}

	public void visit(NoRetExpr ret_expr){
		if(currentMethod!=null){
			if(currentMethod.getType()!=Tab.noType){
				report_error("Semanticka greska: Povratna vrednost funkcije nije void.",ret_expr);
			}
			returnFound=true;
		}

	}

	public void visit(ForExpr forr){
		whileForCnt++;
	}
	public void visit(WhileExpr whileExpr){whileForCnt++;}


	public void visit(PrintStmt printStmt){
		int exprType=printStmt.getExpr().struct.getKind();
		if(exprType!=Struct.Bool && exprType!=Struct.Char && exprType!=Struct.Int){
			report_error("Semanticka greska: Izraz mora biti tipa Integer,Char ili Bool",printStmt);
		}
		printCallCount++;
	}

	public void visit(NumListt numListt){
		numListt.obj=new Obj(Obj.Con,"param",Tab.intType,0,numListt.getN1());
	}

	public void visit(NumNoList numNoList){
		numNoList.obj=null;
	}

	public void visit(ReadStmt rdStmt){
		if(rdStmt.getDesignator().obj.getKind()!=Obj.Var && rdStmt.getDesignator().obj.getKind()!=Obj.Elem && rdStmt.getDesignator().obj.getKind()!=Obj.Fld){
			report_error("Semanticka greska: Tip promenljive za koji se poziva funkcija read mora da bude promenljiva,element niza ili polje klase.",rdStmt);
		}
		if(rdStmt.getDesignator().obj.getType().getKind()!=Struct.Int && rdStmt.getDesignator().obj.getType().getKind()!=Struct.Char && rdStmt.getDesignator().obj.getType().getKind()!=Struct.Bool){
			report_error("Semanticka greska: Tip promenljive mora da bude Bool, Integer ili Char.",rdStmt);
		}
	}

	public void visit(IfStmt ifStmt){
		if(!condBool){
			report_error("Semanticka greska: Uslov mora da bude tipa bool!",ifStmt);
		}
		condBool=true;
	}

	public void visit(WhileStmt whileStmt){
		if(!condBool){
			report_error("Semanticka greska:Uslov mora da bude tipa bool!",whileStmt);
		}
		condBool=true;
		whileForCnt--;
	}

	public void visit(ForStmt stmt){
		enteredFor=false;
		if(stmt.getForExpr().getDesignator().obj.getKind()!=Obj.Var || stmt.getForExpr().getDesignator().obj.getType().getKind()!=Struct.Array){
			report_error("Semanticka greska: Tip promenljive nad kojom se vrsi operacija mora da bude niz.",stmt);
			return;
		}
		String ident=stmt.getForExpr().getForVar().getI1();
		Obj meth = Tab.find(ident);
		if (meth==Tab.noObj){
			report_error("Semanticka greska: Data promenljiva "+ident + " nije deklarisana.",stmt);
			return;
		}
		if(meth.getType().getKind()!=stmt.getForExpr().getDesignator().obj.getType().getElemType().getKind()){
			report_error("Semanticka greska: Promenljiva koja se koristi za iteraciju mora da bude istog tipa kao i element niza.",stmt);
		}
		stmt.getForExpr().getForVar().obj=meth;
		whileForCnt--;


	}

	public void visit(DesignatorInc dsgn){
		if(dsgn.getDesignator().obj!=null) {
			if (dsgn.getDesignator().obj.getKind() != Obj.Var && dsgn.getDesignator().obj.getKind() != Obj.Elem && dsgn.getDesignator().obj.getKind() != Obj.Fld) {
				report_error("Semanticka greska: Objekat koji se inkrementira mora da bude promenljiva, element niza ili polje klase.", dsgn);
			}
			if (dsgn.getDesignator().obj.getType() != Tab.intType) {
				report_error("Semanticka greska: Tip promenljive mora da bude celobrojnog tipa(integer).", dsgn);

			}
		}
	}

	public void visit(CondRelExpr cond){
		if(cond.getExpr().struct.getKind()!=cond.getExpr1().struct.getKind()) {
			report_error("Semanticka greska:Promenljive nisu istog tipa",cond);
			condBool=false;
		}
		if(!RelClassArray && (cond.getExpr().struct.isRefType() || cond.getExpr1().struct.isRefType()) ){
			report_error("Semanticka greska:Klasa/niz se ne moze porediti na dati nacin",cond);
		}
		RelClassArray=false;
	}

	public void visit(CondExpr cond){
			if(cond.getExpr().struct.getKind()!=Struct.Bool) condBool=false;
	}

	public void visit(BreakStmt brk_Stmt){
		if(whileForCnt==0){
			report_error("Semanticka greska: Break moze biti pozvan samo u okviru while ili for petlje.",brk_Stmt);
		}
	}

	public void visit(ContinueStmt cnt_Stmt){
		if(whileForCnt==0){
			report_error("Semanticka greska: Continue moze biti pozvan samo u okviru while ili for petlje.",cnt_Stmt);
		}
	}

	public void visit(DesignatorDec dsgn){
		if(dsgn.getDesignator().obj!=null) {
			if (dsgn.getDesignator().obj.getKind() != Obj.Var && dsgn.getDesignator().obj.getKind() != Obj.Elem && dsgn.getDesignator().obj.getKind() != Obj.Fld) {
				report_error("Semanticka greska: Objekat koji se dekrementira mora da bude promenljiva, element niza ili polje klase.", dsgn);
			}
			if (dsgn.getDesignator().obj.getType() != Tab.intType) {
				report_error("Semanticka greska: Tip promenljive mora da bude celobrojnog tipa(integer).", dsgn);

			}
		}

	}

	public void visit(DesignatorParamsExist designatorParams){
       if(funcCall!=null) {
		   Obj o;
		   o = funcCall;
		   if (o.getLevel() != numOfArgs) {
			   report_error("Semanticka greska: Neadekvatan broj argumenata pri pozivanju funkcije " + o.getName(), designatorParams);
		   }
	   }
		numOfArgs=0;
		designatorExpr=null;
		funcCall=null;
	}


	public void visit(AcParsList parsList){
		if(funcCall!=null) {
			Obj o;
			o = funcCall;
			List list = new ArrayList(o.getLocalSymbols());
			if (parsList.getExpr().struct.getKind() != ((Obj) list.get(numOfArgs)).getType().getKind()) {
				report_error("Semanticka greska:" + numOfArgs + ". parametar se ne poklapa sa argumentom funkcije " + o.getName(), parsList);
			}
			numOfArgs++;
		}
	}

	public void visit(AcParsOne parsOne){
		if(funcCall!=null) {
			Obj o;
			o = funcCall;
			List list = new ArrayList(o.getLocalSymbols());
			if (parsOne.getExpr().struct.getKind() != ((Obj) list.get(numOfArgs)).getType().getKind()) {
				report_error("Semanticka greska:" + numOfArgs + ". parametar se ne poklapa sa argumentom funkcije " + o.getName(), parsOne);
			}
			numOfArgs++;
		}

	}

	public void visit(DesignatorName designatorName){
		funcCall=designatorName.getDesignator().obj;
		if(funcCall.getKind()==Obj.Meth){
			funcCall=designatorName.getDesignator().obj;
		}
		else{
			report_error("Semanticka greska: Metoda " + funcCall.getName() + " nije deklarisana.",designatorName);
		    funcCall=null;
		}
	}

	public void visit(FactorNumConst num){
		num.struct=Tab.intType;
	}

	public void visit(FactorBool bool){
		bool.struct=boolType;
	}

	public void visit(FactorExpr factorExpr){
		factorExpr.struct=factorExpr.getExpr().struct;
	}

	public void visit(FactorNewExpr factor){
		if(factor.getType().struct.getKind()==Struct.Int) {
			factor.struct=arrInt;
		}
		else if(factor.getType().struct.getKind()==Struct.Bool) factor.struct=arrBool;
		else if(factor.getType().struct.getKind()==Struct.Char) factor.struct=arrChar;
		
	}

	public void visit(FactorDesignatorParams factor){
		if(factor.getDesignator().obj!=null)
			factor.struct=factor.getDesignator().obj.getType();
	}

	public void visit(FactorDesignatorFunParams factor){
		factor.struct=factor.getDesignatorName().getDesignator().obj.getType();
	}

	public void visit(FactorChar chr){
		chr.struct=Tab.charType;
	}

	public void visit(ModifFactorNeg factor){
		if(factor.getFactor().struct!=Tab.intType){
			report_error("Semanticka greska: Samo ceo broj moze biti negativan.",factor);
		}
		factor.struct=Tab.intType;
	}

	public void visit(FactorListt factor){
		if(factor.getModifFactorList().struct.getKind()!=Struct.Int){
			report_error("Semanticka greska:Mogu da se mnoze samo celi brojevi.",factor);
		}
		factor.struct=Tab.intType;
	}

	public void visit(TermListt term){
		if(term.getTerm().struct.getKind()!=Struct.Int){
			report_error("Semanticka greska: Mogu da se sabiraju samo celi brojevi.",term);
		}
		term.struct=Tab.intType;
	}

	public void visit(FactorOne factor){
		factor.struct=factor.getModifFactorList().struct;
	}

	public void visit(TermListOne term){
		term.struct=term.getTerm().struct;
	}

	public void visit(Term term){
		term.struct=term.getFactorList().struct;
	}

	public void visit(ModifFactor modifFactor){ modifFactor.struct=modifFactor.getFactor().struct;}

	public void visit(ModifFactorListt modifFactorListt){ modifFactorListt.struct=modifFactorListt.getFactor().struct;}

	public void visit(ExprNewList exprNewList){
		newFun=true;
		if(exprNewList.getExpr().struct.getKind()!=Struct.Int){
			report_error("Semanticka greska: Niz moze biti indeksiran samo celim brojevima.",exprNewList);
		}
	}

	public void visit(FindAnyStmt stmt){
	    if(stmt.getDesignatorFind().getDesignator().obj.getType().getKind()!=Struct.Bool){
			report_error("Semanticka greska: Tip promenljive kojoj se dodeljuje mora biti tipa bool!",stmt);
		}
		if(stmt.getDesignatorFind().getDesignator1().obj.getType().getKind()!=Struct.Array){
			report_error("Semanticka greska: Tip promenljive nad kojom se izvrsava funkcija findAny mora biti niz!",stmt);
		}
		else{
			if(stmt.getExpr().struct.getKind()!=stmt.getDesignatorFind().getDesignator1().obj.getType().getElemType().getKind()){
				report_error("Semanticka greska: Izraz koji se pretrazuje mora biti isog tipa kao i element niza",stmt);
			}
		}
	}

	public void visit(FindAndReplaceStmt stmt) {
		if (stmt.getDesignatorFind().getDesignator().obj.getType().getKind() != Struct.Array) {
			report_error("Semanticka greska: Tip promenljive kojoj se dodeljuje mora biti niz!", stmt);
		}
		if(stmt.getDesignatorFind().getDesignator1().obj.getType().getKind()!=Struct.Array){
			report_error("Semanticka greska: Tip promenljive nad kojom se izvrsava funkcija findAny mora biti niz!",stmt);
		}
		if (stmt.getDesignatorFind().getDesignator().obj.getType().getKind() == Struct.Array && stmt.getDesignatorFind().getDesignator1().obj.getType().getKind()==Struct.Array ) {
			if (stmt.getDesignatorFind().getDesignator1().obj.getType().getElemType() != stmt.getDesignatorFind().getDesignator().obj.getType().getElemType()) {
				report_error("Semanticka greska: Nizovi nisu kompatibilnog tipa!", stmt);
			}
			Obj o=Tab.find(stmt.getFindVar().getI2());
			if(o==Tab.noObj){
				report_error("Semanticka greska: Ne postoji promenljiva sa datim imenom "+stmt.getFindVar().getI2(),stmt);
			}
            stmt.getFindVar().obj=o;
			if(stmt.getExpr().struct.getKind()!=stmt.getDesignatorFind().getDesignator1().obj.getType().getElemType().getKind()){
					report_error("Semanticka greska: Tip elementa u nizu koji se trazi mora biti istog tipa kao i element niza!",stmt);

			}
			if(stmt.getFindVar().getExpr().struct.getKind()!=stmt.getDesignatorFind().getDesignator1().obj.getType().getElemType().getKind()){
				report_error("Semanticka greska: Tip vrednosti koju zelimo da smestimo na poziciju elementa mora da bude istog tipa kao i element niza!",stmt);
			}
		}
	}



	public void visit(Relop rel){

	}

	public void visit(EQ equal){
		RelClassArray=true;
	}

	public void visit(NOTEQ notequal){
		RelClassArray=true;
	}

	public boolean passed(){
		return !errorDetected;
	}

}

