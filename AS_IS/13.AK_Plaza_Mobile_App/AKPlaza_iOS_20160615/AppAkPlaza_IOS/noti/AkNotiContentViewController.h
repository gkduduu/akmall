//
//  BarcodeModalViewController.h
//  UWCutomView
//
//  Created by 미영 신 on 11. 6. 6..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UWNavigation.h"

#import "AkLoginWithNotiSettingView.h"

#import "fontSizeUtil.h"
#import "colorUtil.h"
#import "AkNotiModel.h"

#import "ASIHTTPRequest.h"
#import "JSON.h"

#import "OAuthConsumerCredentials.h"

#import "OAuth.h"
#import "OAuth+UserDefaults.h"

#import "ASIFormDataRequest.h"

#import "NotiListItems.h"

#define kAccessToken @"accessToken"

@interface AkNotiContentViewController : UWNavigation <baseModelDelegate, UIActionSheetDelegate> {
    
    NSString *fshortUrl;
    NSString *tshortUrl;
    
    
}
@property (nonatomic, retain) AkNotiModel *oModel;
@property (nonatomic, retain) NSString* strTitle;
@property (nonatomic, retain) UIActivityIndicatorView *activityView;
@property (nonatomic, retain) UIWebView *webView;
@property (nonatomic, retain) NSString* strShortURL;

@property (nonatomic, retain) NSMutableDictionary *arrNotiContent;

- (void)onTwitt:(id)sender;

@end
