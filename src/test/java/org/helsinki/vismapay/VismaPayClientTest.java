package org.helsinki.vismapay;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.helsinki.vismapay.model.Initiator;
import org.helsinki.vismapay.model.Product;
import org.helsinki.vismapay.model.Source;
import org.helsinki.vismapay.model.PaymentMethod;
import org.helsinki.vismapay.request.*;
import org.helsinki.vismapay.response.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertSame;

public class VismaPayClientTest {

	private final static String MOCK_WEB_SERVER_BASE_URL = "/some-url";
	private final static String DEFAULT_CONTENT_TYPE_HEADER = "application/json; charset=" + StandardCharsets.UTF_8;

	private MockWebServer mockWebServer;
	private VismaPayClient client;

	@Before
	public void setUp() throws IOException {
		mockWebServer = new MockWebServer();
		mockWebServer.start();

		HttpUrl baseUrl = mockWebServer.url(MOCK_WEB_SERVER_BASE_URL);
		client = new VismaPayClient(
				"TESTAPIKEY",
				"private_key",
				"w3.1",
				baseUrl.toString()
		);
	}

	@After
	public void teardown() throws IOException {
		mockWebServer.shutdown();
	}

	@Test
	public void testGetTokenEPayment() throws Exception {
		String expectedResponseBody = read("get_token_e-payment_response_body.json");
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<ChargeResponse> responseCF =
				client.sendRequest(getChargeRequestForGetTokenEPayment());

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/auth_payment", request.getPath());
		assertEquals(
				DEFAULT_CONTENT_TYPE_HEADER,
				request.getHeader("Content-Type")
		);
		JSONAssert.assertEquals(
				read("get_token_e-payment_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		ChargeResponse response = responseCF.get();
		assertEquals("test_token", response.getToken());
		assertEquals(PaymentMethod.TYPE_EPAYMENT, response.getType());
	}

	@Test
	public void testGetTokenCard() throws Exception {
		String expectedResponseBody = read("get_token_card_response_body.json");
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<ChargeResponse> responseCF =
				client.sendRequest(getChargeRequestForGetTokenCard());

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/auth_payment", request.getPath());
		JSONAssert.assertEquals(
				read("get_token_card_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		ChargeResponse response = responseCF.get();
		assertEquals("test_token", response.getToken());
		assertEquals(Source.TYPE_CARD, response.getType());
	}

	@Test
	public void testGetStatusWithToken() throws Exception {
		String expectedResponseBody = "{\"result\": 0}";
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<PaymentStatusResponse> responseCF =
				client.sendRequest(getPaymentStatusRequest(true));

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/check_payment_status", request.getPath());
		JSONAssert.assertEquals(
				read("check_payment_status_with_token_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		PaymentStatusResponse response = responseCF.get();
		assertSame(0, response.getResult());
	}

	@Test
	public void testGetStatusWithOrderNumber() throws Exception {
		String expectedResponseBody = "{\"result\": 0}";
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<PaymentStatusResponse> responseCF =
				client.sendRequest(getPaymentStatusRequest(false));

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/check_payment_status", request.getPath());
		JSONAssert.assertEquals(
				read("check_payment_status_with_order_number_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		PaymentStatusResponse response = responseCF.get();
		assertSame(0, response.getResult());
	}

	// TODO: currently does not throw exception... remove later if not needed
	/*@Test
	public void testGetTokenThrowsException() throws Exception {
		arrangeMockServerResponse("kkk", 500);

		PaymentToken paymentToken = new PaymentToken();
		paymentToken.setAmount(BigInteger.valueOf(100))
				.setOrderNumber("a")
				.setCurrency("EUR");

		CompletableFuture<ChargeResponse> chargeResponseCF = client.sendRequest(new ChargeRequest(paymentToken));

		assertSame(1, mockWebServer.getRequestCount());
	}*/

	@Test
	public void testChargeCardToken() throws Exception {
		String expectedResponseBody = read("charge_card_token_response_body.json");
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<ChargeCardTokenResponse> responseCF =
				client.sendRequest(getChargeCardTokenRequest(false));

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/charge_card_token", request.getPath());
		JSONAssert.assertEquals(
				read("charge_card_token_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		ChargeCardTokenResponse response = responseCF.get();
		assertSame(0, response.getResult());
	}

	@Test
	public void testChargeCardTokenCIT() throws Exception {
		String expectedResponseBody = read("charge_card_token_cit_response_body.json");
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<ChargeCardTokenResponse> responseCF =
				client.sendRequest(getChargeCardTokenRequest(true));

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/charge_card_token", request.getPath());
		JSONAssert.assertEquals(
				read("charge_card_token_cit_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		ChargeCardTokenResponse response = responseCF.get();
		assertSame(30, response.getResult());
	}

	@Test
	public void testCapturePayment() throws Exception {
		String expectedResponseBody = "{\"result\": 0}";
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<VismaPayResponse> responseCF =
				client.sendRequest(getCapturePaymentRequest());

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/capture", request.getPath());
		JSONAssert.assertEquals(
				read("capture_payment_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		VismaPayResponse response = responseCF.get();
		assertSame(0, response.getResult());
	}

	@Test
	public void testCancelPayment() throws Exception {
		String expectedResponseBody = "{\"result\": 0}";
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<VismaPayResponse> responseCF =
				client.sendRequest(getCancelPaymentRequest());

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/cancel", request.getPath());
		JSONAssert.assertEquals(
				read("cancel_payment_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		VismaPayResponse response = responseCF.get();
		assertSame(0, response.getResult());
	}

	@Test
	public void testGetCardToken() throws Exception {
		String expectedResponseBody = read("get_card_token_response_body.json");
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<CardTokenResponse> responseCF =
				client.sendRequest(getCardTokenRequest());

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/get_card_token", request.getPath());
		JSONAssert.assertEquals(
				read("get_card_token_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		assertCardTokenResponseValues(responseCF.get());
	}

	@Test
	public void testDeleteCardToken() throws Exception {
		String expectedResponseBody = "{\"result\": 0}";
		arrangeMockServerResponse(expectedResponseBody);

		CompletableFuture<VismaPayResponse> responseCF =
				client.sendRequest(getDeleteCardTokenRequest());

		RecordedRequest request = mockWebServer.takeRequest();
		assertSame(1, mockWebServer.getRequestCount());
		assertEquals(MOCK_WEB_SERVER_BASE_URL + "/delete_card_token", request.getPath());
		JSONAssert.assertEquals(
				read("get_card_token_request_body.json"),
				request.getBody().readUtf8(),
				true
		);

		VismaPayResponse response = responseCF.get();
		assertSame(0, response.getResult());
	}

	// TODO: testDeleteCardToken
	// TODO: testGetMerchantPaymentMethods
	// TODO: testGetPayment
	// TODO: testGetRefund
	// TODO: testCreateRefund
	// TODO: testCancelRefund

	private ChargeRequest getChargeRequestForGetTokenEPayment() {
		PaymentMethod paymentMethod = new PaymentMethod();
		paymentMethod.setType(PaymentMethod.TYPE_EPAYMENT)
				.setReturnUrl("https://localhost/return")
				.setNotifyUrl("https://localhost/return");

		Product product = new Product();
		product.setId("as123")
				.setTitle("Product 1")
				.setCount(1)
				.setPretaxPrice(BigDecimal.valueOf(300))
				.setTax(24)
				.setPrice(BigDecimal.valueOf(372));

		ChargeRequest.PaymentTokenPayload payload = new ChargeRequest.PaymentTokenPayload();
		payload.setAmount(BigInteger.valueOf(100))
				.setOrderNumber("a")
				.setCurrency("EUR")
				.setPaymentMethod(paymentMethod)
				.addProduct(product);

		return new ChargeRequest(payload);
	}

	private ChargeRequest getChargeRequestForGetTokenCard() {
		PaymentMethod paymentMethod = new PaymentMethod();
		paymentMethod.setType(PaymentMethod.TYPE_CARD);

		ChargeRequest.PaymentTokenPayload payload = new ChargeRequest.PaymentTokenPayload();
		payload.setAmount(BigInteger.valueOf(100))
				.setOrderNumber("a")
				.setCurrency("EUR")
				.setPaymentMethod(paymentMethod);

		return new ChargeRequest(payload);
	}

	private CheckPaymentStatusRequest getPaymentStatusRequest(boolean useToken) {
		CheckPaymentStatusRequest.PaymentStatusPayload payload = new CheckPaymentStatusRequest.PaymentStatusPayload();

		if (useToken) {
			payload.setToken("test_token");
		} else {
			payload.setOrderNumber("test_token");
		}
		return new CheckPaymentStatusRequest(payload);
	}

	private ChargeCardTokenRequest getChargeCardTokenRequest(boolean citVersion) {
		ChargeCardTokenRequest.CardTokenPayload payload = new ChargeCardTokenRequest.CardTokenPayload();
		payload.setAmount(BigInteger.valueOf(100))
				.setOrderNumber("a")
				.setCurrency("EUR")
				.setCardToken("card_token");

		if(citVersion) {
			Initiator initiator = new Initiator().setType(Initiator.TYPE_CUSTOMER_INITIATED)
					.setReturnUrl("https://localhost/return")
					.setNotifyUrl("https://localhost/return");

			payload.setInitiator(initiator);
		}
		return new ChargeCardTokenRequest(payload);
	}

	private CapturePaymentRequest getCapturePaymentRequest() {
		CapturePaymentRequest.CapturePaymentPayload payload = new CapturePaymentRequest.CapturePaymentPayload();
		payload.setOrderNumber("a");
		return new CapturePaymentRequest(payload);
	}

	private CancelPaymentRequest getCancelPaymentRequest() {
		CancelPaymentRequest.CancelPaymentPayload payload = new CancelPaymentRequest.CancelPaymentPayload();
		payload.setOrderNumber("a");
		return new CancelPaymentRequest(payload);
	}

	private CardTokenRequest getCardTokenRequest() {
		CardTokenRequest.CardTokenPayload payload = new CardTokenRequest.CardTokenPayload();
		payload.setCardToken("card_token");
		return new CardTokenRequest(payload);
	}

	private DeleteCardTokenRequest getDeleteCardTokenRequest() {
		DeleteCardTokenRequest.DeleteCardTokenPayload payload = new DeleteCardTokenRequest.DeleteCardTokenPayload();
		payload.setCardToken("card_token");
		return new DeleteCardTokenRequest(payload);
	}

	private void assertCardTokenResponseValues(CardTokenResponse response) {
		Source source = response.getSource();

		assertSame(0, response.getResult());
		assertEquals(Source.TYPE_CARD, source.getObject());
		assertEquals("1234", source.getLast4());
		assertEquals(Short.valueOf((short)2015), source.getExpYear());
		assertSame((byte)5, source.getExpMonth());
		assertEquals("Visa", source.getBrand());
		assertEquals("card_token", source.getCardToken());
	}

	private void arrangeMockServerResponse(String body) {
		arrangeMockServerResponse(body, 200);
	}

	private void arrangeMockServerResponse(String body, int code) {
		mockWebServer.enqueue(new MockResponse()
				.addHeader("Content-Type", DEFAULT_CONTENT_TYPE_HEADER)
				.setBody(body)
				.setResponseCode(code));
	}

	private String read(String fileName) throws Exception {
		Path filePath = Paths.get("src","test","resources", "org.helsinki.vismapay", fileName);

		byte[] bytes = Files.readAllBytes(filePath);
		return new String(bytes, StandardCharsets.UTF_8);
	}
}