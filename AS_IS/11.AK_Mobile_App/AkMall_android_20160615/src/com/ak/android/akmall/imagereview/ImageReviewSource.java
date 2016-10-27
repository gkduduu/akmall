/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ak.android.akmall.imagereview;

import java.io.File;

public class ImageReviewSource {

	private File uploadFile;

	private String title;
	private String content;

	private String designRating;
	private String priceRating;
	private String qualityRating;
	private String deliveryRating;

	private String dispClassCd;
	private String goodsCd;

	public String getDispClassCd() {
		return dispClassCd;
	}

	public void setDispClassCd(String dispClassCd) {
		this.dispClassCd = dispClassCd;
	}

	public String getGoodsCd() {
		return goodsCd;
	}

	public void setGoodsCd(String goodsCd) {
		this.goodsCd = goodsCd;
	}

	public File getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDesignRating() {
		return designRating;
	}

	public void setDesignRating(String designRating) {
		this.designRating = designRating;
	}

	public String getPriceRating() {
		return priceRating;
	}

	public void setPriceRating(String priceRating) {
		this.priceRating = priceRating;
	}

	public String getQualityRating() {
		return qualityRating;
	}

	public void setQualityRating(String qualityRating) {
		this.qualityRating = qualityRating;
	}

	public String getDeliveryRating() {
		return deliveryRating;
	}

	public void setDeliveryRating(String deliveryRating) {
		this.deliveryRating = deliveryRating;
	}

	@Override
	public String toString() {
		return "ImageReviewSource [uploadFile=" + uploadFile + ", title=" + title + ", content="
				+ content + ", designRating=" + designRating + ", priceRating=" + priceRating
				+ ", qualityRating=" + qualityRating + ", deliveryRating=" + deliveryRating
				+ ", dispClassCd=" + dispClassCd + ", goodsCd=" + goodsCd + "]";
	}

}
