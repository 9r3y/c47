package com.y3r9.c47.dog.algorithms.alg;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Evaluation {
    private static Map<String, Integer> opLvl;

    static {
        init();
    }

    private static void init() {
        opLvl = new HashMap<String, Integer>();
        opLvl.put("+", 0);
        opLvl.put("-", 0);
        opLvl.put("*", 1);
        opLvl.put("/", 1);
        opLvl.put("sqr", 2);
        opLvl.put("(", -1);
        opLvl.put(")", -2);
    }

    /**
     * 将中缀表达式转换成后缀表达式
     */
    public static String nifix2Postfix(String nifix) {
        Stack<String> stack = new Stack<String>();
        StringBuilder ret = new StringBuilder("");
        int length = nifix.length();
        int begin = 0;
        boolean numStat = false;
        char c = nifix.charAt(0);
        if (Character.isDigit(c) || c == '.') {
            numStat = true;
        }

        for (int i = 1; i < length; i++) {
            c = nifix.charAt(i);
            if (Character.isDigit(c) || c == '.') { // meet operands
                if (!numStat) {
                    String op = nifix.substring(begin, i);
                    begin = i;

                    int lvl = opLvl.get(op);
                    switch (lvl) {
                        case 2:
                        case -1:
                            stack.push(op);
                            break;
                        case -2:
                            while (true) {
                                String leftOp = stack.pop();
                                if (!"(".equals(leftOp)) {
                                    ret.append(leftOp);
                                } else {
                                    break;
                                }
                            }
                            if (opLvl.get(stack.peek()) >= 2) {
                                ret.append(stack.pop());
                            }
                            break;
                        default:
                            if (!stack.isEmpty()) {
                                String topOp = stack.peek();
                                if (opLvl.get(topOp) <= lvl) {
                                    stack.push(op);
                                } else {
                                    while (opLvl.get(stack.peek()) > lvl) {
                                        ret.append(stack.pop());
                                    }
                                }
                            } else {
                                stack.push(op);
                            }
                            break;
                    }
                    numStat = true;
                }
            } else { // meet operator
                if (numStat) {
                    ret.append(nifix.substring(begin, i));
                    begin = i;
                    numStat = false;
                }
            }
        }
        while (stack.size() != 0) {
            ret.append(stack.pop());
        }

        return ret.toString();
    }

    public static void main(String[] args) {
        Evaluation.init();
        System.out.println(nifix2Postfix("9+(3-1)*3+10/2"));
    }

    /**
     * 对后缀表达式求值
     */
    public double envaluate(String postfix) {
        double ret = -1;

        return ret;
    }

}
