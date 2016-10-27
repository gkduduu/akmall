//
//  BarcodeModalViewController.h
//  UWCutomView
//
//  Created by 미영 신 on 11. 6. 6..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UWNavigation.h"
#import "UWTabBar.h" 

#import "ASIHTTPRequest.h"
#import "JSON.h"

#import "OAuthConsumerCredentials.h"

#import "OAuth.h"
#import "OAuth+UserDefaults.h"

#import "AkNotiModel.h"
#import "NotiListItems.h"


@interface AkNotiContentViewController : UWNavigation <baseModelDelegate,UWTabBarViewDelegate, UIScrollViewDelegate, UIActionSheetDelegate> {
    
        NSMutableDictionary					*arrNotiContent;
    
    NSString *shortUrl;
    NSString *fshortUrl;
    NSString *tshortUrl;
    
    
    
    AkNotiModel *oModel;
    
    UIWebView *webView;
}
@property (nonatomic, retain) UIImageView *bgImage;
@property (nonatomic, retain)  UWTabBar *tabView;
@property (nonatomic, retain) AkNotiModel *oModel;
@property (nonatomic, retain) UIActivityIndicatorView *activityView;
- (void)onTwitt:(id)sender;

@property (nonatomic, retain) NSMutableDictionary *arrNotiContent;

@end
