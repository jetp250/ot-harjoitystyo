package rollingball.expressions;

import java.util.function.ToDoubleFunction;

public final class Expressions {
    private Expressions() {} // Make non-instantiable

    public static final class EvalContext {
        public final double varX;
        public final double varT;

        public EvalContext(double varX, double varT) {
            this.varX = varX;
            this.varT = varT;
        }
    }

    public enum Op {
        ADD(1), SUB(1), MUL(2), DIV(2);

        public final int precedence;
        private Op(int precedence) {
            this.precedence = precedence;
        }

        public double apply(double lhs, double rhs) {
            return switch (this) {
                case ADD -> lhs + rhs;
                case SUB -> lhs - rhs;
                case MUL -> lhs * rhs;
                case DIV -> lhs / rhs;
            };
        }
    }

    public interface Expr {
        double evaluate(EvalContext ctx);
        Double tryConstEvaluate();

        static Expr constant(double val) {
            return new Expr() {
                @Override
                public double evaluate(EvalContext ctx) {
                    return val;
                }

                @Override
                public Double tryConstEvaluate() {
                    return val;
                }
            };
        }

        static Expr runtimeEvaluatable(ToDoubleFunction<EvalContext> eval) {
            return new Expr() {
                @Override
                public double evaluate(EvalContext ctx) {
                    return eval.applyAsDouble(ctx);
                }

                @Override
                public Double tryConstEvaluate() {
                    return null;
                }
            };
        }
        static Expr binary(Op op, Expr lhs, Expr rhs) {
            return runtimeEvaluatable(ctx -> op.apply(lhs.evaluate(ctx), rhs.evaluate(ctx)));
        }
    }
}