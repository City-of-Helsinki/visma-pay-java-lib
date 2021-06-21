package org.helsinki.vismapay.model.payment;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class Refund implements Serializable {

	@SerializedName("refund_id")
	private Long refundId;

	private BigDecimal amount;

	@SerializedName("order_number")
	private String orderNumber;

	@SerializedName("created_at")
	private String createdAt; // TODO: date conversion?

	private Short status;

	@SerializedName("payment_products")
	private Product[] paymentProducts;
}
