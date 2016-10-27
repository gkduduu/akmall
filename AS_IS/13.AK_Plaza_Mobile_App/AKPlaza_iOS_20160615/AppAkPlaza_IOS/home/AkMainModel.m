//
//  AkMainModel.m
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 22..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import "AkMainModel.h"
#import "ASIFormDataRequest.h"
#import "JSON.h"
#import "AlertMassageUtil.h"
//#import "IMTWebView.h"

@implementation AkMainModel


-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
//    if (buttonIndex == 1) {
    
        NSString *strUrl = [NSString stringWithFormat:@"%@/app/versionIOS.jsp", kRootURL];
        
        NSURL *url = [NSURL URLWithString:strUrl];
        
        
        ASIFormDataRequest *request = [ASIFormDataRequest requestWithURL:url];
        
        [request setDelegate:self];
        
        [request setRequestMethod:@"GET"];
        
        [request startSynchronous];
        
        NSString *responseString = [request responseString];
        DLog(@"Got responseString procVersionResult INFO: %@", responseString);
        
        NSMutableDictionary *responseJSON = [responseString JSONValue];   
        
        NSString *sendurl = [responseJSON objectForKey:@"url"];
        
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:sendurl]];
    
        exit(0);
//    }
}

//버전 체크 
- (void)procVersionResult:(ASIFormDataRequest*)request
{
    
    
    NSString *responseString = [request responseString];
    DLog(@"Got responseString procVersionResult INFO: %@", responseString);
    
    NSMutableDictionary *responseJSON = [responseString JSONValue];   

    //최신버전
    NSString *version = [responseJSON objectForKey:@"version"];
    NSString *title = [responseJSON objectForKey:@"title"];
    NSString *msg = [responseJSON objectForKey:@"msg"];

    float fVersion = [[version stringByReplacingOccurrencesOfString:@"." withString:@""] floatValue];
     
    //현재버전
    NSString *currVersion = [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"];
    
    float fCurrVersion = [[currVersion stringByReplacingOccurrencesOfString:@"." withString:@""] floatValue];
    
    DLog(@"appVersion : %@",  currVersion);
    
    //ex)현재 버전 : 1.2 < 최신 버전 : 1.1
    if (fCurrVersion < fVersion) 
    {

        //무조건 업데이트 하도록 수정.
//        MSG_DELEGATE_BTN2(title, @"취소", @"확인", msg, self);
        MSG_DELEGATE(title, @"확인", msg, self);
    }
    
    
   
}




- (void)procVersion
{
    
    NSString *strUrl = [NSString stringWithFormat:@"%@/app/versionIOS.jsp", kRootURL];
    
    NSURL *url = [NSURL URLWithString:strUrl];
    
    
    ASIFormDataRequest *request = [ASIFormDataRequest requestWithURL:url];
    
    [request setDelegate:self];
    
    [request setRequestMethod:@"GET"];
    
    [request setDidFinishSelector:@selector(procVersionResult:)];
    
    [request startSynchronous];
    
    if ([request error])
    {
        [self activityStop]; 
    }
    
    
    
}

- (UIWebView *)mainAkPlazaWebView
{
    
    UIWebView *webView = [[UIWebView alloc] init];
    
    for (UIView *v in webView.subviews) {
        if ([v isKindOfClass:[UIScrollView class]]) {
            ((UIScrollView *)v).scrollsToTop = NO;
        }
    }
    
    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"%@?act=index", kAddressURL ]];
    [NSURLRequest requestWithURL:url];
    
    webView.scalesPageToFit = YES;
    //webView.multipleTouchEnabled = YES;
    
//    [webView loadRequest:reqURL];
    
    [webView setDelegate:self];
    
    return [webView autorelease];
}


- (UIWebView *)mainSubWebView:(NSString*)strURL
{
    
    UIWebView *webView = [[UIWebView alloc] init];
    
    NSURL *url = [NSURL URLWithString:strURL];
    NSURLRequest *reqURL = [NSURLRequest requestWithURL:url];
    
    webView.scalesPageToFit = YES;
    //webView.multipleTouchEnabled = YES;
    
    [webView loadRequest:reqURL];
    
    [webView setDelegate:self];
    
    
    return [webView autorelease];
}


//
//-(void) webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error
//{
//    
//    //[error code]
//    
//    [self activityStop];
//    
//    DLog(@"-------err---------");
//    DLog(@"Connection failed! Error - %@ %@", [(NSError *)error localizedDescription], [[(NSError *)error userInfo] objectForKey:NSURLErrorFailingURLStringErrorKey]);
//    
//    if ([[(NSError *)error localizedDescription] isEqualToString:@"로드 중단됨"]) {
//        NSString *filePath = [[NSBundle mainBundle] pathForResource:@"qrcodeInfo" ofType:@"html"];  
//        NSData* htmlData = [NSData dataWithContentsOfFile:filePath];  
//        [webView loadData:htmlData MIMEType:@"text/html" textEncodingName:@"UTF-8" baseURL:nil]; 
//    }
//    else
//    {
//        NSString *filePath = [[NSBundle mainBundle] pathForResource:@"networkerr" ofType:@"html"];  
//        NSData* htmlData = [NSData dataWithContentsOfFile:filePath];  
//        [webView loadData:htmlData MIMEType:@"text/html" textEncodingName:@"UTF-8" baseURL:nil]; 
//    }
//    
//    
//    
//    
//}




@end
