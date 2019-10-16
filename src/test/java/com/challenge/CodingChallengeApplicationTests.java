//package com.challenge;
//
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.reactive.server.WebTestClient;
//
//import com.challenge.controller.BaseController;
//import com.challenge.handler.BaseHandler;
//import com.challenge.modal.EndPointsInfo;
//import com.challenge.modal.Visitor;
//import com.challenge.routes.FunctionalWeb;
//import com.challenge.service.UserService;
//
//import reactor.core.publisher.Flux;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class CodingChallengeApplicationTests {
//	
//	@Autowired
//	private FunctionalWeb config;
//	
//	@MockBean
//	private BaseHandler baseHandler;
//	
//	@MockBean
//	private UserService userService;
//	
////	@MockBean
////	private BaseController baseController;
//
//	@Test
//	public void getVisitorsTestCase() {
//		WebTestClient client = WebTestClient
//		        .bindToRouterFunction(config.routes(baseHandler))
//		        .build();
//		List<Visitor> visitorList = new ArrayList<>();
//	
//	//        Visitor employee1 = new Visitor("1", "Employee 1");
//		    Visitor expected = new Visitor("123", 1, "abhilash", "gaddam");
//		    visitorList.add(expected);
//		    when(baseHandler.getVisitors()).thenReturn(Flux.just(expected));
//		    client.get()
//		        .uri("/teams/all")
//		        .exchange()
//		        .expectStatus()
//		        .isOk()
//		        .expectBodyList(Visitor.class)
//		        .isEqualTo(visitorList);
//	}
//	
//	@Test
//	public void getEndPointsTestCase() {
//		WebTestClient client = WebTestClient
//				 .bindToController(new BaseController())
//		        .build();
//		List<EndPointsInfo> endPointsList = new ArrayList<>();
//		EndPointsInfo expected = new EndPointsInfo("{GET /statistics}", " ", "com.challenge.modal.UserStatistics");
//	    endPointsList.add(expected);
//	    Flux<EndPointsInfo> employeeFlux = Flux.fromIterable(endPointsList);
//	    when(userService.findAllEndPointsInfo()).thenReturn(employeeFlux);
//		    client.get()
//		        .uri("/teams/all")
//		        .exchange()
//		        .expectStatus()
//		        .isOk()
//		        .expectBodyList(EndPointsInfo.class)
//		        .isEqualTo(endPointsList);
//	}
//
//}
