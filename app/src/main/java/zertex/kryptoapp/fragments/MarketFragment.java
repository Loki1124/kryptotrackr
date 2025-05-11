package zertex.kryptoapp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import zertex.kryptoapp.api.CryptoApiService;
import zertex.kryptoapp.model.CryptoModel;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import zertex.kryptoapp.CryptoDetailActivity;
import zertex.kryptoapp.R;

public class MarketFragment extends Fragment {

    private LinearLayout cryptoListContainer;
    private EditText searchBar;
    private List<Crypto> allCryptos = new ArrayList<>();

    public MarketFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);

        cryptoListContainer = view.findViewById(R.id.cryptoListContainer);
        searchBar = view.findViewById(R.id.searchBar);

        loadLiveCryptoData(); // instead of loadDummyData();

        showCryptos(allCryptos);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCryptos(s.toString());
            }
        });

        return view;
    }

    private void loadLiveCryptoData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.coingecko.com/api/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CryptoApiService api = retrofit.create(CryptoApiService.class);

        api.getMarketData("usd", "market_cap_desc", 50, 1, false)
                .enqueue(new Callback<List<CryptoModel>>() {
                    @Override
                    public void onResponse(Call<List<CryptoModel>> call, Response<List<CryptoModel>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            allCryptos.clear();
                            for (CryptoModel model : response.body()) {
                                allCryptos.add(new Crypto(
                                        model.getName(),
                                        model.getSymbol().toUpperCase(),
                                        "$" + model.getCurrentPrice(),
                                        String.format("%.2f%%", model.getPriceChangePercentage24h())
                                ));
                            }

                            showCryptos(allCryptos);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CryptoModel>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }


    private void showCryptos(List<Crypto> cryptoList) {
        cryptoListContainer.removeAllViews();

        for (Crypto crypto : cryptoList) {
            TextView item = new TextView(getContext());
            item.setText(crypto.name + " (" + crypto.symbol + ") • " + crypto.price + " • " + crypto.change);
            item.setTextSize(16);
            item.setTextColor(Color.WHITE);
            item.setPadding(24, 24, 24, 24);

            // Click to open detail
            item.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), CryptoDetailActivity.class);
                intent.putExtra("name", crypto.name);
                intent.putExtra("symbol", crypto.symbol);
                intent.putExtra("price", crypto.price);
                intent.putExtra("change", crypto.change);
                startActivity(intent);
            });

            cryptoListContainer.addView(item);
        }
    }

    private void filterCryptos(String query) {
        List<Crypto> filtered = new ArrayList<>();
        for (Crypto crypto : allCryptos) {
            if (crypto.name.toLowerCase().contains(query.toLowerCase()) ||
                    crypto.symbol.toLowerCase().contains(query.toLowerCase())) {
                filtered.add(crypto);
            }
        }
        showCryptos(filtered);
    }

    // Model class
    static class Crypto {
        String name, symbol, price, change;

        Crypto(String name, String symbol, String price, String change) {
            this.name = name;
            this.symbol = symbol;
            this.price = price;
            this.change = change;
        }
    }
}
