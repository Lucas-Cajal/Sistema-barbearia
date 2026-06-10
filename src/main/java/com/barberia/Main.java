package com.barberia;

import javax.xml.crypto.Data;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
    Scanner leitor = new Scanner(System.in);

        System.out.println("========== Sistema de cadastro ==========");

        System.out.println("Digite o nome do cliente: ");
        String nomeDigitado = leitor.nextLine();

        System.out.println("Digite o telefone do cliente: ");
        String telDigitado = leitor.nextLine();

        Cliente cliente = new Cliente(nomeDigitado, telDigitado);

        System.out.println("=== Dados do agendamento ===");

        System.out.println("Digite a data: ");
        String dataDigitada = leitor.nextLine();

        System.out.println("Digite o horario: ");
        String horaDigitada = leitor.nextLine();

        System.out.println("Digite o servico (cabelo, barba, copleto): ");
        String servicoDigitado = leitor.nextLine();

        Agendamento agendamento = new Agendamento(cliente,dataDigitada, horaDigitada, servicoDigitado);

        System.out.println("\n=== Resumo do Agendamento ===");
        System.out.println("Cliente: " + agendamento.getCliente().getNome());
        System.out.println("Telefone: " + agendamento.getCliente().getTel());
        System.out.println("Data/Hora: " + agendamento.getData() + " às " + agendamento.getHorario());
        System.out.println("Serviço: " + agendamento.getServico());

        leitor.close();
    }
}
