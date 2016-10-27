        //
//  AKAppDelegate.m
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 21..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import <AudioToolbox/AudioToolbox.h>


#import "AKAppDelegate.h"
#import "GlobalValues.h"
#import "UWTabBarDefine.h"
#import "AkNotiModel.h"
#import "AkMainModel.h"

#import "AkNotiListBoxViewController.h"

#import "LoginViewController.h"

#import "NotiListItems.h"//p65458 20130620 add
#import "AkMainView.h"//p65458 20130711 add

//changuk 16.08.09 IGAWorks 추가
#import <IgaworksCore/IgaworksCore.h>
#import <AdSupport/AdSupport.h>

// changuk 2013.08.26 add 아이폰5 대응 이미지 매크로
#define ASSET_BY_SCREEN_HEIGHT(regular, longScreen) ((    [[UIScreen mainScreen] bounds].size.height) == 480 ? regular:longScreen)

@implementation AKAppDelegate

@synthesize window = _window;
@synthesize tabView;
@synthesize activityImageView;
@synthesize managedObjectContext = __managedObjectContext;
@synthesize managedObjectModel = __managedObjectModel;
@synthesize persistentStoreCoordinator = __persistentStoreCoordinator;

@synthesize baseURL;
@synthesize arrNotilist, nPageNum,pushid,bc;//p65458 20130620 add

SystemSoundID soundID; 


- (void)dealloc
{
    [_window release];
    [__managedObjectContext release];
    [__managedObjectModel release];
    [__persistentStoreCoordinator release];
    
    
    if(soundID) {
        AudioServicesDisposeSystemSoundID (soundID);
        CFRelease (baseURL);
        
    }
    
    [activityImageView removeFromSuperview];
    
    [super dealloc];
}


- (void)loginProc
{   
//    DLog(@"%@",[NSString stringWithFormat:@"Version %@ (%@)", , [[UIDevice currentDevice] systemVersion]]);

    
    
    OLogin *oLogin = [OLogin loadOLoginContextFromUserDefaults];
    OLogin *oAutoLogin = nil;
    
    //로그인 
    if (oLogin.isAutoLogin) {
        DLog(@"##login proc start ################################");
        
        //자동 로그인을 하기전에..
        //세션정보 지워주기.. 세션때문에.. 로그인 할때 Jsessionid를 넣으면.. 문제가 있으니깐.. 
        [OLogin removeOLoginContextToSessionValue];
        
        AkLoginModel* oModel = [[AkLoginModel alloc] init];
        
        [oModel performSelectorOnMainThread:@selector(procAutoLogin:) withObject:oLogin waitUntilDone:YES];
        
        oLogin = nil;
        
        oAutoLogin = [OLogin loadOLoginContextFromUserDefaults];
        
        if (!oAutoLogin.isLogin) 
        {
            //ios5일때 인트로에서 비활성화된채로 가만있다가 뷰다 가 뜨고 나서 활성화되는 문제 때문에 구분한다.
//            if([[[[UIDevice currentDevice] systemVersion] substringToIndex:1] isEqualToString:@"5"])
            if([[GlobalValues sharedSingleton] majorIOSVersion] > 4)
            {
                //자동로그인실패 
                MSG_DELEGATE( AUTO_LOGIN_TITLE,  @"확인", AUTO_LOGIN_MSG, self);
            }
            else
            {
                //자동로그인실패 
                MSG_DELEGATE( AUTO_LOGIN_TITLE,  @"확인", AUTO_LOGIN_MSG, self);
//                MSG( AUTO_LOGIN_TITLE,  @"확인", AUTO_LOGIN_MSG);
            }
        }
        
        
        [oModel release];
        
        oModel = nil;
        
        DLog(@"##login proc end ################################");
    }
    
    //자동로그인이 실패나거나 아닐경우에도 어플이 가지고 있는 값 remove하기 
    if (oAutoLogin == nil || !oAutoLogin.isAutoLogin) {
        
        [OLogin removeOLoginContextToAllUserDefault];
    }
    
    oAutoLogin = nil;

}
- (void)mainView
{
    //인터넷 종류, 연결 여부 확인. 
    //2011.7.18 /////////////////////////////////////////////////////////////////////////
    //3g, wifi check
    int result = [[Reachability reachabilityForInternetConnection] currentReachabilityStatus];
    
    //0:접속불능, 1:와이파이 2: 쓰리지 
    if (result == 0) {
        //ios5일때 인트로에서 비활성화된채로 가만있다가 뷰다 가 뜨고 나서 활성화되는 문제 때문에 구분한다.
//        if([[[[UIDevice currentDevice] systemVersion] substringToIndex:1] isEqualToString:@"5"])
        if([[GlobalValues sharedSingleton] majorIOSVersion] > 4)
            MSG_DELEGATE( NETWORK_ERROR_TITLE,  @"확인", NETWORK_ERROR_MSG, self);
        else
            MSG_DELEGATE( NETWORK_ERROR_TITLE,  @"확인", NETWORK_ERROR_MSG, self);
//            MSG( NETWORK_ERROR_TITLE,  @"확인", NETWORK_ERROR_MSG);
    }
    
    //인터넷이 되면...
    if (result != 0) {
        
        [self loginProc];
        
        //3g일떄 
        if (result == 2)
        {
            //ios5일때 인트로에서 비활성화된채로 가만있다가 뷰다 가 뜨고 나서 활성화되는 문제 때문에 구분한다.
//            if([[[[UIDevice currentDevice] systemVersion] substringToIndex:1] isEqualToString:@"5"])
            if([[GlobalValues sharedSingleton] majorIOSVersion] > 4)
                //3g면 ... 안내 메세지 뿌리기
                MSG_DELEGATE( DATA_3G_TITLE_MSG,  @"확인" ,  DATA_3G_MSG, self);
            else
                MSG_DELEGATE( DATA_3G_TITLE_MSG,  @"확인" ,  DATA_3G_MSG, self);
//                MSG( DATA_3G_TITLE_MSG,  @"확인" ,  DATA_3G_MSG);
        }

        //wifi일떄 
        if (result == 1) {
            //화면 뿌리기 (xml, ui)
            tabView = [[UWTabBarView alloc] init];

//            [_window addSubview:tabView.view];
            [_window setRootViewController:tabView];
        }
        else
        {
            //ios4, //3g면 ...
//            if(![[[[UIDevice currentDevice] systemVersion] substringToIndex:1] isEqualToString:@"5"])
            if([[GlobalValues sharedSingleton] majorIOSVersion] <= 4)
            {
                //화면 뿌리기 (xml, ui)
                tabView = [[UWTabBarView alloc] init];
//                [_window addSubview:tabView.view];
                [_window setRootViewController:tabView];
            }
        }
    }
    
    //p65458 test
#ifdef p65458
    UIAlertView *confirmDiag = [[UIAlertView alloc] initWithTitle:@"AK플라자" message:@"TEST!!!!" delegate:self cancelButtonTitle:@"아니오"otherButtonTitles:@"예", nil];
    
    [confirmDiag show];
    
    [confirmDiag autorelease];
        //p65458 test
#endif
    /*
    tabView = [[UWTabBarView alloc] init];
    
    [self.window addSubview:tabView.view];
    */
}



-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    //ios5일때 인트로에서 알럿이 비활성화되는 바람에 추가된 코드  
    if ([alertView.title isEqualToString:AUTO_LOGIN_TITLE]) {
        DLog(AUTO_LOGIN_TITLE);
        [OLogin removeOLoginContextToAllUserDefault];
    }
    else if ([alertView.title isEqualToString:NETWORK_ERROR_TITLE]) {
        DLog(NETWORK_ERROR_TITLE);
        exit(0);
    }
    else if ([alertView.title isEqualToString:DATA_3G_TITLE_MSG]) 
    {
        DLog(DATA_3G_TITLE_MSG);
        //화면 뿌리기 (xml, ui)
        /*
        tabView = [[UWTabBarView alloc] init];
        [_window addSubview:tabView.view];
         */
    } 
    else
    {
        //Push MSG
        if (buttonIndex == 1) {
            //p65458 20130620 add
            
            //푸시 서비스 할때 배지값을 0으로 초기화한다.
            [[UIApplication sharedApplication] setApplicationIconBadgeNumber:0];
            
            
            self.pushid = [[NSUserDefaults standardUserDefaults] objectForKey:@"PID"];
            DLog(@"pushid : %@", self.pushid);
            self.bc = [[NSUserDefaults standardUserDefaults] objectForKey:@"BC"];
            DLog(@"bc : %@", self.bc);
            
            //self.pushid =@"141";

            //AkMainView *mainView = [[AkMainView alloc] init];
            //http://m.akplaza.com/app/lib.do?act=viewPushDetail&push_id=3953
            //[[GlobalValues sharedSingleton] setPushUrl:@"http://www.daum.net"];
            
            //p65458 20150520 화면 이동이 되지 않는 문제로 추가 - 웹페이지가 업데이트 되면서 동작이 안되게 변견된듯
            [[GlobalValues sharedSingleton] setPushUrl:[NSString stringWithFormat:@"%@?act=viewPushDetail&push_id=%@", kLibURL, self.pushid  ]];
            
            [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"PID"];
            [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"BC"];
            
            UWTabBar *uwTabBar = [[GlobalValues sharedSingleton] tabBar];
            UINavigationController *navi = [uwTabBar.tabViewControllers objectAtIndex:0];
            
            NSLog(@"#######%@", navi);
            
            NSURLRequest *reqURL = [NSURLRequest requestWithURL:[NSURL URLWithString:[[GlobalValues sharedSingleton] pushUrl]]];
            //UWTabBar *tabBar = [[GlobalValues sharedSingleton] tabBar];
            uwTabBar.currentIndex = 0;
            [uwTabBar.webView loadRequest:reqURL];
            NSLog(@"reqUrl###########%@", reqURL);
            NSLog(@"self.navigationController###########%@", navi);
            [navi popViewControllerAnimated:YES];
            
            //p65458 20150520 화면 이동이 되지 않는 문제로 블럭 - 웹페이지가 업데이트 되면서 동작이 안되게 변견된듯

            
            
#ifdef p65458 //p65458 20150520 화면 이동이 되지 않는 문제로 블럭 - 웹페이지가 업데이트 되면서 동작이 안되게 변견된듯
            
            [[GlobalValues sharedSingleton] setPushUrl:[NSString stringWithFormat:@"%@?act=viewPushDetail&push_id=%@", kLibURL, self.pushid  ]];
            
            [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"PID"];
            [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"BC"];
            
            UWTabBar *uwTabBar = [[GlobalValues sharedSingleton] tabBar];
            
            
            [uwTabBar selectedItem: [[uwTabBar tabItemsArray ] objectAtIndex:0]];
#endif
            
            //루트 네비 뷰에서 네비 얻어오기
            //UINavigationController *navi = [uwTabBar.tabViewControllers objectAtIndex:0];

            
            //로구인으로 이동
            //[navi pushViewController:mainView animated:YES];//탭바 0 인덱스를 선택시 이미 메인페이지를 로딩하므로 메인페이지를 push할필요가 없음
            
            
#ifdef p65458
            
            AkLoginModel* oModel = [[AkLoginModel alloc] init];
            
            self.arrNotilist = [[[NSMutableArray alloc] initWithArray:[oModel performSelector:@selector(notiList)]] autorelease];
            
            [oModel activityStop];

            self.pushid = [[NSUserDefaults standardUserDefaults] objectForKey:@"PID"];
            DLog(@"pushid : %@", self.pushid);
            self.bc = [[NSUserDefaults standardUserDefaults] objectForKey:@"BC"];
            DLog(@"bc : %@", self.bc);
            
            if (self.pushid && [self.pushid length] > 0)
            {

                    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"PID"];
                    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"BC"];
    
                    
                    for (NSMutableDictionary* dicParam in self.arrNotilist) {
                        
                        NSString* strPushID = [NSString stringWithString: [dicParam objectForKey:@"PUSH_ID"]];
                        
                        if ([self.pushid isEqualToString:strPushID]) {
                            
                            UIWebView *webView = [[UIWebView alloc]initWithFrame:CGRectMake(0, 0, 320, 440)];
                            //[webView setDelegate:self];
                            [webView setDelegate:(id<UIWebViewDelegate>)self];
                            
                            NSString *url = [NSString stringWithString:[dicParam objectForKey:[[kNotiListElements objectForKey:@"alarm"] objectAtIndex:3]]];
                            
                            //NSString *url = @"http://m.naver.com";
                            
                            [webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:url]]];
                            //[self.view addSubview:webView];
                            [webView release];
                            
                            
                            
                            /*
                            AkNotiContentViewController *akNoti = [[[AkNotiContentViewController alloc] init] autorelease];
                            
                            akNoti.arrNotiContent = dicParam;
                            
                            [self.navigationController pushViewController:akNoti animated:YES];
                            */

                            self.pushid = nil;
                            
                            return;
                        }
                    
                    }
            }
#endif
            
            #ifdef p65458//p65458 20130620 기존 시나리오 변경으로 block
            //NSString* strPushID = [[NSUserDefaults standardUserDefaults] objectForKey:@"PID"];
            //NSString* strBC = [[NSUserDefaults standardUserDefaults] objectForKey:@"BC"];
            
            //탭바 My로 이동
            UWTabBar *uwTabBar = [[GlobalValues sharedSingleton] tabBar];
            
            //MY 메인의 서브 뷰일때 푸쉬가 오면 루트로 옮긴 다음에 다시
            //현재 탭의 view랑 4번 탭의 뷰가 같으냐?
            
            //루트 네비 뷰에서 네비 얻어오기 
            UINavigationController *navi = [uwTabBar.tabViewControllers objectAtIndex:0];
            
            [uwTabBar selectedItem: [[uwTabBar tabItemsArray ] objectAtIndex:0]];
            
            //알림보관함 리스트 화면 
            AkNotiListBoxViewController *akNotiListBoxView = [[AkNotiListBoxViewController alloc] init];
            //AkMyMainViewContorller *myMain = [[AkMyMainViewContorller alloc] init];
            
            //akNotiListBoxView.pushid = strPushID;
            //akNotiListBoxView.bc = strBC;
            
            //MY 메인만날때까지 뷰 제거
            //[navi pushViewController:akNotiListBoxView animated:YES];
            
            //알림 보관함 리트트로 이동 
            [navi pushViewController:akNotiListBoxView animated:YES];
            
            

            //strPushID = nil;
            //strBC = nil;
            #endif //p65458 20130620 기존 시나리오 변경으로 block
            
            
            
        }   
        else
        {
            //알림안보겠다고 했을때..
            [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"PID"];
            [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"BC"];
        }
        
    }
    
    
}


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    //changuk 16.08.09 IGAWorks 추가
    if (NSClassFromString(@"ASIdentifierManager")){
        NSUUID *ifa =[[ASIdentifierManager sharedManager]advertisingIdentifier];
        BOOL isAppleAdvertisingTrackingEnalbed = [[ASIdentifierManager sharedManager]isAdvertisingTrackingEnabled];
        [IgaworksCore setAppleAdvertisingIdentifier:[ifa UUIDString] isAppleAdvertisingTrackingEnabled:isAppleAdvertisingTrackingEnalbed];
        
        NSLog(@"[ifa UUIDString] %@", [ifa UUIDString]);
    }

    [IgaworksCore igaworksCoreWithAppKey:@"201510655" andHashKey:@"98172f01d8db43dd"];
    
    //푸시 서비스 할때 배지값을 0으로 초기화한다.
//    [[UIApplication sharedApplication] setApplicationIconBadgeNumber:0];
    
    //KCP설정추가 2014.07.16
    [NSUserDefaults resetStandardUserDefaults];
    
    [[NSUserDefaults standardUserDefaults] setInteger:1 forKey:@"state"]; //처음
    //KCP설정추가 종료
    
    self.window = [[[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]] autorelease];
    //마지막에 나오는 이미지 (투명도 조절해야할듯)
    NSString *path = [[NSBundle mainBundle] pathForResource:ASSET_BY_SCREEN_HEIGHT(@"Default10", @"Default10-568h@2x")   ofType:@"png"];
    UIImage *lodingDefaultImage = [[UIImage alloc] initWithContentsOfFile:path];
    //UIImage *lodingDefaultImage = [UIImage imageWithContentsOfFile:path];
   
    self.activityImageView = [[UIImageView alloc] initWithImage:lodingDefaultImage];

    if ([[UIScreen mainScreen] respondsToSelector:@selector(displayLinkWithTarget:selector:)]){
        if([UIScreen mainScreen].scale == 3.0) {
            [self.activityImageView setFrame:CGRectMake(0, -50, self.window.frame.size.width, self.window.frame.size.height+100)];
        }
    }
    
    path = nil;

    [lodingDefaultImage release];
    lodingDefaultImage = nil;
    
    //Add more images which will be used for the animation
    NSMutableArray *animationImages = [[NSMutableArray alloc]init];
    NSString *h568 = @"";
    if([[UIScreen mainScreen] bounds].size.height != 480)
    {
        h568 = @"-568h@2x";
    }
    for (int i = 1; i <= 30; i++) {
        [animationImages addObject:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:[NSString stringWithFormat:@"Default%02d%@",i,h568] ofType:@"png"]]];
    }
    
    self.activityImageView.animationImages = animationImages;
    
    self.activityImageView.backgroundColor = [UIColor clearColor];
    //Set the duration of the animation (play with it
    //until it looks nice for you)
    self.activityImageView.animationDuration=3;  // 초단위 이미지 변경 시간(숫자가 작을수록 빨라집니다)
    self.activityImageView.animationRepeatCount=1; // 반복횟수, 0이면 무한 반복

// changuk 2013.08.26 5인치에서 잘려나옴으로 주석처리
//    self.activityImageView.frame = self.window.frame; //CGRectMake(
//                                              0,
//                                              0, 
//                                              320, 
//                                              480);
    
    [self.window addSubview:self.activityImageView];
    
    //Start the animation
    [self.activityImageView startAnimating];
    
    
    
//    [UIView beginAnimations:nil context:nil];
//    
//    [UIView setAnimationDuration:1.0];
    
    //self.activityImageView.alpha = 0.3;
    
    [self mainView];
	//0.3f 후에 탭바를 포함한 메인 view 띄우기 
//	[NSTimer scheduledTimerWithTimeInterval:3.0f
//									 target:self
//								   selector:@selector(mainView)
//								   userInfo:nil
//									repeats:NO];
//
//    [UIView commitAnimations];

    //removeFromSuperview 이걸 해줘야 인트로이미지 view가 사라진다 , 하지만 애니메이션 효과가 나지 않는다 
    [self.activityImageView.animationImages release];
    //[self.activityImageView removeFromSuperview];
    [self.activityImageView release];
    self.activityImageView = nil;
    
    
    // Override point for customization after application launch.
    self.window.backgroundColor = [UIColor clearColor];
    

    
    
    //인터넷 접속 안될시 앱이 죽기때문에 인터넷 접속 체크
    int result = [[Reachability reachabilityForInternetConnection] currentReachabilityStatus];
    if (result != 0) {
        [self.window makeKeyAndVisible];
    }
    
    
    //푸시 
    NSDictionary* userInfo = [launchOptions objectForKey:UIApplicationLaunchOptionsRemoteNotificationKey];
    
    if (userInfo != nil) {
        [self application:application didReceiveRemoteNotification:userInfo];
    }
    
    //푸시 등록은...  ak플라자는 로그인/알림설정 뷰에서 한다 
    
    
    //2012.04.08 myak하면서 로직이 바뀜 
    //getDenyState로 서버단에 알림설정정보 받아서 처리하기 
    //알림설정할때 푸시 등록하기
    
    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 8.0)
    {
        [[UIApplication sharedApplication] registerUserNotificationSettings:[UIUserNotificationSettings settingsForTypes:
                                                                             (UIUserNotificationTypeSound | UIUserNotificationTypeAlert | UIUserNotificationTypeBadge) categories:nil]];
        [[UIApplication sharedApplication] registerForRemoteNotifications];
    }
    else
    {
        [[UIApplication sharedApplication] registerForRemoteNotificationTypes:UIRemoteNotificationTypeAlert | UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeSound];
    }
    
    return YES;
}


//전화왔을때.. 전화 상태바 문제 해결 
-(void)application:(UIApplication *)application willChangeStatusBarFrame:(CGRect)newStatusBarFrame
{
//앱 리뉴얼 이후 필요없음
//    _window.frame = [[UIScreen mainScreen] applicationFrame];
//    
//    //tabbar viewDidAppear 메소드에 같은 frame셋팅하는 부분이 있다.
//    //차후 tab bg 이미지를 받으면 해당 이미지 사이즈에 맞춰서 53인부분을 수정하면 된다.
//    int nTabBgImgH = kTabBgImgHegiht;
//    
//    [[GlobalValues sharedSingleton] tabBar].tabBarHolder.frame = CGRectMake([[UIScreen mainScreen] applicationFrame].origin.x, ([[UIScreen mainScreen] applicationFrame].size.height)-nTabBgImgH, [[UIScreen mainScreen] applicationFrame].size.width, nTabBgImgH);
    
    
}



-(void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    
    DLog(@"userInfo : %@", userInfo);
    
    //푸시 알림이 오면 보이는 메세지 
    MSG_DELEGATE_BTN2(@"AK플라자",  @"닫기",  @"보기", [[userInfo objectForKey:@"aps"] objectForKey:@"alert" ] , self);
    
    DLog(@"내용 : %@",[[userInfo objectForKey:@"aps"] objectForKey:@"alert" ]);
    DLog(@"PID : %@",[userInfo objectForKey:@"PID"]);
    DLog(@"BC : %@",[userInfo objectForKey:@"BC"]);
    
    
    //NSString* strPushID = @"35";
    NSString* strPushID = [userInfo objectForKey:@"PID"];
    
    //2012.04.17 지점코드 사용안함. 지점코드사용해서 로그인
    //NSString* strBC = [userInfo objectForKey:@"BC"];
    //공통이든 각각의 지점이든 그냥 99로 셋팅. 알림보관함의 로그인 화면 처리 로직때문에.. 
    NSString* strBC = @"99";
    
    [[NSUserDefaults standardUserDefaults] setObject:strPushID  forKey:@"PID"];
    [[NSUserDefaults standardUserDefaults] setObject:strBC      forKey:@"BC"];
    
    strPushID = nil;
    strBC = nil;
    
    
    int nNotiSound = [[NSUserDefaults standardUserDefaults] integerForKey:@"notiSound"];
    
    //어플 실행 중일때 알림소리 .
    if (nNotiSound == 0) 
    {
        //무음.
    }
    else if (nNotiSound == 1) 
    {
        AudioServicesPlaySystemSound(kSystemSoundID_Vibrate);
        
    }
    else if (nNotiSound == 2)
    {
        //파일명과 확장자명이 대소문자 구분하기때문에 명확히 입력해줘야한다. 
        NSString* sndPath = [[NSBundle mainBundle] pathForResource:@"sound" ofType:@"WAV"];
        //DLog(@"sndPath : %@", sndPath);
        if (sndPath) {
            //음원 경로
            baseURL = (CFURLRef) [[NSURL alloc] initFileURLWithPath:sndPath];
            //음원 생성
            AudioServicesCreateSystemSoundID(baseURL, &soundID);
            
            //음원 재생
            AudioServicesPlaySystemSound(soundID);
        }
        else
        {
            AudioServicesPlaySystemSound(kSystemSoundID_Vibrate);
        }        
    }
    
}






#pragma Notification Push
-(void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
    
    //푸시 때 테스트할 디바이스 토큰 값
    //0x93,0x60,0x83,0xe7,0x46,0x21,0x46,0x36,
    //0x27,0x77,0x93,0xdb,0x8e,0x69,0x5e,0x8c,
    //0x0f,0x05,0xca,0x2b,0x0d,0x72,0x29,0xc5,
    //0x47,0x6a,0x28,0xfb,0xd6,0x0a,0x52,0x8f
    /*
     const unsigned char* bytes = (const unsigned char*)[deviceToken bytes];
     for (int i=0;i<[deviceToken length];i++) {
     if (i%8==0 && i) printf("\n");
     printf("0x%.2x,",bytes[i]&0xff);
     }
     */
    //URL connection 맺기 
    //http://91.3.115.208:8080/AKTest/push.jsp?fn=devicereg&token=936083e746214636277793db8e695e8c0f05ca2b0d7229c5476a28fbd60a528f%20&pType=1&appType=1
    
    
    DLog(@"didRegisterForRemoteNotificationsWithDeviceToken");
    
    
    
    //최초 디바이스 등록 했냐? 
    NSString *userDeviceToken = [[NSUserDefaults standardUserDefaults] objectForKey:@"deviceToken"];
    
    DLog(@"최초 디바이스 등록 했다? userDeviceToken string : %@", userDeviceToken);
    
    if ([userDeviceToken length] <= 0) {
        
        const char* data = [deviceToken bytes];
        NSMutableString* token = [NSMutableString string];
        
        for (int i = 0; i < [deviceToken length]; i++) {
            [token appendFormat:@"%02.2hhX", data[i]];
        }
        
        NSString *resultString =  [[token copy] autorelease];
        
        [[NSUserDefaults standardUserDefaults] setObject:resultString forKey:@"deviceToken"];
        
        //NSString *resultString = [[NSString alloc] initWithData:deviceToken encoding:NSUTF8StringEncoding];
        
        DLog(@"deviceToken string : %@", resultString);
        
        AkNotiModel *oNotiModel = [[AkNotiModel alloc] init];
        
        //oNotiModel.delegate = self;
        
        [oNotiModel performSelector:@selector(notiDeviceRegist:) withObject:resultString];
        
        [oNotiModel release];
        oNotiModel = nil;
        
        resultString = nil;
    }
    
    
    
    
    //디바이스 등록한뒤 알림설정 화면으로 넘어가기 
    
    
    
    
}

-(BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url
{
    if (!url) {  return NO; }
    
    
    DLog(@"application:(UIApplication *)application handleOpenURL:(NSURL *)url");
    
    NSString *URLString = [url absoluteString];
    
    
    DLog(@"current url : %@", URLString);
    
        
    NSMutableString *mutaOrg = [NSMutableString stringWithString:URLString];
    
    
    //회원가입시 처리 로직입니다. 
    NSRange strRange = [mutaOrg rangeOfString:@"akplaza://"]; 
    
    if (strRange.location != NSNotFound)
    {
        
        NSArray *arrJoin = [URLString componentsSeparatedByString:@"membersReturn="];
        
        if(arrJoin && [arrJoin count] > 1)
        {
            //홈 탭으로 이동 하기 
            if ([[arrJoin objectAtIndex:1] isEqualToString:@"home"]) {
                //탭바 home로 이동

                UWTabBar *uwTabBar = [[GlobalValues sharedSingleton] tabBar];
                
                
                [uwTabBar selectedItem: [[uwTabBar tabItemsArray ] objectAtIndex:0]];

            }
            
            if ([[arrJoin objectAtIndex:1] isEqualToString:@"login"]) {
                
                //탭바 My로 이동
                UWTabBar *uwTabBar = [[GlobalValues sharedSingleton] tabBar];
                
                
                //MY 메인의 서브 뷰일때 푸쉬가 오면 루트로 옮긴 다음에 다시
                //현재 탭의 view랑 4번 탭의 뷰가 같으냐?
                
                //루트 네비 뷰에서 네비 얻어오기 
                UINavigationController *navi = [uwTabBar.tabViewControllers objectAtIndex:0];
                
                [uwTabBar selectedItem: [[uwTabBar tabItemsArray ] objectAtIndex:0]];
                
                //로그인 화면 
                LoginViewController *loginView = [[LoginViewController alloc] init];
                
                //로구인으로 이동 
                [navi pushViewController:loginView animated:YES];
                
            }
            
        }
        
        NSArray *arrReturnUrl = [URLString componentsSeparatedByString:@"returnURL="];
        
        if(arrReturnUrl && [arrReturnUrl count] > 1)
        {
            
                // p65458 20130708 url 이동으로 기능 추가
//            NSString* moveUrl = [URLString stringByReplacingOccurrencesOfString:@"akplaza://returnURL=" withString:@""];

            //[[GlobalValues sharedSingleton] setReturnUrl:@"http://www.daum.net"];
            [[GlobalValues sharedSingleton] setReturnUrl:arrReturnUrl[1]];

                //탭바 home로 이동
                
                UWTabBar *uwTabBar = [[GlobalValues sharedSingleton] tabBar];
            
            
                [uwTabBar selectedItem: [[uwTabBar tabItemsArray ] objectAtIndex:0]];
            /*
                //Web URL View
                UIWebView *webView = [[UIWebView alloc]initWithFrame:CGRectMake(0, 35.0, 320, 328)];
                //[webView setDelegate:self];
                [webView setDelegate:(id<UIWebViewDelegate>)self];
            
                //[webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:URLString]]];
                [webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:@"http://www.daum.net"]]];
            
                [self.view addSubview:webView];
                [webView release];
            */
            /*
            //로그인 화면
            AkMainView *mainView = [[AkMainView alloc] init];
            
            //루트 네비 뷰에서 네비 얻어오기
            UINavigationController *navi = [uwTabBar.tabViewControllers objectAtIndex:0];
            
            mainView.strURL = @"http://www.daum.net";
            
            //로구인으로 이동
            [navi pushViewController:mainView animated:YES];
             */

                // p65458 20130708 url 이동으로 기능 추가

        }
    }
    
    //KCP설정 추가 20140716
    NSArray *arrIspInfo = [[NSString stringWithFormat:@"%@",url] componentsSeparatedByString:@"approval_key="];
    
    NSString* strURL = [url absoluteString];
    if([strURL rangeOfString:@"approval_key"].location != NSNotFound)
    {
        NSRange range={[[arrIspInfo objectAtIndex:1] length]-4,4};
        
        NSString *resultString=[[arrIspInfo objectAtIndex:1] substringWithRange:range];
        
        if([resultString isEqualToString:@"0000"])
        {
            [[NSUserDefaults standardUserDefaults] setInteger:0 forKey:@"state"]; //성공
            
            NSRange range2={0,[[arrIspInfo objectAtIndex:1] length]-4};
            
            NSString *resultString2=[[arrIspInfo objectAtIndex:1] substringWithRange:range2];
            
            NSString *returnUrl=[NSString stringWithFormat:@"%@/app.do?ActionResult=app&approval_key=%@&AppUrl=true",KCPAddress,resultString2];
            
            [[NSUserDefaults standardUserDefaults] setObject:returnUrl forKey:@"returnUrl"];
        }
        else
        {
            [[NSUserDefaults standardUserDefaults] setInteger:-99 forKey:@"state"]; //취소
        }
        
        if ([[NSUserDefaults standardUserDefaults] integerForKey:@"state"]!=1 &&
            [[NSUserDefaults standardUserDefaults] integerForKey:@"state"]!=-99)
        {
            [[[GlobalValues sharedSingleton] tabBar].webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:[[NSUserDefaults standardUserDefaults] stringForKey:@"returnUrl"]]]];
        }
        
    }
    //KCP설정추가 종료
    
    return YES;

}


-(void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error
{
    DLog(@"didFailToRegisterForRemoteNotificationsWithError");
    DLog(@"%@", error);
    
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"deviceToken"];
}



- (void)applicationWillResignActive:(UIApplication *)application
{
    /*
     Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
     Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
     */
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    /*
     Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
     If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
     */
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    /*
     Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
     */
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    /*
     Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
     */
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Saves changes in the application's managed object context before the application terminates.
    [self saveContext];
}

- (void)saveContext
{
    NSError *error = nil;
    NSManagedObjectContext *managedObjectContext = self.managedObjectContext;
    if (managedObjectContext != nil)
    {
        if ([managedObjectContext hasChanges] && ![managedObjectContext save:&error])
        {
            /*
             Replace this implementation with code to handle the error appropriately.
             
             abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development. 
             */
            DLog(@"Unresolved error %@, %@", error, [error userInfo]);
            abort();
        } 
    }
}

#pragma mark - Core Data stack

/**
 Returns the managed object context for the application.
 If the context doesn't already exist, it is created and bound to the persistent store coordinator for the application.
 */
- (NSManagedObjectContext *)managedObjectContext
{
    if (__managedObjectContext != nil)
    {
        return __managedObjectContext;
    }
    
    NSPersistentStoreCoordinator *coordinator = [self persistentStoreCoordinator];
    if (coordinator != nil)
    {
        __managedObjectContext = [[NSManagedObjectContext alloc] init];
        [__managedObjectContext setPersistentStoreCoordinator:coordinator];
    }
    return __managedObjectContext;
}

/**
 Returns the managed object model for the application.
 If the model doesn't already exist, it is created from the application's model.
 */
- (NSManagedObjectModel *)managedObjectModel
{
    if (__managedObjectModel != nil)
    {
        return __managedObjectModel;
    }
    NSURL *modelURL = [[NSBundle mainBundle] URLForResource:@"AppAkPlaza_IOS" withExtension:@"momd"];
    __managedObjectModel = [[NSManagedObjectModel alloc] initWithContentsOfURL:modelURL];
    return __managedObjectModel;
}

/**
 Returns the persistent store coordinator for the application.
 If the coordinator doesn't already exist, it is created and the application's store added to it.
 */
- (NSPersistentStoreCoordinator *)persistentStoreCoordinator
{
    if (__persistentStoreCoordinator != nil)
    {
        return __persistentStoreCoordinator;
    }
    
    NSURL *storeURL = [[self applicationDocumentsDirectory] URLByAppendingPathComponent:@"AppAkPlaza_IOS.sqlite"];
    
    NSError *error = nil;
    __persistentStoreCoordinator = [[NSPersistentStoreCoordinator alloc] initWithManagedObjectModel:[self managedObjectModel]];
    if (![__persistentStoreCoordinator addPersistentStoreWithType:NSSQLiteStoreType configuration:nil URL:storeURL options:nil error:&error])
    {
        /*
         Replace this implementation with code to handle the error appropriately.
         
         abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development. 
         
         Typical reasons for an error here include:
         * The persistent store is not accessible;
         * The schema for the persistent store is incompatible with current managed object model.
         Check the error message to determine what the actual problem was.
         
         
         If the persistent store is not accessible, there is typically something wrong with the file path. Often, a file URL is pointing into the application's resources directory instead of a writeable directory.
         
         If you encounter schema incompatibility errors during development, you can reduce their frequency by:
         * Simply deleting the existing store:
         [[NSFileManager defaultManager] removeItemAtURL:storeURL error:nil]
         
         * Performing automatic lightweight migration by passing the following dictionary as the options parameter: 
         [NSDictionary dictionaryWithObjectsAndKeys:[NSNumber numberWithBool:YES], NSMigratePersistentStoresAutomaticallyOption, [NSNumber numberWithBool:YES], NSInferMappingModelAutomaticallyOption, nil];
         
         Lightweight migration will only work for a limited set of schema changes; consult "Core Data Model Versioning and Data Migration Programming Guide" for details.
         
         */
        DLog(@"Unresolved error %@, %@", error, [error userInfo]);
        abort();
    }    
    
    return __persistentStoreCoordinator;
}

#pragma mark - Application's Documents directory

/**
 Returns the URL to the application's Documents directory.
 */
- (NSURL *)applicationDocumentsDirectory
{
    return [[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask] lastObject];
}

@end
