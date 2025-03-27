package com.example.jogodaforcav2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ForcaViewModel extends ViewModel {
    private static final List<String> PALAVRAS_AMETAIS = Arrays.asList("HIDROGENIO", "CARBONO", "NITROGENIO", "OXIGENIO", "FLUOR",
            "FOSFORO", "ENXOFRE", "BROMO");
    private static final List<String> PALAVRAS_GASES = Arrays.asList("HELIO", "NEONIO", "ARGONIO", "CRIPTONIO", "XENONIO",
            "RADONIO");
    private static final List<String> PALAVRAS_TRANSICAO = Arrays.asList("TITANIO", "CROMO", "COBALTO", "PLATINA", "OURO",
            "NIQUEL", "PRATA", "COBRE", "FERRO");
    private static final List<String> PALAVRAS_REPRESENTA = Arrays.asList("LITIO", "SODIO", "MAGNESIO", "CALCIO", "ALUMINIO",
            "POLONIO", "CESIO", "POTASSIO", "BERILIO");

    private final List<String> palavras;
    private String palavra;
    private char[] palavraOcultaArray;
    private int tentativasRestantes;
    private final Set<Character> letrasTentadas = new HashSet<>();

    private final MutableLiveData<String> _palavraOculta = new MutableLiveData<>();
    private final MutableLiveData<Integer> _tentativas = new MutableLiveData<>();
    private final MutableLiveData<GameStatus> _status = new MutableLiveData<>();

    public ForcaViewModel(String time) {
        switch (time) {
            case "GASES":
                this.palavras = PALAVRAS_GASES;
                break;
            case "TRANSICAO":
                this.palavras = PALAVRAS_TRANSICAO;
                break;
            case "REPRESENTA":
                this.palavras = PALAVRAS_REPRESENTA;
                break;
            default:
                this.palavras = PALAVRAS_AMETAIS; // ametais como padrão
        }
        iniciarNovoJogo();
    }

    public LiveData<String> getPalavraOculta() {
        return _palavraOculta;
    }

    public LiveData<Integer> getTentativas() {
        return _tentativas;
    }

    public LiveData<GameStatus> getStatus() {
        return _status;
    }

    public void iniciarNovoJogo() {
        palavra = palavras.get(new Random().nextInt(palavras.size()));
        palavraOcultaArray = new char[palavra.length()];
        Arrays.fill(palavraOcultaArray, '_');
        tentativasRestantes = 6;
        letrasTentadas.clear();
        _status.setValue(new GameStatus("Jogo iniciado!", null));
        atualizarUI();
    }

    public void processarPalpite(char letra) {
        if (!letrasTentadas.add(letra)) {
            return;
        }

        boolean acertou = false;
        for (int i = 0; i < palavra.length(); i++) {
            if (palavra.charAt(i) == letra) {
                palavraOcultaArray[i] = letra;
                acertou = true;
            }
        }

        if (!acertou) {
            tentativasRestantes--;
        }

        atualizarUI();
        verificarFimDeJogo(acertou);
    }

    private void atualizarUI() {
        _palavraOculta.setValue(new String(palavraOcultaArray));
        _tentativas.setValue(tentativasRestantes);
    }

    private void verificarFimDeJogo(boolean acertou) {
        if (tentativasRestantes <= 0) {
            _status.setValue(new GameStatus("Você perdeu! A palavra era: " + palavra, false));
            _palavraOculta.setValue(palavra);
        } else if (new String(palavraOcultaArray).indexOf('_') == -1) {
            _status.setValue(new GameStatus("Parabéns! Você ganhou!", true));
        } else {
            _status.setValue(new GameStatus("Continue jogando...", acertou));
        }
    }

    public static class ForcaViewModelFactory implements ViewModelProvider.Factory {
        private final String time;

        public ForcaViewModelFactory(String time) {
            this.time = time;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ForcaViewModel.class)) {
                return (T) new ForcaViewModel(time);
            }
            throw new IllegalArgumentException("Classe ViewModel desconhecida");
        }
    }
}
