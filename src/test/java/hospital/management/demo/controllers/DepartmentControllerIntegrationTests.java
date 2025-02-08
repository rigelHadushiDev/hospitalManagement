package hospital.management.demo.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import hospital.management.demo.TestDataUtil;
import hospital.management.demo.domain.entities.DepartmentEntity;
import hospital.management.demo.repositories.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class DepartmentControllerIntegrationTests {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, DepartmentRepository departmentRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.departmentRepository = departmentRepository;
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsHttp200Status() throws Exception {

        DepartmentEntity testDepartment = TestDataUtil.createDepartmentEntity();
        testDepartment.setDepartment_id(null);
        String departmentJson = objectMapper.writeValueAsString(testDepartment);

        mockMvc.perform(MockMvcRequestBuilders.post("/department")
                .contentType(MediaType.APPLICATION_JSON)
                .content(departmentJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }


}
