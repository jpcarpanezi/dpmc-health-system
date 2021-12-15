package dpmc.health.system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Optional;

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

    private TextView name;
    private TextView birthDate;
    private TextView email;
    private TextView phone;
    private TextView bloodType;
    private TextView medicalConditions;
    private TextView allergies;
    private TextView comments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_patients);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_consult_patients);

        this.name = findViewById(R.id.name);
        this.birthDate = findViewById(R.id.birthDate);
        this.email = findViewById(R.id.email);
        this.phone = findViewById(R.id.phone);
        this.bloodType = findViewById(R.id.bloodType);
        this.medicalConditions = findViewById(R.id.medicalConditions);
        this.allergies = findViewById(R.id.allergies);
        this.comments = findViewById(R.id.comments);

        this.name.setText(getString(R.string.person_name, ""));
        this.birthDate.setText(getString(R.string.person_birth_date, ""));
        this.email.setText(getString(R.string.person_email, ""));
        this.phone.setText(getString(R.string.person_phone, ""));
        this.bloodType.setText(getString(R.string.person_blood_type, ""));
        this.medicalConditions.setText(getString(R.string.person_medical_conditions, ""));
        this.allergies.setText(getString(R.string.person_allergies, ""));
        this.comments.setText(getString(R.string.person_comments, ""));

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);

                return true;
        }
        return super.onOptionsItemSelected(item);
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

                    name.setText(getString(R.string.person_name, person.getPersonName()));
                    birthDate.setText(getString(R.string.person_birth_date, person.getBirthDate()));
                    email.setText(getString(R.string.person_email, person.getEmail()));
                    phone.setText(getString(R.string.person_phone, person.getPhone()));
                    bloodType.setText(getString(R.string.person_blood_type, Optional.ofNullable(person.getMedicalInformations().getBloodType()).orElse("Sem dados")));
                    medicalConditions.setText(getString(R.string.person_medical_conditions, Optional.ofNullable(person.getMedicalInformations().getMedicalConditions()).orElse("Sem dados")));
                    allergies.setText(getString(R.string.person_allergies, Optional.ofNullable(person.getMedicalInformations().getAllergies()).orElse("Sem dados")));
                    comments.setText(getString(R.string.person_comments, Optional.ofNullable(person.getMedicalInformations().getObservations()).orElse("Sem dados")));
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
