package dpmc.health.system;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.evrencoskun.tableview.TableView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import dpmc.health.system.interfaces.ManageMedicationsClient;
import dpmc.health.system.models.MedicinesContent;
import dpmc.health.system.models.MedicinesPaginatedView;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManageMedicationsActivity extends AppCompatActivity {
    private ManageMedicationsClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_medications);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_manage_medications);

        this.retrofitBuild();
        this.getMedications();
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

        this.client =  retrofit.create(ManageMedicationsClient.class);
    }

    public void getMedications() {


        Call<MedicinesPaginatedView> call = this.client.getMedicines(0, 10);
        call.enqueue(new Callback<MedicinesPaginatedView>() {
            @Override
            public void onResponse(Call<MedicinesPaginatedView> call, Response<MedicinesPaginatedView> response) {
                MedicinesPaginatedView medicinesPaginatedView = response.body();
                TableView tv = (TableView) findViewById(R.id.tableView);

//                for (MedicinesContent medicinesContent : medicinesPaginatedView.getMedicines().getContent()) {
//                    TableLayout manageMedicationsTable = (TableLayout) findViewById(R.id.manageMedicationsTable);
//
//                    TableRow row = new TableRow(ManageMedicationsActivity.this);
//                    TextView manufacturerHeader = (TextView) findViewById(R.id.manufacturerHeader);
//
//                    TextView name = new TextView(ManageMedicationsActivity.this);
//                    name.setLayoutParams(manufacturerHeader.getLayoutParams());
//                    name.setSingleLine(true);
//                    name.setEllipsize(TextUtils.TruncateAt.END);
//                    name.setText(medicinesContent.getDrugName());
//
//                    TextView principle = new TextView(ManageMedicationsActivity.this);
//                    principle.setLayoutParams(manufacturerHeader.getLayoutParams());
//                    principle.setSingleLine(true);
//                    principle.setEllipsize(TextUtils.TruncateAt.END);
//                    principle.setText(medicinesContent.getActiveIngredient());
//
//                    TextView ingestion = new TextView(ManageMedicationsActivity.this);
//                    ingestion.setLayoutParams(manufacturerHeader.getLayoutParams());
//                    ingestion.setSingleLine(true);
//                    ingestion.setEllipsize(TextUtils.TruncateAt.END);
//                    ingestion.setText(medicinesContent.getFormRoute());
//
//                    TextView manufacturer = new TextView(ManageMedicationsActivity.this);
//                    manufacturer.setLayoutParams(manufacturerHeader.getLayoutParams());
//                    manufacturer.setSingleLine(true);
//                    manufacturer.setEllipsize(TextUtils.TruncateAt.END);
//                    manufacturer.setText(medicinesContent.getCompany());
//
//                    row.addView(name);
//                    row.addView(principle);
//                    row.addView(ingestion);
//                    row.addView(manufacturer);
//
//                    manageMedicationsTable.addView(row);
//                }
            }

            @Override
            public void onFailure(Call<MedicinesPaginatedView> call, Throwable t) {

            }
        });
    }
}