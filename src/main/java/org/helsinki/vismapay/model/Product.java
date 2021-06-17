package org.helsinki.vismapay.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class Product implements Serializable {
	private static final long serialVersionUID = -823447845648L;

	public enum Type {
		PRODUCT(1),
		SHIPMENT_COST(2),
		HANDLING_COST(3),
		DISCOUNT(4);

		private final int value;

		Type(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	private String id;
	private String title;
	private Integer count;
	private Integer tax; // TODO: will integer be enough?

	@SerializedName(value = "pretax_price")
	private BigDecimal pretaxPrice;

	private BigDecimal price;
	private Type type = Type.PRODUCT;

	@SerializedName(value = "merchant_id")
	private Long merchantId;

	private String cp;

	@Override
	public final int hashCode() {
		return (id != null ? id.hashCode() : System.identityHashCode(this));
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!this.getClass().isAssignableFrom(o.getClass())) {
			return false;
		}

		final Product that = (Product) o;

		if (id == null || that.id == null) {
			return false;
		}
		return id.equals(that.id);
	}
}
