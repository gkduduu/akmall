//
//  baseModel.m
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 22..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import "baseModel.h"
#import "UWRequest.h"
#import "UWNavigation.h"
#import "AkNotiModel.h"

@implementation baseModel


@synthesize delegate;

bool cookieFlag = true;


-(void)dealloc
{
    
    [super dealloc];
}

#pragma mark activityIndicatorView 


-(void)activityStart
{
    /*
    [[UIApplication sharedApplication] beginIgnoringInteractionEvents];
    
    [[UIApplication sharedApplication] endIgnoringInteractionEvents];
    [출처] ActivityIndicator 동작시 화면 터치 안되게 (맥부기 애플(iOS,Mac) 개발자모임) |작성자 JHK
    */
    
    /*
    // The hud will dispable all input on the view (use the higest view possible in the view hierarchy)
    act = [[MBProgressHUD alloc] initWithView:[self.delegate navigationControllerSetting].view];
    [[self.delegate navigationControllerSetting].view addSubview:act];
    
    // Regiser for HUD callbacks so we can remove it from the window at the right time
    act.delegate = self;
    
    // Show the HUD while the provided method executes in a new thread
    [act showWhileExecuting:@selector(myTask) onTarget:self withObject:nil animated:YES];
     */
    
    if (!act.isAnimating) {
        [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
        
        
        //메모리 릭 나는 곳.. release하고 removeFromSuperview하니깐 더이상 low memory 안난다 
        [act release];
        [act removeFromSuperview];
        act = nil;
        
        act = [[UIActivityIndicatorView alloc] initWithFrame:CGRectMake(0, 0, 32, 32)] ;
        
        act.activityIndicatorViewStyle = UIActivityIndicatorViewStyleGray;
        
        [self.delegate activityStartView:act];
    }



    
}

-(void)activityStop
{
    if (act.isAnimating) {
        [act stopAnimating];
        //[act hide:YES];
        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
        
        
    }

}

//2014.12.04
//앱 실행시 메인화면에서 버전정보등을 못불러오기에 추가.(로컬에서는 작동안함)
-(void) SetCookie {
    if(cookieFlag){
        NSMutableDictionary *cookieProperties = [NSMutableDictionary dictionary];
        [cookieProperties setObject:@"makp-device" forKey:NSHTTPCookieName];
        [cookieProperties setObject:@"iOS" forKey:NSHTTPCookieValue];
        [cookieProperties setObject:kURL forKey:NSHTTPCookieDomain];
        [cookieProperties setObject:kURL forKey:NSHTTPCookieOriginURL];
        [cookieProperties setObject:@"/" forKey:NSHTTPCookiePath];
        [cookieProperties setObject:@"0" forKey:NSHTTPCookieVersion];
        
        
        [[NSHTTPCookieStorage sharedHTTPCookieStorage] setCookie:[NSHTTPCookie cookieWithProperties:cookieProperties]];
        
        [cookieProperties setObject:@"makp-version" forKey:NSHTTPCookieName];
        [cookieProperties setObject:[[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"] forKey:NSHTTPCookieValue];
        
        [[NSHTTPCookieStorage sharedHTTPCookieStorage] setCookie:[NSHTTPCookie cookieWithProperties:cookieProperties]];
        cookieFlag = false;
    }
}


#pragma mark webView 

-(void) webViewDidStartLoad:(UIWebView *)webView
{
    
    [self activityStart];
    
    [self SetCookie];
    

}



-(void) webViewDidFinishLoad:(UIWebView *)wView
{
    /*
    NSString *title = [wView stringByEvaluatingJavaScriptFromString:@"document.title" ];
    
    [(UWNavigation *)self.delegate viewLblNaviTitle:title];
    */
    
    
//    NSString *js = @"\
//    var d = document.getElementsByTagName('a');\
//    for (var i = 0; i < d.length; i++) {\
//    if (d[i].getAttribute('target') == '_blank') {\
//    d[i].removeAttribute('target');\
//    }\
//    }\
//    ";
    
    [self activityStop];
    
    
    //_blank 새창 띄우기 ////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
    //클리어 
    [[NSUserDefaults standardUserDefaults] removeObjectForKey: @"_blank"];
    
    NSString *js = @"\
    var d = document.getElementsByTagName('a');\
    var s = '';\
    for (var i = 0; i < d.length; i++) {\
        if (d[i].getAttribute('target') == '_blank') {\
            if(i < (d.length - 1))\
                s += d[i].getAttribute('href') + ',';\
            else\
                s += d[i].getAttribute('href');\
        }\
    }\
    ";
 
    
    //NSString *js = @"$('a[target=_blank]').map(function(i, v) { return v.getAttribute('href');});";
    
    DLog(@"javascript _blank url find= %@", js);
    
    NSString *jsResult = [wView stringByEvaluatingJavaScriptFromString:js];
    
    DLog(@"a target _blank is href = %@", jsResult);
    
    
    //적재 
    [[NSUserDefaults standardUserDefaults]  setObject:jsResult        forKey: @"_blank"];
    
    */
    ////////////////////////////////////////////////////////////////////////////////////////////
    
    if ([self.delegate respondsToSelector:@selector(actionGoBack)]) {
        [self.delegate actionGoBack];
    } 
    
}

-(void) webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error
{
    [self activityStop];
//    DLog(@"-------err---------");
//    DLog(@"Connection failed! Error - %@ %@", [(NSError *)error localizedDescription], [[(NSError *)error userInfo] objectForKey:NSURLErrorFailingURLStringErrorKey]);    
//    NSString *filePath = [[NSBundle mainBundle] pathForResource:@"networkerr" ofType:@"html"];  
//    NSData* htmlData = [NSData dataWithContentsOfFile:filePath];  
//    [webView loadData:htmlData MIMEType:@"text/html" textEncodingName:@"UTF-8" baseURL:nil];
}

-(BOOL) webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    
    NSURL *url = request.URL;
	NSString *eventStr = url.absoluteString;
    
    NSLog(@"%@",eventStr);
    
    if ([[[request URL] absoluteString] isEqualToString:@"about:blank"]) {
        return NO;
    }
    
    if([eventStr isEqualToString:@"itms-appss://itunes.apple.com/kr/app/id369125087?mt=8"] || [eventStr isEqualToString:@"itms-apps://itunes.apple.com/kr/app/id369125087?mt=8"])
    {
        [[UIApplication sharedApplication] openURL: [NSURL URLWithString:@"http://itunes.apple.com/kr/app/mobail-anjeongyeolje-isp/id369125087?mt=8"]];
    }
    
    if([eventStr isEqualToString:@"itms-appss://itunes.apple.com/us/app/paypin-peipin/id490896496?ls=1&mt=8"] || [eventStr isEqualToString:@"itms-apps://itunes.apple.com/us/app/paypin-peipin/id490896496?ls=1&mt=8"])
    {
        [[UIApplication sharedApplication] openURL: [NSURL URLWithString:@"http://itunes.apple.com/us/app/paypin-peipin/id490896496?ls=1&mt=8"]];
    }
    
    NSArray *arr_ISP_info = [eventStr componentsSeparatedByString:@"ispmobile://TID="];
	
    if( [arr_ISP_info count] == 2 )
    {
        NSArray *arr_ISP_info_2 = [[arr_ISP_info objectAtIndex:1] componentsSeparatedByString:@","];
        
        if([arr_ISP_info_2 count]==2)
        {
            NSURL *appUrl=[NSURL URLWithString:[NSString stringWithFormat:@"ispmobile://TID=%@",[arr_ISP_info objectAtIndex:1]]];
            
            BOOL installedApp=[[UIApplication sharedApplication]openURL:appUrl];
            
            if(installedApp==0)
            {
                [[UIApplication sharedApplication] openURL: [NSURL URLWithString:@"http://itunes.apple.com/kr/app/mobail-anjeongyeolje-isp/id369125087?mt=8"]];
            }
        }
	}
    
    if([eventStr hasPrefix:@"paypin://"])
    {
        BOOL installedApp=[[UIApplication sharedApplication]openURL:[NSURL URLWithString:eventStr]];
        
        if(installedApp==0)
        {
            [[UIApplication sharedApplication] openURL: [NSURL URLWithString:@"http://itunes.apple.com/us/app/paypin-peipin/id490896496?ls=1&mt=8"]];
        }
    }
    
    if([eventStr hasPrefix:@"fb://"])
    {
        BOOL installedApp=[[UIApplication sharedApplication]openURL:[NSURL URLWithString:eventStr]];
        
        if(installedApp==0)
        {
            [[UIApplication sharedApplication] openURL: [NSURL URLWithString:@"https://itunes.apple.com/kr/app/facebook/id284882215?mt=8"]];
        }
    }
    
    if([eventStr hasPrefix:@"storylink://"])
    {
        BOOL installedApp=[[UIApplication sharedApplication]openURL:[NSURL URLWithString:eventStr]];
        
        if(installedApp==0)
        {
            [[UIApplication sharedApplication] openURL: [NSURL URLWithString:@"https://itunes.apple.com/kr/app/kakaoseutoli/id486244601?mt=8"]];
        }
    }
    
    if([eventStr hasPrefix:@"instagram://"])
    {
        BOOL installedApp=[[UIApplication sharedApplication]openURL:[NSURL URLWithString:eventStr]];
        
        if(installedApp==0)
        {
            [[UIApplication sharedApplication] openURL: [NSURL URLWithString:@"https://itunes.apple.com/kr/app/instagram/id389801252?mt=8"]];
        }
    }
    
    if([eventStr rangeOfString:@"/jsp/iPhone_response.jsp"].location != NSNotFound)
    {
        return NO;
    }
    
    if([eventStr rangeOfString:@"card_cookie_result.jsp"].location != NSNotFound)
    {
        return NO;
    }
    
    //2015.11.25 youtube링크 처리
    if([eventStr rangeOfString:@"youtube.com"].location != NSNotFound || [eventStr rangeOfString:@"youtu.be"].location != NSNotFound)
    {
        [[UIApplication sharedApplication]openURL:[NSURL URLWithString:eventStr]];
        return NO;
        
    }
    

    //2014.12.04 버전정보 추가로인해 필요없을듯?
    //2014.01.23 URL 2번 호출로 주석처리
//    if([eventStr rangeOfString:kRootURL].location != NSNotFound) {
//        if (navigationType == UIWebViewNavigationTypeLinkClicked || navigationType == UIWebViewNavigationTypeFormSubmitted) {
//            NSMutableURLRequest *personalRequest = (NSMutableURLRequest *)request;
//            if ([personalRequest respondsToSelector:@selector(setValue:forHTTPHeaderField:)]) {
//                [personalRequest setValue:@"iOS" forHTTPHeaderField:@"makp-device"];
//                [personalRequest setValue:[[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"] forHTTPHeaderField:@"makp-version"];
//                [personalRequest setValue:[[webView.request URL]absoluteString] forHTTPHeaderField: @"Referer"];
//                [webView loadRequest:personalRequest];
////            return NO;
//            }
//        }
//    }
    DLog(@"shouldStartLoadWithRequest :: %@",[[request URL] absoluteString]);
    UWRequest *oReq = [[UWRequest alloc] init];
    
    BOOL isReturn = [oReq commonShouldStartLoadWithRequest:(NSURLRequest*)request withObject:[self.delegate navigationControllerSetting]];
    
    oReq = nil;
    [oReq release];
        
    
    return isReturn;
}

@end
