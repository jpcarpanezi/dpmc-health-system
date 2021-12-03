package dpmc.health.system;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ManageMedicationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_medications);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_manage_medications);
    }
}