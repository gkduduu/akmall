//
//  OLogin.h
//  AppAkMall_iOS
//
//  Created by uniwis on 11. 9. 27..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface OLogin : NSObject {
    
    BOOL isAutoLogin;
    BOOL isIDSave;
    BOOL isLogin;
    
    NSString *userID;
    NSString *userPW;
    NSString *sessionKey;
    
}



@property (nonatomic) BOOL isAutoLogin;
@property (nonatomic) BOOL isIDSave;
@property (nonatomic) BOOL isLogin;

@property (nonatomic, retain) NSString *userID;
@property (nonatomic, retain) NSString *userPW;
@property (nonatomic, retain) NSString *sessionKey;


@end
