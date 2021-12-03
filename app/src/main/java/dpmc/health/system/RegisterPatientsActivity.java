package dpmc.health.system;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RegisterPatientsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_patients);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_register_patients);
    }
}