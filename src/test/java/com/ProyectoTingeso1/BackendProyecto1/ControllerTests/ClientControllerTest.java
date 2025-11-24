package com.ProyectoTingeso1.BackendProyecto1.ControllerTests;

import com.ProyectoTingeso1.BackendProyecto1.Controllers.ClientController;
import com.ProyectoTingeso1.BackendProyecto1.Entities.Client;
import com.ProyectoTingeso1.BackendProyecto1.Services.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setName("Juan Pérez");
        client.setRut("12345678-9");
        client.setPhoneNumber("+56912345678");
        client.setEmail("juan@example.com");
        client.setStatus("ACTIVE");
    }

    @Test
    void whenSaveClient_thenReturnCreatedClient() throws Exception {
        // Given
        when(clientService.saveClient(any(Client.class))).thenReturn(client);

        // When & Then
        mockMvc.perform(post("/api/clients/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Juan Pérez"))
                .andExpect(jsonPath("$.rut").value("12345678-9"));

        verify(clientService, times(1)).saveClient(any(Client.class));
    }

    @Test
    void whenListClients_thenReturnListOfClients() throws Exception {
        // Given
        Client client2 = new Client();
        client2.setId(2L);
        client2.setName("María González");
        client2.setRut("98765432-1");

        ArrayList<Client> clients = new ArrayList<>(Arrays.asList(client, client2));
        when(clientService.getClients()).thenReturn(clients);

        // When & Then
        mockMvc.perform(get("/api/clients/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Juan Pérez"))
                .andExpect(jsonPath("$[1].name").value("María González"));

        verify(clientService, times(1)).getClients();
    }

    @Test
    void whenListClientsRut_thenReturnListOfRuts() throws Exception {
        // Given
        List<String> ruts = Arrays.asList("12345678-9", "98765432-1");
        when(clientService.getAllRuts()).thenReturn(ruts);

        // When & Then
        mockMvc.perform(get("/api/clients/rutsClients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value("12345678-9"))
                .andExpect(jsonPath("$[1]").value("98765432-1"));

        verify(clientService, times(1)).getAllRuts();
    }

    @Test
    void whenUpdateClient_thenReturnUpdatedClient() throws Exception {
        // Given
        client.setName("Juan Pérez Actualizado");
        when(clientService.updateClient(any(Client.class))).thenReturn(client);

        // When & Then
        mockMvc.perform(put("/api/clients/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Juan Pérez Actualizado"));

        verify(clientService, times(1)).updateClient(any(Client.class));
    }

    @Test
    void whenDeleteClient_thenReturnNoContent() throws Exception {
        // Given
        when(clientService.deleteClient(anyLong())).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isNoContent());

        verify(clientService, times(1)).deleteClient(1L);
    }

    @Test
    void whenDeleteClient_throwsException_thenReturnError() throws Exception {
        // Given
        when(clientService.deleteClient(anyLong())).thenThrow(new Exception("Error al eliminar"));

        // When & Then
        try {
            mockMvc.perform(delete("/api/clients/1"))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            // La excepción es esperada
        }

        verify(clientService, times(1)).deleteClient(1L);
    }
}

