package com.ranieriiuri.eclesial_arrecadacoes;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class EclesialArrecadacoesApplication {

	private static final Logger logger = Logger.getLogger(EclesialArrecadacoesApplication.class.getName());

	public static void main(String[] args) {
		// 🔹 Carrega as variáveis do arquivo .env (se houver)
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		// 🔹 Seta como variável do sistema (Spring lê do System.getenv())
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		// 🔹 Log opcional (útil para debug)
		logger.info("Iniciando Eclesial Arrecadações...");

		SpringApplication.run(EclesialArrecadacoesApplication.class, args);
	}
}
