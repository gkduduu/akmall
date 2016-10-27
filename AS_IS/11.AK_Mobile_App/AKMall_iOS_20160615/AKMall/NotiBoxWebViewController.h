//
//  NotiBoxWebViewController.h
//  AKMall
//
//  Created by 한병일 on 2014. 6. 21..
//  Copyright (c) 2014년 한병일. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface NotiBoxWebViewController : UIViewController <UIWebViewDelegate, UIAlertViewDelegate>

@property (nonatomic, retain) NSString *URL;
@property (nonatomic, retain) IBOutlet UIWebView *webView;
@property (nonatomic, retain) UIActivityIndicatorView *activityView;

@end
