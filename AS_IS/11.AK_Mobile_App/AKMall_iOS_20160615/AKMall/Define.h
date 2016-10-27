//
//  Define.h
//  test
//
//  Created by 한병일 on 2014. 6. 9..
//  Copyright (c) 2014년 한병일. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Define : NSObject

//실서버
#define RootURL         @"http://m.akmall.com"
#define MembersRootURL  @"http://m.akmembers.com"
//개발
//#define RootURL         @"http://devm.akmall.com"
//#define RootURL         @"http://91.3.115.209" //민석
//#define RootURL         @"http://91.3.115.184:8082"
#define MainURL_1       RootURL @"/main/Main.do?isAkApp=iPhone"
#define MainURL_2       RootURL @"/search/SearchMain.do?isAkApp=iPhone"
#define MainURL_3       RootURL @"/order/ShoppingCart.do?isAkApp=iPhone"
#define MainURL_4       RootURL @"/mypage/OrderDeliInquiry.do?isAkApp=iPhone"
#define MainURL_5       RootURL @"/mypage/MyPlaceMain.do?isAkApp=iPhone"

#define MainURL_6       RootURL @"/mypage/UserSetupMain.do?isAkApp=iPhone"
#define MainURL_7       RootURL @"/mypage/RecentChoicGoods.do?isAkApp=iPhone"

#define MembersLogin    RootURL @"/login/Login.do?isAkApp=iPhone"
#define MembersLogout   RootURL @"/login/Logout.do?isAkApp=iPhone"

#define PushViewURL    RootURL @"/app/lib.do?isAkApp=iPhone&act=viewPushDetail&push_id="


#define LibURL          RootURL @"/app/lib.do"
#define VersionCheck    RootURL @"/app/lib.do?act=versionInfo&phonetype=0"
//#define AddressURL      RootURL @"/app/address.do"

#define UIColorFromRGB(rgbValue) [UIColor colorWithRed:((float)((rgbValue & 0xFF0000) >> 16))/255.0 green:((float)((rgbValue & 0xFF00) >> 8))/255.0 blue:((float)(rgbValue & 0xFF))/255.0 alpha:1.0]


#define IS_IPAD (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad)
#define IS_IPHONE (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
#define IS_RETINA ([[UIScreen mainScreen] scale] >= 2.0)

#define SCREEN_WIDTH ([[UIScreen mainScreen] bounds].size.width)
#define SCREEN_HEIGHT ([[UIScreen mainScreen] bounds].size.height)
#define SCREEN_MAX_LENGTH (MAX(SCREEN_WIDTH, SCREEN_HEIGHT))
#define SCREEN_MIN_LENGTH (MIN(SCREEN_WIDTH, SCREEN_HEIGHT))

#define IS_IPHONE_4_OR_LESS (IS_IPHONE && SCREEN_MAX_LENGTH < 568.0)
#define IS_IPHONE_5 (IS_IPHONE && SCREEN_MAX_LENGTH == 568.0)
#define IS_IPHONE_6 (IS_IPHONE && SCREEN_MAX_LENGTH == 667.0)
#define IS_IPHONE_6P (IS_IPHONE && SCREEN_MAX_LENGTH == 736.0)
@end
