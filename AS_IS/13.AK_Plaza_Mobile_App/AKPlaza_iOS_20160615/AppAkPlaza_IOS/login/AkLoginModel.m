//
//  AkLoginModel.m
//  AppAkPlaza_IOS
//
//  Created by  on 12. 1. 3..
//  Copyright (c) 2012년 __MyCompanyName__. All rights reserved.
//

#import "AkLoginModel.h"
#import "AkNotiModel.h"
#import "CommonURL.h"


//#import "UWRequest.h"

@implementation AkLoginModel

@synthesize oLogin;

-(void)dealloc
{
    [oLogin release];
    self.oLogin = nil;
    
    [super dealloc];
}

//로그인 
- (void)procLoginResult:(ASIFormDataRequest*) request
{
    
    
   
    
    // Use when fetching text data
    NSString *responseString = [request responseString];
    DLog(@"Got responseString Login INFO: %@", responseString);
    
    NSMutableDictionary *responseJSON = [responseString JSONValue];   
    
    self.oLogin.isLogin      = [[responseJSON objectForKey:@"logined"] boolValue];
    self.oLogin.sessionKey   = [responseJSON objectForKey:@"jsessionid"];
    
    if (self.oLogin.isLogin) 
    {
        //로그인 성공
        //설정 정보 셋팅 하기
        [OLogin saveOLoginContextToUserDefaults:self.oLogin];
        
        //2012.04.08 
        //알림설정 체크 
        AkNotiModel *oNotiModel = [[AkNotiModel alloc] init];
        
        //oNotiModel.delegate = self;
        
        [oNotiModel performSelector:@selector(notiSettingIfLogin) withObject:nil];
        
        [oNotiModel release];
        oNotiModel = nil;
        
        
        //로그인 뷰 pop하기 
        UINavigationController* navi = [self.delegate navigationControllerSetting];
        
        [navi popViewControllerAnimated:YES];
        

         
    } 
    else 
    {
        //로그인 실패
        MSG(nil, @"확인", @"로그인실패 하였습니다");
    }
    
    [self activityStop]; 


}


- (void)procLogin:(OLogin*)objLogin
{
    
    [self activityStart]; 
    
    self.oLogin = [objLogin retain];
    
    
    //운영에서
    NSURL *url = [NSURL URLWithString:[kLibURL stringByReplacingOccurrencesOfString:@"http://" withString:kHTTP]];
    ASIFormDataRequest *request = [ASIFormDataRequest requestWithURL:url];
    
    [request setDelegate:self];
    
    [request setRequestMethod:@"POST"];
    
    [request addRequestHeader:@"User-Agent" value:@"ASIHTTPRequest"];
    
    //applyCookieHeader : 여기서 jsseionid를 알아서 넣어준다 
    
    [request setPostValue:@"login"             forKey:@"act"];
    [request setPostValue:self.oLogin.userID   forKey:@"userid"];
    [request setPostValue:self.oLogin.userPW   forKey:@"passwd"];
    
    [request setDidFinishSelector:@selector(procLoginResult:)];
    
    [request startSynchronous];
    
    if ([request error])
    {
        MSG(nil, @"확인", @"로그인 요청 실패");
        [self activityStop]; 
    }


    
}

//로그아웃
- (void)procLogoutResult:(ASIFormDataRequest*)request
{
    
    
    NSString *responseString = [request responseString];
    DLog(@"Got responseString Login INFO: %@", responseString);
    
    NSMutableDictionary *responseJSON = [responseString JSONValue];   
    
    BOOL isLogout = [[responseJSON objectForKey:@"logout"] boolValue];

    if (isLogout) {
        [OLogin removeOLoginContextToAllUserDefault];
        MSG(nil, @"확인", @"로그아웃 하였습니다");
        

        
    }
    else
    {
        MSG(nil, @"확인", @"로그아웃 실패하셨습니다");
    }
    
    
    [self activityStop]; 
}




- (void)procLogout
{
    
    [self activityStart]; 
    
    NSURL *url = [NSURL URLWithString:kLibURL];
    
    
    ASIFormDataRequest *request = [ASIFormDataRequest requestWithURL:url];
    
    [request setDelegate:self];
    
    [request setRequestMethod:@"POST"];
    
    [request addRequestHeader:@"User-Agent" value:@"ASIHTTPRequest"];
    
    [request setPostValue:@"logout"             forKey:@"act"];
    
    [request setDidFinishSelector:@selector(procLogoutResult:)];
    
    [request startSynchronous];
    
    if ([request error])
    {
        MSG(nil, @"확인", @"로그아웃 요청 실패");
        [self activityStop]; 
    }
    
    
    
}




//자동로그인 처리 
- (void)procAutoLoginResult:(ASIFormDataRequest*)request
{
    NSString *responseString = [request responseString];
    DLog(@"Got responseString Login INFO: %@", responseString);
    
    NSMutableDictionary *responseJSON = [responseString JSONValue];   
    
    self.oLogin.isLogin      = [[responseJSON objectForKey:@"logined"] boolValue];
    self.oLogin.sessionKey   = [responseJSON objectForKey:@"jsessionid"];
    self.oLogin.isAutoLogin  = YES;
    
    
    if (self.oLogin.isLogin) 
    {
        //로그인 성공
        //설정 정보 셋팅 하기
        [OLogin saveOLoginContextToUserDefaults:self.oLogin];
        
    } 
    /*
    else 
    {
        //로그인 실패
        MSG(nil, @"확인", @"자동 로그인실패 하였습니다");
    }
     */
    


}

- (void)procAutoLogin:(OLogin*)objLogin
{
    
    self.oLogin = [objLogin retain];
    
    NSURL *url = [NSURL URLWithString:kLibURL];
    
    
    ASIFormDataRequest *request = [ASIFormDataRequest requestWithURL:url];
    
    [request setDelegate:self];
    
    [request setRequestMethod:@"POST"];
    
    [request addRequestHeader:@"User-Agent" value:@"ASIHTTPRequest"];
    
    [request setPostValue:@"login"             forKey:@"act"];
    [request setPostValue:self.oLogin.userID   forKey:@"userid"];
    [request setPostValue:self.oLogin.userPW   forKey:@"passwd"];
    
    [request setDidFinishSelector:@selector(procAutoLoginResult:)];
    
    [request startSynchronous];
    
    if ([request error])
    {
        //MSG(nil, @"확인", @"자동 로그인 요청 실패");
        [self activityStop]; 
    }
    
    
    
}


@end
