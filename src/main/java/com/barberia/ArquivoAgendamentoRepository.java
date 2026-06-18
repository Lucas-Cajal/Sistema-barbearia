package com.barberia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArquivoAgendamentoRepository implements AgendamentoRepository {
    private static final String ARQUIVO_DADOS = "agendamentos.txt";
    private static final String ARQUIVO_BLOQUEIOS = "datas_bloqueadas.txt";

    private ArrayList<Agendamento> listaAgendamentos = new ArrayList<>();
    private Map<String, String> datasBloqueadas = new HashMap<>();

    public ArquivoAgendamentoRepository() {
        carregarDadosDoArquivo();
        carregarBloqueiosDoArquivo();
    }

    @Override
    public boolean adicionar(Agendamento agendamento) {
        if (agendamento == null) return false;
        listaAgendamentos.add(agendamento);
        salvarDadoNoArquivo(agendamento);
        return true;
    }

    @Override
    public boolean cancelar(String nomeCliente, String data, String horario) {
        for (Agendamento a : listaAgendamentos) {
            if (a.getCliente().getNome().equalsIgnoreCase(nomeCliente) &&
                    a.getData().equals(data) &&
                    a.getHorario().equals(horario)) {

                listaAgendamentos.remove(a);
                atualizarArquivoCompleto();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean horarioDisponivel(String data, String horario) {
        for (Agendamento a : listaAgendamentos) {
            if (a.getData().equals(data) && a.getHorario().equals(horario)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ArrayList<Agendamento> getLista() {
        return listaAgendamentos;
    }

    @Override
    public void travarData(String data, String recado) {
        datasBloqueadas.put(data, recado);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_BLOQUEIOS, true))) {
            writer.write(data + ";" + recado);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao salvar o bloqueio de data.");
        }
    }

    @Override
    public String obterRecadoDaDataBloqueada(String data) {
        return datasBloqueadas.get(data);
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
            System.out.println("Erro ao salvar o agendamento.");
        }
    }

    private void atualizarArquivoCompleto() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_DADOS, false))) {
            for (Agendamento a : listaAgendamentos) {
                String linha = a.getCliente().getNome() + ";" +
                        a.getCliente().getTelefone() + ";" +
                        a.getData() + ";" +
                        a.getHorario() + ";" +
                        a.getServico();
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao atualizar o arquivo de dados.");
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

    private void carregarBloqueiosDoArquivo() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_BLOQUEIOS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 2) {
                    datasBloqueadas.put(partes[0], partes[1]);
                }
            }
        } catch (IOException e) {
        }
    }
}