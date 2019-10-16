package com.challenge.controller.tests;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.challenge.controller.BaseController;
import com.challenge.exception.UserStatisticsException;
import com.challenge.modal.EndPointsInfo;
import com.challenge.modal.UserStatistics;
import com.challenge.modal.Visitor;
import com.challenge.request.Name;
import com.challenge.service.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * test cases for diffeent routes in the {@link RestController} are written here
 * @author Abhilash Reddy
 *
 */
@RunWith(SpringRunner.class)
@WebFluxTest(BaseController.class)
public class ControllerTests {
	
	@Autowired
    WebTestClient webTestClient;
	
	@MockBean
	private UserService userService;
	
	@Test
	public void getEndPointsTestCase() {
		List<EndPointsInfo> endPointsList = new ArrayList<>();
	    EndPointsInfo expected = new EndPointsInfo("{GET /statistics}", " ", "com.challenge.modal.UserStatistics");
	    endPointsList.add(expected);
	    Flux<EndPointsInfo> employeeFlux = Flux.fromIterable(endPointsList);
	    when(userService.findAllEndPointsInfo()).thenReturn(employeeFlux);
	    webTestClient.get()
	        .uri("/")
	        .exchange()
	        .expectStatus()
	        .isOk()
	        .expectBodyList(EndPointsInfo.class)
	        .isEqualTo(endPointsList);
	}
	
	@Test
	public void getUserStatisticsTestCase() throws UserStatisticsException {
	    UserStatistics expected = new UserStatistics();
	    expected.setTotalVisits(5);
	    List<String> firstNames = new ArrayList<String>();
	    firstNames.add("abhilash");
	    expected.setVisitorFirstNames(firstNames);
	    expected.setVisitorLastNames(firstNames);
	    List<Visitor> visitors = new ArrayList<Visitor>();
	    visitors.add(new Visitor("1", 2, "abhilash", "gaddam"));
	    expected.setVisitors(visitors);
	    when(userService.findUserStatistics()).thenReturn(expected);
	    webTestClient.get()
	        .uri("/statistics")
	        .exchange()
	        .expectStatus()
	        .isOk()
	        .expectBody(UserStatistics.class)
	        .isEqualTo(expected);
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void getGreetingTestCase() throws UserStatisticsException {
	    Name name = new Name();
	    name.setFirstName("abhilash");
	    name.setLastName("gaddam");
	    when(userService.findAndUpdateUserVisits(name)).thenReturn("welcome "+name.getFirstName()+" " + name.getLastName());
	    webTestClient.post()
	        .uri("/greeting")
	        .body(Mono.just(name), Name.class)
	        .exchange()
	        .expectStatus()
	        .isOk()
	        .expectBody()
	        .equals("welcome "+ name.getFirstName() + " " + name.getLastName());
	}
	
	@Test
	public void getGreetingWithError() throws UserStatisticsException {
	    Name name = new Name();
	    when(userService.findAndUpdateUserVisits(name)).thenReturn("welcome "+name.getFirstName()+" " + name.getLastName());
	    webTestClient.post()
	        .uri("/greeting")
	        .body(Mono.just(name), Name.class)
	        .exchange()
	        .expectStatus()
	        .is5xxServerError()
	        .expectBody()
	        .jsonPath("$.message")
	        .isEqualTo("please enter a valid name");
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void getVisitors() throws UserStatisticsException {
	    Visitor visitor = new Visitor();
	    visitor.setUserFirst("Mike");
	    visitor.setUserLast("Lyon");
	    visitor.setVisitCount(10);
	    List<Visitor> visitorList = new ArrayList<Visitor>();
	    when(userService.findReactiveVisitors()).thenReturn(Flux.fromIterable(visitorList));
	    webTestClient.get()
	        .uri("/visitors")
	        .exchange()
	        .expectStatus()
	        .isOk()
	        .expectBodyList(Visitor.class)
	        .isEqualTo(visitorList);
	}
	
	@Test
	public void getVisitorsWithError() throws UserStatisticsException {
	    Visitor visitor = new Visitor();
	    visitor.setUserFirst("Mike");
	    visitor.setUserLast("Lyon");
	    visitor.setVisitCount(10);
	    List<Visitor> visitorList = new ArrayList<Visitor>();
	    when(userService.findReactiveVisitors()).thenReturn(Flux.error(new Exception("Error in fetching Visitors Info")));
	    webTestClient.get()
	        .uri("/visitors")
	        .exchange()
	        .expectStatus()
	        .is5xxServerError()
	        .expectBody()
	        .jsonPath("$.message")
	        .isEqualTo("Error in fetching Visitors Info");
	}
}
