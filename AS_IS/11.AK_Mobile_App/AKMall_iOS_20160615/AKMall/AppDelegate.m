//
//  AppDelegate.m
//  test
//
//  Created by 한병일 on 2014. 6. 9..
//  Copyright (c) 2014년 한병일. All rights reserved.
//

#import "AppDelegate.h"
#import "HomeViewController.h"
#import "SearchViewController.h"
#import "CartViewController.h"
#import "OrderListViewController.h"
#import "MyViewController.h"
#import "Define.h"
// 탈옥검사
#import "JBroken.h"
// iAD (for IGAW)
#import <AdSupport/AdSupport.h>
//2015-12-01 오창욱 애드브릭스
#import <IgaworksCore/IgaworksCore.h>
#import <LiveOps/LiveOps.h>
//사파리 새창 리젝으로인한 사파리 컨트롤러
#import <SafariServices/SafariServices.h>

@implementation AppDelegate

#define kNotiDeviceUSERRoot_Item         @"root"
#define kNotiDeviceUSER_Item             @"updateduser"
#define kNotiDeviceUSER_Gubun            @"gubun"
#define NOTI_DEVICEUSER_ITEM_ATTREBUTE [[NSArray alloc] initWithObjects:kNotiDeviceUSERRoot_Item, kNotiDeviceUSER_Item, kNotiDeviceUSER_Gubun, nil]

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    //shortcutitem add
//    UIApplicationShortcutItem *item1 = [[UIApplicationShortcutItem alloc] initWithType:@"com.akmall.touchAKmall.item0" localizedTitle:@"Action1" localizedSubtitle:@"title2" icon:[UIApplicationShortcutIcon iconWithType:UIApplicationShortcutIconTypeLove] userInfo:nil];
//    UIApplicationShortcutItem *item2 = [[UIApplicationShortcutItem alloc] initWithType:@"com.akmall.touchAKmall.item1" localizedTitle:@"Action2" localizedSubtitle:@"title3" icon:[UIApplicationShortcutIcon iconWithType:UIApplicationShortcutIconTypeCloud] userInfo:nil];
//    UIApplicationShortcutItem *item3 = [[UIApplicationShortcutItem alloc] initWithType:@"com.akmall.touchAKmall.item2" localizedTitle:@"Action3" localizedSubtitle:nil icon:[UIApplicationShortcutIcon iconWithType:UIApplicationShortcutIconTypeTask] userInfo:nil];
//    UIApplicationShortcutItem *item4 = [[UIApplicationShortcutItem alloc] initWithType:@"com.akmall.touchAKmall.item3" localizedTitle:@"Action4" localizedSubtitle:nil icon:[UIApplicationShortcutIcon iconWithType:UIApplicationShortcutIconTypeTask] userInfo:nil];
//    
//    [[UIApplication sharedApplication] setShortcutItems: @[ item1, item2, item3, item4 ]];

    
    [self setIntro];
    
    NSLog(@"deviceToken %@", [[NSUserDefaults standardUserDefaults] objectForKey:@"deviceToken"]);
    
    barHidden = NO;
    
    // IGAW
    [IgaworksCore igaworksCoreWithAppKey:@"657336955"
                              andHashKey:@"415d451a0440411e"
            andIsUseIgaworksRewardServer:NO];
    
    // 나중에 앱 업데이트할때 http://support.ad-brix.com/page/ea2b6361170f4ce28b55de129c5721f3/IDFA_Guide/kos
    /* IDFA등록! 하고 있는대 */
    if (NSClassFromString(@"ASIdentifierManager")){
        NSUUID *ifa =[[ASIdentifierManager sharedManager]advertisingIdentifier];
        BOOL isAppleAdvertisingTrackingEnalbed = [[ASIdentifierManager sharedManager]isAdvertisingTrackingEnabled];
        [IgaworksCore setAppleAdvertisingIdentifier:[ifa UUIDString] isAppleAdvertisingTrackingEnabled:isAppleAdvertisingTrackingEnalbed];
//        NSLog(@"[ifa UUIDString] %@", [ifa UUIDString]);
    }
    
    //2015-12-01 오창욱 애드브릭스
//    [IgaworksCore setUserId:[self getUUID]];
//    [LiveOpsPush initPush];
    
    //공지팝업
//    [LiveOpsPopup getPopups:^{
////        공지팝업 데이터가 있을 경우 이 코드블럭이 호출됩니다.
////        [LiveOpsPopup showPopups:@"test"];
//    }];
    
    // TODO : 배포전 제거 (IGAW로그 출력) 
//    [IgaworksCore setLogLevel:IgaworksCoreLogInfo];
    
    
// 푸쉬처리부분 ios7 이상부터 처리방시 변경으로 사용안함
//    NSDictionary* userInfo = [launchOptions objectForKey:UIApplicationLaunchOptionsRemoteNotificationKey];
    //PUSH 서버 등록
//    if (userInfo != nil) {
//        [self application:application didReceiveRemoteNotification:userInfo];
//    }
    
    
    //버전체크
    
    
    UITabBarController *tabBarController = (UITabBarController *)self.window.rootViewController;
    tabBarController.delegate = self;
    UITabBar *tabBar = tabBarController.tabBar;
    UITabBarItem *tabBarItem1 = [tabBar.items objectAtIndex:0];
    UITabBarItem *tabBarItem2 = [tabBar.items objectAtIndex:1];
    UITabBarItem *tabBarItem3 = [tabBar.items objectAtIndex:2];
    UITabBarItem *tabBarItem4 = [tabBar.items objectAtIndex:3];
    UITabBarItem *tabBarItem5 = [tabBar.items objectAtIndex:4];
    
    HomeViewController *homeView = [tabBarController.viewControllers objectAtIndex:0];
    homeView.urls = MainURL_1;
    
    tabBarItem1.selectedImage = [[UIImage imageNamed:@"tab_home_on.png"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    tabBarItem1.image = [[UIImage imageNamed:@"tab_home.png"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    
    tabBarItem2.selectedImage = [[UIImage imageNamed:@"tab_category_on.png"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    tabBarItem2.image = [[UIImage imageNamed:@"tab_category.png"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    
    
    tabBarItem3.selectedImage = [[UIImage imageNamed:@"tab_cart_on.png"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    tabBarItem3.image = [[UIImage imageNamed:@"tab_cart.png"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    
    tabBarItem4.selectedImage = [[UIImage imageNamed:@"tab_delivery_on.png"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    tabBarItem4.image = [[UIImage imageNamed:@"tab_delivery.png"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    
    tabBarItem5.selectedImage = [[UIImage imageNamed:@"tab_myak_on.png"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    tabBarItem5.image = [[UIImage imageNamed:@"tab_myak.png"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    
    tabBarController.selectedIndex = 4;
    selectTab = tabBarController.selectedIndex;
    dispatch_async(dispatch_get_main_queue(), ^{
        [self versionCheck];
    
        // Override point for customization after application launch.
        int result = [[Reachability reachabilityForInternetConnection] currentReachabilityStatus];
        
        //인터넷이 되면...
        if (result != 0) {
            if (result == 2)
            {
                NSUserDefaults *UD = [NSUserDefaults standardUserDefaults];
                if([UD integerForKey:@"3gDialog"] != 1){
                    UIAlertView *alert;
                    alert = [[UIAlertView alloc] initWithTitle:@"데이터 통화료 안내"
                                                       message:@"3G/4G로 서비스 이용 시 많은 데이터 통화료가 발생할 수 있습니다.무선랜(Wi-Fi)이나 데이터 정액제를 통해 부담없이 사용하실 수 있습니다"
                                                      delegate:self
                                             cancelButtonTitle:@"확인"
                                             otherButtonTitles:@"다시보지 않음",nil];
                    [alert setTag:100];
                    [alert show];
                }
            }
        }
    });
    
    //푸시 등록 하기 , 토큰이 존재 안하면..
    //푸시 알림 알럿,뱃지, 소리로 받을껀지 설정 처리
    //iOS8.0 이상 조건문 추가 2015.01.19 changuk
    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 8.0)
    {
        [[UIApplication sharedApplication] registerUserNotificationSettings:[UIUserNotificationSettings settingsForTypes:(UIUserNotificationTypeSound | UIUserNotificationTypeAlert | UIUserNotificationTypeBadge) categories:nil]];
        [[UIApplication sharedApplication] registerForRemoteNotifications];
    }
    else
    {
        [[UIApplication sharedApplication] registerForRemoteNotificationTypes:UIRemoteNotificationTypeAlert| UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeSound];
    }
    
    return YES;
}

- (NSNumber*)updateDeviceUser:(NSString*)userID
{
    NSString *deviceToken = [[NSUserDefaults standardUserDefaults] objectForKey:@"deviceToken"];
    
    NSString *strURL = [NSString stringWithFormat:@"%@?act=updateDeviceUser&phonetype=0&token=%@&userid=%@&version=%@&appid=%@", LibURL, deviceToken, userID ,[[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"], [self getUUID]];
//    NSLog(@"updateDeviceUser %@", strURL);
    
    XmlParser *xmlParser = [[XmlParser alloc] initParser:[NSURL URLWithString:strURL] andWithItems:NOTI_DEVICEUSER_ITEM_ATTREBUTE andWithParserType:(ParserType *)Parser1];
    
    if (xmlParser.parsedItems && [xmlParser.parsedItems count] > 0)
    {
        NSString *gubun = [[xmlParser.parsedItems objectAtIndex:0] objectForKey:kNotiDeviceUSER_Gubun];
        
//        NSLog(@"updateDeviceUser_Gubun : %@", gubun);
        
        if ([gubun isEqualToString:@"COMPLETE"]) {
            return [[NSNumber alloc] initWithBool:YES];
        }
        
    }


    
    return [[NSNumber alloc] initWithBool:NO];
}
				
- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    //앱실행시 배지초기화 2014.12.16
    [[UIApplication sharedApplication] setApplicationIconBadgeNumber:0];
    
    // 탈옥검사 : 앱이 Front로 올라올때 마다 검사 합니다.
    if(isDeviceJailbroken()){
        // TODO : 타이틀 및 메시지 변경할 것
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:nil
                                                        message:@"고객님의 안전한 쇼핑을 위해 변경되지 않은 운영체제(순정상태)를 탑재한 단말기에 한해 서비스 이용이 가능합니다."
                                                       delegate:self
                                              cancelButtonTitle:@"닫기"
                                              otherButtonTitles:nil];
        [alert setTag:99];
        [alert show];
    }
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

//퀵액션 버튼 실행시 사용
- (void)application:(UIApplication *)application performActionForShortcutItem:(nonnull UIApplicationShortcutItem *)shortcutItem completionHandler:(nonnull void (^)(BOOL))completionHandler {
    UITabBarController *tabBar = (UITabBarController *)self.window.rootViewController;
    tabBar.selectedIndex = 4;
    MyViewController *myViewController = (MyViewController *)[self getSelectViewController];
    NSString *url;
    if ([shortcutItem.type isEqualToString:@"com.akmall.touchAKmall.item0"]) {
        url = [NSString stringWithFormat:@"%@/search/SearchMain.do?isAkApp=iPhone",RootURL];
    } else if ([shortcutItem.type isEqualToString:@"com.akmall.touchAKmall.item1"]) {
        url = [NSString stringWithFormat:@"%@/mypage/OrderDeliInquiry.do?isAkApp=iPhone",RootURL];
    } else if ([shortcutItem.type isEqualToString:@"com.akmall.touchAKmall.item2"]) {
        url = [NSString stringWithFormat:@"%@/main/Main.do?isAkApp=iPhone#section2",RootURL];
    } else if ([shortcutItem.type isEqualToString:@"com.akmall.touchAKmall.item3"]) {
        url = [NSString stringWithFormat:@"%@/goods/RecentChoicGoods.do?isAkApp=iPhone",RootURL];
    }
    myViewController.urls = url;
    [[self getSelectWebView] loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:url]]];
}

-(void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
    //2015-12-01 오창욱 애드브릭스
    [LiveOpsPush setDeviceToken:deviceToken];
    
//    NSLog(@"deviceToken : %@",deviceToken);
    
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
    
    
//    NSLog(@"didRegisterForRemoteNotificationsWithDeviceToken");
    
    //최초 디바이스 등록 했냐?
    NSString *userDeviceToken = [[NSUserDefaults standardUserDefaults] objectForKey:@"deviceToken"];
    
    const char* data = [deviceToken bytes];
    NSMutableString* token = [NSMutableString string];
    
    for (int i = 0; i < [deviceToken length]; i++) {
        [token appendFormat:@"%02.2hhX", data[i]];
    }
    
    NSString *resultString =  [[token copy] init];
    
    
//    NSLog(@"최초 디바이스 등록 했다? userDeviceToken string : %@", userDeviceToken);
    
    
    //메모리에 토큰값이 존재하지 않거나 존재하는토큰값이 서버에서 넘겨준 값이랑 틀릴경우에.. 재등록..
    if ([userDeviceToken length] <= 0 || ![userDeviceToken isEqualToString:resultString])
    {
        [[NSUserDefaults standardUserDefaults] setObject:resultString forKey:@"deviceToken"];
//        NSLog(@"deviceToken string : %@", resultString);
        
        [self notiDeviceRegist:resultString];
    }
     NSString *loginedID = [[NSUserDefaults standardUserDefaults] stringForKey:@"LoginID"];
    [self updateDeviceUser:loginedID];
    
    resultString = nil;
}


- (NSNumber*)notiDeviceRegist:(NSString*)token
{
    //버전전송 추가 (20140929 오창욱)
    NSString *strURL = [NSString stringWithFormat:@"%@?act=DeviceRegist&phonetype=0&token=%@&version=%@&appid=%@", LibURL, token, [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"], [self getUUID] ];
    NSArray *deviceitemattr = [[NSArray alloc] initWithObjects:@"root", @"deviceregist", @"gubun", nil];
    
//    NSLog(@"notiDeviceRegist %@",strURL);
    
    
    XmlParser *xmlParser = [[XmlParser alloc] initParser:[NSURL URLWithString:strURL] andWithItems:deviceitemattr andWithParserType:(ParserType *)Parser1];
    
    
    if (xmlParser.parsedItems && [xmlParser.parsedItems count] > 0)
    {
        NSString *gubun = [[xmlParser.parsedItems objectAtIndex:0] objectForKey:@"gubun"];
        //ERROR
        
        if ([gubun isEqualToString:@"COMPLETE"]) {
//            NSLog(@"notiDeviceRegist_Gubun : %@", gubun);
            return [[NSNumber alloc] initWithBool:YES];
        }
        else if ([gubun isEqualToString:@"DUPLICATE"])
        {
//            NSLog(@"notiDeviceRegist_Gubun : %@", gubun);
            return [[NSNumber alloc] initWithBool:YES];
        }
    }
    //[self activityStop];
    
    
    return [[NSNumber alloc] initWithBool:NO];
}



-(void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error
{
//    NSLog(@"didFailToRegisterForRemoteNotificationsWithError");
    
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"deviceToken"];
//    NSLog(@"Device Token Register Failed: %@", error);
}


-(void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(nonnull void (^)(UIBackgroundFetchResult))completionHandler
{
//    NSLog(@"userInfo : %@", userInfo);
    
    NSString* strPushID = [userInfo objectForKey:@"PID"];
    
    
    //백그라운드 푸쉬시에 해당url로 바로이동 2016.03.15
    if ([UIApplication sharedApplication].applicationState ==
        UIApplicationStateInactive) {
        UITabBarController *tabBar = (UITabBarController *)self.window.rootViewController;
//        tabBar.selectedIndex = 0;
//        HomeViewController *homeViewController = (HomeViewController *)[[tabBar viewControllers] objectAtIndex:0];
        tabBar.selectedIndex = 4;
        
        if (strPushID.length != 0) { // 기존 푸쉬 처리
            NSString *url = [[NSString stringWithFormat:@"%@%@",PushViewURL,strPushID] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
//            [homeViewController.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:url]]];
            MyViewController *myViewController = (MyViewController *)[self getSelectViewController];
            myViewController.urls = url;
            [[self getSelectWebView] loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:url]]];
            [[UIApplication sharedApplication] setApplicationIconBadgeNumber:0];
        } else {
//            [homeViewController.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:[userInfo objectForKey:@"deepLinkUrl"]]]];
            [[self getSelectWebView] loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:[userInfo objectForKey:@"deepLinkUrl"]]]];
        }
        return;
    }
    
    if (strPushID.length != 0) { // 기존 푸쉬 처리
        [[NSUserDefaults standardUserDefaults] setObject:strPushID forKey:@"PID"];
        //[[NSUserDefaults standardUserDefaults] setObject:@"1" forKey:@"PID"];
        [[NSUserDefaults standardUserDefaults] synchronize];
        
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"터치 AK" message:[[userInfo objectForKey:@"aps"] objectForKey:@"alert" ] delegate:self cancelButtonTitle:@"닫기" otherButtonTitles:@"보기", nil];
        [alert show];
    } else { // 라이브 오퍼레이션
        UITabBarController *tabBar = (UITabBarController *)self.window.rootViewController;
        tabBar.selectedIndex = 4;
//        tabBar.selectedIndex = 0;
//        HomeViewController *homeViewController = (HomeViewController *)[[tabBar viewControllers] objectAtIndex:0];
        UIAlertController *Alert = [UIAlertController alertControllerWithTitle:@"터치 AK" message:[[userInfo objectForKey:@"aps"] objectForKey:@"alert" ] preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction *ok = [UIAlertAction actionWithTitle:@"보기" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
//            [homeViewController.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:[userInfo objectForKey:@"deepLinkUrl"]]]];
            [[self getSelectWebView] loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:[userInfo objectForKey:@"deepLinkUrl"]]]];
        }];
        UIAlertAction *cencel = [UIAlertAction actionWithTitle:@"취소" style:UIAlertActionStyleCancel handler:nil];
        [Alert addAction:ok];
        [Alert addAction:cencel];
        [self.window.rootViewController presentViewController:Alert animated:YES completion:nil];
    }
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    switch(alertView.tag){
        case 99:
            // 탈옥된 단말기 강제종료
            exit(0);
            break;
        case 100:
            if (buttonIndex == 1)
            {
                NSUserDefaults *UD = [NSUserDefaults standardUserDefaults];
                if (UD)
                {
                    [UD setObject:[NSNumber numberWithInt:1] forKey:@"3gDialog"];
                }
                [UD synchronize];
            }
            break;
        case 101:
            if (buttonIndex == 1)
            {
                alertUrl = [alertUrl stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
                if (alertUrl.length > 0) {
                    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:alertUrl]];
                }
            }

            break;
        default:
            if (buttonIndex == 1)
            {
                NSString* strPushID = [[NSUserDefaults standardUserDefaults] objectForKey:@"PID"];
                
                UITabBarController *tabBar = (UITabBarController *)self.window.rootViewController;
                tabBar.selectedIndex = 4;
                
//                HomeViewController *homeViewController = (HomeViewController *)[[tabBar viewControllers] objectAtIndex:0];
                NSString *url = [[NSString stringWithFormat:@"%@%@",PushViewURL,strPushID] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
//                [homeViewController.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:url]]];
                [[self getSelectWebView] loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:url]]];
                
                [[UIApplication sharedApplication] setApplicationIconBadgeNumber:0];
            }
            break;
    }
}

- (BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url
{
    UITabBarController *tabBar = (UITabBarController *)self.window.rootViewController;
    NSString *URLString = [url absoluteString];
//    NSLog(@"handleOpenURL %@",URLString);
    NSRange strRangeISPApp = [URLString rangeOfString:@"akmall://"];
    if (strRangeISPApp.location != NSNotFound)
    {
        NSArray *arr = [URLString componentsSeparatedByString:@"returnUrl="];
        NSArray *arrJoin = [URLString componentsSeparatedByString:@"membersReturn="];
        if(arrJoin && [arrJoin count] > 1)
        {
            //홈 탭으로 이동 하기
            if ([[arrJoin objectAtIndex:1] isEqualToString:@"home"]) {
                //탭바 home로 이동
                tabBar.selectedIndex = 0;
            }
            if ([[arrJoin objectAtIndex:1] isEqualToString:@"login"]) {
                HomeViewController *homeViewController = (HomeViewController *)tabBar.selectedViewController;
                [homeViewController.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:MembersLogin]]];
            }
        }
        
        // 결제 완료후 akamll:// 스키마로 App이 Front로 올라온 경우
        if(arr && [arr count] > 1)
        {
            NSString *ispURL = [[arr objectAtIndex:1] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
//            NSLog(@"ispURL : %@",ispURL);
            NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:ispURL]];
            
            if(tabBar.selectedIndex == 0){
                HomeViewController *homeViewController = (HomeViewController *)tabBar.selectedViewController;
                homeViewController.urls = ispURL;
                [homeViewController.webView loadRequest:request];
            } else if(tabBar.selectedIndex == 1){
                SearchViewController *searchViewController = (SearchViewController *)tabBar.selectedViewController;
                [searchViewController.webView loadRequest:request];
            } else if(tabBar.selectedIndex == 2){
                CartViewController *cartViewController = (CartViewController *)tabBar.selectedViewController;
                [cartViewController.webView loadRequest:request];
            } else if(tabBar.selectedIndex == 3){
                OrderListViewController *orderListViewController = (OrderListViewController *)tabBar.selectedViewController;
                [orderListViewController.webView loadRequest:request];
            } else if(tabBar.selectedIndex == 4){
                UINavigationController *navigationController = (UINavigationController *)tabBar.selectedViewController;
                MyViewController *myViewController = (MyViewController *)navigationController.visibleViewController;
                [myViewController.webView loadRequest:request];
            }
        }
        
    }
    return YES;
}

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    NSString *req = [[request URL] absoluteString];
    NSLog(@"%@",req);
    
    if( [req rangeOfString:@"goods/GoodsImgDetail.do"].location != NSNotFound)
    {
        [webView setScalesPageToFit:YES];
        [webView setMultipleTouchEnabled:YES];
    }else
    {
        [webView setScalesPageToFit:NO];
        [webView setMultipleTouchEnabled:NO];
    }

    
    if([[[request URL] scheme] isEqualToString:@"tel"])
    {
        
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:nil
                                                        message:@"고객센터로 연결하시겠습니까?"
                                                       delegate:self
                                              cancelButtonTitle:@"취소"
                                              otherButtonTitles:@"전화연결",nil];
        [alert setTag:[[[[[request URL] absoluteString] componentsSeparatedByCharactersInSet:
                         [[NSCharacterSet characterSetWithCharactersInString:@"0123456789"] invertedSet]] componentsJoinedByString:@""] intValue]];
        [alert show];
        
        return NO;
    }
    
    UITabBarController *tabBar = (UITabBarController *)self.window.rootViewController;
    
    //MyViewController 가 아닐때만 이동하도록
    //main2 사용시엔 사용안함
//    if(tabBar.selectedIndex != 4) {
//        NSRange myPage = [req rangeOfString:@"/mypage/UserSetupMain.do"];
//        if(myPage.location != NSNotFound)
//        {
//            UITabBarController *tabBar = (UITabBarController *)self.window.rootViewController;
//            UINavigationController *navigationController = (UINavigationController *)[tabBar.viewControllers objectAtIndex:4];
//            MyViewController *myViewController = (MyViewController *)navigationController.visibleViewController;
//                    
//            [tabBar setSelectedIndex:4];
//            [myViewController setNaviString:request];
//            return NO;
//        }
//    }
    
    //newtab 스키마시 새창
    BOOL newtab = [req hasPrefix:@"newtab"];
    if (newtab) {
        NSString *url = [req stringByReplacingOccurrencesOfString:@"newtab" withString:@"http" options:NSCaseInsensitiveSearch range:NSMakeRange(0, 6)];
        [self openUrl:[NSURL URLWithString:url]];
        return NO;
    }
    
    
    //회원가입 선택시 새창에서 뜨게 하기(20140929 오창욱)
    NSRange strRangemembers = [req rangeOfString:MembersRootURL];
    
    if (strRangemembers.location != NSNotFound)
    {
        [self openUrl:[NSURL URLWithString:[NSString stringWithFormat:@"%@&isAkApp=iPhone&appVersion=%@", req, [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"]]]];
        return NO;
    }
    
    //페이스북 이동시 새창으로
    NSRange strFacebook = [req rangeOfString:@"facebook.com"];
    if (strFacebook.location != NSNotFound) {
        [self openUrl:request.URL];
        return NO;
    }
    
    //핀터레스트 새창으로
    NSRange strPin = [req rangeOfString:@"pinterest"];
    if (strPin.location != NSNotFound) {
        [[UIApplication sharedApplication] openURL:request.URL];
        return NO;
    }
    
    //sms 내부 전송으로
    NSRange strSms = [req rangeOfString:@"sms:"];
    if (strSms.location != NSNotFound) {
        MFMessageComposeViewController *controller = [[MFMessageComposeViewController alloc] init];
        NSArray *body = [[req stringByRemovingPercentEncoding] componentsSeparatedByString:@"://?body="];
        if([MFMessageComposeViewController canSendText])
        {
            controller.body = [body objectAtIndex:1];
            controller.messageComposeDelegate = self;
            [self.window.rootViewController presentViewController:controller animated:YES completion:nil];
        }
        return NO;
    }
    
    //클립보드 복사
    NSRange strClip = [req rangeOfString:@"clip:"];
    if (strClip.location != NSNotFound) {
        NSArray *body = [[req stringByRemovingPercentEncoding] componentsSeparatedByString:@"://?body="];
        [[UIPasteboard generalPasteboard] setString:[body objectAtIndex:1]];
        
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"주소가 복사되었습니다." message:nil delegate:nil cancelButtonTitle:@"확인" otherButtonTitles: nil];
        [alert show];
        
        return NO;
    }
    
    NSRange actionLogin = [req rangeOfString:@"Login.do"];
    if(actionLogin.location != NSNotFound)
    {
        NSRange rangeToken = [req rangeOfString:@"token"];
        if(rangeToken.location == NSNotFound)
        {
            NSString *deviceToken = [[NSUserDefaults standardUserDefaults] objectForKey:@"deviceToken"];
            req = [NSString stringWithFormat:@"%@&token=%@",req, deviceToken];
            
            [webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:req]]];
            return NO;
        }
        
        
        NSHTTPCookie *cookie;
        NSArray* arrCookies = [[NSHTTPCookieStorage sharedHTTPCookieStorage] cookies];
        
        //Login 쿠키값을 체크한다.
        for (cookie in arrCookies)
        {
            if ([[cookie name] isEqualToString:@"keep_id"]) {
                [[NSUserDefaults standardUserDefaults] setValue:[cookie value] forKey:@"LoginID"];
                [[NSUserDefaults standardUserDefaults] setValue:[cookie value] forKey:@"FirstAuthID"];
                [[NSUserDefaults standardUserDefaults] synchronize];
            }
        }
    }
    
    
    
    return YES;
}

- (void)webViewDidFinishLoad:(UIWebView *)webView {
    if (introView != nil) {
        [introView removeFromSuperview];
        [activityView removeFromSuperview];
        introView = nil;
        activityView = nil;
    }
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
    
    
    
}

//버전체크 2016.01.20 추가
- (void)versionCheck
{
    NSDictionary *dic = [[NSDictionary alloc] initWithObjectsAndKeys:[[NSArray alloc] initWithObjects:@"MUST_YN", @"LASTEST_VERSION",@"TITLE", @"BTN_TYPE",@"LINK", @"CONTENT", nil], @"LastestAppInfo", nil];
    
    XmlParser *xmlParser = [[XmlParser alloc]initParser:[NSURL URLWithString:VersionCheck] andWithItems:@[@"root",@[@"LastestAppInfo"],@"item",dic] andWithParserType:(ParserType *)Parser4];
    
    if (xmlParser.parsedItems && [xmlParser.parsedItems count] > 0)
    {
        NSDictionary *dic = [[xmlParser.parsedItems objectAtIndex:0] objectAtIndex:0];
        NSLog(@"xmlParser.parsedItems %@",dic);
        NSString *MUST_YN = [dic objectForKey:@"MUST_YN"];
        NSString *LASTEST_VERSION = [[dic objectForKey:@"LASTEST_VERSION"] stringByReplacingOccurrencesOfString:@"." withString:@""];
        NSString *TITLE = [dic objectForKey:@"TITLE"];
        NSString *BTN_TYPE = [dic objectForKey:@"BTN_TYPE"];
        NSString *LINK = [dic objectForKey:@"LINK"];
        NSString *CONTENT = [dic objectForKey:@"CONTENT"];
        
        if ([MUST_YN compare:@"Y" options:NSCaseInsensitiveSearch] == NSOrderedSame) {
            NSString *deviceVersion = [[[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleShortVersionString"] stringByReplacingOccurrencesOfString:@"." withString:@""];
            
            if ([deviceVersion integerValue] < [LASTEST_VERSION integerValue]) {
            
                UIAlertView *alert;
                if ([BTN_TYPE compare:@"C" options:NSCaseInsensitiveSearch] == NSOrderedSame) {
                    alert = [[UIAlertView alloc] initWithTitle:TITLE
                                                       message:CONTENT
                                                      delegate:self
                                             cancelButtonTitle:@"취소"
                                             otherButtonTitles:@"확인", nil];
                } else {
                    alert = [[UIAlertView alloc] initWithTitle:TITLE
                                                       message:CONTENT
                                                      delegate:self
                                             cancelButtonTitle:nil
                                             otherButtonTitles:@"확인",nil];
                }
                
                alertUrl = LINK;
                [alert setTag:101];
                [alert show];
            }
        }
        
    }

    
}

- (void)setIntro
{
    introView = [[UIImageView alloc]initWithFrame:self.window.bounds];
    
    NSString *imgName;
    if (IS_IPHONE_4_OR_LESS) {
        imgName = @"LaunchImage-700";
    } else if(IS_IPHONE_5) {
        imgName = @"LaunchImage-700-568h";
    } else if(IS_IPHONE_6) {
        imgName = @"LaunchImage-800-667h";
    } else if(IS_IPHONE_6P) {
        imgName = @"LaunchImage-800-Portrait-736h";
    }
    
    introView.image = [UIImage imageNamed:imgName];
    introView.contentMode = UIViewContentModeScaleToFill;
    [self.window.rootViewController.view addSubview:introView];
//    activityView = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
//    activityView.center = self.window.center;
//    [activityView setColor:UIColorFromRGB(0XFFFFFF)];
//    [self.window.rootViewController.view addSubview:activityView];
//    [activityView setHidden:NO];
//    [activityView startAnimating];
    
    //인트로 종료시간 기본 10초
    [self performSelector:@selector(webViewDidFinishLoad:) withObject:nil afterDelay:10.0f];
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];
    dispatch_async(dispatch_get_main_queue(), ^{
        XmlParser *xmlParser = [[XmlParser alloc] initParser:[NSURL URLWithString:[NSString stringWithFormat:@"%@?act=appIntro",LibURL]] andWithItems:@[@"root",@"appIntro",@"use_yn",@"link",@"dtime"] andWithParserType:(ParserType *)Parser5];
        if (xmlParser.parsedItems && [xmlParser.parsedItems count] > 0)
        {
            if ([[[xmlParser.parsedItems objectAtIndex:0] objectForKey:@"use_yn"] compare:@"Y" options:NSCaseInsensitiveSearch] == NSOrderedSame) {
                NSString *dtime = [[xmlParser.parsedItems objectAtIndex:0] objectForKey:@"dtime"];
                NSString *link = [[xmlParser.parsedItems objectAtIndex:0] objectForKey:@"link"];
                NSString *fileName = [[link componentsSeparatedByString:@"/"] lastObject];
                NSString * tempFilePath = [NSTemporaryDirectory() stringByAppendingPathComponent:fileName];
                NSUserDefaults *UD = [NSUserDefaults standardUserDefaults];
                if (![[UD stringForKey:@"link"] isEqualToString:link]) {
                    [self clearTmpDirectory];
                    [UD setObject:link forKey:@"link"];
                    NSFileManager * fileManager = [NSFileManager defaultManager];
                    if (![fileManager fileExistsAtPath:tempFilePath]) {
                        NSData * imageData = [NSData dataWithContentsOfURL:[NSURL URLWithString:link]];
                        [imageData writeToFile:tempFilePath atomically:NO];
                    }
                }
                if (introView != nil) {
                    introView.image = [UIImage imageWithData:[NSData dataWithContentsOfFile:tempFilePath]];
                }
                [self performSelector:@selector(webViewDidFinishLoad:) withObject:nil afterDelay:[dtime floatValue]];
            }  else {
                [self webViewDidFinishLoad:nil];
            }
        }

    });
}

- (void)clearTmpDirectory
{
    NSArray* tmpDirectory = [[NSFileManager defaultManager] contentsOfDirectoryAtPath:NSTemporaryDirectory() error:NULL];
    for (NSString *file in tmpDirectory) {
        [[NSFileManager defaultManager] removeItemAtPath:[NSString stringWithFormat:@"%@%@", NSTemporaryDirectory(), file] error:NULL];
    }
}

//버전에따라 사파리 새창과 사파리 뷰 컨트롤러 새창으로 열기
- (void)openUrl:(NSURL *)url
{
    if ([SFSafariViewController class]) {
        SFSafariViewController *sf = [[SFSafariViewController alloc]initWithURL:url];
        [self.window.rootViewController presentViewController:sf animated:YES completion:nil];
        [self contract];
    } else {
        [[UIApplication sharedApplication] openURL:url];
    }
}

#pragma mark - UUID Check in Keychanin
- (NSString*) getUUID
{
    // initialize keychaing item for saving UUID.
    KeychainItemWrapper *wrapper = [[KeychainItemWrapper alloc] initWithIdentifier:@"UUID" accessGroup:nil];
    
    NSString *uuid = [wrapper objectForKey:(__bridge id)(kSecAttrAccount)];
    
    if( uuid == nil || uuid.length == 0)
    {
        // if there is not UUID in keychain, make UUID and save it.
        CFUUIDRef uuidRef = CFUUIDCreate(NULL);
        CFStringRef uuidStringRef = CFUUIDCreateString(NULL, uuidRef);
        CFRelease(uuidRef);
        uuid = [NSString stringWithString:(__bridge NSString *) uuidStringRef];
        CFRelease(uuidStringRef);
        
        // save UUID in keychain
        [wrapper setObject:uuid forKey:(__bridge id)(kSecAttrAccount)];
    }
    
    return uuid;
}

#pragma mark - MFMessageComposeViewControllerDelegate Method

- (void)messageComposeViewController:(MFMessageComposeViewController *)controller didFinishWithResult:(MessageComposeResult)result
{
    switch (result) {
        case MessageComposeResultCancelled:
            NSLog(@"Cacelled");
            break;
        case MessageComposeResultFailed:
            NSLog(@"failed");
        case MessageComposeResultSent:
            break;
        default:
            break;
    }
    
    [self.window.rootViewController dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark - 탭바 관련
- (void)tabBarController:(UITabBarController *)tabBarController didSelectViewController:(UIViewController *)viewController
{
    if (selectTab == tabBarController.selectedIndex) {
        UIWebView *webView = [self getSelectWebView];
        switch (selectTab) {
            case 0:
                [webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:MainURL_1]]];
                break;
            case 1:
                [webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:MainURL_2]]];
                break;
            case 2:
                [webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:MainURL_3]]];
                break;
            case 3:
                [webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:MainURL_4]]];
                break;
            case 4:
                [webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:MainURL_5]]];
                break;
                
            default:
                break;
        }
    }
    selectTab = tabBarController.selectedIndex;
}

- (UIWebView *)getSelectWebView {
    UITabBarController *tabBarController = (UITabBarController *)self.window.rootViewController;
    UIWebView *webView;
    if (tabBarController.selectedIndex == 0) {
        webView = ((HomeViewController *)[[tabBarController viewControllers] objectAtIndex:tabBarController.selectedIndex]).webView;
    } else if (tabBarController.selectedIndex == 1) {
        webView = ((SearchViewController *)[[tabBarController viewControllers] objectAtIndex:tabBarController.selectedIndex]).webView;
    } else if (tabBarController.selectedIndex == 2) {
        webView = ((CartViewController *)[[tabBarController viewControllers] objectAtIndex:tabBarController.selectedIndex]).webView;
    } else if (tabBarController.selectedIndex == 3) {
        webView = ((OrderListViewController *)[[tabBarController viewControllers] objectAtIndex:tabBarController.selectedIndex]).webView;
    } else if (tabBarController.selectedIndex == 4) {
        UINavigationController *navigationController = (UINavigationController *)tabBarController.selectedViewController;
        webView = ((MyViewController *)[[navigationController viewControllers] lastObject]).webView;
    }
    return webView;
}

- (UIViewController *)getSelectViewController {
    UITabBarController *tabBarController = (UITabBarController *)self.window.rootViewController;
    if (tabBarController.selectedIndex == 0) {
        return [[tabBarController viewControllers] objectAtIndex:tabBarController.selectedIndex];
    } else if (tabBarController.selectedIndex == 1) {
        return [[tabBarController viewControllers] objectAtIndex:tabBarController.selectedIndex];
    } else if (tabBarController.selectedIndex == 2) {
        return [[tabBarController viewControllers] objectAtIndex:tabBarController.selectedIndex];
    } else if (tabBarController.selectedIndex == 3) {
        return [[tabBarController viewControllers] objectAtIndex:tabBarController.selectedIndex];
    } else if (tabBarController.selectedIndex == 4) {
        UINavigationController *navigationController = (UINavigationController *)tabBarController.selectedViewController;
        return [[navigationController viewControllers] lastObject];
    }
    return nil;
}

#pragma mark - 웹뷰 스크롤 관련
#define TABBAR_HEIGHT (49)
- (void)setTabBarHidden:(BOOL)hidden
{
    UITabBarController *tabBarController = (UITabBarController *)self.window.rootViewController;
    UITabBar *tabBar = tabBarController.tabBar;
    UIViewController *selectTabController = [self getSelectViewController];
    if(hidden)
    {
            
            [UIView animateWithDuration:0.4 animations:^{
                 tabBar.frame = CGRectMake(tabBarController.view.bounds.origin.x,
                                                tabBarController.view.bounds.size.height,
                                                tabBarController.view.bounds.size.width,
                                                TABBAR_HEIGHT);
                

                
                CGRect frame = selectTabController.view.frame;
                frame.size.height = frame.size.height + TABBAR_HEIGHT;
                selectTabController.view.frame = frame;
//                CGRect frame = tabBarController.view.frame;
//                frame.size.height = frame.size.height + TABBAR_HEIGHT;
//                tabBarController.view.frame = frame;
                
             }
             completion:^(BOOL finished) {
                CGRect frame = tabBarController.view.frame;
                frame.size.height = frame.size.height + TABBAR_HEIGHT;
                tabBarController.view.frame = frame;
             }];
    }
    else
    {
        CGRect frame = tabBarController.view.frame;
        frame.size.height = frame.size.height - TABBAR_HEIGHT;
        tabBarController.view.frame = frame;
            [UIView animateWithDuration:0.4 animations:^{
                tabBar.frame = CGRectMake(tabBarController.view.bounds.origin.x,
                                          tabBarController.view.bounds.size.height - TABBAR_HEIGHT,
                                          tabBarController.view.bounds.size.width,
                                          TABBAR_HEIGHT);
                CGRect frame = selectTabController.view.frame;
                frame.size.height -= TABBAR_HEIGHT;
                selectTabController.view.frame = frame;
                
             }   completion:^(BOOL finished) {

             }];

    }
}


-(void)expand
{
    if(barHidden)
        return;
    
    barHidden = YES;
    
    [self setTabBarHidden:YES];

}

-(void)contract
{
    if(!barHidden)
        return;
    
    barHidden = NO;
    
    [self setTabBarHidden:NO];
}

- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView
{
    startContentOffset = lastContentOffset = scrollView.contentOffset.y;
    //NSLog(@"scrollViewWillBeginDragging: %f", scrollView.contentOffset.y);
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    CGFloat currentOffset = scrollView.contentOffset.y;
    CGFloat differenceFromStart = startContentOffset - currentOffset;
    CGFloat differenceFromLast = lastContentOffset - currentOffset;
    lastContentOffset = currentOffset;
    
    if((differenceFromStart) < 0)
    {
        // scroll up
//        if(scrollView.isTracking && (fabs(differenceFromLast)>1))
//            [self expand];
    }
    else {
//        if(scrollView.isTracking && (fabs(differenceFromLast)>1))
//            [self contract];
    }
    
}

- (BOOL)scrollViewShouldScrollToTop:(UIScrollView *)scrollView
{
    [self contract];
    return YES;
}
@end
