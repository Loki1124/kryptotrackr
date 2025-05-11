package zertex.kryptoapp.model;

import com.google.gson.annotations.SerializedName;

public class CryptoModel {

    @SerializedName("name")
    private String name;

    @SerializedName("symbol")
    private String symbol;

    @SerializedName("current_price")
    private double currentPrice;

    @SerializedName("price_change_percentage_24h")
    private double priceChangePercentage24h;

    @SerializedName("image")
    private String imageUrl;

    // Getters
    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public double getPriceChangePercentage24h() {
        return priceChangePercentage24h;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
