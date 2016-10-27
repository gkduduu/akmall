//
//  AkNotiModel.h
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 28..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "baseModel.h"
#import "UWXmlParser.h"
#import "ASIFormDataRequest.h"
#import "JSON.h"
#import "AlertMassageUtil.h"

@interface AkNotiModel : baseModel



- (UIWebView *)notiContentWebview:(NSString *)notiContentURL;
- (NSMutableArray *)notiList;
- (void)notiDeviceRegist:(NSString*)token ;
- (void)notiSetUpdate:(NSString*)deny;
- (void)notiSetState;
- (void)notiSettingIfLogin;
- (void)signinDevice;


@end
