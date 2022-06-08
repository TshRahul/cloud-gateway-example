package com.stereo.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.event.RefreshRoutesResultEvent;
import org.springframework.cloud.gateway.route.CachingRouteLocator;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class GatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}

	@Bean
	ApplicationListener<RefreshRoutesResultEvent> routesRefreshed(){
		return rre -> {
			System.out.println("routes updated");
			var crl = (CachingRouteLocator)rre.getSource();
			Flux<Route> routes = crl.getRoutes();
			routes.subscribe(System.out::println);
		};
	}

	@Bean
	RouteLocator gateway(RouteLocatorBuilder rlb){
		return rlb
				.routes()
				.route(routeSpec -> routeSpec
						.path("/customers")
						.uri("lb://customers/")

				)
				.build();
	}

}
