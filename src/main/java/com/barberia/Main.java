package com.barberia;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        ArrayList<Agendamento> listaAgendamentos = new ArrayList<>();
        boolean continuar = true;

        System.out.println("=== SISTEMA DE BARBEARIA ===");

        while (continuar) {
            System.out.println("\n--- Novo Agendamento ---");

            Cliente cliente = cadastrarCliente(leitor);
            Agendamento agendamento = cadastrarAgendamento(leitor, cliente);

            listaAgendamentos.add(agendamento);

            System.out.print("\nDeseja realizar outro agendamento? (s/n): ");
            String resposta = leitor.nextLine();

            if (resposta.equalsIgnoreCase("n")) {
                continuar = false;
            }
        }

        exibirAgendamentos(listaAgendamentos);

        System.out.println("\nSistema encerrado. Obrigado!");
        leitor.close();
    }

    private static Cliente cadastrarCliente(Scanner leitor) {
        String nome;
        while (true) {
            System.out.print("Digite o nome do cliente: ");
            nome = leitor.nextLine().trim();
            if (!nome.isEmpty() && nome.matches("[a-zA-ZÀ-ÿ\\s]+")) {
                break;
            }
            System.out.println("Erro: O nome deve conter apenas letras! Tente novamente.");
        }

        System.out.print("Digite o telefone do cliente: ");
        String tel = leitor.nextLine();

        return new Cliente(nome, tel);
    }

    private static Agendamento cadastrarAgendamento(Scanner leitor, Cliente cliente) {
        String data;
        while (true) {
            System.out.print("Digite a data (ex: 12/06): ");
            data = leitor.nextLine().trim();


            if (data.matches("(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])")) {
                break;
            }
            System.out.println("Erro: Data inválida! Use o padrão DD/MM com dias (01-31) e meses (01-12).");
        }

        String horario;
        while (true) {
            System.out.print("Digite o horário (ex: 14:30): ");
            horario = leitor.nextLine().trim();

            if (horario.matches("([01][0-9]|2[0-3]):[0-5][0-9]")) {
                break;
            }
            System.out.println("Erro: Horário inválido! Use o padrão HH:MM com horas (00-23) e minutos (00-59).");
        }

        String servico;
        while (true) {
            System.out.print("Digite o serviço (Cabelo/Barba/Completo): ");
            servico = leitor.nextLine().trim();
            if (servico.equalsIgnoreCase("cabelo") ||
                    servico.equalsIgnoreCase("barba") ||
                    servico.equalsIgnoreCase("completo")) {
                servico = servico.substring(0, 1).toUpperCase() + servico.substring(1).toLowerCase();
                break;
            }
            System.out.println("Erro: Serviço inválido! Escolha apenas entre Cabelo, Barba ou Completo.");
        }

        return new Agendamento(cliente, data, horario, servico);
    }

    private static void exibirAgendamentos(ArrayList<Agendamento> lista) {
        System.out.println("\n=== TODOS OS AGENDAMENTOS ===");
        for (Agendamento a : lista) {
            System.out.println("Cliente: " + a.getCliente().getNome() +
                    " | Data: " + a.getData() +
                    " | Horário: " + a.getHorario() +
                    " | Serviço: " + a.getServico());
        }
    }
}