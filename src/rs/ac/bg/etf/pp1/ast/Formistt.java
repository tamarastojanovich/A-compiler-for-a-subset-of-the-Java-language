// generated with ast extension for cup
// version 0.8
// 25/7/2023 0:35:39


package rs.ac.bg.etf.pp1.ast;

public class Formistt extends FormList {

    private PreCommaForm PreCommaForm;
    private FormList FormList;

    public Formistt (PreCommaForm PreCommaForm, FormList FormList) {
        this.PreCommaForm=PreCommaForm;
        if(PreCommaForm!=null) PreCommaForm.setParent(this);
        this.FormList=FormList;
        if(FormList!=null) FormList.setParent(this);
    }

    public PreCommaForm getPreCommaForm() {
        return PreCommaForm;
    }

    public void setPreCommaForm(PreCommaForm PreCommaForm) {
        this.PreCommaForm=PreCommaForm;
    }

    public FormList getFormList() {
        return FormList;
    }

    public void setFormList(FormList FormList) {
        this.FormList=FormList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(PreCommaForm!=null) PreCommaForm.accept(visitor);
        if(FormList!=null) FormList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(PreCommaForm!=null) PreCommaForm.traverseTopDown(visitor);
        if(FormList!=null) FormList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(PreCommaForm!=null) PreCommaForm.traverseBottomUp(visitor);
        if(FormList!=null) FormList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Formistt(\n");

        if(PreCommaForm!=null)
            buffer.append(PreCommaForm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormList!=null)
            buffer.append(FormList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Formistt]");
        return buffer.toString();
    }
}
