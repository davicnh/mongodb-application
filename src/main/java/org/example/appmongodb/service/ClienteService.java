package org.example.appmongodb.service;

import lombok.RequiredArgsConstructor;
import org.example.appmongodb.entity.Cliente;
import org.example.appmongodb.entity.enums.TipoCliente;
import org.example.appmongodb.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    public void delete(String id){
        repository.deleteById(id);
    }

    public Cliente findById(String id){
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado"));
        return cliente;
    }

    public List<Cliente> findAll(){
        return repository.findAll();
    }

    public Cliente save(Cliente cliente){
        cliente.setId(null);
        cliente.setCriadoEm(LocalDateTime.now());
        cliente.setEmail(cliente.getEmail().trim().toLowerCase());

        if (repository.existsByEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        if (cliente.getTipo() == TipoCliente.B2C) {
            if (cliente.getCpf() == null || cliente.getCpf().isBlank()) {
                throw new IllegalArgumentException("CPF é obrigatório para pessoa física");
            }
            cliente.setCnpj(null);
        } else {
            if (cliente.getCnpj() == null || cliente.getCnpj().isBlank()) {
                throw new IllegalArgumentException("CNPJ é obrigatório para pessoa jurídica");
            }
            cliente.setCpf(null);
        }

        return repository.save(cliente);
    }

    public Cliente atualizar(String id, Cliente dados) {
        Cliente existente = findById(id);

        dados.setId(id);
        dados.setCriadoEm(existente.getCriadoEm());
        dados.setEmail(dados.getEmail().trim().toLowerCase());

        if (!existente.getEmail().equals(dados.getEmail())
                && repository.existsByEmail(dados.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        if (dados.getTipo() == TipoCliente.B2C) {
            if (dados.getCpf() == null || dados.getCpf().isBlank()) {
                throw new IllegalArgumentException("CPF é obrigatório para pessoa física");
            }
            dados.setCnpj(null);
        } else {
            if (dados.getCnpj() == null || dados.getCnpj().isBlank()) {
                throw new IllegalArgumentException("CNPJ é obrigatório para pessoa jurídica");
            }
            dados.setCpf(null);
        }

        return repository.save(dados);
    }
}
