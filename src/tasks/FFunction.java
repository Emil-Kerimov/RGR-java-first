package tasks;

import edu.hws.jcm.data.Expression;
import edu.hws.jcm.data.Parser;
import edu.hws.jcm.data.Variable;

public class FFunction implements Evaluatable{

    private Parser parser;
    private Variable par;
    private Variable var;
    private Expression fun;
    private Expression der;

    public FFunction(String funStr) {

        parser = new Parser(Parser.STANDARD_FUNCTIONS);
        var = new Variable("x",0);
        par = new Variable("a",1);
        parser.add(var);
        parser.add(par);

        fun = parser.parse(funStr);
        der = fun.derivative(var);
    }

    public FFunction(String funStr, double a) {


        parser = new Parser(Parser.STANDARD_FUNCTIONS);
        var = new Variable("x");
        par = new Variable("a",a);
        parser.add(var);
        parser.add(par);

        fun = parser.parse(funStr);
        der = fun.derivative(var);
    }

    public double getA() {
        return par.getVal();
    }

    public void setA(double a) {
        this.par.setVal(a);
    }


    public String getFun() {
        return fun.toString();
    }

    public String  getDer() {
        return der.toString();
    }

    public void setFun(String funStr){
        if(funStr.equals(fun.toString())) return;
        fun = parser.parse(funStr);
        der = fun.derivative(var);
    }

    public double derivative(double x){
        var.setVal(x);
        return der.getVal();
    }


    @Override
    public double evalf(double x) {
        var.setVal(x);
        return fun.getVal();
    }
}