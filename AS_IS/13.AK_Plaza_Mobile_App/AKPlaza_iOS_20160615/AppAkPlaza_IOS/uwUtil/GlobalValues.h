//
//  GlobalValues.h
//  AppAkMall_iOS
//
//  Created by uniwis on 11. 6. 27..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "UWTabBar.h"
#import "JSON.h"

@interface GlobalValues : NSObject {
    UWTabBar *tabBar;
    int majorIOSVersion;
    NSString* returnUrl;//p65458 20130711 return url add
    NSString* actUrl;//p65458 20130808 영수증 이전페이지 변경 문제 add
}

+(GlobalValues *)sharedSingleton;
+ (NSInteger)getNotice;
@property (nonatomic, retain) UWTabBar *tabBar;
@property (nonatomic) int majorIOSVersion;

@property (nonatomic, retain) NSString* returnUrl;//p65458 20130711 return url add
@property (nonatomic, retain) NSString* actUrl;//p65458 20130808 영수증 이전페이지 변경 문제 add
@property (nonatomic, retain) NSString* pushUrl;//p65458 20130808 푸쉬 페이지 이동 문제 add
@end
