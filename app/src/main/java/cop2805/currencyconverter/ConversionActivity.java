package cop2805.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ConversionActivity extends AppCompatActivity {

    Bundle extras;
    double rate;
    EditText inputTarget;
    EditText inputBase;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion);

        TextView currencyLabelTwo = findViewById(R.id.currency_label_two);

        extras = getIntent().getExtras();
        this.inputBase = findViewById(R.id.input_base);
        inputBase.setText("1.00");
        this.inputTarget = findViewById(R.id.input_target);
        if (extras != null) {
            String currency = extras.getString("currency");
            this.rate = extras.getDouble("rate");

            BigDecimal rounded = new BigDecimal(rate).setScale(4, RoundingMode.HALF_UP);
            inputTarget.setText(rounded.toString());
            switch (currency) {
                case ("JPY"): {
                    currencyLabelTwo.setText(R.string.jpy);
                    break;
                }
                case ("EUR"): {
                    currencyLabelTwo.setText(R.string.eur);
                    break;
                }
                case ("CAD"): {
                    currencyLabelTwo.setText(R.string.cad);
                    break;
                }
            }
        }
        addEventListeners();
    }
    public void goBack (View view) {
        finish();
    }

    @SuppressLint("SetTextI18n")
    public void addEventListeners() {

        try {
                this.inputBase.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String valueFrom = inputBase.getText().toString();
                        if (getCurrentFocus().getId() == inputBase.getId()) {
                            if (!valueFrom.equals("")) {
                                double value = Double.parseDouble(valueFrom) * rate;
                                BigDecimal rounded = new BigDecimal(value).setScale(4, RoundingMode.HALF_UP);
                                inputTarget.setText(rounded.toString());
                            }
                        }
                    }
                });


                this.inputTarget.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        String valueTo = inputTarget.getText().toString();
                        if (getCurrentFocus().getId() == inputTarget.getId()) {
                            if (!valueTo.equals("")) {
                                double value = Double.parseDouble(valueTo) / rate;
                                BigDecimal rounded = new BigDecimal(value).setScale(4, RoundingMode.HALF_UP);
                                inputBase.setText(rounded.toString());
                            }
                        }
                    }
                });
        } catch (Exception exception) {
            exception.getStackTrace();
        }
    }
}