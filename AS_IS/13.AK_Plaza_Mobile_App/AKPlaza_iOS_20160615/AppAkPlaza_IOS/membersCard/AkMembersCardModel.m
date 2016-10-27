//
//  AkMembersCardModel.m
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 22..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import "AkMembersCardModel.h"


@implementation AkMembersCardModel





- (UIWebView *)subMyMileageListWebView
{
    
    UIWebView *webView = [[UIWebView alloc] init];
    
    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat: @"%@?act=myQnaList", kAddressURL]];
    NSURLRequest *reqURL = [NSURLRequest requestWithURL:url];
    
    webView.scalesPageToFit = YES;
    //webView.multipleTouchEnabled = YES;
    
    [webView loadRequest:reqURL];
    
    [webView setDelegate:self];
    
    
    return [webView autorelease];
}



- (UIWebView *)subCardInfoWebView
{
    
    UIWebView *webView = [[UIWebView alloc] init];
    
    //NSURL *url = [NSURL URLWithString:[NSString stringWithFormat: @"%@?act=membersCard", kAddressURL]];
    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat: @"%@?act=myak", kAddressURL]];
    NSURLRequest *reqURL = [NSURLRequest requestWithURL:url];
    
    webView.scalesPageToFit = YES;
    //webView.multipleTouchEnabled = YES;
    
    [webView loadRequest:reqURL];
    
    [webView setDelegate:self];
    
    
    return [webView autorelease];
}
@end
