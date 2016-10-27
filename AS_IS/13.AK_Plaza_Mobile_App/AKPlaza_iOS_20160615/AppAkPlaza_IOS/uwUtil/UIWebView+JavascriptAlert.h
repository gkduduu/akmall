//
//  UIWebView (JavascriptAlert).h
//  AppAkMall_iOS
//
//  Created by  on 11. 12. 8..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

//@Geunwon,Mo 2010.9.17 : UIWebView 의 Javascript alert을 위한 카테고리.
@class WebFrame;
@interface UIWebView (JavaScriptAlert)

- (void)webView:(UIWebView *)sender runJavaScriptAlertPanelWithMessage:(NSString *)message initiatedByFrame:(WebFrame *)frame;


- (BOOL)webView:(UIWebView *)sender runJavaScriptConfirmPanelWithMessage:(NSString *)message initiatedByFrame:(WebFrame *)frame;


@end