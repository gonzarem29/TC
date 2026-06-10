public class Quadruple {
    public String op;
    public String arg1;
    public String arg2;
    public String result;

    public Quadruple(String op, String arg1, String arg2, String result) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    @Override
    public String toString() {
        if (op.equals("label")) {
            return result + ":";
        }
        if (op.equals("goto")) {
            return "goto " + result;
        }
        if (op.equals("if_False")) {
            return "if_False " + arg1 + " goto " + result;
        }
        if (op.equals("param")) {
            return "param " + arg1;
        }
        if (op.equals("call")) {
            return result + " = call " + arg1;
        }
        if (op.equals("return")) {
            if (arg1 != null) return "return " + arg1;
            return "return";
        }
        if (op.equals("=")) {
            return result + " = " + arg1;
        }
        return result + " = " + arg1 + " " + op + " " + arg2;
    }
}
