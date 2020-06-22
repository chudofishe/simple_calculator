import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static final int LEFT_ASSOC  = 0;
    private static final int RIGHT_ASSOC = 1;

    private static final Map<String, int[]> OPERATORS = new HashMap<>();
    static
    {
        OPERATORS.put("+", new int[] { 0, LEFT_ASSOC });
        OPERATORS.put("-", new int[] { 0, LEFT_ASSOC });
        OPERATORS.put("*", new int[] { 5, LEFT_ASSOC });
        OPERATORS.put("/", new int[] { 5, LEFT_ASSOC });
    }

    private static boolean isOperator(String token) {
        return OPERATORS.containsKey(token);
    }

    // Проверка на ассоциативность операции
    private static boolean isAssociative(String token, int type)
    {
        if (!isOperator(token))
        {
            throw new IllegalArgumentException("Invalid token: " + token);
        }

        if (OPERATORS.get(token)[1] == type) {
            return true;
        }
        return false;
    }

    // Сравниваем порядок операторов
    private static final int cmpPrecedence(String token1, String token2)
    {
        if (!isOperator(token1) || !isOperator(token2))
        {
            throw new IllegalArgumentException("Invalid tokens: " + token1
                    + " " + token2);
        }
        return OPERATORS.get(token1)[0] - OPERATORS.get(token2)[0];
    }

    // Алгоритм сортировочной станции
    // Переводим выражение из инфиксной нотации в польскую для постраения АСТ
    public static String[] infixToRPN(String[] inputTokens)
    {
        ArrayList<String> out = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        for (String token : inputTokens)
        {
            if (isOperator(token))
            {
                while (!stack.empty() && isOperator(stack.peek()))
                {
                    if ((isAssociative(token, LEFT_ASSOC)         &&
                            cmpPrecedence(token, stack.peek()) <= 0) ||
                            (isAssociative(token, RIGHT_ASSOC)        &&
                                    cmpPrecedence(token, stack.peek()) < 0))
                    {
                        out.add(stack.pop());
                        continue;
                    }
                    break;
                }
                stack.push(token);
            }
            else if (token.equals("("))
            {
                stack.push(token);  //
            }
            else if (token.equals(")"))
            {
                while (!stack.empty() && !stack.peek().equals("("))
                {
                    out.add(stack.pop());
                }
                stack.pop();
            }
            // Если токен - число, добавляем его в аутпут
            else
            {
                out.add(token);
            }
        }
        while (!stack.empty())
        {
            out.add(stack.pop());
        }
        String[] output = new String[out.size()];
        return out.toArray(output);
    }

    public static String[] assignVariables (Scanner scanner, String expression) {
        // Узнаем кол-во уникальных переменных
        Matcher matcher = Pattern.compile("([a-z]+[0-9]*)").matcher(expression);
        Set<String> variables = new HashSet<>();
        while (matcher.find()) {
            variables.add(matcher.group());
        }
        //Ввод значений переменных
        while (variables.size() > 0){
            String[] line = scanner.nextLine().replace(" ", "").split("=");
            if (variables.contains(line[0])) {
                expression = expression.replaceAll(line[0], line[1]);
                variables.remove(line[0]);
            } else {
                System.out.println("Такой переменной в выражении нет");
            }
        }
        return expression.split("((?<=\\D)|(?=\\D))");
    }

    public static Tree RPNtoAST(String[] tokens)
    {
        Stack<TreeNode> stack = new Stack<>();

        /* Вообще здесь можно сразу посчитать значение выражения, но можно и построить дерево. */

        for (String token : tokens)
        {
            // Если токен число - добавляем в стек
            if (!isOperator(token))
            {
                stack.push(new NumberNode(Integer.valueOf(token)));
            }
            else
            {
                // Токен оператор, так что строим из него ветвь
                TreeNode right = stack.pop();
                TreeNode left = stack.pop();

                BinaryNode binaryNode = new BinaryNode(left, right, token);

                stack.push(binaryNode);
            }
        }

        return new Tree((BinaryNode) stack.pop());
    }

    public static boolean checkExpression (String expression){
        Pattern illegalSigns = Pattern.compile("[^\\w()+-\\/*]|[.,]|([a-z]*[\\d]+[a-z]+)|([\\d]+[a-z]+)");
        Matcher matcher = illegalSigns.matcher(expression);

        if (matcher.find()) {
            String sign = matcher.group();
            if (sign.equals(",") || sign.equals(".")) {
                System.out.println("Работаем только с целыми положительными числами");
                return false;
            }
            System.out.println("Не корректная операция или переменная " + sign);
            return false;
        }

        if (expression.chars().filter(x -> x == '(').count()
                != expression.chars().filter(x -> x == ')').count()) {
            System.out.println("У вас скобки не закрыты");
            return false;
        }

        String[] noOperators = expression.replaceAll("[()]", "").split("[+-\\/*]");
        String[] noNumbers = expression.replaceAll("[()\\w]", "").split("");

        if (noOperators.length - noNumbers.length != 1) {
            System.out.println("В выражении неверное число операций");
            return false;
        }

        System.out.println("Какие значения у переменных?");
        return true;
    }
}
