package com.ElectroMarket.paymentservice;

import com.ElectroMarket.paymentservice.config.SecurityConfig;
import com.ElectroMarket.paymentservice.dto.PaymentRequestMessage;
import com.ElectroMarket.paymentservice.dto.PaymentCompletedMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureWebTestClient
@Import(SecurityConfig.class)
class PaymentServiceApplicationTests {
	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	ReactiveJwtDecoder reactiveJwtDecoder;

	@Test
	void whenCreatePaymentThenReturn()	{

		var request = new PaymentRequestMessage(123L, BigDecimal.ONE);
        webTestClient
				.mutateWith(SecurityMockServerConfigurers.mockJwt()
						.authorities(new SimpleGrantedAuthority("ROLE_customer")))
				.post()
                .uri("/createPayment")
                .bodyValue(request)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(String.class).value(paypalLink -> {
					assertThat(paypalLink).isNotNull();
					assertThat(paypalLink).isNotEmpty();
				});
	}

	@Test
	void whenCapturePaymentThenReturn()	{
		webTestClient
				.post()
				.uri("/capturePayment")
				.bodyValue("orderId")
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBodyList(PaymentCompletedMessage.class);
	}
}
