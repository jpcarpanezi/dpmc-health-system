package dpmc.health.system;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dpmc.health.system.interfaces.MedicationsClient;
import dpmc.health.system.interfaces.PersonClient;
import dpmc.health.system.models.RegisterPerson;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterPatientsActivity extends AppCompatActivity {
    private PersonClient client;

    private DatePickerDialog datePickerDialog;
    private ImageButton dateButton;

    private TextView userName;
    private TextView userEmail;
    private TextView userCpf;
    private TextView userPhone;
    private TextView birthDate;
    private TextView birthCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_patients);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_register_patients);
        this.addMasks();
        this.initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setTag(getTodayDate());

        this.userName = findViewById(R.id.userName);
        this.userEmail = findViewById(R.id.userEmail);
        this.userCpf = findViewById(R.id.userCpf);
        this.userPhone = findViewById(R.id.userPhone);
        this.birthDate = findViewById(R.id.birthDate);
        this.birthCity = findViewById(R.id.birthCity);

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

        this.client = retrofit.create(PersonClient.class);
    }

    private void addMasks() {
        EditText cpf = findViewById(R.id.userCpf);
        EditText phone = findViewById(R.id.userPhone);
        EditText date = findViewById(R.id.birthDate);

        cpf.addTextChangedListener(MaskUtils.insert(cpf, MaskType.CPF));
        phone.addTextChangedListener(MaskUtils.insert(phone, MaskType.Phone));
        date.addTextChangedListener(MaskUtils.insert(date, MaskType.Date));
    }

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        month += 1;
        int year = cal.get(Calendar.YEAR);
        return makeDateString(day, month, year);
    }

    private boolean validateFields() {
        boolean hasErrors = false;

        if (userName.length() == 0) {
            userName.setError("Este campo é obrigatório");
            hasErrors = true;
        }

        if (userEmail.length() == 0) {
            userEmail.setError("Este campo é obrigatório");
            hasErrors = true;
        }

        if (userCpf.length() == 0) {
            userCpf.setError("Este campo é obrigatório");
            hasErrors = true;
        }

        if (userPhone.length() == 0) {
            userPhone.setError("Este campo é obrigatório");
            hasErrors = true;
        }

        if (birthDate.length() == 0) {
            birthDate.setError("Este campo é obrigatório");
            hasErrors = true;
        }

        if (birthCity.length() == 0) {
            birthCity.setError("Este campo é obrigatório");
            hasErrors = true;
        }

        return hasErrors;
    }

    public void registerPatient(View view) {
        if (validateFields()) {
            return;
        }

        // WARNING: Gambiarra ahead
        String[] splitBirthDate = birthDate.getText().toString().split("/");
        String formattedBirthDate = splitBirthDate[2] + "-" + splitBirthDate[1] + "-" + splitBirthDate[0];

        RegisterPerson registerPerson = new RegisterPerson(userName.getText().toString(), userCpf.getText().toString(), MaskUtils.unmask(userPhone.getText().toString()), formattedBirthDate, userEmail.getText().toString(), birthCity.getText().toString());
        Call<RegisterPerson> call = this.client.insertPerson(registerPerson);
        call.enqueue(new Callback<RegisterPerson>() {
            @Override
            public void onResponse(Call<RegisterPerson> call, Response<RegisterPerson> response) {
                if (response.code() == 201) {
                    userName.setText(null);
                    userCpf.setText(null);
                    userPhone.setText(null);
                    birthDate.setText(null);
                    userEmail.setText(null);
                    birthCity.setText(null);

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    Toast successToast = Toast.makeText(getApplicationContext(), "Cadastrado com sucesso", Toast.LENGTH_LONG);
                    successToast.show();
                } else {
                    Toast failureToast = Toast.makeText(getApplicationContext(), "Ocorreu um erro", Toast.LENGTH_LONG);
                    failureToast.show();
                }
            }

            @Override
            public void onFailure(Call<RegisterPerson> call, Throwable t) {
                Toast failureToast = Toast.makeText(getApplicationContext(), "Ocorreu um erro", Toast.LENGTH_LONG);
                failureToast.show();
            }
        });
    }

    public void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            dateButton.setTag(date);

            EditText birthDate = findViewById(R.id.birthDate);
            birthDate.setText(null);
            DecimalFormat decimalFormat = new DecimalFormat("00");
            birthDate.setText(decimalFormat.format(day) + "/" + decimalFormat.format(month) + "/" + year);
        };

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private String makeDateString(int day, int month, int year) {
        return day + "/" + getMonthFormat(month) + "/" + year;
    }

    private String getMonthFormat(int month) {
        switch (month) {
            case 1:
                return "JAN";
            case 2:
                return "FEV";
            case 3:
                return "MAR";
            case 4:
                return "ABR";
            case 5:
                return "MAI";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AGO";
            case 9:
                return "SET";
            case 10:
                return "OUT";
            case 11:
                return "NOV";
            case 12:
                return "DEZ";
            default:
                return "JAN";
        }
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}