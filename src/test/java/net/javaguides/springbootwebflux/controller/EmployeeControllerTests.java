package net.javaguides.springbootwebflux.controller;

import net.javaguides.springbootwebflux.dto.EmployeeDto;
import net.javaguides.springbootwebflux.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = EmployeeController.class)
class EmployeeControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private EmployeeService employeeService;

    //Junit test for Reactive save employee REST API
    @DisplayName("Junit test for Reactive save employee REST API")
    @Test
    void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee() {
        //given - precondition or setup
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Isy");
        employeeDto.setLastName("Fawcer");
        employeeDto.setEmail("gauaa@qas.df");

        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(EmployeeDto.class)))
                .willReturn(Mono.just(employeeDto));

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

    //Junit test for Reactive get employee REST API
    @DisplayName("Junit test for Reactive get employee REST API")
    @Test
    void givenEmployeeId_whenGetEmployee_thenReturnEmployeeObject() {
        //given - precondition or setup
        String employeeId = "123";

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Isy");
        employeeDto.setLastName("Fawcer");
        employeeDto.setEmail("gauaa@qas.df");

        BDDMockito.given(employeeService.getEmployee(employeeId))
                .willReturn(Mono.just(employeeDto));

        //when - action or the behaviour that we are going to test
        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/api/employees/{id}", Collections.singletonMap("id",employeeId))
                .exchange();

        //then - verify the output
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    //Junit test for Reactive get all employees REST API
    @DisplayName("Junit test for Reactive get all employees REST API")
    @Test
    void givenListOfEmployees_whenGetAllEmployees_thenReturnListOfEmployees() {
        //given - precondition or setup
        List<EmployeeDto> employeeDtoList = new ArrayList<>();

        EmployeeDto employeeDto1 = new EmployeeDto();
        employeeDto1.setFirstName("Isy");
        employeeDto1.setLastName("Fawcer");
        employeeDto1.setEmail("gauaa@qas.df");

        employeeDtoList.add(employeeDto1);

        EmployeeDto employeeDto2 = new EmployeeDto();
        employeeDto2.setFirstName("Zertyu");
        employeeDto2.setLastName("Krawl");
        employeeDto2.setEmail("zaza@wer.hu");

        employeeDtoList.add(employeeDto2);

        Flux<EmployeeDto> employeeDtoFlux = Flux.fromIterable(employeeDtoList);

        BDDMockito.given(employeeService.getAllEmployees()).willReturn(employeeDtoFlux);

        //when - action or the behaviour that we are going to test
        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/api/employees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then - verify the output
        response.expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(System.out::println);
    }

    //Junit test for Reactive update employee REST API
    @DisplayName("Junit test for Reactive update employee REST API")
    @Test
    void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() {
        //given - precondition or setup
        String employeeId = "123";

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Isy");
        employeeDto.setLastName("Fawcer");
        employeeDto.setEmail("gauaa@qas.df");

        BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers. any(EmployeeDto.class),ArgumentMatchers.any(String.class)))
                .willReturn(Mono.just(employeeDto));

        //when - action or the behaviour that we are going to test
        WebTestClient.ResponseSpec response = webTestClient.put()
                .uri("/api/employees/{id}", Collections.singletonMap("id",employeeId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange();

        //then - verify the output
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    //Junit test for Reactive delete employee REST API
    @DisplayName("Junit test for Reactive delete employee REST API")
    @Test
    void givenEmployeeId_whenDeleteEmployee_thenReturnNothing() {
        //given - precondition or setup
        String employeeId = "123";
        BDDMockito.given(employeeService.deleteEmployee(employeeId))
                .willReturn(Mono.empty());

        //when - action or the behaviour that we are going to test
        WebTestClient.ResponseSpec response = webTestClient.delete()
                .uri("/api/employees/{id}", Collections.singletonMap("id",employeeId))
                .exchange();

        //then - verify the output
        response.expectStatus().isNoContent()
                .expectBody()
                .consumeWith(System.out::println);
    }
}
