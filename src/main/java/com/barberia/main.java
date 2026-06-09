package com.barberia;

import java.util.Scanner;

public class main {
    public static void main(String[] args) {
    Scanner leitor = new Scanner(System.in);

        System.out.println("========== Sistema de cadastro ==========");

        System.out.println("Digite o nome do cliente: ");
        String nomeDigitado = leitor.nextLine();

        System.out.println("Digite o telefone do cliente: ");
        String telDigitado = leitor.nextLine();

        Cliente cliente1 = new Cliente(nomeDigitado, telDigitado);

        System.out.println("Cliente cadastrado: " + cliente1.getNome());
        System.out.println("\n=== CLIENTE CADASTRADO! ===");
        System.out.println("Nome: " + cliente1.getNome());
        System.out.println("Telefone: " + cliente1.getTel() );

        leitor.close();
    }
}
