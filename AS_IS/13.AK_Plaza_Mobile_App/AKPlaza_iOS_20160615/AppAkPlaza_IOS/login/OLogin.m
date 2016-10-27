//
//  OLogin.m
//  AppAkMall_iOS
//
//  Created by uniwis on 11. 9. 27..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "OLogin.h"


@implementation OLogin
@synthesize isLogin, isAutoLogin, isIDSave;
@synthesize userID, userPW, sessionKey;


-(void)dealloc
{
    [userID release];
    [userPW release];
    [sessionKey release];
    
    [super dealloc];
    
}

@end
