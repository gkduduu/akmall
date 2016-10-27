//
//  UWCustomTabBar.m
//  UWCustomView
//
//  Created by 미영 신 on 11. 6. 4..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "UWTabBarView.h"
#import "UWTabBarDefine.h"
#import "UWTabBar.h"

#import "UWTabBarItem.h"
#import "GlobalValues.h"

#import "AkMainView.h"
#import "AkBrandSearchView.h"
#import "AkGiftInfoView.h"
#import "AkMembersCardDetailView.h"

#import "CommonMsg.h"





@implementation UWTabBarView
@synthesize tabView;



- (void)dealloc {

    tabView = nil;
    tabViewController1 = nil;
    [super dealloc];
}



- (void)hideTabBar {

    //tabView.isTabBarHidden = NO;
	//tabView.tabBarHolder.hidden = YES;
}

- (void)showTabBar {
    //tabView.isTabBarHidden = YES;
	//tabView.tabBarHolder.hidden = NO;
}

- (void)historyBack {
    tabView.currentIndex = 0;
    if(!tabView.flag){
        if([tabViewController1.navigationController.viewControllers count] > 1) {
            [tabViewController1.navigationController popToRootViewControllerAnimated:YES];
        } else if ([tabViewController1.webView canGoBack]) {
            [tabViewController1.webView goBack];
            
        }
    }
}

- (void)brandSearch {
    tabView.currentIndex = 0;
    if(!tabView.flag){
        NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"%@?act=searchBrand", kAddressURL]];
        NSURLRequest *reqURL = [NSURLRequest requestWithURL:url];
        [tabViewController1.webView loadRequest:reqURL];
        if([tabViewController1.navigationController.viewControllers count] > 1) {
            [tabViewController1.navigationController popToRootViewControllerAnimated:YES];
        }
    }
}

- (void)MYAK {
    tabView.currentIndex = 0;
    if(!tabView.flag){
        NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"%@?act=myak", kAddressURL]];
        NSURLRequest *reqURL = [NSURLRequest requestWithURL:url];
        [tabViewController1.webView loadRequest:reqURL];
        if([tabViewController1.navigationController.viewControllers count] > 1) {
            [tabViewController1.navigationController popToRootViewControllerAnimated:YES];
        }
    }
}

- (void)appAlimList{
    if(tabView.currentIndex == 0){
        tabView.flag = true;

        tabView.currentIndex = 4;
        AkNotiListBoxViewController *subView = [[[AkNotiListBoxViewController alloc] init]autorelease];
        
        [tabViewController1.navigationController pushViewController:subView animated:YES];
    }
}

- (void)clickAKMall:(id)sender
{
    

    
    MSG_DELEGATE_BTN2(nil, @"취소", @"확인", kAkmallMsg, self);
    
    /*
    ASIHTTPRequest *request = [ASIHTTPRequest requestWithURL:[NSURL URLWithString: strURL]];
    
    [request setDelegate:self];
    
    [request start];
    */
    
}


-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    
    if ([[alertView message] isEqualToString:kAkmallMsg]) 
    {
        
        if (buttonIndex == 1) {
            // openURL 로 링크 실행
            NSString *strURL = [NSString stringWithFormat:@"%@?act=akmall", kAddressURL];
            
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:strURL]]; 
        }
    }
}


-(id)init
{
    //메뉴 좌표 처리
    tabView.flag = false;
    NSString *path = [[NSBundle mainBundle] bundlePath];
    NSString *finalPath = [path stringByAppendingPathComponent:kMenuRectMakeInfo];
    NSDictionary *outlineData = [[NSDictionary dictionaryWithContentsOfFile:finalPath] retain];
    
    NSArray * arrMenu1 = [outlineData objectForKey:kMenuFirstRectMakeForKey];
    NSArray * arrMenu2 = [outlineData objectForKey:kMenuSecondRectMakeForKey];
    NSArray * arrMenu3 = [outlineData objectForKey:kMenuThirdRectMakeForKey];
    NSArray * arrMenu4 = [outlineData objectForKey:kMenuForthRectMakeForKey];
    NSArray * arrMenu5 = [outlineData objectForKey:kMenuFifthRectMakeForKey];


    UWTabBarItem *tabItem1 = [[UWTabBarItem alloc]  initWithFrame:CGRectMake([[arrMenu1 objectAtIndex:0] floatValue], [[arrMenu1 objectAtIndex:1] floatValue], [[arrMenu1 objectAtIndex:2] floatValue], [[arrMenu1 objectAtIndex:3] floatValue])  ImgNormalState:kMenuFirstImgNormalState ImgToggledState:kMenuFirstImgToggledState andAccessibilityLabel:@"에이케이플라자 홈"];
    [tabItem1 addTarget:self action:@selector(historyBack) forControlEvents:UIControlEventTouchUpInside];
    tabItem1.tag = 0;
    
	UWTabBarItem *tabItem2 = [[UWTabBarItem alloc] initWithFrame:CGRectMake([[arrMenu2 objectAtIndex:0] floatValue], [[arrMenu2 objectAtIndex:1] floatValue], [[arrMenu2 objectAtIndex:2] floatValue], [[arrMenu2 objectAtIndex:3] floatValue]) ImgNormalState:kMenuSecondImgNormalState ImgToggledState:kMenuSecondImgToggledState andAccessibilityLabel:@"마이에이케이"];
    [tabItem2 addTarget:self action:@selector(MYAK) forControlEvents:UIControlEventTouchUpInside];
    
    tabItem2.tag = 1;
    
	UWTabBarItem *tabItem3 = [[UWTabBarItem alloc] initWithFrame:CGRectMake([[arrMenu3 objectAtIndex:0] floatValue], [[arrMenu3 objectAtIndex:1] floatValue], [[arrMenu3 objectAtIndex:2] floatValue], [[arrMenu3 objectAtIndex:3] floatValue]) ImgNormalState:kMenuThirdImgNormalState ImgToggledState:kMenuThirdImgToggledState  andAccessibilityLabel:@"인터넷백화점"];
	
    [tabItem3 addTarget:self action:@selector(brandSearch) forControlEvents:UIControlEventTouchUpInside];
    
    tabItem3.tag = 2;
    
    
    UWTabBarItem *tabItem4 = [[UWTabBarItem alloc] initWithFrame:CGRectMake([[arrMenu4 objectAtIndex:0] floatValue], [[arrMenu4 objectAtIndex:1] floatValue], [[arrMenu4 objectAtIndex:2] floatValue], [[arrMenu4 objectAtIndex:3] floatValue]) ImgNormalState:kMenuForthImgNormalState ImgToggledState:kMenuForthImgToggledState andAccessibilityLabel:@"브랜드검색"];
    [tabItem4 addTarget:self action:@selector(appAlimList) forControlEvents:UIControlEventTouchUpInside];
    tabItem4.tag = 3;
    
	UWTabBarItem *tabItem5 = [[UWTabBarItem alloc] initWithFrame:CGRectMake([[arrMenu5 objectAtIndex:0] floatValue], [[arrMenu5 objectAtIndex:1] floatValue], [[arrMenu5 objectAtIndex:2] floatValue], [[arrMenu5 objectAtIndex:3] floatValue]) ImgNormalState:kMenuFifthImgNormalState ImgToggledState:kMenuFifthImgToggledState andAccessibilityLabel:@"상품권안내"];
    [tabItem5 addTarget:self action:@selector(clickAKMall:) forControlEvents:UIControlEventTouchUpInside];
    tabItem5.tag = 4;
    
    tabViewController1 = [[AkMainView alloc] init];
    //인터넷백화점
    AkMembersCardDetailView *tabViewController2 = [[AkMembersCardDetailView alloc] init];
	AkBrandSearchView *tabViewController4 = [[AkBrandSearchView alloc] init];
	AkGiftInfoView *tabViewController5 = [[AkGiftInfoView alloc] init];

    
    //네비게이션 바에다가 뷰를 넣기
	UINavigationController *naviViewAKMall001 = [[UINavigationController alloc] initWithRootViewController:tabViewController1];
    
	naviViewAKMall001.delegate = self;
    naviViewAKMall001.navigationBarHidden = YES;
    
    
    //네비게이션 바에다가 뷰를 넣기
    
	UINavigationController *naviViewAKMall002 = [[UINavigationController alloc] initWithRootViewController:tabViewController2];
    
	naviViewAKMall002.delegate = self;
    naviViewAKMall002.navigationBarHidden = YES;

	
	//네비게이션에다가  뷰 넣기 
    /*
	UINavigationController *naviViewAKMall003 = [[UINavigationController alloc] initWithRootViewController:tabViewController2];
	
	naviViewAKMall003.delegate = self;
    naviViewAKMall003.navigationBarHidden = YES;
	*/
   
    
	//네비게이션에다가  뷰 넣기 
	UINavigationController *naviViewAKMall004 = [[UINavigationController alloc] initWithRootViewController:tabViewController4];
	
	naviViewAKMall004.delegate = self;
    naviViewAKMall004.navigationBarHidden = YES;
	
	//네비게이션에다가  뷰 넣기 
	UINavigationController *naviViewAKMall005 = [[UINavigationController alloc] initWithRootViewController:tabViewController5];
	
	naviViewAKMall005.delegate = self;
    naviViewAKMall005.navigationBarHidden = YES;
    
    
    
	NSMutableArray *viewControllersArray = [[NSMutableArray alloc] init];
	[viewControllersArray addObject:naviViewAKMall001];
	[viewControllersArray addObject:[NSNull null]];
	[viewControllersArray addObject:[NSNull null]];
	[viewControllersArray addObject:[NSNull null]];
	[viewControllersArray addObject:[NSNull null]];
	
	NSMutableArray *tabItemsArray = [[NSMutableArray alloc] init];
	[tabItemsArray addObject:tabItem1];
	[tabItemsArray addObject:tabItem2];
	[tabItemsArray addObject:tabItem3];
	[tabItemsArray addObject:tabItem4];
	[tabItemsArray addObject:tabItem5];

    tabView = [[UWTabBar alloc] initWithTabViewControllers:[viewControllersArray copy] tabItems:[tabItemsArray copy] initialTab:0];
    
    tabView.delegate = self;
    tabView.isTabBarHidden = NO;
    
    [[GlobalValues sharedSingleton] setTabBar:tabView];
    [[GlobalValues sharedSingleton] setMajorIOSVersion:[[[[[UIDevice currentDevice] systemVersion] componentsSeparatedByString:@"."] objectAtIndex:0] intValue]];
  
//    [outlineData release];
//
//    outlineData = nil;
//    
//    [tabItem1 release];
//    tabItem1 = nil;
//    [tabItem2 release];
//    tabItem2 = nil;
//    [tabItem3 release];
//    tabItem3 = nil;
//    [tabItem4 release];
//    tabItem4 = nil;
//    [tabItem5 release];
//    tabItem5 = nil;
//    
//    [viewControllersArray release];
//    viewControllersArray = nil;
//    [tabItemsArray release];
//    viewControllersArray = nil;
    
    //[tabView autorelease]하면 탭 선택시 -(void)buttonPressed:(id)target에 instance null 에러남
    
    return (UWTabBarView*)tabView;
}
@end

