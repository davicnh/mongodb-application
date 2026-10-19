package org.example.appmongodb.service;

import lombok.RequiredArgsConstructor;
import org.example.appmongodb.repository.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;
}
