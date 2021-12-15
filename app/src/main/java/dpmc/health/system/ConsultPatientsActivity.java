package dpmc.health.system;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;

import dpmc.health.system.interfaces.MedicationsClient;
import dpmc.health.system.interfaces.PersonClient;
import dpmc.health.system.models.MedicinesContent;
import dpmc.health.system.models.Person;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConsultPatientsActivity extends AppCompatActivity {
    private PersonClient client;

    private TextWatcher textWatcher = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_patients);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_consult_patients);

        this.retrofitBuild();

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                EditText text = (EditText) findViewById(R.id.personSearchParameter);
                text.setText("");

                String selected = spinner.getSelectedItem().toString();

                switch (selected) {
                    case "CPF":
                        text.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        text.setHint("CPF do usuário");
                        
                        textWatcher = MaskUtils.insert(text, MaskType.CPF);
                        text.addTextChangedListener(textWatcher);
                        break;
                    case "E-mail":
                        text.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        text.setHint("E-mail do usuário");

                        if (textWatcher != null) {
                            text.removeTextChangedListener(textWatcher);
                            textWatcher = null;
                        }
                        break;
                    case "ID":
                        text.setInputType(InputType.TYPE_CLASS_NUMBER);
                        text.setHint("ID do usuário");

                        if (textWatcher != null) {
                            text.removeTextChangedListener(textWatcher);
                            textWatcher = null;
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    public void searchButtonClick(View view) {
        EditText text = (EditText) findViewById(R.id.personSearchParameter);
        String searchParam = text.getText().toString();

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String searchMode = spinner.getSelectedItem().toString();
        if ("E-mail".equals(searchMode)) {
            searchMode = "email";
        } else {
            searchMode = searchMode.toLowerCase();
        }

        Call<Person> call = client.searchPerson(searchMode, searchParam);
        call.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                if (response.code() == 200) {
                    Person person = response.body();
                    Toast.makeText(getApplicationContext(), "Encontrado: " + person.getPersonName(), Toast.LENGTH_SHORT).show();

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                } else {
                    Toast.makeText(getApplicationContext(), "A pessoa não foi encontrada", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Ocorreu um erro", Toast.LENGTH_SHORT).show();
            }
        });
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

        this.client =  retrofit.create(PersonClient.class);
    }
}
