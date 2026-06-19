package com.barberia;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        AgendamentoRepository gerenciador = new ArquivoAgendamentoRepository();
        boolean executarSistema = true;

        while (executarSistema) {
            System.out.println("\n========== SISTEMA DE BARBEARIA ==========");
            System.out.println("1. Cadastrar Novo Agendamento");
            System.out.println("2. Listar Todos os Agendamentos");
            System.out.println("3. Cancelar um Agendamento");
            System.out.println("4. Bloquear Data (Modo Barbeiro)");
            System.out.println("5. Sair do Sistema");
            System.out.print("Escolha uma opção: ");

            String opcao = leitor.nextLine().trim();

            try {
                switch (opcao) {
                    case "1":
                        System.out.println("\n--- Novo Agendamento ---");
                        Cliente cliente = cadastrarCliente(leitor);
                        Agendamento agendamento = cadastrarAgendamento(leitor, cliente);
                        gerenciador.adicionar(agendamento);
                        System.out.println("\nAgendamento salvo com sucesso e gravado em disco!");
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

                        gerenciador.cancelar(nomeCancelamento, dataCancelamento, horarioCancelamento);
                        System.out.println("\nAgendamento cancelado e removido do disco com sucesso!");
                        break;
                    case "4":
                        System.out.println("\n--- Bloquear Data de Expediente ---");
                        String dataTrava = validarFormatacaoData(leitor);
                        System.out.print("Digite o recado/motivo do bloqueio: ");
                        String recado = leitor.nextLine().trim();
                        gerenciador.travarData(dataTrava, recado);
                        System.out.println("\nAviso registrado! A data " + dataTrava + " está oficialmente bloqueada.");
                        break;
                    case "5":
                        System.out.println("\nSistema encerrado. Obrigado e bom trabalho!");
                        executarSistema = false;
                        break;
                    default:
                        System.out.println("Erro: Opção inválida! Escolha um número de 1 a 5.");
                        break;
                }
            } catch (NegocioException e) {
                System.out.println("\n❌ Erro de Validação: " + e.getMessage());
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

    private static String validarFormatacaoData(Scanner leitor) {
        DateTimeFormatter formatadorInput = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatadorExibicao = DateTimeFormatter.ofPattern("dd/MM");
        while (true) {
            System.out.print("Digite a data (ex: 12/06): ");
            String dataInput = leitor.nextLine().trim();
            String dataCompleta = dataInput + "/" + LocalDate.now().getYear();
            try {
                LocalDate dataValida = LocalDate.parse(dataCompleta, formatadorInput);
                if (dataValida.isBefore(LocalDate.now())) {
                    System.out.println("Erro: Não é possível usar uma data que já passou!");
                    continue;
                }
                return dataValida.format(formatadorExibicao);
            } catch (DateTimeParseException e) {
                System.out.println("Erro: Data inválida ou inexistente! Use o padrão DD/MM.");
            }
        }
    }

    private static Agendamento cadastrarAgendamento(Scanner leitor, Cliente cliente) {
        String dataFormatada = validarFormatacaoData(leitor);

        String horario;
        while (true) {
            System.out.print("Digite o horário (das 08:00 às 20:00, a cada 30 min - ex: 14:30): ");
            horario = leitor.nextLine().trim();

            if (!horario.matches("([01][0-9]|2[0-3]):[0-5][0-9]")) {
                System.out.println("Erro: Horário inválido! Use o padrão HH:MM.");
                continue;
            }

            String[] partesHora = horario.split(":");
            int hora = Integer.parseInt(partesHora[0]);
            int minutos = Integer.parseInt(partesHora[1]);

            if (hora < 8 || hora > 20 || (hora == 20 && minutos > 0)) {
                System.out.println("Erro: Fora do horário de expediente! Funcionamos das 08:00 às 20:00.");
                continue;
            }

            if (minutos != 0 && minutos != 30) {
                System.out.println("Erro: Os agendamentos devem ser feitos de 30 em 30 minutos (ex: :00 ou :30).");
                continue;
            }

            break;
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

    private static void exibirAgendamentos(AgendamentoRepository gerenciador) {
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