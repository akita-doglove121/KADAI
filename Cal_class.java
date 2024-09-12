package tyuukannkadai;

import java.util.Stack;

public class Cal_class {
    public static double cal(String formula) {
        // 空白を消して計算用の×や÷などの文字列に対応できるようにする
        formula = formula.replaceAll("×", "*");
        formula = formula.replaceAll("÷", "/");
        formula = formula.replaceAll(" ", "");
                         
        // 数値と括弧が連続する場合に掛け算を自動で補完
        formula = autoInsertMultiplication(formula);

        // 文字列の数式を解析し、計算して値を返す
        return evaluateExpression(formula);
    }

    // 省略された掛け算を補完する
    private static String autoInsertMultiplication(String formula) {
        StringBuilder updatedFormula = new StringBuilder();

        for (int i = 0; i < formula.length(); i++) {
            char currentChar = formula.charAt(i);
            updatedFormula.append(currentChar);

            // 数字の直後に開き括弧が続く場合、自動で '*' を補う
            if (i + 1 < formula.length() &&
                Character.isDigit(currentChar) &&
                formula.charAt(i + 1) == '(') {
                updatedFormula.append('*');
            }
        }

        return updatedFormula.toString();
    }

    // 数式の評価
    private static double evaluateExpression(String formula) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        double num = 0;
        char prevOp = '+';

        boolean isDecimal = false;
        double decimalFactor = 1.0;

        for (int i = 0; i < formula.length(); i++) {
            char c = formula.charAt(i);

            if (Character.isDigit(c)) {
                if (isDecimal) {
                    decimalFactor /= 10;
                    num += (c - '0') * decimalFactor;
                } else {
                    num = num * 10 + (c - '0');
                }
            } else if (c == '.') {
                isDecimal = true;
            } else {
                // 演算子や括弧の処理
                if (c == '(') {
                    int j = findClosingParenthesis(formula, i);
                    num = evaluateExpression(formula.substring(i + 1, j));
                    i = j;
                } else if (c == ')') {
                    // 括弧閉じ
                    continue;
                }

                // 現在の演算子で数値を処理
                if (prevOp == '+') {
                    numbers.push(num);
                } else if (prevOp == '-') {
                    numbers.push(-num);
                } else if (prevOp == '*') {
                    numbers.push(numbers.pop() * num);
                } else if (prevOp == '/') {
                    numbers.push(numbers.pop() / num);
                }
                
                System.out.printf("num: %.2f, prevOp: %c, numbers: %s%n", num, prevOp, numbers);

                
                prevOp = c;
                num = 0;
                isDecimal = false;
                decimalFactor = 1.0;
            }
        }

        // 最後の数値を処理
        if (prevOp == '+') {
            numbers.push(num);
        } else if (prevOp == '-') {
            numbers.push(-num);
        } else if (prevOp == '*') {
            numbers.push(numbers.pop() * num);
        } else if (prevOp == '/') {
            numbers.push(numbers.pop() / num);
        }
        
        double result = 0;
        while (!numbers.isEmpty()) {
            result += numbers.pop();
        }
     // デバッグ用出力
        System.out.printf("Final result: %.2f%n", result);
        return result;
    }

    // 括弧の対応を見つける
    private static int findClosingParenthesis(String formula, int startIndex) {
        int count = 0;
        for (int i = startIndex; i < formula.length(); i++) {
            if (formula.charAt(i) == '(') count++;
            if (formula.charAt(i) == ')') count--;
            if (count == 0) return i;
        }
        throw new IllegalArgumentException("括弧が一致しません");
    }
}