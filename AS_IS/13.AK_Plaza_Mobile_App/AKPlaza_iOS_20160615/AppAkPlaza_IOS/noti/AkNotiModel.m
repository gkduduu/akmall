//
//  AkNotiModel.m
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 28..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import "AkNotiModel.h"
#import "CommonURL.h"
#import "NotiListItems.h"
#import "NotiDeviceRegItem.h"
#import "NotiSettingUpdateItems.h"

@implementation AkNotiModel



- (UIWebView *)notiContentWebview:(NSString *)notiContentURL
{
    UIWebView *webView = [[UIWebView alloc] init];
    
    //NSString *strURL = [[NSString stringWithFormat:@"%@", @"bit.ly/tJdvWX"] stringByReplacingOccurrencesOfString:@"http://" withString:@""];
    
    NSString *strURL = [NSString stringWithFormat:@"%@", notiContentURL];
    
    DLog(@"%@", strURL);
    
    NSURL *url = [NSURL URLWithString:strURL];
    
    NSURLRequest *reqURL = [NSURLRequest requestWithURL:url];
    
    [webView loadRequest:reqURL];
    
    webView.scalesPageToFit = YES;
    
    webView.delegate = self;
    
    //[self activityStart];
    
    return [webView autorelease];
}




- (NSMutableArray *)notiList
{
    [self activityStart];
    
    NSString *strURL = [NSString stringWithFormat:@"%@?act=getAlarmList", kLibURL];
    
    UWXmlParser *xmlParser = [[[UWXmlParser alloc] initParser:[NSURL URLWithString:strURL] andWithItems:NOTILIST_DETAIL_ATTREBUTE andWithParserType:(ParserType *)Parser3] autorelease];

    
    if (xmlParser.parsedItems && [xmlParser.parsedItems count] > 0) {
        return [xmlParser.parsedItems objectAtIndex:0];
    }
    
    
    return nil;
    
}


//디바이스 정보
- (void)procNotiDeviceRegistResult:(ASIFormDataRequest*)request
{
    
    
    NSString *responseString = [request responseString];
    DLog(@"Got responseString Login INFO: %@", responseString);
    
    NSMutableDictionary *responseJSON = [responseString JSONValue];   
    
    NSString *result = [responseJSON objectForKey:@"result"];
    
    if ([result isEqualToString:@"success"]) 
    {
        //MSG(nil, @"확인", @"알림설정 하였습니다");
        DLog(@"알림설정 하였습니다");
        
    }
    else if ([result isEqualToString:@"fail"]) 
    {
        //MSG(nil, @"확인", @"알림설정 실패하셨습니다");
        DLog(@"알림설정 실패하셨습니다");
    }
    
    
    //[self activityStop]; 
}



//디바이스 정보
- (void)notiDeviceRegist:(NSString*)token 
{
    /*
     token : 디바이스 정보 
     phone_type : 0 - iOS, 1 - android
     appid :   빈값- iOS, 안드로이드 유닉한값 - android
     deny :  0 - off, 1 - on  (알림사용 여부)
     */
    
    
    //[self activityStart];
    
    NSURL *url = [NSURL URLWithString:kLibURL];
    
    ///app/lib.do?act=pushDeviceRegist&phonetype=0&token=1&deny=1
    
    ASIFormDataRequest *request = [ASIFormDataRequest requestWithURL:url];
    
    [request setDelegate:self];
    
    [request setRequestMethod:@"POST"];
    
    [request addRequestHeader:@"User-Agent" value:@"ASIHTTPRequest"];
    
    [request setPostValue:@"pushDeviceRegist"             forKey:@"act"];
    [request setPostValue:@"0"             forKey:@"phonetype"];
    [request setPostValue:token             forKey:@"token"];
    [request setPostValue:@"1"             forKey:@"deny"];
    
    [request setDidFinishSelector:@selector(procNotiDeviceRegistResult:)];
    
    [request startSynchronous];
    
    if ([request error])
    {
        MSG(nil, @"확인", @"알림설정 요청 실패");
        //[self activityStop]; 
    }
    
    
}


//알림 설정 
- (void)procNotiSetUpdateResult:(ASIFormDataRequest*)request
{
    
    
    NSString *responseString = [request responseString];
    DLog(@"Got responseString Login INFO: %@", responseString);
    
    NSMutableDictionary *responseJSON = [responseString JSONValue];   
    
    NSString *result = [responseJSON objectForKey:@"result"];
    
    if ([result isEqualToString:@"success"]) 
    {
        MSG(nil, @"확인", @"알림설정 하였습니다");
        
    }
    else if ([result isEqualToString:@"fail"]) 
    {
        MSG(nil, @"확인", @"알림설정 실패하셨습니다");
    }
    
    
    [self activityStop]; 
}


//알림 설정 
- (void)notiSetUpdate:(NSString*)deny
{
    
    
    /*
     token : 디바이스 정보 
     deny :  0 - off, 1 - on   (알림사용 여부)
     
     */
    
    
    [self activityStart];
    
    
    
    
    NSString *deviceToken = [[NSUserDefaults standardUserDefaults] objectForKey:@"deviceToken"];
    
    NSURL *url = [NSURL URLWithString:kLibURL];
    
    ASIFormDataRequest *request = [ASIFormDataRequest requestWithURL:url];
    
    [request setDelegate:self];
    
    [request setRequestMethod:@"POST"];
    
    [request addRequestHeader:@"User-Agent" value:@"ASIHTTPRequest"];
    
    [request setPostValue:@"updateDeny"             forKey:@"act"];
    [request setPostValue:deviceToken             forKey:@"token"];
    [request setPostValue:deny             forKey:@"deny"];
    
    [request setDidFinishSelector:@selector(procNotiSetUpdateResult:)];
    
    [request startSynchronous];
    
    if ([request error])
    {
        MSG(nil, @"확인", @"알림설정 요청 실패");
        [self activityStop]; 
    }

    
}







//PUSH 거부상태 확인

 
- (void)procNotiSetStateResult:(ASIFormDataRequest*)request
{
    
    
    NSString *responseString = [request responseString];
    DLog(@"Got responseString Login INFO: %@", responseString);
    
    NSMutableDictionary *responseJSON = [responseString JSONValue];   
    
    NSString *result = [responseJSON objectForKey:@"DENY"];
    
    if ([result isEqualToString:@"1"]) 
    {
        //알림 받음
        [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"PUSHSET"];
        
        DLog(@"PUSH 거부상태 확인 - 알림 받음");
        
    }
    else if ([result isEqualToString:@"0"]) 
    {
        //알림 거부 
        [[NSUserDefaults standardUserDefaults] setBool:NO forKey:@"PUSHSET"];
        DLog(@"PUSH 거부상태 확인 - 알림 거부");
    }
    
    
    [self activityStop]; 
}


//PUSH 거부상태 확인

- (void)notiSetState
{
    
    
    /*
     token : 디바이스 정보 
     deny :  0 - off, 1 - on   (알림사용 여부)
     
     */
    
    
    [self activityStart];
    
    
    
    
    NSString *deviceToken = [[NSUserDefaults standardUserDefaults] objectForKey:@"deviceToken"];
    
    NSURL *url = [NSURL URLWithString:kLibURL];
    
    ASIFormDataRequest *request = [ASIFormDataRequest requestWithURL:url];
    
    [request setDelegate:self];
    
    [request setRequestMethod:@"POST"];
    
    [request addRequestHeader:@"User-Agent" value:@"ASIHTTPRequest"];
    
    [request setPostValue:@"getDenyState"             forKey:@"act"];
    [request setPostValue:deviceToken             forKey:@"token"];
    
    [request setDidFinishSelector:@selector(procNotiSetStateResult:)];
    
    [request startSynchronous];
    
    if ([request error])
    {
        MSG(nil, @"확인", @"알림설정 요청 실패");
        [self activityStop]; 
    }
    
    
}














//알림설정했나? 했다면 아이디 업데이트 
/*
 
 1. 토큰이 기준
 2. 아이디 한개당 토큰 여러개 가능 
 3. 토큰 중복 허용 안함
 4. 알림설정을 했을 경우에는 토큰이 존재하므로 아이디 업데이트해줌(항상) 
 
 
 * 알림설정 켰을떄  
 로그인 상태면, 토큰값 넘겨줄때 서버단에서 아이디 업데이트 해줌
 
 */

- (void)procNotiSettingIfLoginResult:(ASIFormDataRequest*)request
{
    
    
    NSString *responseString = [request responseString];
    DLog(@"Got responseString Login INFO: %@", responseString);
    
    NSMutableDictionary *responseJSON = [responseString JSONValue];   
    
    NSString *result = [responseJSON objectForKey:@"result"];
    
    if ([result isEqualToString:@"success"]) 
    {
        DLog("로그인 했을때 토큰 체크 성공");
    }
    else if ([result isEqualToString:@"fail"]) 
    {
        DLog("로그인 했을때 토큰 체크 실패");
    }
    
    
    [self activityStop]; 
}




- (void)notiSettingIfLogin
{
    
    
    /*
     token : 디바이스 정보 
     deny :  0 - off, 1 - on   (알림사용 여부)
     
     */
    
    
    [self activityStart];
    
    
    
    
    NSString *deviceToken = [[NSUserDefaults standardUserDefaults] objectForKey:@"deviceToken"];
    
    NSURL *url = [NSURL URLWithString:kLibURL];
    
    ASIFormDataRequest *request = [ASIFormDataRequest requestWithURL:url];
    
    [request setDelegate:self];
    
    [request setRequestMethod:@"POST"];
    
    [request addRequestHeader:@"User-Agent" value:@"ASIHTTPRequest"];
    
    [request setPostValue:@"updateDeviceTokenUserid"             forKey:@"act"];
    [request setPostValue:deviceToken             forKey:@"token"];
    
    [request setDidFinishSelector:@selector(procNotiSettingIfLoginResult:)];
    
    [request startSynchronous];
    
    if ([request error])
    {
        MSG(nil, @"확인", @"알림설정 요청 실패");
        [self activityStop]; 
    }
    
    
}











/*
 
 1. 페이지 로딩 전이나 후에 쿠키에서 sdvs의 값이 true인지 확인한다.
 
 2. sdvs가 true가 아니면 서버에 device token을 전송하여 device signin 을 한다.
 
 device signin addr: /app/lib.do/act=signinDevice&token=[token]
 
 */

- (void)procSigninDevice:(ASIFormDataRequest*)request
{
    
    
    NSString *responseString = [request responseString];
    DLog(@"Got responseString Login INFO: %@", responseString);
    
    NSMutableDictionary *responseJSON = [responseString JSONValue];   
    
    NSString *result = [responseJSON objectForKey:@"result"];
    
    if ([result isEqualToString:@"success"]) 
    {
        DLog("sdvs 성공");
    }
    else if ([result isEqualToString:@"fail"]) 
    {
        DLog("sdvs 실패");
    }
    
    
}




- (void)signinDevice
{   
    NSString *deviceToken = [[NSUserDefaults standardUserDefaults] objectForKey:@"deviceToken"];
    
    NSURL *url = [NSURL URLWithString:kLibURL];
    
    ASIFormDataRequest *request = [ASIFormDataRequest requestWithURL:url];
    
    [request setDelegate:self];
    
    [request setRequestMethod:@"POST"];
    
    [request addRequestHeader:@"User-Agent" value:@"ASIHTTPRequest"];
    
    [request setPostValue:@"signinDevice" forKey:@"act"];
    [request setPostValue:deviceToken forKey:@"token"];
    
    // 로그인시 버전 정보 전송
    [request setPostValue:[[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"] forKey:@"version"];
    DLog(@"전송 내용 : %@",request);
    
    [request setDidFinishSelector:@selector(procSigninDevice:)];
    
    [request startSynchronous];
    
    if ([request error])
    {
        //changuk 2013.08.26 사용자가 알기 쉬운 메세지로 변경
//        MSG(nil, @"확인", @"sdvs 설정 요청 실패");
        MSG(nil, @"확인", @"네트워크 연결상태가 불안정 합니다");
    }
    
    
}











@end
