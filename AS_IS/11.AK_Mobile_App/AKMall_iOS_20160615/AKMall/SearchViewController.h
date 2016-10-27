//
//  SearchViewController.h
//  test
//
//  Created by 한병일 on 2014. 6. 9..
//  Copyright (c) 2014년 한병일. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Define.h"

@interface SearchViewController : UIViewController <UIWebViewDelegate, UIAlertViewDelegate, UITabBarControllerDelegate>

@property (nonatomic, retain) IBOutlet UIWebView *webView;
@property (nonatomic, retain) UIActivityIndicatorView *activityView;
@end
