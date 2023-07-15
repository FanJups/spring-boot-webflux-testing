package net.javaguides.springbootwebflux.controller.integration;

import net.javaguides.springbootwebflux.dto.EmployeeDto;
import net.javaguides.springbootwebflux.repository.EmployeeRepository;
import net.javaguides.springbootwebflux.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeControllerIntegrationTests {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp(){
     employeeRepository.deleteAll().subscribe();
    }

    @Test
    void testSaveEmployee(){

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Isy");
        employeeDto.setLastName("Fawcer");
        employeeDto.setEmail("gauaa@qas.df");

        //when - action or the behaviour that we are going to test
        WebTestClient.ResponseSpec response = webTestClient.post()
                .uri("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange();

        //then - verify the output
        response.expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());

    }

    @Test
    void testGettingEmployee() {
        //given - precondition or setup
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Isy");
        employeeDto.setLastName("Fawcer");
        employeeDto.setEmail("gauaa@qas.df");

        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();

        //when - action or the behaviour that we are going to test
        assert savedEmployee != null;
        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/api/employees/{id}", Collections.singletonMap("id",savedEmployee.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then - verify the output
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    void givenListOfEmployees_whenGetAllEmployees_thenReturnListOfEmployees() {
        //given - precondition or setup

        EmployeeDto employeeDto1 = new EmployeeDto();
        employeeDto1.setFirstName("Isy");
        employeeDto1.setLastName("Fawcer");
        employeeDto1.setEmail("gauaa@qas.df");

        employeeService.saveEmployee(employeeDto1).block();

        EmployeeDto employeeDto2 = new EmployeeDto();
        employeeDto2.setFirstName("Zertyu");
        employeeDto2.setLastName("Krawl");
        employeeDto2.setEmail("zaza@wer.hu");

        employeeService.saveEmployee(employeeDto2).block();


        //when - action or the behaviour that we are going to test
        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/api/employees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then - verify the output
        response.expectStatus().isOk()
                .expectBodyList(EmployeeDto.class).hasSize(2)
                .consumeWith(System.out::println);

    }

    @Test
    void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() {
        //given - precondition or setup

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Isy");
        employeeDto.setLastName("Fawcer");
        employeeDto.setEmail("gauaa@qas.df");

        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();

        EmployeeDto updatedEmployee = new EmployeeDto();
        updatedEmployee.setFirstName("Isaac");
        updatedEmployee.setLastName("Fabbbyw");
        updatedEmployee.setEmail("ertyaa@qas.df");


        //when - action or the behaviour that we are going to test
        assert savedEmployee != null;
        WebTestClient.ResponseSpec response = webTestClient.put()
                .uri("/api/employees/{id}", Collections.singletonMap("id",savedEmployee.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedEmployee), EmployeeDto.class)
                .exchange();

        //then - verify the output
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(updatedEmployee.getFirstName())
                .jsonPath("$.lastName").isEqualTo(updatedEmployee.getLastName())
                .jsonPath("$.email").isEqualTo(updatedEmployee.getEmail());
    }

    @Test
    void givenEmployeeId_whenDeleteEmployee_thenReturnNothing() {
        //given - precondition or setup
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Isy");
        employeeDto.setLastName("Fawcer");
        employeeDto.setEmail("gauaa@qas.df");

        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();
        //when - action or the behaviour that we are going to test
        WebTestClient.ResponseSpec response = webTestClient.delete()
                .uri("/api/employees/{id}", Collections.singletonMap("id",savedEmployee.getId()))
                .exchange();

        //then - verify the output
        response.expectStatus().isNoContent()
                .expectBody()
                .consumeWith(System.out::println);
    }

}
