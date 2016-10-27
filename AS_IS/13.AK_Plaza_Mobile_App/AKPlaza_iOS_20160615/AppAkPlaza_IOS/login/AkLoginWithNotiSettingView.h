//
//  NoticeViewController.h
//  UWCutomView
//
//  Created by uniwis on 11. 6. 8..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UWNavigation.h"
#import "CustomUISwitch.h"

#import "FBFunLoginDialog.h"

#import "AlertMassageUtil.h"

#import "TwitterLoginPopupDelegate.h"
#import "TwitterLoginUiFeedback.h"

#import "ASIHTTPRequest.h"
#import "JSON.h"

#import "OAuthConsumerCredentials.h"

//@class OAuth, CustomLoginPopup;

#import "OAuth.h"
#import "CustomLoginPopup.h"
#import "OAuth+UserDefaults.h"

#import "OLogin.h"
#import "OLogin+UserDefaults.h"

#import "fontSizeUtil.h"
#import "colorUtil.h"

#import "AkLoginModel.h"


@interface AkLoginWithNotiSettingView : UWNavigation <baseModelDelegate, TwitterLoginPopupDelegate, TwitterLoginUiFeedback,FBFunLoginDialogDelegate, UITableViewDelegate, UITableViewDataSource> {
    
    UITableView *tableView;
    
    
    FBFunLoginDialog  *fb;
    
    UIView *loginDialogView;
    
    OAuth *	oAuth;
    
    CustomLoginPopup *twitter;
    
}

@property (nonatomic, retain) UITableView *tableView;
@property (nonatomic, retain) UIActivityIndicatorView *activityView;


@property (copy) NSString *accessToken;

@property (retain) IBOutlet UIView *loginDialogView;
@property (nonatomic, retain) CustomLoginPopup *twitter;
@property (nonatomic, retain) FBFunLoginDialog  *fb;
@property (nonatomic, retain) OAuth *	oAuth;


- (void)getFacebookProfileFinished:(ASIHTTPRequest *)request;

@end
