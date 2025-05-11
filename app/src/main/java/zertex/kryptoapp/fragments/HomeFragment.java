package zertex.kryptoapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import zertex.kryptoapp.CryptoDetailActivity;
import zertex.kryptoapp.R;
import zertex.kryptoapp.model.CryptoModel;
import zertex.kryptoapp.model.HomeViewModel;

public class HomeFragment extends Fragment {

    private LinearLayout tabGainers, tabLosers, gainersContainer, losersContainer;
    private HomeViewModel homeViewModel;

    // Horizontal card views
    private LinearLayout bitcoinCard, ethereumCard, litecoinCard, cardanoCard, polkadotCard;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize tab and container views
        tabGainers = view.findViewById(R.id.tabGainers);
        tabLosers = view.findViewById(R.id.tabLosers);
        gainersContainer = view.findViewById(R.id.gainersContainer);
        losersContainer = view.findViewById(R.id.losersContainer);

        // Initialize horizontal cards
        bitcoinCard = view.findViewById(R.id.bitcoinCard);
        ethereumCard = view.findViewById(R.id.ethereumCard);
        litecoinCard = view.findViewById(R.id.litecoinCard);
        cardanoCard = view.findViewById(R.id.cardanoCard);
        polkadotCard = view.findViewById(R.id.polkadotCard);

        // Set tab switcher
        tabGainers.setOnClickListener(v -> switchTab(true));
        tabLosers.setOnClickListener(v -> switchTab(false));
        switchTab(true); // Default

        // ViewModel setup
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        homeViewModel.getCryptoList().observe(getViewLifecycleOwner(), new Observer<List<CryptoModel>>() {
            @Override
            public void onChanged(List<CryptoModel> cryptoModels) {
                if (cryptoModels != null) {
                    updateCryptoContainers(cryptoModels);
                    updateHorizontalCards(cryptoModels);
                }
            }
        });

        homeViewModel.fetchCryptoData("usd", "market_cap_desc", 10, 1, false);

        return view;
    }

    private void updateHorizontalCards(List<CryptoModel> list) {
        for (CryptoModel crypto : list) {
            String name = crypto.getName();
            String symbol = crypto.getSymbol();
            String price = "$" + crypto.getCurrentPrice();
            String change = crypto.getPriceChangePercentage24h() + "%";
            String tag = name + "|" + symbol + "|" + crypto.getCurrentPrice() + "|" + crypto.getPriceChangePercentage24h();

            if (name.equalsIgnoreCase("Bitcoin")) {
                updateCard(bitcoinCard, name, price, tag);
            } else if (name.equalsIgnoreCase("Ethereum")) {
                updateCard(ethereumCard, name, price, tag);
            } else if (name.equalsIgnoreCase("Litecoin")) {
                updateCard(litecoinCard, name, price, tag);
            } else if (name.equalsIgnoreCase("Cardano")) {
                updateCard(cardanoCard, name, price, tag);
            } else if (name.equalsIgnoreCase("Polkadot")) {
                updateCard(polkadotCard, name, price, tag);
            }
        }
    }

    private void updateCard(LinearLayout cardLayout, String name, String price, String tag) {
        TextView nameView = (TextView) cardLayout.getChildAt(1);
        TextView priceView = (TextView) cardLayout.getChildAt(2);
        nameView.setText(name);
        priceView.setText(price);
        cardLayout.setTag(tag);
        cardLayout.setOnClickListener(this::openCryptoDetail);
    }

    private void updateCryptoContainers(List<CryptoModel> cryptoModels) {
        gainersContainer.removeAllViews();
        losersContainer.removeAllViews();

        for (CryptoModel crypto : cryptoModels) {
            View cryptoCard = LayoutInflater.from(getContext()).inflate(R.layout.item_crypto_card, null);
            TextView name = cryptoCard.findViewById(R.id.cryptoName);
            TextView price = cryptoCard.findViewById(R.id.cryptoPrice);
            TextView change = cryptoCard.findViewById(R.id.cryptoChange);

            name.setText(crypto.getName());
            price.setText("$" + crypto.getCurrentPrice());
            change.setText(crypto.getPriceChangePercentage24h() + "%");

            cryptoCard.setTag(crypto.getName() + "|" + crypto.getSymbol() + "|" + crypto.getCurrentPrice() + "|" + crypto.getPriceChangePercentage24h());
            cryptoCard.setOnClickListener(this::openCryptoDetail);

            if (crypto.getPriceChangePercentage24h() > 0)
                {
                gainersContainer.addView(cryptoCard);
            } else {
                losersContainer.addView(cryptoCard);
            }
        }
    }

    private void switchTab(boolean isGainers) {
        if (getContext() == null) return;

        tabGainers.setBackground(ContextCompat.getDrawable(getContext(), isGainers ? R.drawable.tab_selected : R.drawable.tab_unselected));
        tabLosers.setBackground(ContextCompat.getDrawable(getContext(), !isGainers ? R.drawable.tab_selected : R.drawable.tab_unselected));

        TextView tabGainersText = tabGainers.findViewById(R.id.tabGainersText);
        TextView tabLosersText = tabLosers.findViewById(R.id.tabLosersText);

        tabGainersText.setTextColor(ContextCompat.getColor(getContext(), isGainers ? R.color.white : R.color.grey));
        tabLosersText.setTextColor(ContextCompat.getColor(getContext(), !isGainers ? R.color.white : R.color.grey));

        gainersContainer.setVisibility(isGainers ? View.VISIBLE : View.GONE);
        losersContainer.setVisibility(isGainers ? View.GONE : View.VISIBLE);
    }

    public void openCryptoDetail(View v) {
        String tag = (String) v.getTag();
        if (getContext() == null || tag == null) return;

        String[] data = tag.split("\\|");
        Intent intent = new Intent(getContext(), CryptoDetailActivity.class);
        intent.putExtra("name", data[0]);
        intent.putExtra("symbol", data[1]);
        intent.putExtra("price", data[2]);
        intent.putExtra("change", data[3]);
        startActivity(intent);
    }
}
