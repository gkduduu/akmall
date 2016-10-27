//
//  OLogin+UserDefaults.h
//  AppAkMall_iOS
//
//  Created by uniwis on 11. 9. 27..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

@class OLogin;
@interface OLogin (OLogin_UserDefaults) 

+ (OLogin *) loadOLoginContextFromUserDefaults;


+ (void) saveOLoginContextToUserDefaults;

+ (void) saveOLoginContextToUserDefaults:(OLogin *)oLoginContext;

+ (void)removeOLoginContextToSessionValue;
+ (void) removeOLoginContextToAllUserDefault;




@end
