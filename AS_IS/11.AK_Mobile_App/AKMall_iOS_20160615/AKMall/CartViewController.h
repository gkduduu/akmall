//
//  CartViewController.h
//  test
//
//  Created by 한병일 on 2014. 6. 12..
//  Copyright (c) 2014년 한병일. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Define.h"

@interface CartViewController : UIViewController <UIWebViewDelegate, UIAlertViewDelegate, UITabBarControllerDelegate>

@property (nonatomic, retain) IBOutlet UIWebView *webView;
@property (nonatomic, retain) UIActivityIndicatorView *activityView;

@end
