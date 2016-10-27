//
//  AkBrandSearchModel.m
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 22..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import "AkBrandSearchModel.h"

@implementation AkBrandSearchModel



- (UIWebView *)mainBrandSearcWebView
{
    
    UIWebView *webView = [[UIWebView alloc] init];
    
    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"%@?act=searchBrand", kAddressURL]];
    
    NSURLRequest *reqURL = [NSURLRequest requestWithURL:url];
    
    webView.scalesPageToFit = YES;
    //webView.multipleTouchEnabled = YES;
    
    [webView loadRequest:reqURL];
    
    [webView setDelegate:self];
    
    
    return [webView autorelease];
}



@end
