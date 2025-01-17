package rollingball.functions;

import java.util.function.Supplier;

import rollingball.functions.Function.Expr;

public final class BuiltinFunctions {
    private BuiltinFunctions() {
    } // Make non-instantiable

    public static Expr parseFunctionCall(String name, Expr firstParam, Supplier<Expr> paramSupplier) {
        Expr result;
        if ((result = tryParseTrigonometric(name, firstParam)) != null) {
            return result;
        }
        if ((result = tryParseMiscellaneous(name, firstParam)) != null) {
            return result;
        }
        if ((result = tryParseMultiParam(name, firstParam, paramSupplier.get())) != null) {
            return result;
        }

        throw new ParserException("Unknown function: '" + name + "'");
    }

    private static Expr tryParseTrigonometric(String name, Expr param) {
        return switch (name.toLowerCase()) {
            case "sin" -> ctx -> Math.sin(param.eval(ctx));
            case "cos" -> ctx -> Math.cos(param.eval(ctx));
            case "tan" -> ctx -> Math.tan(param.eval(ctx));
            case "asin", "arcsin" -> ctx -> Math.asin(param.eval(ctx));
            case "acos", "arccos" -> ctx -> Math.acos(param.eval(ctx));
            case "atan", "arctan" -> ctx -> Math.atan(param.eval(ctx));
            case "sinh" -> ctx -> Math.sinh(param.eval(ctx));
            case "cosh" -> ctx -> Math.cosh(param.eval(ctx));
            case "tanh" -> ctx -> Math.tanh(param.eval(ctx));
            default -> null;
        };
    }

    private static Expr tryParseMiscellaneous(String name, Expr param) {
        return switch (name.toLowerCase()) {
            case "exp" -> ctx -> Math.exp(param.eval(ctx));
            case "log", "ln" -> ctx -> Math.log(param.eval(ctx));
            case "log10", "lg" -> ctx -> Math.log10(param.eval(ctx));
            case "sqrt" -> ctx -> Math.sqrt(param.eval(ctx));
            case "cbrt" -> ctx -> Math.cbrt(param.eval(ctx));
            case "abs" -> ctx -> Math.abs(param.eval(ctx));
            case "floor" -> ctx -> Math.floor(param.eval(ctx));
            case "ceil" -> ctx -> Math.ceil(param.eval(ctx));
            case "round" -> ctx -> Math.round(param.eval(ctx));
            case "sign", "signum" -> ctx -> Math.signum(param.eval(ctx));
            default -> null;
        };
    }

    private static Expr tryParseMultiParam(String name, Expr param1, Expr param2) {
        return switch (name) {
            case "min" -> ctx -> Math.min(param1.eval(ctx), param2.eval(ctx));
            case "max" -> ctx -> Math.max(param1.eval(ctx), param2.eval(ctx));
            case "pow" -> ctx -> Math.pow(param1.eval(ctx), param2.eval(ctx));
            case "atan2" -> ctx -> Math.atan2(param1.eval(ctx), param2.eval(ctx));
            case "hypot" -> ctx -> {
                var x1 = param1.eval(ctx);
                var x2 = param2.eval(ctx);
                return Math.sqrt(x1 * x1 + x2 * x2);
            };
            default -> null;
        };
    }
}
