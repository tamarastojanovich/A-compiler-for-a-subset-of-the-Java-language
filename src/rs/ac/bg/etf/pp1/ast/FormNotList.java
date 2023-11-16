// generated with ast extension for cup
// version 0.8
// 25/7/2023 0:35:39


package rs.ac.bg.etf.pp1.ast;

public class FormNotList extends FormList {

    private Form Form;

    public FormNotList (Form Form) {
        this.Form=Form;
        if(Form!=null) Form.setParent(this);
    }

    public Form getForm() {
        return Form;
    }

    public void setForm(Form Form) {
        this.Form=Form;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Form!=null) Form.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Form!=null) Form.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Form!=null) Form.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormNotList(\n");

        if(Form!=null)
            buffer.append(Form.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormNotList]");
        return buffer.toString();
    }
}
