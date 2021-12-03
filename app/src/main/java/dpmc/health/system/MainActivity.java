package dpmc.health.system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
    }

    public void consultPatients(View view) {
        Toast toast = Toast.makeText(getApplicationContext(), "Carregando...", Toast.LENGTH_SHORT);
        toast.show();

        Intent intent = new Intent(MainActivity.this, ConsultPatientsActivity.class);
        startActivity(intent);
    }

    public void manageMedications(View view) {
        Toast toast = Toast.makeText(getApplicationContext(), "Carregando...", Toast.LENGTH_SHORT);
        toast.show();

        Intent intent = new Intent(MainActivity.this, ManageMedicationsActivity.class);
        startActivity(intent);
    }

    public void registerMedications(View view) {
        Toast toast = Toast.makeText(getApplicationContext(), "Carregando...", Toast.LENGTH_SHORT);
        toast.show();

        Intent intent = new Intent(MainActivity.this, RegisterMedicationsActivity.class);
        startActivity(intent);
    }

    public void registerPatients(View view) {
        Toast toast = Toast.makeText(getApplicationContext(), "Carregando...", Toast.LENGTH_SHORT);
        toast.show();

        Intent intent = new Intent(MainActivity.this, RegisterPatientsActivity.class);
        startActivity(intent);
    }
}