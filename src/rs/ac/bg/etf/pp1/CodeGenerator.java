package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import org.apache.log4j.Logger;

import java.lang.reflect.Array;
import java.util.*;

public class CodeGenerator extends VisitorAdaptor {
	
	private int varCount;
	
	private int paramCnt;
	
	private int mainPc;

	private Obj lenObj;
	private Obj ordObj;
	private Obj chrObj;

	static Obj temp=null;
	static Obj temp1=null;
	static Obj temp2=null;
	static int type=0;
	static Obj curr=null;

	static int jmp=0;
	static int jmp1=0;
	static int jmp2=0;
	static int addr=0;
	static Obj cnt=null;
	List<Integer> addrCondFact=new ArrayList();
	List<Integer> CondTermVal=new ArrayList<>();
	List<Integer> CondJmpAddr=new ArrayList<>();
	List<Integer> IfJmpAddr=new ArrayList<>();

	Stack<Integer> whileForAddr=new Stack<>();
	Map<Integer,List<Integer>> break_in_while_for=new HashMap<>();
	Logger log = Logger.getLogger(getClass());

	private int addOp=0;

	private int mulOp=0;

	private int relOp=0;

	CodeGenerator(){

//		Code.dataSize++;

		cnt=new Obj(Obj.Var,"cnt",Tab.intType);
		cnt.setAdr(Code.dataSize++);


		temp=new Obj(Obj.Var,"temp",Tab.intType);
		temp.setAdr(Code.dataSize++);


		temp1=new Obj(Obj.Var,"temp1",Tab.intType);
		temp1.setAdr(Code.dataSize++);

		temp2=new Obj(Obj.Var,"temp2",Tab.intType);
		temp2.setAdr(Code.dataSize++);

		curr=new Obj(Obj.Var,"curr",Tab.intType);
		curr.setAdr(Code.dataSize++);

		lenObj=Tab.find("len");
		lenObj.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n);
		Code.put(Code.arraylength);
		Code.put(Code.exit);
		Code.put(Code.return_);

		chrObj=Tab.find("chr");
		chrObj.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n);
		Code.put(Code.exit);
		Code.put(Code.return_);

		ordObj=Tab.find("ord");
		ordObj.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n);
		Code.put(Code.exit);
		Code.put(Code.return_);



	}
	
	public int getMainPc() {
		return mainPc;
	}

	@Override
	public void visit(MethodReturnType MethodTypeName) {
		if ("main".equalsIgnoreCase(MethodTypeName.getMethName())) {
			mainPc = Code.pc;
		}
		int level=MethodTypeName.obj.getLevel();
		MethodTypeName.obj.setAdr(Code.pc);
		
		// Collect arguments and local variables.
		SyntaxNode methodNode = MethodTypeName.getParent();
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);




		Code.put(Code.enter);

		Code.put(MethodTypeName.obj.getLevel());
		Code.put(MethodTypeName.obj.getLocalSymbols().size());


	}
	
	@Override
	public void visit(VarDecl VarDecl) {
		varCount++;
	}

	@Override
	public void visit(Form FormalParam) {
		paramCnt++;
	}	
	
	@Override
	public void visit(MethodDecl MethodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	@Override
	public void visit(ReturnStmt ReturnExpr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	
	@Override
	public void visit(DesignatorEquals Assignment) {
		Code.store(Assignment.getDesignator().obj);
	}

	@Override
	public void visit(FindAnyStmt stmt){
		//inic
		Code.loadConst(0);
		Code.store(curr);
		Code.load(stmt.getDesignatorFind().getDesignator1().obj);
		Code.put(Code.arraylength);
		Code.store(temp);//broj elemenata u nizu

		addr=Code.pc;
		Code.load(curr);
		Code.load(temp);
		Code.putFalseJump(Code.ne,0);
		jmp=Code.pc-2;
		Code.put(Code.dup);
		Code.load(stmt.getDesignatorFind().getDesignator1().obj);
		Code.load(curr);
		if(stmt.getDesignatorFind().getDesignator1().obj.getType().getElemType().getKind()==Struct.Char) Code.put(Code.baload);
		else Code.put(Code.aload);

		Code.put(Code.sub);
		Code.loadConst(0);
		Code.putFalseJump(Code.ne,0);
		jmp1=Code.pc-2;
		Code.loadConst(1);
		Code.load(curr);
		Code.put(Code.add);
		Code.store(curr);//inkrementiramo na kraju


		Code.putJump(addr);
		Code.fixup(jmp1);
		Code.loadConst(1);
		Code.store(stmt.getDesignatorFind().getDesignator().obj);
		Code.fixup(jmp);
		Code.put(Code.pop);
	}


	@Override
	public void visit(FactorNumConst Const) {
		Code.load(new Obj(Obj.Con, "$", Const.struct, Const.getN1(), 0));
	}

	@Override
	public void visit(FactorChar Const){
		Code.load(new Obj(Obj.Con,"$",Const.struct,Const.getC1(),0));
	}

	@Override
	public void visit(FactorBool Const){
		int val=Const.getB1().toString()=="true"?1:0;
		Code.load(new Obj(Obj.Con,"$",Const.struct,val,0));
	}
	
	@Override
	public void visit(DesignatorOneIden Designator) {
		SyntaxNode parent = Designator.getParent();
		if (DesignatorFind.class !=parent.getClass() && DesignatorEquals.class != parent.getClass() && DesignatorName.class != parent.getClass() && ForExpr.class!=parent.getClass() && ReadStmt.class!= parent.getClass()
		&& DesignatorParams.class != parent.getClass()) {
			Code.load(Designator.obj);
		}
	}

	@Override
	public void visit(DesignatorExprr Designator){
		SyntaxNode parent = Designator.getParent();
		if (DesignatorFind.class !=parent.getClass() && DesignatorEquals.class != parent.getClass() && DesignatorName.class != parent.getClass() && ForExpr.class!=parent.getClass() && ReadStmt.class != parent.getClass()) {
			if(parent.getClass()==DesignatorInc.class || parent.getClass()==DesignatorDec.class) Code.put(Code.dup2);
			Code.load(Designator.obj);
		}
	}

	@Override
	public void visit(FactorDesignatorFunParams FuncCall) {
		Obj functionObj = FuncCall.getDesignatorName().getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;


		Code.put(Code.call);


		Code.put2(offset);
	}
	
	@Override
	public void visit(PrintStmt printStmt) {
		if(printStmt.getExpr().struct == Tab.intType || printStmt.getExpr().struct.getKind()==Struct.Bool){
			if(printStmt.getNumList().obj==null)Code.loadConst(Code.const_5);
            else Code.loadConst(printStmt.getNumList().obj.getLevel());
			Code.put(Code.print);
		}else{
			if(printStmt.getNumList().obj==null)Code.loadConst(Code.const_1);
			else Code.loadConst(printStmt.getNumList().obj.getLevel());
			Code.put(Code.bprint);
		}
	}


	@Override
	public void visit(ReadStmt readStmt){
		if(readStmt.getDesignator().obj.getType().getKind()==Struct.Char) Code.put(Code.bread);
		else Code.put(Code.read);
		Code.store(readStmt.getDesignator().obj);
	}

	@Override
	public void visit(CondExpr cond){
		Code.loadConst(0);
		Code.putFalseJump(Code.ne,Code.pc);
		addrCondFact.add(Code.pc-2);
	}

	@Override
	public void visit(CondRelExpr cond){
		Code.putFalseJump(relOp,Code.pc);
		addrCondFact.add(Code.pc-2);
	}

	@Override
	public void visit(ConditionsAND cond){
//sve rue
		Code.putJump(Code.pc);//true
		CondTermVal.add(Code.pc-2);

		//ovde mi skace ako je neso false
		for(int addr:addrCondFact){
			Code.fixup(addr);
		}

		addrCondFact.clear();
	}

	@Override
	public void visit(CondOne cond){
		//deo anda
		SyntaxNode parent=cond.getParent();

		if(parent.getClass()!=ConditionsAND.class){

			Code.putJump(Code.pc);//true
			CondTermVal.add(Code.pc-2);
			//ovde ko mi skoci znaci nema nis
			for(int addr:addrCondFact){
				Code.fixup(addr);
			}
			addrCondFact.clear();
		}
	}



	@Override
	public void visit(ConditionOne cond){
		//skupandova
		SyntaxNode parent=cond.getParent();

          //skoci na else ili kraj pelje
		if(parent.getClass()==IfConditionsDecl.class || parent.getClass()==WhileStmt.class){
			//Code.loadConst(0);//ako nije dobro
			//Code.store(cond_cnt);
			//false
			Code.putJump(Code.pc);
			CondJmpAddr.add(Code.pc-2);
			//rue
			for(int addr:CondTermVal){//ako je okej
				Code.fixup(addr);
			}
	//		Code.loadConst(0);
		//	Code.store(cond_cnt);
			CondTermVal.clear();
		}
	}


	@Override
	public void visit(IfStatements stmt){
		Code.putJump(Code.pc);
		IfJmpAddr.add(Code.pc-2);
	}
	@Override
	public void visit(ElseWrd withElse){
		int addr=CondJmpAddr.get(CondJmpAddr.size()-1);
		CondJmpAddr.remove(CondJmpAddr.size()-1);
		Code.fixup(addr);
	}

	@Override
	public void visit(NoElse noElse){
		int addr=CondJmpAddr.get(CondJmpAddr.size()-1);
		CondJmpAddr.remove(CondJmpAddr.size()-1);
		Code.fixup(addr);
		addr=IfJmpAddr.get(IfJmpAddr.size()-1);
		IfJmpAddr.remove(IfJmpAddr.size()-1);
		Code.fixup(addr);
	}

	public void visit(WithElse withElse){
		int addr=IfJmpAddr.get(IfJmpAddr.size()-1);
		IfJmpAddr.remove(IfJmpAddr.size()-1);
		Code.fixup(addr);
	}

	//while

	@Override
	public void visit(BreakStmt breakStmt){
		Code.putJump(Code.pc);
		break_in_while_for.get(whileForAddr.lastElement()).add(Code.pc-2);
		//pamtimo break adrese u mapu
	}

	@Override
	public void visit(ContinueStmt continueStmt){
		Code.putJump(whileForAddr.lastElement());
	}

	@Override
	public void visit(WhileStmt whileStmt){
		Code.putJump(whileForAddr.lastElement());
		int addr=CondJmpAddr.get(CondJmpAddr.size()-1);
		CondJmpAddr.remove(CondJmpAddr.size()-1);
		Code.fixup(addr);
		int whileAddress=whileForAddr.pop();
		List<Integer> list=break_in_while_for.get(whileAddress);
		for(int i:list){
			Code.fixup(i);
		}
		//Code.putJump(whileAddress);
	}

	@Override
	public void visit(WhileExpr expr){
		whileForAddr.add(Code.pc);//ovo nam je bitno
		break_in_while_for.put(Code.pc,new ArrayList<>());
	}

	//for
	public void visit(ForExpr expr){

		//forCnt.put(,o);
		Code.load(cnt);
		Code.load(expr.getForVar().obj);
		Code.loadConst(0);
		Code.store(cnt);
		whileForAddr.add(Code.pc);//ovo nam je bitno

		Code.load(cnt);
		Code.load(expr.getDesignator().obj);
		Code.put(Code.arraylength);
		Code.putFalseJump(Code.ne,Code.pc);

		break_in_while_for.put(whileForAddr.lastElement(),new ArrayList<>());
		break_in_while_for.get(whileForAddr.lastElement()).add(Code.pc-2);


		Code.load(expr.getDesignator().obj);
		Code.load(cnt);
		if(expr.getDesignator().obj.getType().getElemType().getKind()==Struct.Char) Code.put(Code.baload);
		else Code.put(Code.aload);
		Code.store(expr.getForVar().obj);

		Code.loadConst(1);
		Code.load(cnt);
		Code.put(Code.add);
		Code.store(cnt);
		//kako da brojim kroz ove gluposti

	}

	public void visit(ForStmt stmt){
		//dodam 1 na brojac
		//Obj o=Tab.find(stmt.getForExpr().getForVar().obj);
		Code.putJump(whileForAddr.lastElement());
		int whileAddress=whileForAddr.pop();
		List<Integer> list=break_in_while_for.get(whileAddress);
		for(int i:list){//
			Code.fixup(i);
		}
		break_in_while_for.remove(whileAddress);
		Code.store(stmt.getForExpr().getForVar().obj);
		Code.store(cnt);
	}

    @Override
	public void visit(DesignatorFind dsgn){
		SyntaxNode parent=dsgn.getParent();

		if(parent.getClass()==FindAndReplaceStmt.class){
			type=dsgn.getDesignator1().obj.getType().getElemType().getKind();
			Code.loadConst(0);
			Code.store(curr);
			Code.load(dsgn.getDesignator1().obj);
			Code.put(Code.arraylength);
			Code.store(temp);//inic duzinu niza i curr
			//if(dsgn.getDesignator1().obj.getType().getElemType().getKind()==Struct.Char) temp1.
			Code.load(temp);
			Code.put(Code.newarray);
			if(dsgn.getDesignator1().obj.getType().getElemType()== Tab.intType || dsgn.getDesignator1().obj.getType().getElemType().getKind()==Struct.Bool) Code.put(Code.const_1);
			else Code.put(Code.const_n);
			Code.store(temp1);
			//alociramo niz

			//upisujemo u niz

			int addr=Code.pc;
			Code.load(curr);
			Code.load(temp);
			Code.putFalseJump(Code.ne,Code.pc);
			jmp=Code.pc-2;
			Code.load(temp1);
			Code.load(curr);
			Code.load(dsgn.getDesignator1().obj);
			Code.load(curr);
			if(dsgn.getDesignator1().obj.getType().getElemType().getKind()==Struct.Char){
				Code.put(Code.baload);
				Code.put(Code.bastore);
			}
			else {
				Code.put(Code.aload);
				Code.put(Code.astore);
			}


			Code.loadConst(1);
			Code.load(curr);
			Code.put(Code.add);
			Code.store(curr);
			Code.putJump(addr);
			Code.fixup(jmp);
			//i onda kada se zavrsi skace ovde i to je to

			Code.loadConst(0);
			Code.store(curr);



		}
	}


	@Override
	public void visit(FindVar var){

		Code.load(var.obj);
		Code.store(temp);//pamtimo vrednosti promenljive identa pre ngo da je koristimo da mozemo posle da je iskoristimo


		jmp=Code.pc;

		//prvo treba proveriti da li su vrednosti uopste jednake da bismo znali da li da izvvrsavamo
		Code.load(curr);
		Code.load(temp1);
		Code.put(Code.arraylength);
		Code.putFalseJump(Code.ne,Code.pc);
        addr=Code.pc-2;

		Code.put(Code.dup);

		Code.load(temp1);
		Code.load(curr);
		if(type==Struct.Char) Code.put(Code.baload);
		else Code.put(Code.aload);
		Code.store(var.obj);

		Code.load(var.obj);
		Code.putFalseJump(Code.eq,Code.pc);
        jmp1=Code.pc-2;

	}
	@Override
	public void visit(FindAndReplaceStmt stmt){


		Code.store(temp2);

		Code.load(temp2);
		Code.store(stmt.getFindVar().obj);


		Code.fixup(jmp1);
		Code.load(temp1);
        Code.load(curr);
		Code.load(stmt.getFindVar().obj);
		if(type==Struct.Char) Code.put(Code.bastore);
		else Code.put(Code.astore);

		Code.loadConst(1);
		Code.load(curr);
		Code.put(Code.add);
		Code.store(curr);//inkrementiramo na kraju
        Code.putJump(jmp);
		Code.fixup(addr);

		Code.load(temp1);
		Code.store(stmt.getDesignatorFind().getDesignator().obj);
		Code.put(Code.pop);
		Code.load(temp);
		Code.store(stmt.getFindVar().obj);
	}


	@Override
	public void visit(ConditionsOr cond){

		SyntaxNode parent=cond.getParent();
		if(parent.getClass()==IfConditionsDecl.class || parent.getClass()==WhileStmt.class){
			Code.putJump(Code.pc);
			CondJmpAddr.add(Code.pc-2);
			for(int addr:CondTermVal){
				Code.fixup(addr);
			}
			CondTermVal.clear();
		}
	}


	
	@Override
	public void visit(TermListt AddExpr) {
		if (addOp==0) Code.put(Code.add);
		else Code.put(Code.sub);
	}


	@Override
	public void visit(FactorNewExpr newExpr){
		Code.put(Code.newarray);
		if(newExpr.getType().struct== Tab.intType || newExpr.getType().struct.getKind()==Struct.Bool) Code.put(Code.const_1);
		else Code.put(Code.const_n);
	}

	@Override
	public void visit(FactorListt factorListt){
		switch (mulOp){
			case 0:
				Code.put(Code.mul);
				break;
			case 1:
				Code.put(Code.div);
				break;
			case 2:
				Code.put(Code.rem);
				break;
		}
	}

	@Override
	public void visit(DesignatorInc designatorInc){


		Code.loadConst(1);
		Code.put(Code.add);

		Code.store(designatorInc.getDesignator().obj);
	}

	@Override
	public void visit(DesignatorDec designatorDec){
		Code.loadConst(1);
		Code.put(Code.sub);

		Code.store(designatorDec.getDesignator().obj);
	}

	@Override
	public void visit(ModifFactorNeg factorNeg){
		Code.put(Code.neg);
	}

	@Override
	public void visit(ModifFactorListt modifFactorListt){

		Code.put(Code.dup2);
		Code.putFalseJump(Code.ne,Code.pc);
		int eqFactors=Code.pc-2;

		//Code.put(Code.dup2);
		Code.putFalseJump(Code.le,Code.pc);
		int gtFactor=Code.pc-2;

		Code.loadConst(-1);
		Code.putJump(Code.pc);
		int ltFactor=Code.pc-2;

		Code.fixup(gtFactor);
		Code.loadConst(1);
		Code.putJump(Code.pc);
		gtFactor=Code.pc-2;

		Code.fixup(eqFactors);
		Code.loadConst(0);
		Code.fixup(ltFactor);
		Code.fixup(gtFactor);

	}

	@Override
	public void visit(ADD add){
		addOp=0;
	}

	@Override
	public void visit(SUB sub)
	{
		addOp=1;
	}

	@Override
	public void visit(Mul mul){
		mulOp=0;
	}

	@Override
	public void visit(Div div){
		mulOp=1;
	}

	@Override
	public void visit(Mod mod){
		mulOp=2;
	}

	@Override
	public void visit(EQ rel){
		relOp=Code.eq;
	}

	@Override
	public void visit(NOTEQ rel){
		relOp=Code.ne;
	}

	@Override
	public void visit(GREATER rel){
		relOp=Code.gt;
	}

	@Override
	public void visit(GREATEREQ rel){
		relOp=Code.ge;
	}

	@Override
	public void visit(LESS rel){
		relOp=Code.lt;
	}

	@Override
	public void visit(LESSEQ rel){
		relOp=Code.le;
	}
}
