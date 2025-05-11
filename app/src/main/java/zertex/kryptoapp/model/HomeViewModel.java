package zertex.kryptoapp.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zertex.kryptoapp.api.CryptoApiService;
import zertex.kryptoapp.api.RetrofitClient;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<CryptoModel>> cryptoList = new MutableLiveData<>();
    private final CryptoApiService apiService;

    public HomeViewModel() {
        apiService = RetrofitClient.getInstance().create(CryptoApiService.class);
    }

    public LiveData<List<CryptoModel>> getCryptoList() {
        return cryptoList;
    }

    public void fetchCryptoData(String vsCurrency, String order, int perPage, int page, boolean sparkline) {
        apiService.getMarketData(vsCurrency, order, perPage, page, sparkline)
                .enqueue(new Callback<List<CryptoModel>>() {
                    @Override
                    public void onResponse(Call<List<CryptoModel>> call, Response<List<CryptoModel>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            cryptoList.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CryptoModel>> call, Throwable t) {
                        // You can log the error here
                    }
                });
    }
}
