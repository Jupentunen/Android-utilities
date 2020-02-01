package com.example.Utilities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.EmptyStackException;

import static com.example.Utilities.EvaluateString.*;

public class Calculator extends AppCompatActivity  {

    private EditText textExpression, textResult;
    private Boolean decimal, equalDone;
    private int parenthesisCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        setToolbar();

        textResult = findViewById(R.id.textResult);
        textExpression = findViewById(R.id.textExpression);

        textResult.getText().clear();
        textExpression.getText().clear();
        decimal = false;
        equalDone = false;
    }


    /**
     * Creates toolbar and sets back-button functionality
     */
    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.calculator));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(Calculator.this, MainActivity.class);
                    startActivity(intent);
                }
        });
    }

    public void addDot(View view) {
        checkExpression();
        if (!decimal) {
            if (textExpression.getText().length() < 1) {
                textExpression.setText("0.");
                decimal = true;
            }
            if (Character.isDigit(textExpression.getText().charAt(textExpression.getText().length() - 1))) {
                textExpression.setText(textExpression.getText().append("."));
                decimal = true;
            }
        }
    }

    public void addZero(View view) {
        checkExpression();
        if (checkZero()) {
            textExpression.setText(textExpression.getText().append('0'));
        }
    }

    public void addOne(View view) {
        checkExpression();
        textExpression.setText(textExpression.getText().append('1'));
    }

    public void addTwo(View view) {
        checkExpression();
        textExpression.setText(textExpression.getText().append('2'));
    }

    public void addThree(View view) {
        checkExpression();
        textExpression.setText(textExpression.getText().append('3'));
    }

    public void addFour(View view) {
        checkExpression();
        textExpression.setText(textExpression.getText().append('4'));
    }

    public void addFive(View view) {
        checkExpression();
        textExpression.setText(textExpression.getText().append('5'));
    }

    public void addSix(View view) {
        checkExpression();
        textExpression.setText(textExpression.getText().append('6'));
    }

    public void addSeven(View view) {
        checkExpression();
        textExpression.setText(textExpression.getText().append('7'));
    }

    public void addEight(View view) {
        checkExpression();
        textExpression.setText(textExpression.getText().append('8'));
    }

    public void addNine(View view) {
        checkExpression();
        textExpression.setText(textExpression.getText().append('9'));
    }

    public void addPlus(View view) {
        if (checkOperator()) {
            textExpression.setText(textExpression.getText().append(" + "));
            decimal = false;
        }
    }

    public void addMinus(View view) {
        if (checkOperator()) {
            textExpression.setText(textExpression.getText().append(" - "));
            decimal = false;
        }
    }

    public void addMultiply(View view) {
        if (checkOperator()) {
            textExpression.setText(textExpression.getText().append(" • "));
            decimal = false;
        }
    }

    public void addDivide(View view) {
        if (checkOperator()) {
            textExpression.setText(textExpression.getText().append(" / "));
            decimal = false;
        }
    }

    public void addParenthesisOpen(View view) {
        checkExpression();
        textExpression.setText(textExpression.getText().append(" ( "));
        parenthesisCount++;
    }

    public void addParenthesisClose(View view) {
        if (parenthesisCount > 0) {
            checkExpression();
            textExpression.setText(textExpression.getText().append(" ) "));
            parenthesisCount--;
        }
    }

    public void clearAll(View view) {
        clearAll();
    }

    public void backspace(View view) {
        if (textExpression.getText().length() > 0) {
            String halp = textExpression.getText().toString();
            if (halp.endsWith(".")) decimal = false;
            if (halp.endsWith("(")) parenthesisCount--;
            if (halp.endsWith(")")) parenthesisCount++;
            if (halp.endsWith(" "))textExpression.setText((halp.substring(0, halp.length() - 3)));
            else textExpression.setText(halp.substring(0, halp.length() - 1));
            equalDone = false;
            textResult.getText().clear();
        }
    }

    /**
     * Koittaa laskea lauseketta tai ilmoittelee lausekkeen virheistä
     * @param view
     */
    public void equals(View view) {
        if (textExpression.getText().length() < 1) return;

        if (parenthesisCount > 0) {
            showToast("Tarkista sulkeet!");
            return;
        }

        if (checkOperator()) {
            String halp = textExpression.getText().toString();
            if (halp.contains(" / 0 ") || halp.endsWith(" / 0")) {
                showToast("Älä jaa nollalla!");
                return;
            }

            halp = halp.replace("( -", "( 0 -");

            try {
                double result = evaluate(halp);
                textResult.setText("= " + String.valueOf(result));
                equalDone = true;
            } catch (EmptyStackException e) {
                showToast("Laskus kusee!");
            }
        }
    }

    /**
     * Tyhjentää koko roskan
     */
    public void clearAll() {
        textResult.getText().clear();
        textExpression.getText().clear();

        equalDone = false;
        decimal = false;
    }

    /**
     * Tyhjentää mahdollisen vanhan lausekkeen, kun uutta aletaan kirjoittamaan.
     */
    public void checkExpression() {
        if (equalDone) clearAll();
    }

    /**
     * Tarkistaa lausekkeen operaattorien spämmäyksen estämiseksi. Heittää operaattorin mahdollisen vastaus-arvon perään, jos lauseke tyhjä
     * @return totuusarvo
     */
    public boolean checkOperator() {
        if (equalDone) {
            textExpression.setText(textResult.getText().toString().substring(2));
            textResult.getText().clear();
            equalDone = false;
        }

        if (textExpression.getText().length() > 0) {
            String halp = textExpression.getText().toString().trim();
            return !halp.endsWith("+") || halp.endsWith("-") || halp.endsWith("/") || halp.endsWith("•");
        }

        return false;
    }

    /**
     * Tarkistaa lausekkeen pelkkien nollien spämmäyksen estämiseksi
     * @return totuusarvo
     */
    public boolean checkZero() {
        String halp = textExpression.getText().toString();
        if (halp.length() > 0) {
            if (halp.endsWith(" 0") || halp.matches("0")) return false;
        }
        return true;
    }

    /**
     * Helppo Toastin luonti
     * @param text Haluttu teksti toastiin
     */
    public void showToast(String text) {
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP,0,200);
        toast.show();
    }
}
