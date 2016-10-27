//
//  AkBarcodeHistoryDetailView.h
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 27..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import "UWNavigation.h"
#import "AkMainModel.h"
#import "colorUtil.h"
#import "fontSizeUtil.h"
#import "GlobalValues.h"

#import "AkBarcodeHistoryListView.h"

#import "ZBarSDK.h"
#import "UWTabBar.h"

@interface AkBarcodeDetailView : UWNavigation <baseModelDelegate,ZBarReaderDelegate,UWTabBarViewDelegate>
   


@property (nonatomic, retain) UIActivityIndicatorView *activityView;
@property (nonatomic, retain) UIWebView *webView;
@property (nonatomic, retain)  UWTabBar *tabView;

@property (nonatomic, retain) NSString* strURL;


@property(retain, nonatomic) NSTimer *barcodeTimer;
@property(retain, nonatomic) ZBarReaderViewController *reader;//p65458 20150511 ios8 not action -> add
@end
