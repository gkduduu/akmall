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
package com.ak.android.akmall.my;

public class MyList {
	int Type;
	int Text;
	int Text2;
	int Text3;
	int Image1;
	int Image2;
	int Image3;
	int Image4;
	boolean status;
	
	
	public MyList(int aType, int aText,int bText,int aImage, int cText,int bImage, int cImage,int dImage,boolean astatus){
		setType(aType);
		Text = aText;
		Text2 = bText;
		Image1 = aImage;
		Text3 = cText;		
		Image2 = bImage;
		Image3 = cImage;
		Image4 = dImage;
		status = astatus;
	}
	public MyList(int aType, int aText,int bText,int aImage, int cText,int bImage, int cImage,int dImage){
		setType(aType);
		Text = aText;
		Text2 = bText;
		Image1 = aImage;
		Text3 = cText;		
		Image2 = bImage;
		Image3 = cImage;
		Image4 = dImage;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	public int getText() {
		return Text;
	}

	public void setText(int text) {
		Text = text;
	}

	public int getText2() {
		return Text2;
	}

	public void setText2(int text2) {
		Text2 = text2;
	}

	public int getText3() {
		return Text3;
	}

	public void setText3(int text3) {
		Text3 = text3;
	}

	public int getImage1() {
		return Image1;
	}

	public void setImage1(int image1) {
		Image1 = image1;
	}

	public int getImage2() {
		return Image2;
	}

	public void setImage2(int image2) {
		Image2 = image2;
	}

	public int getImage3() {
		return Image3;
	}

	public void setImage3(int image3) {
		Image3 = image3;
	}

	public int getImage4() {
		return Image4;
	}

	public void setImage4(int image4) {
		Image4 = image4;
	}







	
}
