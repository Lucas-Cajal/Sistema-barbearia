package com.barberia;

import java.util.ArrayList;

public interface AgendamentoRepository {
    boolean adicionar(Agendamento agendamento);
    boolean cancelar(String nomeCliente, String data, String horario);
    boolean horarioDisponivel(String data, String horario);
    ArrayList<Agendamento> getLista();
}