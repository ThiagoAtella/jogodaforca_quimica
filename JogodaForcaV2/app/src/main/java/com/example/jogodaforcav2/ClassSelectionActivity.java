package com.example.jogodaforcav2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jogodaforcav2.databinding.ActivityTeamSelectionBinding;

public class ClassSelectionActivity extends AppCompatActivity {
    private ActivityTeamSelectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeamSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonAmetais.setOnClickListener(v -> iniciarJogo("AMETAIS"));

        binding.buttonGases.setOnClickListener(v -> iniciarJogo("GASES"));

        binding.buttonTransicao.setOnClickListener(v -> iniciarJogo("TRANSICAO"));

        binding.buttonRepresenta.setOnClickListener(v -> iniciarJogo("REPRESENTA"));

        binding.buttonBack.setOnClickListener(v -> finish()); // Volta para o menu
    }

    private void iniciarJogo(String time) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("CLASSE_ESCOLHIDA", time); // Passa a classe selecionada para a MainActivity
        startActivity(intent);
    }
}
