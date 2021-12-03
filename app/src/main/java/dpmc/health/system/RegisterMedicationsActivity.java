package dpmc.health.system;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RegisterMedicationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_medications);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_register_medications);
    }
}