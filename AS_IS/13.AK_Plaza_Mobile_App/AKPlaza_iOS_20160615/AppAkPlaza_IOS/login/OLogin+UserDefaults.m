//
//  OLogin+UserDefaults.m
//  AppAkMall_iOS
//
//  Created by uniwis on 11. 9. 27..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "OLogin.h"
#import "OLogin+UserDefaults.h"


@implementation OLogin (OLogin_UserDefaults) 




+ (OLogin *) loadOLoginContextFromUserDefaults
{
    
    OLogin *oLogin = nil;
    
    oLogin = [[OLogin alloc] init];

    oLogin.isAutoLogin = [[NSUserDefaults standardUserDefaults] boolForKey:@"isAutoLogin"];
    oLogin.isLogin = [[NSUserDefaults standardUserDefaults]     boolForKey:@"isLogin"];
    oLogin.userID = [[NSUserDefaults standardUserDefaults]      objectForKey:@"LoginID"];
    oLogin.userPW = [[NSUserDefaults standardUserDefaults]      objectForKey:@"LoginPassword"];
    oLogin.sessionKey = [[NSUserDefaults standardUserDefaults]  objectForKey:@"sessionKey"];
    
    
    return [oLogin autorelease];
    
}

+ (void) saveOLoginContextToUserDefaults
{

}


+ (void) saveOLoginContextToUserDefaults:(OLogin *)oLoginContext
{

    [[NSUserDefaults standardUserDefaults] setBool:oLoginContext.isAutoLogin    forKey: @"isAutoLogin"];
    [[NSUserDefaults standardUserDefaults] setBool:oLoginContext.isLogin        forKey: @"isLogin"];
    [[NSUserDefaults standardUserDefaults] setValue:oLoginContext.userID        forKey: @"LoginID"];
    [[NSUserDefaults standardUserDefaults] setValue:oLoginContext.userPW        forKey: @"LoginPassword"];
    [[NSUserDefaults standardUserDefaults] setValue:oLoginContext.sessionKey    forKey: @"sessionKey"];
    
    
    oLoginContext = nil;
}

+ (void) removeOLoginContextToAllUserDefault
{

    /*
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"isAutoLogin"];
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"LoginID"];
    //비밀번호 md5 보완 처리함.
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"LoginPassword"];
    */
    
    [[NSUserDefaults standardUserDefaults] setBool:NO    forKey: @"isAutoLogin"];
    [[NSUserDefaults standardUserDefaults] setValue:@""        forKey: @"LoginID"];
    [[NSUserDefaults standardUserDefaults] setValue:@""        forKey: @"LoginPassword"];
    
    [self removeOLoginContextToSessionValue];
}

+(void)removeOLoginContextToSessionValue
{
    /*

    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"sessionKey"];
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"isLogin"];
    
     */
    
    
    [[NSUserDefaults standardUserDefaults] setBool:NO        forKey: @"isLogin"];
    [[NSUserDefaults standardUserDefaults] setValue:@""    forKey: @"sessionKey"];

}


@end
