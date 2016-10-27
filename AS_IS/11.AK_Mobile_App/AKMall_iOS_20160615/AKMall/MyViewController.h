//
//  MyViewController.h
//  test
//
//  Created by 한병일 on 2014. 6. 9..
//  Copyright (c) 2014년 한병일. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Define.h"
#import "SettingTableViewController.h"
#import "NotiBoxTableViewController.h"

@interface MyViewController : UIViewController <UIWebViewDelegate, UIAlertViewDelegate, UITabBarControllerDelegate, UIScrollViewDelegate,NotiBoxTableViewControllerDelegate>

@property (nonatomic, retain) NSString* pushid;
@property (nonatomic, retain) IBOutlet UIWebView *webView;
@property (nonatomic, retain) IBOutlet UIWebView *hiddenWebView;
@property (nonatomic, retain) UIActivityIndicatorView *activityView;
@property (nonatomic, retain) NSURLRequest* naviString;
@property (nonatomic, retain) NSString *urls;

@property (weak, nonatomic) IBOutlet UIView *bottomTab;
-(void)GoUrl:(NSString*)URL;
- (IBAction)tabToggle:(UIButton *)sender;
@end