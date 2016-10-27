//
//  UWRequest.m
//  AppAkMall_iOS
//
//  Created by uniwis on 11. 8. 24..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "UWRequest.h"


#import "AkLoginWithNotiSettingView.h"
#import "AkNotiListBoxViewController.h"
#import "AkReceiptBarcodeRegView.h"

#import "AkBarcodeDetailView.h"
#import "PhotoEventViewControllerViewController.h"





#define kLogin          @"Login"
#define kSessionId      @"SessionId"

/*
@interface NSMutableURLRequest (DummyInterface)
+(BOOL)allowsAnyHTTPSCertificateForHost:(NSString *)host;
+(void)setAllowsAnyHTTPSCertificate:(BOOL)allow forHost:(NSString *)host;
@end
*/

@implementation UWRequest

+ (NSString *)createQueryString:(NSDictionary *)dictParam
{
    NSMutableString *resultString = [NSMutableString string];
    for (NSString* key in [dictParam allKeys]){
        if ([resultString length]>0)
            [resultString appendString:@"&"];
        [resultString appendFormat:@"%@=%@", key, [dictParam objectForKey:key]];
    }
    
    return resultString;
}

+ (NSMutableURLRequest *)urlRequestParam:(NSString *)queryString andWithURL:(NSString *)strURL
{
    NSURL *URL = [NSURL URLWithString:strURL];
    
    //[NSMutableURLRequest setAllowsAnyHTTPSCertificate:YES forHost:[URL host]];
    
    NSData *queryData = [NSData dataWithBytes:[queryString UTF8String] length:[queryString length]];
    
    NSMutableURLRequest *urlRequest = [[NSMutableURLRequest alloc] initWithURL:URL cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval:60.0];
    
    NSString *contentLength = [NSString stringWithFormat:@"%d", [queryData length]];
    
    
    [urlRequest addValue:@"application/x-www-form-urlencoded; charset=utf-8" forHTTPHeaderField:@"Content-Type"];
    [urlRequest addValue:contentLength forHTTPHeaderField:@"Content-Length"];
    
    //sessionID ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    NSMutableArray *items = [[NSMutableArray alloc] initWithArray:[[NSUserDefaults standardUserDefaults] objectForKey:@"LoginInfo"]];
    
    NSString *strSessionId = nil;
    NSString *jSessionID = nil;
    
    if ([items count] > 0 ) {
        //strSessionId = [[items objectAtIndex:0] objectForKey:kSessionId];
        
        jSessionID = [NSString stringWithFormat:@"JSESSIONID=%@",strSessionId];
        
        [urlRequest addValue:jSessionID forHTTPHeaderField:@"cookie"];
        [urlRequest setHTTPShouldHandleCookies:YES];
    }
    
//    DLog(@"url : %@", strURL);
//    DLog(@"queryData : %@", [[[NSString alloc] initWithData:queryData encoding:NSUTF8StringEncoding] autorelease]);
    [urlRequest setHTTPMethod:@"POST"];
    [urlRequest setHTTPBody:queryData];
    
    [items release];
    
    return [urlRequest autorelease];
}


- (BOOL) isAppLogin:(NSMutableString *)str withObject:(UINavigationController *)oNavigation
{
    //로그인,알림설정
    NSRange strRangeAppLogin = [str rangeOfString:@"act=applogin"];
    
    if (strRangeAppLogin.location != NSNotFound) {
        AkLoginWithNotiSettingView *subView = [[AkLoginWithNotiSettingView alloc] init];
        [oNavigation pushViewController:subView animated:YES];
        subView = nil;
        return NO;
    }
    return YES;
}



- (BOOL) isAppAlimList:(NSMutableString *)str withObject:(UINavigationController *)oNavigation
{
    //알림보관함
    NSRange strRangeAppAlimList = [str rangeOfString:@"act=appAlimList"];
    
    if (strRangeAppAlimList.location != NSNotFound) {
        
        AkNotiListBoxViewController *subView = [[AkNotiListBoxViewController alloc] init];
        
        [oNavigation pushViewController:subView animated:YES];
        
        subView = nil;
        
        return NO;
    }
    
    return YES;
}



- (BOOL) isAppReceiptReg:(NSMutableString *)str withObject:(UINavigationController *)oNavigation
{
    //영수증등록
    NSRange strRangeAppReceiptReg = [str rangeOfString:@"act=appleReceiptReg"];
    
    
    if (strRangeAppReceiptReg.location != NSNotFound) {
        
        AkReceiptBarcodeRegView *subView = [[AkReceiptBarcodeRegView alloc] init]; 
        
        [oNavigation pushViewController:subView animated:YES];
        
        subView = nil;
        
        
        [[GlobalValues sharedSingleton] setActUrl:@"act=appleReceiptReg"];//p65458 20130807 영수증 등록후 이전 페이지로 돌아갈 때 메인페이지로 돌아 가지 않도록 변경
        
        return NO;
    }
    
    return YES;
}



- (BOOL) isAppQRCode:(NSMutableString *)str withObject:(UINavigationController *)oNavigation
{
    //QR코드 
    NSRange strRangeAppReceiptReg = [str rangeOfString:@"act=appQRCode"];
    
    
    if (strRangeAppReceiptReg.location != NSNotFound) {
        
        AkBarcodeDetailView * subView = [[AkBarcodeDetailView alloc ] init ];
        
        [oNavigation pushViewController:subView animated:YES];
        
        subView = nil;
        


        return NO;
    }

    
    return YES;
}

- (BOOL) isPhotoEvent:(NSMutableString *)str withObject:(UINavigationController *)oNavigation withUserID:(NSString*)userID
{
    //포토이벤트 처리 
    NSRange strRangeAppReceiptReg = [str rangeOfString:@"act=viewEventJoinForm"];
    
    if (strRangeAppReceiptReg.location != NSNotFound) {
        PhotoEventViewControllerViewController *subView = [[PhotoEventViewControllerViewController alloc]initWithNibName:@"PhotoEventViewControllerViewController" bundle:nil];
        NSMutableDictionary *params = [[NSMutableDictionary alloc] init];
        for (NSString *param in [[[str componentsSeparatedByString:@"?"] objectAtIndex:1] componentsSeparatedByString:@"&"]) {
            NSArray *elts = [param componentsSeparatedByString:@"="];
            if([elts count] < 2) continue;
            [params setObject:[elts objectAtIndex:1] forKey:[elts objectAtIndex:0]];
        }
        
        [subView setParamTokensUserID:userID 
                                  act:@"joinEvent"
                                 type:[params objectForKey:@"type_cd"] 
                          event_index:[params objectForKey:@"event_index"] 
                          event_token:[params objectForKey:@"event_token"]
                                 name:[params objectForKey:@"name"]
                                phone:[params objectForKey:@"phone"]];
        
        [oNavigation pushViewController:subView animated:YES];
        subView = nil;
        [subView release];
        params = nil;
        [params release];
        return NO;
    }
    return YES;
}

- (BOOL) isPhotoEventModify:(NSMutableString *)str withObject:(UINavigationController *)oNavigation withUserID:(NSString*)userID
{
    //포토이벤트 수정 처리 
    NSRange strRangeAppReceiptReg = [str rangeOfString:@"act=updateEntryForm"];
    
    if (strRangeAppReceiptReg.location != NSNotFound) {
        PhotoEventViewControllerViewController *subView = [[PhotoEventViewControllerViewController alloc]initWithNibName:@"PhotoEventViewControllerViewController" bundle:nil];
        NSMutableDictionary *params = [[NSMutableDictionary alloc] init];
        for (NSString *param in [[[str componentsSeparatedByString:@"?"] objectAtIndex:1] componentsSeparatedByString:@"&"]) {
            NSArray *elts = [param componentsSeparatedByString:@"="];
            if([elts count] < 2) continue;
            [params setObject:[elts objectAtIndex:1] forKey:[elts objectAtIndex:0]];
        }
        
        [subView setParamTokensForUpdateUserID:userID 
                                  act:@"updateEntry"
                                 type:[params objectForKey:@"type_cd"] 
                          event_index:[params objectForKey:@"event_index"] 
                          entry_indexno:[params objectForKey:@"entry_indexno"]
                                name:[params objectForKey:@"name"]
                                phone:[params objectForKey:@"phone"]];
        
        [oNavigation pushViewController:subView animated:YES];
        subView = nil;
        [subView release];
        params = nil;
        [params release];
        return NO;
    }
    return YES;
}



-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    DLog(@"alertView.title");
    
    if ([alertView.title isEqualToString:kAkmallMsg]) 
    {
        /*
        if (buttonIndex == 1) {
            // openURL 로 링크 실행
            NSString *strURL = [NSString stringWithFormat:@"%@?act=akmall", kAddressURL];
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:strURL]];  
        }
         */
    }
    else if ([alertView.title isEqualToString:kMapMsg])
    {
        //맵 
        if (buttonIndex == 1) {
            
            NSString* addressText = nil;
            
            NSString *bc = [NSString stringWithFormat:@"0%d", alertView.tag]; 
            
            DLog(@"bc : %@", bc);
            DLog(@"bc_tag : %d", alertView.tag);
            
            addressText = @"서울시 구로구 구로 5 -73+AK플라자 구로본점";
            
            if ([bc isEqualToString:@"01"])
            {
                addressText = @"서울시 구로구 구로 5 -73+AK플라자 구로본점";
            }
            else if ([bc isEqualToString:@"02"])
            {
                addressText = @"수원시 팔달구 매산로 1 18+AK플라자 수원점";
            }
            else if ([bc isEqualToString:@"03"]) 
            {
                addressText = @"경기도 성남시 분당구 서현동 263+(AK플라자 분당점)";
            }
            else if ([bc isEqualToString:@"04"])
            {
                addressText = @"경기도 평택시 평택 185 -568+(AK플라자 평택점)";
            }
            else if ([bc isEqualToString:@"05"])
            {
                addressText = @"강원도 원주시 단계동 1123+(AK플라자 원주점)";
            }  
            
            
            // 특수 문자 처리를 위해 인코딩을 합니다.
            NSString* searchQuery = [addressText stringByAddingPercentEscapesUsingEncoding:
                                     NSUTF8StringEncoding];
            
            // URL 문자열을 생성합니다.
            NSString* urlString = [NSString stringWithFormat:
                                   @"http://maps.google.com/maps?q=%@",
                                   searchQuery];
            // 실행
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:urlString]];
            
            addressText = nil;
            bc = nil;
            urlString = nil;
            searchQuery = nil;
            
            
        }
    }
    
    alertView = nil;
}


- (BOOL) isAkmall:(NSMutableString *)str withObject:(UINavigationController *)oNavigation
{
    
    //akmall
    NSRange strRangeAkmallWeb = [str rangeOfString:@"m.akmall.com"]; 

    if (strRangeAkmallWeb.location != NSNotFound) {
        
        BOOL isQRCode = [[NSUserDefaults standardUserDefaults] boolForKey: @"isQRCode"];
        
        if (isQRCode) 
        {
           
            //웹뷰에 보여준다 
            return YES;

        }
        else
        {
            // openURL 로 링크 실행
//            NSString *strURL = [NSString stringWithFormat:@"%@?act=akmall", kAddressURL];
//            NSLog(@"strURL %@ : %@",str,strURL);
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:str]];
        }
        
        
        //MSG_DELEGATE_BTN2(kAkmallMsg, @"취소", @"확인", nil, self);
        
        //NSString *pStr = [[NSString stringWithString:@"http://m.akmall.com"] stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        
        
        
        //탭바의 akmall메뉴는 uwtabbarview.m으로 가서 수정하면 된다 
                
        return NO;
    }
    
    
    return YES;
}




- (BOOL) isGoogleMap:(NSURLRequest *)request withObject:(UINavigationController *)oNavigation
{
    
    NSString *req = [[request URL] absoluteString];    
    
    NSArray *arr = [req componentsSeparatedByString:@"?"];
    
    
    
    //pc버전
    NSMutableString *str = [NSMutableString stringWithString:req];
    
    
    
    //구글맵
    NSRange strRangeGooglMap = [str rangeOfString:@"act=location"];
    
    if (strRangeGooglMap.location != NSNotFound)
    {
        NSString* bc = @"";
        NSString* isAppMapp = @"N";
        
        
        arr = [arr objectAtIndex:1];
        
        NSArray *paramArr = [req componentsSeparatedByString:@"&"];
        
        //NSDictionary *dicArr = [[NSDictionary alloc] init];
        
        for (NSString* strParam in paramArr) {
            
            NSArray *paramSubArr = [strParam componentsSeparatedByString:@"="];
            
            if ([[paramSubArr objectAtIndex:0] isEqualToString:@"isAppMap"]) {
                isAppMapp = [paramSubArr objectAtIndex:1];
            }
            
            
            if ([[paramSubArr objectAtIndex:0] isEqualToString:@"bc"]) {
                
                bc = [paramSubArr objectAtIndex:1];     
                
            }
        }
        
        // 쿼리문 생성
        
        if ([isAppMapp isEqualToString:@"Y"])
        {
            //MSG_DELEGATE_BTN2(@"aa", @"취소", @"확인", kMapMsg, self);
            
            UIAlertView *mapAlert = [[[UIAlertView alloc] initWithTitle:kMapMsg message:nil delegate:self cancelButtonTitle:@"취소" otherButtonTitles:@"확인", nil] autorelease] ;
            
            //[[UIAlertView alloc] initWithTitle:@"" message:kMapMsg delegate:self cancelButtonTitle:@"취소" otherButtonTitles:@"확인", nil]  ;            
            
             
            if ([bc isEqualToString:@"01"])
            {
                mapAlert.tag = 1;
            }
            else if ([bc isEqualToString:@"02"])
            {
                mapAlert.tag = 2;
            }
            else if ([bc isEqualToString:@"03"]) 
            {
                mapAlert.tag = 3;
            }
            else if ([bc isEqualToString:@"04"])
            {
                mapAlert.tag = 4;
            }
            else if ([bc isEqualToString:@"05"])
            {
                mapAlert.tag = 5;
            }  
            
            
            [mapAlert show];
             
            
            
            //MSG_DELEGATE_BTN2(kMap, @"취소", @"확인", bc, self);
 
            
            return NO;
            
        }
    }
    
    return YES;
    
}







- (BOOL) commonShouldStartLoadWithRequest:(NSURLRequest *)request withObject:(UINavigationController *)oNavigation
{
    DLog(@"commonShouldStartLoadWithRequest");
    
    BOOL isLogin = NO;
    BOOL isSessionID = NO;
    
    OLogin *oLogin = [OLogin loadOLoginContextFromUserDefaults];
    
    //로그인 체크
    NSHTTPCookie *cookie = nil;
    NSArray* arrCookies = nil;
    arrCookies = [[NSHTTPCookieStorage sharedHTTPCookieStorage] cookies];
    
    //Login 쿠키값을 체크한다.
    for (cookie in arrCookies)
    {
        if ([[cookie name] isEqualToString:@"logined"]) {
            if ([[cookie value] isEqualToString:@"true"]) {
                isLogin = YES;
                oLogin.isLogin = isLogin;
            }
             DLog(@"Login cookie : %d", oLogin.isLogin);
        }
    }
    
    //Login 쿠키값이 존재하면, JSESSIONID 쿠키값을 체크한다. 
    if (isLogin) {
        for (cookie in arrCookies)
        {
            if ([[cookie name] isEqualToString:@"JSESSIONID"]) {
                isSessionID = YES;
                oLogin.sessionKey = [cookie value];
                DLog(@"JSESSIONID : %@", oLogin.sessionKey);
            }
        }
        
        for (cookie in arrCookies)
        {
            if ([[cookie name] isEqualToString:@"userid"]) {
                storedUserID = oLogin.userID = [cookie value];
                DLog(@"userid : %@", oLogin.userID);
            }
        }
    }
    
    //2012.04.19
    //sdvc 쿠키값을 체크한다.
    for (cookie in arrCookies)
    {
        if ([[cookie name] isEqualToString:@"sdvc"]) {
            DLog(@"[cookie value] :: %@",[cookie value]);
            if (![[cookie value] isEqualToString:@"true"]) {
                /*
                 1. 페이지 로딩 전이나 후에 쿠키에서 sdvs의 값이 true인지 확인한다.
                 2. sdvs가 true가 아니면 서버에 device token을 전송하여 device signin 을 한다.
                 device signin addr: /app/lib.do/act=signinDevice&token=[token]
                */
                //세션에 디바이스 토큰 셋팅
                AkNotiModel *oNotiModel = [[AkNotiModel alloc] init];
//                [oNotiModel signinDevice];
                [oNotiModel performSelector:@selector(signinDevice) withObject:nil];
                [oNotiModel release];
                
                oNotiModel = nil;
                
            }
            DLog(@"sdvc cookie : %@", [cookie value]);
        }
    }
    
    
    arrCookies = nil;
    cookie = nil;
    
    //Login 쿠키값이 존재하면, JSESSIONID 쿠키값이 존재하면 마이정보를 가져온다. 
    if (isLogin && isSessionID) {
        DLog(@"isLogin && isSessionID YES");
        [OLogin saveOLoginContextToUserDefaults:oLogin];
        oLogin = nil;
    }
    else 
    {
        DLog(@"isLogin && isSessionID NO");
        OLogin *doLogin = [OLogin loadOLoginContextFromUserDefaults];
        
        //자동로그인 상태인지 확인한다. 
        if (doLogin.isAutoLogin) {
            DLog(@"##login proc start ################################");
            
            //자동 로그인을 하기전에..
            //로그인정보 지워주기.. 세션때문에.. 로그인 할때 Jsessionid를 넣으면.. 문제가 있으니깐.. 
            [OLogin removeOLoginContextToSessionValue];
            
            AkLoginModel *oModel = [[AkLoginModel alloc] init];
            
            [oModel performSelectorOnMainThread:@selector(procAutoLogin:) withObject:doLogin waitUntilDone:YES];

            [oModel release];
            
            oModel = nil;
            doLogin = nil;
                           
            OLogin *autoLogin = [OLogin loadOLoginContextFromUserDefaults];               
                           
            
            //자동로그인이 실패하면 
            if (!autoLogin.isLogin) {
                isLogin = NO;
                isSessionID = NO;
                
                //Login 쿠키값이 존재안하거나 JSESSIONID 쿠키값이 존재안하면
                //자동로그인 상태값을 제거한다. 어플의 로그인 정보도 제거 한다. 
                
                MSG( AUTO_LOGIN_TITLE,  @"확인", AUTO_LOGIN_MSG);
            }
            else
            {
                isLogin = YES;
                isSessionID = YES;            
            }
            
            autoLogin = nil;
            
            
            DLog(@"##login proc end ################################");
        }
          
    }
    
    
    if (!isLogin || !isSessionID) 
    {
        //로그인 실패거나 안했을때 
        
        [OLogin removeOLoginContextToAllUserDefault];
        
        //알림설정 아이디 체크 하지 않기 
        [[NSUserDefaults standardUserDefaults] setBool:NO forKey:@"NotiSettingCK"];
        

    }
    else
    {
        //로그인 성공
        
        BOOL isNotiSettingCK = [[NSUserDefaults standardUserDefaults] boolForKey:@"NotiSettingCK"];
        
        if (!isNotiSettingCK) 
        {
            //2012.04.08 
            //알림설정 체크 
            AkNotiModel *oNotiModel = [[AkNotiModel alloc] init];
            
            //oNotiModel.delegate = self;
            
            [oNotiModel performSelector:@selector(notiSettingIfLogin) withObject:nil];
            
            [oNotiModel release];
            oNotiModel = nil;
            
            [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"NotiSettingCK"];
        }

    }


	
	//파람값을 가져와야 하는데 없음.
	NSString *req = [[request URL] absoluteString];    
    
    //NSString *reqQurey = [[request URL] query];
    
    DLog(@"req url : %@", req); 
    
    DLog(@"reqQurey url : %@", [[request URL] query]);

    //p65458 20130807 영수증 등록후 이전 페이지로 돌아갈 때 메인페이지로 돌아 가지 않도록 변경
    if([[GlobalValues sharedSingleton] actUrl] == nil){
        
    }else{

        [[GlobalValues sharedSingleton] setActUrl:nil];
        
        return NO;
    }

    //p65458 20130807 영수증 등록후 이전 페이지로 돌아갈 때 메인페이지로 돌아 가지 않도록 변경
    
    //pc버전
    NSMutableString *mutaOrgPlazaWeb = [NSMutableString stringWithString:req];
    
    BOOL isLoading = NO;

    isLoading = [self isAppLogin:mutaOrgPlazaWeb withObject:oNavigation];    
    
    if (isLoading) {
        isLoading = [self isAppAlimList:mutaOrgPlazaWeb withObject:oNavigation];
        
        if (isLoading) {
            
            isLoading = [self isAppReceiptReg:mutaOrgPlazaWeb withObject:oNavigation];
            
            if (isLoading) {
                isLoading = [self isAppQRCode:mutaOrgPlazaWeb withObject:oNavigation];
                
                if (isLoading) {
                    isLoading = [self isGoogleMap:request withObject:oNavigation];
                    
                    if (isLoading) {
                        isLoading = [self isAkmall:mutaOrgPlazaWeb withObject:oNavigation];
                        
                        if(isLoading){
                            isLoading = [self isPhotoEvent:mutaOrgPlazaWeb withObject:oNavigation withUserID:storedUserID]; 
                            
                            if(isLoading){
                                isLoading = [self isPhotoEventModify:mutaOrgPlazaWeb withObject:oNavigation withUserID:storedUserID]; 
                                
                            }
                        }
                    }
                }
            }
        }
    }
    
    
    //회원가입 선택시 새창에서 뜨게 하기 
    NSMutableString *membersApp = [NSMutableString stringWithString:req];
    
    NSRange strRangemembers = [membersApp rangeOfString:kMembersRootURL];
    
    
    if (strRangemembers.location != NSNotFound) 
    {
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@&isAkApp=iPhone&appVersion=%@", req, [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"]]]]; 
        
        isLoading =  NO;
    }
    
    
    //ituns url 선택시 새창에서 뜨게 하기 
    NSMutableString *itunesApp = [NSMutableString stringWithString:req];
    
    NSRange strRangeItunes = [itunesApp rangeOfString:@"itunes.apple.com"];
    
    
    if (strRangeItunes.location != NSNotFound) 
    {
        BOOL isOpen = [[UIApplication sharedApplication] openURL:[NSURL URLWithString:req]];  
        
        
        if (!isOpen) {
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:req]];
        }
        
        
        
        
        isLoading =  NO;
    }
    
    
	
	return isLoading;
}

@end
