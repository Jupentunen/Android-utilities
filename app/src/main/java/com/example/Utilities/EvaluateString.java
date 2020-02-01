package com.example.Utilities;
import java.util.Stack;

/**
 * Created by Juuso on 11.11.2017.
 */

public class EvaluateString {

    public static double evaluate(String expression) {
        char[] tokens = expression.toCharArray();

        Stack<Double> values = new Stack<Double>();
        Stack<Character> operators = new Stack<Character>();

        for (int i = 0; i < tokens.length; i++) {

            //Ohittaa, jos tyhjä
            if (tokens[i] == ' ') continue;

            //Jos numero, lisää values-pinoon
            if (tokens[i] >= '0' && tokens[i] <= '9') {

                StringBuffer sbuf = new StringBuffer();

                while (i < tokens.length && (tokens[i] >= '0' && tokens[i] <= '9' || tokens[i] == '.')) {
                    sbuf.append(tokens[i++]);
                }
                values.push(Double.parseDouble(sbuf.toString()));
            }

            //Jos sulku auki, lisää operators-pinoon
            else if (tokens[i] == '(') operators.push(tokens[i]);

            //Jos sulku kiinni, laske sulkeiden sisältö
            else if (tokens[i] == ')') {
                while (operators.peek() != '(') {
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                }
                operators.pop();
            }

            //Jos operaattori
            else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '•' || tokens[i] == '/') {

                //Jos operaattori-pinon päällimäinen on laskujärjestyksessä edellä tai samanarvoinen,
                //laskee pinon päällimäisen ennen nykyisen operaattorin lisäystä pinoon
                while (!operators.empty() && hasPrecedence(tokens[i], operators.peek())) {
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                }
                operators.push(tokens[i]);
            }
        }

        //Lausekkeen käsittelyn jälkeen laskee jäljellä olevat
        while (!operators.empty())
            values.push((applyOperator(operators.pop(), values.pop(), values.pop())));

        //Palauttaa laskun tuloksen
        return values.pop();
    }

    /**
     * Tarkistaa onko operaattori laskujärjestyksessä edellä aiempaa operaattoria
     * @param operator1 jota verrataan
     * @param operator2 johon verrataan
     * @return true, jos operaattori2 lasketaan ennen tai samanarvoinen
     */
    public static boolean hasPrecedence(char operator1, char operator2) {
        if (operator2 == '(' || operator2 == ')') return false;
        if ((operator1 == '•' || operator1 == '/') && (operator2 == '+' || operator2 == '-')) return false;
        else return true;
    }

    /**
     * Laskee a:n ja b:n käyttäen operaattoria
     * @param operator operaattori
     * @param b laskettava arvo
     * @param a laskettava arvo
     * @return tulos
     */
    public static double applyOperator(char operator, double b, double a) {
        switch (operator) {
            case '+': return a + b;
            case '-': return a - b;
            case '•': return a * b;
            case '/':
                if (b == 0)
                    throw new UnsupportedOperationException("Älä jaa nollalla");
                return a / b;
        }
        return 0;
    }
}
