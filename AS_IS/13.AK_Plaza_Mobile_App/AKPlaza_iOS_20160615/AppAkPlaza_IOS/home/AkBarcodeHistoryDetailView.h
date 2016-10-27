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

@interface AkBarcodeHistoryDetailView : UWNavigation <baseModelDelegate>



@property (nonatomic, retain) UIActivityIndicatorView *activityView;
@property (nonatomic, retain) UIWebView *webView;


@property (nonatomic, retain) NSString* strURL;

@end
