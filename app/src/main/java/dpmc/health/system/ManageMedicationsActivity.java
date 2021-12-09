package dpmc.health.system;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.evrencoskun.tableview.TableView;

import dpmc.health.system.interfaces.MedicationsClient;
import dpmc.health.system.models.MedicinesContent;
import dpmc.health.system.models.MedicinesPaginatedView;
import dpmc.health.system.models.RegisterMedicines;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManageMedicationsActivity extends AppCompatActivity {
    private MedicationsClient client;
    private EditText drugName;
    private EditText activeIngredient;
    private EditText formRoute;
    private EditText company;

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

        this.client =  retrofit.create(MedicationsClient.class);
    }

    private void editMedications(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Medicamento");

        LinearLayout layout = new LinearLayout(this);
        layout.setPadding(55, 55, 55, 55);
        layout.setOrientation(LinearLayout.VERTICAL);

        drugName = new EditText(this);
        drugName.setHint("Nome do remédio");
        layout.addView(drugName);

        activeIngredient = new EditText(this);
        activeIngredient.setHint("Princípio ativo");
        layout.addView(activeIngredient);

        formRoute = new EditText(this);
        formRoute.setHint("Forma de ingestão");
        layout.addView(formRoute);

        company = new EditText(this);
        company.setHint("Empresa fabricante");
        layout.addView(company);

        builder.setView(layout);

        Call<MedicinesContent> call = this.client.getMedicineById(id);
        call.enqueue(new Callback<MedicinesContent>() {
            @Override
            public void onResponse(Call<MedicinesContent> call, Response<MedicinesContent> response) {
                if (response.code() == 200) {
                    MedicinesContent medicinesContent = response.body();
                    drugName.setText(medicinesContent.getDrugName());
                    activeIngredient.setText(medicinesContent.getActiveIngredient());
                    formRoute.setText(medicinesContent.getFormRoute());
                    company.setText(medicinesContent.getCompany());
                } else {
                    builder.show().dismiss();
                }
            }

            @Override
            public void onFailure(Call<MedicinesContent> call, Throwable t) {
                builder.show().dismiss();
            }
        });

        builder.setPositiveButton("Editar", (dialog, which) -> {
            if (validateFields()) {
                Toast errorToast = Toast.makeText(getApplicationContext(), "Ocorreu um erro", Toast.LENGTH_LONG);
                errorToast.show();
                return;
            }

            RegisterMedicines registerMedicines = new RegisterMedicines(drugName.getText().toString(), activeIngredient.getText().toString(), formRoute.getText().toString(), company.getText().toString());
            Call<MedicinesContent> updateCall = this.client.updateMedicines(registerMedicines, id);
            updateCall.enqueue(new Callback<MedicinesContent>() {
                @Override
                public void onResponse(Call<MedicinesContent> call, Response<MedicinesContent> response) {
                    if (response.code() == 200) {
                        Toast successToast = Toast.makeText(getApplicationContext(), "Atualizado com sucesso", Toast.LENGTH_LONG);
                        successToast.show();

                        // TODO: Alerta de gambiarra, levando em consideração o prazo.
                        finish();
                        startActivity(getIntent());
                    } else {
                        Toast errorToast = Toast.makeText(getApplicationContext(), "Ocorreu um erro", Toast.LENGTH_LONG);
                        errorToast.show();
                    }
                }

                @Override
                public void onFailure(Call<MedicinesContent> call, Throwable t) {
                    Toast errorToast = Toast.makeText(getApplicationContext(), "Ocorreu um erro", Toast.LENGTH_LONG);
                    errorToast.show();
                }
            });


        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
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

    public void getMedications() {
        Call<MedicinesPaginatedView> call = this.client.getMedicines(0, 10);

        call.enqueue(new Callback<MedicinesPaginatedView>() {
            @Override
            public void onResponse(Call<MedicinesPaginatedView> call, Response<MedicinesPaginatedView> response) {
                MedicinesPaginatedView medicinesPaginatedView = response.body();

                int i = 0;
                for (MedicinesContent medicinesContent : medicinesPaginatedView.getMedicines().getContent()) {

                    TableLayout manageMedicationsTable = (TableLayout) findViewById(R.id.manageMedicationsTable);
                    TextView textView = findViewById(R.id.nameHeader);

                    TableRow row = new TableRow(ManageMedicationsActivity.this);
                    row.setId(medicinesContent.getId());
                    row.setBackgroundResource(i % 2 == 0 ? R.color.white : R.color.tableOdd);

                    row.setOnClickListener(v -> editMedications(v.getId()));

                    TextView name = new TextView(ManageMedicationsActivity.this);
                    name.setPadding(30, 30, 30, 30);
                    name.setLayoutParams(textView.getLayoutParams());
                    name.setSingleLine(true);
                    name.setEllipsize(TextUtils.TruncateAt.END);
                    name.setText(medicinesContent.getDrugName());

                    TextView principle = new TextView(ManageMedicationsActivity.this);
                    principle.setPadding(30, 30, 30, 30);
                    principle.setLayoutParams(textView.getLayoutParams());
                    principle.setSingleLine(true);
                    principle.setEllipsize(TextUtils.TruncateAt.END);
                    principle.setText(medicinesContent.getActiveIngredient());

                    TextView ingestion = new TextView(ManageMedicationsActivity.this);
                    ingestion.setPadding(30, 30, 30, 30);
                    ingestion.setLayoutParams(textView.getLayoutParams());
                    ingestion.setSingleLine(true);
                    ingestion.setEllipsize(TextUtils.TruncateAt.END);
                    ingestion.setText(medicinesContent.getFormRoute());

                    TextView manufacturer = new TextView(ManageMedicationsActivity.this);
                    manufacturer.setPadding(30, 30, 30, 30);
                    manufacturer.setLayoutParams(textView.getLayoutParams());
                    manufacturer.setSingleLine(true);
                    manufacturer.setEllipsize(TextUtils.TruncateAt.END);
                    manufacturer.setText(medicinesContent.getCompany());

                    row.addView(name);
                    row.addView(principle);
                    row.addView(ingestion);
                    row.addView(manufacturer);

                    manageMedicationsTable.addView(row);

                    i++;
                }

                i = 0;
            }

            @Override
            public void onFailure(Call<MedicinesPaginatedView> call, Throwable t) {
                // TODO: Implementar estratégia contra erros
                Toast errorToast = Toast.makeText(getApplicationContext(), "Ocorreu um erro", Toast.LENGTH_LONG);
                errorToast.show();
            }
        });
    }
}