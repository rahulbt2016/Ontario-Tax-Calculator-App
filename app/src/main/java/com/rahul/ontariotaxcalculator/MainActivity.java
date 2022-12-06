package com.rahul.ontariotaxcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;

public class MainActivity extends AppCompatActivity {

    private EditText totalIncome;
    private Slider rrspContribution;
    private TextView federalTax, provincialTax, totalTax, afterTaxIncome, rrspContributionTV;
    private TaxCalculation taxCalculation;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String TOTAL_INCOME = "totalIncome";
    private static final String RRSP_CONTRIBUTION = "rrspContribution";

    private double loadedTotalIncome, loadedRrspContribution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalIncome = (EditText) findViewById(R.id.totalIncome);
        rrspContribution = (Slider) findViewById(R.id.rrspContribution);
        rrspContributionTV = (TextView) findViewById(R.id.rrspContributionTV);
        federalTax = (TextView) findViewById(R.id.federalTax);
        provincialTax = (TextView) findViewById(R.id.provincialTax);
        totalTax = (TextView) findViewById(R.id.totalTax);
        afterTaxIncome = (TextView) findViewById(R.id.afterTaxIncome);

        loadData();
        loadView();

        totalIncome.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                saveData();
                refreshView();
            }
        });

        rrspContribution.addOnChangeListener((slider, value, fromUser) -> {
            saveData();
            refreshView();
        });

    }

    protected void refreshView() {

        double newTotalIncome, newRRSP;

        if (totalIncome.getText().toString().isEmpty())
            newTotalIncome = 0;
        else
            newTotalIncome = Double.parseDouble(totalIncome.getText().toString());

        newRRSP = rrspContribution.getValue();

        taxCalculation = new TaxCalculation(newTotalIncome, newRRSP);

        rrspContributionTV.setText("$" + String.format("%,.2f", newRRSP));
        federalTax.setText("$" + String.format("%,.2f", taxCalculation.getFederalTax()));
        provincialTax.setText("$" + String.format("%,.2f", taxCalculation.getProvincialTax()));
        totalTax.setText("$" + String.format("%,.2f", taxCalculation.getTotalTax()));
        afterTaxIncome.setText("$" + String.format("%,.2f", taxCalculation.getAfterTaxIncome()));
    }

    protected void saveData() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TOTAL_INCOME, totalIncome.getText().toString());
        editor.putFloat(RRSP_CONTRIBUTION, rrspContribution.getValue());

        editor.apply();
    }

    protected void loadData() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        if(sharedPreferences.getString(TOTAL_INCOME, "0").isEmpty())
            loadedTotalIncome = 0;
        else
            loadedTotalIncome = Double.parseDouble(sharedPreferences.getString(TOTAL_INCOME, "0"));
        loadedRrspContribution = sharedPreferences.getFloat(RRSP_CONTRIBUTION, 0);
    }

    protected void loadView() {

        totalIncome.setText(Double.toString(loadedTotalIncome));
        rrspContribution.setValue((float) loadedRrspContribution);

        taxCalculation = new TaxCalculation(loadedTotalIncome, loadedRrspContribution);

        rrspContributionTV.setText("$" + String.format("%,.2f", loadedRrspContribution));
        federalTax.setText("$" + String.format("%,.2f", taxCalculation.getFederalTax()));
        provincialTax.setText("$" + String.format("%,.2f", taxCalculation.getProvincialTax()));
        totalTax.setText("$" + String.format("%,.2f", taxCalculation.getTotalTax()));
        afterTaxIncome.setText("$" + String.format("%,.2f", taxCalculation.getAfterTaxIncome()));
    }
}