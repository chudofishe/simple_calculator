import java.util.Scanner;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {

        System.out.println("Введите алгебраическое выражение, к примеру  (12 + х) * 23 + y");
        System.out.println("Поддерживаемые операции: + - * / и скобки. Только целые не отрицательные числа в формате" +
                " x или var1.");

        Scanner scanner = new Scanner(System.in);
        String expression;

        do {
            expression = Stream.of(scanner.nextLine().replace(" ", ""))
                    .map(String::toLowerCase)
                    .reduce("", String::concat);
        } while(!Parser.checkExpression(expression));


        String[] infix = Parser.assignVariables(scanner, expression);

        Tree AST = Parser.RPNtoAST(Parser.infixToRPN(infix));

        // т.к. деление и умножение целочисленные, из-за округления получается погрешность
        System.out.println(AST.accept(new TreeEvaluator()));

    }
}
