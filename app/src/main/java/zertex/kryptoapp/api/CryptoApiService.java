package zertex.kryptoapp.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import zertex.kryptoapp.model.CryptoModel;

public interface CryptoApiService {
    @GET("coins/markets")
    Call<List<CryptoModel>> getMarketData(
            @Query("vs_currency") String currency,
            @Query("order") String order,
            @Query("per_page") int perPage,
            @Query("page") int page,
            @Query("sparkline") boolean sparkline
    );
}
