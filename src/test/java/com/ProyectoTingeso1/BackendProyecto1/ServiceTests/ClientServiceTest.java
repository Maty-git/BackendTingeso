package com.ProyectoTingeso1.BackendProyecto1.ServiceTests;

import com.ProyectoTingeso1.BackendProyecto1.Entities.Client;
import com.ProyectoTingeso1.BackendProyecto1.Repositories.ClientRepository;
import com.ProyectoTingeso1.BackendProyecto1.Services.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private Client client1;
    private Client client2;

    @BeforeEach
    void setUp() {
        client1 = new Client();
        client1.setId(1L);
        client1.setName("Juan Pérez");
        client1.setRut("12345678-9");
        client1.setPhoneNumber("+56912345678");
        client1.setEmail("juan@example.com");
        client1.setStatus("ACTIVE");

        client2 = new Client();
        client2.setId(2L);
        client2.setName("María González");
        client2.setRut("98765432-1");
        client2.setPhoneNumber("+56987654321");
        client2.setEmail("maria@example.com");
        client2.setStatus("RESTRICTED");
    }

    @Test
    void whenSaveClient_thenClientIsSaved() {
        // Given
        when(clientRepository.save(any(Client.class))).thenReturn(client1);

        // When
        Client saved = clientService.saveClient(client1);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Juan Pérez");
        verify(clientRepository, times(1)).save(client1);
    }

    @Test
    void whenGetClients_thenReturnAllClients() {
        // Given
        ArrayList<Client> clients = new ArrayList<>(Arrays.asList(client1, client2));
        when(clientRepository.findAll()).thenReturn(clients);

        // When
        ArrayList<Client> result = clientService.getClients();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void whenGetAllRuts_thenReturnListOfRuts() {
        // Given
        List<String> ruts = Arrays.asList("12345678-9", "98765432-1");
        when(clientRepository.findAllRuts()).thenReturn(ruts);

        // When
        List<String> result = clientService.getAllRuts();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).contains("12345678-9", "98765432-1");
        verify(clientRepository, times(1)).findAllRuts();
    }

    @Test
    void whenUpdateClient_thenClientIsUpdated() {
        // Given
        client1.setName("Juan Pérez Actualizado");
        when(clientRepository.save(any(Client.class))).thenReturn(client1);

        // When
        Client updated = clientService.updateClient(client1);

        // Then
        assertThat(updated).isNotNull();
        assertThat(updated.getName()).isEqualTo("Juan Pérez Actualizado");
        verify(clientRepository, times(1)).save(client1);
    }

    @Test
    void whenDeleteClient_thenClientIsDeleted() throws Exception {
        // Given
        doNothing().when(clientRepository).deleteById(anyLong());

        // When
        boolean result = clientService.deleteClient(1L);

        // Then
        assertThat(result).isTrue();
        verify(clientRepository, times(1)).deleteById(1L);
    }

    @Test
    void whenDeleteClientThrowsException_thenThrowException() {
        // Given
        doThrow(new RuntimeException("Error al eliminar")).when(clientRepository).deleteById(anyLong());

        // When & Then
        assertThatThrownBy(() -> clientService.deleteClient(1L))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Error al eliminar");
        verify(clientRepository, times(1)).deleteById(1L);
    }

    @Test
    void whenGetClientByRut_thenReturnClient() {
        // Given
        when(clientRepository.findByRut("12345678-9")).thenReturn(client1);

        // When
        Client found = clientService.getClientByRut("12345678-9");

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getRut()).isEqualTo("12345678-9");
        assertThat(found.getName()).isEqualTo("Juan Pérez");
        verify(clientRepository, times(1)).findByRut("12345678-9");
    }

    @Test
    void whenGetClientByRutNotExists_thenReturnNull() {
        // Given
        when(clientRepository.findByRut("11111111-1")).thenReturn(null);

        // When
        Client found = clientService.getClientByRut("11111111-1");

        // Then
        assertThat(found).isNull();
        verify(clientRepository, times(1)).findByRut("11111111-1");
    }
}

