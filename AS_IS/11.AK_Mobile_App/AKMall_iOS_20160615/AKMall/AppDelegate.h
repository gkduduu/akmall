//
//  AppDelegate.h
//  test
//
//  Created by 한병일 on 2014. 6. 9..
//  Copyright (c) 2014년 한병일. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AudioToolbox/AudioToolbox.h>
#import <MessageUI/MessageUI.h>
#import "Define.h"
#import "Reachability.h"
#import "XmlParser.h"
#import "MyViewController.h"
#import "KeychainItemWrapper.h"


@interface AppDelegate : UIResponder <UIApplicationDelegate, UIAlertViewDelegate, UIWebViewDelegate,MFMessageComposeViewControllerDelegate, UIScrollViewDelegate, UITabBarControllerDelegate>{
    NSString *alertUrl;
    UIImageView *introView;
    UIActivityIndicatorView *activityView;
    
    CGFloat startContentOffset;
    CGFloat lastContentOffset;
    BOOL barHidden;
    
    NSInteger selectTab;
}

@property (strong, nonatomic) UIWindow *window;
- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType;
- (void)webViewDidFinishLoad:(UIWebView *)webView;
-(void)contract;
@end
