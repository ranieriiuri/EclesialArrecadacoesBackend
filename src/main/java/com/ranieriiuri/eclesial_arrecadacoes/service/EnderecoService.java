package com.ranieriiuri.eclesial_arrecadacoes.service;

import com.ranieriiuri.eclesial_arrecadacoes.domain.model.Endereco;
import com.ranieriiuri.eclesial_arrecadacoes.domain.repository.EnderecoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    public Endereco atualizarEndereco(UUID id, Endereco novoEndereco) {
        Endereco existente = enderecoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado"));

        existente.setCep(novoEndereco.getCep());
        existente.setLogradouro(novoEndereco.getLogradouro());
        existente.setNumero(novoEndereco.getNumero());
        existente.setComplemento(novoEndereco.getComplemento());
        existente.setBairro(novoEndereco.getBairro());
        existente.setCidade(novoEndereco.getCidade());
        existente.setEstado(novoEndereco.getEstado());
        existente.setPais(novoEndereco.getPais());

        return enderecoRepository.save(existente);
    }
}
