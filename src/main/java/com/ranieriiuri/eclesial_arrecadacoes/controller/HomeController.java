package com.ranieriiuri.eclesial_arrecadacoes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Bem vindo à home page do projeto...\n API Eclesial Arrecadações está rodando!⛪ ");
    }
}
