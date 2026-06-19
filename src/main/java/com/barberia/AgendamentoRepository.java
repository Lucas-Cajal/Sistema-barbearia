package com.barberia;

import java.util.ArrayList;

public interface AgendamentoRepository {
    void adicionar(Agendamento agendamento);
    void cancelar(String nomeCliente, String data, String horario);
    boolean horarioDisponivel(String data, String horario);
    ArrayList<Agendamento> getLista();
    void travarData(String data, String recado);
    String obtenerRecadoDaDataBloqueada(String data);
}