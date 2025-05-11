package zertex.kryptoapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CryptoDetailActivity extends AppCompatActivity {

    private TextView cryptoName, cryptoPrice, cryptoChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_detail);

        // Initialize Views
        cryptoName = findViewById(R.id.cryptoName);
        cryptoPrice = findViewById(R.id.cryptoPrice);
        cryptoChange = findViewById(R.id.cryptoChange);

        // Get data from Intent
        String name = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");
        String change = getIntent().getStringExtra("change");

        // Set values
        cryptoName.setText(name);
        cryptoPrice.setText(price);
        cryptoChange.setText(change);
    }

    // Handle back button press
    public void onBackPressed() {
        super.onBackPressed();
    }
}
