// generated with ast extension for cup
// version 0.8
// 25/7/2023 0:35:39


package rs.ac.bg.etf.pp1.ast;

public class LESSEQ extends Relop {

    public LESSEQ () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("LESSEQ(\n");

        buffer.append(tab);
        buffer.append(") [LESSEQ]");
        return buffer.toString();
    }
}
