package com.barberia;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        GerenciadorAgendamentos gerenciador = new GerenciadorAgendamentos();
        boolean executarSistema = true;

        while (executarSistema) {
            System.out.println("\n========== SISTEMA DE BARBEARIA ==========");
            System.out.println("1. Cadastrar Novo Agendamento");
            System.out.println("2. Listar Todos os Agendamentos");
            System.out.println("3. Cancelar um Agendamento");
            System.out.println("4. Sair do Sistema");
            System.out.print("Escolha uma opção: ");

            String opcao = leitor.nextLine().trim();

            switch (opcao) {
                case "1":
                    System.out.println("\n--- Novo Agendamento ---");
                    Cliente cliente = cadastrarCliente(leitor);
                    Agendamento agendamento = cadastrarAgendamento(leitor, cliente, gerenciador);

                    if (gerenciador.adicionar(agendamento)) {
                        System.out.println("\nAgendamento salvo com sucesso e gravado em disco!");
                    }
                    break;
                case "2":
                    exibirAgendamentos(gerenciador);
                    break;
                case "3":
                    System.out.println("\n--- Cancelar Agendamento ---");
                    System.out.print("Digite o nome exato do cliente: ");
                    String nomeCancelamento = leitor.nextLine().trim();

                    System.out.print("Digite a data (ex: 12/06): ");
                    String dataCancelamento = leitor.nextLine().trim();

                    System.out.print("Digite o horário (ex: 14:30): ");
                    String horarioCancelamento = leitor.nextLine().trim();

                    if (gerenciador.cancelar(nomeCancelamento, dataCancelamento, horarioCancelamento)) {
                        System.out.println("\nAgendamento cancelado e removido do disco com sucesso!");
                    } else {
                        System.out.println("\nErro: Nenhum agendamento encontrado com esses dados.");
                    }
                    break;
                case "4":
                    System.out.println("\nSistema encerrado. Obrigado e bom trabalho!");
                    executarSistema = false;
                    break;
                default:
                    System.out.println("Erro: Opção inválida! Escolha um número de 1 a 4.");
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

            if (tel.matches("\\d{11}")) {
                tel = "(" + tel.substring(0, 2) + ") " + tel.substring(2, 7) + "-" + tel.substring(7);
                break;
            }
            System.out.println("Erro: Telefone inválido! Digite exatamente 11 números com o DDD (ex: 11999998888).");
        }

        return new Cliente(nome, tel);
    }

    private static Agendamento cadastrarAgendamento(Scanner leitor, Cliente cliente, GerenciadorAgendamentos gerenciador) {
        String dataInput;
        String dataFormatada = "";
        DateTimeFormatter formatadorInput = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatadorExibicao = DateTimeFormatter.ofPattern("dd/MM");

        while (true) {
            System.out.print("Digite a data (ex: 12/06): ");
            dataInput = leitor.nextLine().trim();

            String dataCompleta = dataInput + "/" + LocalDate.now().getYear();

            try {
                LocalDate dataValida = LocalDate.parse(dataCompleta, formatadorInput);

                if (dataValida.isBefore(LocalDate.now())) {
                    System.out.println("Erro: Não é possível agendar uma data que já passou!");
                    continue;
                }

                dataFormatada = dataValida.format(formatadorExibicao);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Erro: Data inválida ou inexistente no calendário! Use o padrão DD/MM.");
            }
        }

        String horario;
        while (true) {
            System.out.print("Digite o horário (ex: 14:30): ");
            horario = leitor.nextLine().trim();
            if (horario.matches("([01][0-9]|2[0-3]):[0-5][0-9]")) {
                break;
            }
            System.out.println("Erro: Horário inválido! Use o padrão HH:MM.");
        }

        if (!gerenciador.horarioDisponivel(dataFormatada, horario)) {
            System.out.println("\n❌ Erro: Esse horário já está ocupado por outro cliente nesta data!");
            System.out.println("Agendamento cancelado. Tente iniciar o processo novamente escolhendo outro horário.");
            return null;
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

        return new Agendamento(cliente, dataFormatada, horario, servico);
    }

    private static void exibirAgendamentos(GerenciadorAgendamentos gerenciador) {
        if (gerenciador.getLista().isEmpty()) {
            System.out.println("\nNenhum agendamento encontrado na lista.");
            return;
        }

        System.out.println("\n=== TODOS OS AGENDAMENTOS ===");
        for (Agendamento a : gerenciador.getLista()) {
            System.out.println("Cliente: " + a.getCliente().getNome() +
                    " | Telefone: " + a.getCliente().getTelefone() +
                    " | Data: " + a.getData() +
                    " | Horário: " + a.getHorario() +
                    " | Serviço: " + a.getServico());
        }
    }
}