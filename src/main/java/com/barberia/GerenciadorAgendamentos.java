package com.barberia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GerenciadorAgendamentos {
    private static final String ARQUIVO_DADOS = "agendamentos.txt";
    private ArrayList<Agendamento> listaAgendamentos = new ArrayList<>();

    public GerenciadorAgendamentos() {
        carregarDadosDoArquivo();
    }

    public boolean adicionar(Agendamento agendamento) {
        if (agendamento == null) {
            return false;
        }
        listaAgendamentos.add(agendamento);
        salvarDadoNoArquivo(agendamento);
        return true;
    }

    public ArrayList<Agendamento> getLista() {
        return listaAgendamentos;
    }

    public boolean horarioDisponivel(String data, String horario) {
        for (Agendamento a : listaAgendamentos) {
            if (a.getData().equals(data) && a.getHorario().equals(horario)) {
                return false;
            }
        }
        return true;
    }

    private void salvarDadoNoArquivo(Agendamento agendamento) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_DADOS, true))) {
            String linha = agendamento.getCliente().getNome() + ";" +
                    agendamento.getCliente().getTelefone() + ";" +
                    agendamento.getData() + ";" +
                    agendamento.getHorario() + ";" +
                    agendamento.getServico();
            writer.write(linha);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao salvar o agendamento no arquivo local.");
        }
    }

    private void carregarDadosDoArquivo() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_DADOS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 5) {
                    Cliente cliente = new Cliente(partes[0], partes[1]);
                    Agendamento agendamento = new Agendamento(cliente, partes[2], partes[3], partes[4]);
                    listaAgendamentos.add(agendamento);
                }
            }
        } catch (IOException e) {
        }
    }
}