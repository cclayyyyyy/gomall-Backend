package com.atchen.gomall.model.entity.product;

import com.atchen.gomall.model.entity.base.BaseEntity;
import lombok.Data;

@Data
public class ProductDetails extends BaseEntity {

	private Long productId;
	private String imageUrls;

}