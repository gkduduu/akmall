//
//  AkReceiptBarcodeRegView.h
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 28..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import "UWNavigation.h"
#import "ZBarSDK.h"
#import "GlobalValues.h"
#import "colorUtil.h"
#import "fontSizeUtil.h"
#import "AlertMassageUtil.h"
#import "AkReceiptRegModel.h"
#import "OLogin.h"
#import "OLogin+UserDefaults.h"
#import "LoginViewController.h"

@interface AkReceiptBarcodeRegView : UWNavigation <baseModelDelegate, ZBarReaderDelegate, UITextFieldDelegate>


@property (nonatomic, retain) UWTabBar *tabView;
@property (nonatomic, retain) UITextField *txtBarcode;
@property (nonatomic, retain) UITextField *txtAmount;
@property (nonatomic, retain) UIActivityIndicatorView *activityView;

@property(retain, nonatomic) NSTimer *barcodeTimer;

@end
