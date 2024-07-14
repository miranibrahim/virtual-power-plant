
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.virtual_power_plant.controller.BatteryController;
import com.example.virtual_power_plant.model.Battery;
import com.example.virtual_power_plant.service.BatteryService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class BatteryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BatteryService batteryService;

    @InjectMocks
    private BatteryController batteryController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(batteryController).build();
    }

    @Test
    public void testAddBatteries() throws Exception {
        // Mock data
        List<Battery> batteries = Arrays.asList(
                new Battery("Battery1", "12345", 100),
                new Battery("Battery2", "23456", 200),
                new Battery("Battery3", "34567", 150)
        );

        // Mock service method
        when(batteryService.saveBatteries(anyList())).thenReturn(batteries);

        // Perform POST request
        mockMvc.perform(post("/batteries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(batteries)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(batteries.size()));
    }

    @Test
    public void testGetBatteriesByPostcodeRange() throws Exception {
        // Mock data
        List<Battery> batteries = Arrays.asList(
                new Battery("Battery1", "12345", 100),
                new Battery("Battery2", "23456", 200),
                new Battery("Battery3", "34567", 150)
        );

        // Mock service method
        when(batteryService.getBatteriesByPostcodeRange("20000", "30000")).thenReturn(batteries);

        // Perform GET request
        mockMvc.perform(get("/batteries?postcodeRange=20000-30000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batteryNames").isArray())
                .andExpect(jsonPath("$.batteryNames[0]").value("Battery1"))
                .andExpect(jsonPath("$.batteryNames[1]").value("Battery2"))
                .andExpect(jsonPath("$.batteryNames[2]").value("Battery3"))
                .andExpect(jsonPath("$.statistics.totalWattCapacity").value(450))
                .andExpect(jsonPath("$.statistics.averageWattCapacity").value(150.0));
    }

    // Utility method to convert object to JSON string
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
