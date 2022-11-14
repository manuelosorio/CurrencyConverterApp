package cop2805.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    Button next;
    Intent conversionIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addOnButtonListener();
    }
    public void addOnButtonListener() {
        this.radioGroup = findViewById(R.id.radioGroup);
        this.next = findViewById(R.id.next);

        this.next.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            this.radioButton = findViewById(selectedId);

            if (this.radioButton != null) {
                CharSequence selectedRadioButton = radioButton.getText();
                this.conversionIntent = new Intent(this, ConversionActivity.class);
                conversionIntent.putExtra("currency", selectedRadioButton);
                this.getRate("USD", (String) selectedRadioButton);
            } else {
                Toast.makeText(this, "Please choice an option.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // This makes a request to a nodejs serverless function I created as an abstraction layer
    // with the sole purpose of caching requested base/target rates to protect my API KEY.
    // Doing this also allows for future proofing in case I want to further this project and
    // do other currency conversion like (CAD to EUR) or any other outside of the
    // ones for this assignment.
    //
    // I provided that code in the submission too or it can be view on my github:
    // https://github.com/manuelosorio/currency-converter-serverless
    private void getRate(String base,
                         String target) {
        RequestQueue volleyQueue = Volley.newRequestQueue(this);
        String host ="https://beamish-duckanoo-fc4afa.netlify.app";
        String url = host + "/.netlify/functions/get-rate?base=" + base +"&target=" + target;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        conversionIntent.putExtra("rate", response.getDouble("rate"));
                        startActivity(conversionIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    if (Objects.requireNonNull(error.getLocalizedMessage()).contains("Unable to resolve host")) {

                        Toast.makeText(this, "Unable to connect to host", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        volleyQueue.add(jsonObjectRequest);
    }
}