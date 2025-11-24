package com.ProyectoTingeso1.BackendProyecto1.RepositoryTests;

import com.ProyectoTingeso1.BackendProyecto1.DTOs.ClientDTO;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Client;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClientRepository clientRepository;

    private Client client1;
    private Client client2;

    @BeforeEach
    void setUp() {
        client1 = new Client();
        client1.setName("Juan Pérez");
        client1.setRut("12345678-9");
        client1.setPhoneNumber("+56912345678");
        client1.setEmail("juan@example.com");
        client1.setStatus("ACTIVE");

        client2 = new Client();
        client2.setName("María González");
        client2.setRut("98765432-1");
        client2.setPhoneNumber("+56987654321");
        client2.setEmail("maria@example.com");
        client2.setStatus("RESTRICTED");

        entityManager.persist(client1);
        entityManager.persist(client2);
        entityManager.flush();
    }

    @Test
    void whenFindByRut_thenReturnClient() {
        // When
        Client found = clientRepository.findByRut("12345678-9");

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Juan Pérez");
        assertThat(found.getRut()).isEqualTo("12345678-9");
    }

    @Test
    void whenFindByRutNotExists_thenReturnNull() {
        // When
        Client found = clientRepository.findByRut("11111111-1");

        // Then
        assertThat(found).isNull();
    }

    @Test
    void whenFindAllRuts_thenReturnListOfRuts() {
        // When
        List<String> ruts = clientRepository.findAllRuts();

        // Then
        assertThat(ruts).isNotNull();
        assertThat(ruts).hasSize(2);
        assertThat(ruts).contains("12345678-9", "98765432-1");
    }

    @Test
    void whenFindClientDTOByRut_thenReturnClientDTO() {
        // When
        ClientDTO clientDTO = clientRepository.findClientDTOByRut("12345678-9");

        // Then
        assertThat(clientDTO).isNotNull();
        assertThat(clientDTO.getName()).isEqualTo("Juan Pérez");
        assertThat(clientDTO.getRut()).isEqualTo("12345678-9");
        assertThat(clientDTO.getEmail()).isEqualTo("juan@example.com");
        assertThat(clientDTO.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    void whenFindClientDTOByRutNotExists_thenReturnNull() {
        // When
        ClientDTO clientDTO = clientRepository.findClientDTOByRut("11111111-1");

        // Then
        assertThat(clientDTO).isNull();
    }

    @Test
    void whenSaveClient_thenClientIsPersisted() {
        // Given
        Client newClient = new Client();
        newClient.setName("Pedro Soto");
        newClient.setRut("11223344-5");
        newClient.setPhoneNumber("+56911223344");
        newClient.setEmail("pedro@example.com");
        newClient.setStatus("ACTIVE");

        // When
        Client saved = clientRepository.save(newClient);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Pedro Soto");
    }

    @Test
    void whenDeleteClient_thenClientIsRemoved() {
        // Given
        Client found = clientRepository.findByRut("12345678-9");
        Long clientId = found.getId();

        // When
        clientRepository.deleteById(clientId);
        entityManager.flush();

        // Then
        Client deletedClient = clientRepository.findByRut("12345678-9");
        assertThat(deletedClient).isNull();
    }
}

