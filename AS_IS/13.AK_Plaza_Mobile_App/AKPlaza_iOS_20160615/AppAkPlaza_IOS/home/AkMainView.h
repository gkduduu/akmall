//
//  AkMainView.h
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 21..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import "UWNavigation.h"
#import "AkMainModel.h"

#import "ZBarSDK.h"
#import "UWTabBar.h"
#import "GlobalValues.h"
#import "colorUtil.h"
#import "fontSizeUtil.h"
#import "CommonURL.h"

//#import "IMTWebView.h"
//#import "PDColoredProgressView.h"


#import "AkLoginWithNotiSettingView.h"
#import "AkNotiListBoxViewController.h"
#import "AkReceiptBarcodeRegView.h"

@interface AkMainView : UWNavigation <baseModelDelegate,ZBarReaderDelegate,UWTabBarViewDelegate>
{
    NSString* strViewType;
}



//@property (nonatomic, retain) MBProgressHUD *activityView;
@property (nonatomic, retain) UIActivityIndicatorView *activityView;
//@property (nonatomic, retain) PDColoredProgressView *progressView;

@property (nonatomic, retain) UIWebView *webView;

@property (nonatomic, retain) AkMainModel *oModel;



@property (nonatomic, retain)  UWTabBar *tabView;

@property (nonatomic, retain) NSString* strURL;


@property (nonatomic) BOOL isPhotoBack;


@end
