//
//  UIWebView (JavascriptAlert).m
//  AppAkMall_iOS
//
//  Created by  on 11. 12. 8..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import "UIWebView+JavascriptAlert.h"

//@Geunwon,Mo 2010.9.17 : UIWebView 의 Javascript alert을 위한 카테고리.

@implementation UIWebView (JavaScriptAlert)

typedef enum _WEBVIEW_JS_CMD_STATE {
    WEBVIEW_JS_CMD_STATE_NONE = 0x00,
    WEBVIEW_JS_CMD_STATE_CANCEL   = 0x01,
    WEBVIEW_JS_CMD_STATE_OK  = 0x02
} WEBVIEW_JS_CMD_STATE;

static volatile WEBVIEW_JS_CMD_STATE _pageWaitUserSelection = WEBVIEW_JS_CMD_STATE_NONE;

- (void)webView:(UIWebView *)sender runJavaScriptAlertPanelWithMessage:(NSString *)message initiatedByFrame:(WebFrame *)frame {
    
    
    DLog(@"javascript alert : %@",message);
    
    UIAlertView* customAlert = [[UIAlertView alloc] initWithTitle:@"AK플라자" message:message delegate:nil cancelButtonTitle:@"확인" otherButtonTitles:nil];
    
    [customAlert show];
    
    [customAlert autorelease];
    
}

- (BOOL)webView:(UIWebView *)sender runJavaScriptConfirmPanelWithMessage:(NSString *)message initiatedByFrame:(WebFrame *)frame{
    
    UIAlertView *webViewConfirm = [[UIAlertView alloc] initWithTitle:@"AK플라자" message:message delegate:self cancelButtonTitle:@"아니오"otherButtonTitles:@"예", nil];    [webViewConfirm show];
    
    
    _pageWaitUserSelection = WEBVIEW_JS_CMD_STATE_NONE;
    while (_pageWaitUserSelection == WEBVIEW_JS_CMD_STATE_NONE)
    {
        [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode beforeDate:[NSDate distantFuture]];
    }
    WEBVIEW_JS_CMD_STATE confirm_value = _pageWaitUserSelection;
    _pageWaitUserSelection             = WEBVIEW_JS_CMD_STATE_NONE;
    
    webViewConfirm.delegate = nil;
    [webViewConfirm release];
    
    if(confirm_value == WEBVIEW_JS_CMD_STATE_OK)
    {
        return YES;
    }
    else
    {
        return NO;
    }
    
}



//요놈은 UIAlertViewDelegate 를 구현하여 버튼이 눌렸을때 실행될 메소드

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    
    //index 0 : NO , 1 : YES
    
    if(alertView)
    {
        if (alertView.cancelButtonIndex == buttonIndex)
            _pageWaitUserSelection = WEBVIEW_JS_CMD_STATE_CANCEL;
        else
            _pageWaitUserSelection = WEBVIEW_JS_CMD_STATE_OK;
    }
    else
    {
        _pageWaitUserSelection = WEBVIEW_JS_CMD_STATE_CANCEL;
    }
}



@end