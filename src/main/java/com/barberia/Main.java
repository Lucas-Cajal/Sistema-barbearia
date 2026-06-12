package com.barberia;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        ArrayList<Agendamento> listaAgendamentos = new ArrayList<>();
        boolean executarSistema = true;

        while (executarSistema) {
            System.out.println("\n========== SISTEMA DE BARBEARIA ==========");
            System.out.println("1. Cadastrar Novo Agendamento");
            System.out.println("2. Listar Todos os Agendamentos");
            System.out.println("3. Sair do Sistema");
            System.out.print("Escolha uma opção: ");

            String opcao = leitor.nextLine().trim();

            switch (opcao) {
                case "1":
                    System.out.println("\n--- Novo Agendamento ---");
                    Cliente cliente = cadastrarCliente(leitor);
                    Agendamento agendamento = cadastrarAgendamento(leitor, cliente);
                    listaAgendamentos.add(agendamento);
                    System.out.println("\nAgendamento salvo com sucesso!");
                    break;
                case "2":
                    exibirAgendamentos(listaAgendamentos);
                    break;
                case "3":
                    System.out.println("\nSistema encerrado. Obrigado e bom trabalho!");
                    executarSistema = false;
                    break;
                default:
                    System.out.println("Erro: Opção inválida! Escolha um número de 1 a 3.");
                    break;
            }
        }

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

        String tel;
        while (true) {
            System.out.print("Digite o telefone (11 dígitos, apenas números): ");
            tel = leitor.nextLine().trim();

            // \d{11} garante que sejam exatamente 11 caracteres numéricos (0-9)
            if (tel.matches("\\d{11}")) {
                // Formata o número de 11912345678 para (11) 91234-5678 antes de salvar
                tel = "(" + tel.substring(0, 2) + ") " + tel.substring(2, 7) + "-" + tel.substring(7);
                break;
            }
            System.out.println("Erro: Telefone inválido! Digite exatamente 11 números com o DDD (ex: 11999998888).");
        }

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
        if (lista.isEmpty()) {
            System.out.println("\nNenhum agendamento encontrado na lista.");
            return;
        }

        System.out.println("\n=== TODOS OS AGENDAMENTOS ===");
        for (Agendamento a : lista) {
            System.out.println("Cliente: " + a.getCliente().getNome() +
                    " | Data: " + a.getData() +
                    " | Horário: " + a.getHorario() +
                    " | Serviço: " + a.getServico());
        }
    }
}