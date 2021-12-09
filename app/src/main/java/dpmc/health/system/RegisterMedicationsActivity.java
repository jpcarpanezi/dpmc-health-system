package dpmc.health.system;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import dpmc.health.system.interfaces.MedicationsClient;
import dpmc.health.system.models.RegisterMedicines;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterMedicationsActivity extends AppCompatActivity {
    private MedicationsClient client;
    private TextView drugName;
    private TextView activeIngredient;
    private TextView formRoute;
    private TextView company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_medications);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_register_medications);

        this.drugName = (TextView) findViewById(R.id.drugName);
        this.activeIngredient = (TextView) findViewById(R.id.activeIngredient);
        this.formRoute = (TextView) findViewById(R.id.formRoute);
        this.company = (TextView) findViewById(R.id.company);

        this.retrofitBuild();
    }

    private void retrofitBuild() {
        String API_BASE_URL = "http://207.244.232.139:1234/";

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit =
                builder
                        .client(httpClient.build())
                        .build();

        this.client = retrofit.create(MedicationsClient.class);
    }

    private boolean validateFields() {
        boolean hasErrors = false;

        if (drugName.length() == 0) {
            drugName.setError("Este campo é obrigatório");
            hasErrors = true;
        }

        if (activeIngredient.length() == 0) {
            activeIngredient.setError("Este campo é obrigatório");
            hasErrors = true;
        }

        if (formRoute.length() == 0) {
            formRoute.setError("Este campo é obrigatório");
            hasErrors = true;
        }

        if (company.length() == 0) {
            company.setError("Este campo é obrigatório");
            hasErrors = true;
        }

        return hasErrors;
    }

    public void registerMedication(View view) {
        if (validateFields()) {
            return;
        }

        RegisterMedicines registerMedicines = new RegisterMedicines(drugName.getText().toString(), activeIngredient.getText().toString(), formRoute.getText().toString(), company.getText().toString());

        Call<RegisterMedicines> call = this.client.registerMedicines(registerMedicines);
        call.enqueue(new Callback<RegisterMedicines>() {
            @Override
            public void onResponse(Call<RegisterMedicines> call, Response<RegisterMedicines> response) {
                switch (response.code()) {
                    case 201:
                        drugName.setText(null);
                        activeIngredient.setText(null);
                        formRoute.setText(null);
                        company.setText(null);

                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                        Toast successToast = Toast.makeText(getApplicationContext(), "Cadastrado com sucesso", Toast.LENGTH_LONG);
                        successToast.show();
                        break;
                    default:
                        Toast failureToast = Toast.makeText(getApplicationContext(), "Ocorreu um erro", Toast.LENGTH_LONG);
                        failureToast.show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<RegisterMedicines> call, Throwable t) {
                Toast failureToast = Toast.makeText(getApplicationContext(), "Ocorreu um erro", Toast.LENGTH_LONG);
                failureToast.show();
            }
        });
    }
}